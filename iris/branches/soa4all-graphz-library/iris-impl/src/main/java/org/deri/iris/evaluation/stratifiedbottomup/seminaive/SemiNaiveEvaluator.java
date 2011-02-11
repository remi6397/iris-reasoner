/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.evaluation.stratifiedbottomup.seminaive;

import java.util.List;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.evaluation.stratifiedbottomup.IRuleEvaluator;
import org.deri.iris.facts.Facts;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rules.compiler.ICompiledRule;
import org.deri.iris.storage.IRelation;

/**
 * Semi-naive evaluation. see Ullman, Vol. 1
 */
public class SemiNaiveEvaluator implements IRuleEvaluator
{
	public void evaluateRules( List<ICompiledRule> rules, IFacts facts, Configuration configuration ) throws EvaluationException
	{
		IFacts deltas = new Facts( configuration.relationFactory );
		
		// One pass with simple evaluation to generate deltas
		// For each rule in the collection (stratum)
		for (final ICompiledRule rule : rules )
		{
			IRelation delta = rule.evaluate();

			if( delta != null && delta.size() > 0 )
			{
				IPredicate predicate = rule.headPredicate();
				deltas.get( predicate ).addAll( delta );
			}
		}
		
		// Update the facts
		addAll( facts, deltas );
		
		// Now do iterative evaluation (semi-naive)
		boolean newTuples;
		for(;;)
		{
			newTuples = false;
			
			IFacts previousDeltas = deltas;
			
			deltas = new Facts( configuration.relationFactory );
			
			for (final ICompiledRule rule : rules )
			{
				IPredicate predicate = rule.headPredicate();

				IRelation delta = rule.evaluateIteratively( previousDeltas );
				
				// Remove already known tuples
				if( delta != null && delta.size() > 0 )
				{
					IRelation programFacts = facts.get( predicate );
					delta = removeDeducedTuples( predicate, delta, programFacts, configuration );
				}

				if( delta != null && delta.size() > 0)
				{
					newTuples = true;
					deltas.get( predicate ).addAll( delta );
				}
			}
			
			if( ! newTuples )
				break;

			// Iterate new tuples in dP[i] and add to program
			addAll( facts, deltas );
		}
	}

	/**
	 * Add all the tuples from each of the deltas to the target facts.
	 * @param target The facts to be added to.
	 * @param deltas The facts to be added.
	 */
	private static void addAll( IFacts target, IFacts deltas )
	{
		for( IPredicate predicate : deltas.getPredicates() )
			target.get( predicate ).addAll( deltas.get( predicate ) );
	}

	/**
	 * Helper to remove tuples from a delta that are already known or computed.
	 * @param predicate The predicate identifying the relation.
	 * @param delta The deltas produced by the last round of evaluation.
	 * @param programFacts The already known or computed facts.
	 * @return
	 */
	private static IRelation removeDeducedTuples( IPredicate predicate, IRelation delta, IRelation programFacts, Configuration configuration )
	{
		// If there is nothing to take away from, or just nothing to take-away...
		if( delta.size() == 0 || programFacts.size() == 0 )
			return delta;
		
		IRelation result = configuration.relationFactory.createRelation();
		
		for( int t = 0; t < delta.size(); ++ t )
		{
			ITuple tuple = delta.get( t );
			if( ! programFacts.contains( tuple ) )
				result.add( tuple );
		}
		
		return result;
	}
}
