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

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.BooleanBuiltin;
import org.deri.iris.terms.StringTerm;

/**
 * Represents a string ends-with operation as described in
 * http://www.w3.org/TR/xpath-functions/#func-ends-with.
 */
public class StringEndsWithBuiltin extends BooleanBuiltin {

	private static final IPredicate PREDICATE1 = BASIC.createPredicate(
			"STRING_ENDSWITH2", 2);
	private static final IPredicate PREDICATE2 = BASIC.createPredicate(
			"STRING_ENDSWITH3", 3);

	/**
	 * Constructor.
	 * 
	 * @param haystack
	 *            The term representing the haystack, i.e. the string being
	 *            searched for the occurrence of the needle.
	 * @param needle
	 *            The term representing the needle, i.e. the string to be
	 *            searched for in the haystack.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringEndsWithBuiltin(final ITerm haystack, final ITerm needle) {
		super(PREDICATE1, new ITerm[] { haystack, needle });
	}

	/**
	 * Constructor.
	 * 
	 * @param haystack
	 *            The term representing the haystack, i.e. the string being
	 *            searched for the occurrence of the needle.
	 * @param needle
	 *            The term representing the needle, i.e. the string to be
	 *            searched for in the haystack.
	 * @param collation
	 *            The collation to be used. Currently only "Minimal match" is
	 *            supported.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringEndsWithBuiltin(final ITerm haystack, final ITerm needle,
			final ITerm collation) {
		super(PREDICATE2, new ITerm[] { haystack, needle, collation });
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		assert terms.length >= 2 && terms.length <= 3;

		String haystack = null;
		String needle = null;

		// The default collation.
		String collation = "Minimal Match";

		if (terms[0] instanceof IStringTerm && terms[1] instanceof IStringTerm) {
			haystack = ((StringTerm) terms[0]).getValue();
			needle = ((StringTerm) terms[1]).getValue();
		} else {
			return false;
		}

		// If the value of haystack is the zero-length string, the function
		// returns false.
		if (haystack.length() == 0 && needle.length() > 0) {
			return false;
		}

		if (terms.length > 2) {
			if (terms[2] instanceof IStringTerm) {
				collation = ((IStringTerm) terms[2]).getValue();

				// Only "Minimal Match" is supported at the moment.
				if (!collation.equalsIgnoreCase("Minimal Match")) {
					throw new IllegalArgumentException("Unsupported collation");
				}
			} else {
				return false;
			}
		}

		return haystack.endsWith(needle);
	}

	public int maxUnknownVariables() {
		return 0;
	}

}
