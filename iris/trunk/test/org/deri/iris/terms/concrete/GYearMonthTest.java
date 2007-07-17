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
		ObjectTests.runTestClone(new GYearMonth(YEAR, MONTH));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new GYearMonth(YEAR, MONTH),
				new GYearMonth(YEAR, MONTH), new GYearMonth(YEAR,
						MONTHMORE), new GYearMonth(YEAR, MONTHMORE1));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new GYearMonth(YEAR, MONTH),
				new GYearMonth(YEAR, MONTH));
	}

	public static Test suite() {
		return new TestSuite(GYearMonthTest.class, GYearMonthTest.class
				.getSimpleName());
	}
	
	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new GYearMonth(1, 2));
	}
}
