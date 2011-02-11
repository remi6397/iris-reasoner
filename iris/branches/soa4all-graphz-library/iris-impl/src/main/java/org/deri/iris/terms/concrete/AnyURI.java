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
import org.deri.iris.api.terms.concrete.IAnyURI;

/**
 * <p>
 * An simple implementation of anyURI.
 * </p>
 * 
 * @author Adrian Marte
 */
public class AnyURI implements IAnyURI {

	private URI uri;

	/**
	 * Creates a new AnyURI instance for the given URI.
	 * 
	 * @param uri The URI with which the AnyURI is created.
	 */
	public AnyURI(URI uri) {
		this.uri = uri;
	}

	public URI getValue() {
		return uri;
	}

	public URI getDatatypeIRI() {
		return URI.create(IAnyURI.DATATYPE_URI);
	}

	public String toCanonicalString() {
		return uri.toString();
	}

	@Override
	public String toString() {
		return toCanonicalString();
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(ITerm o) {
		if (!(o instanceof IAnyURI)) {
			return 1;
		}

		IAnyURI thatUri = (IAnyURI) o;

		return uri.compareTo(thatUri.getValue());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IAnyURI)) {
			return false;
		}

		IAnyURI thatAnyURI = (IAnyURI) obj;
		return uri.equals(thatAnyURI.getValue());
	}

	@Override
	public int hashCode() {
		return uri.hashCode();
	}

}
