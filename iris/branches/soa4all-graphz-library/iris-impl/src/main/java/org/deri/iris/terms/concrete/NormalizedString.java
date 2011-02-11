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

import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.INormalizedString;

/**
 * <p>
 * A simple implementation of NormalizedString.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NormalizedString implements INormalizedString {

	protected String value;

	private static String[] removePatterns = new String[] { "\\t", "\\n", "\\r" };

	/**
	 * Creates a new NormalizedString instance. The specified string is
	 * normalized if it is not normalized already.
	 * 
	 * @param string The normalized or non-normalized string.
	 */
	public NormalizedString(String string) {
		value = normalize(string);
	}

	public static String normalize(String string) {
		String normalizedString = string;

		for (String pattern : removePatterns) {
			normalizedString = normalizedString.replaceAll(pattern, "");
		}

		return normalizedString;
	}

	public String getValue() {
		return value;
	}

	public URI getDatatypeIRI() {
		return URI.create(INormalizedString.DATATYPE_URI);
	}

	public String toCanonicalString() {
		return value;
	}

	@Override
	public String toString() {
		return toCanonicalString();
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(ITerm o) {
		if (!(o instanceof INormalizedString)) {
			return 1;
		}

		INormalizedString thatString = (INormalizedString) o;
		return value.compareTo(thatString.getValue());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof INormalizedString)) {
			return false;
		}

		INormalizedString thatString = (INormalizedString) obj;
		return value.equals(thatString.getValue());
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

}
