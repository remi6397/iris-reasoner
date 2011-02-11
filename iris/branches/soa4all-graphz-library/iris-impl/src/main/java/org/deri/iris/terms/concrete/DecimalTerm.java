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

import org.deri.iris.api.terms.concrete.IDecimalTerm;

/**
 * <p>
 * Simple implementation of IDecimalTerm.
 * </p>
 */
public class DecimalTerm extends AbstractNumericTerm implements IDecimalTerm {

	protected final BigDecimal value;

	DecimalTerm(double value) {
		this(new BigDecimal(Double.toString(value)));
	}

	DecimalTerm(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getValue() {
		return value;
	}

	public URI getDatatypeIRI() {
		return URI.create(IDecimalTerm.DATATYPE_URI);
	}
	
	public boolean isNotANumber() {
		return false;
	}

	public boolean isPositiveInfinity() {
		return false;
	}

	public boolean isNegativeInfinity() {
		return false;
	}

}
