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

import static org.deri.iris.factory.Factory.BASIC;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;

/**
 * Built-in to either:
 * a) compare two terms for exact equality, OR
 * b) assign a constant expression to a variable
 * 
 * Two terms are exactly equal if they:
 * a) have exactly the same type, AND
 * b) have the same value
 * 
 * This comparison respects floating point round-off errors.
 */
public class ExactEqualBuiltin extends ArithmeticBuiltin
{
	/**
	 * Constructor.
	 * @param terms The terms, must be two of these
	 */
	public ExactEqualBuiltin(final ITerm... t)
	{
		super(PREDICATE, t);
	}

	protected ITerm computeMissingTerm( int missingTermIndex, ITerm[] terms )
	{
		return terms[ missingTermIndex == 0 ? 1 : 0 ];
	}
	
	@Override
    protected boolean testForEquality( ITerm t0, ITerm t1 )
    {
		assert t0 != null;
		assert t1 != null;
		
		return BuiltinHelper.exactlyEqual( t0, t1 );
    }
	
	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate( "EXACT_EQUAL", 2 );
}
