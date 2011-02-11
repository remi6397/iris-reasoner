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
 * Represents a subtract operation. At evaluation time there must be only one
 * unknown variable left for computation, otherwise an exception will be thrown.
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.15 $
 */
public class SubtractBuiltin extends ArithmeticBuiltin {
	/**
	 * Construct a new SubtractBuiltin for the specific predicate and terms.
	 * 
	 * @param predicate The predicate of the built-in.
	 * @param terms The terms.
	 * @throws NullPointerException If the predicate or one of the terms is
	 *             <code>null</code>.
	 * @throws IllegalArgumentException If the length of the terms and the arity
	 *             of the predicate do not match.
	 */
	protected SubtractBuiltin(IPredicate predicate, ITerm... terms) {
		super(predicate, terms);
	}

	/**
	 * Constructs a built-in. Three terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param t the terms
	 * @throws NullPointerException If the predicate or one of the terms is
	 *             <code>null</code>.
	 * @throws IllegalArgumentException If the length of the terms and the arity
	 *             of the predicate do not match.
	 */
	public SubtractBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
		switch (missingTermIndex) {
		case 0:
			return BuiltinHelper.add(terms[2], terms[1]);

		case 1:
			return BuiltinHelper.subtract(terms[0], terms[2]);

		default:
			return BuiltinHelper.subtract(terms[0], terms[1]);
		}
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"SUBTRACT", 3);
}
