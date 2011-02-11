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
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.INMTOKEN;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link INMTOKEN} instances. The following data types are
 * supported:
 * <ul>
 * <li>Numeric</li>
 * <li>String</li>
 * </ul>
 */
public class ToNMTokenBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_NMTOKEN", 2);

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
	public ToNMTokenBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected INMTOKEN convert(ITerm term) {
		if (term instanceof INMTOKEN) {
			return (INMTOKEN) term;
		} else if (term instanceof INumericTerm) {
			return toNMTOKEN((INumericTerm) term);
		} else if (term instanceof IStringTerm) {
			return toNMTOKEN((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a {@link INumericTerm} term to a {@link INMTOKEN} term.
	 * 
	 * @param term The {@link INumericTerm} term to be converted.
	 * @return A new {@link INMTOKEN} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static INMTOKEN toNMTOKEN(INumericTerm term) {
		return CONCRETE.createNMTOKEN(term.toCanonicalString());
	}

	/**
	 * Converts a {@link IStringTerm} term to a {@link INMTOKEN} term.
	 * 
	 * @param term The {@link IStringTerm} term to be converted.
	 * @return A new {@link INMTOKEN} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static INMTOKEN toNMTOKEN(IStringTerm term) {
		return CONCRETE.createNMTOKEN(term.toCanonicalString());
	}

}
