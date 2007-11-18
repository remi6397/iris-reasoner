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

import static org.deri.iris.factory.Factory.ALGEBRA;
import static org.deri.iris.factory.Factory.BASIC;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.deri.iris.VariableExtractor;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.evaluation.algebra.IBuiltinDescriptor;
import org.deri.iris.api.evaluation.algebra.IComponent;
import org.deri.iris.api.evaluation.algebra.IJoinDescriptor;
import org.deri.iris.api.evaluation.algebra.IProjectionDescriptor;
import org.deri.iris.api.evaluation.algebra.IRelationDescriptor;
import org.deri.iris.api.evaluation.algebra.ISelectionDescriptor;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.operations.relations.JoinCondition;

/**
 * <p>
 * Algorithm 3.1 (Chapter 3. Logic as a data model, Principles of Database and
 * Knowledge - Base Systems, Ullman):
 * </p>
 * <p>
 * Computing the relation for a rule body, using relational algebra operations.
 * </p>
 * 
 * <p>Remark: Constructed terms are not considered in the current
 * Rule2Relation transformation!</p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date Dec 12, 2006
 * 
 */
public class Rule2Relation {

	/** Prefix for the variables */
	private static final String VAR_PREFIX = "?Y_";

	/** Counter for an arbitrarly chosen variable */
	private static int VAR_COUNTER = 0;

	// TODO: If you don't need - remove it!
	//private Map<ILiteral, List<IVariable>> m = null;
	//private Set<IVariable> oVars = null;

	/**
	 * <p>
	 * Transform a set of rules into relational algebra expressions
	 * </p>
	 * 
	 * @param rules
	 *            A set of rules to be translated
	 * @return A map of IDB predicates with corresponding relational algebra
	 *        	  expressions
	 */
	public Map<IRule, IComponent> translateRules(final Set<IRule> rls) {

		Map<IRule, IComponent> results = new Hashtable<IRule, IComponent>();		
		
		for (IRule rule : rls)
		{
			/* Rectify rules */
			IRule r = rule;	//MiscOps.rectify(rule);
			
			IComponent body = translateBody(r.getBody());

			int[] pInds = getProjectionIndexes(VariableExtractor.getLiteralVariablesList(r.getHead()), body.getVariables());

			// TODO: don't use pInds! remove them from the constructor
			IProjectionDescriptor projection = ALGEBRA.createProjectionDescriptor(pInds);
			projection.addChild(body);
			projection.addVariables(VariableExtractor.getLiteralVariablesList(r.getHead()));
			results.put(r, projection);
			
		}
		return results;
	}

	public Map<IPredicate, IComponent> translateQueries(final Set<IQuery> queries) {
		Map<IPredicate, IComponent> results = new Hashtable<IPredicate, IComponent>(queries.size());		
		
		for(IQuery q : queries){
			results.put(
					q.getLiterals().get(0).getAtom().getPredicate(), 
					translateBody(q.getLiterals()));
		}
		return results;
	}
	
	public IComponent translateQuery(final IQuery query) {
		return	translateBody(query.getLiterals());
	}
	
