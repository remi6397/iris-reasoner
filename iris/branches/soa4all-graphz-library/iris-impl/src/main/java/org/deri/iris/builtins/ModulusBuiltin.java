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

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;

/**
 * Represents a modulus operation. At evaluation time there must be either:
 * a) 1 unknown variable for computation, OR
 * b) no unknown variables, in which case the evaluation is just a check that term0 % term1 = term2
 */
public class ModulusBuiltin extends ArithmeticBuiltin
{
	/**
	 * Construct a new ModulusBuiltin for the specific predicate and terms.
	 * 
	 * @param predicate The predicate of the built-in.
	 * @param terms The terms.
	 * @throws NullPointerException If the predicate or one of the terms is
	 *             <code>null</code>.
	 * @throws IllegalArgumentException If the length of the terms and the arity
	 *             of the predicate do not match.
	 */
	protected ModulusBuiltin(IPredicate predicate, ITerm... terms) {
		super(predicate, terms);
	}
	
	/**
	 * Constructor.
	 * @param terms the terms. There must always be 3 terms.
	 */
	public ModulusBuiltin( final ITerm... terms )
	{
		super(PREDICATE, terms );
	}

	protected ITerm computeMissingTerm( int missingTermIndex, ITerm[] terms ) throws EvaluationException
	{
		switch( missingTermIndex )
		{
		case 0:
			return terms[ 2 ]; // +n x terms[ 1 ], where n >= 0
			
		case 1:
			if( BuiltinHelper.less( terms[ 2 ], terms[ 0 ] ) )
				return BuiltinHelper.subtract( terms[ 0 ], terms[ 2 ] );
			
			if( BuiltinHelper.equal( terms[ 2 ], terms[ 0 ] ) )
				return BuiltinHelper.increment( terms[ 2 ] );
			
			// x % y = z, does not make sense when when x < z
			return null;
			
		default:
			return BuiltinHelper.modulus( terms[ 0 ], terms[ 1 ] );
		}
	}
	
	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate( "MODULUS", 3 );
}
