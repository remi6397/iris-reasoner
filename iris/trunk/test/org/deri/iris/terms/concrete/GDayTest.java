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
		final GDayImpl equal = new GDayImpl(DAY);

		assertEquals("Somethin wrong with constructor", equal,
				new GDayImpl(DAY));
		assertEquals("Somethin wrong with constructor", equal, new GDayImpl(
				REFERENCE));
		assertEquals("Somethin wrong with getDay", DAY, equal.getDay());
	}

	public void testEquals() {
		ObjectTest.runTestEquals(new GDayImpl(DAY), new GDayImpl(DAY),
				new GDayImpl(DAYMORE));
	}

	public void testClone() {
		ObjectTest.runTestClone(new GDayImpl(DAY));
	}

	public void testCompareTo() {
		ObjectTest.runTestCompareTo(new GDayImpl(DAY), new GDayImpl(DAY),
				new GDayImpl(DAYMORE), new GDayImpl(DAYMORE1));
	}

	public void testHashCode() {
		ObjectTest.runTestHashCode(new GDayImpl(DAY), new GDayImpl(DAY));
	}

	public static Test suite() {
		return new TestSuite(GDayTest.class, GDayTest.class.getSimpleName());
	}
}
