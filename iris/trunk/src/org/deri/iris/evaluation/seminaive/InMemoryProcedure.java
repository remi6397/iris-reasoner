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

import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.evaluation.seminaive.model.IRule;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.evaluation.seminaive.model.*;
import org.deri.iris.api.evaluation.seminaive.IEvaluationProcedure;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.operations.relations.*;
import org.deri.iris.api.IEDB;


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
	public IRelation<ITuple> eval(ITree pi, IEDB EDB, Map<IRule, IRelation<ITuple>> IDB) {
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
	public IRelation<ITuple> eval_incr(ITree pi, IEDB EDB, Map<IRule, IRelation<ITuple>>  P, Map<IRule, IRelation<ITuple>>  AQ) {
		// TODO Set parameters
		// R1,..., Rk & pi = body;
		// Q1,...,Qm; = Q		
		//TODO Get the results 
		//Pbody.addAll(evaluator.evaluate());
		return null;
	}

	private IRelation<ITuple> evaluate(ITree node, IEDB EDB, Map<IRule, IRelation<ITuple>> IDB){
		if (node instanceof DifferenceDescription){
			
		}else if (node instanceof JoinDescription){
			JoinDescription j = (JoinDescription)node;
			org.deri.iris.api.operations.relation.IJoin join = 
				OperationFactory.getInstance().createJoinOperator(
						evaluate((ITree)j.getChildren().get(0), EDB, IDB), 
						evaluate((ITree)j.getChildren().get(1), EDB, IDB),
						j.getIndexes(), j.getCondition() );
			return join.join();
		}else if (node instanceof NaturalJoinDescription){
			NaturalJoinDescription nj = (NaturalJoinDescription)node;
			// TODO. Cartesian product
			
		}else if (node instanceof ProjectionDescription){
			ProjectionDescription p = (ProjectionDescription)node;
			org.deri.iris.api.operations.relation.IProjection projection =
				OperationFactory.getInstance().createProjectionOperator(
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
			}			
		}else if (node instanceof SelectionDescription){
			SelectionDescription s = (SelectionDescription)node;
			org.deri.iris.api.operations.relation.ISelection selection =
				OperationFactory.getInstance().createSelectionOperator(
						evaluate((ITree)s.getChildren().get(0), EDB, IDB),
						s.getPattern());
			return selection.select();			
		}else if (node instanceof UnionDescription){
			UnionDescription u = (UnionDescription)node;
			//TODO. Union
		}
			
		
	}

}