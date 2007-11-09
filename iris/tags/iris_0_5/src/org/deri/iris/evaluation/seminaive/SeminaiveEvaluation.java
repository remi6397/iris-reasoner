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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.storage.IMixedDatatypeRelation;

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
	 * Algorithm from Ullman, Principles of Database and Knowledge-base Systems,
	 * page 127, adapted for strata of rules instead of strata of predicates.
	 */
	public boolean evaluate()
	{
		// Evaluate all the rules one stratum at a time
		for( int stratum = 0; stratum < mProgram.getRuleStrataSize(); ++stratum )
		{
			// A map of IDB predicates and their incremental relations
			//Map<IPredicate, IMixedDatatypeRelation> dP = new HashMap<IPredicate, IMixedDatatypeRelation>();
			IncrementalRelations dP = new IncrementalRelations();
			
			// All head predicates of each rule
			Collection<IRule> rules = mProgram.getRulesOfStratum( stratum );
			
			for( final IRule rule : rules )
			{
				IPredicate predicate = rule.getHead().get( 0 ).getAtom().getPredicate();
				IMixedDatatypeRelation dPi = method.evaluate( idbMap.get( rule ), mProgram );
				
				if( ! dPi.isEmpty() )
					dP.add( predicate, dPi );
				
				mProgram.addFacts( predicate, dPi );
			}

			boolean newTuples;
			for(;;)
			{
				newTuples = false;
				
				IncrementalRelations dQ = dP;
				
				dP = new IncrementalRelations();
				
				for (final IRule rule : rules)
				{
					IPredicate predicate = rule.getHead().get( 0 ).getAtom().getPredicate();
					
					IMixedDatatypeRelation dPi = method.evaluateIncrementally(idbMap.get(rule), mProgram, dQ.getRelations() );
					
					// Anything to do?
					if ( dPi.size() > 0 )
					{
						IMixedDatatypeRelation programFacts = mProgram.getFacts(predicate);
						if( programFacts == null )
						{
							newTuples = true;
						}
						else
						{
							if ( programFacts.containsAll(dPi) )
								dPi.clear();
							else
								removeDeducedTuples(dPi, programFacts);
						}
					}

					if( ! dPi.isEmpty() )
					{
						newTuples = true;
					
						dP.add( predicate, dPi );
					}
				}
				
				if( ! newTuples )
					break;

				// Iterate new tuples in dP[i] and add to program
				for( Map.Entry<IPredicate, IMixedDatatypeRelation> e : dP.getRelations().entrySet() )
				{
					mProgram.addFacts( e.getKey(), e.getValue() );
				}
			}
		}

		return true;
	}

	/**
	 * Helper for holding the incremental tuples from each iteration.
	 */
	private static class IncrementalRelations
	{
		void add( IPredicate predicate, IMixedDatatypeRelation relation )
		{
			IMixedDatatypeRelation existing_dPi = mRelations.get( predicate );
			if( existing_dPi == null )
				mRelations.put( predicate, relation );
			else
				existing_dPi.addAll( relation );
		}
		
		Map<IPredicate, IMixedDatatypeRelation> getRelations()
		{
			return mRelations;
		}
		
		void clear()
		{
			mRelations.clear();
		}
		
		final Map<IPredicate, IMixedDatatypeRelation> mRelations = new HashMap<IPredicate, IMixedDatatypeRelation>();
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
