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
package org.deri.iris.terms;

import java.net.URI;

import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * Simple implementation of the IStringTerm.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class StringTerm implements IStringTerm {

	private String value = "";

	StringTerm(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(ITerm o) {
		if (o == null || !(o instanceof IStringTerm)) {
			return 1;
		}

		IStringTerm st = (IStringTerm) o;
		return value.compareTo(st.getValue());
	}

	public int hashCode() {
		return value.hashCode();
	}

	public boolean equals(final Object o) {
		if (!(o instanceof IStringTerm)) {
			return false;
		}
		IStringTerm st = (IStringTerm) o;
		return value.equals(st.getValue());
	}

	/**
	 * Simple toString() method wich only returns the holded value surrounded by
	 * &quot;'&quot;.
	 * 
	 * @return the containing String
	 */
	public String toString() {
		return "'" + value + "'";
	}

	public URI getDatatypeIRI() {
		return URI.create("http://www.w3.org/2001/XMLSchema#string");
	}

	public String toCanonicalString() {
		return new String(value);
	}
}
