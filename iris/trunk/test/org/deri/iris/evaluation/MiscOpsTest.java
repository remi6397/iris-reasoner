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
package org.deri.iris.evaluation;

import static org.deri.iris.MiscHelper.createLiteral;
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.BUILTIN;
import static org.deri.iris.factory.Factory.TERM;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.basics.seminaive.ConstLiteral;

/**
 * <p>
 * Tests the methods in the MiscOps class.
 * </p>
 * <p>
 * $Id: MiscOpsTest.java,v 1.10 2007-10-24 15:08:47 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.10 $
 */
public class MiscOpsTest extends TestCase {

	// often used terms
	private static final ITerm X = TERM.createVariable("X");
	private static final ITerm Y = TERM.createVariable("Y");
	private static final ITerm Z = TERM.createVariable("Z");
	private static final ITerm X0 = TERM.createVariable("?X_0");
	private static final ITerm X1 = TERM.createVariable("?X_1");
	private static final ITerm X2 = TERM.createVariable("?X_2");
	private static final IPredicate p = BASIC.createPredicate("p", 2);

	public static TestSuite suite() {
		return new TestSuite(MiscOpsTest.class, MiscOpsTest.class
				.getSimpleName());
	}

	public void testRectrifySimple0() {
		final ILiteral hl = BASIC.createLiteral(true, BASIC.createPredicate(
				"p", 3), BASIC.createTuple(TERM.createString("a"), TERM
				.createVariable("X"), TERM.createVariable("Y")));
	 	// p(a, X, Y) :- r(X, Y)
		final IRule r0 = BASIC.createRule(BASIC.createHead(hl), BASIC
				.createBody(createLiteral("r", "X", "Y")));
	 	// p(?X_0, ?X_1, ?X_2) :- r(?X_1, ?X_2), ?X_0 = a
		final IRule rec0 = BASIC.createRule(BASIC.createHead(createLiteral("p",
				"?X_0", "?X_1", "?X_2")), BASIC.createBody(createLiteral("r",
				"?X_1", "?X_2"), new ConstLiteral(
						true,TERM.createString("a"),TERM.createVariable("?X_0"))));

		assertEquals(rec0, MiscOps.rectify(r0));
	}
		
	public void testRectifySimple1() {
	 	// p(X, Y, X) :- r(Y, X)
		final IRule r = BASIC
				.createRule(
						BASIC.createHead(createLiteral("p", "X", "Y", "X")),
						BASIC.createBody(createLiteral("r", "Y", "X")));
	 	// p(?X_0, ?X_1, ?X_2) :- r(?X_1, ?X_0), EQUAL(?X_0, ?X_2)
		final IRule rec = BASIC.createRule(BASIC.createHead(createLiteral("p",
				"?X_0", "?X_1", "?X_2")), BASIC.createBody(createLiteral("r",
				"?X_1", "?X_0"), BASIC.createLiteral(true, BUILTIN.createEqual(
				TERM.createVariable("?X_0"), TERM.createVariable("?X_2")))));

		assertEquals(rec, MiscOps.rectify(r));
	}


	public void testRectifySimple2() {
		// p(X, Y) :- p(X, Z), p(Z, Y)
		final IRule in = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, p, BASIC.createTuple(X, Y))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X, Z)), 
					BASIC.createLiteral(true, p, BASIC.createTuple(Z, Y))));
		final IRule inBackup = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, p, BASIC.createTuple(X, Y))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X, Z)), 
					BASIC.createLiteral(true, p, BASIC.createTuple(Z, Y))));
		// p(?X_0, ?X_1) :- p(?X_0, Z), p(Z, ?X_1)
		final IRule out = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, p, BASIC.createTuple(X0, X1))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X0, Z)), 
					BASIC.createLiteral(true, p, BASIC.createTuple(Z, X1))));

		assertEquals(out, MiscOps.rectify(in));
		// the original rule must not be altered
		assertEquals("The original rule must remain the same", inBackup, in);
	}


	public void testRectifyExtreme() {
		// q(X, X, X) :- p(X, X), p(X, X)
		final IRule in = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, BASIC.createPredicate("q", 3), BASIC.createTuple(X, X, X))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X, X)), 
					BASIC.createLiteral(true, p, BASIC.createTuple(X, X))));
		final IRule backup = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, BASIC.createPredicate("q", 3), BASIC.createTuple(X, X, X))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X, X)), 
					BASIC.createLiteral(true, p, BASIC.createTuple(X, X))));
		// q(?X_0, ?X_1, ?X_2) :- p(?X_0, ?X_0), p(?X_0, ?X_0), EQUAL(?X_0, ?X_1), EQUAL(?X_1, ?X_2)
		final IRule out = BASIC.createRule(
				BASIC.createHead(BASIC.createLiteral(true, BASIC.createPredicate("q", 3), BASIC.createTuple(X0, X1, X2))), 
				BASIC.createBody(
					BASIC.createLiteral(true, p, BASIC.createTuple(X0, X0)), 
					BASIC.createLiteral(true, p, BASIC.createTuple(X0, X0)), 
					BASIC.createLiteral(true, BUILTIN.createEqual(X0, X1)), 
					BASIC.createLiteral(true, BUILTIN.createEqual(X1, X2))));

		assertEquals(out, MiscOps.rectify(in));
		// the original rule must not be altered
		assertEquals("The original rule must remain the same", backup, in);
	}

	public void testRectifyBuiltin() {
		// p(X, Y) :- p(X, Z), Z = Y
		final IRule in = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, p, BASIC.createTuple(X, Y))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X, Z)), 
					BASIC.createLiteral(true, BUILTIN.createEqual(Z, Y))));
		final IRule backup = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, p, BASIC.createTuple(X, Y))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X, Z)), 
					BASIC.createLiteral(true, BUILTIN.createEqual(Z, Y))));
		// p(?X_0, ?X_1) :- p(?X_0, Z), Z = ?X_1
		final IRule out = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, p, BASIC.createTuple(X0, X1))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X0, Z)), 
					BASIC.createLiteral(true, BUILTIN.createEqual(Z, X1))));

		assertEquals(out, MiscOps.rectify(in));
		// the original rule must not be altered
		assertEquals("The original rule must remain the same", backup, in);
	}
	
}
