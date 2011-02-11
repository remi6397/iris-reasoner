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
package org.deri.iris.builtins.datatype;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.BooleanBuiltin;

/**
 * Checks whether a term is not of any numeric type (integer, float, double,
 * decimal).
 */
public class IsNotNumericBuiltin extends BooleanBuiltin {
	/**
	 * Constructor.
	 * 
	 * @param terms List of exactly one term.
	 */
	public IsNotNumericBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected boolean computeResult(ITerm[] terms) {
		return !IsNumericBuiltin.isNumeric(terms[0]);
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = org.deri.iris.factory.Factory.BASIC
			.createPredicate("IS_NOT_NUMERIC", 1);
}
