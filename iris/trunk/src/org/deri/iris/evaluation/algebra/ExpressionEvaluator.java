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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.RELATION_OPERATION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.evaluation.algebra.IBuiltinDescriptor;
import org.deri.iris.api.evaluation.algebra.IComponent;
import org.deri.iris.api.evaluation.algebra.IConstantDescriptor;
import org.deri.iris.api.evaluation.algebra.IDifferenceDescriptor;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.evaluation.algebra.IJoinDescriptor;
import org.deri.iris.api.evaluation.algebra.IProjectionDescriptor;
import org.deri.iris.api.evaluation.algebra.IRelationDescriptor;
import org.deri.iris.api.evaluation.algebra.ISelectionDescriptor;
import org.deri.iris.api.evaluation.algebra.IUnionDescriptor;
import org.deri.iris.api.evaluation.algebra.IComponent.ComponentType;
import org.deri.iris.api.operations.relation.IBuiltinEvaluator;
import org.deri.iris.api.operations.relation.IDifference;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.operations.relation.IProjection;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.operations.relation.IUnion;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.operations.relations.MiscOps;

/**
 * <p>
 * An evaluator of a relational algebra expression. This evaluator is used
 * whenever an evaluation of relational algebra expressions is needed regardless
 * of a particular evaluation algorithm (e.g. naive evaluation, semi-naive
 * evaluation etc.).
 * </p>
 * <p>
 * This evaluator takes a program previously transformed to a set of relation
 * algebra expressions, as an input, and executes these expressions. Each relation 
 * in algebra expressions may contain tuples with terms of different data types 
 * in arbitrary positions.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date Dec 18, 2006 
 */
public class ExpressionEvaluator implements IExpressionEvaluator {

	/** A program to be evaluated */
	private IProgram p = null;
	
	/** A map of IDB predicates and their incremental relations */
	Map<IPredicate, IMixedDatatypeRelation> aq = null;
	
	/** List of IDB predicates for which one incremental relation is already 
	 *  substituted */
	private List<IPredicate> duplicateIDB = null;
	
	/** Set of IDB predicates for a particular rule that is being evaluated */
	private Set<IPredicate> ruleIDBpredicates = null;
	
	/** First incremental relation that is substituted */
	private boolean firstIDB = true;
	
	public ExpressionEvaluator() {
	}

	public IMixedDatatypeRelation evaluate(IComponent c, IProgram p) {
		this.p = p;
		this.ruleIDBpredicates = new HashSet<IPredicate>();
		this.duplicateIDB = new ArrayList<IPredicate>();
		return evalRuleIncr(c, p, null);
	}

	public IMixedDatatypeRelation evaluateIncrementally(IComponent c, IProgram p,
			Map<IPredicate, IMixedDatatypeRelation> aq) {

		this.p = p;
		this.aq = aq;
		this.duplicateIDB = new ArrayList<IPredicate>();
		this.ruleIDBpredicates = new HashSet<IPredicate>();
		return evalRuleIncr(c, p, aq);
	}

	private IMixedDatatypeRelation evaluate(IComponent c) {
		switch (c.getType()) {
		case BUILTIN:
			return evaluateBuiltin(c, null, null);
		case DIFFERENCE:
			return evaluateDifference(c);
		case JOIN:
			return evaluateJoin(c, null);
		case PROJECTION:
			return evaluateProjection(c);
		case SELECTION:
			return evaluateSelection(c);
		case UNION:
			return evaluateUnion(c);
		case RELATION:
			return evaluateRelation(c);
		case CONSTANT:
			return evaluateConstant(c);
		default:
			throw new IllegalArgumentException(
				"Relational algebra statement to be evaluated is of unknown type.");
		}
	}
	
	private IMixedDatatypeRelation evalRuleIncr(IComponent c, IProgram p,
			Map<IPredicate, IMixedDatatypeRelation> aq) {
		
		if (c == null) {
			throw new IllegalArgumentException(
				"Please provide a non null component, " +
				"otherwise the evalRuleIncr cannot be performed!");
		}
		if(aq != null){
			List<IMixedDatatypeRelation> rels = new ArrayList<IMixedDatatypeRelation>(aq.size());
			this.duplicateIDB.clear();
			this.firstIDB = true;
			boolean firstRound = true;
			while(firstRound || getNextIDB(aq)){
				rels.add(evaluate(c));
				firstRound = false;
			}
			// Return result for the evaluated EDB relation
			if(rels.size() == 1) return rels.get(0);
			// Return result for the evaluated IDB relation
			IUnion un = Factory.RELATION_OPERATION.createUnionOperator(rels);
			return un.union();
		}else{
			// There are no incremental tuples => use the full relations for the evaluation 
			return evaluate(c);
		}
	}
	
