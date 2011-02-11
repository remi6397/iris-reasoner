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
package org.deri.iris.terms.concrete;

import java.net.URI;

import org.deri.iris.api.terms.concrete.IToken;

/**
 * <p>
 * A simple implementation of Token.
 * </p>
 * 
 * @author Adrian Marte
 */
public class Token extends NormalizedString implements IToken {

	private static String[] removePatterns = new String[] { "[\\x20]{2,}" };

	/**
	 * Creates a new Token for the specified string. The string is normalized if
	 * it is not normalized already.
	 * 
	 * @param string The normalized or non-normalized string.
	 */
	public Token(String string) {
		super(string);
		value = normalize(value);
	}

	public static String normalize(String string) {
		// Remove carriage-returns, line-feeds and tabs.
		String normalizedString = NormalizedString.normalize(string);

		// Remove any sequence of two or more spaces
		for (String pattern : removePatterns) {
			normalizedString = normalizedString.replaceAll(pattern, " ");
		}

		// Remove leading or trailing spaces.
		return normalizedString.trim();
	}

	@Override
	public URI getDatatypeIRI() {
		return URI.create(IToken.DATATYPE_URI);
	}

}
