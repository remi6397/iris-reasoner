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

import org.deri.iris.ObjectTest;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;

/**
 * @author richi
 * 
 */
public class AtomTest extends TestCase {

	private static final int ARITY = 3;

	private static final String SYMBOL = "date";

	private static final List<ITerm> TERMS;

	private static final List<ITerm> TERMSMORE;

	private static final IPredicate PREDICATE = BASIC.createPredicate(SYMBOL,
			ARITY);

	private static final IPredicate PREDICATEMORE = BASIC.createPredicate(
			SYMBOL + 1, ARITY);

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
		return new TestSuite(AtomTest.class, AtomTest.class.getSimpleName());
	}

	public void testBasic() {
		Atom REFERENCE = new Atom(PREDICATE, TERMS);
		Atom MUTABLE = new Atom(PREDICATE);
		boolean rightExceptionThrown = false;
		List<ITerm> tooManyTerms = new ArrayList<ITerm>(ARITY + 1);
		tooManyTerms.add(TERM.createString("a"));
		tooManyTerms.add(TERM.createString("b"));
		tooManyTerms.add(TERM.createString("c"));
		tooManyTerms.add(TERM.createString("d"));

		MUTABLE.setTerm(TERM.createString("a"), 0);
		MUTABLE.setTerm(TERM.createString("b"), 1);
		MUTABLE.setTerm(TERM.createString("c"), 2);

		try {
			MUTABLE.setTerm(TERM.createString("d"), 3);
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
		ObjectTest.runTestEquals(new Atom(PREDICATE, TERMS), new Atom(
				PREDICATE, TERMS), new Atom(PREDICATE, TERMSMORE));
	}

	public void testHashCode() {
		ObjectTest.runTestHashCode(new Atom(PREDICATE, TERMS), new Atom(
				PREDICATE, TERMS));
	}

	public void testCompareTo() {
		ObjectTest.runTestCompareTo(new Atom(PREDICATE, TERMS), new Atom(
				PREDICATE, TERMS), new Atom(PREDICATE, TERMSMORE), new Atom(
				PREDICATEMORE, TERMS));
	}

}