	private boolean getNextIDB(Map<IPredicate, IMixedDatatypeRelation> aq){
		if(aq == null) return false;
		this.ruleIDBpredicates.removeAll(this.duplicateIDB);
		Iterator<IPredicate> it = this.ruleIDBpredicates.iterator();
		while(it.hasNext()){
			if(aq.containsKey(it.next())){
				this.firstIDB = true;
				return true;
			}
		}
		return false;
	}
	
	private IMixedDatatypeRelation evaluateDifference(IComponent c) {

		if (c.getChildren().size() != 2) {
			throw new IllegalArgumentException(
					"Please provide the component with two subcomponents (children), " +
					"otherwise the evaluateDifference cannot be performed!");
		}
		IDifferenceDescriptor d = (IDifferenceDescriptor) c;
		IDifference diff = Factory.RELATION_OPERATION.createDifferenceOperator(
				evaluate(d.getChildren().get(0)), 
				evaluate(d.getChildren().get(1)));

		return diff.difference();
	}

	@SuppressWarnings("deprecation")
	private IMixedDatatypeRelation evaluateJoin(IComponent c, int[] pInds) {

		if (c.getChildren().size() < 2) {
			throw new IllegalArgumentException(
					"Please provide the component with at least two subcomponents " +
					"(children), otherwise the join operation cannot be performed!");
		}
		IJoinDescriptor j = (IJoinDescriptor) c;
		IJoin jo = null;
		List<IVariable> vars = new ArrayList<IVariable>();
		IComponent c0 = j.getChildren().get(0);
		vars.addAll(c0.getVariables());
		IComponent c1 = null;
		IMixedDatatypeRelation r0 = evaluate(c0);
		@SuppressWarnings("unused")
		boolean emptyRel = (r0.size() == 0) ? true : false;
		boolean addVars = true;
		IMixedDatatypeRelation r1 = null;
		
		for (int i = 1; i < j.getChildren().size(); i++) {
			c1 = j.getChildren().get(i);
			//if (!emptyRel) {
				if (c1.getType().equals(ComponentType.BUILTIN)) {
					IBuiltinDescriptor con = (IBuiltinDescriptor) c1;
					if (con.getBuiltin().isEvaluable(vars)) {
						r0 = evaluateBuiltin(c1, vars, r0);
						addVars = true;
					} else {
						j.getChildren().remove(i);
						i--;
						j.addChild(c1);
						addVars = false;
					}
				} else {
					r1 = evaluate(c1);
					if (c1.isPositive()) {
						jo = Factory.RELATION_OPERATION.createSortMergeJoinOperator(
								r0, r1, 
								// TODO: get correct projection indexes!
								MiscOps.getJoinIndexes(vars, c1.getVariables()), 
								j.getCondition());
						addVars = true;
					} else {
						jo = Factory.RELATION_OPERATION
								.createJoinComplementOperator(r0, r1,
										MiscOps.getJoinIndexes(vars, c1
												.getVariables()));
						addVars = false;
					}
					r0 = jo.join();
				}
				/*if (r0 != null && r0.size() == 0)
					emptyRel = true;
			}*/
			if (addVars && c1 != null && c1.getVariables() != null && c1.isPositive())
				vars.addAll(c1.getVariables());
		}
		j.getVariables().clear();
		j.addVariables(vars);
		return r0;
	}

	private IMixedDatatypeRelation evaluateProjection(IComponent c) {

		if (c.getChildren().size() != 1) {
			throw new IllegalArgumentException(
				"Please provide the component with only one subcomponent "
				+ "(child),otherwise the evaluateProjection cannot be performed!");
		}
		IProjectionDescriptor pr = (IProjectionDescriptor) c;
		IMixedDatatypeRelation rel = evaluate(pr.getChildren().get(0));
		if (!Arrays.equals(pr.getVariables().toArray(), 
						   pr.getChildren().get(0).getVariables().toArray())) {
			IProjection projection = Factory.RELATION_OPERATION.
					createProjectionOperator(rel,
					// TODO: If you don't use project inds, created in
					// Rule2Relation, remove them!
					// pr.getIndexes());
					MiscOps.getProjectionIndexes(pr.getChildren()
							.get(0).getVariables(), pr.getVariables()));

			return projection.project();
		}
		return rel;
	}

