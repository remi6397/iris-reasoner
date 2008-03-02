/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
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
package org.deri.iris.optimisations;

import static org.deri.iris.MiscHelper.createLiteral;
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.BUILTIN;
import static org.deri.iris.factory.Factory.TERM;
import java.util.Arrays;
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
import org.deri.iris.compiler.Parser;
import org.deri.iris.graph.LabeledEdge;

/**
 * <p>
 * Runns various test on the sip.
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 */
public class SipImplTest extends TestCase {

	public static Test suite() {
		return new TestSuite(SipImplTest.class, SipImplTest.class
				.getSimpleName());
	}

	/**
	 * Tests whether the sip contains all expected edges.
	 */
	public void testForEdges0() throws Exception {
		final String prog = "sg(?X, ?Y) :- up(?X, ?Z1), sg(?Z1, ?Z2), flat(?Z2, ?Z3), sg(?Z3, ?Z4), down(?Z4, ?Y).\n"
						 + "?- sg('john', ?X).";
		final Parser p = new Parser();
		p.parse(prog);
		final IRule r = p.getRules().iterator().next();
		final IQuery q = p.getQueries().iterator().next();
		final SIPImpl sip = new SIPImpl(r, q);

		final IVariable X = TERM.createVariable("X");
		final IVariable Z1 = TERM.createVariable("Z1");
		final IVariable Z2 = TERM.createVariable("Z2");
		final IVariable Z3 = TERM.createVariable("Z3");
		final IVariable Z4 = TERM.createVariable("Z4");

		final Set<LabeledEdge<ILiteral, Set<IVariable>>> edges = new HashSet<LabeledEdge<ILiteral, Set<IVariable>>>();
		edges.add(createEdge(createLiteral("sg", "X", "Y"), createLiteral("up", "X", "Z1"), X));
		edges.add(createEdge(createLiteral("up", "X", "Z1"), createLiteral("sg", "Z1", "Z2"), Z1));
		edges.add(createEdge(createLiteral("sg", "Z1", "Z2"), createLiteral("flat", "Z2", "Z3"), Z2));
		edges.add(createEdge(createLiteral("flat", "Z2", "Z3"), createLiteral("sg", "Z3", "Z4"), Z3));
		edges.add(createEdge(createLiteral("sg", "Z3", "Z4"), createLiteral("down", "Z4", "Y"), Z4));

		assertEquals("The edge set does not match.", edges, sip.getEdges());
	}

	/**
	 * Tests whether the sip contains all expected edges.
	 */
	public void testForEdges1() throws Exception {
		final String prog = "rsg(?X, ?Y) :- up(?X, ?X1), rsg(?Y1, ?X1), down(?Y1, ?Y).\n"
						  + "?- rsg('a', ?X).";
		final Parser p = new Parser();
		p.parse(prog);
		final IRule r = p.getRules().iterator().next();
		final IQuery q = p.getQueries().iterator().next();
		final SIPImpl sip = new SIPImpl(r, q);

		final IVariable X = TERM.createVariable("X");
		final IVariable X1 = TERM.createVariable("X1");
		final IVariable Y1 = TERM.createVariable("Y1");

		final Set<LabeledEdge<ILiteral, Set<IVariable>>> edges = new HashSet<LabeledEdge<ILiteral, Set<IVariable>>>();
		edges.add(createEdge(createLiteral("rsg", "X", "Y"), createLiteral("up", "X", "X1"), X));
		edges.add(createEdge(createLiteral("up", "X", "X1"), createLiteral("rsg", "Y1", "X1"), X1));
		edges.add(createEdge(createLiteral("rsg", "Y1", "X1"), createLiteral("down", "Y1", "Y"), Y1));

		assertEquals("The edge set does not match.", edges, sip.getEdges());
	}

