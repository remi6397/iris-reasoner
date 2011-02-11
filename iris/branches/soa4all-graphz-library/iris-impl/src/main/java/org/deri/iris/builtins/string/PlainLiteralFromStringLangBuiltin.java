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
import static org.deri.iris.factory.Factory.CONCRETE;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.FunctionalBuiltin;

/**
 * Represents the RIF built-in function func:PlainLiteral-from-string-lang.
 */
public class PlainLiteralFromStringLangBuiltin extends FunctionalBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TEXT_FROM_STRING_LANG", 3);

	/**
	 * Constructor. Three terms must be passed to the constructor, otherwise an
	 * exception will be thrown.
	 * 
	 * @param terms The terms.
	 * @throws IllegalArgumentException If one of the terms is {@code null}.
	 * @throws IllegalArgumentException If the number of terms submitted is not
	 *             3.
	 * @throws IllegalArgumentException If terms is <code>null</code>.
	 */
	public PlainLiteralFromStringLangBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		if (terms[0] instanceof IStringTerm && terms[1] instanceof IStringTerm) {
			String text = ((IStringTerm) terms[0]).getValue();
			
			// http://www.w3.org/TR/rdf-plain-literal/#plfn:PlainLiteral-from-string-lang
			// Note that, since in the value space of rdf:PlainLiteral language 
			// tags are in lowercase, this function converts $arg2 to lowercase
			String lang = ((IStringTerm) terms[1]).getValue().toLowerCase();

			return CONCRETE.createPlainLiteral(text, lang);
		}

		return null;
	}

}
