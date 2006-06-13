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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deri.iris.ObjectTest;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.terms.Variable;
import org.deri.iris.terms.concrete.IntegerTerm;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author richi
 * 
 */
public class HeadTest extends TestCase {

	private static final List<ILiteral> LITERALS;

	private static final List<ILiteral> LITERALS_UNEQ;

	private static final List<ITerm> VARIABLES;

	static {
		List<ILiteral> tempLiterals = new ArrayList<ILiteral>();
		List<ILiteral> tempLiteralsUnequ = new ArrayList<ILiteral>();
		List<ITerm> tempVariables = new ArrayList<ITerm>();

		ILiteral literal = new Literal(new Predicate("sin", 1));
		literal.setTerm(new IntegerTerm(1), 0);
		tempLiterals.add(literal);
		tempLiteralsUnequ.add(literal);

		literal = new Literal(new Predicate("cos", 1));
		literal.setTerm(new Variable("X"), 0);
		tempLiterals.add(literal);
		tempLiteralsUnequ.add(literal);
		tempVariables.add(literal.getTerm(0));

		literal = new Literal(new Predicate("date", 3));
		literal.setTerm(new IntegerTerm(2005), 0);
		literal.setTerm(new IntegerTerm(12), 1);
		literal.setTerm(new IntegerTerm(24), 2);
		tempLiterals.add(literal);

		LITERALS = Collections.unmodifiableList(tempLiterals);
		LITERALS_UNEQ = Collections.unmodifiableList(tempLiteralsUnequ);
		VARIABLES = Collections.unmodifiableList(tempVariables);
	}

	public static Test suite() {
		return new TestSuite(HeadTest.class, HeadTest.class.getSimpleName());
	}

	public static void testBasic() {
		final Head REFERENCE = new Head(LITERALS);
		assertEquals("getHeadLength doesn't work properly", LITERALS.size(),
				REFERENCE.getHeadLenght());
		assertTrue("getHeadLiterals doesn't work properly", REFERENCE
				.getHeadLiterals().equals(LITERALS));
		for (int iCounter = 0; iCounter < LITERALS.size(); iCounter++) {
			assertEquals("getHeadLiteral doesn't work properly", LITERALS
					.get(iCounter), REFERENCE.getHeadLiteral(iCounter));
		}
		assertEquals("The size of getHeadVariables doesn't work properly",
				VARIABLES.size(), REFERENCE.getHeadVariables().size());
		assertTrue("getHeadVariables doesn't work properly", REFERENCE
				.getHeadVariables().containsAll(VARIABLES));
	}

	public static void testEquals() {
		ObjectTest.runTestEquals(new Head(LITERALS), new Head(LITERALS),
				new Head(LITERALS_UNEQ));
	}

	public static void testHashCode() {
		ObjectTest.runTestHashCode(new Head(LITERALS), new Head(LITERALS));
	}

}
