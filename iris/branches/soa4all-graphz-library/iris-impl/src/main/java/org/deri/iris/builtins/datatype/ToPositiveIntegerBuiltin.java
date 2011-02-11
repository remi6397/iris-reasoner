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

import java.math.BigInteger;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IPositiveInteger;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link IPositiveInteger} instances. The following data
 * types are supported:
 * <ul>
 * <li>Numeric</li>
 * <li>String</li>
 * </ul>
 */
public class ToPositiveIntegerBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_POSITIVEINTEGER", 2);

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
	public ToPositiveIntegerBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IPositiveInteger) {
			return (IPositiveInteger) term;
		} else if (term instanceof INumericTerm) {
			return toPositiveInteger((INumericTerm) term);
		} else if (term instanceof IStringTerm) {
			return toPositiveInteger((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a {@link INumericTerm} term to a {@link IPositiveInteger} term.
	 * 
	 * @param term The {@link INumericTerm} term to be converted.
	 * @return A new {@link IPositiveInteger} term representing the result of
	 *         the conversion, or <code>null</code> if the conversion fails.
	 */
	public static IPositiveInteger toPositiveInteger(INumericTerm term) {
		try {
			return CONCRETE.createPositiveInteger(term.getValue()
					.toBigInteger());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Converts a {@link IStringTerm} term to a {@link IPositiveInteger} term.
	 * 
	 * @param term The {@link IStringTerm} term to be converted.
	 * @return A new {@link IPositiveInteger} term representing the result of
	 *         the conversion, or <code>null</code> if the conversion fails.
	 */
	public static IPositiveInteger toPositiveInteger(IStringTerm term) {
		try {
			String string = term.getValue();

			int indexOfDot = string.indexOf(".");
			if (indexOfDot > -1) {
				string = string.substring(0, indexOfDot);
			}

			return CONCRETE.createPositiveInteger(new BigInteger(string));
		} catch (NumberFormatException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
