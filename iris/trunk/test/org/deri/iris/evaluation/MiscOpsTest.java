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
import static org.deri.iris.factory.Factory.PROGRAM;
import static org.deri.iris.factory.Factory.TERM;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.basics.seminaive.ConstLiteral;

/**
 * <p>
 * Tests the methods in the MiscOps class.
 * </p>
 * <p>
 * $Id: MiscOpsTest.java,v 1.3 2007-05-11 09:52:25 darko_anicic Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.3 $
 */
public class MiscOpsTest extends TestCase {

	public static TestSuite suite() {
		return new TestSuite(MiscOpsTest.class, MiscOpsTest.class
				.getSimpleName());
	}

	/**
	 * Tests the rectify method.
	 * 
	 * Input:
	 * p(a, X, Y) :- r(X, Y)
	 * p(X, Y, X) :- r(Y, X)
	 * 
	 * Output:
	 * p(?X_0, ?X_1, ?X_2) :- r(?X_1, ?X_2), ?X_0 = a
	 * p(?X_0, ?X_1, ?X_2) :- r(?X_1, ?X_0), EQUAL(?X_2, ?X_0)
	 * 
	 */
	public void testRectrify() {
		final ILiteral hl = BASIC.createLiteral(true, BASIC.createPredicate(
				"p", 3), BASIC.createTuple(TERM.createString("a"), TERM
				.createVariable("X"), TERM.createVariable("Y")));
		final IRule r0 = BASIC.createRule(BASIC.createHead(hl), BASIC
				.createBody(createLiteral("r", "X", "Y")));
		final IRule rec0 = BASIC.createRule(BASIC.createHead(createLiteral("p",
				"?X_0", "?X_1", "?X_2")), BASIC.createBody(createLiteral("r",
				"?X_1", "?X_2"), new ConstLiteral(
						true,TERM.createString("a"),TERM.createVariable("?X_0"))));
		assertEquals(rec0, MiscOps.rectify(r0));
		
		final IRule r1 = BASIC
				.createRule(
						BASIC.createHead(createLiteral("p", "X", "Y", "X")),
						BASIC.createBody(createLiteral("r", "Y", "X")));
		final IRule rec1 = BASIC.createRule(BASIC.createHead(createLiteral("p",
				"?X_0", "?X_1", "?X_2")), BASIC.createBody(createLiteral("r",
				"?X_1", "?X_0"), BASIC.createLiteral(true, BUILTIN.createEqual(
				TERM.createVariable("?X_0"), TERM.createVariable("?X_2")))));
		assertEquals(rec1, MiscOps.rectify(r1));
	}
	
	public void testStratify() {

		final String stratProg = "p(?X) :- r(?X).\n" + 
			"p(?X) :- p(?X).\n" + 
			"q(?X) :- s(?X), !p(?X).";
		
		IProgram e = PROGRAM.createProgram(); 	
		org.deri.iris.compiler.Parser.parse(stratProg, e);
	
		assertEquals(true, MiscOps.stratify(e));
		
		final String unstratProg = "p(?X) :- r(?X), !q(?X).\n" + 
			"q(?X) :- r(?X), !p(?X).";

		// remove all the rules from the program again. (this is because
		// of a bug which still remains in iris
		while (!e.getRules().isEmpty()) {
			e.removeRule(e.getRules().iterator().next());
		}

		e = PROGRAM.createProgram(); 
		org.deri.iris.compiler.Parser.parse(unstratProg, e);
		
		MiscOps.stratify(e);
		for (final IRule rule : e.getRules()) {
			System.out.println(rule);
			for (final ILiteral l : rule.getHeadLiterals()) {
				final IPredicate pred = l.getPredicate();
				System.out.printf("%s: %d\n", pred.getPredicateSymbol(), pred.getStratum());
			}
			for (final ILiteral l : rule.getBodyLiterals()) {
				final IPredicate pred = l.getPredicate();
				System.out.printf("%s: %d\n", pred.getPredicateSymbol(), pred.getStratum());
			}
		}

		assertEquals(false, MiscOps.stratify(e));
	}
}
