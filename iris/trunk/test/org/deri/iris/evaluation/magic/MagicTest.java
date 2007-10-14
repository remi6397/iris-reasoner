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
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.compiler.Parser;
import org.deri.iris.evaluation.common.AdornedProgram;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;
import org.deri.iris.evaluation.common.Adornment;
import org.deri.iris.evaluation.common.AdornmentsTest;
import org.deri.iris.factory.Factory;
import org.deri.iris.MiscHelper;

/**
 * <p>
 * Tests the magic sets.
 * </p>
 * <p>
 * $Id: MagicTest.java,v 1.6 2007-10-14 14:49:04 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.6 $
 */
public class MagicTest extends TestCase {

	/** The 1st magic sets. */
	private MagicSetImpl ms0;

	/** Rules used to create the 1st magic sets. */
	private Set<IRule> rules0;

	/** The 2nd magic sets. */
	private MagicSetImpl ms1;

	/** Rules used to create the 2nd magic sets. */
	private Set<IRule> rules1;

	public void setUp() {
		// constructing the rules for ms0
		rules0 = new HashSet<IRule>();
		// sg(X, Y) :- flat(X, Y)
		IHead head = BASIC.createHead(createLiteral("sg", "X", "Y"));
		IBody body = BASIC.createBody(createLiteral("flat", "X", "Y"));
		rules0.add(BASIC.createRule(head, body));

		// sg(X, Y) :- up(X, Z1), sg(Z1, Z2), flat(Z2, Z3), sg(Z3, Z4), down(Z4,
		// Y)
		head = BASIC.createHead(createLiteral("sg", "X", "Y"));
		List<ILiteral> bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("up", "X", "Z1"));
		bodyLiterals.add(createLiteral("sg", "Z1", "Z2"));
		bodyLiterals.add(createLiteral("flat", "Z2", "Z3"));
		bodyLiterals.add(createLiteral("sg", "Z3", "Z4"));
		bodyLiterals.add(createLiteral("down", "Z4", "Y"));
		body = BASIC.createBody(bodyLiterals);
		rules0.add(BASIC.createRule(head, body));

		// constructing the query for ms0
		// sg(john, Y)
		IQuery query = BASIC.createQuery(BASIC.createLiteral(true, BASIC
				.createPredicate("sg", 2), BASIC.createTuple(TERM
				.createString("john"), TERM.createVariable("Y"))));
		ms0 = new MagicSetImpl(new AdornedProgram(rules0, query));

