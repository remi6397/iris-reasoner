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
 * Simple tests for the implementation of the rdf:XMLLiteral
 * 
 * @author gigi
 * 
 */
public class XMLLiteralTest extends TestCase {

	private static final String STRING = "<tag>Text</tag>";
	private static final String LANG = "en";

	public void testEquals() {
		XMLLiteral literal1 = new XMLLiteral(STRING, LANG);
		XMLLiteral literal2 = new XMLLiteral(STRING, LANG);
		XMLLiteral literal3 = new XMLLiteral(STRING, "de");
		XMLLiteral literal4 = new XMLLiteral(STRING, "en");
		XMLLiteral literal5 = new XMLLiteral("<tag>Foobar</tag>", LANG);

		ObjectTests.runTestEquals(literal1, literal2, literal5);
		ObjectTests.runTestEquals(literal3, literal4, literal5);
	}

	public void testHashCode() {
		XMLLiteral literal1 = new XMLLiteral(STRING, LANG);
		XMLLiteral literal2 = new XMLLiteral(STRING, LANG);

		ObjectTests.runTestHashCode(literal1, literal2);
	}

}
