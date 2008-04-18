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
package org.deri.iris.optimisations.magicsets;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.BUILTIN;
import static org.deri.iris.factory.Factory.TERM;
import static org.deri.iris.MiscHelper.createLiteral;
import static org.deri.iris.MiscHelper.createVarList;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import org.deri.iris.compiler.ParserException;
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
	 * Parses a rule and a query and constructs the sip out of them.
	 * @param prog the program to parse
	 * @return the constructed sip
	 */
	private static SIPImpl parseProgram(final String prog) throws ParserException {
		assert prog != null: "The program must not be null";

		final Parser p = new Parser();
		p.parse(prog);

		assert !p.getRules().isEmpty(): "There are no rules parsed!";
		assert !p.getQueries().isEmpty(): "There are no queries parsed!";

		return new SIPImpl(p.getRules().get(0), p.getQueries().get(0));
	}

	/**
	 * Parses a single rule out of a string.
	 * @param prog the program to parse containing the rule
	 * @return the parsed rule
	 */
	private static IRule parseSingleRule(final String prog) throws ParserException {
		assert prog != null: "The program must not be null";

		final Parser p = new Parser();
		p.parse(prog);

		assert !p.getRules().isEmpty(): "There are no rules parsed!";

		return p.getRules().get(0);
	}

	/**
	 * Tests whether the sip contains all expected edges.
	 */
	public void testForEdges0() throws Exception {
		final String prog = "sg(?X, ?Y) :- up(?X, ?Z1), sg(?Z1, ?Z2), flat(?Z2, ?Z3), sg(?Z3, ?Z4), down(?Z4, ?Y).\n"
						 + "?- sg('john', ?X).";
		final SIPImpl sip = parseProgram(prog);

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
		final SIPImpl sip = parseProgram(prog);

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
		final SIPImpl sip = parseProgram(prog);

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
	 * <b>Note: Rules containing one literal multiple times will only
	 * contain one edge representing it, no matter, how often such a
	 * literal occurs in the rule.</b>
	 * </p>
	 */
	public void testEqualLiterals() throws Exception {
		final String prog = "tmp(?X) :- p(?X), p(?X), p(?X).\n"
					      + "?- tmp(?X).";
		final SIPImpl sip = parseProgram(prog);

		final IVariable X = TERM.createVariable("X");
		final ILiteral lit = createLiteral("p", "X");

		final Set<LabeledEdge<ILiteral, Set<IVariable>>> edges = new HashSet<LabeledEdge<ILiteral, Set<IVariable>>>();
		edges.add(createEdge(lit, lit, X));

		assertEquals("The edge set does not match.", edges, sip.getEdges());
	}

	/**
	 * Checks whether the dependency retrieval of a literal depending on
	 * itself succeeds.
	 */
	public void testEqualLiteralsDependency() throws Exception {
		final String prog = "a(?X) :- a(?X).\n"
			+ "?- a(1).";
		final SIPImpl sip = parseProgram(prog);

		final ILiteral ax = createLiteral("a", "X");
		assertEquals(ax + " depends on itself.", Collections.singleton(ax), sip.getDepends(ax));
	}

	/**
	 * Tests whether the bound variables are correct.
	 */
	public void testBounds0() throws Exception {
		final String prog = "sg(?X, ?Y) :- up(?X, ?Z1), sg(?Z1, ?Z2), down(?X, ?Z1, ?Z2, ?Z3), again(?X, ?Z1, ?Z3, ?Y).\n"
						 + "?- sg('john', ?X).";
		final SIPImpl sip = parseProgram(prog);

		final IVariable X = TERM.createVariable("X");
		final IVariable Z1 = TERM.createVariable("Z1");
		final IVariable Z2 = TERM.createVariable("Z2");
		final IVariable Z3 = TERM.createVariable("Z3");

		final Set<IVariable> bound_up = new HashSet<IVariable>(Arrays.asList(new IVariable[]{X}));
		final Set<IVariable> bound_sg = new HashSet<IVariable>(Arrays.asList(new IVariable[]{Z1}));
		final Set<IVariable> bound_down = new HashSet<IVariable>(Arrays.asList(new IVariable[]{X, Z1, Z2}));
		final Set<IVariable> bound_again = new HashSet<IVariable>(Arrays.asList(new IVariable[]{X, Z1, Z3}));

		final ILiteral up = createLiteral("up", "X", "Z1");
		final ILiteral sg = createLiteral("sg", "Z1", "Z2");
		final ILiteral down = createLiteral("down", "X", "Z1", "Z2", "Z3");
		final ILiteral again = createLiteral("again", "X", "Z1", "Z3", "Y");

		assertEquals("Bounds of up wrong", bound_up, sip.getBoundVariables(up));
		assertEquals("Bounds of sg wrong", bound_sg, sip.getBoundVariables(sg));
		assertEquals("Bounds of down wrong", bound_down, sip.getBoundVariables(down));
		assertEquals("Bounds of again wrong", bound_again, sip.getBoundVariables(again));
	}

	/**
	 * Tests whether the bound variables are correct.
	 */
	public void testBounds1() throws Exception {
		final String prog = "rsg(?X, ?Y) :- up(?X, ?X1), rsg(?Y1, ?X1), down(?Y1, ?Y).\n"
						  + "?- rsg('a', ?X).";
		final SIPImpl sip = parseProgram(prog);

		final IVariable X = TERM.createVariable("X");
		final IVariable X1 = TERM.createVariable("X1");
		final IVariable Y1 = TERM.createVariable("Y1");

		final Set<IVariable> bound_up = new HashSet<IVariable>(Arrays.asList(new IVariable[]{X}));
		final Set<IVariable> bound_rsg0 = new HashSet<IVariable>(Arrays.asList(new IVariable[]{X1}));
		final Set<IVariable> bound_down = new HashSet<IVariable>(Arrays.asList(new IVariable[]{Y1}));

		final ILiteral up = createLiteral("up", "X", "X1");
		final ILiteral rsg0 = createLiteral("rsg", "Y1", "X1");
		final ILiteral down = createLiteral("down", "Y1", "Y");

		assertEquals("Bounds of up wrong", bound_up, sip.getBoundVariables(up));
		assertEquals("Bounds of rsg0 wrong", bound_rsg0, sip.getBoundVariables(rsg0));
		assertEquals("Bounds of down wrong", bound_down, sip.getBoundVariables(down));
	}

	/**
	 * Tests whether the {@link SIPImpl#orderLiterals(IRule) orderLiterals}
	 * method could run into a infinite loop.
	 */
	public void testOrderLiterals() throws Exception {
		final Set<IVariable> X = Collections.singleton(TERM.createVariable("X"));

		final String prog0 = "x(?X) :- a(?X), !b(?B).";
		final String exp0 = "x(?X) :- a(?X), !b(?B).";
		assertEquals("Rule not ordered correctly", parseSingleRule(exp0),
				SIPImpl.orderLiterals(parseSingleRule(prog0), X));

		final String prog1 = "x(?X) :- !b(?B), a(?X), !c(?C).";
		final String exp1 = "x(?X) :- a(?X), !c(?C), !b(?B).";
		assertEquals("Rule not ordered correctly", parseSingleRule(exp1),
				SIPImpl.orderLiterals(parseSingleRule(prog1), X));

		final String prog2 = "x(?X) :- !b(?B), a(?X, ?B), !c(?C).";
		final String exp2 = "x(?X) :- a(?X, ?B), !b(?B), !c(?C).";
		assertEquals("Rule not ordered correctly", parseSingleRule(exp2),
				SIPImpl.orderLiterals(parseSingleRule(prog2), X));

		final String prog3 = "x(?X) :- !b(?Y), !b(?Y), a(?X).";
		final String exp3 = "x(?X) :- a(?X), !b(?Y), !b(?Y).";
		assertEquals("Rule not ordered correctly", parseSingleRule(exp3),
				SIPImpl.orderLiterals(parseSingleRule(prog3), X));
	}

	/**
	 * Tests the behaviour of the literal comparator with recursive literal
	 * dependencies in rules.
	 */
	public void testRecursiveLiteralComparator() throws Exception {
		final String prog = "a(?X) :- b(?X), c(?X), b(?X).\n"
			+ "?- a('john').";
		final SIPImpl sip = parseProgram(prog);

		// the literal edges are:
		// a -> b -> c
		// b -> b
		// c -> b

		final Comparator<ILiteral> lc = sip.getLiteralComparator();
		final ILiteral a = createLiteral("a", "X");
		final ILiteral b = createLiteral("b", "X");
		final ILiteral c = createLiteral("c", "X");

		assertEquals(a + " must be equals to " + a, 0, lc.compare(a, a));
		assertEquals(b + " must be equals to " + b, 0, lc.compare(b, b));
		assertEquals(c + " must be equals to " + c, 0, lc.compare(c, c));
		assertEquals(b + " must be equals to " + c, 0, lc.compare(b, c));
		assertEquals(c + " must be equals to " + b, 0, lc.compare(c, b));

		assertTrue(a + " must be smaller than " + b, lc.compare(a, b) < 0);
		assertTrue(a + " must be smaller than " + c, lc.compare(a, c) < 0);

		assertTrue(b + " must be bigger than " + a, lc.compare(b, a) > 0);
		assertTrue(c + " must be bigger than " + a, lc.compare(c, a) > 0);
	}

	/**
	 * Creates a edge.
	 * 
	 * @param s the source literal
	 * @param t the target literal
	 * @param v the passed variables (label)
	 * @return the edge
	 */
	private static LabeledEdge<ILiteral, Set<IVariable>> createEdge(
			final ILiteral s, final ILiteral t, final IVariable... v) {
		assert s != null: "The source literal must not be null";
		assert t != null: "The target literal must not be null";
		assert v != null: "The variables must not be null";

		return new LabeledEdge<ILiteral, Set<IVariable>>(s, t,
				new HashSet<IVariable>(Arrays.asList(v)));
	}
}
