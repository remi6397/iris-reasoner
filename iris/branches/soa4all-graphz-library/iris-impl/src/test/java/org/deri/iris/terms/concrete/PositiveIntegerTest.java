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

import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.concrete.IPositiveInteger;
import org.deri.iris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the PositiveInteger data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class PositiveIntegerTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new PositiveInteger(1);
	}

	@Override
	protected String createBasicString() {
		return "1";
	}

	@Override
	protected INumericTerm createEqual() {
		return new PositiveInteger(1);
	}

	@Override
	protected String createEqualString() {
		return "1";
	}

	@Override
	protected INumericTerm createGreater() {
		return new PositiveInteger(1337);
	}

	@Override
	protected String createGreaterString() {
		return "1337";
	}

	@Override
	protected URI getDatatypeIRI() {
		return URI.create(IPositiveInteger.DATATYPE_URI);
	}

	public void testValidity() {
		try {
			new PositiveInteger(-1);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}

		try {
			new PositiveInteger(-12131);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}
	}

}
