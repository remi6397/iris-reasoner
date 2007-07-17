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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTests;
import org.deri.iris.TermTests;
import org.deri.iris.factory.Factory;

/**
 * <p>
 * Tests the functionality of the <code>DoubleTerm</code>.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision$
 */
public class DoubleTest extends TestCase {

	private final static double BASIC = 0.1d;

	private final static double MORE = 0.2d;

	private final static double MORE1 = 0.3d;

	public static Test suite() {
		return new TestSuite(DoubleTest.class, DoubleTest.class.getSimpleName());
	}

	public void testBasic() {
		DoubleTerm basic = new DoubleTerm(BASIC);
		DoubleTerm changed = new DoubleTerm(MORE);
		changed.setValue(BASIC);
		assertEquals("object not initialized correctly", BASIC, basic
				.getValue());
		assertEquals("setValue(..) doesn't work correctly", basic, changed);
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new DoubleTerm(BASIC), new DoubleTerm(BASIC),
				new DoubleTerm(MORE));
	}

	public void testClone() {
		ObjectTests.runTestClone(new DoubleTerm(BASIC));
	}

	public void testCompare() {
		ObjectTests.runTestCompareTo(new DoubleTerm(BASIC),
				new DoubleTerm(BASIC), new DoubleTerm(MORE), new DoubleTerm(
						MORE1));
	}

	public void testHashCode() {
		ObjectTests
				.runTestHashCode(new DoubleTerm(BASIC), new DoubleTerm(BASIC));
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new DoubleTerm(Double.MIN_VALUE + 0.0001));
	}
}
