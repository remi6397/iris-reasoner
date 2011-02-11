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

import java.math.BigDecimal;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTests;
import org.deri.iris.TermTests;

/**
 * <p>
 * Tests the functionality of the <code>DecimalTerm</code>.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision$
 */
public class DecimalTest extends TestCase {

	private final static BigDecimal BASIC = new BigDecimal(0.1d);

	private final static BigDecimal MORE = new BigDecimal(0.2d);

	private final static BigDecimal MORE1 = new BigDecimal(0.3d);

	public static Test suite() {
		return new TestSuite(DecimalTest.class, DecimalTest.class
				.getSimpleName());
	}

	public void testBasic() {
		DecimalTerm basic = new DecimalTerm(BASIC);

		assertEquals("object not initialized correctly", BASIC, basic
				.getValue());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new DecimalTerm(BASIC),
				new DecimalTerm(BASIC), new DecimalTerm(MORE));
	}

	public void testCompare() {
		ObjectTests.runTestCompareTo(new DecimalTerm(BASIC), new DecimalTerm(
				BASIC), new DecimalTerm(MORE), new DecimalTerm(MORE1));
	}

	public void testEqualsPositiveNegativeZero() {
		ObjectTests.runTestCompareTo(new DecimalTerm(+0.0),
				new DecimalTerm(-0.0), new DecimalTerm(0.000000001), new DecimalTerm(
								0.000000002));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new DecimalTerm(BASIC), new DecimalTerm(
				BASIC));
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new DecimalTerm(Double.MIN_VALUE + 0.0001));
	}
}
