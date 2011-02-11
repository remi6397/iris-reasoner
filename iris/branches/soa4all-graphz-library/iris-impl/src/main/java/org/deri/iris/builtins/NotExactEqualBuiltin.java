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
import org.deri.iris.factory.Factory;

/**
 * Built-in to compare two terms for exact inequality.
 * Two terms are exactly not equal if they either:
 * a) have different types
 * b) have the same type, but have different values.
 * This comparison respects floating point round-off errors.
 */
public class NotExactEqualBuiltin extends BooleanBuiltin
{
	/**
	 * Constructs a built-in. Two terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * @param terms the terms
	 */
	public NotExactEqualBuiltin( final ITerm... terms )
	{
		super( PREDICATE, terms );
		assert terms.length == 2;
	}

	protected boolean computeResult( ITerm[] terms )
	{
		assert terms.length == 2;
		
		return ! BuiltinHelper.exactlyEqual( terms[ 0 ], terms[ 1 ] );
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = Factory.BASIC.createPredicate( "NOT_EXACT_EQUAL", 2 );
}
