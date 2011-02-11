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

import org.deri.iris.api.terms.concrete.IUnsignedInt;

/**
 * <p>
 * A simple implementation of UnsignedInt.
 * </p>
 * 
 * @author Adrian Marte
 */
public class UnsignedInt extends UnsignedLong implements IUnsignedInt {

	/**
	 * Creates a new UnsignedInt for the specified long.
	 * 
	 * @param value The long representing a number not less than 0 and not
	 *            greater than 4294967295.
	 * @throws IllegalArgumentException If the specified long is less than 0 or
	 *             greater than 4294967295.
	 */
	public UnsignedInt(long value) {
		super(BigInteger.valueOf(value));

		if (value > IUnsignedInt.MAX_INCLUSIVE) {
			throw new IllegalArgumentException(
					"Value must not be greater than "
							+ IUnsignedInt.MAX_INCLUSIVE);
		}
	}

	@Override
	public URI getDatatypeIRI() {
		return URI.create(IUnsignedInt.DATATYPE_URI);
	}

}
