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

public class TimeTest extends TestCase {
	private static final Calendar CALENDAR = new GregorianCalendar(TimeZone
			.getTimeZone("GMT+1"));

	private static final String SREFERENCE = "13:56:00GMT+01:00";

	static {
		CALENDAR.clear();
		CALENDAR.set(0, 0, 0, 13, 56, 00);
	}

	public void testBasic() {
		Time t = Time.parse(SREFERENCE);

		assertEquals("Something wrong with getHour", 13, t.getHour());
		assertEquals("Something wrong with getMinute", 56, t.getMinute());
		assertEquals("Something wrong with getSecond", 00, t.getSecond());

		Time t0 = new Time(13, 56, 0, 1, 0);
		assertEquals("Something wrong with setting or equals", t, t0);
		t0 = new Time(CALENDAR);
		assertEquals("Something wrong with setting or equals", t, t0);
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new Time(12, 01, 00, 1, 0),
				new Time(12, 01, 00, 1, 0), new Time(12, 02, 00, 1, 0));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new Time(12, 01, 00, 1, 0),
				new Time(11, 01, 00, 0, 0), new Time(11, 02, 00, 0, 0), 
				new Time(11, 03, 00, 0, 0));
	}

	public void testClone() {
		ObjectTests.runTestClone(new Time(CALENDAR));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Time(CALENDAR), new Time(
				CALENDAR));
	}

	public static Test suite() {
		return new TestSuite(TimeTest.class, TimeTest.class
				.getSimpleName());
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new Time(0, 0, 1));
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
			new Time(0, 0, 0, -1, 1);
			fail("It is possible to create a time with a negative tzHour and positive tzMinute");
		} catch (IllegalArgumentException e) {
		}

		try {
			new Time(0, 0, 0, 1, -1);
			fail("It is possible to create a time with a positive tzHour and negative tzMinute");
		} catch (IllegalArgumentException e) {
		}

		// the following should be possible
		new Time(0, 0, 0, 0, 0);
		new Time(0, 0, 0, 1, 0);
		new Time(0, 0, 0, 0, 1);
		new Time(0, 0, 0, 1, 1);
		new Time(0, 0, 0, -1, 0);
		new Time(0, 0, 0, 0, -1);
		new Time(0, 0, 0, -1, -1);
	}
}
