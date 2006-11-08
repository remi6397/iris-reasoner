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
package org.deri.iris.evaluation.seminaive;

import static org.deri.iris.MiscHelper.createLiteral;
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.PROGRAM;
import static org.deri.iris.factory.Factory.BUILTIN;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import junit.framework.TestSuite;


import org.deri.iris.api.IEDB;
import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.evaluation.seminaive.MiscOps;

/**
 * <p>
 * Tests the methods in the MiscOps class.
 * </p>
 * <p>
 * $Id: MiscOpsTest.java,v 1.4 2006-11-08 13:27:53 graham Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.4 $
 * @date $Date: 2006-11-08 13:27:53 $
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
	 * p(r_0, r_1, r_2) :- r(r_1, r_2), EQUAL(r_0, a)
	 * p(r_0, r_1, r_2) :- r(r_1, r_0), EQUAL(r_2, r_0)
	 * 
	 */
	public void testRectrify() {
		final ILiteral hl = BASIC.createLiteral(true, BASIC.createPredicate(
				"p", 3), BASIC.createTuple(TERM.createString("a"), TERM
				.createVariable("X"), TERM.createVariable("Y")));
		final IRule r0 = BASIC.createRule(BASIC.createHead(hl), BASIC
				.createBody(createLiteral("r", "X", "Y")));
		final IRule rec0 = BASIC.createRule(BASIC.createHead(createLiteral("p",
				"r_0", "r_1", "r_2")), BASIC.createBody(createLiteral("r",
				"r_1", "r_2"), BASIC.createLiteral(true, BUILTIN.createEqual(
				TERM.createVariable("r_0"), TERM.createString("a")))));
		assertEquals(rec0, MiscOps.rectify(r0));
		
		final IRule r1 = BASIC
				.createRule(
						BASIC.createHead(createLiteral("p", "X", "Y", "X")),
						BASIC.createBody(createLiteral("r", "Y", "X")));
		final IRule rec1 = BASIC.createRule(BASIC.createHead(createLiteral("p",
				"r_0", "r_1", "r_2")), BASIC.createBody(createLiteral("r",
				"r_1", "r_0"), BASIC.createLiteral(true, BUILTIN.createEqual(
				TERM.createVariable("r_2"), TERM.createVariable("r_0")))));
		assertEquals(rec1, MiscOps.rectify(r1));
	}
	
	public void testStratification() {
		
		IPredicate p = BASIC.createPredicate("p", 1);
		IPredicate q = BASIC.createPredicate("q", 1);
		IPredicate r = BASIC.createPredicate("r", 1);
		IPredicate s = BASIC.createPredicate("s", 1);
		
		Set<IRule> rules = new HashSet<IRule>();
		
		// ******First rule:  p(x) :- r(x)
		// Computing head
		ILiteral lh = BASIC.createLiteral(true, p, BASIC.createTuple(
							TERM.createVariable("X")));
		IHead h = BASIC.createHead(lh);
		
		// Computing body
		ILiteral lb = BASIC.createLiteral(true, r, BASIC.createTuple(
								TERM.createVariable("X")));
		IBody b = BASIC.createBody(lb);

		rules.add(BASIC.createRule(h, b));

		// *****Second rule: p(x) :- p(x)
		// Computing head
		lh = BASIC.createLiteral(true, p, BASIC.createTuple(
							TERM.createVariable("X")));
		h = BASIC.createHead(lh);
		
		// Computing body
		lb = BASIC.createLiteral(true, p, BASIC.createTuple(
						TERM.createVariable("X")));
		b = BASIC.createBody(lb);

		rules.add(BASIC.createRule(h, b));

		// *****Third rule: q(x) :- s(x), ~p(x)
		//Computing head
		lh = BASIC.createLiteral(true, q, BASIC.createTuple(
							TERM.createVariable("X")));
		h = BASIC.createHead(lh);
		
		// Computing body
		List<ILiteral> bl = new ArrayList<ILiteral>();
		bl.add(BASIC.createLiteral(true, s, BASIC.createTuple(
									TERM.createVariable("X"))));
		bl.add(BASIC.createLiteral(false, p, BASIC.createTuple(
						TERM.createVariable("X"))));
		
		b = BASIC.createBody(bl);

		rules.add(BASIC.createRule(h, b));
		
		//System.out.println("****** input: ******");
		// for (IRule _r : rules) {
		// System.out.println(_r);
		// }
		IEDB e = PROGRAM.createEDB(); 	
		for (IRule _r : rules) {
			e.addRule(_r);
		}
	
		assertEquals(true, MiscOps.stratify(e));
		//System.out.println("****** output: ******");
		//System.out.println("Is stratified? " + MiscOps.stratify(e));
		
		rules = new HashSet<IRule>();
		
		p = BASIC.createPredicate("p", 1);
		q = BASIC.createPredicate("q", 1);
		r = BASIC.createPredicate("r", 1);
		
		// ******First rule:  p(x) :- r(x), ~q(x)
		// Computing head
		lh = BASIC.createLiteral(true, p, BASIC.createTuple(
							TERM.createVariable("X")));
		h = BASIC.createHead(lh);
		
		// Computing body
		bl = new ArrayList<ILiteral>();
		bl.add(BASIC.createLiteral(true, r, BASIC.createTuple(
									TERM.createVariable("X"))));
		bl.add(BASIC.createLiteral(false, q, BASIC.createTuple(
						TERM.createVariable("X"))));
		
		b = BASIC.createBody(bl);

		rules.add(BASIC.createRule(h, b));
		
		// ******Second rule:  q(x) :- r(x), ~p(x)
		// Computing head
		lh = BASIC.createLiteral(true, q, BASIC.createTuple(
							TERM.createVariable("X")));
		h = BASIC.createHead(lh);
		
		// Computing body
		bl = new ArrayList<ILiteral>();
		bl.add(BASIC.createLiteral(true, r, BASIC.createTuple(
									TERM.createVariable("X"))));
		bl.add(BASIC.createLiteral(false, p, BASIC.createTuple(
						TERM.createVariable("X"))));
		
		b = BASIC.createBody(bl);

		rules.add(BASIC.createRule(h, b));
		
		// System.out.println("****** input: ******");
		// for (IRule _r : rules) {
		// System.out.println(_r);
		// }
		
		e = PROGRAM.createEDB(); 
		for (IRule _r : rules) {
			e.addRule(_r);
		}
		
		// System.out.println("****** output: ******");
		// System.out.println("Is stratified? " + MiscOps.stratify(e));
		assertEquals(false, MiscOps.stratify(e));
	}
}
