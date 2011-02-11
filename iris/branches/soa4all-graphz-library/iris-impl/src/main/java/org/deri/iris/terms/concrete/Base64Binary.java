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
import java.util.regex.Pattern;

import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IBase64Binary;

/**
 * <p>
 * Simple implementation of the IBase64Binary.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class Base64Binary implements IBase64Binary {

	public static final Pattern PATTERN = Pattern.compile("([a-zA-Z0-9/+]{4})*"
			+ "(([a-zA-Z0-9/+]{2}[AEIMQUYcgkosw048]=)|"
			+ "([a-zA-Z0-9/+]{1}[AQgw]==))?");

	private String content = "";

	Base64Binary() {
	}

	Base64Binary(final String content) {
		this();
		_setValue(content);
	}

	public int compareTo(ITerm o) {
		if (o == null)
			return 1;

		Base64Binary b64 = (Base64Binary) o;
		return content.compareTo(b64.getValue());
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof Base64Binary)) {
			return false;
		}
		Base64Binary b64 = (Base64Binary) obj;
		return content.equals(b64.content);
	}

	public String getValue() {
		return content;
	}

	public int hashCode() {
		return content.hashCode();
	}

	private void _setValue(final String content) {
		if (PATTERN.matcher(content).matches()) {
			this.content = content;
		} else {
			throw new IllegalArgumentException("Couldn't parse " + content
					+ " to a valid Base64Binary");
		}
	}

	public String toString() {
		return getClass().getSimpleName() + "(" + getValue() + ")";
	}

	public boolean isGround() {
		return true;
	}

	public URI getDatatypeIRI() {
		return URI.create("http://www.w3.org/2001/XMLSchema#base64Binary");
	}

	public String toCanonicalString() {
		return new String(getValue());
	}
}
