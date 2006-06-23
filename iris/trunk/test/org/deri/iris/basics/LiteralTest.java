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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTest;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.terms.StringTerm;

/**
 * @author richi
 * 
 */
public class LiteralTest extends TestCase {

	private static final boolean NEGATIVE = false;

	private static final int ARITY = 3;

	private static final String SYMBOL = "date";

	private static final List<ITerm> TERMS;

	private static final List<ITerm> TERMSMORE;

	private static final Predicate PREDICATE = new Predicate(SYMBOL, ARITY);

	private static final Predicate PREDICATEMORE = new Predicate(SYMBOL + 1,
			ARITY);

	static {
		List<ITerm> temp = new ArrayList<ITerm>(ARITY);
		temp.add(new StringTerm("a"));
		temp.add(new StringTerm("b"));
		temp.add(new StringTerm("c"));
		TERMS = Collections.unmodifiableList(temp);

		temp = new ArrayList<ITerm>(ARITY);
		temp.add(new StringTerm("a"));
		temp.add(new StringTerm("b"));
		temp.add(new StringTerm("d"));
		TERMSMORE = Collections.unmodifiableList(temp);
	}

	public static Test suite() {
		return new TestSuite(LiteralTest.class, LiteralTest.class
				.getSimpleName());
	}

	public void testBasic() {
		Literal REFERENCE = new Literal(NEGATIVE, BASIC.createAtom(PREDICATE,
				TERMS));
		Literal MUTABLE = new Literal(NEGATIVE, BASIC.createAtom(PREDICATE));
		boolean rightExceptionThrown = false;
		List<ITerm> tooManyTerms = new ArrayList<ITerm>(ARITY + 1);
		tooManyTerms.add(new StringTerm("a"));
		tooManyTerms.add(new StringTerm("b"));
		tooManyTerms.add(new StringTerm("c"));
		tooManyTerms.add(new StringTerm("d"));

		MUTABLE.setTerm(new StringTerm("a"), 0);
		MUTABLE.setTerm(new StringTerm("b"), 1);
		MUTABLE.setTerm(new StringTerm("c"), 2);

		try {
			MUTABLE.setTerm(new StringTerm("d"), 3);
		} catch (IllegalArgumentException e) {
			rightExceptionThrown = true;
		} finally {
			if (!rightExceptionThrown) {
				throw new AssertionFailedError(
						"You shouldn't be able to add more "
								+ "terms to the atom than the predicate allows.");
			}
		}

		try {
			MUTABLE.setTerms(tooManyTerms);
		} catch (IllegalArgumentException e) {
			rightExceptionThrown = true;
		} finally {
			if (!rightExceptionThrown) {
				throw new AssertionFailedError(
						"You shouldn't be able to add more "
								+ "terms to the atom than the predicate allows.");
			}
		}

		MUTABLE.setTerms(TERMS);
		MUTABLE.setPositive(NEGATIVE);

		assertEquals("isPositive doesn't work properly", NEGATIVE, REFERENCE
				.isPositive());
		assertEquals("isPositive doesn't work properly", NEGATIVE, MUTABLE
				.isPositive());
		assertEquals("getPredicate doesn't work properly", PREDICATE, REFERENCE
				.getPredicate());
		assertEquals("getPredicate doesn't work properly", PREDICATE, MUTABLE
				.getPredicate());
		for (int iCounter = 0; iCounter < TERMS.size(); iCounter++) {
			assertEquals("getTerm doesn't work properly", TERMS.get(iCounter),
					REFERENCE.getTerm(iCounter));
		}
		for (int iCounter = 0; iCounter < TERMS.size(); iCounter++) {
			assertEquals("getTerm doesn't work properly", TERMS.get(iCounter),
					MUTABLE.getTerm(iCounter));
		}
		assertEquals("The two objects should be equal", REFERENCE, MUTABLE);

	}

	public void testEquals() {
		ObjectTest.runTestEquals(new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, TERMS)), new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, TERMS)), new Literal(BASIC.createAtom(PREDICATE,
				TERMSMORE)));
		ObjectTest.runTestEquals(new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, TERMS)), new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, TERMS)), new Literal(!NEGATIVE, BASIC.createAtom(
				PREDICATE, TERMS)));
	}

	public void testHashCode() {
		ObjectTest.runTestHashCode(new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, TERMS)), new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, TERMS)));
	}

	public void testCompareTo() {
		ObjectTest.runTestCompareTo(new Literal(BASIC.createAtom(PREDICATE,
				TERMS)), new Literal(BASIC.createAtom(PREDICATE, TERMS)),
				new Literal(BASIC.createAtom(PREDICATE, TERMSMORE)),
				new Literal(BASIC.createAtom(PREDICATEMORE, TERMS)));
		ObjectTest.runTestCompareTo(new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, TERMS)), new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, TERMS)), new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, TERMSMORE)), new Literal(BASIC.createAtom(PREDICATE,
				TERMS)));
	}

}
