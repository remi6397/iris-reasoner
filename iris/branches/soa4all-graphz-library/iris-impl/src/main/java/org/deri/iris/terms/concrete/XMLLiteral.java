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
import org.deri.iris.api.terms.concrete.IXMLLiteral;

/**
 * Implementation of the rdf:XMLLiteral data-type.
 * 
 * @author gigi
 */
public class XMLLiteral implements IXMLLiteral {

	/**
	 * The string representing the XML element content.
	 */
	private String string;

	/**
	 * The language identifier for this XML element.
	 */
	private String lang;

	/**
	 * Creates a new term representing a XMLLiteral.
	 * 
	 * @param string The string.
	 * @param lang The language.
	 * @throws NullPointerException If the value of <code>string</code> or
	 *             <code>lang</code> is <code>null</code>.
	 */
	XMLLiteral(String string, String lang) {
		if (string == null) {
			throw new NullPointerException("String value must not be null");
		}

		if (lang == null) {
			throw new NullPointerException("Language tag must not be null");
		}

		this.string = string;
		this.lang = lang;
	}

	/**
	 * Creates a new term representing a XMLLiteral.
	 * 
	 * @param string The string.
	 * @throws NullPointerException If the value of <code>string</code> is
	 *             <code>null</code>.
	 */
	XMLLiteral(String string) {
		this(string, "");
	}

	public String toString() {
		return toCanonicalString();
	}

	public URI getDatatypeIRI() {
		return URI
				.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral");
	}

	public String getString() {
		return string;
	}

	public String getLang() {
		return lang;
	}

	public String[] getValue() {
		return new String[] { string, lang };
	}

	public boolean isGround() {
		return true;
	}

	public boolean equals(Object thatObject) {
		if (!(thatObject instanceof IXMLLiteral)) {
			return false;
		}

		return compareTo((ITerm) thatObject) == 0;
	}

	public int hashCode() {
		int result = string.hashCode();

		if (lang != null) {
			result = result * 37 + lang.hashCode();
		}

		return result;
	}

	public int compareTo(ITerm thatObject) {
		if (thatObject == null) {
			return 1;
		}

		IXMLLiteral thatLiteral = (IXMLLiteral) thatObject;

		return this.getString().compareTo(thatLiteral.getString());
	}

	public String toCanonicalString() {
		return new String(string);
	}
}
