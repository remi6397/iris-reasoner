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

import java.util.Map;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.evaluation.algebra.IComponent;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.exception.DataModelException;

/**
 * <p>
 * Algorithm 3.3: Evaluation of Datalog Equations
 * </p>
 * INPUT: A collection of datalog rules with EDB predicates r1,...rk and IDB
 * predicates p1,...,pm. A list of relations R1, ..., Rk to serve as values of
 * the EDB predicates.
 * </p>
 * <p>
 * OUTPUT: The least fixed point solution to the datalog equations obtained from
 * these rules.
 * </p>
 * <p>
 * NOTE: Rules need to be rectified, safe and stratified.
 * </p>
 * <p>
 * @author Darko Anicic, DERI Innsbruck
 * @author Paco Garcia, University of Murcia
 * @date 01-sep-2006
 * </p>
 */
public class NaiveEvaluation extends GeneralSeminaiveEvaluation {

	public NaiveEvaluation(IExpressionEvaluator e, IProgram p,
			Map<IPredicate, IComponent> idbMap, Map<IPredicate, IComponent> qMap) {

		super(e, p, idbMap, qMap);
	}

	/**
	 * <p>Algorithm:</p>
	 * <p>
	 * for i:=1 to m do 
	 * 	Pi := 0; 
	 * 	repeat 
	 * 		for i:= 1 to m do 
	 * 			Qi := Pi; // save old values of Pi's 
	 * 		for i := 1 to m do 
	 * 			Pi := EVAL(pi, R1, ..., Rk, Q1,..., Qm);
	 * 	until Pi = Qi for all i, 1 <= i <= m; 
	 * output Pi's
	 * </p>
	 */
	public boolean evaluate() throws DataModelException {

		boolean newTupleAdded = false, cont = true;
		IRelation r = null;

		// Evaluate rules
		for (int i = 1, maxStrat = Complementor.getMaxStratum(this.idbMap
				.keySet()); i <= maxStrat; i++) {

			cont = true;
			while (cont) {
				// Iterating through all predicates of the stratum
				for (final IPredicate pr : Complementor
						.getPredicatesOfStratum(this.idbMap.keySet(), i)) {
					cont = false;
					// EVAL (pi, R1,..., Rk, Q1,..., Qm);
					r = method.evaluate(this.idbMap.get(pr), this.p);
					if (r != null && r.size() > 0) {
						newTupleAdded = this.p.addFacts(pr, r);
						cont = cont || newTupleAdded;
					}
				}
			}
		}

		// Evaluate queries
		for (IPredicate pr : this.queries.keySet()) {
			// EVAL (pi, R1,..., Rk, Q1,..., Qm);
			r = method.evaluate(this.queries.get(pr), this.p);
			if (r != null && r.size() > 0)
				this.getResultSet().getResults().put(pr, r);
		}
		return true;
	}
}
