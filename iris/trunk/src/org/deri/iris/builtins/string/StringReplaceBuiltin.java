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

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.AbstractBuiltin;
import org.deri.iris.factory.Factory;

/**
 * Represents a string replace operation as described in
 * http://www.w3.org/TR/xpath-functions/#func-replace.
 */
public class StringReplaceBuiltin extends AbstractBuiltin {

	private static final IPredicate PREDICATE1 = BASIC.createPredicate(
			"STRING_REPLACE3", 3);
	private static final IPredicate PREDICATE2 = BASIC.createPredicate(
			"STRING_REPLACE4", 4);

	/**
	 * Constructor.
	 * 
	 * @param string
	 *            The term representing the string to apply the replace
	 *            operation on.
	 * @param regex
	 *            The regular expression.
	 * @param replacement
	 *            The replacement for the matching substrings.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringReplaceBuiltin(final ITerm string, final ITerm regex,
			final ITerm replacement) {
		super(PREDICATE1, new ITerm[] { string, regex, replacement });
	}

	/**
	 * Constructor.
	 * 
	 * @param string
	 *            The term representing the string to apply the replace
	 *            operation on.
	 * @param regex
	 *            The regular expression.
	 * @param replacement
	 *            The replacement for the matching substrings.
	 * @param flags
	 *            The flags as described in
	 *            http://www.w3.org/TR/xpath-functions/#flags.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringReplaceBuiltin(final ITerm string, final ITerm regex,
			final ITerm replacement, final ITerm flags) {
		super(PREDICATE2, new ITerm[] { string, regex, replacement, flags });
	}

	protected ITerm evaluateTerms(ITerm[] terms, int[] variableIndexes)
			throws EvaluationException {
		assert variableIndexes.length == 0;
		assert terms.length >= 3 && terms.length <= 4;

		String string = null;
		String regex = null;
		String replacement = null;
		String flags = "";

		if (terms[0] instanceof IStringTerm && terms[1] instanceof IStringTerm
				&& terms[2] instanceof IStringTerm) {
			string = ((IStringTerm) terms[0]).getValue();
			regex = ((IStringTerm) terms[1]).getValue();
			replacement = ((IStringTerm) terms[2]).getValue();
		} else {
			return null;
		}

		if (terms.length > 3) {
			if (terms[3] instanceof IStringTerm) {
				flags = ((IStringTerm) terms[3]).getValue();
			} else {
				return null;
			}
		}

		String result = replace(string, regex, replacement, flags);

		if (result != null) {
			return Factory.TERM.createString(result);
		}

		return null;
	}

	private String replace(String string, String regex, String replacement,
			String flags) {
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

		return matcher.replaceAll(replacement);
	}

	public int maxUnknownVariables() {
		return 0;
	}

}
