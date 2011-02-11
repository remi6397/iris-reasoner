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

import org.deri.iris.api.terms.concrete.INonPositiveInteger;

/**
 * <p>
 * A simple implementation of NonPositiveInteger.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NonPositiveInteger extends IntegerTerm implements
		INonPositiveInteger {

	/**
	 * Creates a new NonPositiveInteger for the specified integer.
	 * 
	 * @param value The integer representing a number not greater than 0.
	 * @throws IllegalArgumentException If the specified integer is greater than
	 *             0.
	 */
	public NonPositiveInteger(int value) {
		this(BigInteger.valueOf(value));
	}

	/**
	 * Creates a new NonPositiveInteger for the specified BigInteger.
	 * 
	 * @param value The BigInteger representing a number not greater than 0.
	 * @throws IllegalArgumentException If the specified BigInteger is greater
	 *             than 0.
	 */
	public NonPositiveInteger(BigInteger value) {
		super(value);

		if (value.compareTo(BigInteger.ZERO) > 0) {
			throw new IllegalArgumentException(
					"Value must not be greater than 0");
		}
	}

	@Override
	public URI getDatatypeIRI() {
		return URI.create(INonPositiveInteger.DATATYPE_URI);
	}

}
