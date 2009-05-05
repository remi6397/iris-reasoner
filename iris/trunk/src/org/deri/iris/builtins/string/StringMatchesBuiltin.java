/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2009 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.builtins.string;

import static org.deri.iris.factory.Factory.BASIC;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.BooleanBuiltin;

/**
 * Represents a string starts-with operation as described in
 * http://www.w3.org/TR/xpath-functions/#func-starts-with.
 */
public class StringMatchesBuiltin extends BooleanBuiltin {

	private static final IPredicate PREDICATE1 = BASIC.createPredicate(
			"STRING_MATCHES2", 2);
	private static final IPredicate PREDICATE2 = BASIC.createPredicate(
			"STRING_MATCHES3", 3);

	/**
	 * Constructor.
	 * 
	 * @param string
	 *            The term representing the string the regular expression is
	 *            being matched against.
	 * @param pattern
	 *            The term representing the regular expression.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringMatchesBuiltin(final ITerm string, final ITerm pattern) {
		super(PREDICATE1, new ITerm[] { string, pattern });
	}

	/**
	 * Constructor.
	 * 
	 * @param string
	 *            The term representing the string the regular expression is
	 *            being matched against.
	 * @param pattern
	 *            The term representing the regular expression.
	 * @param flags
	 *            The flags as described in
	 *            http://www.w3.org/TR/xpath-functions/#flags.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringMatchesBuiltin(final ITerm string, final ITerm pattern,
			final ITerm flags) {
		super(PREDICATE2, new ITerm[] { string, pattern, flags });
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		assert terms.length >= 2 && terms.length <= 3;

		String string = null;
		String pattern = null;
		String flags = "";

		if (terms[0] instanceof IStringTerm && terms[1] instanceof IStringTerm) {
			string = ((IStringTerm) terms[0]).getValue();
			pattern = ((IStringTerm) terms[1]).getValue();
		} else {
			return false;
		}

		if (terms.length > 2) {
			if (terms[2] instanceof IStringTerm) {
				flags = ((IStringTerm) terms[2]).getValue();
			} else {
				return false;
			}
		}

		return matches(string, pattern, flags);
	}

	private boolean matches(String string, String regex, String flags) {
		int flag = 0;

		// See http://www.w3.org/TR/xpath-functions/#flags.
		if (flags.contains("s")) {
			flag |= Pattern.DOTALL;
		}

		if (flags.contains("m")) {
			flag |= Pattern.MULTILINE;
		}

		if (flags.contains("i")) {
			flag |= Pattern.UNICODE_CASE;
		}

		if (flags.contains("x")) {
			/*
			 * Naive approach would be to use Pattern.COMMENTS but whit this
			 * flag set, whitespace elimination in character class expression
			 * may lead to errors, e.g. the pattern hello[ ]world with COMMENT
			 * mode on results in a character class with no elements and
			 * therefore results in an exception.
			 */

			// Sort this array in order to enable binary search.
			int[] whitespaces = { 0x9, 0xA, 0xD, 0x20 };
			StringBuffer buffer = new StringBuffer();

			int brackets = 0;

			for (int i = 0; i < regex.length(); i++) {
				char character = regex.charAt(i);

				/*
				 * With this mechanism brackets is 0 if and only if the current
				 * character is outside a character class.
				 */
				if (character == '[') {
					brackets++;
				} else if (character == ']') {
					brackets--;
				}

				if (Arrays.binarySearch(whitespaces, character) >= 0
						&& brackets == 0) {
				} else {
					buffer.append(character);
				}
			}

			regex = buffer.toString();
		}

		Pattern pattern = Pattern.compile(regex, flag);
		Matcher matcher = pattern.matcher(string);

		return matcher.find();
	}

	public int maxUnknownVariables() {
		return 0;
	}

}
