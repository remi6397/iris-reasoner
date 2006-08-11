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

import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTest;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   24.07.2006 14:19:17
 * 
 */
public class TupleTest extends TestCase {

	private static final int ARITY = 3;
	
	private static List<ITerm> TERMS;
	
	private static List<ITerm> MORETERMS;
	
	private static List<ITerm> tooManyTerms;
	
	private Tuple REFERENCE;
	
	private Tuple MUTABLE;
	
	private Tuple MORE;
	
	private Tuple EVENMORE;
	
	
	public static Test suite() {
		return new TestSuite(TupleTest.class, TupleTest.class
				.getSimpleName());
	}

	public void setUp() {
		List<ITerm> temp = new ArrayList<ITerm>(ARITY);
		temp.add(0, TERM.createString("a"));
		temp.add(1, TERM.createString("b"));
		temp.add(2, TERM.createString("c"));
		TERMS = Collections.unmodifiableList(temp);
		
		REFERENCE = new Tuple(temp);
		
		temp = new ArrayList<ITerm>(ARITY);
		temp.add(0, TERM.createString("b"));
		temp.add(1, TERM.createString("b"));
		temp.add(2, TERM.createString("c"));
		
		MORETERMS = Collections.unmodifiableList(temp);
		
		MUTABLE = new Tuple(ARITY);
		MORE = new Tuple(MORETERMS);
		
		tooManyTerms = new ArrayList<ITerm>(ARITY + 1);
		tooManyTerms.add(TERM.createString("c"));
		tooManyTerms.add(TERM.createString("b"));
		tooManyTerms.add(TERM.createString("c"));
		tooManyTerms.add(TERM.createString("d"));

		EVENMORE = new Tuple(tooManyTerms);
	}

	public void testBasic() {
		boolean rightExceptionThrown = false;
		
		MUTABLE.setTerm(0, TERM.createString("a"));
		MUTABLE.setTerm(1, TERM.createString("b"));
		MUTABLE.setTerm(2, TERM.createString("c"));

		try {
			MUTABLE.setTerm(3, TERM.createString("d"));
		} catch (IndexOutOfBoundsException e) {
			rightExceptionThrown = true;
		} finally {
			if (!rightExceptionThrown) {
				throw new AssertionFailedError(
						"You shouldn't be able to add more "
								+ "terms to the atom than the arity allows.");
			}
		}
		try {
			MUTABLE.setTerms(tooManyTerms);
		} catch (IndexOutOfBoundsException e) {
			rightExceptionThrown = true;
		} finally {
			if (!rightExceptionThrown) {
				throw new AssertionFailedError(
						"You shouldn't be able to add more "
								+ "terms to the atom than the arity allows.");
			}
		}
		MUTABLE.setTerms(TERMS);

		for (int iCounter = 0; iCounter < TERMS.size(); iCounter++) {
			assertEquals("getTerm doesn't work properly", TERMS.get(iCounter),
					REFERENCE.getTerm(iCounter));
		}
		for (int iCounter = 0; iCounter < TERMS.size(); iCounter++) {
			assertEquals("getTerm doesn't work properly", TERMS.get(iCounter),
					MUTABLE.getTerm(iCounter));
		}
		assertEquals("The two objects should be equal", REFERENCE.getTerms(), MUTABLE.getTerms());
		assertTrue("The REFERENCE tuple is a ground tuple", REFERENCE.isGround());
		assertEquals("(a, b, c)", REFERENCE.toString());
	}

	
	public void testEquals() {
		// Correct it!
		// something has changed MUTABLE in meantime!
		MUTABLE.setTerms(TERMS);
		ObjectTest.runTestEquals(REFERENCE, MUTABLE, MORE);
	}
	
	public void testCompareTo() {
		// Correct it!
		// something has changed MUTABLE in meantime!
		MUTABLE.setTerms(TERMS);
		ObjectTest.runTestCompareTo(REFERENCE, MUTABLE, MORE,
				EVENMORE);
	}
	
	public void testHashCode() {
		// Correct it!
		// something has changed MUTABLE in meantime!
		MUTABLE.setTerms(TERMS);
		ObjectTest.runTestHashCode(REFERENCE, MUTABLE);
	}
	
	public void testVariables() {
		Set<IVariable> variables = new HashSet<IVariable>();
		IVariable x = Factory.TERM.createVariable("X");
		IVariable y = Factory.TERM.createVariable("Y");
		
		variables.add(x);
		variables.add(y);
		
		IConstructedTerm c1 = Factory.TERM.createConstruct("c1", y);
		IConstructedTerm c2 = Factory.TERM.createConstruct("c2", c1, x);
		List<ITerm> terms = new ArrayList<ITerm>();
		terms.addAll(TERMS);
		terms.add(c2);
		
		assertEquals(variables, 
				Factory.BASIC.createTuple(terms).getVariables());
	}
}
