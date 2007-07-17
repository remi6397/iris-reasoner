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

public class GMonthTest extends TestCase {
	private static final int MONTH = Calendar.MARCH;

	private static final int MONTHMORE = Calendar.APRIL;

	private static final int MONTHMORE1 = Calendar.MAY;

	private static final Calendar REFERENCE = new GregorianCalendar(TimeZone
			.getTimeZone("GMT"));

	static {
		REFERENCE.clear();
		REFERENCE.set(Calendar.MONTH, MONTH);
	}

	public void testBasic() {
		GMonth basic = new GMonth(MONTH);

		assertEquals("Somethin wrong with constructor", basic, new GMonth(
				MONTH));
		assertEquals("Somethin wrong with constructor", basic, new GMonth(
				REFERENCE));
		assertEquals("Somethin wrong with getMonth", MONTH, basic.getMonth());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new GMonth(MONTH), new GMonth(MONTH),
				new GMonth(MONTHMORE));
	}

	public void testClone() {
		ObjectTests.runTestClone(new GMonth(MONTH));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new GMonth(MONTH),
				new GMonth(MONTH), new GMonth(MONTHMORE),
				new GMonth(MONTHMORE1));
	}

	public void testHashCode() {
		ObjectTests
				.runTestHashCode(new GMonth(MONTH), new GMonth(MONTH));
	}

	public static Test suite() {
		return new TestSuite(GMonthTest.class, GMonthTest.class.getSimpleName());
	}
	
	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new GMonth(1));
	}
}
