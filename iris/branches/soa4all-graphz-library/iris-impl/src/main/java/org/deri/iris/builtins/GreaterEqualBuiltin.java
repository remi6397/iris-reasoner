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
 * <p>
 * Built-in to compare two terms and determine which one is bigger or if they are
 * equal.
 * </p>
 * <p>
 * $Id: GreaterEqualBuiltin.java,v 1.15 2007-10-12 12:40:58 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.15 $
 */
public class GreaterEqualBuiltin extends BooleanBuiltin
{
	/**
	 * Construct a new GreaterEqualBuiltin for the specific predicate and terms.
	 * 
	 * @param predicate The predicate of the built-in.
	 * @param terms The terms.
	 * @throws NullPointerException If the predicate or one of the terms is
	 *             <code>null</code>.
	 * @throws IllegalArgumentException If the length of the terms and the arity
	 *             of the predicate do not match.
	 */
	protected GreaterEqualBuiltin(IPredicate predicate, ITerm... terms) {
		super(predicate, terms);
	}
	
	/**
	 * Constructs a built-in. Two terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param t the terms
	 * @throws NullPointerException if one of the terms is null
	 * @throws IllegalArgumentException if the number of terms submitted is
	 * not 2
	 * @throws NullPointerException if t is <code>null</code>
	 */
	public GreaterEqualBuiltin(final ITerm... t)
	{
		super(PREDICATE, t);
	}

	protected boolean computeResult( ITerm[] terms )
	{
		return BuiltinHelper.lessEquals( terms[ 1 ], terms[ 0 ] );
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = Factory.BASIC.createPredicate( "GREATER_EQUAL", 2);
}
