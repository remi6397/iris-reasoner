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
package org.deri.iris.terms;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTest;
import org.deri.iris.api.terms.ITerm;

/**
 * @author richi
 * 
 */
public class ConstantTermTest extends TestCase {

	private static final String SYMBOL = "and";

	private static ConstructedTerm BASIC;

	private static ConstructedTerm MORE;

	private static ConstructedTerm MORE1;

	public static Test suite() {
		return new TestSuite(ConstantTermTest.class, ConstantTermTest.class
				.getSimpleName());
	}

	public void setUp() {
		List<ITerm> terms = new ArrayList<ITerm>();
		terms.add(0, new StringTerm("a"));
		terms.add(1, new StringTerm("b"));

		BASIC = new ConstructedTerm(SYMBOL, terms);

		terms = new ArrayList<ITerm>();
		terms.add(0, new StringTerm("a"));
		terms.add(1, new StringTerm("c"));

		MORE = new ConstructedTerm(SYMBOL, terms);

		terms = new ArrayList<ITerm>();
		terms.add(0, new StringTerm("a"));
		terms.add(1, new StringTerm("c"));
		terms.add(2, new StringTerm("a"));

		MORE1 = new ConstructedTerm(SYMBOL, terms);
	}

	public void testBasic() {
		List<ITerm> terms = new ArrayList<ITerm>();
		terms.add(0, new StringTerm("a"));
		terms.add(1, new StringTerm("b"));

		assertEquals("Object not initialized correctly", SYMBOL, BASIC
				.getFunctionSymbol());
		assertEquals("The collections must have the same size", terms.size(),
				BASIC.getParameters().size());
		assertTrue("The collections must contain the same elements", BASIC
				.getParameters().containsAll(terms));
	}

	public void testEquals() {
		ObjectTest.runTestEquals(BASIC, BASIC.clone(), MORE1);
	}

	public void testClone() {
		ObjectTest.runTestClone(BASIC);
	}

	public void testHashCode() {
		ObjectTest.runTestHashCode(BASIC, BASIC.clone());
	}

	public void testCompareTo() {
		ObjectTest.runTestCompareTo(BASIC, (ConstructedTerm) BASIC.clone(), MORE,
				MORE1);
	}
}
