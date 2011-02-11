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

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.FunctionalBuiltin;
import org.deri.iris.factory.Factory;
import org.deri.iris.terms.StringTerm;

/**
 * Represents the RIF built-in func:substring-after as described in
 * http://www.w3.org/TR/xpath-functions/#func-substring-after. Restricts the
 * value of collation to Unicode code point collation
 * (http://www.w3.org/2005/xpath-functions/collation/codepoint).
 */
public class StringSubstringAfterWithoutCollationBuiltin extends
		FunctionalBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"STRING_SUBSTRING_AFTER2", 3);

	/**
	 * Constructor.
	 * 
	 * @param terms The terms, where the term at the first position is the
	 *            <code>haystack</code>, the term at the second position is the
	 *            <code>needle</code> and the term at the last position
	 *            represents the result. The <code>haystack</code> is the string
	 *            being searched for the occurrence of the <code>needle</code>.
	 *            The <code>needle</code> is the string to be searched for in
	 *            the <code>haystack</code>.
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 */
	public StringSubstringAfterWithoutCollationBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		String haystack = null;
		String needle = null;

		if (terms[0] instanceof IStringTerm && terms[1] instanceof StringTerm) {
			haystack = ((IStringTerm) terms[0]).getValue();
			needle = ((IStringTerm) terms[1]).getValue();
		} else {
			return null;
		}

		String result = StringSubstringAfterBuiltin.substring(haystack, needle,
				null);

		if (result != null) {
			return Factory.TERM.createString(result);
		}

		return null;
	}

}
