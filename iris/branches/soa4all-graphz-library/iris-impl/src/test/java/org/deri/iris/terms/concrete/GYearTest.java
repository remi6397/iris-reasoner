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

public class GYearTest extends TestCase {

	private static final int YEAR = 2005;

	public void testBasic() {
		final GYear gyear = new GYear(YEAR);

		assertEquals("Something wrong with getYear", YEAR, gyear.getYear());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new GYear(YEAR), new GYear(YEAR),
				new GYear(YEAR + 1));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new GYear(YEAR), new GYear(YEAR),
				new GYear(YEAR + 1), new GYear(YEAR + 2));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new GYear(YEAR), new GYear(YEAR));
	}

	public static Test suite() {
		return new TestSuite(GYearTest.class, GYearTest.class.getSimpleName());
	}
	
	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new GYear(2));
	}

	/**
	 * <p>
	 * This test checks whether it is possible to specify inconsisntent
	 * timezones. E.g. a timezone with positive hours and negative minutes.
	 * </p>
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1778705&group_id=167309&atid=842434">bug #1778705: it is possible to specify inconsistent timezones</a>
	 */
	public void testConsistentTimezones() {
		try {
			new GYear(2000, -1, 1);
			fail("It is possible to create a year with a negative tzHour and positive tzMinute");
		} catch (IllegalArgumentException e) {
		}

		try {
			new GYear(2000, 1, -1);
			fail("It is possible to create a year with a positive tzHour and negative tzMinute");
		} catch (IllegalArgumentException e) {
		}

		// the following should be possible
		new GYear(2000, 0, 0);
		new GYear(2000, 1, 0);
		new GYear(2000, 0, 1);
		new GYear(2000, 1, 1);
		new GYear(2000, -1, 0);
		new GYear(2000, 0, -1);
		new GYear(2000, -1, -1);
	}
}
