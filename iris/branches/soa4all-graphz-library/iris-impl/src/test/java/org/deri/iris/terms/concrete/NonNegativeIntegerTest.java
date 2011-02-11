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
import org.deri.iris.api.terms.concrete.INonNegativeInteger;
import org.deri.iris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the NonNegativeInteger data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NonNegativeIntegerTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new NonNegativeInteger(0);
	}

	@Override
	protected String createBasicString() {
		return "0";
	}

	@Override
	protected INumericTerm createEqual() {
		return new NonNegativeInteger(0);
	}

	@Override
	protected String createEqualString() {
		return "0";
	}

	@Override
	protected INumericTerm createGreater() {
		return new NonNegativeInteger(1337);
	}

	@Override
	protected String createGreaterString() {
		return "1337";
	}

	@Override
	protected URI getDatatypeIRI() {
		return URI.create(INonNegativeInteger.DATATYPE_URI);
	}
	
	public void testValidity() {
		try {
			new NonNegativeInteger(0);
		} catch (IllegalArgumentException e) {
			fail("0 is a valid value for nonNegativeInteger");
		}
		
		try {
			new NonNegativeInteger(-1);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}
		
		try {
			new NonNegativeInteger(-12131);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}
	}

}
