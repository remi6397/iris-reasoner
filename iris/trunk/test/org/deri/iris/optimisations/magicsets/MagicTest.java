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

import static org.deri.iris.MiscHelper.createLiteral;
import static org.deri.iris.MiscHelper.createVarList;
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.deri.iris.api.IProgramOptimisation.Result;
import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.compiler.Parser;
import org.deri.iris.optimisations.magicsets.AdornedProgram.AdornedPredicate;
import org.deri.iris.compiler.ParserException;

/**
 * <p>
 * Tests the magic sets.
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 */
public class MagicTest extends TestCase {

	/** The prefix for magic literals. */
	private static final String MAGIC_PREFIX = "magic_";

	/** The prefix for labeled literals. */
	private static final String LABELED_PREFIX = "label_";

	/**
	 * Creates a magic literal.
	 * 
	 * @param symbol the predicate symbot to use for the literal
	 * @param ad the adornments
	 * @param t the terms for the literal
	 * @return the constructed magic literal
	 */
	private static ILiteral createMagicLiteral(final String symbol,
			final Adornment[] ad, final ITerm[] t) {
		return createAdornedLiteral(MAGIC_PREFIX + symbol, ad, t);
	}

	/**
	 * Creates a labeled literal.
	 *
	 * @param symbol the predicate symbot to use for the literal
	 * @param ad the adornments
	 * @param t the terms for the literal
	 * @return the constructed magic literal
	 */
	private static ILiteral createLabeledLiteral(final String symbol,
			final Adornment[] ad, final ITerm[] t) {
		return createAdornedLiteral(LABELED_PREFIX + symbol, ad, t);
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
	 */
	private static ILiteral createAdornedLiteral(final String symbol,
			final Adornment[] ad, final ITerm[] t) {
		return createAdornedLiteral(true, symbol, ad, t);
	}

	/**
	 * Creates an adorned literal.
	 * 
	 * @param positive <code>true</code> whether the resulting literal
	 * should be positive, or not.
	 * @param symbol the predicate symbot to use for the literal
	 * @param ad the adornments
	 * @param t the terms for the literal
	 * @return the constructed magic literal
	 */
	private static ILiteral createAdornedLiteral(final boolean positive, final String symbol,
			final Adornment[] ad, final ITerm[] t) {
		assert symbol != null: "The symbol must not be null";
		assert symbol.length() > 0: "The symbol must not be an empty string";
		assert ad != null: "The adornments must not be null";
		assert !Arrays.asList(ad).contains(null): "The adornments must not contain null";
		assert t != null: "The terms must not be null";
		assert !Arrays.asList(t).contains(null): "The terms must not contain null";

		return BASIC.createLiteral(positive, 
				new AdornedProgram.AdornedPredicate(symbol, t.length, ad), 
				BASIC.createTuple(t));
	}

	/**
	 * Adorns a literal.
	 * 
	 * @param l
	 *            the literal to adorn
	 * @param a
	 *            the adornments with which to adorn the literal
	 * @return the adorned literal
	 */
	private static ILiteral adornLiteral(final ILiteral l, Adornment... a) {
		assert l != null: "The literal must not be null";
		assert a != null: "The adornments must not be null";
		assert !Arrays.asList(a).contains(null): "The adornments must not contain null";

		final IPredicate lp = l.getAtom().getPredicate();
		final AdornedPredicate p = new AdornedPredicate(
				lp.getPredicateSymbol(), lp.getArity(), a);
		return BASIC.createLiteral(l.isPositive(), p, l.getAtom().getTuple());
	}

	/**
	 * Creates a seed rule for a given atom.
	 * @param a the atom for which to create the seed rule
	 * @return the created rule
	 */
	private static IRule seedRule(final IAtom a) {
		assert a != null: "The atom must not be null";

		return seedRule(BASIC.createLiteral(true, a));
	}

	/**
	 * Creates a seed rule for a given atom.
	 * @param a the atom for which to create the seed rule
	 * @return the created rule
	 */
	private static IRule seedRule(final ILiteral l) {
		assert l != null: "The literal must not be null";

		return BASIC.createRule(Arrays.asList(l), Collections.EMPTY_LIST);
	}

	/**
	 * Parses a program and returns the result of the magic sets
	 * transformation.
	 * @param s the program to parse
	 * @return the transformation result
	 */
	private static Result getResult(final String s) throws ParserException {
		assert s != null: "The string to parse must not be null";

		final Parser p = new Parser();
		p.parse(s);
		final IQuery q = p.getQueries().iterator().next();
		return (new MagicSetImpl()).optimise(p.getRules(), q);
	}

	/**
	 * Asserts the result of the transformation.
	 * @param expected the expected transformation result
	 * @param result the real transformation result
	 */
	private void assertResults(final Result expected, final Result result) {
		if (expected == null) { // test the failed transformation
			assertNull("The transformation should fail", result);
		} else {
			Collections.sort(expected.rules, AdornmentsTest.RC);
			Collections.sort(result.rules, AdornmentsTest.RC);

			assertEquals("The rules are computed as expected", expected.rules, result.rules);
			assertEquals("The query is not correct", expected.query, result.query);
		}
	}

	/**
	 * Tests whether the seed was constructed as it should be.
	 */
	public void testMagic0() throws Exception {
		final String prog = "sg(?X, ?Y) :- flat(?X, ?Y)."
					      + "sg(?X, ?Y) :- up(?X, ?Z1), sg(?Z1, ?Z2), flat(?Z2, ?Z3), sg(?Z3, ?Z4), down(?Z4, ?Y)."
					      + "?- sg('john', ?Y).";
		final Result result = getResult(prog);

		final List<IRule> ref = new ArrayList<IRule>();

		final IPredicate SG_BF = new AdornedProgram.AdornedPredicate("sg", 2, 
				new Adornment[] { Adornment.BOUND, Adornment.FREE });
		final IPredicate MAGIC_SG_BF = new AdornedProgram.AdornedPredicate(MAGIC_PREFIX + "sg", 1, 
				new Adornment[] { Adornment.BOUND, Adornment.FREE });

		// constructing the rule for the seed

		ref.add(seedRule(BASIC.createAtom(MAGIC_SG_BF, BASIC.createTuple(TERM.createString("john")))));

		// constructing the magic rules

		// magic_sg^bf(Z1) :- magic_sg^bf(X), up(X, Z1)
		List<ILiteral> head = Arrays.asList(BASIC.createLiteral(true, MAGIC_SG_BF, BASIC.createTuple(createVarList("Z1"))));
		List<ILiteral> body = Arrays.asList(BASIC.createLiteral(true, MAGIC_SG_BF, BASIC.createTuple(createVarList("X"))), 
				createLiteral("up", "X", "Z1"));
		ref.add(BASIC.createRule(head, body));
		// magic_sg^bf(Z3) :- magic_sg^bf(X), up(X, Z1), sg^bf(Z1, Z2), flat(Z2, Z3)
		head = Arrays.asList(BASIC.createLiteral(true, MAGIC_SG_BF, BASIC.createTuple(createVarList("Z3"))));
		body = Arrays.asList(BASIC.createLiteral(true, MAGIC_SG_BF, BASIC.createTuple(createVarList("X"))), 
				createLiteral("up", "X", "Z1"), 
				BASIC.createLiteral(true, SG_BF, BASIC.createTuple(createVarList("Z1", "Z2"))), 
				createLiteral("flat", "Z2", "Z3"));
		ref.add(BASIC.createRule(head, body));

		// constructing the rewritten rules out of the normal ones

		// sg^bf(X,Y) :- magic_sg^bf(X), flat(X, Y)
		head = Arrays.asList(BASIC.createLiteral(true, SG_BF, BASIC.createTuple(createVarList("X", "Y"))));
		body = Arrays.asList(BASIC.createLiteral(true, MAGIC_SG_BF, BASIC.createTuple(createVarList("X"))), 
				createLiteral("flat", "X", "Y"));
		ref.add(BASIC.createRule(head, body));
		// sg^bf(X,Y) :- magic_sg^bf(X), up(X, Z1), sg^bf(Z1, Z2), flat(Z2, Z3), sg^bf(Z3, Z4), down(Z4, Y)
		head = Arrays.asList(BASIC.createLiteral(true, SG_BF, BASIC.createTuple(createVarList("X", "Y"))));
		body = Arrays.asList(BASIC.createLiteral(true, MAGIC_SG_BF, BASIC.createTuple(createVarList("X"))), 
				createLiteral("up", "X", "Z1"),
				BASIC.createLiteral(true, SG_BF, BASIC.createTuple(createVarList("Z1", "Z2"))), 
				createLiteral("flat", "Z2", "Z3"),
				BASIC.createLiteral(true, SG_BF, BASIC.createTuple(createVarList("Z3", "Z4"))), 
				createLiteral("down", "Z4", "Y"));
		ref.add(BASIC.createRule(head, body));

		Collections.sort(ref, AdornmentsTest.RC);
		Collections.sort(result.rules, AdornmentsTest.RC);
		assertEquals("The rules don't match", ref, result.rules);
		// TODO: match the query
	}

	/**
	 * Tests whether the seed was constructed as it should be.
	 */
	public void testMagic1() throws Exception {
		final String prog = "a(?X, ?Y, ?Z) :- c(?X, ?Y, ?Z)." 
						   + "a(?X, ?Y, ?Z) :- b(?X, ?A), a(?X, ?A, ?B), c(?B, ?Y, ?Z)."
						   + "?- a('john', 'mary', ?Y).";
		final Result result = getResult(prog);

		final List<IRule> ref = new ArrayList<IRule>();

		final Adornment[] bbf = new Adornment[]{Adornment.BOUND, Adornment.BOUND, Adornment.FREE};
		final Adornment[] bff = new Adornment[]{Adornment.BOUND, Adornment.FREE, Adornment.FREE};
		final IVariable A = TERM.createVariable("A");
		final IVariable B = TERM.createVariable("B");
		final IVariable X = TERM.createVariable("X");
		final IVariable Y = TERM.createVariable("Y");
		final IVariable Z = TERM.createVariable("Z");
		final IVariable[] XA = new IVariable[]{X, A};
		final IVariable[] XY = new IVariable[]{X, Y};

		// constructing the rule for the seed

		final IPredicate pred = new AdornedProgram.AdornedPredicate("magic_a", 2, bbf);
		ref.add(seedRule(BASIC.createAtom(pred, BASIC.createTuple(
							TERM.createString("john"), 
							TERM.createString("mary")))));

		// constructing the magic/labeled rules rules

		// label_a_1^bbf(X, A) :- magic_a^bbf(X, Y), b(X, A)
		List<ILiteral> head = Arrays.asList(createLabeledLiteral("a_1", bbf, XA));
		List<ILiteral> body = Arrays.asList(createMagicLiteral("a", bbf, XY), createLiteral("b", "X", "A"));
		ref.add(BASIC.createRule(head, body));
		// label_a_2^bff(X) :- magic_a^bbf(X, Y)
		head = Arrays.asList(createLabeledLiteral("a_2", bff, new ITerm[]{X}));
		body = Arrays.asList(createMagicLiteral("a", bbf, XY));
		ref.add(BASIC.createRule(head, body));
		// magic_a^bbf(X, A) :- label_a_2^bff(X), label_a_1^bbf(X, A)
		head = Arrays.asList(createMagicLiteral("a", bbf, XA));
		body = Arrays.asList(createLabeledLiteral("a_2", bff, new ITerm[]{X}),
			createLabeledLiteral("a_1", bbf, XA));
		ref.add(BASIC.createRule(head, body));

		// constructing the rewritten rules out of the normal ones

		// a^bbf(X, Y, Z) :- magic_a^bbf(X, Y), c(X, Y, Z)
		head = Arrays.asList(createAdornedLiteral("a", bbf, new IVariable[]{X, Y, Z}));
		body = Arrays.asList(createMagicLiteral("a", bbf, XY), 
				createLiteral("c", "X", "Y", "Z"));
		ref.add(BASIC.createRule(head, body));
		// a^bbf(X, Y, Z) :- magic_a^bbf(X, Y), b(X, A), a^bbf(X, A, B), c(B, Y, Z)
		head = Arrays.asList(createAdornedLiteral("a", bbf, new IVariable[]{X, Y, Z}));
		body = Arrays.asList(createMagicLiteral("a", bbf, XY), 
				createLiteral("b", "X", "A"), 
				createAdornedLiteral("a", bbf, new IVariable[]{X, A, B}),
				createLiteral("c", "B", "Y", "Z"));
		ref.add(BASIC.createRule(head, body));

		Collections.sort(ref, AdornmentsTest.RC);
		Collections.sort(result.rules, AdornmentsTest.RC);
		assertEquals("The rules don't match", ref, result.rules);
		// TODO: match the query
	}

	/**
	 * Tests that constatns in bodyliterals are determined as bound.
	 */
	public void testBoundConstant() throws Exception {
		final String prog = "a(?X, ?Y) :- b(?X, ?Z), c('a', ?Z, ?Y). \n" + 
			"c(?X, ?Y, ?Z) :- x(?X, ?Y, ?Z). \n" + 
			"?-a('john', ?Y).";

		final Result result = getResult(prog);

		final ITerm a = TERM.createString("a");
		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final ITerm Z = TERM.createVariable("Z");
		final ITerm[] XYZ = new ITerm[]{X, Y, Z};
		final Adornment[] bbf = new Adornment[]{Adornment.BOUND, Adornment.BOUND, Adornment.FREE};
		final Adornment[] bf = new Adornment[]{Adornment.BOUND, Adornment.FREE};
		final ILiteral b = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("b", 2), BASIC.createTuple(X, Z)));
		final ILiteral x = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("x", 3), BASIC.createTuple(X, Y, Z)));

		final List<IRule> ref = new ArrayList<IRule>();
		// magic_c^bbf(a, Z) :- magic_a^bf(X), b(X, Z)
		ref.add(BASIC.createRule(Arrays.asList(createMagicLiteral("c", bbf, new ITerm[]{a, Z})), 
					Arrays.asList(createMagicLiteral("a", bf, new ITerm[]{X}), b)));
		// a^bf(X, Y) :- magic_a^bf(X), b(X, Z), c^bbf(a, Z, Y)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("a", bf, new ITerm[]{X, Y})), 
					Arrays.asList(
						createMagicLiteral("a", bf, new ITerm[]{X}), 
						b, 
						createAdornedLiteral("c", bbf, new ITerm[]{a, Z, Y}))));
		// c^bbf(X, Y, Z) :- magic_c^bbf(X, Y), x(X, Y, Z)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("c", bbf, XYZ)), 
					Arrays.asList(createMagicLiteral("c", bbf, new ITerm[]{X, Y}), x)));
		// magic_a^bf('john') :- TRUE
		ref.add(seedRule(createMagicLiteral("a", bf, new ITerm[]{TERM.createString("john")})));

		Collections.sort(ref, AdornmentsTest.RC);
		Collections.sort(result.rules, AdornmentsTest.RC);
		assertEquals("The rules don't match", ref, result.rules);
	}

	/**
	 * Tests whether useless magic predicates (magic_q^f()) will be created,
	 * or not.
	 */
	public void testStupidRules() throws Exception {
		final String prog = "q(?X) :- s(?X), not p(?X).\n" + 
			"p(?X) :- r(?X).\n" + 
			"r(?X) :- t(?X).\n" + 
			"?- q(?X).";

		assertNull("The trainsformation should fail.", getResult(prog));
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

		final Result result = getResult(prog);

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

		final List<IRule> ref = new ArrayList<IRule>();
		// magic_r^bbf(b, X) :- p^fb(X, a)
		ref.add(BASIC.createRule(Arrays.asList(createMagicLiteral("r", bbf, 
							new ITerm[]{TERM.createString("b"), X})), 
					Arrays.asList(createAdornedLiteral("p", fb, 
							new ITerm[]{X, TERM.createString("a")}))));
		// magic_s^bb(e, Y) :- p^fb(X, a), r^bbf(b, X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createMagicLiteral("s", bb, 
							new ITerm[]{TERM.createString("e"), Y})),
					Arrays.asList(createAdornedLiteral("p", fb, 
							new ITerm[]{X, TERM.createString("a")}), 
						createAdornedLiteral("r", bbf, 
							new ITerm[]{TERM.createString("b"), X, Y}))));
		// p^fb(X, Y) :- magic_p^fb(Y), c(X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("p", fb, XY)), 
					Arrays.asList(createMagicLiteral("p", fb, new ITerm[]{Y}), c2)));
		// r^bbf(X, Y, Z) :- magic_r^bbf(X, Y), c(X, Y, Z)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("r", bbf, XYZ)), 
					Arrays.asList(createMagicLiteral("r", bbf, XY), c3)));
		// s^bb(X, Y) :- magic_s^bb(X, Y), c(X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("s", bb, XY)), 
					Arrays.asList(createMagicLiteral("s", bb, XY), c2)));
		// p^fb('a') :- TRUE
		ref.add(seedRule(createMagicLiteral("p", fb, new ITerm[]{TERM.createString("a")})));

		Collections.sort(ref, AdornmentsTest.RC);
		Collections.sort(result.rules, AdornmentsTest.RC);
		assertEquals("The rules don't match", ref, result.rules);
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

		final Result result = getResult(prog);

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

		final List<IRule> ref = new ArrayList<IRule>();
		// magic_r^bbf(b, X) :- p^ff(X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createMagicLiteral("r", bbf, 
							new ITerm[]{TERM.createString("b"), X})), 
					Arrays.asList(createAdornedLiteral("p", ff, XY))));
		// magic_s^bb(e, Z) :- p^ff(X, Y), r^bbf(b, X, Z)
		ref.add(BASIC.createRule(Arrays.asList(createMagicLiteral("s", bb, 
							new ITerm[]{TERM.createString("e"), Z})), 
					Arrays.asList(createAdornedLiteral("p", ff, XY), 
						createAdornedLiteral("r", bbf, new ITerm[]{TERM.createString("b"), X, Z}))));
		// r^bbf(X, Y, Z) :- magic_r^bbf(X, Y), c(X, Y, Z)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("r", bbf, XYZ)), 
					Arrays.asList(createMagicLiteral("r", bbf, XY), c3)));
		// s^bb(X, Y) :- magic_s^bb(X, Y), c(X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("s", bb, XY)), 
					Arrays.asList(createMagicLiteral("s", bb, XY), c2)));
		// p^ff(X, Y) :- c(X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("p", ff, XY)), Arrays.asList(c2)));
		// p^ff() :- TRUE
		ref.add(seedRule(createMagicLiteral("p", ff, new ITerm[]{})));

		Collections.sort(ref, AdornmentsTest.RC);
		Collections.sort(result.rules, AdornmentsTest.RC);
		assertEquals("The rules don't match", ref, result.rules);
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

		final Result result = getResult(prog);

		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final ITerm Z = TERM.createVariable("Z");
		final ITerm[] XY = new ITerm[]{X, Y};
		final ITerm[] XYZ = new ITerm[]{X, Y, Z};
		final Adornment[] bb = new Adornment[]{Adornment.BOUND, Adornment.BOUND};
		final Adornment[] bff = new Adornment[]{Adornment.BOUND, Adornment.FREE, Adornment.FREE};
		final ILiteral c2 = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 2), BASIC.createTuple(XY)));
		final ILiteral c3 = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 3), BASIC.createTuple(XYZ)));

		final List<IRule> ref = new ArrayList<IRule>();
		// magic_r^bff(b) :- p^bb(b, a)
		ref.add(BASIC.createRule(Arrays.asList(createMagicLiteral("r", bff, 
							new ITerm[]{TERM.createString("b")})), 
					Arrays.asList(createAdornedLiteral("p", bb, 
							new ITerm[]{TERM.createString("b"), TERM.createString("a")}))));
		// magic_s^bb(e, Y) :- p^bb(b, a), r^bff(b, X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createMagicLiteral("s", bb, 
							new ITerm[]{TERM.createString("e"), Y})), 
					Arrays.asList(createAdornedLiteral("p", bb, 
							new ITerm[]{TERM.createString("b"), TERM.createString("a")}), 
						createAdornedLiteral("r", bff, 
							new ITerm[]{TERM.createString("b"), X, Y}))));
		// p^bb(X, Y) :- magic_p^bb(X, Y), c(X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("p", bb, XY)), 
					Arrays.asList(createMagicLiteral("p", bb, XY), c2)));
		// r^bff(X, Y, Z) :- magic_r^bff(X), c(X, Y, Z)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("r", bff, XYZ)), 
					Arrays.asList(createMagicLiteral("r", bff, new ITerm[]{X}), c3)));
		// s^bb(X, Y) :- magic_s^bb(X, Y), c(X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("s", bb, XY)), 
					Arrays.asList(createMagicLiteral("s", bb, XY), c2)));
		// p^ff() :- TRUE
		ref.add(seedRule(createMagicLiteral("p", bb, new ITerm[]{TERM.createString("b"), TERM.createString("a")})));

		Collections.sort(ref, AdornmentsTest.RC);
		Collections.sort(result.rules, AdornmentsTest.RC);
		assertEquals("The rules don't match", ref, result.rules);
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

		assertNull("The trainsformation should fail.", getResult(prog));
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

		final Result result = getResult(prog);

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

		final List<IRule> ref = new ArrayList<IRule>();
		// magic_r^bbf(b, X) :- p^ff(X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createMagicLiteral("r", bbf, new ITerm[]{TERM.createString("b"), X})),
					Arrays.asList(createAdornedLiteral("p", ff, XY))));
		// magic_r^fbb(Y, X) :- c(X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createMagicLiteral("r", fbb, YX)), Arrays.asList(c2)));
		// magic_s^bb(e, Z) :- p^ff(X, Y), r^bbf(b, X, Z)
		ref.add(BASIC.createRule(Arrays.asList(createMagicLiteral("s", bb, new ITerm[]{TERM.createString("e"), Z})), 
					Arrays.asList(createAdornedLiteral("p", ff, XY), 
						createAdornedLiteral("r", bbf, new ITerm[]{TERM.createString("b"), X, Z}))));
		// r^fbb(X, Y, Z) :- magic_r^fbb(Y, Z), c(X, Y, Z)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("r", fbb, XYZ)), 
					Arrays.asList(createMagicLiteral("r", fbb, YZ), c3)));
		// r^bbf(X, Y, Z) :- magic_r^bbf(X, Y), c(X, Y, Z)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("r", bbf, XYZ)), 
					Arrays.asList(createMagicLiteral("r", bbf, XY), c3)));
		// p^ff(X, Y) :- c(X, Y), r^fbb(Z, Y, X)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("p", ff, XY)), 
					Arrays.asList(c2, 
						createAdornedLiteral("r", fbb, ZYX))));
		// s^bb(X, Y) :- magic_s^bb(X, Y), c(X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("s", bb, XY)), 
					Arrays.asList(createMagicLiteral("s", bb, XY), c2)));
		// p^ff() :- TRUE
		ref.add(seedRule(createMagicLiteral("p", ff, new ITerm[]{})));

		Collections.sort(ref, AdornmentsTest.RC);
		Collections.sort(result.rules, AdornmentsTest.RC);
		assertEquals("The rules don't match", ref, result.rules);
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

		final Result result = getResult(prog);

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

		final List<IRule> ref = new ArrayList<IRule>();
		// magic_r^bbf(b, X) :- p^ff(X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createMagicLiteral("r", bbf, new ITerm[]{TERM.createString("b"), X})),
					Arrays.asList(createAdornedLiteral("p", ff, XY))));
		// magic_s^bb(e, Z) :- p^ff(X, Y), r^bbf(b, X, Z)
		ref.add(BASIC.createRule(Arrays.asList(createMagicLiteral("s", bb, new ITerm[]{TERM.createString("e"), Z})),
					Arrays.asList(createAdornedLiteral("p", ff, XY), 
						createAdornedLiteral("r", bbf, new ITerm[]{TERM.createString("b"), X, Z}))));
		// r^bbf(X, Y, Z) :- magic_r^bbf(X, Y), c(X, Y, Z)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("r", bbf, XYZ)),
					Arrays.asList(createMagicLiteral("r", bbf, XY), c3)));
		// p^ff(X, Y) :- c(X, Y), s^ff(Z, T)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("p", ff, XY)),
					Arrays.asList(c2, createAdornedLiteral("s", ff, new ITerm[]{Z, T}))));
		// s^bb(X, Y) :- magic_s^bb(X, Y), c(X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("s", bb, XY)),
					Arrays.asList(createMagicLiteral("s", bb, XY), c2)));
		// s^ff(X, Y) :- c(X, Y)
		ref.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("s", ff, XY)), Arrays.asList(c2)));
		// p^ff() :- TRUE
		ref.add(seedRule(createMagicLiteral("p", ff, new ITerm[]{})));

		Collections.sort(ref, AdornmentsTest.RC);
		Collections.sort(result.rules, AdornmentsTest.RC);
		assertEquals("The rules don't match", ref, result.rules);
	}

	/**
	 * Test for repeated literals.
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1829204&group_id=167309&atid=842434">bug #1829204: Repeated literal in query fails with magic sets</a>
	 */
	public void testRepeatedLiteralQuery() throws Exception {
		final String prog = "p(1).\n" + 
			"?-p(1),p(1).\n";

		final Result result = getResult(prog);

		final Adornment[] b = new Adornment[]{Adornment.BOUND};
		final ILiteral magic_p = createMagicLiteral("p", b, new ITerm[]{CONCRETE.createInteger(1)});
		final ILiteral p1 = BASIC.createLiteral(true, 
				BASIC.createAtom(BASIC.createPredicate("p", 1), 
				BASIC.createTuple(CONCRETE.createInteger(1))));

		final List<IRule> ref = new ArrayList<IRule>();
		// magic_p^b(1) :- .
		ref.add(BASIC.createRule(Arrays.asList(magic_p), Collections.EMPTY_LIST));
		// magic_p^b(1) :- p(1).
		ref.add(BASIC.createRule(Arrays.asList(magic_p), Arrays.asList(p1)));

		Collections.sort(ref, AdornmentsTest.RC);
		Collections.sort(result.rules, AdornmentsTest.RC);
		assertEquals("The rules don't match", ref, result.rules);
	}

	/**
	 * Test some transformations, which should fail.
	 */
	public void testFailingTransformation() throws Exception {
		assertNull("A query with only variables should fail",
				getResult("?- a(?A, ?B), b(?C, ?D), c(?E, ?F)."));
		assertNull("A query with only constants in builtins should fail",
				getResult("?- a(?A, ?B), LESS('a', ?D), c(?E, ?F)."));
		assertNotNull("A query with constants in literals should succeed",
				getResult("?- a(?A, ?B), b('a', ?D), c(?E, ?F)."));
	}

	/**
	 * Test to ensure the correct handling of conjunctive queries with
	 * negative literals.
	 * @see <a href="https://sourceforge.net/tracker/index.php?func=detail&aid=1904505&group_id=167309&atid=842434">bug #1904505: magic sets: negative rules with negative query literals</a>
	 */
	public void testNegativeConjunctiveQuery() throws Exception {
		final String prog = "?-p(?X, ?Y), not q(?X, 3).\n"
			+ "p(?X, ?Y) :- a(?X, ?Y).\n"
			+ "q(?X, ?Y) :- b(?X, ?Y).\n";

		final Result result = getResult(prog);

		final Adornment[] BB = new Adornment[]{Adornment.BOUND, Adornment.BOUND};
		final Adornment[] FF = new Adornment[]{Adornment.FREE, Adornment.FREE};
		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final ITerm[] XY = new ITerm[]{X, Y};
		final ITerm[] X3 = new ITerm[]{X, CONCRETE.createInteger(3)};

		// assert the rules
		final List<IRule> rules = new ArrayList<IRule>();

		// magic_p^ff() :- .
		rules.add(BASIC.createRule(Arrays.asList(createMagicLiteral("p", FF, new ITerm[]{})),
					Collections.EMPTY_LIST));
		// magic_q^bb(?X, 3) :- p^ff(?X, ?Y).
		rules.add(BASIC.createRule(Arrays.asList(createMagicLiteral("q", BB, X3)),
					Arrays.asList(createAdornedLiteral("p", FF, XY))));
		// p^ff(?X, ?Y) :- a(?X, ?Y).
		rules.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("p", FF, XY)),
					Arrays.asList(createLiteral("a", "X", "Y"))));
		// q^bb(?X, ?Y) :- magic_q^bb(?X, ?Y), b(?X, ?Y).
		rules.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("q", BB, XY)),
					Arrays.asList(createMagicLiteral("q", BB, XY), createLiteral("b", "X", "Y"))));

		Collections.sort(rules, AdornmentsTest.RC);
		Collections.sort(result.rules, AdornmentsTest.RC);
		assertEquals("The rules don't match", rules, result.rules);

		// assert the query
		// ?- p^ff(?X, ?Y), !q^bb(?X, 3).
		final IQuery query = BASIC.createQuery(Arrays.asList(createAdornedLiteral("p", FF, XY),
					createAdornedLiteral(false, "q", BB, X3)));

		assertEquals("The query doesn't match", query, result.query);
	}

	/**
	 * <p>
	 * Test to ensure correct creation of labaled rules.
	 * </p>
	 * <p>
	 * This test checks for 2 problems:
	 * <ul>
	 * <li>rule heads can not be negative (even if they are labeled)</li>
	 * <li>labeled rules must be save</li>
	 * </ul>
	 * </p>
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1907086&group_id=167309&atid=842434">bug #1907086: magic sets: labeled rules are not consturced correctly</a>
	 */
	public void testUnsaveLabeledRuleCreation() throws Exception {
		final String prog = "c(?X, ?Y) :- e(?X, ?Y).\n"
			+ "b(?X, ?Y) :- d(?X, ?Y).\n"
			+ "a(?X, ?Y) :- b(?X, ?Y), not c(?X, ?Y).\n"
			+ "?- a(2, ?Y).\n";
		final Result result = getResult(prog);

		final Adornment[] BB = new Adornment[]{Adornment.BOUND, Adornment.BOUND};
		final Adornment[] BF = new Adornment[]{Adornment.BOUND, Adornment.FREE};
		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final ITerm[] XY = new ITerm[]{X, Y};

		// construct the rules
		final List<IRule> rules = new ArrayList<IRule>();

		// a^bf(?X, ?Y) :- magic_a^bf(?X), b^bf(?X, ?Y), !c^bb(?X, ?Y).
		rules.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("a", BF, XY)),
					Arrays.asList(createMagicLiteral("a", BF, new ITerm[]{X}),
						createAdornedLiteral("b", BF, XY),
						createAdornedLiteral(false, "c", BB, XY))));
		// b^bf(?X, ?Y) :- magic_b^bf(?X), d(?X, ?Y).
		rules.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("b", BF, XY)),
				Arrays.asList(createMagicLiteral("b", BF, new ITerm[]{X}),
					createLiteral("d", "X", "Y"))));
		// c^bb(?X, ?Y) :- magic_c^bb(?X, ?Y), e(?X, ?Y).
		rules.add(BASIC.createRule(Arrays.asList(createAdornedLiteral("c", BB, XY)),
				Arrays.asList(createMagicLiteral("c", BB, XY),
					createLiteral("e", "X", "Y"))));
		// magic_a^bf(2) :- .
		rules.add(seedRule(createMagicLiteral("a", BF, new ITerm[]{CONCRETE.createInteger(2)})));
		// magic_b^bf(?X) :- magic_a^bf(?X).
		rules.add(BASIC.createRule(Arrays.asList(createMagicLiteral("b", BF, new ITerm[]{X})),
				Arrays.asList(createMagicLiteral("a", BF, new ITerm[]{X}))));
		// label_c_1^bf(?X) :- magic_a^bf(?X).
		rules.add(BASIC.createRule(Arrays.asList(createLabeledLiteral("c_1", BF, new ITerm[]{X})),
				Arrays.asList(createMagicLiteral("a", BF, new ITerm[]{X}))));
		// label_c_2^bb(?X, ?Y) :- magic_a^bf(?X), b^bf(?X, ?Y).
		rules.add(BASIC.createRule(Arrays.asList(createLabeledLiteral("c_2", BB, XY)),
				Arrays.asList(createMagicLiteral("a", BF, new ITerm[]{X}),
					createAdornedLiteral("b", BF, XY))));
		// magic_c^bb(?X, ?Y) :- label_c_1^bf(?X), label_c_2^bb(?X, ?Y).
		rules.add(BASIC.createRule(Arrays.asList(createMagicLiteral("c", BB, XY)),
				Arrays.asList(createLabeledLiteral("c_1", BF, new ITerm[]{X}),
					createLabeledLiteral("c_2", BB, XY))));

		// construct the query
		// ?- a^bf(2, ?Y).
		final IQuery query = BASIC.createQuery(Arrays.asList(
					createAdornedLiteral("a", BF, new ITerm[]{CONCRETE.createInteger(2), Y})));

		printDebug("usave", prog, new Result(rules, query), result);

		assertResults(new Result(rules, query), result);
	}

	/**
	 * Prints a program and the resulting magic program in a formated
	 * way.
	 * @param name the name to identify the test
	 * @param prog the input program
	 * @param result the magic set result
	 */
	private static void printDebug(final String name, final String prog, final Result result) {
		printDebug(name, prog, null, result);
	}

	/**
	 * Prints out a program, resulting magic program and the expected
	 * result.
	 * @param name the name to identify the printing
	 * @param prog the string representation of the input program
	 * @param expected the expected transformation result
	 * @param result the real outcome of the transformation
	 */
	private static void printDebug(final String name, final String prog,
			final Result expected, final Result result) {
		System.out.println("---");
		System.out.println(name);
		System.out.println("\tinput:");
		System.out.println(prog);

		if (expected != null) {
			System.out.println("\texpected:");
			System.out.println(resultString(expected));
		}

		System.out.println("\tresult:");
		System.out.println(resultString(result));
	}

	/**
	 * Transforms the result to a string.
	 * @param r the result
	 * @return the string representation
	 */
	private static String resultString(final Result r) {
		assert r != null: "The result must not be null";

		final StringBuilder buffer = new StringBuilder();

		// sorting the reslt rules
		final List<IRule> sortRules = new ArrayList<IRule>(r.rules);
		Collections.sort(sortRules, AdornmentsTest.RC);

		// printing the result rules
		for (final IRule rule : sortRules) {
			buffer.append(rule).append(System.getProperty("line.separator"));
		}
		buffer.append(System.getProperty("line.separator"));
		buffer.append(r.query).append(System.getProperty("line.separator"));
		return buffer.toString();
	}

	public static Test suite() {
		return new TestSuite(MagicTest.class, MagicTest.class.getSimpleName());
	}
}
