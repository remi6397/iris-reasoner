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

import java.math.BigDecimal;
import java.net.URI;

import org.deri.iris.api.terms.concrete.IFloatTerm;

/**
 * <p>
 * Simple implementation of the IFloatTerm.
 * </p>
 */
public class FloatTerm extends AbstractNumericTerm implements IFloatTerm {

	private final Float value;

	private BigDecimal decimalValue;

	/**
	 * Constructs a new float with the given value.
	 * 
	 * @param value the float value for this object
	 * @throws NullPointerException if the float is null
	 */
	FloatTerm(float value) {
		this.value = value;
	}

	public BigDecimal getValue() {
		if (isNotANumber() || isPositiveInfinity() || isNegativeInfinity()) {
			return null;
		}

		if (decimalValue == null) {
			decimalValue = new BigDecimal(Float.toString(value));
		}

		return decimalValue;
	}

	public boolean isNotANumber() {
		return value.equals(Float.NaN);
	}

	public boolean isPositiveInfinity() {
		return value.equals(Float.POSITIVE_INFINITY);
	}

	public boolean isNegativeInfinity() {
		return value.equals(Float.NEGATIVE_INFINITY);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	public URI getDatatypeIRI() {
		return URI.create(IFloatTerm.DATATYPE_URI);
	}

}