	/**
	 * Tests whether the sip contains all expected edges.
	 */
	public void testEqualBuiltinSip() throws Exception {
		final String prog = "rsg(?X, ?Y) :- up(?X, ?X1), rsg(?Y1, ?X1), ?Y1 = ?Y.\n"
						  + "?- rsg('a', ?X).";
		final Parser p = new Parser();
		p.parse(prog);
		final IRule r = p.getRules().iterator().next();
		final IQuery q = p.getQueries().iterator().next();
		final SIPImpl sip = new SIPImpl(r, q);

		final IVariable X = TERM.createVariable("X");
		final IVariable X1 = TERM.createVariable("X1");
		final IVariable Y1 = TERM.createVariable("Y1");

		// builtins are at the moment not handeled correctly
		final Set<LabeledEdge<ILiteral, Set<IVariable>>> edges = new HashSet<LabeledEdge<ILiteral, Set<IVariable>>>();
		edges.add(createEdge(createLiteral("rsg", "X", "Y"), createLiteral("up", "X", "X1"), X));
		edges.add(createEdge(createLiteral("up", "X", "X1"), createLiteral("rsg", "Y1", "X1"), X1));
		edges.add(createEdge(createLiteral("rsg", "Y1", "X1"), 
					BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y1"), TERM.createVariable("Y"))),
					Y1));

		assertEquals("The edge set does not match.", edges, sip.getEdges());
	}

	public void testLiteralOrder0() {
		// a(X, Y) :- not b(X, Y1), c(Y1, X1), d(X1, Y)
		// a(john, Y)
		final ILiteral not_b = createLiteral(false, "b", "X", "Y1");
		final IRule r = BASIC.createRule(
				Arrays.asList(createLiteral("a", "X", "Y")), 
				Arrays.asList(not_b, createLiteral("c", "Y1", "X1"), createLiteral("d", "X1", "Y")));
		final IRule r_ref = BASIC.createRule(
				Arrays.asList(createLiteral( "a", "X", "Y")), 
				Arrays.asList(createLiteral("c", "Y1", "X1"), createLiteral("d", "X1", "Y"), not_b));
		assertEquals("The sorting order isn't correct", r_ref, SIPImpl.orderLiterals(r, Collections
				.singleton(TERM.createVariable("X"))));
	}

	public void testLiteralOrder1() {
		// a(X, Y) :- b(X, Y1), Y1 > X1, d(X1, Y)
		// a(john, Y)
		final ILiteral gt = BASIC.createLiteral(true, BUILTIN.createGreater( TERM.createVariable("X1"), TERM.createVariable("Y1")));
		final IRule r = BASIC.createRule(
				Arrays.asList(createLiteral("a", "X", "Y")), 
				Arrays.asList(createLiteral("b", "X", "Y1"), gt, createLiteral("d", "X1", "Y")));
		final IRule r_ref = BASIC.createRule(
				Arrays.asList(createLiteral("a", "X", "Y")), 
				Arrays.asList(createLiteral("b", "X", "Y1"), gt, createLiteral("d", "X1", "Y")));
		assertEquals("The sorting order isn't correct", r_ref, SIPImpl.orderLiterals(r, Collections.singleton(TERM.createVariable("X"))));
	}

	/**
	 * <p>
	 * Tests the behaviour for rules containing equal literals.
	 * </p>
	 * <p>
	 * <b>Note: Rules containing one literal multible times will only
	 * contain one edge represeinting it, no matter, how often such a
	 * literal occurs in the rule.</b>
	 * </p>
	 */
	public void testEqualLiterals() throws Exception {
		final String prog = "tmp(?X) :- p(?X), p(?X), p(?X).\n"
					      + "?- tmp(?X).";
		final Parser p = new Parser();
		p.parse(prog);
		final IRule r = p.getRules().iterator().next();
		final IQuery q = p.getQueries().iterator().next();
		final SIPImpl sip = new SIPImpl(r, q);

		final IVariable X = TERM.createVariable("X");
		final ILiteral lit = createLiteral("p", "X");

		final Set<LabeledEdge<ILiteral, Set<IVariable>>> edges = new HashSet<LabeledEdge<ILiteral, Set<IVariable>>>();
		edges.add(createEdge(lit, lit, X));

		assertEquals("The edge set does not match.", edges, sip.getEdges());
	}

