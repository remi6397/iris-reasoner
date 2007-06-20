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
}
