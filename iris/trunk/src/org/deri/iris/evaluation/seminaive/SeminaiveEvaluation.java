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

import org.deri.iris.api.IEDB;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.evaluation.seminaive.IEvaluationProcedure;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.storage.IRelation;

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
 * for i:=1 to m do begin APi := EVAL(pi, R1,...,Rk, 0,...,0); Pi := APi; end;
 * repeat for i:= 1 to m do AQi := APi; // save old values of APi's for i := 1
 * to m do begin APi := EVAL-INCR(pi,R1,...,Rk,P1,...,Pm,AQ1,...,AQm); APi :=
 * APi - Pi // remove 'new' tuples that actually appeared before end; for i := 1
 * to m do Pi := Pi U APi; until APi = 0 for all i, 1 <= i <= m; output Pi's
 * 
 * @author Paco Garcia, University of Murcia
 * @author Darko Anicic, DERI Innsbruck
 * @date 05-sep-2006
 */
public class SeminaiveEvaluation extends GeneralSeminaiveEvaluation {

	SeminaiveEvaluation(IEvaluationProcedure e, IEDB edb,
			Map<IPredicate, ITree> idb, Map<IPredicate, ITree> q) {
		super(e, edb, idb, q);
	}

	public boolean evaluate() {
		/**
		 * Input: IDB --> pi = ITree; Relevants Rs for each IDB are the leaves
		 * of the ITree
		 */
		boolean newTupleAdded = false, cont = true;
		IRelation p = null;
		Map<IPredicate, IRelation> aq = new HashMap<IPredicate, IRelation>();

		// Evaluate rules
		for (int i = 1, maxStrat = Complementor
				.getMaxStratum(this.idb.keySet()); i <= maxStrat; i++) {

			cont = true;

			/**
			 * for i := 1 to m do begin APi := EVAL(pi, R1,...,Rk,0,...0); Pi :=
			 * APi; end;
			 */
			for (final IPredicate pr : Complementor.getPredicatesOfStratum(
					this.idb.keySet(), i)) {
				// for (IPredicate pr : this.idb.keySet()) {
				// EVAL (pi, R1,..., Rk, Q1,..., Qm);
				p = method.eval(this.idb.get(pr), this.edb);
				if (p != null && p.size() > 0) {
					aq.put(pr, p);
				}
			}
			/**
			 * repeat for i := 1 to m do AQ := APi; for i := 1 to m do begin APi :=
			 * EVAL-INCR(pi, R1,...,Rk, P1,..., Pm, AQ1,...,AQm); APi := APi -
			 * Pi; end; for i := 1 to m do Pi := Pi U APi; until APi = 0 for all
			 * i;
			 */
			while (cont) {
				// for (IPredicate pr : this.idb.keySet()) {
				for (final IPredicate pr : Complementor.getPredicatesOfStratum(
						this.idb.keySet(), i)) {
					cont = false;
					// EVAL-INCR(pi, R1,...,Rk, P1,..., Pm, AQ1,...,AQm);
					p = method.eval_incr(this.idb.get(pr), this.edb, aq);
					if (p != null && p.size() > 0) {
						p.removeAll(this.edb.getFacts(pr));
						newTupleAdded = this.edb.addFacts(pr, p);
						cont = cont || newTupleAdded;
					}
				}
			}
		}
		// Evaluate queries
		for (IPredicate pr : this.queries.keySet()) {
			// EVAL (pi, R1,..., Rk, Q1,..., Qm);
			p = method.eval(this.queries.get(pr), this.edb);
			if (p != null && p.size() > 0)
				this.getResultSet().getResults().put(pr, p);
		}
		return true;
	}
}
