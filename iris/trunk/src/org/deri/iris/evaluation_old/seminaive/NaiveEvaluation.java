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
package org.deri.iris.evaluation_old.seminaive;

import java.util.Collection;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.evaluation_old.algebra.IExpressionEvaluator;
import org.deri.iris.api.storage_old.IMixedDatatypeRelation;

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
 * @date 01-sep-2006
 * </p>
 */
public class NaiveEvaluation extends GeneralSeminaiveEvaluation {

	public NaiveEvaluation(IExpressionEvaluator e, IProgram p) {

		super(e, p);
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
	public boolean evaluate() {

		boolean newTupleAdded = false, cont = true;
		IMixedDatatypeRelation r = null;
		
		// Evaluate rules
		for( int stratum = 0; stratum < mProgram.getRuleStrataSize(); ++stratum )
		{

			Collection<IRule> rules = mProgram.getRulesOfStratum( stratum );
			cont = true;
			while (cont)
			{
				cont = false;
				// Iterating through all predicates of the stratum
				for (final IRule rule : rules )
				{
					newTupleAdded = false;
					// EVAL (pi, R1,..., Rk, Q1,..., Qm);
					r = method.evaluate(this.idbMap.get(rule), mProgram);
					if (r != null && r.size() > 0)
					{
						newTupleAdded = mProgram.addFacts(rule.getHead().get( 0 ).getAtom().getPredicate(), r);
						cont = cont || newTupleAdded;
					}
				}
			}
		}
		return true;
	}
}
