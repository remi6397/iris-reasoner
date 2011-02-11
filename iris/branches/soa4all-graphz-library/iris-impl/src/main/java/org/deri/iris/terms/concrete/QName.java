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
import org.deri.iris.api.terms.concrete.IQName;

/**
 * <p>
 * An simple implementation of QName.
 * </p>
 * 
 * @author Adrian Marte
 */
public class QName implements IQName {

	private String namespaceName;

	private String localPart;

	/**
	 * Creates a new QName instance for the given namespace name and local part.
	 * 
	 * @param namespaceName The namespace name for this QName. May be null.
	 * @param localPart The local part of this QName.
	 * @throws NullPointerException If the local part is <code>null</code>.
	 */
	public QName(String namespaceName, String localPart) {
		this.namespaceName = namespaceName;
		this.localPart = localPart;

		if (this.localPart == null) {
			throw new NullPointerException(
					"The local part of a QName must not be null");
		}
	}

	public String getNamespaceName() {
		return namespaceName;
	}

	public String getLocalPart() {
		return localPart;
	}

	public String[] getValue() {
		return new String[] { namespaceName, localPart };
	}

	public URI getDatatypeIRI() {
		return URI.create(IQName.DATATYPE_URI);
	}

	public String toCanonicalString() {
		String result = "";

		if (namespaceName != null) {
			result += namespaceName + ":";
		}

		result += localPart;

		return result;
	}

	@Override
	public String toString() {
		return toCanonicalString();
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(ITerm thatObject) {
		if (!(thatObject instanceof IQName)) {
			return 1;
		}

		IQName thatQName = (IQName) thatObject;

		return toCanonicalString().compareTo(thatQName.toCanonicalString());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IQName)) {
			return false;
		}

		IQName thatQName = (IQName) obj;

		return namespaceName.equals(thatQName.getNamespaceName())
				&& localPart.equals(thatQName.getLocalPart());
	}

	@Override
	public int hashCode() {
		return namespaceName.hashCode() + 37 * localPart.hashCode();
	}

}
