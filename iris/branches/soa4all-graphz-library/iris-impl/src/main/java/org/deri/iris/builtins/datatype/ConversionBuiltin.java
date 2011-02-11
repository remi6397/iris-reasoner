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
import org.deri.iris.builtins.FunctionalBuiltin;

/**
 * Represents a data type conversion function. Implementations of this built-in
 * convert supported data types to a specific data type.
 */
public abstract class ConversionBuiltin extends FunctionalBuiltin {

	/**
	 * Creates a new ConversionBuiltin instance. The number of terms submitted
	 * to this constructor must match the arity of the predicate, which in this
	 * case must always be two.
	 * 
	 * @param predicate The special predicate for this built-in.
	 * @param terms The terms defining the values and variables for this
	 *            built-in.
	 * @throws NullPointerException If the predicate or the terms is {@code
	 *             null}.
	 * @throws NullPointerException If the terms contain {@code null}.
	 * @throws IllegalArgumentException If the length of the terms and the arity
	 *             of the predicate do not match.
	 */
	protected ConversionBuiltin(IPredicate predicate, ITerm... terms) {
		super(predicate, terms);
	}

	protected ITerm computeResult(ITerm[] terms) {
		ITerm term = terms[0];

		try {
			return convert(term);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Converts the given supported data type instance to the specific data type
	 * 
	 * @param terms The term representing the data type to be converted.
	 * @return The result of the conversion or <code>null</code> if the
	 *         specified data type is not compatible.
	 * @throws IllegalArgumentException If the conversion fails for the
	 *             specified value.
	 */
	protected abstract ITerm convert(ITerm term);

}
