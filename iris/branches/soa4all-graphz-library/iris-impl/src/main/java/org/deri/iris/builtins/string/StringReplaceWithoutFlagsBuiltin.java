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

/**
 * Represents the RIF built-in func:replace as described in
 * http://www.w3.org/TR/xpath-functions/#func-replace, but restricts the flags
 * to empty flags.
 */
public class StringReplaceWithoutFlagsBuiltin extends FunctionalBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"STRING_REPLACE3", 4);

	/**
	 * Constructor.
	 * 
	 * @param term The terms, where the term at the first position is the
	 *            string, the term at the second position is the regex, the term
	 *            at the third position is the replacement and the term at the
	 *            fourth position represents the result. The string is the
	 *            string to apply the replace operation on. The regex is the
	 *            regular expression. The replacement is the replacement for the
	 *            matching substrings.
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 */
	public StringReplaceWithoutFlagsBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
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

		String result = StringReplaceBuiltin.replace(string, regex,
				replacement, flags);

		if (result != null) {
			return Factory.TERM.createString(result);
		}

		return null;
	}

}
