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

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTests;
import org.deri.iris.api.terms.ITerm;

/**
 * @author richi
 * 
 * Revision 1.1  26.07.2006 09:27:55  Darko Anicic, DERI Innsbruck
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
		temp.add(TERM.createString("a"));
		temp.add(TERM.createString("b"));
		temp.add(TERM.createString("c"));
		TERMS = Collections.unmodifiableList(temp);

		temp = new ArrayList<ITerm>(ARITY);
		temp.add(TERM.createString("a"));
		temp.add(TERM.createString("b"));
		temp.add(TERM.createString("d"));
		TERMSMORE = Collections.unmodifiableList(temp);
	}

	public static Test suite() {
		return new TestSuite(LiteralTest.class, LiteralTest.class
				.getSimpleName());
	}

	public void testBasic() {
		Literal REFERENCE = new Literal(NEGATIVE, BASIC.createAtom(PREDICATE,
				BASIC.createTuple(TERMS)));
		Literal MUTABLE = new Literal(NEGATIVE, BASIC.createAtom(PREDICATE));
		boolean rightExceptionThrown = false;
		List<ITerm> tooManyTerms = new ArrayList<ITerm>(ARITY + 1);
		tooManyTerms.add(TERM.createString("a"));
		tooManyTerms.add(TERM.createString("b"));
		tooManyTerms.add(TERM.createString("c"));
		tooManyTerms.add(TERM.createString("d"));

		MUTABLE.getTuple().setTerm(0, TERM.createString("a"));
		MUTABLE.getTuple().setTerm(1, TERM.createString("b"));
		MUTABLE.getTuple().setTerm(2, TERM.createString("c"));

		try {
			MUTABLE.getTuple().setTerm(3, TERM.createString("d"));
		} catch (IndexOutOfBoundsException e) {
			rightExceptionThrown = true;
		} finally {
			if (!rightExceptionThrown) {
				throw new AssertionFailedError(
						"You shouldn't be able to add more "
								+ "terms to the atom than the predicate allows.");
			}
		}

		try {
			MUTABLE.getTuple().setTerms(tooManyTerms);
		} catch (IndexOutOfBoundsException e) {
			rightExceptionThrown = true;
		} finally {
			if (!rightExceptionThrown) {
				throw new AssertionFailedError(
						"You shouldn't be able to add more "
								+ "terms to the atom than the predicate allows.");
			}
		}

		MUTABLE.getTuple().setTerms(TERMS);
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
					REFERENCE.getTuple().getTerm(iCounter));
		}
		for (int iCounter = 0; iCounter < TERMS.size(); iCounter++) {
			assertEquals("getTerm doesn't work properly", TERMS.get(iCounter),
					MUTABLE.getTuple().getTerm(iCounter));
		}
		assertEquals("The two objects should be equal", REFERENCE, MUTABLE);

	}

	public void testEquals() {
		ObjectTests.runTestEquals(new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, BASIC.createTuple(TERMS))), new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, BASIC.createTuple(TERMS))), new Literal(BASIC.createAtom(PREDICATE,
				BASIC.createTuple(TERMSMORE))));
		ObjectTests.runTestEquals(new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, BASIC.createTuple(TERMS))), new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, BASIC.createTuple(TERMS))), new Literal(!NEGATIVE, BASIC.createAtom(
				PREDICATE, BASIC.createTuple(TERMS))));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, BASIC.createTuple(TERMS))), new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, BASIC.createTuple(TERMS))));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new Literal(BASIC.createAtom(PREDICATE,
				BASIC.createTuple(TERMS))), new Literal(BASIC.createAtom(PREDICATE, 
				BASIC.createTuple(TERMS))),
				new Literal(BASIC.createAtom(PREDICATE, BASIC.createTuple(TERMSMORE))),
				new Literal(BASIC.createAtom(PREDICATEMORE, BASIC.createTuple(TERMS))));
		ObjectTests.runTestCompareTo(new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, BASIC.createTuple(TERMS))), new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, BASIC.createTuple(TERMS))), new Literal(NEGATIVE, BASIC.createAtom(
				PREDICATE, BASIC.createTuple(TERMSMORE))), new Literal(BASIC.createAtom(PREDICATE,
						BASIC.createTuple(TERMS))));
	}

}
