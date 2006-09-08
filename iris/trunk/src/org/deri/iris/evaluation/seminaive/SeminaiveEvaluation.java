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

import org.deri.iris.api.IEDB;
import org.deri.iris.api.evaluation.IEvaluator;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.storage.Relation;
import org.deri.iris.api.evaluation.seminaive.IEvaluationProcedure;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.evaluation.seminaive.model.*;
import org.deri.iris.api.basics.ITuple;
import java.util.Map;
import java.util.Arrays;


/**
 * Algorithm 3.4: Semi-Naive Evaluation of Datalog Equations
 * 
 * INPUT: A collection of rectified datalog rules with EDB predicates r1,...rk and
 * IDB predicates p1,...,pm. Also, a list of relations R1, ..., Rk to serve as 
 * values of the EDB predicates.
 * 
 * OUTPUT: The least fixed point solution to the datalog equations obtained 
 * from these rules.
 * 
 * for i:=1 to m do begin
 *    APi := EVAL(pi, R1,...,Rk, 0,...,0);
 *    Pi := APi;
 * end;
 * repeat
 *    for i:= 1 to m do
 *       AQi := APi; // save old values of APi's
 *    for i := 1 to m do begin
 *       APi := EVAL-INCR(pi,R1,...,Rk,P1,...,Pm,AQ1,...,AQm);
 *       APi := APi - Pi // remove 'new' tuples that actually appeared before
 *    end;
 *    for i := 1 to m do
 *       Pi := Pi U APi; 
 * until APi = 0 for all i, 1 <= i <= m;
 * output Pi's 
 * 
 * @author Paco Garcia, University of Murcia
 * @date 05-sep-2006
 */
public class SeminaiveEvaluation extends GeneralSeminaiveEvaluation{

	SeminaiveEvaluation(IEvaluationProcedure e, IEDB EDB, Map<IRule, ITree> IDB) {
		super(e, EDB, IDB);
	}

	public IRelation<ITuple>[] evaluate() {
		/*
		 * Input: IDB --> pi = ITree; Relevants Rs for each IDB are the leaves
		 * of the ITree
		 */
		IRelation<ITuple>[] P =  new IRelation[IDB.size()];;
		IRelation<ITuple>[] AP = new IRelation[IDB.size()];
		IRelation<ITuple>[] AQ = new IRelation[IDB.size()];
		int i = 0;
		for (IRule head: IDB.keySet())
		{
			int arity = head.getArity();
			P[i] = new Relation(arity);
			AP[i] = new Relation(arity);
			AQ[i] = new Relation(arity);
			i++;
		}
		
		/*
		 * for i := 1 to m do begin
		 *    APi := EVAL(pi, R1,...,Rk,0,...0);
		 *    Pi := APi;
		 * end;
		 */
		i=0;
		for (IRule head: IDB.keySet())
		{
			// EVAL (pi, R1,..., Rk, Q1,..., Qm);
			AP[i] = method.eval(IDB.get(head), EDB, AQ);
			i++;
		}
		copyRelations(AP, P);
		
		/*
		 * repeat
		 *    for i := 1 to m do
		 *       AQ := APi;
		 *    for i := 1 to m do begin
		 *       APi := EVAL-INCR(pi, R1,...,Rk, P1,..., Pm, AQ1,...,AQm);
		 *       APi := APi - Pi;
		 *    end;
		 *    for i := 1 to m do
		 *       Pi := Pi U APi;
		 * until APi = 0 for all i;
		 */		
		for (; !isEmpty(AP);) {
			copyRelations(AP, AQ);
			i = 0;
			for (IRule head: IDB.keySet())
			{
				// EVAL-INCR(pi, R1,...,Rk, P1,..., Pm, AQ1,...,AQm);
				AP[i] = method.eval_incr(IDB.get(head), EDB, P, AQ);
				AP[i].removeAll(Arrays.asList(P[i].toArray()));
				i++;
			}
			addRelations(AP, P);
		}
		return P;
		
	}

	
	/**
	 * 
	 * @param body Rule to evaluate
	 * @param Pbody Tuples for the rule to evaluate (to add in this method)
	 * @param Q Tuples already discovered
	 * @return true if no problems happen; false otherwise
	 */
	private boolean eval(ITree body, IRelation Pbody, IRelation[] Q){
		// TODO Set parameters
		// R1,..., Rk & pi = body;
		// Q1,...,Qm; = Q
		return evaluator.evaluate();
		
		//TODO Get the results 
		//Pbody.addAll(evaluator.evaluate());

	}
	
	/**
	 * 
	 * @param body Rule to evaluate
	 * @param Pbody Tuples for the rule to evaluate (to add in this method)
	 * @param P All the tuples discovered
	 * @param AQ Tuples discovered during the last iteration
	 * @return true if no problems happen; false otherwise
	 */
	private boolean eval_incr(ITree body, IRelation Pbody, IRelation[] P, IRelation[] AQ){
		// TODO Set parameters
		// R1,..., Rk & pi = body;
		// Q1,...,Qm; = Q
		return evaluator.evaluate();
		
		//TODO Get the results 
		//Pbody.addAll(evaluator.evaluate());

	}

}
