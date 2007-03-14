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

public class GYearTest extends TestCase {
	private static final Calendar REFERENCE = new GregorianCalendar(TimeZone
			.getTimeZone("GMT"));

	private static final int YEAR = 2005;

	private static final int YEARMORE = 2006;

	private static final int YEARMORE1 = 2007;
	static {
		REFERENCE.clear();
		REFERENCE.set(Calendar.YEAR, YEAR);
	}

	public void testBasic() {
		final GYear D_REFERENCE = new GYear(YEAR);

		assertEquals("Somethin wrong with constructor", D_REFERENCE,
				new GYear(YEAR));
		assertEquals("Somethin wrong with constructor", D_REFERENCE,
				new GYear(REFERENCE));
		assertEquals("Somethin wrong with getYear", YEAR, D_REFERENCE.getYear());
	}

	public void testEquals() {
		ObjectTest.runTestEquals(new GYear(YEAR), new GYear(YEAR),
				new GYear(YEARMORE));
	}

	public void testClone() {
		ObjectTest.runTestClone(new GYear(YEAR));
	}

	public void testCompareTo() {
		ObjectTest.runTestCompareTo(new GYear(YEAR), new GYear(YEAR),
				new GYear(YEARMORE), new GYear(YEARMORE1));
	}

	public void testHashCode() {
		ObjectTest.runTestHashCode(new GYear(YEAR), new GYear(YEAR));
	}

	public static Test suite() {
		return new TestSuite(GYearTest.class, GYearTest.class.getSimpleName());
	}
	
	public void testGetMinValue() {
		TermTest.runTestGetMinValue(new GYear(2));
	}
}
