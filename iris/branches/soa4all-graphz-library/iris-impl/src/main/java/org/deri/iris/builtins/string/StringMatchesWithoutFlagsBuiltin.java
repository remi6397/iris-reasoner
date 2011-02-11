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
package org.deri.iris.builtins.string;

import static org.deri.iris.factory.Factory.BASIC;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.BooleanBuiltin;

/**
 * Represents the RIF built-in func:matches as described in
 * http://www.w3.org/TR/xpath-functions/#func-matches., but restricts the flags
 * to empty flags.
 */
public class StringMatchesWithoutFlagsBuiltin extends BooleanBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"STRING_MATCHES2", 2);

	/**
	 * Constructor.
	 * 
	 * @param terms The terms, where the term at the first position is the
	 *            string, the terms at the second position is the pattern and
	 *            the term at the third position represents the flags. The
	 *            string is the string the regular expression is being matched
	 *            against. The patterns is the string representing the regular
	 *            expression. The flags are the flags as described in
	 *            http://www.w3.org/TR/xpath-functions/#flags.
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 */
	public StringMatchesWithoutFlagsBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		String string = null;
		String pattern = null;
		String flags = "";

		if (terms[0] instanceof IStringTerm && terms[1] instanceof IStringTerm) {
			string = ((IStringTerm) terms[0]).getValue();
			pattern = ((IStringTerm) terms[1]).getValue();
		} else {
			return false;
		}

		return StringMatchesBuiltin.matches(string, pattern, flags);
	}

}