	/**
	 * <p>
	 * INPUT:
	 * </p>
	 * <p>
	 * Body of rule r = S1,...,Sn with variables X1,...,Xm;
	 * </p>
	 * <p>
	 * Si = pi(Ai1,...,Aiki) where pi <--> Ri & Ai = variable or constant.
	 * </p>
	 * 
	 * @param lits
	 *            A body of a rule to be translated.
	 * @return An expression of relational algebra.
	 */
	private IComponent translateBody(final List<ILiteral> lits) {
		IJoinDescriptor j = ALGEBRA.createJoinDescriptor(JoinCondition.EQUALS);
		IJoinDescriptor jNegative = ALGEBRA.createJoinDescriptor(JoinCondition.EQUALS);
		IJoinDescriptor jBuiltin = ALGEBRA.createJoinDescriptor(JoinCondition.EQUALS);
		IComponent cPos, cNeg, cBuilt;
		
		for (ILiteral l : lits) {
			if (! l.getAtom().isBuiltin()) {
				/** a. Ordinary literal (subgoal) */
				if(l.isPositive()){
					cPos = translateOrdinaryLiteral(l);
					if(cPos != null){
						cPos.setPositive(true);
						j.addChild(cPos);
						//j.addVariables(cPos.getVariables());
					}
				}else{
					/**
					 * b. Negative literal (subgoal): 
					 * needs to be handled after the ordinary subgoals.
					 */
					cNeg = translateOrdinaryLiteral(l);
					if(cNeg != null){
						cNeg.setPositive(false);
						jNegative.addChild(cNeg);
						//jTmp0.addVariables(cNeg.getVariables());
					}
				}
			}else{
				/**
				 * c. Built-in literal (subgoal): needs to be
				 * handled after the ordinary subgoals.
				 */
				cBuilt = translateBuiltInAtom(l);
				// for comparisson builtins
				/*if(cBuilt != null){
					if(l.getAtom() instanceof EqualBuiltin){
						// EqualBuiltin
						cBuilt.setPositive(l.isPositive());
					}else{
						// UnEqualBuiltin
						cBuilt.setPositive(! l.isPositive());
					}
					jTmp1.addChild(cBuilt);
					jTmp1.addVariables(cBuilt.getVariables());
				}*/
				jBuiltin.addChild(cBuilt);
				//jTmp1.addVariables(cBuilt.getVariables());
			}
		}
		
		if(jBuiltin.getChildren().size() > 0){
			j.addChildren(jBuiltin.getChildren());
			//j.addVariables(jTmp1.getVariables());
		}
		
		if(jNegative.getChildren().size() > 0){
			j.addChildren(jNegative.getChildren());
			//j.addVariables(jTmp0.getVariables());	
		}

		if(j.getChildren().size() == 1){
			return j.getChildren().get(0);
		} else {
			return j;
		}
	}

	private IComponent translateOrdinaryLiteral(final ILiteral l) {
		
		ITuple pattern = null;
		final IPredicate pred = l.getAtom().getPredicate();
		final int arity = pred.getArity();
		int[] indexes = new int[arity];
		int[] projectInds = org.deri.iris.operations.relations.MiscOps.getInitProjectionIndexes(arity);
		List<ITerm> ts = new ArrayList<ITerm>(arity);
		List<ITerm> terms = l.getAtom().getTuple();
		List<IVariable> vars = new ArrayList<IVariable>(arity);
		Map<IVariable, Integer> selIndexes = new HashMap<IVariable, Integer>();
		int i = 0, j = 0, n = 0, m = 0;
		boolean projectionNeeded = false;

		for (ITerm t : terms) {
			if (t.isGround()) {
				ts.add(t);
				vars.add(getFreshVariable(t));
				projectionNeeded = true;
			} else {
				ts.add(null);
				projectInds[n] = j++;
				// TODO: Constructed terms are not considered in Rule2Relation transformation yet!
				IVariable v = (IVariable) t;
				vars.add(v);
				if(selIndexes.get(v) == null){
					selIndexes.put(v, ++m);
				}
				indexes[i++] = selIndexes.get(v);
			}
			n++;
		}
		pattern = BASIC.createTuple(ts);
		IRelationDescriptor le = ALGEBRA.createRelationDescriptor(l.isPositive(), pred);

		le.addVariables(vars);
		ISelectionDescriptor s = null;
		// Is the selection required?
		if (m != terms.size()) {
			s = ALGEBRA.createSelectionDescriptor(pattern, indexes);
			s.addChild(le);
			s.addVariables(le.getVariables());
			if (!projectionNeeded) return s;
		}
		if (projectionNeeded) {
			IProjectionDescriptor p = ALGEBRA
					.createProjectionDescriptor(projectInds);
			p.addChild(s);
			p.addVariables(filterProjectionVariables(projectInds, s.getVariables()));
			return p;
		}
		return le;
	}
	