	private IMixedDatatypeRelation evaluateRelation(IComponent c) {

		if (c.getChildren().size() != 0) {
			throw new IllegalArgumentException(
				"Please provide the component with no subcomponent (no child), " +
				"otherwise evaluateRelation cannot be performed!");
		}
		IRelationDescriptor r = (IRelationDescriptor) c;
		IMixedDatatypeRelation rel = null;
		
		if(aq != null && aq.get(r.getPredicate()) != null && aq.get(r.getPredicate()).size()>0 && c.isPositive()) {
			if(this.firstIDB && (! this.duplicateIDB.contains(r.getPredicate()))){
				// Return tuples from the last iteration only!
				rel = aq.get(r.getPredicate());
				this.duplicateIDB.add(r.getPredicate());
				this.firstIDB = false;
			} else {
				// Return all tuples from the KB!
				rel = p.getFacts(r.getPredicate());
			}
			this.ruleIDBpredicates.add(r.getPredicate());
		} else {
			// Return all tuples from the KB!
			rel = p.getFacts(r.getPredicate());
		}
		if (rel == null) {
			return RELATION.getMixedRelation(r.getPredicate().getArity());
		} else {
			return rel;
		}
	}

	private IMixedDatatypeRelation evaluateSelection(IComponent c) {

		if (c.getChildren().size() != 1) {
			throw new IllegalArgumentException(
				"Please provide the component with one subcomponent (child), " +
				"otherwise the evaluateSelection cannot be performed!");
		}
		ISelectionDescriptor s = (ISelectionDescriptor) c;
		ISelection sel = Factory.RELATION_OPERATION.createSelectionOperator(
				evaluate(s.getChildren().get(0)), 
				s.getPattern(), 
				s.getIndexes());

		return sel.select();
	}

	/**<p>
	 * Evaluates a component which is of type of UNION. The execution of this
	 * method may be a very expensive operation if a component to be executed
	 * contains a number of very big relations to be unified.
	 * </p>
	 * 
	 * @param c
	 * @param p
	 * @param aq
	 * @return
	 */
	private IMixedDatatypeRelation evaluateUnion(IComponent c) {

		if (c.getChildren().size() == 0) {
			throw new IllegalArgumentException(
				"Please provide the component with at least one subcomponent "
				+ "(child), otherwise the evaluateUnion cannot be performed!");
		}
		IUnionDescriptor u = (IUnionDescriptor) c;
		List<IMixedDatatypeRelation> rels = new ArrayList<IMixedDatatypeRelation>(u.getChildren().size());
		for (int i = 0; i < u.getChildren().size(); i++) {
			this.duplicateIDB.clear();
			this.ruleIDBpredicates.clear();
			// The first round must be taken
			boolean firstRound = true;
			this.firstIDB = true;
			while((firstRound || getNextIDB(aq))){	
				rels.add(evaluate(u.getChildren().get(i)));
				firstRound = false;
			}
		}
		IUnion un = Factory.RELATION_OPERATION.createUnionOperator(rels);
		return un.union();
	}
	
	private IMixedDatatypeRelation evaluateConstant(IComponent c) {
		IConstantDescriptor con = (IConstantDescriptor) c;
		IMixedDatatypeRelation r = RELATION.getMixedRelation(1);
		r.add(BASIC.createTuple(con.getConstant()));
		return r;
	}

	private IMixedDatatypeRelation evaluateBuiltin(IComponent c, List<IVariable> relVars,
		IMixedDatatypeRelation rel) {

		IBuiltinDescriptor con = (IBuiltinDescriptor) c;
		IBuiltinEvaluator eval = null;
		if (c.getChildren().size() == 0) {
			if (relVars == null || rel == null) {
				eval = RELATION_OPERATION.createBuiltinEvaluatorOperator(
						con.getBuiltin(), new ArrayList<IVariable>(0), 
						RELATION.getMixedRelation(0));
			} else {
				eval = RELATION_OPERATION.createBuiltinEvaluatorOperator(con
						.getBuiltin(), relVars, rel);
			}
		} else {
			throw new IllegalArgumentException(
					"Nested built-ins are currently not supported!");
		}
		c.getVariables().clear();
		c.addVariables(eval.getOutVars());
		return eval.evaluate();
	}
}
