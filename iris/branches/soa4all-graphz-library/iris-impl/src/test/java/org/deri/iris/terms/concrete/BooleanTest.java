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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTests;
import org.deri.iris.TermTests;

public class BooleanTest extends TestCase {

	public void testBasic() {
		BooleanTerm trueTerm = new BooleanTerm(true);
		BooleanTerm trueTermParsed = new BooleanTerm("TRUE");

		assertEquals("Instanciation didn't work", Boolean.TRUE, trueTerm.getValue());
		assertEquals("The parsing didn't work", trueTerm, trueTermParsed);
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new BooleanTerm(true), new BooleanTerm(true),
				new BooleanTerm(false));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new BooleanTerm(false), new BooleanTerm(
				false), new BooleanTerm(true));
	}

	public void testHashCode() {
		ObjectTests
				.runTestHashCode(new BooleanTerm(true), new BooleanTerm(true));
	}

	public static Test suite() {
		return new TestSuite(BooleanTest.class, BooleanTest.class
				.getSimpleName());
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new BooleanTerm(true));
	}
}