	/*private IComponent translateBuiltInAtom(final IAtom a) {
		ITerm t1 = a.getTuple().getTerm(0);
		ITerm t2 = a.getTuple().getTerm(1);
		
		// ?X = ?Y or ?X != ?Y
		if (t1 instanceof IVariable && t2 instanceof IVariable) {
			if ((!oVars.contains(t1)) || (!oVars.contains(t2))) {

				// ?X = ?Y --> Dx = PROJECTIONj(Ri) (Si(...,?Y, ...))
				Iterator<ILiteral> i = m.keySet().iterator();
				ILiteral l = null;
				List<IVariable> v = null;

				while (i.hasNext()) {
					l = i.next();
					v = m.get(l);
					if (v.contains(t2)) {
						ITerm tt = t1;
						t1 = t2; // t1 = ?X
						t2 = tt; // t2 = ?Y
						break;
					}
					if (v.contains(t1) || v.contains(t2)) {
						break;
					}
				}
				List<ITerm> terms = l.getTuple();
				List<IVariable> vars = new ArrayList<IVariable>(terms.size());
				List<IVariable> distinctVars = new ArrayList<IVariable>();
				int[] projectInds = new int[l.getPredicate().getArity()];
				*//**
				 * (b) If Y appears as the jth argument of ordinary subgoal Si,
				 * let Dx be PROJECTIONj(Ri)
				 *//*
				ITerm t = null;
				int j;
				for (j = 0; j < terms.size(); j++) {
					t = terms.get(j);
					if (t.equals(t1)) {
						vars.add((IVariable) t2);
						distinctVars.add((IVariable) t2);
						break;
					} else {
						// TODO: change null
						vars.add(getFreshVariable(null));
					}
				}
				for (int k = j + 1; k < terms.size(); k++) {
					// TODO: change null
					vars.add(getFreshVariable(null));
				}
				for (int k = 0; k < terms.size(); k++) {
					if (k != j) {
						projectInds[k] = -1;
					} else {
						projectInds[k] = 0;
					}
				}
				IRelationDescriptor r = ALGEBRA.createRelationDescriptor(true,
						l.getPredicate());

				r.addVariables(vars);
				IProjectionDescriptor p = ALGEBRA
						.createProjectionDescriptor(projectInds);

				p.addChild(r);
				p.addVariables(distinctVars);
				return p;
			}
		}else {
			// ?X = 'a'
			if (t2 instanceof IVariable) {
				ITerm tt = t1;
				t1 = t2; // t1 = ?X
				t2 = tt; // t2 = 'a'
			}
			IConstantDescriptor c = ALGEBRA.createConstantDescriptor(t2,
					(IVariable) t1);
			return c;
		}
		return null;
	}*/
	
	private IComponent translateBuiltInAtom(final ILiteral l) {
		IBuiltinDescriptor le = ALGEBRA.createBuiltinDescriptor(l.isPositive(), (IBuiltInAtom) l.getAtom());
		return le;
	}

	private IVariable getFreshVariable(ITerm t) {
		return Factory.TERM.createVariable(VAR_PREFIX + VAR_COUNTER++);
	}

	private int[] getProjectionIndexes(final List<IVariable> vars0,
			final List<IVariable> vars1) {
		int j = 0, k=0;
		int[] inds = new int[vars1.size()];

		for (j = 0; j < vars1.size(); j++) {
			inds[j] = -1;
		}
		IVariable v0 = null;
		IVariable v1 = null;
		for (int i=0; i<vars0.size(); i++) {
			v0 = vars0.get(i);
			for (j=0; j<vars1.size(); j++) {
				v1 = vars1.get(j);
				if (v0.equals(v1)) {
					inds[j] = k++;
					break;
				}
			}
		}
		return inds;
	}

//	private int[] getSelectionIndexes(final List<ITerm> selVars,
//			final List<IVariable> allVars, boolean equal) {
//		
//		List<IVariable> vars = new ArrayList<IVariable>(allVars.size());
//		vars.addAll(allVars);
//		int[] inds = new int[allVars.size()];
//		// TODO: Use scwitch (below) to integrate built-ins in IRIS
//		int j = 0, k;
//		if(equal) k = 1;
//		else k = -1;
//		for(ITerm v : selVars){
//			j = vars.indexOf((IVariable)v);
//			while(j != -1){
//				inds[j] = k;
//				vars.set(j, null);
//				j = vars.indexOf(v);
//			}
//		}
//		/*switch (condition){
//			case EQUALS:
//				
//				break;
//			case NOT_EQUAL:
//				
//				break;
//			case LESS_THAN:
//				
//				break;
//			case GREATER_THAN:
//				
//				break;
//			case LESS_OR_EQUAL:
//				
//				break;
//			case GREATER_OR_EQUAL:
//				
//				break;
//		}*/
//		return inds;
//	}
//	
	private List<IVariable> filterProjectionVariables(final int[] pInds,
			final List<IVariable> vars) {
		List<IVariable> v = new ArrayList<IVariable>(pInds.length);
		for (int i = 0; i < pInds.length; i++) {
			if (pInds[i] != -1) {
				v.add(vars.get(i));
			}
		}
		return v;
	}
}
