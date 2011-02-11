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
import org.deri.iris.api.terms.concrete.IHexBinary;

/**
 * <p>
 * Simple implementation of the IHexBinary.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class HexBinary implements IHexBinary {

	public static final Pattern PATTERN = Pattern.compile("([\\dA-F]{2})+");

	private String content = "";

	HexBinary(final String content) {
		_setValue(content);
	}

	public int compareTo(ITerm o) {
		if (o == null) {
			return 1;
		}
		
		HexBinary hb = (HexBinary) o;
		return getValue().compareTo(hb.getValue());
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof HexBinary)) {
			return false;
		}
		HexBinary hb = (HexBinary)obj;
		return content.equals(hb.content);
	}

	public String getValue() {
		return content;
	}
	
	public int hashCode() {
		return content.hashCode();
	}

	private void _setValue(final String arg) {
		final String sStr = arg.toUpperCase();
		if (PATTERN.matcher(sStr).matches()) {
			this.content = sStr;
		} else {
			throw new IllegalArgumentException(
					"Couldn't parse "
							+ sStr
							+ " to a valid HexBinary. The String must have the pattern "
							+ PATTERN.pattern());
		}
	}

	public String toString() {
		return getClass().getSimpleName() + "(" + getValue() + ")";
	}

	public boolean isGround() {
		return true;
	}

	public IHexBinary getMinValue() {
		return new HexBinary("00");
	}

	public URI getDatatypeIRI() {
		return URI.create("http://www.w3.org/2001/XMLSchema#hexBinary");
	}

	public String toCanonicalString() {
		return new String(getValue());
	}
}