	/**
	 * Tests whether the bound variables are correct.
	 */
	public void testBounds0() throws Exception {
		final String prog = "sg(?X, ?Y) :- up(?X, ?Z1), sg(?Z1, ?Z2), down(?X, ?Z1, ?Z2, ?Z3), again(?X, ?Z1, ?Z3, ?Y).\n"
						 + "?- sg('john', ?X).";
		final Parser p = new Parser();
		p.parse(prog);
		final IRule r = p.getRules().iterator().next();
		final IQuery q = p.getQueries().iterator().next();
		final SIPImpl sip = new SIPImpl(r, q);

		final IVariable X = TERM.createVariable("X");
		final IVariable Y = TERM.createVariable("Y");
		final IVariable Z1 = TERM.createVariable("Z1");
		final IVariable Z2 = TERM.createVariable("Z2");
		final IVariable Z3 = TERM.createVariable("Z3");

		final Set<IVariable> bound_up = new HashSet<IVariable>(Arrays.asList(new IVariable[]{X}));
		final Set<IVariable> bound_sg = new HashSet<IVariable>(Arrays.asList(new IVariable[]{Z1}));
		final Set<IVariable> bound_down = new HashSet<IVariable>(Arrays.asList(new IVariable[]{X, Z1, Z2}));
		final Set<IVariable> bound_again = new HashSet<IVariable>(Arrays.asList(new IVariable[]{X, Z1, Z3}));

		assertEquals("Bounds of up wrong", bound_up, sip.getBoundVariables(r.getBody().get(0)));
		assertEquals("Bounds of sg wrong", bound_sg, sip.getBoundVariables(r.getBody().get(1)));
		assertEquals("Bounds of down wrong", bound_down, sip.getBoundVariables(r.getBody().get(2)));
		assertEquals("Bounds of again wrong", bound_again, sip.getBoundVariables(r.getBody().get(3)));
	}

	/**
	 * Tests whether the bound variables are correct.
	 */
	public void testBounds1() throws Exception {
		final String prog = "rsg(?X, ?Y) :- up(?X, ?X1), rsg(?Y1, ?X1), down(?Y1, ?Y).\n"
						  + "?- rsg('a', ?X).";
		final Parser p = new Parser();
		p.parse(prog);
		final IRule r = p.getRules().iterator().next();
		final IQuery q = p.getQueries().iterator().next();
		final SIPImpl sip = new SIPImpl(r, q);

		final IVariable X = TERM.createVariable("X");
		final IVariable X1 = TERM.createVariable("X1");
		final IVariable Y = TERM.createVariable("Y");
		final IVariable Y1 = TERM.createVariable("Y1");

		final Set<IVariable> bound_up = new HashSet<IVariable>(Arrays.asList(new IVariable[]{X}));
		final Set<IVariable> bound_rsg0 = new HashSet<IVariable>(Arrays.asList(new IVariable[]{X1}));
		final Set<IVariable> bound_down = new HashSet<IVariable>(Arrays.asList(new IVariable[]{Y1}));

		assertEquals("Bounds of up wrong", bound_up, sip.getBoundVariables(r.getBody().get(0)));
		assertEquals("Bounds of rsg0 wrong", bound_rsg0, sip.getBoundVariables(r.getBody().get(1)));
		assertEquals("Bounds of down wrong", bound_down, sip.getBoundVariables(r.getBody().get(2)));
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
	 * @return the edge
	 * @throws NullPointerException
	 *             if the source, target or label is {@code null}
	 * @throws NullPointerException
	 *             if the label contains {@code null}
	 */
	private static LabeledEdge<ILiteral, Set<IVariable>> createEdge(
			final ILiteral s, final ILiteral t, final IVariable... v) {
		if ((s == null) || (t == null) || (v == null)) {
			throw new NullPointerException(
					"The source, target and variables must not be null");
		}
		return new LabeledEdge<ILiteral, Set<IVariable>>(s, t,
				new HashSet<IVariable>(Arrays.asList(v)));
	}
}
