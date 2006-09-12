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

import java.util.*;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.builtins.IStringEqual;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.basics.BasicFactory;
import org.deri.iris.terms.TermFactory;
import org.deri.iris.operations.relations.JoinCondition;
import org.deri.iris.evaluation.seminaive.model.ModelFactory;
import org.deri.iris.api.evaluation.seminaive.model.*;


/**
 * Implementation of Algorithm 3.1: Computing the Relation for a Rule Body, 
 * Using Relational Algebra Operations.
 * Chapter 3. Logic as a Data Model.
 * 
 * @author Paco Garcia, University of Murcia
 * @date 01-sep-2006
 *
 */
public class Rule2Relation {
	private Map<IRule, ITree> results = new Hashtable<IRule, ITree>();
	
	/**
	 * Transform a set of rules into relational algebra operations 
	 * @param rule Collection that contains the rules in the program (PRECONDITION: the rules are ordered according to the dependences within the program)
	 * @return An structure that contains the predicates head of the rules and the algebra operations for these rules
	 */
	public Map<IRule, ITree> eval(final Collection<org.deri.iris.api.basics.IRule> rules)
	{		
		for (org.deri.iris.api.basics.IRule r: rules) {
			IRule head = 
				ModelFactory.FACTORY.createRule(r.getHeadLiteral(0).getPredicate().getPredicateSymbol(), 
					r.getHeadLiteral(0).getPredicate().getArity()); 
			ITree body = evalRule(r, head.getArity());
			// Check whether is a rule with the same head predicate; if so, the bodies must be united
			IRule oldHead;
			if ((oldHead = containsKey(results.keySet(), head))!= null)
			{
				// UNION
				ITree newBody = ModelFactory.FACTORY.createUnion();
				newBody.addComponent(results.get(oldHead));
				newBody.addComponent(body);
				results.remove(oldHead);
				results.put(head, newBody);
			} else {
				results.put(head, body);
			}
		}		
		return results;
	}
	
	/**
	 * Returns the predicate that is equals to the one specified as parameter. Returns null if the keyset contains no predicate equals to the specified.
	 * @param keySet Set with the keys of a hashtable
	 * @param head Predicate which is compared with the ones stored in the keyset
	 * @return true if the head is already stored in the keyset; false otherwise.
	 */private IRule containsKey(Set<IRule> keySet, IRule head) {
		Iterator<IRule> keys = keySet.iterator();
		
		while (keys.hasNext())
		{
			IRule r = keys.next();
			if (r.equals(head))
			{
				return r;
			}
		}
		return null;
	}

