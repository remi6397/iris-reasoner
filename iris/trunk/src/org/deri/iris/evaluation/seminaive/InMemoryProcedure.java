/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.evaluation.seminaive;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;

import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.evaluation.seminaive.model.IRule;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.evaluation.seminaive.model.*;
import org.deri.iris.api.evaluation.seminaive.IEvaluationProcedure;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.operations.relations.*;
import org.deri.iris.api.IEDB;
import org.deri.iris.factory.Factory;


/**
 * @author Paco Garcia, University of Murcia
 * @date 08-sep-2006
 */
public class InMemoryProcedure implements IEvaluationProcedure{

	/**
	 * 
	 * @param pi Rule to evaluate
	 * @param EDB Extensional Database tuples
	 * @param Q Tuples already discovered
	 * @return new tuples discovered for the rule evaluated
	 */
	public IRelation<ITuple> eval(ITree pi, IEDB EDB, Map<ITree, IRelation<ITuple>> IDB) {
		return evaluate(pi, EDB, IDB);
	}
	 
	 
	/**
	 * 
	 * @param pi Rule to evaluate
	 * @param EDB Extensional Database tuples
	 * @param P All the tuples discovered so far
	 * @param AQ Tuples discovered during the last iteration
	 * @return new tuples discovered for the rule evaluated
	 */
	public IRelation<ITuple> eval_incr(ITree pi, IEDB EDB, Map<ITree, IRelation<ITuple>>  P, Map<ITree, IRelation<ITuple>>  AQ) {
		return evaluate(pi, EDB, AQ);
	}

	private IRelation<ITuple> evaluate(ITree node, IEDB EDB, Map<ITree, IRelation<ITuple>> IDB){
		if (node instanceof DifferenceDescription){
			DifferenceDescription d = (DifferenceDescription)node;
			// TODO. We do not use difference operations yet
			return null;
		}else if (node instanceof JoinDescription){
			JoinDescription j = (JoinDescription)node;
			org.deri.iris.api.operations.relation.IJoin join = 
				Factory.RELATION.createJoinOperator(
						evaluate((ITree)j.getChildren().get(0), EDB, IDB), 
						evaluate((ITree)j.getChildren().get(1), EDB, IDB),
						j.getIndexes(), j.getCondition() );
			return join.join();
		}else if (node instanceof NaturalJoinDescription){
			NaturalJoinDescription nj = (NaturalJoinDescription)node;
			List njChildren = nj.getChildren();
			if (njChildren.size() == 1) { // No join needed
				return evaluate((ITree)njChildren.get(0), EDB, IDB);
			}
			IRelation<ITuple> result;
			// Check whether we need cartesian product (no common variables) or join operation
			ITree child1 = (ITree)njChildren.get(0);
			ITree child2 = (ITree)njChildren.get(1);
			List<String> joinVariables = getJoinVariables(child1.getVariables(), child2.getVariables());
			int[] joinIndexes = getJoinIndexes(child1.getVariables(), child2.getVariables());

			if (joinVariables.size() < child1.getVariables().size() + child2.getVariables().size()) {
				// Join
				org.deri.iris.api.operations.relation.IJoin join = 
					Factory.RELATION.createJoinOperator(
						evaluate(child1, EDB, IDB), 
						evaluate(child2, EDB, IDB),
						joinIndexes);
				result = join.join();
			} else {
				// Cartesian product
				result = null;
				
			}
			for (int i=2; i < njChildren.size(); i++) {
				result = doJoin(result, joinVariables, (ITree)njChildren.get(i), EDB, IDB);
			}
			return result;

		}else if (node instanceof ProjectionDescription){
			ProjectionDescription p = (ProjectionDescription)node;
			org.deri.iris.api.operations.relation.IProjection projection =
				Factory.RELATION.createProjectionOperator(
						evaluate((ITree)p.getChildren().get(0), EDB, IDB),
						p.getIndexes());
			return projection.project();
		}else if (node instanceof RuleDescription){
			RuleDescription r = (RuleDescription)node;
			// A leaf can be either an EDB predicate or an IDB predicate
			if (IDB.containsKey(r)) {
				// This leaf is an IDB
				return IDB.get(r);
			} else {
				// This leaf is an EDB
				for (org.deri.iris.api.basics.IPredicate p: EDB.getPredicates()) {
					// Is the predicate in EDB refering to the same as 'r'?
					if (p.getPredicateSymbol().equalsIgnoreCase(r.getName()) && 
							p.getArity() == r.getArity())
						return EDB.getFacts(p);
				}
				return null;
			}			
		}else if (node instanceof SelectionDescription){
			SelectionDescription s = (SelectionDescription)node;
			org.deri.iris.api.operations.relation.ISelection selection =
				Factory.RELATION.createSelectionOperator(
						evaluate((ITree)s.getChildren().get(0), EDB, IDB),
						s.getPattern());
			return selection.select();			
		}else if (node instanceof UnionDescription){
			UnionDescription u = (UnionDescription)node;
			//TODO. Union
			return null;
		} else
			return null;
	}
	
	private IRelation<ITuple> doJoin(
			IRelation<ITuple> child1, 
			List<String> child1Variables, 
			ITree child2, 
			IEDB EDB, 
			Map<ITree, IRelation<ITuple>> IDB) {
		
		List<String> joinVariables = getJoinVariables(child1Variables, child2.getVariables());
		int[] joinIndexes = getJoinIndexes(child1Variables, child2.getVariables());
		
		if (joinVariables.size() < child1Variables.size() + child2.getVariables().size()) {
			// Join
			org.deri.iris.api.operations.relation.IJoin join = 
				Factory.RELATION.createJoinOperator(
					child1, 
					evaluate(child2, EDB, IDB),
					joinIndexes);

			child1Variables = joinVariables; // New set of variables for the next join
			return join.join();
		} else {
			// Cartesian product
			return null;			
		}
		
	}
	
	private List<String> getJoinVariables(List<String> rel1, List<String> rel2) {
		List<String> joinVariables = new LinkedList<String>();
		
		for (String s: rel1) 
			joinVariables.add(s);
		for (String s:rel2)
			if (!joinVariables.contains(s))
				joinVariables.add(s);		
		
		return joinVariables;
		
	}
	private int[] getJoinIndexes(List<String> rel1, List<String> rel2){
		int[] indexes;
		if (rel1.size() > rel2.size()) {
			int arity = rel1.size();
			indexes = new int[ arity ];
			for (int i = 0; i < arity; i++)
				indexes[i] = rel2.indexOf(rel1.get(i));
		} else {
			int arity = rel2.size();
			indexes = new int[ arity ];
			for (int i = 0; i < arity; i++)
				indexes[i] = rel1.indexOf(rel2.get(i));
		}
		return indexes;
		

	}

}