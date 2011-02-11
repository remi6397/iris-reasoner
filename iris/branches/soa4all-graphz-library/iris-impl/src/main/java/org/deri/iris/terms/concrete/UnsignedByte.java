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

import org.deri.iris.api.terms.concrete.IUnsignedByte;

/**
 * <p>
 * A simple implementation of UnsignedByte.
 * </p>
 * 
 * @author Adrian Marte
 */
public class UnsignedByte extends UnsignedShort implements IUnsignedByte {

	/**
	 * Creates a new UnsignedByte for the specified short.
	 * 
	 * @param value The short representing a number not less than 0 and not
	 *            greater than 255.
	 * @throws IllegalArgumentException If the specified short is less than 0 or
	 *             greater than 255.
	 */
	public UnsignedByte(short value) {
		super(value);

		if (value > IUnsignedByte.MAX_INCLUSIVE) {
			throw new IllegalArgumentException(
					"Value must not be greater than "
							+ IUnsignedByte.MAX_INCLUSIVE);
		}
	}

	@Override
	public URI getDatatypeIRI() {
		return URI.create(IUnsignedByte.DATATYPE_URI);
	}

}
