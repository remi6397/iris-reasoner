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
package org.deri.iris.terms.concrete;

import java.net.URI;

import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IText;

/**
 * A term representing a rdf:text.
 */
public class Text implements IText {

	private String string;
	private String lang;

	/**
	 * Constructs a new term representing a rdf:text.
	 * 
	 * @param string
	 *            The string.
	 * @param lang
	 *            The language.
	 */
	Text(String string, String lang) {
		super();
		this.string = string;
		this.lang = lang;
	}

	/**
	 * Constructs a new term representing a rdf:text. If the string passed to
	 * this constructor is of the form "text@lang", the value of lang will be
	 * used as the language tag.
	 * 
	 * @param string
	 *            The string value. May be of the form "text@lang", where "lang"
	 *            is the language of the text.
	 */
	Text(String string) {
		this.lang = "";

		if (string.contains("@")) {
			String langtag = string.substring(string.lastIndexOf("@"));

			if (langtag.length() >= 1) {
				this.lang = langtag.substring(1);
			}

			this.string = string.substring(0, string.lastIndexOf("@"));
		} else {
			this.string = string;
		}
	}

	/*
	 * Returns the a string representing the rdf:text in the form "text@lang".
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.deri.iris.api.terms.IStringTerm#getValue()
	 */
	public String[] getValue() {
		return new String[] { string, lang };
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(ITerm o) {
		if (o == null) {
			return 1;
		}

		IText thatText = (IText) o;

		/*
		 * We compare the text value, since per definition two rdf:texts that
		 * are being compared have the same language tag. Otherwise, the value
		 * of the compare function is left unspecified.
		 */
		return this.getString().compareTo(thatText.getString());
	}

	public boolean equals(final Object o) {
		if (!(o instanceof IText)) {
			return false;
		}

		IText thatText = (IText) o;
		return getString().equals(thatText.getString());
	}

	@Override
	public int hashCode() {
		int result = string.hashCode();
		result = result * 37 + lang.hashCode();
		return result;
	}

	public String toString() {
		return toCanonicalString();
	}

	public String getString() {
		return string;
	}

	public String getLang() {
		return lang;
	}

	@Override
	public URI getDatatypeIRI() {
		return URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#text");
	}

	@Override
	public String toCanonicalString() {
		return this.getString() + "@" + this.getLang();
	}

}
