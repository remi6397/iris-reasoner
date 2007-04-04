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
package org.deri.iris.evaluation.algebra;

import static org.deri.iris.factory.Factory.RELATION;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.evaluation.algebra.IComponent;
import org.deri.iris.api.evaluation.algebra.IConstantDescriptor;
import org.deri.iris.api.evaluation.algebra.IDifferenceDescriptor;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.evaluation.algebra.IJoinDescriptor;
import org.deri.iris.api.evaluation.algebra.IProjectionDescriptor;
import org.deri.iris.api.evaluation.algebra.IRelationDescriptor;
import org.deri.iris.api.evaluation.algebra.ISelectionDescriptor;
import org.deri.iris.api.evaluation.algebra.IUnionDescriptor;
import org.deri.iris.api.operations.relation.IDifference;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.operations.relation.IProjection;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.operations.relation.IUnion;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.operations.relations.MiscOps;

/**
 * <p>
 * An evaluator of a relational algebra expression. 
 * This evaluator is used whenever an evaluation of 
 * relational algebra expressions is needed regardless 
 * of a particular evaluation algorithm (e.g. naive 
 * evaluation, semi-naive evaluation etc.).
 * </p>
 * <p>
 * This evaluator takes a program previously transformed to 
 * a set of relation algebra expressions, as an input, 
 * and executes these expressions. 
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date Dec 18, 2006 
 */
public class ExpressionEvaluator implements IExpressionEvaluator {

	public ExpressionEvaluator() {
	}

	public IRelation evaluate(IComponent c, IProgram p) {
		return evaluate(c, p, null);
	}

	public IRelation evaluateIncrementally(IComponent c, IProgram p, 
			Map<IPredicate, IRelation> aq) {
		
		return evaluate(c, p, aq);
	}
	
	private IRelation evaluate(IComponent c, IProgram p,
			Map<IPredicate, IRelation> aq) {
		
		switch(c.getType()){
			case DIFFERENCE:
				return evaluateDifference(c, p, aq);
			case JOIN:
				return evaluateJoin(c, p, aq, null);
			case PROJECTION:
				return evaluateProjection(c, p, aq);
			case SELECTION:
				return evaluateSelection(c, p, aq);
			case UNION:
				return evaluateUnion(c, p, aq);
			case RELATION:
				return evaluateRelation(c, p, aq);
			case CONSTANT:
				return evaluateConstant(c);
		}
		return null;
	}
	
	private IRelation evaluateDifference(IComponent c,IProgram p,
			Map<IPredicate, IRelation> aq){
		
		if (c.getChildren().size() != 2) {
			throw new IllegalArgumentException(
					"Please provide the component with two subcomponents " +
					"(children), otherwise the evaluateDifference cannot be " +
					"performed!");
		}
		IDifferenceDescriptor d = (IDifferenceDescriptor)c;
		IDifference diff = Factory.RELATION_OPERATION
				.createDifferenceOperator(
						evaluate(d.getChildren().get(0), p, aq), 
						evaluate(d.getChildren().get(1), p, aq));
		
		return diff.difference();
	}
	
	private IRelation evaluateJoin(IComponent c,IProgram p,
			Map<IPredicate, IRelation> aq, int[] pInds){
		
		if (c.getChildren().size() < 2) {
			throw new IllegalArgumentException(
					"Please provide the component with at least two " +
					"subcomponents (children), otherwise the join operation " +
					"cannot be performed!");
		}
		IJoinDescriptor j = (IJoinDescriptor)c;
		IJoin jo = null;
		List<IVariable> vars = new ArrayList<IVariable>();
		IComponent c0 = j.getChildren().get(0);
		vars.addAll(c0.getVariables());
		IComponent c1 = null;
		/**
		 * Left relation to be joined contains all tuples from the KB related
		 * to the corresponding predicate, not only "fresh tuples" from 
		 * the last iteration (aq = null)!
		 */
		IRelation r0 = evaluate(c0, p, null);
		boolean emptyRel = (r0.size()==0) ? true : false;
		IRelation r1 = null;
		
		for(int i=1; i<j.getChildren().size(); i++){
			c1 = j.getChildren().get(i);
			if(! emptyRel){
				r1 = evaluate(c1, p, aq);
				if(c1.isPositive()){
					jo = Factory.RELATION_OPERATION.createJoinSimpleOperator(
							r0, r1, 
							// TODO: get correct projection indexes!
							MiscOps.getJoinIndexes(vars, c1.getVariables()), 
							//j.getCondition(), pInds);
							j.getCondition());
				} else {
					jo = Factory.RELATION_OPERATION.createJoinComplementOperator(
							r0, r1, 
							MiscOps.getJoinIndexes(vars, c1.getVariables()));
				}
				r0 = jo.join();
				if(r0.size() == 0) emptyRel = true;
			}
			if(c1 != null && c1.getVariables() != null && c1.isPositive()) 
				vars.addAll(c1.getVariables());
		}
		// TODO: If you don't use variables created in Rule2Relation, remove them!
		j.getVariables().clear();
		j.addVariables(vars);
		
		return r0;
	}
	
