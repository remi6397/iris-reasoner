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

public class GDayTest extends TestCase {
	private static final int DAY = 13;

	private static final int DAYMORE = 14;

	private static final int DAYMORE1 = 15;

	private static final Calendar REFERENCE = new GregorianCalendar(TimeZone
			.getTimeZone("GMT"));

	static {
		REFERENCE.clear();
		REFERENCE.set(Calendar.DAY_OF_MONTH, DAY);
	}

	public void testBasic() {
		final GDay equal = new GDay(DAY);

		assertEquals("Somethin wrong with constructor", equal, new GDay(DAY));
		assertEquals("Somethin wrong with constructor", equal, new GDay(
				REFERENCE));
		assertEquals("Somethin wrong with getDay", DAY, equal.getDay());
	}

	public void testEquals() {
		ObjectTest.runTestEquals(new GDay(DAY), new GDay(DAY),
				new GDay(DAYMORE));
	}

	public void testClone() {
		ObjectTest.runTestClone(new GDay(DAY));
	}

	public void testCompareTo() {
		ObjectTest.runTestCompareTo(new GDay(DAY), new GDay(DAY), new GDay(
				DAYMORE), new GDay(DAYMORE1));
	}

	public void testHashCode() {
		ObjectTest.runTestHashCode(new GDay(DAY), new GDay(DAY));
	}

	public static Test suite() {
		return new TestSuite(GDayTest.class, GDayTest.class.getSimpleName());
	}

	public void testGetMinValue() {
		TermTest.runTestGetMinValue(new GDay(2));
	}

	public void testAdd() {
		assertEquals("The sum of 9days and 1 Feb 1 1:1:1 must be 10days",
				new GDay(10), (new GDay(9)).add(new DateTime(1,
						Calendar.FEBRUARY, 1, 1, 1, 1)));
		assertEquals("The sum of 9days and "
				+ "duration(1year 1month 1day 1:1:1) must be 10days", new GDay(
				10), (new GDay(9)).add(new Duration(1, 1, 1, 1, 1, 1)));
		assertEquals("The sum of 9days and 1 Feb 1 must be 10days",
				new GDay(10), (new GDay(9)).add(new DateTerm(1,
						Calendar.FEBRUARY, 1)));
		assertEquals("The sum of 10days and 1 year must be 10days",
				new GDay(10), (new GDay(10)).add(new GYear(1)));
		assertEquals("The sum of 10days and 1 month must be 10days", new GDay(
				10), (new GDay(10)).add(new GMonth(1)));
		assertEquals("The sum of 9days and 1 day must be 10days", new GDay(10),
				(new GDay(9)).add(new GDay(1)));
		assertEquals("The sum of 10days and 1 year and 1 month must be 10days",
				new GDay(10), (new GDay(10)).add(new GYearMonth(1, 1)));
		assertEquals("The sum of 9days and 1 month and 1 day must be 10days",
				new GDay(10), (new GDay(9)).add(new GMonthDay(1, 1)));
	}

	public void testSubtract() {
		assertEquals("The difference of 10days and 1 Feb 1 1:1:1 must be "
				+ "9days", new GDay(9), (new GDay(10)).subtract(new DateTime(1,
				Calendar.FEBRUARY, 1, 1, 1, 1)));
		assertEquals("The difference of 10days and "
				+ "duration(1year 1month 1day 1:1:1) must be 9days",
				new GDay(9), (new GDay(10)).subtract(new Duration(1, 1, 1, 1,
						1, 1)));
		assertEquals("The difference of 10days and 1 Feb 1 must be 9days",
				new GDay(9), (new GDay(10)).subtract(new DateTerm(1,
						Calendar.FEBRUARY, 1)));
		assertEquals("The difference of 9days and 1 year must be " + "9days",
				new GDay(9), (new GDay(9)).subtract(new GYear(1)));
		assertEquals("The difference of 9days and 1 month must be 9days",
				new GDay(9), (new GDay(9)).subtract(new GMonth(1)));
		assertEquals("The difference of 10days and 1 day must be 9days",
				new GDay(9), (new GDay(10)).subtract(new GDay(1)));
		assertEquals("The difference of 9days and 1 year and 1 month must be "
				+ "9days", new GDay(9), (new GDay(9)).subtract(new GYearMonth(
				1, 1)));
		assertEquals("The difference of 10days and 1 month and 1 day must be "
				+ "9days", new GDay(9), (new GDay(10)).subtract(new GMonthDay(
				1, 1)));
	}
}
