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

import junit.framework.TestCase;

import org.deri.iris.ObjectTests;

/**
 * Test the implementation of the PlainLiteral data-type. Specification is
 * available at http://www.w3.org/TR/rdf-text/.
 * 
 * @author gigi
 */
public class PlainLiteralTest extends TestCase {

	private static final String STRING = "text";

	private static final String LANG = "de";

	private static final String LANG_AND_TEXT = "text@de";

	public void testBasic() {
		PlainLiteral plainliteral = new PlainLiteral(STRING, LANG);

		assertEquals(STRING, plainliteral.getString());
		assertEquals(LANG, plainliteral.getLang());

		PlainLiteral plainliteralParsing = new PlainLiteral(LANG_AND_TEXT);

		assertEquals(STRING, plainliteralParsing.getString());
		assertEquals(LANG, plainliteralParsing.getLang());

		assertEquals("Object not initialized correctly", "foobar",
				new PlainLiteral("foobar").getValue()[0]);
	}

	public void testSimpleParsing() {
		PlainLiteral plainliteral = new PlainLiteral("parser-test@home@de-AT");
		assertEquals("parser-test@home", plainliteral.getString());
		assertEquals("de-AT", plainliteral.getLang());

		plainliteral = new PlainLiteral("some other rdf:PlainLiteral", "de");
		assertEquals("some other rdf:PlainLiteral", plainliteral.getString());
		assertEquals("de", plainliteral.getLang());

		plainliteral = new PlainLiteral("some simple string without any language@");
		assertEquals("some simple string without any language", plainliteral
				.getString());
		assertEquals("", plainliteral.getLang());

		// rdf:PlainLiteral containing only the @ and a language tag
		plainliteral = new PlainLiteral("@en");
		assertEquals("", plainliteral.getString());
		assertEquals("en", plainliteral.getLang());

		plainliteral = new PlainLiteral("@");
		assertEquals("", plainliteral.getString());
		assertEquals("", plainliteral.getLang());
	}

	public void testToString() {
		PlainLiteral plainliteral = new PlainLiteral("");
		assertEquals("@", plainliteral.toString());

		plainliteral = new PlainLiteral("some other rdf:PlainLiteral", "de");
		assertEquals("some other rdf:PlainLiteral@de", plainliteral.toString());
	}

	public void testCompareTo() {
		PlainLiteral plainliteralA = new PlainLiteral("rdf:PlainLiteral to compare 1@en");
		PlainLiteral plainliteralB = new PlainLiteral("rdf:PlainLiteral to compare 2@en");
		PlainLiteral plainliteralC = new PlainLiteral("rdf:PlainLiteral to compare 1@en");

		ObjectTests.runTestCompareTo(plainliteralA, plainliteralC, plainliteralB);

		assertEquals(-1, plainliteralA.compareTo(plainliteralB));
		assertEquals(1, plainliteralB.compareTo(plainliteralA));

		PlainLiteral plainliteralA2 = new PlainLiteral("rdf:PlainLiteral to compare 1@en");
		assertEquals(0, plainliteralA.compareTo(plainliteralA2));
	}

	public void testEquals() {
		PlainLiteral plainliteralA = new PlainLiteral("rdf:PlainLiteral to compare 1@en");
		PlainLiteral plainliteralB = new PlainLiteral("rdf:PlainLiteral to compare 1@de");
		PlainLiteral plainliteralC = new PlainLiteral("rdf:PlainLiteral to compare 2@en");

		ObjectTests.runTestEquals(plainliteralA, plainliteralB, plainliteralC);
	}

	public void testHashCode() {
		PlainLiteral plainliteralA = new PlainLiteral(STRING, LANG);
		PlainLiteral plainliteralB = new PlainLiteral(STRING, LANG);

		ObjectTests.runTestHashCode(plainliteralA, plainliteralB);
	}

}
