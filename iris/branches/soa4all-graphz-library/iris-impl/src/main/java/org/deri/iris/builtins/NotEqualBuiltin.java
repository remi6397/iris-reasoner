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
 * Builtin to compare two terms for inequality.
 */
public class NotEqualBuiltin extends BooleanBuiltin {
	/**
	 * Construct a new NotEqualBuiltin for the specific predicate and terms.
	 * 
	 * @param predicate The predicate of the built-in.
	 * @param terms The terms.
	 * @throws NullPointerException If the predicate or one of the terms is
	 *             <code>null</code>.
	 * @throws IllegalArgumentException If the length of the terms and the arity
	 *             of the predicate do not match.
	 */
	protected NotEqualBuiltin(IPredicate predicate, ITerm... terms) {
		super(predicate, terms);
	}

	/**
	 * Constructs a built-in. Two terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param terms the terms
	 * @throws NullPointerException If the predicate or one of the terms is
	 *             <code>null</code>.
	 * @throws IllegalArgumentException If the length of the terms and the arity
	 *             of the predicate do not match.
	 */
	public NotEqualBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected boolean computeResult(ITerm[] terms) {
		return !BuiltinHelper.equal(terms[0], terms[1]);
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = Factory.BASIC.createPredicate(
			"NOT_EQUAL", 2);
}
