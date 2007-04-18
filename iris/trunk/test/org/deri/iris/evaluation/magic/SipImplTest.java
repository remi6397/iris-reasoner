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
package org.deri.iris.evaluation.magic;

import static org.deri.iris.MiscHelper.createLiteral;
import static org.deri.iris.MiscHelper.createVarList;
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;
import static org.deri.iris.factory.Factory.BUILTIN;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.graph.LabeledEdge;

/**
 * <p>
 * Runns various test on the sip.
 * </p>
 * <p>
 * $Id: SipImplTest.java,v 1.3 2007-04-18 13:33:49 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.3 $
 */
public class SipImplTest extends TestCase {

	/** The first sip to test */
	private static SIPImpl sip;

	/** The second sip to test */
	private static SIPImpl sip1;

	/** The sip with an builtin predicate */
	private static SIPImpl sipBuiltin;

	public static Test suite() {
		return new TestSuite(SipImplTest.class, SipImplTest.class
				.getSimpleName());
	}

	public void setUp() {
		// sg(X, Y) :- up(X, Z1), sg(Z1, Z2), flat(Z2, Z3), sg(Z3, Z4), down(Z4,
		// Y)
		final IRule r = BASIC.createRule(BASIC.createHead(createLiteral("sg",
				"X", "Y")), BASIC.createBody(createLiteral("up", "X", "Z1"),
				createLiteral("sg", "Z1", "Z2"), createLiteral("flat", "Z2",
						"Z3"), createLiteral("sg", "Z3", "Z4"), createLiteral(
						"down", "Z4", "Y")));
		// sg(john, X)
		final IQuery q = BASIC.createQuery(BASIC.createLiteral(true, BASIC
				.createPredicate("sg", 2), BASIC.createTuple(TERM
				.createString("john"), TERM.createVariable("X"))));

		sip = new SIPImpl(r, q);

		// rsg(X, Y) :- up(X, X1), rsg(Y1, X1), down(Y1, Y)
		final IRule r1 = BASIC.createRule(BASIC.createHead(createLiteral("rsg",
				"X", "Y")), BASIC.createBody(createLiteral("up", "X", "X1"),
				createLiteral("rsg", "Y1", "X1"), createLiteral("down", "Y1",
						"Y")));
		// rsg(a, X)
		final IQuery q1 = BASIC.createQuery(BASIC.createLiteral(true, BASIC
				.createPredicate("rsg", 2), BASIC.createTuple(TERM
				.createString("a"), TERM.createVariable("Y"))));

		sip1 = new SIPImpl(r1, q1);

		// rsg(X, Y) :- up(X, X1), rsg(Y1, X1), Y1 = Y
		final IRule r2 = BASIC.createRule(BASIC.createHead(createLiteral("rsg",
				"X", "Y")), BASIC.createBody(createLiteral("up", "X", "X1"),
				createLiteral("rsg", "Y1", "X1"), BASIC.createLiteral(true,
						BUILTIN.createEqual(TERM.createVariable("Y1"), TERM
								.createVariable("Y")))));
		// rsg(a, X)
		final IQuery q2 = BASIC.createQuery(BASIC.createLiteral(true, BASIC
				.createPredicate("rsg", 2), BASIC.createTuple(TERM
				.createString("a"), TERM.createVariable("Y"))));

		sipBuiltin = new SIPImpl(r2, q2);
	}

