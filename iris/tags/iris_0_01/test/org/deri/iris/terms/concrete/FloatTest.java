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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author richi
 * 
 */
public class FloatTest extends TestCase {

	private final static float BASIC = 0.1f;

	private final static float MORE = 0.2f;

	private final static float MORE1 = 0.3f;

	public static Test suite() {
		return new TestSuite(FloatTest.class, FloatTest.class
				.getSimpleName());
	}

	public void testBasic() {
		FloatTerm basic = new FloatTerm(BASIC);
		FloatTerm changed = new FloatTerm(MORE);
		changed.setValue(BASIC);
		assertEquals("object not initialized correctly", BASIC, basic
				.getValue());
		assertEquals("setValue(..) doesn't work correctly", basic, changed);
	}

	public void testEquals() {
		ObjectTest.runTestEquals(new FloatTerm(BASIC),
				new FloatTerm(BASIC), new FloatTerm(MORE));
	}

	public void testClone() {
		ObjectTest.runTestClone(new FloatTerm(BASIC));
	}

	public void testCompare() {
		ObjectTest.runTestCompareTo(new FloatTerm(BASIC), new FloatTerm(
				BASIC), new FloatTerm(MORE), new FloatTerm(MORE1));
	}

	public void testHashCode() {
		ObjectTest.runTestHashCode(new FloatTerm(BASIC), new FloatTerm(
				BASIC));
	}
	
	public void testGetMinValue() {
		TermTest.runTestGetMinValue(new FloatTerm(Float.MIN_VALUE + 0.0001f));
	}
}
