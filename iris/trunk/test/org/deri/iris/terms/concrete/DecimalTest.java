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

import org.deri.iris.ObjectTest;
import org.deri.iris.TermTest;
import org.deri.iris.factory.Factory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests the functionality of the <code>DecimalTerm</code>.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author richi
 * @version $Revision$
 * @date $Date$
 */
public class DecimalTest extends TestCase {

	private final static double BASIC = 0.1d;

	private final static double MORE = 0.2d;

	private final static double MORE1 = 0.3d;

	public static Test suite() {
		return new TestSuite(DecimalTest.class, DecimalTest.class
				.getSimpleName());
	}

	public void testBasic() {
		DecimalTerm basic = new DecimalTerm(BASIC);
		DecimalTerm changed = new DecimalTerm(MORE);
		changed.setValue(BASIC);
		assertEquals("object not initialized correctly", BASIC, basic
				.getValue());
		assertEquals("setValue(..) doesn't work correctly", basic, changed);
	}

	public void testEquals() {
		ObjectTest.runTestEquals(new DecimalTerm(BASIC),
				new DecimalTerm(BASIC), new DecimalTerm(MORE));
	}

	public void testClone() {
		ObjectTest.runTestClone(new DecimalTerm(BASIC));
	}

	public void testCompare() {
		ObjectTest.runTestCompareTo(new DecimalTerm(BASIC), new DecimalTerm(
				BASIC), new DecimalTerm(MORE), new DecimalTerm(MORE1));
	}

	public void testHashCode() {
		ObjectTest.runTestHashCode(new DecimalTerm(BASIC), new DecimalTerm(
				BASIC));
	}

	public void testGetMinValue() {
		TermTest.runTestGetMinValue(new DecimalTerm(Double.MIN_VALUE + 0.0001));
	}

	public void testAdd() {
		assertEquals("The sum of 5 and -2 should be 3", new DecimalTerm(3),
				(new DecimalTerm(5)).add(Factory.CONCRETE.createInteger(-2)));
		assertEquals("The sum of 2.5 and 7.5 should be 10",
				new DecimalTerm(10), (new DecimalTerm(2.5))
						.add(new DecimalTerm(7.5)));
	}

	public void testSubtract() {
		assertEquals("The difference of 5 and -2 should be 7", new DecimalTerm(
				7), (new DecimalTerm(5)).subtract(Factory.CONCRETE
				.createInteger(-2)));
		assertEquals("The difference of 2.5 and 7.5 should be -5",
				new DecimalTerm(-5), (new DecimalTerm(2.5))
						.subtract(new DecimalTerm(7.5)));
	}

	public void testMultiply() {
		assertEquals("The product of 5 and -2 should be -10", new DecimalTerm(
				-10), (new DecimalTerm(5)).multiply(Factory.CONCRETE
				.createInteger(-2)));
		assertEquals("The product of 2.5 and 7.5 should be 18.75",
				new DecimalTerm(18.75), (new DecimalTerm(2.5))
						.multiply(new DecimalTerm(7.5)));
	}

	public void testDivide() {
		assertEquals("The quotient of 5 and -2 should be -2.5",
				new DecimalTerm(-2.5), (new DecimalTerm(5))
						.divide(Factory.CONCRETE.createInteger(-2)));
		assertEquals("The quotient of 5 and 2 should be 2.5", new DecimalTerm(
				2.5), (new DecimalTerm(5)).divide(Factory.CONCRETE
				.createInteger(2)));
	}
}
