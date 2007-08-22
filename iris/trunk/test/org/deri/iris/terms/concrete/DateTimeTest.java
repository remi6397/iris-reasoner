/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.deri.iris.ObjectTests;
import org.deri.iris.TermTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DateTimeTest extends TestCase {
	private static final Calendar CALENDAR = new GregorianCalendar(TimeZone
			.getTimeZone("GMT+1"));

	private static final String SREFERENCE = "2005-03-10T13:56:00GMT+01:00";

	static {
		CALENDAR.clear();
		CALENDAR.set(2005, Calendar.MARCH, 10, 13, 56, 00);
	}

	public void testBasic() {
		DateTime dt = DateTime.parse(SREFERENCE);

		assertEquals("Something wrong with getYear", 2005, dt.getYear());
		assertEquals("Something wrong with getMonth", Calendar.MARCH, dt
				.getMonth());
		assertEquals("Something wrong with getDay", 10, dt.getDay());
		assertEquals("Something wrong with getHour", 13, dt.getHour());
		assertEquals("Something wrong with getMinute", 56, dt.getMinute());
		assertEquals("Something wrong with getSecond", 00, dt.getSecond());

		DateTime dt0 = new DateTime(2005, Calendar.MARCH, 10, 13, 56, 0, 1, 0);
		assertEquals("Something wrong with setting or equals", dt, dt0);
		dt0 = new DateTime(CALENDAR);
		assertEquals("Something wrong with setting or equals", dt, dt0);
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new DateTime(2000, 1, 1, 12, 01, 00, 1, 0),
				new DateTime(2000, 1, 1, 12, 01, 00, 1, 0), new DateTime(2000,
						1, 1, 12, 02, 00, 1, 0));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new DateTime(2000, 1, 1, 12, 01, 00, 1, 0),
				new DateTime(2000, 1, 1, 11, 01, 00, 0, 0), new DateTime(2000,
						1, 1, 11, 02, 00, 0, 0), new DateTime(2000, 1, 1, 11,
						03, 00, 0, 0));
	}

	public void testClone() {
		ObjectTests.runTestClone(new DateTime(CALENDAR));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new DateTime(CALENDAR), new DateTime(
				CALENDAR));
	}

	public static Test suite() {
		return new TestSuite(DateTimeTest.class, DateTimeTest.class
				.getSimpleName());
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new DateTime(0, 0, 0, 0, 0, 1));
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
			new DateTime(2000, 0, 1, 0, 0, 0, -1, 1);
			fail("It is possible to create a datetime with a negative tzHour and positive tzMinute");
		} catch (IllegalArgumentException e) {
		}

		try {
			new DateTime(2000, 0, 1, 0, 0, 0, 1, -1);
			fail("It is possible to create a datetime with a positive tzHour and negative tzMinute");
		} catch (IllegalArgumentException e) {
		}

		// the following should be possible
		new DateTime(2000, 0, 1, 0, 0, 0, 0, 0);
		new DateTime(2000, 0, 1, 0, 0, 0, 1, 0);
		new DateTime(2000, 0, 1, 0, 0, 0, 0, 1);
		new DateTime(2000, 0, 1, 0, 0, 0, 1, 1);
		new DateTime(2000, 0, 1, 0, 0, 0, -1, 0);
		new DateTime(2000, 0, 1, 0, 0, 0, 0, -1);
		new DateTime(2000, 0, 1, 0, 0, 0, -1, -1);
	}
}
