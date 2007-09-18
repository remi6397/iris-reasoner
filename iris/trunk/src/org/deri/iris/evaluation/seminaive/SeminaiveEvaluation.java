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

	/**
	 * Algorithm from Ullman, Principles of Database and Knowledge-base Systems, page 127
	 */
	public boolean evaluate()
	{
		// Evaluate all the rules one stratum at a time
		for (int i = 1, maxStrat = MiscOps.getMaxStratum(mProgram, this.idbMap.keySet()); 
				i <= maxStrat; i++)
		{
			// A map of IDB predicates and their incremental relations
			Map<IPredicate, IMixedDatatypeRelation> dP = new HashMap<IPredicate, IMixedDatatypeRelation>();
			
			// All head predicates of each rule
			Set<IPredicate> preds = MiscOps.getPredicatesOfStratum(mProgram, this.idbMap.keySet(), i);
			
			/**
			 * <p>Algorithm:</p>
			 * <p>
			 * 		for i := 1 to m 
			 * 			dPi := EVAL(pi, R1,...,Rk,0,...0); 
			 * 			Pi := dPi; 
			 * </p>
			 */
			for( final IPredicate pr : preds )
			{
				IMixedDatatypeRelation dPi = method.evaluate( idbMap.get( pr ), mProgram );
				
				//dP.put( pr, RELATION.getMixedRelation( dPi.getArity() ) );
				
				/*
				dP.put( pr, dPi ); // On its own, this line breaks testTransitiveClosure()
				
				IMixedDatatypeRelation programFacts = mProgram.getFacts(pr);
				programFacts.addAll( dPi );
				dPi.addAll( programFacts );
				*/
				
				mProgram.addFacts( pr, dPi );
			}
			/**
			 * <p>Algorithm:</p>
			 * <p>
			 * 	repeat 
			 * 		for i := 1 to m 
			 * 			dQi := dPi; 
			 * 		for i := 1 to m 
			 * 			dPi := EVAL-INCR(pi, R1,...,Rk, P1,..., Pm, dQ1,...,dQm); 
			 * 			dPi := dPi - Pi; 
			 * 		for i := 1 to m 
			 * 			Pi := Pi U dPi; 
			 * 	until dPi = 0 for all i;
			 * </p>
			 */
			boolean newTuples = true;
			while (newTuples)
			{
				newTuples = false;
				
				for (final IPredicate pr : preds)
				{
					IMixedDatatypeRelation dPi = method.evaluateIncrementally(idbMap.get(pr), mProgram, dP);
					
					// Anything to do?
					if ( dPi.size() > 0 )
					{
						IMixedDatatypeRelation programFacts = mProgram.getFacts(pr);
						if(programFacts != null )
						{
							if ( ! programFacts.containsAll(dPi) )
							{
								removeDeducedTuples(dPi, programFacts);
								dP.put(pr, dPi);
								newTuples = true;
							}
						}
					}
					if ( ! newTuples )
						dP.remove( pr );
//						dP.get( pr ).clear();
				}

				// Iterate new tuples in dP[i] and add to program
				Iterator<Map.Entry<IPredicate, IMixedDatatypeRelation>> iterdP = dP.entrySet().iterator();
				while( iterdP.hasNext() )
				{
					Map.Entry<IPredicate, IMixedDatatypeRelation> e = iterdP.next();
					mProgram.addFacts( e.getKey(), e.getValue() );
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
