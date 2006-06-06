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

public class BooleanTest extends TestCase {

	public void testBasic() {
		BooleanTerm trueTerm = new BooleanTerm(true);
		BooleanTerm trueTermParsed = BooleanTerm.parse("TRUE");

		assertEquals("Instanciation didn't work", true, trueTerm.getValue());
		assertEquals("The parsing didn't work", trueTerm, trueTermParsed);
	}

	public void testEquals() {
		ObjectTest.runTestEquals(new BooleanTerm(true), new BooleanTerm(true),
				new BooleanTerm(false));
	}

	public void testClone() {
		ObjectTest.runTestClone(new BooleanTerm(true));
	}

	public void testCompareTo() {
		ObjectTest.runTestCompareTo(new BooleanTerm(false), new BooleanTerm(
				false), new BooleanTerm(true));
	}

	public void testHashCode() {
		ObjectTest
				.runTestHashCode(new BooleanTerm(true), new BooleanTerm(true));
	}

	public static Test suite() {
		return new TestSuite(BooleanTest.class, BooleanTest.class
				.getSimpleName());
	}

	public void testGetMinValue() {
		TermTest.runTestGetMinValue(new BooleanTerm(true));
	}
}
