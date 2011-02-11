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
import org.deri.iris.api.terms.concrete.IBooleanTerm;
import org.deri.iris.api.terms.concrete.IIntegerTerm;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to Integer instances. The following data types are supported:
 * <ul>
 * <li>Decimal</li>
 * <li>Double</li>
 * <li>Float</li>
 * <li>Integer</li>
 * <li>String</li>
 * </ul>
 */
public class ToIntegerBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_INTEGER", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms The term representing the data type instance to be
	 *            converted.
	 */
	public ToIntegerBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected IIntegerTerm convert(ITerm term) {
		if (term instanceof IIntegerTerm) {
			return (IIntegerTerm) term;
		} else if (term instanceof IBooleanTerm) {
			return toInteger((IBooleanTerm) term);
		} else if (term instanceof INumericTerm) {
			return toInteger((INumericTerm) term);
		} else if (term instanceof IStringTerm) {
			return toInteger((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a Boolean term to an Integer term. A Boolean term representing
	 * the value "True" is converted to an Integer term representing "1". A
	 * Boolean term representing the value "False" is converted to an Integer
	 * term representing "0".
	 * 
	 * @param term The Boolean term to be converted.
	 * @return A new Integer term representing the result of the conversion.
	 */
	public static IIntegerTerm toInteger(IBooleanTerm term) {
		if (term.getValue()) {
			return CONCRETE.createInteger(1);
		}

		return CONCRETE.createInteger(0);
	}

	/**
	 * Converts a Numeric term to an Integer term.
	 * 
	 * @param term The Numeric term to be converted.
	 * @return A new Integer term representing the result of the conversion.
	 */
	public static IIntegerTerm toInteger(INumericTerm term) {
		return CONCRETE.createInteger(term.getValue().toBigInteger());
	}

	/**
	 * Converts a String term to an Integer term.
	 * 
	 * @param term The String term to be converted.
	 * @return A new Integer term representing the result of the conversion, or
	 *         <code>null</code> if the conversion fails.
	 */
	public static IIntegerTerm toInteger(IStringTerm term) {
		try {
			String string = term.getValue();

			int indexOfDot = string.indexOf(".");
			if (indexOfDot > -1) {
				string = string.substring(0, indexOfDot);
			}

			return CONCRETE.createInteger(new BigInteger(string));
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
