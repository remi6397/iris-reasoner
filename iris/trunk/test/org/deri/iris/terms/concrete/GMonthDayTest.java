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

public class GMonthDayTest extends TestCase {
	private static final int DAY = 13;

	private static final int DAYMORE = 14;

	private static final int DAYMORE1 = 15;

	private static final int MONTH = Calendar.FEBRUARY;

	private static final Calendar REFERENCE = new GregorianCalendar(TimeZone
			.getTimeZone("GMT"));

	static {
		REFERENCE.clear();
		REFERENCE.set(Calendar.DAY_OF_MONTH, DAY);
		REFERENCE.set(Calendar.MONTH, MONTH);
	}

	public void testBasic() {
		final GMonthDay basic = new GMonthDay(MONTH, DAY);

		assertEquals("Somethin wrong with constructor", basic, new GMonthDay(
				MONTH, DAY));
		assertEquals("Somethin wrong with constructor", basic, new GMonthDay(
				REFERENCE));
		assertEquals("Somethin wrong with getDay", DAY, basic.getDay());
		assertEquals("Somethin wrong with getMonth", MONTH, basic.getMonth());
	}

	public void testEquals() {
		ObjectTest.runTestEquals(new GMonthDay(MONTH, DAY), new GMonthDay(
				MONTH, DAY), new GMonthDay(MONTH, DAYMORE));
	}

	public void testClone() {
		ObjectTest.runTestClone(new GMonthDay(MONTH, DAY));
	}

	public void testCompareTo() {
		ObjectTest.runTestCompareTo(new GMonthDay(MONTH, DAY), new GMonthDay(
				MONTH, DAY), new GMonthDay(MONTH, DAYMORE), new GMonthDay(
				MONTH, DAYMORE1));
	}

	public void testHashCode() {
		ObjectTest.runTestHashCode(new GMonthDay(MONTH, DAY), new GMonthDay(
				MONTH, DAY));
	}

	public static Test suite() {
		return new TestSuite(GMonthDayTest.class, GMonthDayTest.class
				.getSimpleName());
	}

	public void testGetMinValue() {
		TermTest.runTestGetMinValue(new GMonthDay(Calendar.JANUARY, 2));
	}

	public void testAdd() {
		assertEquals(
				"The sum of 9months 9days and 1 Feb 1 1:1:1 must be 10months 10days",
				new GMonthDay(10, 10), (new GMonthDay(9, 9)).add(new DateTime(
						1, Calendar.FEBRUARY, 1, 1, 1, 1)));
		assertEquals("The sum of 9months 9days and "
				+ "duration(1year 1month 1day 1:1:1) must be 10months 10days",
				new GMonthDay(10, 10), (new GMonthDay(9, 9)).add(new Duration(
						1, 1, 1, 1, 1, 1)));
		assertEquals(
				"The sum of 9months 9days and 1 Feb 1 must be 10months 10days",
				new GMonthDay(10, 10), (new GMonthDay(9, 9)).add(new DateTerm(
						1, Calendar.FEBRUARY, 1)));
		assertEquals(
				"The sum of 10months 10days and 1 year must be 10months 10days",
				new GMonthDay(10, 10), (new GMonthDay(10, 10))
						.add(new GYear(1)));
		assertEquals(
				"The sum of 9months 10days and 1 month must be 10months 10days",
				new GMonthDay(10, 10), (new GMonthDay(9, 10))
						.add(new GMonth(1)));
		assertEquals(
				"The sum of 10months 9days and 1 day must be 10months 10days",
				new GMonthDay(10, 10), (new GMonthDay(10, 9)).add(new GDay(1)));
		assertEquals(
				"The sum of 9months 10days and 1 year and 1 month must be 10months 10days",
				new GMonthDay(10, 10), (new GMonthDay(9, 10))
						.add(new GYearMonth(1, 1)));
		assertEquals(
				"The sum of 9months 9days and 1 month and 1 day must be 10months 10days",
				new GMonthDay(10, 10), (new GMonthDay(9, 9)).add(new GMonthDay(
						1, 1)));
	}

	public void testSubtract() {
		assertEquals(
				"The difference of 10months 10days and 1 Feb 1 1:1:1 must be "
						+ "9months 9days", new GMonthDay(9, 9), (new GMonthDay(
						10, 10)).subtract(new DateTime(1, Calendar.FEBRUARY, 1,
						1, 1, 1)));
		assertEquals("The difference of 10months 10days and "
				+ "duration(1year 1month 1day 1:1:1) must be 9months 9days",
				new GMonthDay(9, 9), (new GMonthDay(10, 10))
						.subtract(new Duration(1, 1, 1, 1, 1, 1)));
		assertEquals(
				"The difference of 10months 10days and 1 Feb 1 must be 9months 9days",
				new GMonthDay(9, 9), (new GMonthDay(10, 10))
						.subtract(new DateTerm(1, Calendar.FEBRUARY, 1)));
		assertEquals(
				"The difference of 9months 9days and 1 year must be 9months 9days",
				new GMonthDay(9, 9), (new GMonthDay(9, 9))
						.subtract(new GYear(1)));
		assertEquals(
				"The difference of 10months 9days and 1 month must be 9months 9days",
				new GMonthDay(9, 9), (new GMonthDay(10, 9))
						.subtract(new GMonth(1)));
		assertEquals(
				"The difference of 9months 10days and 1 day must be 9months 9days",
				new GMonthDay(9, 9), (new GMonthDay(9, 10))
						.subtract(new GDay(1)));
		assertEquals(
				"The difference of 10months 9days and 1 year and 1 month must be "
						+ "9months 9days", new GMonthDay(9, 9), (new GMonthDay(
						10, 9)).subtract(new GYearMonth(1, 1)));
		assertEquals(
				"The difference of 10monts 10days and 1 month and 1 day must be "
						+ "9months 9days", new GMonthDay(9, 9), (new GMonthDay(
						10, 10)).subtract(new GMonthDay(1, 1)));
	}
}
