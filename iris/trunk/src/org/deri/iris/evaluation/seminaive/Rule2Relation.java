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
	
	private ITree evalRule(IRule r)
	{
		// Temporal variable repository local to the method to store the literals where a variable appears
		java.util.Hashtable<ITerm, ILiteral> variables = new java.util.Hashtable<ITerm, ILiteral>();
		List<ILiteral> builtins = new java.util.LinkedList<ILiteral>();
		
		/* 
		 * Algorithm 3.1: Computing the relation for a Rule Body, Using relational Algebra Operation
		 * Chapter 3. Logic as a data model. 
		 */
		ITree result = ModelFactory.FACTORY.createJoin(null, JoinCondition.EQUALS);
		
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
			result.addComponent(temporalResult); 
		}
		
		// TODO. D. EVAL-RULE(r, R1,...,Rn) = SELECTION_F(E)
		// F conjunction of built-in subgoals appearing.
		if (!builtins.isEmpty())
		{
			// TODO. Depends on the builtins implementation
			ITuple pattern = BasicFactory.getInstance().createTuple(/* pattern terms */);
			ISelection selection = ModelFactory.FACTORY.createSelection(pattern);
		    selection.addComponent(result);
		    result = selection;
		}
		return result;
	}

}
