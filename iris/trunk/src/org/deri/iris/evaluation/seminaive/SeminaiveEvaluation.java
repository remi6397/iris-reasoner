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

import static org.deri.iris.factory.Factory.RELATION;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.evaluation.MiscOps;

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
 * <p>
 * $Id$
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 05-sep-2006
 */
public class SeminaiveEvaluation extends GeneralSeminaiveEvaluation {

	public SeminaiveEvaluation(IExpressionEvaluator e, IProgram program) {
		super(e, program);
	}

	public boolean evaluate() {
		/**
		 * Input: IDB --> pi = ITree; Relevants Rs for each IDB are the leaves
		 * of the ITree
		 */
		boolean newTupleAdded = false, cont = true;
		// A realtion with newly derived tuples
		IMixedDatatypeRelation p = null;
		// A map of IDB predicates and their incremental relations
		Map<IPredicate, IMixedDatatypeRelation> aq = new HashMap<IPredicate, IMixedDatatypeRelation>();
		// A set of IDB predicates for a given stratum w.r.t the logig program
		Set<IPredicate> preds = null;
		
		/** Evaluate rules */
		for (int i = 1, maxStrat = MiscOps.getMaxStratum(this.p, this.idbMap.keySet()); 
				i <= maxStrat; i++) {
			preds = MiscOps.getPredicatesOfStratum(this.p, this.idbMap.keySet(), i);
			/**
			 * <p>Algorithm:</p>
			 * <p>
			 * for i := 1 to m do begin 
			 * 	APi := EVAL(pi, R1,...,Rk,0,...0); 
			 * 	Pi := APi; 
			 * end;
			 * </p>
			 */
			for (final IPredicate pr : preds) {
				// EVAL (pi, R1,..., Rk, Q1,..., Qm);
				p = method.evaluate(this.idbMap.get(pr), this.p);
				if(! aq.containsKey(pr)) aq.put(pr, RELATION.getMixedRelation(p.getArity()));
				this.p.addFacts(pr, p);
				aq.get(pr).addAll(this.p.getFacts(pr));
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
				cont = false;
				if(preds.size() == 0) break;
				for (final IPredicate pr : preds) {
					newTupleAdded = false;
					// EVAL-INCR(pi, R1,...,Rk, P1,..., Pm, AQ1,...,AQm);
					p = method.evaluateIncrementally(this.idbMap.get(pr), this.p, aq);
					if(this.p.getFacts(pr) != null && 
							! this.p.getFacts(pr).containsAll(p)){
						
						removeDeducedTuples(p, this.p.getFacts(pr));
						aq.put(pr, p);
						this.p.addFacts(pr, p);
						newTupleAdded = true;
					}else{
						if(aq.get(pr) != null) aq.get(pr).clear(); 
						newTupleAdded = false;
					}
					cont = cont || newTupleAdded;
				}
			}
		}
		return true;
	}
	
	/**<p>
	 * Removes tuples from the last iteration that have already been deduced 
	 * (in the previous iterations)
	 * </p>
	 * <p>
	 * It seems that use of the removeDeducedTuples method is more efficient than: 
	 * p.removeAll(this.p.getFacts(pr));
	 * However this statement needs to be checked more carefully (once tests for performance are done).
	 * </p>
	 * @param r0	Relation containing tuples derived during the last iteration. 
	 * @param r1	Relation containing tuples derived during the previous iteration .
	 */
	private void removeDeducedTuples(IMixedDatatypeRelation r0, IMixedDatatypeRelation r1){
		Iterator<ITuple> it = r0.iterator();
		while(it.hasNext()){
			if(r1.contains(it.next())) it.remove();
		}
	}
}
