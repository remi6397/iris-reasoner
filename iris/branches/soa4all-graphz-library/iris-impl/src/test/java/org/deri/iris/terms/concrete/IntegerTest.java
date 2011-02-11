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

/**
 * <p>
 * Tests the functionality of the <code>IntegerTerm</code>.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision$
 */
public class IntegerTest extends TestCase {

	private final static int BASIC = 1;

	private final static int MORE = 2;

	private final static int MORE1 = 3;

	public static Test suite() {
		return new TestSuite(IntegerTest.class, IntegerTest.class
				.getSimpleName());
	}

	public void testBasic() {
		IntegerTerm basic = new IntegerTerm(BASIC);
		assertEquals("object not initialized correctly",
				Integer.valueOf(BASIC), Integer.valueOf(basic.getValue().intValue()));
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new IntegerTerm(BASIC),
				new IntegerTerm(BASIC), new IntegerTerm(MORE));
	}

	public void testCompare() {
		ObjectTests.runTestCompareTo(new IntegerTerm(BASIC), new IntegerTerm(
				BASIC), new IntegerTerm(MORE), new IntegerTerm(MORE1));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new IntegerTerm(BASIC), new IntegerTerm(
				BASIC));
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new IntegerTerm(Integer.MIN_VALUE + 1));
	}
}
