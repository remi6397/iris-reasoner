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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTest;
import org.deri.iris.TermTest;

public class DateTest extends TestCase {
	public void testBasic() {
		DateTerm dt = new DateTerm(2005, Calendar.MARCH, 10);

		assertEquals("Something wrong with getYear", 2005, dt.getYear());
		assertEquals("Something wrong with getMonth", Calendar.MARCH, dt
				.getMonth());
		assertEquals("Something wrong with getDay", 10, dt.getDay());
	}

	public void testEquals() {
		ObjectTest.runTestEquals(new DateTerm(2000, 1, 1), new DateTerm(2000,
				1, 1), new DateTerm(2000, 1, 2));
	}

	public void testCompareTo() {
		ObjectTest.runTestCompareTo(new DateTerm(2000, Calendar.JANUARY, 31),
				new DateTerm(2000, Calendar.JANUARY, 31), new DateTerm(2000,
						Calendar.FEBRUARY, 1), new DateTerm(2000,
						Calendar.FEBRUARY, 2));
	}

	public void testClone() {
		ObjectTest.runTestClone(new DateTerm(2005, Calendar.MARCH, 10));
	}

	public void testHashCode() {
		ObjectTest.runTestHashCode(new DateTerm(2005, Calendar.MARCH, 10),
				new DateTerm(2005, Calendar.MARCH, 10));
	}

	public static Test suite() {
		return new TestSuite(DateTest.class, DateTest.class.getSimpleName());
	}

	public void testGetMinValue() {
		TermTest.runTestGetMinValue(new DateTerm(0, Calendar.JANUARY, 2));
	}

	public void testAdd() {
		assertEquals("The sum of 2005 Feb 9 and 1 Feb 1 1:1:1 must be "
				+ "2006 March 10", new DateTerm(2006, Calendar.MARCH, 10),
				(new DateTerm(2005, Calendar.FEBRUARY, 9)).add(new DateTime(1,
						Calendar.FEBRUARY, 1, 1, 1, 1)));
		assertEquals("The sum of 2005 Feb 9 and "
				+ "duration(1year 1month 1day 1:1:1) must be "
				+ "2006 March 10", new DateTerm(2006, Calendar.MARCH, 10),
				(new DateTerm(2005, Calendar.FEBRUARY, 9)).add(new Duration(1,
						1, 1, 1, 1, 1)));
		assertEquals("The sum of 2005 Feb 9 and 1 Feb 1 must be "
				+ "2006 March 10", new DateTerm(2006, Calendar.MARCH, 10),
				(new DateTerm(2005, Calendar.FEBRUARY, 9)).add(new DateTerm(1,
						Calendar.FEBRUARY, 1)));
		assertEquals("The sum of 2005 March 9 and 1 year must be "
				+ "2006 March 10", new DateTerm(2006, Calendar.MARCH, 10),
				(new DateTerm(2005, Calendar.MARCH, 10)).add(new GYear(1)));
		assertEquals("The sum of 2006 Feb 10 and 1 month must be "
				+ "2006 March 10", new DateTerm(2006, Calendar.MARCH, 10),
				(new DateTerm(2006, Calendar.FEBRUARY, 10)).add(new GMonth(1)));
		assertEquals("The sum of 2006 March 9 and 1 day must be "
				+ "2006 March 10", new DateTerm(2006, Calendar.MARCH, 10),
				(new DateTerm(2006, Calendar.MARCH, 9)).add(new GDay(1)));
		assertEquals("The sum of 2005 Feb 10 and 1 year and 1 month must be "
				+ "2006 March 10", new DateTerm(2006, Calendar.MARCH, 10),
				(new DateTerm(2005, Calendar.FEBRUARY, 10)).add(new GYearMonth(
						1, 1)));
		assertEquals("The sum of 2006 Feb 9 and 1 month and 1 day must be "
				+ "2006 March 10", new DateTerm(2006, Calendar.MARCH, 10),
				(new DateTerm(2006, Calendar.FEBRUARY, 9)).add(new GMonthDay(1,
						1)));
	}

	public void testSubtract() {
		assertEquals(
				"The difference of 2006 March 10 and 1 Feb 1 1:1:1 must be "
						+ "2005 Feb 9",
				new DateTerm(2005, Calendar.FEBRUARY, 9), (new DateTerm(2006,
						Calendar.MARCH, 10)).subtract(new DateTime(1,
						Calendar.FEBRUARY, 1, 1, 1, 1)));
		assertEquals("The difference of 2006 March 10 and "
				+ "duration(1year 1month 1day 1:1:1) must be " + "2005 Feb 9",
				new DateTerm(2005, Calendar.FEBRUARY, 9), (new DateTerm(2006,
						Calendar.MARCH, 10)).subtract(new Duration(1, 1, 1, 1,
						1, 1)));
		assertEquals("The difference of 2006 March 10 and 1 Feb 1 must be "
				+ "2005 Feb 9", new DateTerm(2005, Calendar.FEBRUARY, 9),
				(new DateTerm(2006, Calendar.MARCH, 10)).subtract(new DateTerm(
						1, Calendar.FEBRUARY, 1)));
		assertEquals("The difference of 2006 March 10 and 1 year must be "
				+ "2005 March 9", new DateTerm(2005, Calendar.MARCH, 10),
				(new DateTerm(2006, Calendar.MARCH, 10)).subtract(new GYear(1)));
		assertEquals("The difference of 2006 March 10 and 1 month must be "
				+ "2006 Feb 10", new DateTerm(2006, Calendar.FEBRUARY, 10),
				(new DateTerm(2006, Calendar.MARCH, 10))
						.subtract(new GMonth(1)));
		assertEquals("The difference of 2006 March 10 and 1 day must be "
				+ "2006 March 9", new DateTerm(2006, Calendar.MARCH, 9),
				(new DateTerm(2006, Calendar.MARCH, 10)).subtract(new GDay(1)));
		assertEquals(
				"The difference of 2006 March 10 and 1 year and 1 month must be "
						+ "2005 Feb 10", new DateTerm(2005, Calendar.FEBRUARY,
						10), (new DateTerm(2006, Calendar.MARCH, 10))
						.subtract(new GYearMonth(1, 1)));
		assertEquals(
				"The difference of 2006 March 10 and 1 month and 1 day must be "
						+ "2006 Feb 9",
				new DateTerm(2006, Calendar.FEBRUARY, 9), (new DateTerm(2006,
						Calendar.MARCH, 10)).subtract(new GMonthDay(1, 1)));
	}
}
