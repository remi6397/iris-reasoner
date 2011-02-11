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
package org.deri.iris.builtins;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;

/**
 * Base class of arithmetic built-in predicates.
 * This kind of built-in has:
 * a) can have any arity
 * b) can be evaluated with up to 1 unknown variable
 * c) the unknown variable can be at any position
 * d) if all terms are known at evaluation time, the result is checked to indicate true or false
 */
public abstract class ArithmeticBuiltin extends AbstractBuiltin
{
	/**
	 * Constructor.
	 * @param predicate The predicate that identifies this built-in.
	 * @param terms The terms of the instance. Must be 3.
	 */
	public ArithmeticBuiltin(final IPredicate predicate, final ITerm... terms )
	{
		super( predicate, terms );
	}

	protected ITerm evaluateTerms( ITerm[] terms, int[] variableIndexes ) throws EvaluationException
	{
		assert variableIndexes.length == 0 || variableIndexes.length == 1;
		
		// run the evaluation
		if( variableIndexes.length == 0 )
		{
			int resultIndex = getPredicate().getArity() - 1;
			
			// Perform the operation
			ITerm result = computeMissingTerm( resultIndex, terms );
			
			// return nothing if the operation is invalid
			if( result == null )
				return null;
			
			// Indicate TRUE if the operation is equal to the constant result.
			return testForEquality( terms[ resultIndex ], result ) ? EMPTY_TERM : null;
		}
		else // variableIndexes.length == 1
		{
			assert variableIndexes[ 0 ] >= 0;
			assert variableIndexes[ 0 ] < getPredicate().getArity();
			
			return computeMissingTerm( variableIndexes[ 0 ], terms );
		}
	}
	
	protected boolean testForEquality( ITerm t1, ITerm t2 )
	{
		return BuiltinHelper.equal( t1, t2, getEquivalenceClasses() );
	}

	/**
	 * Compute the missing term when the other two are known.
	 * @param terms The collection of all terms.
	 * @return The computed value.
	 * @throws EvaluationException 
	 */
	protected abstract ITerm computeMissingTerm( int missingTermIndex, ITerm[] terms ) throws EvaluationException;

	public int maxUnknownVariables()
	{
		return 1;
	}
}
