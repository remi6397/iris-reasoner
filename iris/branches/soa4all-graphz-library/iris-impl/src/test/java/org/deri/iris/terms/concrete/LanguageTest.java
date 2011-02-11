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

import org.deri.iris.api.terms.IConcreteTerm;
import org.deri.iris.api.terms.concrete.ILanguage;
import org.deri.iris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the Language data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class LanguageTest extends AbstractConcreteTermTest {

	@Override
	protected IConcreteTerm createBasic() {
		return new Language("   en");
	}

	@Override
	protected String createBasicString() {
		return "en";
	}

	@Override
	protected IConcreteTerm createEqual() {
		return new Language("en");
	}

	@Override
	protected String createEqualString() {
		return "en";
	}

	@Override
	protected IConcreteTerm createGreater() {
		return new Language("es");
	}

	@Override
	protected String createGreaterString() {
		return "es";
	}

	@Override
	protected URI getDatatypeIRI() {
		return URI.create(ILanguage.DATATYPE_URI);
	}

	public void testValidity() {
		try {
			new Language("123");
			fail("Did not recognize invalid language");
		} catch (IllegalArgumentException e) {
		}
		
		try {
			new Language("de1");
			fail("Did not recognize invalid language");
		} catch (IllegalArgumentException e) {
		}

		try {
			new Language("abcdefghi");
			fail("Did not recognize invalid language");
		} catch (IllegalArgumentException e) {
		}

		try {
			new Language("abcdefgh-abcdefgh");
		} catch (IllegalArgumentException e) {
			fail("Did not recognize valid language");
		}

		try {
			new Language("abcdefgh-abcdefghi");
			fail("Did not recognize invalid language");
		} catch (IllegalArgumentException e) {
		}

		try {
			new Language("de-de");
		} catch (IllegalArgumentException e) {
			fail("Did not recognize valid language");
		}
	}

}
