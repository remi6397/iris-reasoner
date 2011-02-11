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

public class GMonthDayTest extends TestCase {
	private static final int DAY = 13;

	private static final int MONTH = 2;

	public void testBasic() {
		final GMonthDay monthday = new GMonthDay(MONTH, DAY);

		assertEquals("Something wrong with getMonth", MONTH, monthday.getMonth());
		assertEquals("Something wrong with getDay", DAY, monthday.getDay());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new GMonthDay(MONTH, DAY), new GMonthDay(
				MONTH, DAY), new GMonthDay(MONTH, DAY + 1));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new GMonthDay(MONTH, DAY), new GMonthDay(
				MONTH, DAY), new GMonthDay(MONTH, DAY + 1), new GMonthDay(
				MONTH, DAY + 2));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new GMonthDay(MONTH, DAY), new GMonthDay(
				MONTH, DAY));
	}

	public static Test suite() {
		return new TestSuite(GMonthDayTest.class, GMonthDayTest.class
				.getSimpleName());
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new GMonthDay(1, 2));
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
			new GMonthDay(1, 1, -1, 1);
			fail("It is possible to create a monthday with a negative tzHour and positive tzMinute");
		} catch (IllegalArgumentException e) {
		}

		try {
			new GMonthDay(1, 1, 1, -1);
			fail("It is possible to create a monthday with a positive tzHour and negative tzMinute");
		} catch (IllegalArgumentException e) {
		}

		// the following should be possible
		new GMonthDay(1, 1, 0, 0);
		new GMonthDay(1, 1, 1, 0);
		new GMonthDay(1, 1, 0, 1);
		new GMonthDay(1, 1, 1, 1);
		new GMonthDay(1, 1, -1, 0);
		new GMonthDay(1, 1, 0, -1);
		new GMonthDay(1, 1, -1, -1);
	}

	/**
	 * <p>
	 * Chechs whether the months are handeled correctly. The months should
	 * count from 1-12.
	 * </p>
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1792385&group_id=167309&atid=842434">bug #1792385: GMonthDay datatype handles months incorrectly</a>
	 */
	public void testCorrectMonthBehaviour() {
		final GMonthDay m1d1 = new GMonthDay(1, 1);
		// if mounthts woule be from 0 - 11 this would shift to 0
		final GMonthDay m12d1 = new GMonthDay(12, 1);
		assertTrue(m1d1 + " must be smaller than " + m12d1, m1d1.compareTo(m12d1) < 0);
	}
}
