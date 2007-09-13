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

public class DurationTest extends TestCase {

	private static final int YEAR = 1;

	private static final int MONTH = 2;

	private static final int DAY = 3;

	private static final int HOUR = 4;

	private static final int MINUTE = 5;

	private static final int SECOND = 6;

	private static final int MILLISECOND = 7;

	public void testBasic() {
		final Duration d = new Duration(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, MILLISECOND);

		assertEquals("Something wrong with getYear", YEAR, d.getYear());
		assertEquals("Something wrong with getMonth", MONTH, d.getMonth());
		assertEquals("Something wrong with getDay", DAY, d.getDay());
		assertEquals("Something wrong with getHour", HOUR, d.getHour());
		assertEquals("Something wrong with getMinute", MINUTE, d.getMinute());
		assertEquals("Something wrong with getSecond", SECOND, d.getSecond());
		assertEquals("Something wrong with getMillisecond", MILLISECOND, d.getMillisecond());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new Duration(2000, 1, 1, 12, 01, 00),
				new Duration(2000, 1, 1, 12, 01, 00), new Duration(2000, 1, 1,
						12, 02, 00));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new Duration(2000, 1, 1, 11, 01, 00),
				new Duration(2000, 1, 1, 11, 01, 00), new Duration(2000, 1, 1,
						11, 02, 00), new Duration(2000, 1, 1, 11, 03, 00));
	}

	public void testClone() {
		// this the underlying duration object is not cloneable
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Duration(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, MILLISECOND), 
				new Duration(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, MILLISECOND));
	}

	public static Test suite() {
		return new TestSuite(DurationTest.class, DurationTest.class
				.getSimpleName());
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new Duration(0, 0, 0, 0, 0, 1));
	}
}
