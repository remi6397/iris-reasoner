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
import org.deri.iris.api.terms.concrete.IBooleanTerm;
import org.deri.iris.api.terms.concrete.IUnsignedByte;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link IUnsignedByte} instances. The following data types
 * are supported:
 * <ul>
 * <li>Boolean</li>
 * <li>Numeric</li>
 * <li>String</li>
 * </ul>
 */
public class ToUnsignedByteBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_UNSIGNEDBYTE", 2);

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
	public ToUnsignedByteBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected IUnsignedByte convert(ITerm term) {
		if (term instanceof IUnsignedByte) {
			return (IUnsignedByte) term;
		} else if (term instanceof IBooleanTerm) {
			return toUnsignedByte((IBooleanTerm) term);
		} else if (term instanceof INumericTerm) {
			return toUnsignedByte((INumericTerm) term);
		} else if (term instanceof IStringTerm) {
			return toUnsignedByte((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a {@link IBooleanTerm} term to a {@link IUnsignedByte} term. A
	 * {@link IBooleanTerm} term representing the value "True" is converted to a
	 * {@link IUnsignedByte} term representing "1". A {@link IBooleanTerm} term
	 * representing the value "False" is converted to a {@link IUnsignedByte}
	 * term representing "0".
	 * 
	 * @param term The {@link IBooleanTerm} term to be converted.
	 * @return A new {@link IUnsignedByte} term representing the result of the
	 *         conversion.
	 */
	public static IUnsignedByte toUnsignedByte(IBooleanTerm term) {
		if (term.getValue()) {
			return CONCRETE.createUnsignedByte((short) 1);
		}

		return CONCRETE.createUnsignedByte((short) 0);
	}

	/**
	 * Converts a {@link INumericTerm} term to a {@link IUnsignedByte} term.
	 * 
	 * @param term The {@link INumericTerm} term to be converted.
	 * @return A new {@link IUnsignedByte} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static IUnsignedByte toUnsignedByte(INumericTerm term) {
		try {
			return CONCRETE.createUnsignedByte(term.getValue().toBigInteger()
					.shortValue());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Converts a {@link IStringTerm} term to a {@link IUnsignedByte} term.
	 * 
	 * @param term The {@link IStringTerm} term to be converted.
	 * @return A new {@link IUnsignedByte} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static IUnsignedByte toUnsignedByte(IStringTerm term) {
		try {
			String string = term.getValue();

			int indexOfDot = string.indexOf(".");
			if (indexOfDot > -1) {
				string = string.substring(0, indexOfDot);
			}

			return CONCRETE.createUnsignedByte(Short.valueOf(string));
		} catch (NumberFormatException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
