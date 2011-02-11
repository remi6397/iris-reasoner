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

import java.math.BigInteger;
import java.net.URI;

import org.deri.iris.api.terms.concrete.IUnsignedLong;

/**
 * <p>
 * A simple implementation of UnsignedLong.
 * </p>
 * 
 * @author Adrian Marte
 */
public class UnsignedLong extends NonNegativeInteger implements IUnsignedLong {

	/**
	 * Creates a new UnsignedLong for the specified BigInteger.
	 * 
	 * @param value The BigInteger representing a number not less than 0 and not
	 *            greater than 18446744073709551615.
	 * @throws IllegalArgumentException If the specified BigInteger is less than
	 *             0 or greater than 18446744073709551615.
	 */
	public UnsignedLong(BigInteger value) {
		super(value);

		if (value.compareTo(IUnsignedLong.MAX_INCLUSIVE) >= 1) {
			throw new IllegalArgumentException(
					"Value must not be greater than "
							+ IUnsignedLong.MAX_INCLUSIVE);
		}
	}

	@Override
	public URI getDatatypeIRI() {
		return URI.create(IUnsignedLong.DATATYPE_URI);
	}

}
