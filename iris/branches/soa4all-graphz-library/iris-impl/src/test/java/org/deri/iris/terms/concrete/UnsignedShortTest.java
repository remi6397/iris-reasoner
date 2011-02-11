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
import org.deri.iris.api.terms.concrete.IUnsignedShort;
import org.deri.iris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the UnsignedShort data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class UnsignedShortTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new UnsignedShort(65534);
	}

	@Override
	protected String createBasicString() {
		return "65534";
	}

	@Override
	protected INumericTerm createEqual() {
		return new UnsignedShort(65534);
	}

	@Override
	protected String createEqualString() {
		return "65534";
	}

	@Override
	protected INumericTerm createGreater() {
		return new UnsignedShort(65535);
	}

	@Override
	protected String createGreaterString() {
		return "65535";
	}

	@Override
	protected URI getDatatypeIRI() {
		return URI.create(IUnsignedShort.DATATYPE_URI);
	}

	public void testValidity() {
		try {
			new UnsignedShort(65536);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}

		try {
			new UnsignedShort(-1);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}

		try {
			new UnsignedShort(-121);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}
	}

}
