/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
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
		PlainLiteral rdfText = new PlainLiteral(STRING, LANG);

		assertEquals(STRING, rdfText.getString());
		assertEquals(LANG, rdfText.getLang());

		PlainLiteral rdfTextParsing = new PlainLiteral(LANG_AND_TEXT);

		assertEquals(STRING, rdfTextParsing.getString());
		assertEquals(LANG, rdfTextParsing.getLang());

		assertEquals("Object not initialized correctly", "foobar",
				new PlainLiteral("foobar").getValue()[0]);
	}

	public void testSimpleParsing() {
		PlainLiteral rdfText = new PlainLiteral("parser-test@home@de-AT");
		assertEquals("parser-test@home", rdfText.getString());
		assertEquals("de-AT", rdfText.getLang());

		rdfText = new PlainLiteral("some other rdf:text", "de");
		assertEquals("some other rdf:text", rdfText.getString());
		assertEquals("de", rdfText.getLang());

		rdfText = new PlainLiteral("some simple string without any language@");
		assertEquals("some simple string without any language", rdfText
				.getString());
		assertEquals("", rdfText.getLang());

		// rdf:text containing only the @ and a language tag
		rdfText = new PlainLiteral("@en");
		assertEquals("", rdfText.getString());
		assertEquals("en", rdfText.getLang());

		rdfText = new PlainLiteral("@");
		assertEquals("", rdfText.getString());
		assertEquals("", rdfText.getLang());
	}

	public void testToString() {
		PlainLiteral rdfText = new PlainLiteral("");
		assertEquals("@", rdfText.toString());

		rdfText = new PlainLiteral("some other rdf:text", "de");
		assertEquals("some other rdf:text@de", rdfText.toString());
	}

	public void testCompareTo() {
		PlainLiteral rdfTextA = new PlainLiteral("rdf:text to compare 1@en");
		PlainLiteral rdfTextB = new PlainLiteral("rdf:text to compare 2@en");
		PlainLiteral rdfTextC = new PlainLiteral("rdf:text to compare 1@en");

		ObjectTests.runTestCompareTo(rdfTextA, rdfTextC, rdfTextB);

		assertEquals(-1, rdfTextA.compareTo(rdfTextB));
		assertEquals(1, rdfTextB.compareTo(rdfTextA));

		PlainLiteral rdfTextA2 = new PlainLiteral("rdf:text to compare 1@en");
		assertEquals(0, rdfTextA.compareTo(rdfTextA2));
	}

	public void testEquals() {
		PlainLiteral rdfTextA = new PlainLiteral("rdf:text to compare 1@en");
		PlainLiteral rdfTextB = new PlainLiteral("rdf:text to compare 1@de");
		PlainLiteral rdfTextC = new PlainLiteral("rdf:text to compare 2@en");

		ObjectTests.runTestEquals(rdfTextA, rdfTextB, rdfTextC);
	}

	public void testHashCode() {
		PlainLiteral rdfTextA = new PlainLiteral(STRING, LANG);
		PlainLiteral rdfTextB = new PlainLiteral(STRING, LANG);

		ObjectTests.runTestHashCode(rdfTextA, rdfTextB);
	}

}