	/**
	 * Tests whether the sip contains all expected edges.
	 */
	public void testForEdges() {
		final Set<LabeledEdge<ILiteral, Set<IVariable>>> e = new HashSet<LabeledEdge<ILiteral, Set<IVariable>>>();
		e.add(createEdge(createLiteral("sg", "X", "Y"), createLiteral("up",
				"X", "Z1"), createVarList("X")));
		e.add(createEdge(createLiteral("up", "X", "Z1"), createLiteral("sg",
				"Z1", "Z2"), createVarList("X", "Z1")));
		e.add(createEdge(createLiteral("sg", "Z1", "Z2"), createLiteral("flat",
				"Z2", "Z3"), createVarList("X", "Z1", "Z2")));
		e.add(createEdge(createLiteral("flat", "Z2", "Z3"), createLiteral("sg",
				"Z3", "Z4"), createVarList("X", "Z1", "Z2", "Z3")));
		e.add(createEdge(createLiteral("sg", "Z3", "Z4"), createLiteral("down",
				"Z4", "Y"), createVarList("X", "Z1", "Z2", "Z3", "Z4")));

		assertEquals("The number of edges doesn't match", e.size(), sip
				.getEdges().size());
		assertTrue("The sip must contain all given edges", e.containsAll(sip
				.getEdges()));

		final Set<LabeledEdge<ILiteral, Set<IVariable>>> e1 = new HashSet<LabeledEdge<ILiteral, Set<IVariable>>>();
		e1.add(createEdge(createLiteral("rsg", "X", "Y"), createLiteral("up",
				"X", "X1"), createVarList("X")));
		e1.add(createEdge(createLiteral("up", "X", "X1"), createLiteral("rsg",
				"Y1", "X1"), createVarList("X", "X1")));
		e1.add(createEdge(createLiteral("rsg", "Y1", "X1"), createLiteral(
				"down", "Y1", "Y"), createVarList("X", "X1", "Y1")));

		assertEquals("The number of edges doesn't match", e1.size(), sip1
				.getEdges().size());
		assertTrue("The sip must contain all given edges", e1.containsAll(sip1
				.getEdges()));
	}

	public void testBuiltinSip() {
		// builtins are at the moment not handeled correctly
		final Set<LabeledEdge<ILiteral, Set<IVariable>>> e2 = new HashSet<LabeledEdge<ILiteral, Set<IVariable>>>();
		e2.add(createEdge(createLiteral("rsg", "X", "Y"), createLiteral("up",
				"X", "X1"), createVarList("X")));
		e2.add(createEdge(createLiteral("up", "X", "X1"), createLiteral("rsg",
				"Y1", "X1"), createVarList("X", "X1")));
		e2.add(createEdge(createLiteral("rsg", "Y1", "X1"), createLiteral(
				"down", "Y1", "Y"), createVarList("X", "X1", "Y1")));

		assertEquals("The number of edges doesn't match", e2.size(), sipBuiltin
				.getEdges().size());
		assertTrue("The sip must contain all given edges", e2
				.containsAll(sipBuiltin.getEdges()));
	}

	public void testLiteralOrder() {
		// a(X, Y) :- not b(X, Y1), c(Y1, X1), d(X1, Y)
		// a(john, Y)
		final ILiteral not_b = createLiteral("b", "X", "Y1");
		not_b.setPositive(false);
		final IRule r0 = BASIC.createRule(BASIC.createHead(createLiteral("a",
				"X", "Y")), BASIC.createBody(not_b, createLiteral("c", "Y1",
				"X1"), createLiteral("d", "X1", "Y")));
		final IRule r0_ref = BASIC.createRule(BASIC.createHead(createLiteral(
				"a", "X", "Y")), BASIC.createBody(
				createLiteral("c", "Y1", "X1"), createLiteral("d", "X1", "Y"),
				not_b));
		assertEquals("The sorting order isn't correct", r0_ref, SIPImpl.orderLiterals(r0, Collections
				.singleton(TERM.createVariable("X"))));
		// a(X, Y) :- b(X, Y1), Y1 > X1, d(X1, Y)
		// a(john, Y)
		// final ILiteral eq = BASIC.createLiteral(true, BUILTIN.createGreater(
		// TERM.createVariable("X1"), TERM.createVariable("Y1")));
		// final IRule r1 = BASIC.createRule(BASIC.createHead(createLiteral("a",
		// "X", "Y")), BASIC.createBody(createLiteral("b", "X", "Y1"), eq,
		// createLiteral("d", "X1", "Y")));
	}

	/**
	 * Creates a edge.
	 * 
	 * @param s
	 *            the source literal
	 * @param t
	 *            the target literal
	 * @param v
	 *            the passed variables (label)
	 * @returs the edge
	 * @throws NullPointerException
	 *             if the source, target or label is {@code null}
	 * @throws NullPointerException
	 *             if the label contains {@code null}
	 */
	private static LabeledEdge<ILiteral, Set<IVariable>> createEdge(
			final ILiteral s, final ILiteral t, final Collection<IVariable> v) {
		if ((s == null) || (t == null) || (v == null)) {
			throw new NullPointerException(
					"The source, target and variables must not be null");
		}
		if (v.contains(null)) {
			throw new NullPointerException(
					"The variables must not contain null");
		}
		return new LabeledEdge<ILiteral, Set<IVariable>>(s, t,
				new HashSet<IVariable>(v));
	}
}
