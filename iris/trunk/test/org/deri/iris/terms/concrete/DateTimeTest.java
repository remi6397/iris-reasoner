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

import org.deri.iris.ObjectTest;
import org.deri.iris.TermTest;

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
		ObjectTest.runTestEquals(new DateTime(2000, 1, 1, 12, 01, 00, 1, 0),
				new DateTime(2000, 1, 1, 12, 01, 00, 1, 0), new DateTime(2000,
						1, 1, 12, 02, 00, 1, 0));
	}

	public void testCompareTo() {
		ObjectTest.runTestCompareTo(new DateTime(2000, 1, 1, 12, 01, 00, 1, 0),
				new DateTime(2000, 1, 1, 11, 01, 00, 0, 0), new DateTime(2000,
						1, 1, 11, 02, 00, 0, 0), new DateTime(2000, 1, 1, 11,
						03, 00, 0, 0));
	}

	public void testClone() {
		ObjectTest.runTestClone(new DateTime(CALENDAR));
	}

	public void testHashCode() {
		ObjectTest.runTestHashCode(new DateTime(CALENDAR), new DateTime(
				CALENDAR));
	}

	public static Test suite() {
		return new TestSuite(DateTimeTest.class, DateTimeTest.class
				.getSimpleName());
	}

	public void testGetMinValue() {
		TermTest.runTestGetMinValue(new DateTime(0, 0, 0, 0, 0, 1));
	}

	public void testAdd() {
		assertEquals(
				"The sum of 2005 Feb 9 10:58:59 and 1 Feb 1 1:1:1 must be "
						+ "2006 March 10 12:00:00", new DateTime(2006,
						Calendar.MARCH, 10, 12, 00, 00), (new DateTime(2005,
						Calendar.FEBRUARY, 9, 10, 58, 59)).add(new DateTime(1,
						Calendar.FEBRUARY, 1, 1, 1, 1)));
		assertEquals("The sum of 2005 Feb 9 10:58:59 and "
				+ "duration(1year 1month 1day 1:1:1) must be "
				+ "2006 March 10 12:00:00", new DateTime(2006, Calendar.MARCH,
				10, 12, 00, 00), (new DateTime(2005, Calendar.FEBRUARY, 9, 10,
				58, 59)).add(new Duration(1, 1, 1, 1, 1, 1)));
		assertEquals("The sum of 2005 Feb 9 12:00:00 and 1 Feb 1 must be "
				+ "2006 March 10 12:00:00", new DateTime(2006, Calendar.MARCH,
				10, 12, 00, 00), (new DateTime(2005, Calendar.FEBRUARY, 9, 12,
				00, 00)).add(new DateTerm(1, Calendar.FEBRUARY, 1)));
		assertEquals("The sum of 2005 March 9 12:00:00 and 1 year must be "
				+ "2006 March 10 12:00:00", new DateTime(2006, Calendar.MARCH,
				10, 12, 00, 00), (new DateTime(2005, Calendar.MARCH, 10, 12,
				00, 00)).add(new GYear(1)));
		assertEquals("The sum of 2006 Feb 10 12:00:00 and 1 month must be "
				+ "2006 March 10 12:00:00", new DateTime(2006, Calendar.MARCH,
				10, 12, 00, 00), (new DateTime(2006, Calendar.FEBRUARY, 10, 12,
				00, 00)).add(new GMonth(1)));
		assertEquals("The sum of 2006 March 9 12:00:00 and 1 day must be "
				+ "2006 March 10 12:00:00", new DateTime(2006, Calendar.MARCH,
				10, 12, 00, 00), (new DateTime(2006, Calendar.MARCH, 9, 12, 00,
				00)).add(new GDay(1)));
		assertEquals(
				"The sum of 2005 Feb 10 12:00:00 and 1 year and 1 month must be "
						+ "2006 March 10 12:00:00", new DateTime(2006,
						Calendar.MARCH, 10, 12, 00, 00), (new DateTime(2005,
						Calendar.FEBRUARY, 10, 12, 00, 00)).add(new GYearMonth(
						1, 1)));
		assertEquals(
				"The sum of 2006 Feb 9 12:00:00 and 1 month and 1 day must be "
						+ "2006 March 10 12:00:00", new DateTime(2006,
						Calendar.MARCH, 10, 12, 00, 00), (new DateTime(2006,
						Calendar.FEBRUARY, 9, 12, 00, 00)).add(new GMonthDay(1,
						1)));
	}

	public void testSubtract() {
		assertEquals(
				"The difference of 2006 March 10 12:00:00 and 1 Feb 1 1:1:1 must be "
						+ "2005 Feb 9 10:58:59", new DateTime(2005,
						Calendar.FEBRUARY, 9, 10, 58, 59), (new DateTime(2006,
						Calendar.MARCH, 10, 12, 00, 00)).subtract(new DateTime(
						1, Calendar.FEBRUARY, 1, 1, 1, 1)));
		assertEquals("The difference of 2006 March 10 12:00:00 and "
				+ "duration(1year 1month 1day 1:1:1) must be "
				+ "2005 Feb 9 10:58:59", new DateTime(2005, Calendar.FEBRUARY,
				9, 10, 58, 59), (new DateTime(2006, Calendar.MARCH, 10, 12, 00,
				00)).subtract(new Duration(1, 1, 1, 1, 1, 1)));
		assertEquals(
				"The difference of 2006 March 10 12:00:00 and 1 Feb 1 must be "
						+ "2005 Feb 9 12:00:00", new DateTime(2005,
						Calendar.FEBRUARY, 9, 12, 00, 00), (new DateTime(2006,
						Calendar.MARCH, 10, 12, 00, 00)).subtract(new DateTerm(
						1, Calendar.FEBRUARY, 1)));
		assertEquals(
				"The difference of 2006 March 10 12:00:00 and 1 year must be "
						+ "2005 March 9 12:00:00", new DateTime(2005,
						Calendar.MARCH, 10, 12, 00, 00), (new DateTime(2006,
						Calendar.MARCH, 10, 12, 00, 00)).subtract(new GYear(1)));
		assertEquals(
				"The difference of 2006 March 10 12:00:00 and 1 month must be "
						+ "2006 Feb 10 12:00:00", new DateTime(2006,
						Calendar.FEBRUARY, 10, 12, 00, 00), (new DateTime(2006,
						Calendar.MARCH, 10, 12, 00, 00))
						.subtract(new GMonth(1)));
		assertEquals(
				"The difference of 2006 March 10 12:00:00 and 1 day must be "
						+ "2006 March 9 12:00:00", new DateTime(2006,
						Calendar.MARCH, 9, 12, 00, 00), (new DateTime(2006,
						Calendar.MARCH, 10, 12, 00, 00)).subtract(new GDay(1)));
		assertEquals(
				"The difference of 2006 March 10 12:00:00 and 1 year and 1 month must be "
						+ "2005 Feb 10 12:00:00", new DateTime(2005,
						Calendar.FEBRUARY, 10, 12, 00, 00), (new DateTime(2006,
						Calendar.MARCH, 10, 12, 00, 00))
						.subtract(new GYearMonth(1, 1)));
		assertEquals(
				"The difference of 2006 March 10 12:00:00 and 1 month and 1 day must be "
						+ "2006 Feb 9 12:00:00", new DateTime(2006,
						Calendar.FEBRUARY, 9, 12, 00, 00), (new DateTime(2006,
						Calendar.MARCH, 10, 12, 00, 00))
						.subtract(new GMonthDay(1, 1)));
	}
}
