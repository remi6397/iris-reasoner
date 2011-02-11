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

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;

/**
 * Base class of all boolean built-in predicates.
 * This kind of built-in ...
 * a) can have any arity
 * b) can only be evaluated when all the terms are known (i.e. no unknown variables)
 * c) evaluates to true or false
 */
public abstract class BooleanBuiltin extends AbstractBuiltin
{
	/**
	 * Constructor.
	 * @param predicate The predicate for this built-in.
	 * @param terms The collection of terms, must be length 2 for comparisons.
	 */
	public BooleanBuiltin( IPredicate predicate, final ITerm... terms )
	{
		super( predicate, terms );
	}

	protected ITerm evaluateTerms( ITerm[] terms, int[] variableIndexes )
	{
		assert variableIndexes.length == 0;

		return computeResult( terms ) ? EMPTY_TERM : null;
	}
	
	/**
	 * Compute the result of the comparison.
	 * @param terms The terms
	 * @return The result of the comparison.
	 */
	protected abstract boolean computeResult( ITerm[] terms );
}
