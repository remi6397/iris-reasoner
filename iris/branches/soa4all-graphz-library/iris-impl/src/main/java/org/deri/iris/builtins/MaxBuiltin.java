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
 * <p>
 * Represents an x = max(y,z) operation. At evaluation time only the result can be unknown.
 * </p>
 * <p>
 * The syntax in Datalog will be, e.g.
 * p(?max) :- q(?x,?y), MAX(?x, ?y, ?max).
 * </p>
 */
public class MaxBuiltin extends ArithmeticBuiltin
{
	/**
	 * Constructor. Three terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param t the terms
	 */
	public MaxBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);

		if( terms.length != 3 )
			throw new IllegalArgumentException( getClass().getSimpleName() + ": Constructor requires exactly three parameters" );
	}

	protected ITerm computeMissingTerm( int missingTermIndex, ITerm[] terms )
	{
		switch( missingTermIndex )
		{
		case 0:
			return null;
			
		case 1:
			return null;
			
		default:
			if( BuiltinHelper.less( terms[ 0 ], terms[ 1 ] ) )
				return terms[ 1 ];
			else
				return terms[ 0 ];
		}
	}
	
	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate( "MAX", 3 );
}
