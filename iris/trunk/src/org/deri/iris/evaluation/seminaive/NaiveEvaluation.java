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

import org.deri.iris.api.evaluation.IEvaluator;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.storage.Relation;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.evaluation.seminaive.model.Tree;

import java.util.List;
import java.util.Arrays;


/**
 * Algorithm 3.3: Evaluation of Datalog Equations
 * 
 * INPUT: A collection of datalog rules with EDB predicates r1,...rk and
 * IDB predicates p1,...,pm. A list of relations R1, ..., Rk to serve as 
 * values of the EDB predicates.
 * 
 * OUTPUT: The least fixed point solution to the datalog equations obtained 
 * from these rules.
 * 
 * for i:=1 to m do
 *    Pi := 0;
 * repeat
 *    for i:= 1 to m do
 *       Qi := Pi; // save old values of Pi's
 *    for i := 1 to m do
 *       Pi := EVAL(pi, R1, ..., Rk, Q1,..., Qm);
 * until Pi = Qi for all i, 1 <= i <= m;
 * output Pi's 
 * 
 * @author PACO
 * @date 01-sep-2006
 */
public class NaiveEvaluation {
	private IEvaluator evaluator;
	
	NaiveEvaluation(IEvaluator e) {
		this.evaluator = e;
	}
	
	public IRelation[] evaluation(List<ITree> IDB) {
		/*
		 * Input: IDB --> pi = ITree; Relevants Rs for each IDB are the leaves
		 * of the ITree
		 */
		IRelation[] P = new IRelation[IDB.size()];
		IRelation[] Q = new IRelation[IDB.size()];
		
		
		for (int i = 0; i < P.length; i++)
		{
			// Pi := 0;
			P[i] = new Relation(((Tree) IDB.get(i)).getArity());
			Q[i] = new Relation(((Tree) IDB.get(i)).getArity());
		}
		
		// 1st iteration
		for (int i = 0; i < IDB.size(); i++)
		{
			// EVAL (pi, R1,..., Rk, Q1,..., Qm);
			
			// TODO Set parameters
			// R1,..., Rk & pi = IDB.get(i);
			// Q1,...,Qm; = 0
			evaluator.evaluate();
			
			//TODO Get the results 
			//P[i].addAll(evaluator.evaluate());
		}
		
		for (; !compare(P, Q);) {
			for (int i = 0; i < P.length; i++)
			{
				Q[i].addAll(Arrays.asList(P[i].toArray()));
			}
			for (int i = 0; i < IDB.size(); i++)
			{
				// EVAL (pi, R1,..., Rk, Q1,..., Qm);
				
				// TODO Set parameters
				// R1,..., Rk & pi = IDB.get(i);
				// Q1,...,Qm; = 0
				evaluator.evaluate();
				
				//TODO Get the results 
				//P[i].addAll(evaluator.evaluate());
			}			
		}
		return P;
		
	}

	// TODO
	private boolean compare(IRelation[] P, IRelation[] Q)
	{
		boolean result = true;
		
		for (int i = 0; (i < P.length) && result == true; i++) 
			if (!Q[i].containsAll(Arrays.asList(P))) 
				result = false;
		
		return result;
	}
}
