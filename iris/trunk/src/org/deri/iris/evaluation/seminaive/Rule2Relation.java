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

import java.util.Set;
import java.util.List;
import java.util.ArrayList;


import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.builtins.IStringEqual;
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
	private Set<IRule> rules = null;
	
	public Rule2Relation(final Set<IRule> r) {
		if (r == null) {
			throw new NullPointerException("Input parameters must not be null");
		}
		this.rules = r;		
	}
	
	public ITree eval()
	{
		ITree result = ModelFactory.FACTORY.createUnion();
		for (IRule r : rules) {
			ITree c = evalRule(r);
			result.addComponent(c);			
		}
		return result;
	}
	
	/**
	 * Algorithm 3.1
	 * @param r Rule Body
	 * @return A tree with the relational algebra operations for the input rule
	 */private ITree evalRule(IRule r)
	{
		// Temporal variable repository local to the method to store the literals where a variable appears
		java.util.Hashtable<ITerm, ILiteral> variables = new java.util.Hashtable<ITerm, ILiteral>();
		List<ILiteral> builtins = new java.util.LinkedList<ILiteral>();
		
		/* 
		 * Algorithm 3.1: Computing the relation for a Rule Body, Using relational Algebra Operation
		 * Chapter 3. Logic as a data model. 
		 */
		
		// TODO. Check whether it is possible a datalog rule with more than one predicate in the head
		ITree result = ModelFactory.FACTORY.createTree(r.getHeadLiteral(0).getPredicate().getPredicateSymbol(), 
				r.getHeadLiteral(0).getPredicate().getArity());
		IJoin globalJoin = ModelFactory.FACTORY.createJoin(null, JoinCondition.EQUALS);
		
		/*
		 * INPUT: Body of rule r = S1,...,Sn with variables X1,...,Xm;
		 * Si = pi(Ai1,..., Aiki) where pi <--> Ri & Ai = variable|constant
		 */
		List<ILiteral> literals = r.getBodyLiterals();
		/*
		 * For each ordinary Si, let Qi be the expression 
		 * PROJECTION_Vi(SELECTION_Fi(Ri))
		 * where Vi = for each variable X in Si exactly one component
		 */
		for (ILiteral l: literals)
		{
			ITree temporalResult ;
			// Preliminary. Check whether it is an ordinary subgoal or one of the type X = a.
			if (! (l.getAtom() instanceof IBuiltInAtom)) 
			{
				
				// A. FOR ORDINARY SUBGOALS (e.g. Si = p(X, b) )
				// 1. Ri
				org.deri.iris.api.evaluation.seminaive.model.IRule leaf = ModelFactory.FACTORY.createRule(
						l.getPredicate().getPredicateSymbol(),
						l.getPredicate().getArity());
				// 2. SELECTION_Fi(Ri)
				// 2.1. Fi (pattern) & Vi (int[]);
				/*
				 * (a) If position k of Si has a constant a, then Fi has the term $k = a
				 * (b) If positions k and l of Si both contain the same variable, then Fi has the term $k = $l
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
						patternTerms.add(null);
						// Store the literal referencing this variable
						variables.put(t, l); 
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
					// If there are no terms in Fi (e.g. Si = p(X,Y)) => Qi = Ri
					temporalResult = leaf;
				}
			
			} else { // Built-in expression
				builtins.add(l);
			//TODO. B. Variable X not among ordinary subgoal (eg. 'U = a' or 'U = W')
			//1. Compute Dx
				if (l instanceof IStringEqual) {
			// (a) If Y = a, Dx = {a}
					//TODO. set the name of the relation correctly. Depends on the builtins implementation
					temporalResult = ModelFactory.FACTORY.createRule("Da",1);
				} else /* if (l instanceof 'other built in expression' */{
			// (b) If X = Y & Si = r(...,Yj,...) --> Dx = PROJECTION_j(Ri)
					ILiteral l_v = variables.get(l);
					//if (l_v != null) {
					org.deri.iris.api.evaluation.seminaive.model.IRule leaf = ModelFactory.FACTORY.createRule(
							l_v.getPredicate().getPredicateSymbol(),
							l_v.getPredicate().getArity());
					int[] j = new int[leaf.getArity()];
					List<ITerm> terms = l_v.getAtom().getTuple().getTerms();
					
					for (int i = 0; i < terms.size(); i++)
					{
						ITerm t = terms.get(i);
						// TODO. Find the index - Depends on the builtins implementation
//						if ((t instanceof org.deri.iris.terms.Variable) && 
//								((org.deri.iris.terms.Variable)t).compareTo("variable in this literal"))
//							j[i] = 0;
//						else
//							j[i] = -1;
					}
					
					temporalResult = ModelFactory.FACTORY.createProjection(j);
					temporalResult.addComponent(leaf);
					//} else {
						// TODO What to do in case an error like this occur?
						// If the rule is safe this cannot happen
					//}
						
				}
			
			}
			
			// C. Natural join of all the things generated (E)
			globalJoin.addComponent(temporalResult); 
		}
		
		// TODO. D. EVAL-RULE(r, R1,...,Rn) = SELECTION_F(E)
		// F conjunction of built-in subgoals appearing.
		if (!builtins.isEmpty())
		{
			// TODO. Depends on the builtins implementation
			ITuple pattern = BasicFactory.getInstance().createTuple(/* pattern terms */);
			ISelection selection = ModelFactory.FACTORY.createSelection(pattern);
		    selection.addComponent(globalJoin);
		    result.addComponent(selection);
		} else
			result.addComponent(globalJoin);
		return result;
	}

}
