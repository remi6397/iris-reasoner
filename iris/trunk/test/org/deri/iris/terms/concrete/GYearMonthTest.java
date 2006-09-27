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

public class GYearMonthTest extends TestCase {
	private static final int MONTH = Calendar.FEBRUARY;

	private static final int MONTHMORE = Calendar.MARCH;

	private static final int MONTHMORE1 = Calendar.MAY;

	private static final Calendar REFERENCE = new GregorianCalendar(TimeZone
			.getTimeZone("GMT"));

	private static final int YEAR = 2005;
	static {
		REFERENCE.clear();
		REFERENCE.set(Calendar.MONTH, MONTH);
		REFERENCE.set(Calendar.YEAR, YEAR);
	}

	public void testBasic() {
		final GYearMonth basic = new GYearMonth(YEAR, MONTH);

		assertEquals("Somethin wrong with constructor", basic,
				new GYearMonth(YEAR, MONTH));
		assertEquals("Somethin wrong with constructor", basic,
				new GYearMonth(REFERENCE));
		assertEquals("Somethin wrong with getYear", YEAR, basic.getYear());
		assertEquals("Somethin wrong with getMonth", MONTH, basic.getMonth());
	}

	public void testClone() {
		ObjectTest.runTestClone(new GYearMonth(YEAR, MONTH));
	}

	public void testCompareTo() {
		ObjectTest.runTestCompareTo(new GYearMonth(YEAR, MONTH),
				new GYearMonth(YEAR, MONTH), new GYearMonth(YEAR,
						MONTHMORE), new GYearMonth(YEAR, MONTHMORE1));
	}

	public void testHashCode() {
		ObjectTest.runTestHashCode(new GYearMonth(YEAR, MONTH),
				new GYearMonth(YEAR, MONTH));
	}

	public static Test suite() {
		return new TestSuite(GYearMonthTest.class, GYearMonthTest.class
				.getSimpleName());
	}
	
	public void testGetMinValue() {
		TermTest.runTestGetMinValue(new GYearMonth(1, 2));
	}

	public void testAdd() {
		assertEquals(
				"The sum of 9years 9months and 1 Feb 1 1:1:1 must be 10years 10months",
				new GYearMonth(10, 10), (new GYearMonth(9, 9)).add(new DateTime(
						1, Calendar.FEBRUARY, 1, 1, 1, 1)));
		assertEquals("The sum of 9years 9months and "
				+ "duration(1year 1month 1day 1:1:1) must be 10years 10months",
				new GYearMonth(10, 10), (new GYearMonth(9, 9)).add(new Duration(
						1, 1, 1, 1, 1, 1)));
		assertEquals(
				"The sum of 9years 9months and 1 Feb 1 must be 10years 10months",
				new GYearMonth(10, 10), (new GYearMonth(9, 9)).add(new DateTerm(
						1, Calendar.FEBRUARY, 1)));
		assertEquals(
				"The sum of 9years 10months and 1 year must be 10years 10months",
				new GYearMonth(10, 10), (new GYearMonth(9, 10))
						.add(new GYear(1)));
		assertEquals(
				"The sum of 10years 9months and 1 month must be 10years 10months",
				new GYearMonth(10, 10), (new GYearMonth(10, 9))
						.add(new GMonth(1)));
		assertEquals(
				"The sum of 10years 10months and 1 day must be 10years 10months",
				new GYearMonth(10, 10), (new GYearMonth(10, 10)).add(new GDay(1)));
		assertEquals(
				"The sum of 9years 9months and 1 year and 1 month must be 10years 10months",
				new GYearMonth(10, 10), (new GYearMonth(9, 9))
						.add(new GYearMonth(1, 1)));
		assertEquals(
				"The sum of 10years 9months and 1 month and 1 day must be 10years 10months",
				new GYearMonth(10, 10), (new GYearMonth(10, 9)).add(new GMonthDay(
						1, 1)));
	}

	public void testSubtract() {
		assertEquals(
				"The difference of 10years 10months and 1 Feb 1 1:1:1 must be "
						+ "9years 9months", new GYearMonth(9, 9), (new GYearMonth(
						10, 10)).subtract(new DateTime(1, Calendar.FEBRUARY, 1,
						1, 1, 1)));
		assertEquals("The difference of 110years 10months and "
				+ "duration(1year 1month 1day 1:1:1) must be 9years 9months",
				new GYearMonth(9, 9), (new GYearMonth(10, 10))
						.subtract(new Duration(1, 1, 1, 1, 1, 1)));
		assertEquals(
				"The difference of 10years 10months and 1 Feb 1 must be 9years 9months",
				new GYearMonth(9, 9), (new GYearMonth(10, 10))
						.subtract(new DateTerm(1, Calendar.FEBRUARY, 1)));
		assertEquals(
				"The difference of 10years 9months and 1 year must be 9years 9months",
				new GYearMonth(9, 9), (new GYearMonth(10, 9))
						.subtract(new GYear(1)));
		assertEquals(
				"The difference of 9years 10months and 1 month must be 9years 9months",
				new GYearMonth(9, 9), (new GYearMonth(9, 10))
						.subtract(new GMonth(1)));
		assertEquals(
				"The difference of 9years 9months and 1 day must be 9years 9months",
				new GYearMonth(9, 9), (new GYearMonth(9, 9))
						.subtract(new GDay(1)));
		assertEquals(
				"The difference of 10years 10months and 1 year and 1 month must be "
						+ "9years 9months", new GYearMonth(9, 9), (new GYearMonth(
						10, 10)).subtract(new GYearMonth(1, 1)));
		assertEquals(
				"The difference of 9years 10months and 1 month and 1 day must be "
						+ "9years 9months", new GYearMonth(9, 9), (new GYearMonth(
						9, 10)).subtract(new GMonthDay(1, 1)));
	}
}