	/**
	 * Algorithm 3.1
	 * @param r Rule Body
	 * @return A tree with the relational algebra operations for the input rule
	 */private ITree evalRule(org.deri.iris.api.basics.IRule r, int headArity)
	{
		java.util.Hashtable<ITerm, ILiteral> variables = new java.util.Hashtable<ITerm, ILiteral>();
		List<ILiteral> builtins = new java.util.LinkedList<ILiteral>();
		
		/* 
		 * Algorithm 3.1: Computing the relation for a Rule Body, Using relational Algebra Operation
		 * Chapter 3. Logic as a data model. 
		 * INPUT: Body of rule r = S1,...,Sn with variables X1,...,Xm;
		 * Si = pi(Ai1,..., Aiki) where pi <--> Ri & Ai = variable|constant
		 */
		
		ITree result;
		ITree globalJoin = ModelFactory.FACTORY.createNaturalJoin();
		
		List<ILiteral> literals = r.getBodyLiterals();
		for (ILiteral l: literals)
		{
			ITree temporalResult ;
			// Preliminary. Check whether it is an ordinary subgoal or one of the type X = a.
			if (! (l.getAtom() instanceof IBuiltInAtom)) 
			{
				/*
				 * For each ordinary Si (e.g. Si = p(X, b) ), let Qi be the expression 
				 * PROJECTION_Vi(SELECTION_Fi(Ri))
				 * where Vi = for each variable X in Si exactly one component
				 */
				
				// 1. Ri
				org.deri.iris.api.evaluation.seminaive.model.ITree leaf = ModelFactory.FACTORY.createRule(
						l.getPredicate().getPredicateSymbol(),
						l.getPredicate().getArity());
				
				// Check whether the predicate is another IDB rule; if so, the leaf will be replace for the whole body of the predicate
				IRule temporalHead;
				if ((temporalHead = containsKey(results.keySet(), (IRule)leaf)) != null)
				{
					leaf = results.get(temporalHead);
				}
				
				/*
				 * 2. SELECTION_Fi(Ri)
				 * Fi: -patternTerms-
				 *  (a) If position k of Si has a constant a, then Fi has the term $k = a
				 *  (b) If positions k and l of Si both contain the same variable, then Fi has the term $k = $l
				 * Vi: -int[]-
				 *  Set of components including, for each valriable X that appears among the arguments of Si, exactly one component where X appears.
				 */		
				List<ITerm> terms = l.getAtom().getTuple().getTerms();
				List<ITerm> patternTerms = new ArrayList<ITerm>();
				int[] projection = new int[terms.size()];
				int i = 0;			
				boolean noFi = true;
				for (ITerm t : terms)
				{
					if (t instanceof org.deri.iris.terms.ConstantTerm)
					{
						projection[i] = -1;
						patternTerms.add(t);
						noFi = false;
					} else if (t instanceof org.deri.iris.terms.ConstructedTerm) {
						/*
						 * TODO recursitivy - Not allowed in datalog expressions
						 */
						projection[i] = -1;
						patternTerms.add(null);
					} else if (t instanceof org.deri.iris.terms.StringTerm) {
						projection[i] = -1;
						patternTerms.add(t);
						noFi = false;
					} else if (t instanceof org.deri.iris.terms.Variable) {
						projection[i] = 0;
						if (variables.containsKey(t) && (variables.get(t) == l)) {
							// Positions k and l of Si both contain the same variable,
							// then Fi has the term $k = $l.
							noFi = false;

							// TODO. Check whether the variable appeared before in this literal
							// and, if so, add condition $k = $l in the "patternTerms way"
							// How can it be expressed?
						} else {
							patternTerms.add(null);
							// Store the literal referencing this variable
							variables.put(t, l);
						}
					}
					i++;
				}
				
				if (!noFi)
				{
					// 2.2. SELECTION
					ISelection s = ModelFactory.FACTORY.createSelection(BasicFactory.getInstance().createTuple(patternTerms));
					s.addComponent(leaf);
					// 3. PROJECTION_Vi(SELECTION_Fi(Ri))
					// 3.1 Vi - already done in 2.1			
					// 3.2 PROJECTION
					IProjection p = ModelFactory.FACTORY.createProjection(projection);
					p.addComponent(s);
					temporalResult = p;
				} else
				{
					/*
					 * As a special case, if Si is such that there are no terms in Fi, e.g., Si = p(X,Y), 
					 * then take Fi to be the identically true condition, so Qi = Ri
					 */
					temporalResult = leaf;
				}
			
			} else { // Built-in expression
				//TODO. B. Variable X not among ordinary subgoal (eg. 'U = a' or 'U = W')
				/*
				 * For each variable X not found among the ordinary subgoals, compute an expression Dx
				 * that produces a unary relation containing all the values that X could possibly have 
				 * in an assignment that satisfies all the subgoals of rule r. 
				 *   (a) If Y = a is a subgoal, then let Dx be the constant expression {a}
				 *   (b) If Y appears as the jth argument of ordinary subgoal Si, let Dx be PROJECTIONj(Ri)
				 */
				builtins.add(l);
				//1. Compute Dx
				if (l instanceof IStringEqual) {
					// (a) If Y = a, Dx = {a}
					//TODO. set the name of the relation correctly. Depends on the builtins implementation
					temporalResult = ModelFactory.FACTORY.createRule("Da",1);
				} else /* if (l instanceof 'other built in expression' */{
					// (b) If X = Y & Si = r(...,Yj,...) --> Dx = PROJECTION_j(Ri)
					ILiteral l_v = variables.get(l);
					if (l_v != null) {
						IRule leaf = ModelFactory.FACTORY.createRule(
								l_v.getPredicate().getPredicateSymbol(),
								l_v.getPredicate().getArity());
						int[] j = new int[leaf.getArity()];
						List<ITerm> terms = l_v.getAtom().getTuple().getTerms();
						
						for (int i = 0; i < terms.size(); i++)
						{
							ITerm t = terms.get(i);
							// TODO. Find the index - Depends on the builtins implementation
							if ((t instanceof org.deri.iris.terms.Variable) && 
									((org.deri.iris.terms.Variable)t).compareTo((IVariable)l.getAtom())== 0)
								j[i] = 0;
							else
								j[i] = -1;
						}
						
						temporalResult = ModelFactory.FACTORY.createProjection(j);
						temporalResult.addComponent(leaf);
					} else {
						// TODO What to do in case an error like this occur?
						// If the rule is safe this cannot happen
						temporalResult = null;
					}
						
				}
			
			}
			
			// C. Natural join of all the things generated (E)
			globalJoin.addComponent(temporalResult); 
		}
		// In case there is only one term there is no need for joining
		if (globalJoin.getChildren().size() == 1)
			globalJoin = (ITree)globalJoin.getChildren().get(0);
		
		// TODO. D. EVAL-RULE(r, R1,...,Rn) = SELECTION_F(E)
		// F conjunction of built-in subgoals appearing.
		if (!builtins.isEmpty())
		{
			// TODO. Depends on the builtins implementation
			ITuple pattern = BasicFactory.getInstance().createTuple(/* pattern terms */);
			ISelection selection = ModelFactory.FACTORY.createSelection(pattern);
		    selection.addComponent(globalJoin);
		    result = selection;
		} else
			result = globalJoin;
		
		int[] globalProjectionIndex = new int[headArity];
		for (int i = 0; i < headArity; i++)
			globalProjectionIndex[i] = 0;
		IProjection globalProjection = ModelFactory.FACTORY.createProjection(globalProjectionIndex);
		globalProjection.addComponent(result);
		return globalProjection;
	}

}
