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

import java.util.HashMap;
import java.util.Map;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.storage.Relation;

/**
 * Algorithm 3.4: Semi-Naive Evaluation of Datalog Equations
 * 
 * INPUT: A collection of rectified datalog rules with EDB predicates r1,...rk
 * and IDB predicates p1,...,pm. Also, a list of relations R1, ..., Rk to serve
 * as values of the EDB predicates.
 * 
 * OUTPUT: The least fixed point solution to the datalog equations obtained from
 * these rules.
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 05-sep-2006
 */
public class SeminaiveEvaluation extends GeneralSeminaiveEvaluation {

	public SeminaiveEvaluation(IExpressionEvaluator e, IProgram p) {
		super(e, p);
	}

	public boolean evaluate() {
		/**
		 * Input: IDB --> pi = ITree; Relevants Rs for each IDB are the leaves
		 * of the ITree
		 */
		boolean newTupleAdded = false, cont = true;
		IRelation p = null;
		Map<IPredicate, IRelation> aq = new HashMap<IPredicate, IRelation>();
		IRelation tempRel = null;
		
		/** Evaluate rules */
		for (int i = 1, maxStrat = Complementor
				.getMaxStratum(this.idbMap.keySet()); i <= maxStrat; i++) {

			/**
			 * <p>Algorithm:</p>
			 * <p>
			 * for i := 1 to m do begin 
			 * 	APi := EVAL(pi, R1,...,Rk,0,...0); 
			 * 	Pi := APi; 
			 * end;
			 * </p>
			 */
			for (final IPredicate pr : Complementor.getPredicatesOfStratum(
					this.idbMap.keySet(), i)) {
				
				// EVAL (pi, R1,..., Rk, Q1,..., Qm);
				p = method.evaluate(this.idbMap.get(pr), this.p);
				if(! this.p.getFacts(pr).containsAll(p)){
					aq.put(pr, p);
					this.p.addFacts(pr, p);
				}
			}
			/**
			 * <p>Algorithm:</p>
			 * <p>
			 * repeat 
			 * 	for i := 1 to m do 
			 * 		AQ := APi; 
			 * 	for i := 1 to m do begin 
			 * 		APi := EVAL-INCR(pi, R1,...,Rk, P1,..., Pm, AQ1,...,AQm); 
			 * 		APi := APi - Pi; 
			 * 	end; 
			 * 	for i := 1 to m do 
			 * 		Pi := Pi U APi; 
			 * until APi = 0 for all i;
			 * </p>
			 */
			cont = true;
			while (cont) {
				for (final IPredicate pr : Complementor.getPredicatesOfStratum(
						this.idbMap.keySet(), i)) {
					
					cont = false;
					// EVAL-INCR(pi, R1,...,Rk, P1,..., Pm, AQ1,...,AQm);
					p = method.evaluateIncrementally(this.idbMap.get(pr), this.p, aq);
					/*if (p != null && p.size() > 0) {
						p.removeAll(this.p.getFacts(pr));
						aq.put(pr, p);
						newTupleAdded = this.p.addFacts(pr, p);
						cont = cont || newTupleAdded;
					}*/
					if(! this.p.getFacts(pr).containsAll(p)){
						tempRel = new Relation(p.getArity());
						tempRel.addAll(p);
						tempRel.removeAll(this.p.getFacts(pr));
						aq.put(pr, tempRel);
						this.p.addFacts(pr, tempRel);
						cont = true;
					}else{
						newTupleAdded = false;
					}
					cont = cont || newTupleAdded;
				}
			}
			for (final IPredicate pr : Complementor.getPredicatesOfStratum(
					this.idbMap.keySet(), i)) {
				
				// EVAL (pi, R1,..., Rk, Q1,..., Qm);
				p = method.evaluate(this.idbMap.get(pr), this.p);
				if(! this.p.getFacts(pr).containsAll(p)){
					aq.put(pr, p);
					this.p.addFacts(pr, p);
				}
			}
		}
		return true;
	}
}