		// constructing the rules for ms1
		rules1 = new HashSet<IRule>();
		// a(X, Y, Z) :- c(X, Y, Z)
		head = BASIC.createHead(createLiteral("a", "X", "Y", "Z"));
		body = BASIC.createBody(createLiteral("c", "X", "Y", "Z"));
		rules1.add(BASIC.createRule(head, body));
		// a(X, Y, Z) :- b(X, A), a(X, A, B), c(B, Y, Z)
		head = BASIC.createHead(createLiteral("a", "X", "Y", "Z"));
		body = BASIC.createBody(createLiteral("b", "X", "A"), createLiteral(
				"a", "X", "A", "B"), createLiteral("c", "B", "Y", "Z"));
		rules1.add(BASIC.createRule(head, body));
		// constructing the query for ms1
		// a(john, mary, Y)
		query = BASIC.createQuery(BASIC.createLiteral(true, BASIC
				.createPredicate("a", 3), BASIC.createTuple(TERM
				.createString("john"), TERM.createString("mary"), TERM
				.createVariable("Y"))));
		ms1 = new MagicSetImpl(new AdornedProgram(rules1, query));
	}

	/**
	 * Creates a magic literal.
	 * 
	 * @param symbol
	 *            the predicate symbot to use for the literal
	 * @param ad
	 *            the adornments
	 * @param t
	 *            the terms for the literal
	 * @return the constructed magic literal
	 * @throws NullPointerException
	 *             if the symbol is {@code null}
	 * @throws IllegalArgumentException
	 *             if the symbol is 0 sighns long
	 * @throws NullPointerException
	 *             if the adornments is or contains {@code null}
	 * @throws NullPointerException
	 *             if the terms is or contains {@code null}
	 */
	private static ILiteral createMagicLiteral(final String symbol,
			final Adornment[] ad, final ITerm[] t) {
		if (symbol == null) {
			throw new NullPointerException("The symbol must not be null");
		}
		if (symbol.length() == 0) {
			throw new IllegalArgumentException(
					"The symbol must be longer than 0 characters");
		}
		if ((ad == null) || Arrays.asList(ad).contains(null)) {
			throw new NullPointerException(
					"The adornments must not be, or contain null");
		}
		if ((t == null) || Arrays.asList(t).contains(null)) {
			throw new NullPointerException(
					"The terms must not be, or contain null");
		}
		final IPredicate p = new AdornedProgram.AdornedPredicate("magic_"
				+ symbol, t.length, ad);
		return BASIC.createLiteral(true, p, BASIC.createTuple(t));
	}

	/**
	 * Creates an adorned literal.
	 * 
	 * @param symbol
	 *            the predicate symbot to use for the literal
	 * @param ad
	 *            the adornments
	 * @param t
	 *            the terms for the literal
	 * @return the constructed magic literal
	 * @throws NullPointerException
	 *             if the symbol is {@code null}
	 * @throws IllegalArgumentException
	 *             if the symbol is 0 sighns long
	 * @throws NullPointerException
	 *             if the adornments is or contains {@code null}
	 * @throws NullPointerException
	 *             if the terms is or contains {@code null}
	 */
	private static ILiteral createAdornedLiteral(final String symbol,
			final Adornment[] ad, final ITerm[] t) {
		if (symbol == null) {
			throw new NullPointerException("The symbol must not be null");
		}
		if (symbol.length() == 0) {
			throw new IllegalArgumentException(
					"The symbol must be longer than 0 characters");
		}
		if ((ad == null) || Arrays.asList(ad).contains(null)) {
			throw new NullPointerException(
					"The adornments must not be, or contain null");
		}
		if ((t == null) || Arrays.asList(t).contains(null)) {
			throw new NullPointerException(
					"The terms must not be, or contain null");
		}
		return BASIC.createLiteral(true, new AdornedProgram.AdornedPredicate(
				symbol, t.length, ad), BASIC.createTuple(t));
	}

	/**
	 * Adorns a literal.
	 * 
	 * @param l
	 *            the literal to adorn
	 * @param a
	 *            the adornments with which to adorn the literal
	 * @return the adorned literal
	 * @throws NullPointerException
	 *             if the literal is {@code null}
	 * @throws NullPointerException
	 *             it the adornemnts is or contains {@code null}
	 */
	private static ILiteral adornLiteral(final ILiteral l, Adornment... a) {
		if (l == null) {
			throw new NullPointerException("The literal must not be null");
		}
		if ((a == null) || Arrays.asList(a).contains(null)) {
			throw new NullPointerException(
					"The the adornents must not be or contain null");
		}
		final IPredicate lp = l.getPredicate();
		final AdornedPredicate p = new AdornedPredicate(
				lp.getPredicateSymbol(), lp.getArity(), a);
		return BASIC.createLiteral(l.isPositive(), p, l.getTuple());
	}

	/**
	 * Tests whether the seed was constructed as it should be.
	 */
	public void testSeed0() {
		IPredicate p = new AdornedProgram.AdornedPredicate("magic_sg", 1,
				new Adornment[] { Adornment.BOUND, Adornment.FREE });
		final IAtom ref = BASIC.createAtom(p, BASIC.createTuple(TERM.createString("john")));
		assertEquals("The seed wasn't constructed correctly", ref, ms0
				.getSeed());
	}

	/**
	 * Tests whether the seed was constructed as it should be.
	 */
	public void testSeed1() {
		IPredicate p = new AdornedProgram.AdornedPredicate("magic_a", 2,
				new Adornment[] { Adornment.BOUND, Adornment.BOUND,
						Adornment.FREE });
		final IAtom ref = BASIC.createAtom(p, BASIC
				.createTuple(TERM.createString("john"), TERM
						.createString("mary")));
		assertEquals("The seed wasn't constructed correctly", ref, ms1
				.getSeed());
	}

	/**
	 * Tests for the magic rules.
	 */
	public void testMagicRules0() {
		final List<IRule> rules = new ArrayList<IRule>(ms0.getMagicRules());
		final List<IRule> ref = new ArrayList<IRule>();

		// constructing the magic rules
		// magic_sg^bf(Z1) :- magic_sg^bf(X), up(X, Z1)
		IHead head = BASIC.createHead(createMagicLiteral("sg", new Adornment[] {
				Adornment.BOUND, Adornment.FREE }, new ITerm[] { TERM
				.createVariable("Z1") }));
		IBody body = BASIC.createBody(createMagicLiteral("sg", new Adornment[] {
				Adornment.BOUND, Adornment.FREE }, new ITerm[] { TERM
				.createVariable("X") }), createLiteral("up", "X", "Z1"));
		ref.add(BASIC.createRule(head, body));
		// magic_sg^bf(Z3) :- magic_sg^bf(X), up(X, Z1), sg^bf(Z1, Z2), flat(Z2,
		// Z3)
		head = BASIC.createHead(createMagicLiteral("sg", new Adornment[] {
				Adornment.BOUND, Adornment.FREE }, new ITerm[] { TERM
				.createVariable("Z3") }));
		body = BASIC
				.createBody(createMagicLiteral("sg", new Adornment[] {
						Adornment.BOUND, Adornment.FREE }, new ITerm[] { TERM
						.createVariable("X") }),
						createLiteral("up", "X", "Z1"), createAdornedLiteral(
								"sg", new Adornment[] { Adornment.BOUND,
										Adornment.FREE }, new ITerm[] {
										TERM.createVariable("Z1"),
										TERM.createVariable("Z2") }),
						createLiteral("flat", "Z2", "Z3"));
		ref.add(BASIC.createRule(head, body));

		Collections.sort(rules, AdornmentsTest.RC);
		assertEquals(ref, rules);
	}

	/**
	 * Tests for the magic rules.
	 */
	public void testMagicRules1() {
		final List<IRule> rules = new ArrayList<IRule>(ms1.getMagicRules());
		final List<IRule> ref = new ArrayList<IRule>();

		// constructing the magic rules
		// magic_a^bbf(X, A) :- magic_a^bbf(X, Y), b(X, A)
		IHead head = BASIC.createHead(createMagicLiteral("a", new Adornment[] {
				Adornment.BOUND, Adornment.BOUND, Adornment.FREE },
				new ITerm[] { TERM.createVariable("X"),
						TERM.createVariable("A") }));
		IBody body = BASIC.createBody(createMagicLiteral("a", new Adornment[] {
				Adornment.BOUND, Adornment.BOUND, Adornment.FREE },
				new ITerm[] { TERM.createVariable("X"),
						TERM.createVariable("Y") }), createLiteral("b", "X",
				"A"));
		ref.add(BASIC.createRule(head, body));

		Collections.sort(rules, AdornmentsTest.RC);
		assertEquals(ref, rules);
	}

	/**
	 * Tests the rewritten rules.
	 */
	public void testRewrittenRules0() {
		final List<IRule> ref = new ArrayList<IRule>();
		final List<IRule> mrules = new ArrayList<IRule>(ms0.getRewrittenRules());
		// constructing the reference rules out of the normal ones
		for (final IRule r : rules0) {
			final ILiteral head = createAdornedLiteral("sg", new Adornment[] {
					Adornment.BOUND, Adornment.FREE }, new ITerm[] {
					TERM.createVariable("X"), TERM.createVariable("Y") });
			final List<ILiteral> body = new ArrayList<ILiteral>(r
					.getBody().getLiterals());
			int i = 0;
			// adorning the sg's with bf
			for (final ILiteral l : body) {
				if (l.getPredicate().getPredicateSymbol().equals("sg")) {
					body.set(i, adornLiteral(l, new Adornment[] {
							Adornment.BOUND, Adornment.FREE }));
				}
				i++;
			}
			// adding the magic guard
			body.add(0, createMagicLiteral("sg", new Adornment[] {
					Adornment.BOUND, Adornment.FREE }, new ITerm[] { TERM
					.createVariable("X") }));
			// adding the rule to the list
			ref.add(BASIC.createRule(BASIC.createHead(head), BASIC
					.createBody(body)));
		}
		// sorting all rules
		Collections.sort(ref, AdornmentsTest.RC);
		Collections.sort(mrules, AdornmentsTest.RC);
		// comparing the rules
		for (Iterator<IRule> i0 = ref.iterator(), i1 = mrules.iterator(); i0
				.hasNext();) {
			final IRule r0 = i0.next();
			final IRule r1 = i1.next();
			assertEquals(
					"The rules\n" + r0 + "\nand\n" + r1 + "\ndon't match.", 0,
					AdornmentsTest.RC.compare(r0, r1));
		}
	}

	/**
	 * Tests the rewritten rules.
	 */
	public void testRewrittenRules1() {
		final List<IRule> ref = new ArrayList<IRule>();
		final List<IRule> mrules = new ArrayList<IRule>(ms1.getRewrittenRules());
		// constructing the reference rules out of the normal ones
		for (final IRule r : rules1) {
			final ILiteral head = createAdornedLiteral(
					"a",
					new Adornment[] { Adornment.BOUND, Adornment.BOUND,
							Adornment.FREE },
					new ITerm[] { TERM.createVariable("X"),
							TERM.createVariable("Y"), TERM.createVariable("Z") });
			final List<ILiteral> body = new ArrayList<ILiteral>(r
					.getBody().getLiterals());
			int i = 0;
			// adorning the sg's with bf
			for (final ILiteral l : body) {
				if (l.getPredicate().getPredicateSymbol().equals("a")) {
					body
							.set(i, adornLiteral(l, new Adornment[] {
									Adornment.BOUND, Adornment.BOUND,
									Adornment.FREE }));
				}
				i++;
			}
			// adding the magic guard
			body.add(0, createMagicLiteral("a", new Adornment[] {
					Adornment.BOUND, Adornment.BOUND, Adornment.FREE },
					new ITerm[] { TERM.createVariable("X"),
							TERM.createVariable("Y") }));
			// adding the rule to the list
			ref.add(BASIC.createRule(BASIC.createHead(head), BASIC
					.createBody(body)));
		}
		// sorting all rules
		Collections.sort(ref, AdornmentsTest.RC);
		Collections.sort(mrules, AdornmentsTest.RC);
		// comparing the rules
		for (Iterator<IRule> i0 = ref.iterator(), i1 = mrules.iterator(); i0
				.hasNext();) {
			final IRule r0 = i0.next();
			final IRule r1 = i1.next();
			assertEquals(
					"The rules\n" + r0 + "\nand\n" + r1 + "\ndon't match.", 0,
					AdornmentsTest.RC.compare(r0, r1));
		}
	}

	/**
	 * Tests that constatns in bodyliterals are determined as bound.
	 */
	public void testBoundConstant() throws Exception {
		final String program = "a(?X, ?Y) :- b(?X, ?Z), c('a', ?Z, ?Y). \n" + 
			"c(?X, ?Y, ?Z) :- x(?X, ?Y, ?Z). \n" + 
			"?-a('john', ?Y).";
		final IProgram p = Factory.PROGRAM.createProgram();
		Parser.parse(program, p);

		final MagicSetImpl ms = new MagicSetImpl(new AdornedProgram(
					p.getRules(), p.getQueries().iterator().next()));

		final ITerm a = TERM.createString("a");
		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final ITerm Z = TERM.createVariable("Z");
		final ITerm[] XYZ = new ITerm[]{X, Y, Z};
		final Adornment[] bbf = new Adornment[]{Adornment.BOUND, Adornment.BOUND, Adornment.FREE};
		final Adornment[] bf = new Adornment[]{Adornment.BOUND, Adornment.FREE};
		final ILiteral b = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("b", 2), BASIC.createTuple(X, Z)));
		final ILiteral x = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("x", 3), BASIC.createTuple(X, Y, Z)));

		final Set<IRule> ref = new HashSet<IRule>();
		// magic_c^bbf(a, Z) :- magic_a^bf(X), b(X, Z)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("c", bbf, new ITerm[]{a, Z})), 
					BASIC.createBody(createMagicLiteral("a", bf, new ITerm[]{X}), b)));
		// a^bf(X, Y) :- magic_a^bf(X), b(X, Z), c^bbf(a, Z, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("a", bf, new ITerm[]{X, Y})), 
					BASIC.createBody(
						createMagicLiteral("a", bf, new ITerm[]{X}), 
						b, 
						createAdornedLiteral("c", bbf, new ITerm[]{a, Z, Y}))));
		// c^bbf(X, Y, Z) :- magic_c^bbf(X, Y), x(X, Y, Z)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("c", bbf, XYZ)), 
					BASIC.createBody(createMagicLiteral("c", bbf, new ITerm[]{X, Y}), x)));

		final Set<IRule> rules = new HashSet<IRule>(ms.getRewrittenRules());
		rules.addAll(ms.getMagicRules());

		assertTrue("The rules must be '" + MiscHelper.join("\n", ref) + "', but were '" + 
				MiscHelper.join("\n", rules) + "'", 
				MiscHelper.compare(rules, ref, AdornmentsTest.RC));
	}

	/**
	 * Tests whether useless magic predicates (magic_q^f()) will be created,
	 * or not.
	 */
	public void testStupidRules() throws Exception {
		final String program = "q(?X) :- s(?X), not p(?X).\n" + 
			"p(?X) :- r(?X).\n" + 
			"r(?X) :- t(?X).\n" + 
			"?- q(?X).";
		final IProgram p = Factory.PROGRAM.createProgram();
		Parser.parse(program, p);

		final AdornedProgram ap = new AdornedProgram(
				p.getRules(), p.getQueries().iterator().next());
		final MagicSetImpl ms = new MagicSetImpl(new AdornedProgram(
					p.getRules(), p.getQueries().iterator().next()));

		final ITerm[] X = new ITerm[]{TERM.createVariable("X")};
		final Adornment[] b = new Adornment[]{Adornment.BOUND};
		final Adornment[] f = new Adornment[]{Adornment.FREE};
		final ILiteral s = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("s", 1), BASIC.createTuple(X)));
		final ILiteral t = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("t", 1), BASIC.createTuple(X)));
		final ILiteral q = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("q", 1), BASIC.createTuple(X)));
		final ILiteral neg_ad_p = createAdornedLiteral("p", b, X);
		neg_ad_p.setPositive(false);
		final ILiteral ad_q = createAdornedLiteral("q", f, X);
		final ILiteral ad_p = createAdornedLiteral("p", b, X);
		final ILiteral ad_r = createAdornedLiteral("r", b, X);
		final ILiteral magic_p = createMagicLiteral("p", b, X);
		final ILiteral magic_r = createMagicLiteral("r", b, X);

		final Set<IRule> ref = new HashSet<IRule>();
		// magic_r^b(X) :- magic_p^b(X)
		ref.add(BASIC.createRule(BASIC.createHead(magic_r), BASIC.createBody(magic_p)));
		// magic_p^b(X) :- magic_q^f(), s(X)
		ref.add(BASIC.createRule(BASIC.createHead(magic_p), 
					BASIC.createBody(createMagicLiteral("q", f, new ITerm[]{}), s)));
		// q^f(X) :- magic_q^f(), s(X), -p^b(X)
		ref.add(BASIC.createRule(BASIC.createHead(ad_q), 
					BASIC.createBody(createMagicLiteral("q", f, new ITerm[]{}), s, neg_ad_p)));
		// r^b(X) :- magic_r^b(X), t(X)
		ref.add(BASIC.createRule(BASIC.createHead(ad_r), BASIC.createBody(magic_r, t)));
		// p^b(X) :- magic_p^b(X), r^b(X)
		ref.add(BASIC.createRule(BASIC.createHead(ad_p), BASIC.createBody(magic_p, ad_r)));
		
		final Set<IRule> rules = new HashSet<IRule>(ms.getRewrittenRules());
		rules.addAll(ms.getMagicRules());

		assertTrue("The two rule collections must be equal", MiscHelper.compare(rules, ref, AdornmentsTest.RC));
	}

	/**
	 * Tests conjunctive queries.
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1798276&group_id=167309&atid=842434">bug #1798276: Magic Sets evaluation does not allow conjunctive queries</a>
	 */
	public void testConjunctiveQuery0() throws Exception {
		final String prog = "p(?X, ?Y) :- c(?X, ?Y).\n" + 
			"r(?X, ?Y, ?Z) :- c(?X, ?Y, ?Z).\n" + 
			"s(?X, ?Y) :- c(?X, ?Y).\n" + 
			"?- p(?X, 'a'), r('b', ?X, ?Y), s('e', ?Y).";
		final IProgram p = Parser.parse(prog);
		final MagicSetImpl ms = new MagicSetImpl(new AdornedProgram(p.getRules(), p.getQueries().iterator().next()));

		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final ITerm Z = TERM.createVariable("Z");
		final ITerm[] XY = new ITerm[]{X, Y};
		final ITerm[] XYZ = new ITerm[]{X, Y, Z};
		final Adornment[] fb = new Adornment[]{Adornment.FREE, Adornment.BOUND};
		final Adornment[] bb = new Adornment[]{Adornment.BOUND, Adornment.BOUND};
		final Adornment[] bbf = new Adornment[]{Adornment.BOUND, Adornment.BOUND, Adornment.FREE};
		final ILiteral c2 = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 2), BASIC.createTuple(XY)));
		final ILiteral c3 = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 3), BASIC.createTuple(XYZ)));

		final Set<IRule> ref = new HashSet<IRule>();
		// magic_r^bbf(b, X) :- p^fb(X, a)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("r", bbf, 
							new ITerm[]{TERM.createString("b"), X})), 
					BASIC.createBody(createAdornedLiteral("p", fb, 
							new ITerm[]{X, TERM.createString("a")}))));
		// magic_s^bb(e, Y) :- p^fb(X, a), r^bbf(b, X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("s", bb, 
							new ITerm[]{TERM.createString("e"), Y})),
					BASIC.createBody(createAdornedLiteral("p", fb, 
							new ITerm[]{X, TERM.createString("a")}), 
						createAdornedLiteral("r", bbf, 
							new ITerm[]{TERM.createString("b"), X, Y}))));
		// p^fb(X, Y) :- magic_p^fb(Y), c(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("p", fb, XY)), 
					BASIC.createBody(createMagicLiteral("p", fb, new ITerm[]{Y}), c2)));
		// r^bbf(X, Y, Z) :- magic_r^bbf(X, Y), c(X, Y, Z)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("r", bbf, XYZ)), 
					BASIC.createBody(createMagicLiteral("r", bbf, XY), c3)));
		// s^bb(X, Y) :- magic_s^bb(X, Y), c(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("s", bb, XY)), 
					BASIC.createBody(createMagicLiteral("s", bb, XY), c2)));

		final Set<IRule> rules = new HashSet<IRule>(ms.getRewrittenRules());
		rules.addAll(ms.getMagicRules());

		assertTrue("The two rule collections must be equal", MiscHelper.compare(rules, ref, AdornmentsTest.RC));
		assertEquals("The seed is not correct", 
				createMagicLiteral("p", fb, new ITerm[]{TERM.createString("a")}).getAtom(), 
				ms.getSeed());
	}

	/**
	 * Tests conjunctive queries.
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1798276&group_id=167309&atid=842434">bug #1798276: Magic Sets evaluation does not allow conjunctive queries</a>
	 */
	public void testConjunctiveQuery1() throws Exception {
		final String prog = "p(?X, ?Y) :- c(?X, ?Y).\n" + 
			"r(?X, ?Y, ?Z) :- c(?X, ?Y, ?Z).\n" + 
			"s(?X, ?Y) :- c(?X, ?Y).\n" + 
			"?- p(?X, ?Y), r('b', ?X, ?Z), s('e', ?Z).";
		final IProgram p = Parser.parse(prog);
		final MagicSetImpl ms = new MagicSetImpl(new AdornedProgram(p.getRules(), p.getQueries().iterator().next()));

		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final ITerm Z = TERM.createVariable("Z");
		final ITerm[] XY = new ITerm[]{X, Y};
		final ITerm[] XYZ = new ITerm[]{X, Y, Z};
		final Adornment[] ff = new Adornment[]{Adornment.FREE, Adornment.FREE};
		final Adornment[] bb = new Adornment[]{Adornment.BOUND, Adornment.BOUND};
		final Adornment[] bbf = new Adornment[]{Adornment.BOUND, Adornment.BOUND, Adornment.FREE};
		final ILiteral c2 = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 2), BASIC.createTuple(XY)));
		final ILiteral c3 = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 3), BASIC.createTuple(XYZ)));

		final Set<IRule> ref = new HashSet<IRule>();
		// magic_r^bbf(b, X) :- p^ff(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("r", bbf, 
							new ITerm[]{TERM.createString("b"), X})), 
					BASIC.createBody(createAdornedLiteral("p", ff, XY))));
		// magic_s^bb(e, Z) :- p^ff(X, Y), r^bbf(b, X, Z)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("s", bb, 
							new ITerm[]{TERM.createString("e"), Z})), 
					BASIC.createBody(createAdornedLiteral("p", ff, XY), 
						createAdornedLiteral("r", bbf, new ITerm[]{TERM.createString("b"), X, Z}))));
		// r^bbf(X, Y, Z) :- magic_r^bbf(X, Y), c(X, Y, Z)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("r", bbf, XYZ)), 
					BASIC.createBody(createMagicLiteral("r", bbf, XY), c3)));
		// s^bb(X, Y) :- magic_s^bb(X, Y), c(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("s", bb, XY)), 
					BASIC.createBody(createMagicLiteral("s", bb, XY), c2)));
		// p^ff(X, Y) :- magic_p^ff(), c(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("p", ff, XY)), 
					BASIC.createBody(createMagicLiteral("p", ff, new ITerm[]{}), c2)));

		final Set<IRule> rules = new HashSet<IRule>(ms.getRewrittenRules());
		rules.addAll(ms.getMagicRules());

		assertTrue("The two rule collections must be equal", MiscHelper.compare(rules, ref, AdornmentsTest.RC));
		assertEquals("The seed is not correct", 
				createMagicLiteral("p", ff, new ITerm[]{}).getAtom(), 
				ms.getSeed());
	}

	/**
	 * Tests conjunctive queries.
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1798276&group_id=167309&atid=842434">bug #1798276: Magic Sets evaluation does not allow conjunctive queries</a>
	 */
	public void testConjunctiveQuery2() throws Exception {
		final String prog = "p(?X, ?Y) :- c(?X, ?Y).\n" + 
			"r(?X, ?Y, ?Z) :- c(?X, ?Y, ?Z).\n" + 
			"s(?X, ?Y) :- c(?X, ?Y).\n" + 
			"?- p('b', 'a'), r('b', ?X, ?Y), s('e', ?Y).";
		final IProgram p = Parser.parse(prog);
		final MagicSetImpl ms = new MagicSetImpl(new AdornedProgram(p.getRules(), p.getQueries().iterator().next()));

		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final ITerm Z = TERM.createVariable("Z");
		final ITerm[] XY = new ITerm[]{X, Y};
		final ITerm[] XYZ = new ITerm[]{X, Y, Z};
		final Adornment[] bb = new Adornment[]{Adornment.BOUND, Adornment.BOUND};
		final Adornment[] bff = new Adornment[]{Adornment.BOUND, Adornment.FREE, Adornment.FREE};
		final ILiteral c2 = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 2), BASIC.createTuple(XY)));
		final ILiteral c3 = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 3), BASIC.createTuple(XYZ)));

		final Set<IRule> ref = new HashSet<IRule>();
		// magic_r^bff(b) :- p^bb(b, a)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("r", bff, 
							new ITerm[]{TERM.createString("b")})), 
					BASIC.createBody(createAdornedLiteral("p", bb, 
							new ITerm[]{TERM.createString("b"), TERM.createString("a")}))));
		// magic_s^bb(e, Y) :- p^bb(b, a), r^bff(b, X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("s", bb, 
							new ITerm[]{TERM.createString("e"), Y})), 
					BASIC.createBody(createAdornedLiteral("p", bb, 
							new ITerm[]{TERM.createString("b"), TERM.createString("a")}), 
						createAdornedLiteral("r", bff, 
							new ITerm[]{TERM.createString("b"), X, Y}))));
		// p^bb(X, Y) :- magic_p^bb(X, Y), c(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("p", bb, XY)), 
					BASIC.createBody(createMagicLiteral("p", bb, XY), c2)));
		// r^bff(X, Y, Z) :- magic_r^bff(X), c(X, Y, Z)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("r", bff, XYZ)), 
					BASIC.createBody(createMagicLiteral("r", bff, new ITerm[]{X}), c3)));
		// s^bb(X, Y) :- magic_s^bb(X, Y), c(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("s", bb, XY)), 
					BASIC.createBody(createMagicLiteral("s", bb, XY), c2)));

		final Set<IRule> rules = new HashSet<IRule>(ms.getRewrittenRules());
		rules.addAll(ms.getMagicRules());

		assertTrue("The two rule collections must be equal", MiscHelper.compare(rules, ref, AdornmentsTest.RC));
		assertEquals("The seed is not correct", 
				createMagicLiteral("p", bb, new ITerm[]{TERM.createString("b"), TERM.createString("a")}).getAtom(), 
				ms.getSeed());
	}

	/**
	 * Tests conjunctive queries.
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1798276&group_id=167309&atid=842434">bug #1798276: Magic Sets evaluation does not allow conjunctive queries</a>
	 */
	public void testConjunctiveQuery3() throws Exception {
		final String prog = "p(?X, ?Y) :- c(?X, ?Y).\n" + 
			"r(?X, ?Y) :- c(?X, ?Y).\n" + 
			"s(?W, ?X, ?Y, ?Z) :- c(?W, ?X, ?Y, ?Z).\n" + 
			"?- p(?W, ?X), r(?Y, ?Z), s(?W, ?X, ?Y, ?Z).";
		final IProgram p = Parser.parse(prog);
		final MagicSetImpl ms = new MagicSetImpl(new AdornedProgram(p.getRules(), p.getQueries().iterator().next()));

		final ITerm W = TERM.createVariable("W");
		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final ITerm Z = TERM.createVariable("Z");
		final ITerm[] WX = new ITerm[]{W, X};
		final ITerm[] XY = new ITerm[]{X, Y};
		final ITerm[] YZ = new ITerm[]{Y, Z};
		final ITerm[] WXYZ = new ITerm[]{W, X, Y, Z};
		final Adornment[] ff = new Adornment[]{Adornment.FREE, Adornment.FREE};
		final Adornment[] bbbb = new Adornment[]{Adornment.BOUND, Adornment.BOUND, Adornment.BOUND, Adornment.BOUND};
		final ILiteral c2 = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 2), BASIC.createTuple(XY)));
		final ILiteral c4 = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 4), BASIC.createTuple(WXYZ)));

		final Set<IRule> ref = new HashSet<IRule>();
		// magic_s^bbbb(W, X, Y, Z) :- p^ff(W, X), r^ff(Y, Z)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("s", bbbb, WXYZ)), 
					BASIC.createBody(createAdornedLiteral("p", ff, WX), 
						createAdornedLiteral("r", ff, YZ))));
		// magic_r^ff() :- p^ff(W, X)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("r", ff, new ITerm[]{})), 
					BASIC.createBody(createAdornedLiteral("p", ff, WX))));
		// s^bbbb(W, X, Y, Z) :- magic_s^bbbb(W, X, Y, Z), c(W, X, Y, Z)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("s", bbbb, WXYZ)), 
					BASIC.createBody(createMagicLiteral("s", bbbb, WXYZ), c4)));
		// r^ff(X, Y) :- magic_r^ff(), c(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("r", ff, XY)), 
					BASIC.createBody(createMagicLiteral("r", ff, new ITerm[]{}), c2)));
		// p^ff(X, Y) :- magic_p^ff(), c(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("p", ff, XY)), 
					BASIC.createBody(createMagicLiteral("p", ff, new ITerm[]{}), c2)));

		final Set<IRule> rules = new HashSet<IRule>(ms.getRewrittenRules());
		rules.addAll(ms.getMagicRules());

		assertTrue("The two rule collections must be equal", MiscHelper.compare(rules, ref, AdornmentsTest.RC));
		assertEquals("The seed is not correct", 
				createMagicLiteral("p", ff, new ITerm[]{}).getAtom(), 
				ms.getSeed());
	}

	/**
	 * Tests conjunctive queries.
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1798276&group_id=167309&atid=842434">bug #1798276: Magic Sets evaluation does not allow conjunctive queries</a>
	 */
	public void testComplicatedConjunctiveQuery0() throws Exception {
		final String prog = "p(?X, ?Y) :- c(?X, ?Y), r(?Z, ?Y, ?X).\n" + 
			"r(?X, ?Y, ?Z) :- c(?X, ?Y, ?Z).\n" + 
			"s(?X, ?Y) :- c(?X, ?Y).\n" + 
			"?- p(?X, ?Y), r('b', ?X, ?Z), s('e', ?Z).";
		final IProgram p = Parser.parse(prog);
		final MagicSetImpl ms = new MagicSetImpl(new AdornedProgram(p.getRules(), p.getQueries().iterator().next()));

		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final ITerm Z = TERM.createVariable("Z");
		final ITerm[] XY = new ITerm[]{X, Y};
		final ITerm[] YX = new ITerm[]{Y, X};
		final ITerm[] YZ = new ITerm[]{Y, Z};
		final ITerm[] XYZ = new ITerm[]{X, Y, Z};
		final ITerm[] ZYX = new ITerm[]{Z, Y, X};
		final Adornment[] ff = new Adornment[]{Adornment.FREE, Adornment.FREE};
		final Adornment[] bb = new Adornment[]{Adornment.BOUND, Adornment.BOUND};
		final Adornment[] fbb = new Adornment[]{Adornment.FREE, Adornment.BOUND, Adornment.BOUND};
		final Adornment[] bbf = new Adornment[]{Adornment.BOUND, Adornment.BOUND, Adornment.FREE};
		final ILiteral c2 = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 2), BASIC.createTuple(XY)));
		final ILiteral c3 = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 3), BASIC.createTuple(XYZ)));

		final Set<IRule> ref = new HashSet<IRule>();
		// magic_r^bbf(b, X) :- p^ff(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("r", bbf, new ITerm[]{TERM.createString("b"), X})),
					BASIC.createBody(createAdornedLiteral("p", ff, XY))));
		// magic_r^fbb(Y, X) :- magic_p^ff(), c(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("r", fbb, YX)), 
					BASIC.createBody(createMagicLiteral("p", ff, new ITerm[]{}), c2)));
		// magic_s^bb(e, Z) :- p^ff(X, Y), r^bbf(b, X, Z)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("s", bb, new ITerm[]{TERM.createString("e"), Z})), 
					BASIC.createBody(createAdornedLiteral("p", ff, XY), 
						createAdornedLiteral("r", bbf, new ITerm[]{TERM.createString("b"), X, Z}))));
		// r^fbb(X, Y, Z) :- magic_r^fbb(Y, Z), c(X, Y, Z)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("r", fbb, XYZ)), 
					BASIC.createBody(createMagicLiteral("r", fbb, YZ), c3)));
		// r^bbf(X, Y, Z) :- magic_r^bbf(X, Y), c(X, Y, Z)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("r", bbf, XYZ)), 
					BASIC.createBody(createMagicLiteral("r", bbf, XY), c3)));
		// p^ff(X, Y) :- magic_p^ff(), c(X, Y), r^fbb(Z, Y, X)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("p", ff, XY)), 
					BASIC.createBody(createMagicLiteral("p", ff, new ITerm[]{}), 
						c2, 
						createAdornedLiteral("r", fbb, ZYX))));
		// s^bb(X, Y) :- magic_s^bb(X, Y), c(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("s", bb, XY)), 
					BASIC.createBody(createMagicLiteral("s", bb, XY), c2)));

		final Set<IRule> rules = new HashSet<IRule>(ms.getRewrittenRules());
		rules.addAll(ms.getMagicRules());

		assertTrue("The two rule collections must be equal", MiscHelper.compare(rules, ref, AdornmentsTest.RC));
		assertEquals("The seed is not correct", 
				createMagicLiteral("p", ff, new ITerm[]{}).getAtom(), 
				ms.getSeed());
	}

	/**
	 * Tests conjunctive queries.
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1798276&group_id=167309&atid=842434">bug #1798276: Magic Sets evaluation does not allow conjunctive queries</a>
	 */
	public void testComplicatedConjunctiveQuery1() throws Exception {
		final String prog = "p(?X, ?Y) :- c(?X, ?Y), s(?Z, ?T).\n" + 
			"r(?X, ?Y, ?Z) :- c(?X, ?Y, ?Z).\n" + 
			"s(?X, ?Y) :- c(?X, ?Y).\n" + 
			"?- p(?X, ?Y), r('b', ?X, ?Z), s('e', ?Z).";
		final IProgram p = Parser.parse(prog);
		final MagicSetImpl ms = new MagicSetImpl(new AdornedProgram(p.getRules(), p.getQueries().iterator().next()));

		final ITerm T = TERM.createVariable("T");
		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final ITerm Z = TERM.createVariable("Z");
		final ITerm[] XY = new ITerm[]{X, Y};
		final ITerm[] XYZ = new ITerm[]{X, Y, Z};
		final Adornment[] ff = new Adornment[]{Adornment.FREE, Adornment.FREE};
		final Adornment[] bb = new Adornment[]{Adornment.BOUND, Adornment.BOUND};
		final Adornment[] bbf = new Adornment[]{Adornment.BOUND, Adornment.BOUND, Adornment.FREE};
		final ILiteral c2 = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 2), BASIC.createTuple(XY)));
		final ILiteral c3 = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 3), BASIC.createTuple(XYZ)));

		final Set<IRule> ref = new HashSet<IRule>();
		// magic_r^bbf(b, X) :- p^ff(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("r", bbf, new ITerm[]{TERM.createString("b"), X})),
					BASIC.createBody(createAdornedLiteral("p", ff, XY))));
		// magic_s^bb(e, Z) :- p^ff(X, Y), r^bbf(b, X, Z)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("s", bb, new ITerm[]{TERM.createString("e"), Z})),
					BASIC.createBody(createAdornedLiteral("p", ff, XY), 
						createAdornedLiteral("r", bbf, new ITerm[]{TERM.createString("b"), X, Z}))));
		// magic_s^ff() :- magic_p^ff(), c(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createMagicLiteral("s", ff, new ITerm[]{})),
					BASIC.createBody(createMagicLiteral("p", ff, new ITerm[]{}), c2)));
		// r^bbf(X, Y, Z) :- magic_r^bbf(X, Y), c(X, Y, Z)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("r", bbf, XYZ)),
					BASIC.createBody(createMagicLiteral("r", bbf, XY), c3)));
		// p^ff(X, Y) :- magic_p^ff(), c(X, Y), s^ff(Z, T)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("p", ff, XY)),
					BASIC.createBody(createMagicLiteral("p", ff, new ITerm[]{}), 
						c2, 
						createAdornedLiteral("s", ff, new ITerm[]{Z, T}))));
		// s^bb(X, Y) :- magic_s^bb(X, Y), c(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("s", bb, XY)),
					BASIC.createBody(createMagicLiteral("s", bb, XY), c2)));
		// s^ff(X, Y) :- magic_s^ff(), c(X, Y)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("s", ff, XY)),
					BASIC.createBody(createMagicLiteral("s", ff, new ITerm[]{}), c2)));

		final Set<IRule> rules = new HashSet<IRule>(ms.getRewrittenRules());
		rules.addAll(ms.getMagicRules());

		assertTrue("The two rule collections must be equal", MiscHelper.compare(rules, ref, AdornmentsTest.RC));
		assertEquals("The seed is not correct", 
				createMagicLiteral("p", ff, new ITerm[]{}).getAtom(), 
				ms.getSeed());
	}

	/**
	 * Prints a program and the resulting magic program in a formated
	 * way.
	 * @param name the name to identify the test
	 * @param prog the input program
	 * @param res the resulting program
	 */
	private static void printDebug(final String name, final String prog, final MagicSetImpl res) {
		System.out.println("---");
		System.out.println(name);
		System.out.println("\tinput: ");
		System.out.println(prog);
		System.out.println("\tresult: ");
		System.out.println(res);
	}

	public static Test suite() {
		return new TestSuite(MagicTest.class, MagicTest.class.getSimpleName());
	}
}
