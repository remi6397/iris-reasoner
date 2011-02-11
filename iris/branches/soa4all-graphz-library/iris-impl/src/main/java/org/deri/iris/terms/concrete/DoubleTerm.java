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

import org.deri.iris.api.terms.concrete.IDoubleTerm;

/**
 * <p>
 * Simple implementation of the IDoubleTerm.
 * </p>
 */
public class DoubleTerm extends AbstractNumericTerm implements IDoubleTerm {

	private final Double value;

	private BigDecimal decimalValue;

	DoubleTerm(float value) {
		this(Double.valueOf(Float.toString(value)));
	}

	DoubleTerm(double value) {
		this.value = value;
	}

	public BigDecimal getValue() {
		if (isNotANumber() || isPositiveInfinity() || isNegativeInfinity()) {
			return null;
		}

		if (decimalValue == null) {
			decimalValue = new BigDecimal(Double.toString(value));
		}

		return decimalValue;
	}

	public boolean isNotANumber() {
		return value.equals(Double.NaN);
	}

	public boolean isPositiveInfinity() {
		return value.equals(Double.POSITIVE_INFINITY);
	}

	public boolean isNegativeInfinity() {
		return value.equals(Double.NEGATIVE_INFINITY);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	public URI getDatatypeIRI() {
		return URI.create(IDoubleTerm.DATATYPE_URI);
	}

}
