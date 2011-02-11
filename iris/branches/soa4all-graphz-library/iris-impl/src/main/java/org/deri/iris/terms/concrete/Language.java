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

import org.deri.iris.api.terms.concrete.ILanguage;

/**
 * <p>
 * A simple implementation of a language tag.
 * </p>
 * 
 * @author Adrian Marte
 */
public class Language extends Token implements ILanguage {

	/**
	 * Defines the pattern of all conformant language tags.
	 */
	private static String pattern = "[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*";

	/**
	 * Creates a new Language instance for the specified language tag and checks
	 * for validity of the language tag.
	 * 
	 * @param language The language tag.
	 * @throws IllegalArgumentException If the specified language is no valid
	 *             language tag.
	 */
	public Language(String language) {
		this(language, true);
	}

	/**
	 * Creates a new Language instance for the specified language tag.
	 * 
	 * @param language The language tag.
	 * @param isValidating If set to true the specified language is tested for
	 *            validity.
	 * @throws IllegalArgumentException If isValidating is set to true and the
	 *             specified language is no valid language tag.
	 */
	public Language(String language, boolean isValidating)
			throws IllegalArgumentException {
		super(language);

		if (isValidating && !validate(value)) {
			throw new IllegalArgumentException("Invalid language tag");
		}
	}

	public static boolean validate(String language) {
		return language.matches(pattern);
	}

	@Override
	public URI getDatatypeIRI() {
		return URI.create(ILanguage.DATATYPE_URI);
	}

}
