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
import org.deri.iris.api.terms.IConcreteTerm;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IPlainLiteral;
import org.deri.iris.api.terms.concrete.IXMLLiteral;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to PlainLiteral instances. The following data types are
 * supported:
 * <ul>
 * <li>String</li>
 * <li>All data types for which casting to string is supported</li>
 * </ul>
 */
public class ToPlainLiteralBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_TEXT", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms The term representing the data type instance to be
	 *            converted.
	 */
	public ToPlainLiteralBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		return toPlainLiteral(term);
	}

	/**
	 * Converts a XMLLiteral term to a PlainLiteral term.
	 * 
	 * @param term The XMLLiteral term to be converted.
	 * @return A new PlainLiteral term representing the result of the
	 *         conversion.
	 */
	public static IPlainLiteral toPlainLiteral(IXMLLiteral term) {
		return CONCRETE.createPlainLiteral(term.getString(), term.getLang());
	}

	/**
	 * Converts a String term to a PlainLiteral term.
	 * 
	 * @param term The String term to be converted.
	 * @return A new PlainLiteral term representing the result of the
	 *         conversion.
	 */
	public static IPlainLiteral toPlainLiteral(IStringTerm term) {
		String value = term.getValue();
		return CONCRETE.createPlainLiteral(value);
	}

	/**
	 * Converts a constant term to a PlainLiteral term. For data types other
	 * than Text and XMLLiteral, the <code>toCanonicalString</code> method of
	 * the given term is used to convert to first convert to a String term and
	 * then to a PlainLiteral term.
	 * 
	 * @param term The term to be converted.
	 * @return A new PlainLiteral term representing the result of the
	 *         conversion, or <code>null</code> if the data type represented by
	 *         the given term is not supported.
	 */
	public static IPlainLiteral toPlainLiteral(ITerm term) {
		if (term instanceof IConcreteTerm) {
			if (term instanceof IPlainLiteral) {
				return (IPlainLiteral) term;
			} else if (term instanceof IStringTerm) {
				return toPlainLiteral((IStringTerm) term);
			} else if (term instanceof IXMLLiteral) {
				return toPlainLiteral((IXMLLiteral) term);
			}

			IStringTerm string = ToStringBuiltin.toString(term);

			if (string != null) {
				return toPlainLiteral(string);
			}
		}

		return null;
	}

}