	private IRelation evaluateProjection(IComponent c,IProgram p,
			Map<IPredicate, IRelation> aq){
		
		if (c.getChildren().size() != 1) {
			throw new IllegalArgumentException(
					"Please provide the component with only one subcomponent " +
					"(child),otherwise the evaluateProjection cannot be " +
					"performed!");
		}
		IProjectionDescriptor pr = (IProjectionDescriptor)c;
		IProjection projection = Factory.RELATION_OPERATION
			.createProjectionOperator(
					evaluate(pr.getChildren().get(0), p, aq), 
					// TODO: If you don't use project inds, created in Rule2Relation, remove them!
					//pr.getIndexes());
					MiscOps.getProjectionIndexes(
							pr.getChildren().get(0).getVariables(), pr.getVariables()));
		
		return projection.project();
	}
	
	private IRelation evaluateRelation(IComponent c,IProgram p,
			Map<IPredicate, IRelation> aq){
		
		if (c.getChildren().size() != 0) {
			throw new IllegalArgumentException(
					"Please provide the component with no subcomponent " +
					"(no child), otherwise evaluateRelation cannot be " +
					"performed!");
		}
		IRelationDescriptor r = (IRelationDescriptor)c;
		// TODO: If you don't use Positive property from RelationDescriptor, created in Rule2Relation, remove them!
		//if (r.isPositive()) {
		IRelation rel = null;
		if (aq != null && aq.get(r.getPredicate()) != null) {
			// Return tuples from the last iteration only!
			rel = aq.get(r.getPredicate());
		} else {
			// Return all tuples from the KB!
			rel = p.getFacts(r.getPredicate());
		}
		if(rel == null){
			return	RELATION.getRelation(r.getPredicate().getArity());
		}else{
			return rel; 
		}
	}
	
	private IRelation evaluateSelection(IComponent c,IProgram p,
			Map<IPredicate, IRelation> aq){
		
		if (c.getChildren().size() != 1) {
			throw new IllegalArgumentException(
					"Please provide the component with one subcomponent " +
					"(child), otherwise the evaluateSelection cannot be " +
					"performed!");
		}
		ISelectionDescriptor s = (ISelectionDescriptor)c;
		ISelection sel = Factory.RELATION_OPERATION
				.createSelectionOperator(
						evaluate(s.getChildren().get(0), p, aq),
						s.getPattern(), s.getIndexes());
		
		return sel.select();
	}
	
	/**
	 * <p>
	 * Evaluates a component which is of type of UNION. The execution of 
	 * this method may be a very expensive operation if a component to be 
	 * executed contains a number of very big relations to be unified.
	 * </p>
	 * 
	 * @param c
	 * @param p
	 * @param aq
	 * @return
	 */
	private IRelation evaluateUnion(IComponent c,IProgram p,
			Map<IPredicate, IRelation> aq){
		
		if (c.getChildren().size() == 0) {
			throw new IllegalArgumentException(
					"Please provide the component with at least one subcomponent " +
					"(child), otherwise the evaluateUnion cannot be" +
					"performed!");
		}
		IUnionDescriptor u = (IUnionDescriptor)c;
		List<IRelation> rels = new ArrayList<IRelation>(u.getChildren().size());
		for(int i=0; i<u.getChildren().size(); i++){
			rels.add(evaluate(u.getChildren().get(i), p, aq));
		}
		IUnion un = Factory.RELATION_OPERATION
				.createUnionOperator(rels);
		
		return un.union();
	}

	private IRelation evaluateConstant(IComponent c){
		
		IConstantDescriptor con = (IConstantDescriptor)c;
		IRelation r = RELATION.getRelation(1);
		r.add(Factory.BASIC.createMinimalTuple(con.getConstant()));
		return r;
	}
}
