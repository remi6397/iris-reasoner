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
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.FunctionalBuiltin;
import org.deri.iris.factory.Factory;

/**
 * Represents a string substring operation.
 * 
 * @author gigi
 * @author Adrian Marte
 */
public class StringSubstringBuiltin extends FunctionalBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate("STRING_SUBSTRING3", 4);

	/**
	 * Constructor.
	 * 
	 * @param terms The terms, where the first term is the string, the second
	 *            term is the begin index, the third term is the endIndex and
	 *            the fourth term represents the result.
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 */
	public StringSubstringBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		// FIXME value spaces of xs:integer, xs:double, xs:float and xs:decimal for numeric terms
		
		if (terms[0] instanceof IStringTerm 
				&& terms[1] instanceof INumericTerm
				&& terms[2] instanceof INumericTerm) {
			IStringTerm string = (IStringTerm) terms[0];
			INumericTerm beginIndex = (INumericTerm) terms[1];
			INumericTerm lengthIndex = (INumericTerm) terms[2];

			int start = beginIndex.getValue().intValue();
			int length = lengthIndex.getValue().intValue();
			String substring = string.getValue().substring(
					start,
					start + length);

			return Factory.TERM.createString(substring);
		}

		return null;
	}

}
