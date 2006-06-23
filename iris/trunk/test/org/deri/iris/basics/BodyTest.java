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
package org.deri.iris.basics;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTest;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.terms.concrete.IntegerTerm;

/**
 * @author richi
 * 
 */
public class BodyTest extends TestCase {

	private static final List<ILiteral> LITERALS;

	private static final List<ILiteral> LITERALS_UNEQ;

	private static final List<ITerm> VARIABLES;

	static {
		List<ILiteral> tempLiterals = new ArrayList<ILiteral>();
		List<ILiteral> tempLiteralsUnequ = new ArrayList<ILiteral>();
		List<ITerm> tempVariables = new ArrayList<ITerm>();

		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate(
				"sin", 1));
		literal.setTerm(new IntegerTerm(1), 0);
		tempLiterals.add(literal);
		tempLiteralsUnequ.add(literal);

		literal = BASIC.createLiteral(true, BASIC.createPredicate("cos", 1));
		literal.setTerm(TERM.createVariable("X"), 0);
		tempLiterals.add(literal);
		tempLiteralsUnequ.add(literal);
		tempVariables.add(literal.getTerm(0));

		literal = BASIC.createLiteral(true, BASIC.createPredicate("date", 3));
		literal.setTerm(new IntegerTerm(2005), 0);
		literal.setTerm(new IntegerTerm(12), 1);
		literal.setTerm(new IntegerTerm(24), 2);
		tempLiterals.add(literal);

		LITERALS = Collections.unmodifiableList(tempLiterals);
		LITERALS_UNEQ = Collections.unmodifiableList(tempLiteralsUnequ);
		VARIABLES = Collections.unmodifiableList(tempVariables);
	}

	public static Test suite() {
		return new TestSuite(BodyTest.class, BodyTest.class.getSimpleName());
	}

	public static void testBasic() {
		final Body REFERENCE = new Body(LITERALS);
		assertEquals("getBodyLenght doesn't work properly", LITERALS.size(),
				REFERENCE.getBodyLenght());
		assertTrue("getBodyLiterals doesn't work properly", REFERENCE
				.getBodyLiterals().equals(LITERALS));
		for (int iCounter = 0; iCounter < LITERALS.size(); iCounter++) {
			assertEquals("getBodyLiteral doesn't work properly", LITERALS
					.get(iCounter), REFERENCE.getBodyLiteral(iCounter));
		}
		assertEquals("The size of getBodyVariables doesn't work properly",
				VARIABLES.size(), REFERENCE.getBodyVariables().size());
		assertTrue("getBodyVariables doesn't work properly", REFERENCE
				.getBodyVariables().containsAll(VARIABLES));
	}

	public static void testEquals() {
		ObjectTest.runTestEquals(new Body(LITERALS), new Body(LITERALS),
				new Body(LITERALS_UNEQ));
	}

	public static void testHashCode() {
		ObjectTest.runTestHashCode(new Body(LITERALS), new Body(LITERALS));
	}

}
