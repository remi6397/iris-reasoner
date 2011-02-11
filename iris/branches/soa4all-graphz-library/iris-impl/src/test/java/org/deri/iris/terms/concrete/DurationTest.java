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

public class DurationTest extends TestCase {

	private static final int YEAR = 1;

	private static final int MONTH = 2;

	private static final int DAY = 3;

	private static final int HOUR = 4;

	private static final int MINUTE = 5;

	private static final int SECOND = 6;

	private static final int MILLISECOND = 7;

	public static Test suite() {
		return new TestSuite(DurationTest.class, DurationTest.class
				.getSimpleName());
	}
	
	public void testBasic() {
		Duration d1 = new Duration( true, YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, MILLISECOND);

		assertEquals("Something wrong with getYear", YEAR, d1.getYear());
		assertEquals("Something wrong with getMonth", MONTH, d1.getMonth());
		assertEquals("Something wrong with getDay", DAY, d1.getDay());
		assertEquals("Something wrong with getHour", HOUR, d1.getHour());
		assertEquals("Something wrong with getMinute", MINUTE, d1.getMinute());
		assertEquals("Something wrong with getSecond", SECOND, d1.getSecond());
		assertEquals("Something wrong with getMillisecond", MILLISECOND, d1.getMillisecond());
		
		Duration d2 = new Duration( true, YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, MILLISECOND);
		assertEquals( d1, d2 );
	}

	public void testDecimalSeconds() {
		double seconds = 1.234567;
		Duration d1 = new Duration(true, YEAR, MONTH, DAY, HOUR, MINUTE, seconds);
		assertEquals( seconds, d1.getDecimalSecond() );

		Duration d2 = new Duration(true, YEAR, MONTH, DAY, HOUR, MINUTE, seconds);
		assertEquals( d1, d2 );
	}
	
	public void testEquals() {
		ObjectTests.runTestEquals(new Duration(true, 2000, 1, 1, 12, 01, 00),
				new Duration(true, 2000, 1, 1, 12, 01, 00), new Duration(true, 2000, 1, 1,
						12, 02, 00));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(
						new Duration(true, 2000, 1, 1, 11, 01, 00),
						new Duration(true, 2000, 1, 1, 11, 01, 00),
						new Duration(true, 2000, 1, 1, 11, 01, 01),
						new Duration(true, 2000, 1, 1, 11, 02, 00));
		
		ObjectTests.runTestCompareTo(
						new Duration(false, 2000, 1, 1, 11, 01, 00),
						new Duration(false, 2000, 1, 1, 11, 01, 00),
						new Duration(false, 2000, 1, 1, 11, 01, 01),
						new Duration(false, 2000, 1, 1, 11, 02, 00));
		
		// should be correct:  http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/
		ObjectTests.runTestCompareTo(
						new Duration(false, 2000, 1, 1, 11, 01, 00),
						new Duration(false, 2000, 1, 1, 11, 01, 00),
						new Duration(true, 2000, 1, 1, 11, 01, 01),
						new Duration(true, 2000, 1, 1, 11, 02, 00));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Duration(true, YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, MILLISECOND), 
				new Duration(true, YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, MILLISECOND));
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new Duration(true, 0, 0, 0, 0, 0, 1));
	}
}
