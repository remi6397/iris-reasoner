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
package org.deri.iris.terms;

import java.net.URI;

import junit.framework.TestCase;

import org.deri.iris.ObjectTests;
import org.deri.iris.api.terms.IConcreteTerm;

/**
 * @author Adrian Marte
 */
public abstract class AbstractConcreteTermTest extends TestCase {

	protected abstract IConcreteTerm createBasic();

	protected abstract IConcreteTerm createEqual();

	protected abstract IConcreteTerm createGreater();

	protected abstract String createBasicString();

	protected abstract String createEqualString();

	protected abstract String createGreaterString();

	protected abstract URI getDatatypeIRI();

	private IConcreteTerm basic;

	private IConcreteTerm equal;

	private IConcreteTerm greater;

	private String basicString;

	private String equalString;

	private String greaterString;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		basic = createBasic();
		equal = createEqual();
		greater = createGreater();

		basicString = createBasicString();
		equalString = createEqualString();
		greaterString = createGreaterString();
	}

	public void testNotNull() {
		assertNotNull(basic);
		assertNotNull(equal);
		assertNotNull(greater);

		assertNotNull(basicString);
		assertNotNull(equalString);
		assertNotNull(greaterString);
	}

	public void testToCanonicalString() {
		assertEquals(basicString, basic.toCanonicalString());
		assertEquals(equalString, equal.toCanonicalString());
		assertEquals(greaterString, greater.toCanonicalString());
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(basic, equal, greater);
	}

	public void testEquals() {
		ObjectTests.runTestEquals(basic, equal, greater);
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(basic, equal);
	}

	public void testDatatypeIRI() {
		assertEquals(getDatatypeIRI(), basic.getDatatypeIRI());
		assertEquals(getDatatypeIRI(), equal.getDatatypeIRI());
		assertEquals(getDatatypeIRI(), greater.getDatatypeIRI());
	}

}
