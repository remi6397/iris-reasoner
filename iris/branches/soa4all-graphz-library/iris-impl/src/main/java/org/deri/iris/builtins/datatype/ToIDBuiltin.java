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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IID;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link IID} instances. The following data types are
 * supported:
 * <ul>
 * <li>String</li>
 * </ul>
 */
public class ToIDBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate("TO_ID",
			2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms An array of terms, where first one is the term to convert
	 *            and the last term represents the result of this data type
	 *            conversion.
	 * @throws NullPointerException If <code>terms</code> is <code>null</code>.
	 * @throws NullPointerException If the terms contain a <code>null</code>
	 *             value.
	 * @throws IllegalArgumentException If the length of the terms and the arity
	 *             of the predicate do not match.
	 */
	public ToIDBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected IID convert(ITerm term) {
		if (term instanceof IID) {
			return (IID) term;
		} else if (term instanceof IStringTerm) {
			return toID((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a {@link IStringTerm} term to a {@link IID} term.
	 * 
	 * @param term The {@link IStringTerm} term to be converted.
	 * @return A new {@link IID} term representing the result of the conversion,
	 *         or <code>null</code> if the conversion fails.
	 */
	public static IID toID(IStringTerm term) {
		return CONCRETE.createID(term.toCanonicalString());
	}

}
