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
import org.deri.iris.basics.seminaive.EqualityLiteral;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.basics.BasicFactory;
import org.deri.iris.evaluation.seminaive.model.ModelFactory;
import org.deri.iris.factory.Factory;
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
	private Map<ITree, ITree> results = new Hashtable<ITree, ITree>();
	
	/**
	 * Transform a set of rules into relational algebra operations 
	 * @param rule Collection that contains the rules in the program (PRECONDITION: the rules are ordered according to the dependences within the program)
	 * @return An structure that contains the predicates head of the rules and the algebra operations for these rules
	 */
	public Map<ITree, ITree> eval(final Collection<org.deri.iris.api.basics.IRule> rules)
	{		
		
		for (org.deri.iris.api.basics.IRule r: rules) {
			ITree head = (ITree)ModelFactory.FACTORY.createTree(
					r.getHeadLiteral(0).getPredicate().getPredicateSymbol());
			head.addAllVariables(r.getHeadVariables());
			

			ITree body = evalRule(r, head);
			if (body != null) {
				// Check whether there is a rule with the same head predicate; if so, the bodies must be united
				ITree oldHead;
				if ((oldHead = containsKey(results.keySet(), head))!= null)
				{
					// UNION
					// The relations should be union-compatible:
					//  (a) Same number of fields.
					//  (b) Corresponding fields have the same type.
					ITree newBody = ModelFactory.FACTORY.createUnion();
					newBody.addComponent(results.get(oldHead));
					newBody.addComponent(body);
					results.remove(oldHead);
					results.put(head, newBody);
				} else {
					results.put(head, body);
				}
			}
		}		
		return results;
	}
	
	/**
	 * Returns the predicate that is equals to the one specified as parameter. Returns null if the keyset contains no predicate equals to the specified.
	 * @param keySet Set with the keys of a hashtable
	 * @param head Predicate which is compared with the ones stored in the keyset
	 * @return true if the head is already stored in the keyset; false otherwise.
	 */
	private ITree containsKey(Set<ITree> keySet, ITree head) {
		Iterator<ITree> keys = keySet.iterator();
		
		while (keys.hasNext())
		{
			ITree r = keys.next();
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
	 */
	 private ITree evalRule(org.deri.iris.api.basics.IRule r, ITree head)
	 {
		Map<IVariable, List<ILiteral>> variables = new java.util.Hashtable<IVariable, List<ILiteral>>();
		List<EqualityLiteral> equalityLiterals = new java.util.LinkedList<EqualityLiteral>();

		
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
			if (! (l instanceof EqualityLiteral)) 
			{
				temporalResult = evalLiteral(l, head, variables);
			} else { // Equality literal
				equalityLiterals.add((EqualityLiteral)l);
				temporalResult = evalEqualityLiteral(l, head, variables, equalityLiterals);
			}
			
			// C. Natural join of all the things generated (E)
			if (temporalResult != null)
				if (!temporalResult.equals(head))
					globalJoin.addComponent(temporalResult); 
			else
				return null; // this rule has no sense
		}
		
		// In case there is only one term there is no need for joining
		if (globalJoin.getChildren().size() == 1)
			globalJoin = (ITree)globalJoin.getChildren().get(0);
		
		// TODO. D. EVAL-RULE(r, R1,...,Rn) = SELECTION_F(E)
		// F conjunction of built-in subgoals appearing.
		if (!equalityLiterals.isEmpty())
		{
			// TODO. Depends on the equalityLiterals implementation
			/*
			ITuple pattern = BasicFactory.getInstance().createTuple();
			ISelection selection = ModelFactory.FACTORY.createSelection(pattern);
		    selection.addComponent(globalJoin);
		    result = selection;
		    */
			result = globalJoin;
		} else
			result = globalJoin;
		
		int[] globalProjectionIndex = new int[head.getArity()];
		IProjection globalProjection = ModelFactory.FACTORY.createProjection(globalProjectionIndex);
		globalProjection.addVariables(head.getVariables());
		globalProjection.addComponent(result);
		return globalProjection;
	}
	 
	 /**
	  * Evaluate an ordinary subgoal 
	  * @param l Literal containing the ordinary subgoal
	  * @param head Head of the rule that is being analyzed
	  * @param variables Set of variables found in this rule
	  * @return The algebraic expression for this literal
	  */
	 private ITree evalLiteral(ILiteral l, ITree head, Map<IVariable, List<ILiteral>> variables){
		 ITree result;
		 
		 /*
		  * For each ordinary Si (e.g. Si = p(X, b) ), let Qi be the expression 
		  * PROJECTION_Vi(SELECTION_Fi(Ri))
		  * where Vi = for each variable X in Si exactly one component
		  */
			
		 // 1. Ri
		 org.deri.iris.api.evaluation.seminaive.model.IRule leaf = ModelFactory.FACTORY.createRule(
				 l.getPredicate().getPredicateSymbol(),
				 l.getPredicate().getArity());
		 leaf.addAllVariables(l.getTuple().getVariables());
		 
		 /*
		  * 2. SELECTION_Fi(Ri)
		  * Fi: -patternTerms-
		  *  (a) If position k of Si has a constant a, then Fi has the term $k = a
		  *  (b) If positions k and l of Si both contain the same variable, then Fi has the term $k = $l
		  * Vi: -int[]-
		  *  Set of components including, for each valriable X that appears among the arguments of Si, exactly one component where X appears.
		  */
		 int arity = l.getPredicate().getArity();
		 List<ITerm> terms = l.getAtom().getTuple().getTerms();
		 List<ITerm> selectionPattern = new ArrayList<ITerm>();
		 List<IVariable> projectionVariables = new LinkedList<IVariable>();
		 int[] selectionIndex = new int[arity];
		 int[] projectionIndex = new int[arity];
		 int[] joinIndex = new int[arity];
		 
		 boolean noFi = getIndexes(terms, selectionPattern, selectionIndex, projectionIndex, projectionVariables, joinIndex, variables, l, head);
		 
		 
		 if (!noFi)
		 {
			 // 2.2. SELECTION
			 ISelection s = ModelFactory.FACTORY.createSelection(BasicFactory.getInstance().createTuple(selectionPattern));
			 s.addComponent(leaf);
			 // 3. PROJECTION_Vi(SELECTION_Fi(Ri))
			 // 3.1 Vi - already done in 2.1			
			 // 3.2 PROJECTION
			 IProjection p = ModelFactory.FACTORY.createProjection(projectionIndex);
			 p.addAllVariables(projectionVariables);
			 p.addComponent(s);
			 result = p;
		 } else
		 {
			 /*
			  * As a special case, if Si is such that there are no terms in Fi, e.g., Si = p(X,Y), 
			  * then take Fi to be the identically true condition, so Qi = Ri
			  */
			 result = leaf;
		 }
		 
		 return result;
	 }
	 
	 /**
	  * Evaluate an equality literal (e.g. ?X = 'a', ?X=?Y);
	  * @param el Literal containing the equality subgoal
	  * @param head Head of the rule that is being analyzed
	  * @param variables Set of variables found in this rule
	  * @return The algebraic expression for this literal
	  */
	 private ITree evalEqualityLiteral (ILiteral el, ITree head, Map<IVariable, List<ILiteral>> variables, List<EqualityLiteral> equalityLiterals){
		 
		 /*B. Variable X not among ordinary subgoal (eg. 'U = a' or 'U = W')
		  *
		  * For each variable X not found among the ordinary subgoals, compute an expression Dx
		  * that produces a unary relation containing all the values that X could possibly have 
		  * in an assignment that satisfies all the subgoals of rule r. 
		  *   (a) If Y = a is a subgoal, then let Dx be the constant expression {a}
		  *   (b) If Y appears as the jth argument of ordinary subgoal Si, let Dx be PROJECTIONj(Ri)
		  */

		 ITerm t1 = el.getTuple().getTerm(0);
		 ITerm t2 = el.getTuple().getTerm(1);
		 
		 if (t1 instanceof org.deri.iris.terms.Variable && t2 instanceof org.deri.iris.terms.Variable) {
			 // t1 & t2 are variables
			 if (!t1.equals(t2)) {
				 if (el.isPositive()) {
					 equalityLiterals.add((EqualityLiteral)el);
					 // ?X = ?Y --> Dx = PROJECTIONj(Ri) (Si(...,?Y, ...))
					 if (variables.containsKey(t2)) {
						 ITerm tt = t1;
						 t1 = t2; // t1 = ?X
						 t2 = tt; // t2 = ?Y
					 }
					 
					 ILiteral l = variables.get(t1).get(0);
					 List<IVariable> elvariables = new LinkedList<IVariable>();
					 List<ITerm> literalTerms = l.getTuple().getTerms();
					 for(ITerm t: literalTerms) {
						 if (t.equals(t1))
							 elvariables.add((IVariable)t2);
						 else 
							 elvariables.add(createVariable(variables.keySet()));
					 }
					 ITree elleaf = ModelFactory.FACTORY.createRule(
							 l.getPredicate().getPredicateSymbol(), 
							 l.getPredicate().getArity());
					 elleaf.addAllVariables(elvariables);
					 
					 IProjection elprojection = ModelFactory.FACTORY.createProjection(new int[1]);
					 elprojection.addVariable((IVariable)t2);
					 elprojection.addComponent(elleaf);
					 return elprojection;
				 } else {
					 // ?X != ?Y --> needed for the global selection
					 equalityLiterals.add((EqualityLiteral)el);
					 return head; // do not include any term
				 }
			 } else if (el.isPositive()) {
				 return head; // ?X = ?X --> true -- do not include any term
			 } else
				 return null; // ?X != ?X --> false -- invalidate the rule
			 
			 
		 } else if(t1 instanceof org.deri.iris.terms.StringTerm && t2 instanceof org.deri.iris.terms.StringTerm) {
			 // t1 & t2 are strings
			 if (!t1.equals(t2))
				 return null; // 'b' = 'c' --> false -- invalidate the rule
			 else 
				 return head; // 'a' ='a' --> true -- do not include any term
			 
		 } else {
			 // one is variable and the other string (e.g. ?X='a')
			 // ?X = 'a' needed for global selection
			 equalityLiterals.add((EqualityLiteral)el);

			 if (t1 instanceof org.deri.iris.terms.StringTerm) {
				 ITerm tt = t1;
				 t1 = t2; // t1 = ?X
				 t2 = tt; // t2 = 'a'
			 }
			 // TODO			 
			 return null;
		 }
	 }
	 
	 private boolean getIndexes(List<ITerm> terms, 
			 List<ITerm> selectionPattern, 
			 int[] selectionIndex, 
			 int[] projectionIndex, 
			 List<IVariable> projectionVariables,
			 int[] joinIndex, 
			 Map<IVariable, List<ILiteral>> variables, 
			 ILiteral l,
			 ITree head){
		
		 initializeIndex(selectionIndex);
		 initializeIndex(projectionIndex);
		 initializeIndex(joinIndex);
		 int i = 0;
		 boolean noFi = true;
		 for (ITerm t : terms)
		 {
			 if (t instanceof org.deri.iris.terms.ConstantTerm)
			 {
				 projectionIndex[i] = -1;
				 selectionPattern.add(t);
				 selectionIndex[i] = -1;
				 noFi = false;
			 } else if (t instanceof org.deri.iris.terms.ConstructedTerm) {
				 /*
				  * TODO recursitivy - Not allowed in datalog expressions
				  */
				 projectionIndex[i] = -1;
				 selectionPattern.add(null);
				 selectionIndex[i] = -1;
			 } else if (t instanceof org.deri.iris.terms.StringTerm) {
				 projectionIndex[i] = -1;
				 selectionPattern.add(t);
				 selectionIndex[i] = -1;
				 noFi = false;
			 } else if (t instanceof org.deri.iris.terms.Variable) {
				 IVariable v = (IVariable)t;
				 projectionIndex[i] = 0;
				 projectionVariables.add(v);
				 selectionPattern.add(null);
				 if (variables.containsKey(v)) {
					 List<ILiteral> literalList = variables.get(v);
					 for (ILiteral literal: literalList) {
						 if (literal.equals(l)){
							 // Positions k and l of Si both contain the same variable,
							 // then Fi has the term $k = $l.
							 noFi = false;							 
							 selectionIndex[i] = l.getAtom().getTuple().getTerms().indexOf(v);
							 // TODO. Check whether the variable appeared before in this literal
							 // and, if so, add condition $k = $l in the "patternTerms way"
							 // How can it be expressed? DARKO IS STUDYING IT
						 } else {
							 // A Join between these two literals should be created
							 joinIndex[i] = literal.getAtom().getTuple().getTerms().indexOf(v);
						 }
					 }							
				 } else {					 
					 // Store the literal referencing this variable
					 List<ILiteral> literalList = new LinkedList<ILiteral>();
					 literalList.add(l);
					 variables.put(v, literalList);
				 }
			 }
			 i++;
		 }

		 
		 return noFi; 
	 }

	 
	 private void initializeIndex(int[] i)
	 {
		 for (int j = 0; j < i.length; j++)
			 i[j] = -1;
	 }

	 private IVariable createVariable(Set<IVariable> variables) {
		 IVariable v = null;
		 char[] c = {'a','a','a'};
		 for (; c[0] <= 'z'; c[0]++)
			 for (; c[1] <= 'z'; c[1]++)
				 for (; c[2] <= 'z'; c[2]++){
					 v = Factory.TERM.createVariable("?" + String.valueOf(c));
					 if (!variables.contains(v))
						 return v;
				 }
		 return v;
	 }
}
