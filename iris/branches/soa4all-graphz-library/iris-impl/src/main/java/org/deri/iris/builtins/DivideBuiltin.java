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
 * <p>
 * Represents a multiply operation. In at the evaluation time there must be only one
 * variable be left for computation, otherwise an exception will be thrown.
 * </p>
 * <p>
 * $Id: DivideBuiltin.java,v 1.15 2007-10-12 12:40:58 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.15 $
 */
public class DivideBuiltin extends ArithmeticBuiltin {

	/** The predicate defining this builtin. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"DIVIDE", 3);
	
	/**
	 * Construct a new DivideBuiltin for the specific predicate and terms.
	 * 
	 * @param predicate The predicate of the built-in.
	 * @param terms The terms.
	 * @throws NullPointerException If the predicate or one of the terms is
	 *             <code>null</code>.
	 * @throws IllegalArgumentException If the length of the terms and the arity
	 *             of the predicate do not match.
	 */
	protected DivideBuiltin(IPredicate predicate, ITerm... terms) {
		super(predicate, terms);
	}

	/**
	 * Constructs a builtin. Three terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param t the terms
	 * @throws NullPointerException If the predicate or one of the terms is
	 *             <code>null</code>.
	 * @throws IllegalArgumentException If the length of the terms and the arity
	 *             of the predicate do not match.
	 */
	public DivideBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	protected ITerm computeMissingTerm( int missingTermIndex, ITerm[] terms ) throws EvaluationException
	{
		switch( missingTermIndex )
		{
		case 0:
			return BuiltinHelper.multiply( terms[ 2 ], terms[ 1 ] );
			
		case 1:
			return BuiltinHelper.divide( terms[ 0 ], terms[ 2 ] );
			
		default:
			return BuiltinHelper.divide( terms[ 0 ], terms[ 1 ] );
		}
	}
}
