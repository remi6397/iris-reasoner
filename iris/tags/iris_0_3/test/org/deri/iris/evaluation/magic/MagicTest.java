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
 * $Id: MagicTest.java,v 1.2 2007-04-16 14:59:44 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.2 $
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
		final IQuery ref = BASIC.createQuery(BASIC.createLiteral(true, p, BASIC
				.createTuple(TERM.createString("john"))));
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
		final IQuery ref = BASIC.createQuery(BASIC.createLiteral(true, p, BASIC
				.createTuple(TERM.createString("john"), TERM
						.createString("mary"))));
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
					.getBodyLiterals());
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
					.getBodyLiterals());
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
	public void testBoundConstant() {
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
	public void testStupidRules() {
		final String program = "q(?X) :- s(?X), not p(?X)." + 
			"p(?X) :- r(?X)." + 
			"r(?X) :- t(?X)." + 
			"?- q(?X).";
		final IProgram p = Factory.PROGRAM.createProgram();
		Parser.parse(program, p);

		final MagicSetImpl ms = new MagicSetImpl(new AdornedProgram(
					p.getRules(), p.getQueries().iterator().next()));

		final ITerm[] X = new ITerm[]{TERM.createVariable("X")};
		final Adornment[] sb = new Adornment[]{Adornment.BOUND};
		final ILiteral s = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("s", 1), BASIC.createTuple(X)));
		final ILiteral t = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("t", 1), BASIC.createTuple(X)));
		final ILiteral q = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("q", 1), BASIC.createTuple(X)));
		final ILiteral neg_ad_p = createAdornedLiteral("p", sb, X);
		neg_ad_p.setPositive(false);
		final ILiteral ad_p = createAdornedLiteral("p", sb, X);
		final ILiteral ad_r = createAdornedLiteral("r", sb, X);
		final ILiteral magic_p = createMagicLiteral("p", sb, X);
		final ILiteral magic_r = createMagicLiteral("r", sb, X);

		final Set<IRule> ref = new HashSet<IRule>();
		//magic_r^b(X) :- magic_p^b(X)
		ref.add(BASIC.createRule(BASIC.createHead(magic_r), BASIC.createBody(magic_p)));
		//magic_p^b(X) :- s(X)
		ref.add(BASIC.createRule(BASIC.createHead(magic_p), BASIC.createBody(s)));
		//r^b(X) :- magic_r^b(X), t(X)
		ref.add(BASIC.createRule(BASIC.createHead(ad_r), BASIC.createBody(magic_r, t)));
		//q(X) :- s(X), -p^b(X)
		ref.add(BASIC.createRule(BASIC.createHead(q), BASIC.createBody(s, neg_ad_p)));
		//p^b(X) :- magic_p^b(X), r^b(X)
		ref.add(BASIC.createRule(BASIC.createHead(ad_p), BASIC.createBody(magic_p, ad_r)));
		
		final Set<IRule> rules = new HashSet<IRule>(ms.getRewrittenRules());
		rules.addAll(ms.getMagicRules());

		assertTrue("The two rule collections must be equals", MiscHelper.compare(rules, ref, AdornmentsTest.RC));
	}

	public static Test suite() {
		return new TestSuite(MagicTest.class, MagicTest.class.getSimpleName());
	}
}
