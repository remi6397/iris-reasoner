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
package org.deri.iris.evaluation.common;

// TODO: test handling of builtins

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;
import static org.deri.iris.MiscHelper.createLiteral;
import static org.deri.iris.MiscHelper.createVarList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import org.deri.iris.api.evaluation.common.IAdornedPredicate;
import org.deri.iris.api.evaluation.common.IAdornedProgram;
import org.deri.iris.api.evaluation.common.IAdornedRule;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.compiler.Parser;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedRule;
import org.deri.iris.evaluation.magic.SIPImpl;
import org.deri.iris.factory.Factory;
import org.deri.iris.MiscHelper;

/**
 * <p>
 * Tests the adornments.
 * </p>
 * <p>
 * $Id: AdornmentsTest.java,v 1.7 2007-09-27 14:52:39 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.7 $
 */
public class AdornmentsTest extends TestCase {

	private IAdornedProgram p0;

	private IAdornedProgram p1;

	public static final Comparator<IRule> RC = new RuleComparator();

	public static Test suite() {
		return new TestSuite(AdornmentsTest.class, AdornmentsTest.class
				.getSimpleName());
	}

	public void setUp() {
		// constructing the rules for p0
		Set<IRule> rules = new HashSet<IRule>();
		// sg(X, Y) :- flat(X, Y)
		IHead head = BASIC.createHead(createLiteral("sg", "X", "Y"));
		IBody body = BASIC.createBody(createLiteral("flat", "X", "Y"));
		rules.add(BASIC.createRule(head, body));

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
		rules.add(BASIC.createRule(head, body));

		// constructing the query
		// sg(john, Y)
		IQuery query0 = BASIC.createQuery(BASIC.createLiteral(true, BASIC
				.createPredicate("sg", 2), BASIC.createTuple(TERM
				.createString("john"), TERM.createVariable("Y"))));
		p0 = new AdornedProgram(rules, query0);

		// constructing the rules for p1
		rules = new HashSet<IRule>();
		// rsg(X, Y) :- flat(X, Y)
		head = BASIC.createHead(createLiteral("rsg", "X", "Y"));
		body = BASIC.createBody(createLiteral("flat", "X", "Y"));
		rules.add(BASIC.createRule(head, body));

		// rsg(X, Y) :- up(X, X1), rsg(Y1, X1), down(Y1, Y)
		head = BASIC.createHead(createLiteral("rsg", "X", "Y"));
		bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("up", "X", "X1"));
		bodyLiterals.add(createLiteral("rsg", "Y1", "X1"));
		bodyLiterals.add(createLiteral("down", "Y1", "Y"));
		body = BASIC.createBody(bodyLiterals);
		rules.add(BASIC.createRule(head, body));

		// constructing the query
		// rsg(a, Y)
		IQuery query1 = BASIC.createQuery(BASIC.createLiteral(true, BASIC
				.createPredicate("rsg", 2), BASIC.createTuple(TERM
				.createString("a"), TERM.createVariable("Y"))));
		p1 = new AdornedProgram(rules, query1);
	}

	/**
	 * Tests whether all adorned predicates are available.
	 */
	public void testAdornedPredicatesP0() {
		// constructing the reference adorned predicate set
		final Set<IAdornedPredicate> preds = new HashSet<IAdornedPredicate>(1);
		preds.add(new AdornedPredicate("sg", new Adornment[] { Adornment.BOUND,
				Adornment.FREE }));

		// asserting the adorned predicates
		assertEquals("There are not all predicates created", preds, p0
				.getAdornedPredicates());
	}

	/**
	 * Tests whether all adorned predicates are available.
	 */
	public void testAdornedPredicatesP1() {
		// constructing the reference adorned predicate set
		final Set<IAdornedPredicate> preds = new HashSet<IAdornedPredicate>(1);
		preds.add(new AdornedPredicate("rsg", new Adornment[] {
				Adornment.BOUND, Adornment.FREE }));
		preds.add(new AdornedPredicate("rsg", new Adornment[] { Adornment.FREE,
				Adornment.BOUND }));

		// asserting the adorned predicates
		assertEquals("There are not all predicates created", preds, p1
				.getAdornedPredicates());
	}

	/**
	 * <p>
	 * Tests whether all adorned rules are computed
	 * </p>
	 * <p>
	 * <b>ATTENTION: at the moment only the &quot;core&quot; rules are compared,
	 * because while the computation of the adorned rules different queries are
	 * created to get the corresponding sip. So the sip of the adorned rules
	 * isn't compared</b>
	 * </p>
	 */
	public void testAdornedRulesP0() {
		// constructing the adorned predicate
		final IAdornedPredicate pred = new AdornedPredicate("sg",
				new Adornment[] { Adornment.BOUND, Adornment.FREE });

		// constructing the adorned rules
		Set<IAdornedRule> rules = new HashSet<IAdornedRule>();
		// sg^bf(X, Y) :- flat(X, Y)
		IHead head = BASIC.createHead(BASIC.createLiteral(true, pred, BASIC
				.createTuple(new ArrayList<ITerm>(createVarList("X", "Y")))));
		IBody body = BASIC.createBody(createLiteral("flat", "X", "Y"));
		IRule r = BASIC.createRule(head, body);
		// we can call new AdornedRule, because there is no computation, and
		// we assume that the sip is created correct.
		rules
				.add(new AdornedRule(r, new SIPImpl(unadornRule(r), p0
						.getQuery())));

		// sg^bf(X, Y) :- up(X, Z1), sg^bf(Z1, Z2), flat(Z2, Z3), sg^bf(Z3, Z4),
		// down(Z4, Y)
		head = BASIC.createHead(BASIC.createLiteral(true, pred, BASIC
				.createTuple(new ArrayList<ITerm>(createVarList("X", "Y")))));
		final List<ILiteral> bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("up", "X", "Z1"));
		bodyLiterals.add(BASIC.createLiteral(true, pred, BASIC
				.createTuple(new ArrayList<ITerm>(createVarList("Z1", "Z2")))));
		bodyLiterals.add(createLiteral("flat", "Z2", "Z3"));
		bodyLiterals.add(BASIC.createLiteral(true, pred, BASIC
				.createTuple(new ArrayList<ITerm>(createVarList("Z3", "Z4")))));
		bodyLiterals.add(createLiteral("down", "Z4", "Y"));
		body = BASIC.createBody(bodyLiterals);
		r = BASIC.createRule(head, body);
		// we can call new AdornedRule, because there is no computation, and
		// we assume that the sip is created correct.
		rules
				.add(new AdornedRule(r, new SIPImpl(unadornRule(r), p0
						.getQuery())));

		final List<IAdornedRule> l0 = new ArrayList<IAdornedRule>(rules);
		final List<IAdornedRule> l1 = new ArrayList<IAdornedRule>(p0
				.getAdornedRules());

		assertEquals("The amount of rules must be equal", l0.size(), l1.size());

		Collections.sort(l0, RC);
		Collections.sort(l1, RC);

		// TODO: maybe look whether the sip contains all edges
		for (Iterator<IAdornedRule> i0 = l0.iterator(), i1 = l1.iterator(); i0
				.hasNext();) {
			final IAdornedRule r0 = i0.next();
			final IAdornedRule r1 = i1.next();
			assertEquals(
					"The rules\n" + r0 + "\nand\n" + r1 + "\ndon't match.", 0,
					RC.compare(r0, r1));
		}
	}

	/**
	 * <p>
	 * Tests whether all adorned rules are computed
	 * </p>
	 * <p>
	 * <b>ATTENTION: at the moment only the &quot;core&quot; rules are compared,
	 * because while the computation of the adorned rules different queries are
	 * created to get the corresponding sip. So the sip of the adorned rules
	 * isn't compared</b>
	 * </p>
	 */
	public void testAdornedRulesP1() {
		// constructing the adorned predicate
		final IAdornedPredicate pred_bf = new AdornedPredicate("rsg",
				new Adornment[] { Adornment.BOUND, Adornment.FREE });
		final IAdornedPredicate pred_fb = new AdornedPredicate("rsg",
				new Adornment[] { Adornment.FREE, Adornment.BOUND });

		// constructing the adorned rules
		Set<IAdornedRule> rules = new HashSet<IAdornedRule>();
		// rsg^bf(X, Y) :- flat(X, Y)
		IHead head = BASIC.createHead(BASIC.createLiteral(true, pred_bf, BASIC
				.createTuple(new ArrayList<ITerm>(createVarList("X", "Y")))));
		IBody body = BASIC.createBody(createLiteral("flat", "X", "Y"));
		IRule r = BASIC.createRule(head, body);
		// we can call new AdornedRule, because there is no computation, and
		// we assume that the sip is created correct.
		rules
				.add(new AdornedRule(r, new SIPImpl(unadornRule(r), p1
						.getQuery())));
		// rsg^fb(X, Y) :- flat(X, Y)
		head = BASIC.createHead(BASIC.createLiteral(true, pred_fb, BASIC
				.createTuple(new ArrayList<ITerm>(createVarList("X", "Y")))));
		body = BASIC.createBody(createLiteral("flat", "X", "Y"));
		r = BASIC.createRule(head, body);
		// we can call new AdornedRule, because there is no computation, and
		// we assume that the sip is created correct.
		rules
				.add(new AdornedRule(r, new SIPImpl(unadornRule(r), p1
						.getQuery())));

		// rsg^bf(X, Y) :- up(X, X1), rsg^fb(Y1, X1), down(Y1, Y)
		head = BASIC.createHead(BASIC.createLiteral(true, pred_bf, BASIC
				.createTuple(new ArrayList<ITerm>(createVarList("X", "Y")))));
		List<ILiteral> bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("up", "X", "X1"));
		bodyLiterals.add(BASIC.createLiteral(true, pred_fb, BASIC
				.createTuple(new ArrayList<ITerm>(createVarList("Y1", "X1")))));
		bodyLiterals.add(createLiteral("down", "Y1", "Y"));
		body = BASIC.createBody(bodyLiterals);
		r = BASIC.createRule(head, body);
		// we can call new AdornedRule, because there is no computation, and
		// we assume that the sip is created correct.
		rules
				.add(new AdornedRule(r, new SIPImpl(unadornRule(r), p1
						.getQuery())));

		// rsg^fb(X, Y) :- up(X, X1), rsg^fb(Y1, X1), down(Y1, Y)
		head = BASIC.createHead(BASIC.createLiteral(true, pred_fb, BASIC
				.createTuple(new ArrayList<ITerm>(createVarList("X", "Y")))));
		bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("up", "X", "X1"));
		bodyLiterals.add(BASIC.createLiteral(true, pred_fb, BASIC
				.createTuple(new ArrayList<ITerm>(createVarList("Y1", "X1")))));
		bodyLiterals.add(createLiteral("down", "Y1", "Y"));
		body = BASIC.createBody(bodyLiterals);
		r = BASIC.createRule(head, body);
		// we can call new AdornedRule, because there is no computation, and
		// we assume that the sip is created correct.
		rules
				.add(new AdornedRule(r, new SIPImpl(unadornRule(r), p1
						.getQuery())));

		final List<IAdornedRule> l0 = new ArrayList<IAdornedRule>(rules);
		final List<IAdornedRule> l1 = new ArrayList<IAdornedRule>(p1
				.getAdornedRules());

		assertEquals("The amount of rules must be equal", l0.size(), l1.size());

		Collections.sort(l0, RC);
		Collections.sort(l1, RC);

		// TODO: maybe look whether the sip contains all edges
		for (Iterator<IAdornedRule> i0 = l0.iterator(), i1 = l1.iterator(); i0
				.hasNext();) {
			final IAdornedRule r0 = i0.next();
			final IAdornedRule r1 = i1.next();
			assertEquals(
					"The rules\n" + r0 + "\nand\n" + r1 + "\ndon't match.", 0,
					RC.compare(r0, r1));
		}
	}

	/**
	 * Creates an unadorned version of an adorned rule.
	 * 
	 * @param r
	 *            the adorned rule
	 * @return the unadorned rule
	 * @throws NullPointerException
	 *             if the rule is {@code null}
	 */
	public static IRule unadornRule(final IRule r) {
		if (r == null) {
			throw new NullPointerException("The rule must not be null");
		}
		boolean changed = false;
		List<ILiteral> lits = new ArrayList<ILiteral>(r.getHeadLiterals());
		int i = 0;
		for (final ILiteral l : lits) {
			if (l.getPredicate() instanceof IAdornedPredicate) {
				lits.set(i, BASIC.createLiteral(l.isPositive(),
						((IAdornedPredicate) l.getPredicate())
								.getUnadornedPredicate(), l.getTuple()));
				changed = true;
			}
			i++;
		}
		final IHead h = BASIC.createHead(lits);
		lits = new ArrayList<ILiteral>(r.getBodyLiterals());
		i = 0;
		for (final ILiteral l : lits) {
			if (l.getPredicate() instanceof IAdornedPredicate) {
				lits.set(i, BASIC.createLiteral(l.isPositive(),
						((IAdornedPredicate) l.getPredicate())
								.getUnadornedPredicate(), l.getTuple()));
				changed = true;
			}
			i++;
		}
		final IBody b = BASIC.createBody(lits);
		return changed ? BASIC.createRule(h, b) : r;
	}

	/**
	 * <p>
	 * Tests whether all adorned rules are computed when the query got no
	 * arguments bound.
	 * </p>
	 * <p>
	 * <b>ATTENTION: at the moment only the &quot;core&quot; rules are compared,
	 * because while the computation of the adorned rules different queries are
	 * created to get the corresponding sip. So the sip of the adorned rules
	 * isn't compared</b>
	 * </p>
	 */
	public void testFreeQuery() {
		// w(Y) :- k(X, Y), l(X)
		final IRule r = BASIC.createRule(BASIC.createHead(createLiteral("w",
				"Y")), BASIC.createBody(createLiteral("k", "X", "Y"),
				createLiteral("l", "X")));
		// :- w(X)
		final IQuery q = BASIC.createQuery(createLiteral("w", "X"));
		final AdornedProgram a = new AdornedProgram(Collections.singleton(r), q);
		assertTrue("There must not any adorned rules be created", a
				.getAdornedRules().isEmpty());
	}

	/**
	 * <p>
	 * Tests whether all adorned rules are computed when the query got no
	 * arguments bound.
	 * </p>
	 * <p>
	 * <b>ATTENTION: at the moment only the &quot;core&quot; rules are compared,
	 * because while the computation of the adorned rules different queries are
	 * created to get the corresponding sip. So the sip of the adorned rules
	 * isn't compared</b>
	 * </p>
	 */
	public void testFreeQuery1() {
		// constructing the adorned predicate
		final IAdornedPredicate pred = new AdornedPredicate("w",
				new Adornment[] { Adornment.BOUND, Adornment.FREE });

		// w(X, Y) :- k(X, B), l(B, C), w(C, Y)
		final IRule r = BASIC.createRule(BASIC.createHead(createLiteral("w",
				"X", "Y")), BASIC.createBody(createLiteral("k", "X", "B"),
				createLiteral("l", "B", "C"), createLiteral("w", "C", "Y")));
		// w(X, Y)
		final IQuery q = BASIC.createQuery(createLiteral("w", "X", "Y"));

		// constructing the adorned rules
		Set<IAdornedRule> rules = new HashSet<IAdornedRule>();

		// w^bf(X, Y) :- k(X, B), l(B, C), w^bf(C, Y)
		IHead head = BASIC.createHead(BASIC.createLiteral(true, pred, BASIC
				.createTuple(new ArrayList<ITerm>(createVarList("X", "Y")))));
		IBody body = BASIC.createBody(createLiteral("k", "X", "B"),
				createLiteral("l", "B", "C"), BASIC.createLiteral(true, pred,
						BASIC.createTuple(new ArrayList<ITerm>(createVarList(
								"C", "Y")))));
		IRule rule = BASIC.createRule(head, body);
		// we can call new AdornedRule, because there is no computation, and
		// we assume that the sip is created correct.
		rules.add(new AdornedRule(rule, new SIPImpl(unadornRule(rule), q)));

		// w(X, Y) :- k(X, B), l(B, C), w^bf(C, Y)
		head = BASIC.createHead(createLiteral("w", "X", "Y"));
		body = BASIC.createBody(createLiteral("k", "X", "B"), createLiteral(
				"l", "B", "C"), BASIC.createLiteral(true, pred, BASIC
				.createTuple(new ArrayList<ITerm>(createVarList("C", "Y")))));
		rule = BASIC.createRule(head, body);
		// we can call new AdornedRule, because there is no computation, and
		// we assume that the sip is created correct.
		rules.add(new AdornedRule(rule, new SIPImpl(unadornRule(rule), q)));

		final AdornedProgram a = new AdornedProgram(Collections.singleton(r), q);

		final List<IAdornedRule> l0 = new ArrayList<IAdornedRule>(rules);
		final List<IAdornedRule> l1 = new ArrayList<IAdornedRule>(a
				.getAdornedRules());

		assertEquals("The amount of rules must be equal", l0.size(), l1.size());

		Collections.sort(l0, RC);
		Collections.sort(l1, RC);

		// TODO: maybe look whether the sip contains all edges
		for (Iterator<IAdornedRule> i0 = l0.iterator(), i1 = l1.iterator(); i0
				.hasNext();) {
			final IAdornedRule r0 = i0.next();
			final IAdornedRule r1 = i1.next();
			assertEquals(
					"The rules\n" + r0 + "\nand\n" + r1 + "\ndon't match.", 0,
					RC.compare(r0, r1));
		}
	}

	/**
	 * <p>
	 * Tests whether all adorned rules are computed when the query got no
	 * arguments bound.
	 * </p>
	 * <p>
	 * <b>ATTENTION: at the moment only the &quot;core&quot; rules are compared,
	 * because while the computation of the adorned rules different queries are
	 * created to get the corresponding sip. So the sip of the adorned rules
	 * isn't compared</b>
	 * </p>
	 */
	public void testFreeQuery2() {
		// w(X, Y) :- k(X, B), l(B, C), w(D, Y)
		final IRule r = BASIC.createRule(BASIC.createHead(createLiteral("w",
				"X", "Y")), BASIC.createBody(createLiteral("k", "X", "B"),
				createLiteral("l", "B", "C"), createLiteral("w", "D", "Y")));
		// w(X, Y)
		final IQuery q = BASIC.createQuery(createLiteral("w", "X", "Y"));
		final AdornedProgram a = new AdornedProgram(Collections.singleton(r), q);
		assertTrue("There must not any adorned rules be created", a
				.getAdornedRules().isEmpty());
	}

	public void testConstructedAdornment() {
		// w(const(X, A, B)
		final ILiteral constr = BASIC.createLiteral(true, BASIC
				.createPredicate("w", 3), BASIC.createTuple(TERM
				.createConstruct("const", TERM.createVariable("X"), TERM
						.createVariable("A"), TERM.createVariable("B")), TERM
				.createVariable("C"), TERM.createVariable("Y")));
		// w(X, Y, Z) :- k(A, B, Y), w(const(X, A, B), C, Y)
		final IRule r = BASIC.createRule(BASIC.createHead(createLiteral("w",
				"X", "Y", "Z")), BASIC.createBody(createLiteral("k", "A", "B",
				"Y"), constr));
		// w(asdf, jklö, Z)
		final IQuery q = BASIC.createQuery(BASIC.createLiteral(true, BASIC
				.createPredicate("w", 3), BASIC.createTuple(TERM
				.createString("asdf"), TERM.createString("jklö"), TERM
				.createVariable("Z"))));

		final AdornedProgram a = new AdornedProgram(Collections.singleton(r),
				q);

		// constructing the adorned rules
		// constructing the adorned predicates
		final IAdornedPredicate bbf = new AdornedPredicate("w",
				new Adornment[] { Adornment.BOUND, Adornment.BOUND,
						Adornment.FREE });
		final IAdornedPredicate bfb = new AdornedPredicate("w",
				new Adornment[] { Adornment.BOUND, Adornment.FREE,
						Adornment.BOUND });

		final Set<IAdornedRule> rules = new HashSet<IAdornedRule>();

		// w^bbf(X, Y, Z) :- k(A, B, Y), w^bfb(const[X, A, B], C, Y)
		IHead head = BASIC.createHead(BASIC.createLiteral(true, bbf,
				BASIC.createTuple(new ArrayList<ITerm>(createVarList("X", "Y",
						"Z")))));
		IBody body = BASIC.createBody(createLiteral("k", "X", "B", "Y"), BASIC
				.createLiteral(true, bfb, BASIC.createTuple(TERM
						.createConstruct("const", TERM.createVariable("X"),
								TERM.createVariable("A"), TERM
										.createVariable("B")), TERM
						.createVariable("C"), TERM.createVariable("Y"))));
		IRule rule = BASIC.createRule(head, body);
		// we can call new AdornedRule, because there is no computation, and
		// we assume that the sip is created correct.
		rules.add(new AdornedRule(rule, new SIPImpl(unadornRule(rule), q)));
		
		// w^bfb(X, Y, Z) :- k(A, B, Y), w^bfb(const[X, A, B], C, Y)
		head = BASIC.createHead(BASIC.createLiteral(true, bfb,
				BASIC.createTuple(new ArrayList<ITerm>(createVarList("X", "Y",
				"Z")))));
		body = BASIC.createBody(createLiteral("k", "X", "B", "Y"), BASIC
				.createLiteral(true, bfb, BASIC.createTuple(TERM
						.createConstruct("const", TERM.createVariable("X"),
								TERM.createVariable("A"), TERM
								.createVariable("B")), TERM
								.createVariable("C"), TERM.createVariable("Y"))));
		rule = BASIC.createRule(head, body);
		// we can call new AdornedRule, because there is no computation, and
		// we assume that the sip is created correct.
		rules.add(new AdornedRule(rule, new SIPImpl(unadornRule(rule), q)));
		
		// asserting the result
		final List<IAdornedRule> l0 = new ArrayList<IAdornedRule>(rules);
		final List<IAdornedRule> l1 = new ArrayList<IAdornedRule>(a
				.getAdornedRules());

		assertEquals("The amount of rules must be equal", l0.size(), l1.size());

		Collections.sort(l0, RC);
		Collections.sort(l1, RC);

		// TODO: maybe look whether the sip contains all edges
		for (Iterator<IAdornedRule> i0 = l0.iterator(), i1 = l1.iterator(); i0
				.hasNext();) {
			final IAdornedRule r0 = i0.next();
			final IAdornedRule r1 = i1.next();
			assertEquals(
					"The rules\n" + r0 + "\nand\n" + r1 + "\ndon't match.", 0,
					RC.compare(r0, r1));
		}
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
	 * Tests that constants in literals in the body will be marked as bound.
	 */
	public void testConstantsInBody() throws Exception {
		final String program = "a(?X, ?Y) :- b(?X, ?Z), c('a', ?Z, ?Y). \n" + 
			"c(?X, ?Y, ?Z) :- x(?X, ?Y, ?Z). \n" + 
			"?-a('john', ?Y).";
		final IProgram p = Factory.PROGRAM.createProgram();
		Parser.parse(program, p);
		final AdornedProgram ap = new AdornedProgram(p.getRules(), p.getQueries().iterator().next());

		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final ITerm Z = TERM.createVariable("Z");
		final ITerm[] XYZ = new ITerm[]{X, Y, Z};
		final Adornment[] bbf = new Adornment[]{Adornment.BOUND, Adornment.BOUND, Adornment.FREE};
		final Adornment[] bf = new Adornment[]{Adornment.BOUND, Adornment.FREE};
		final ILiteral b = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("b", 2), BASIC.createTuple(X, Z)));
		final ILiteral x = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("x", 3), BASIC.createTuple(XYZ)));

		final Set<IRule> ref = new HashSet<IRule>();
		// a^bf(?X, ?Y) :- b(?X, ?Z), c^bbf('a', ?Z, ?Y)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("a", bf, new ITerm[]{X, Y})), 
						BASIC.createBody(
							b, createAdornedLiteral("c", bbf, new ITerm[]{TERM.createString("a"), Z, Y}))));
		// c^bbf(?X, ?Y, ?Z) :- x(?X, ?Y, ?Z)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("c", bbf, XYZ)), BASIC.createBody(x)));

		assertTrue("The rules must be '" + MiscHelper.join("\n", ref) + "', but were '" + 
				MiscHelper.join("\n", ap.getAdornedRules()) + "'", 
				MiscHelper.compare(ap.getAdornedRules(), ref, RC));
	}

	/**
	 * Tests conjunctive queries.
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1798276&group_id=167309&atid=842434">bug #1798276: Magic Sets evaluation does not allow conjunctive queries</a>
	 */
	public void testConjunctiveQuery() throws Exception {
		final String prog = "p(?X, ?X) :- c(?X).\n" + 
			"r(?X, ?X, ?X) :- c(?X).\n" + 
			"s(?X, ?X) :- c(?X).\n" + 
			"?- p(?X, 'a'), r('b', ?X, ?Y), s('e', ?Y).";
		final IProgram p = Parser.parse(prog);
		final AdornedProgram ap = new AdornedProgram(p.getRules(), p.getQueries().iterator().next());

		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final Adornment[] bbf = new Adornment[]{Adornment.BOUND, Adornment.BOUND, Adornment.FREE};
		final Adornment[] fb = new Adornment[]{Adornment.FREE, Adornment.BOUND};
		final Adornment[] bb = new Adornment[]{Adornment.BOUND, Adornment.BOUND};
		final ILiteral c = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("c", 1), BASIC.createTuple(X)));
		final ITerm[] XX = new ITerm[]{X, X};

		final Set<IRule> ref = new HashSet<IRule>();
		// p^fb(?X, ?X) :- c(?X)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("p", fb, XX)), BASIC.createBody(c)));
		// r^bbf(?X, ?X, ?X) :- c(?X)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("r", bbf, new ITerm[]{X, X, X})), BASIC.createBody(c)));
		// s^bb(?X, ?X) :- c(?X)
		ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("s", bb, XX)), BASIC.createBody(c)));

		assertTrue("The rules must be '" + MiscHelper.join("\n", ref) + "', but were '" + 
				MiscHelper.join("\n", ap.getAdornedRules()) + "'", 
				MiscHelper.compare(ap.getAdornedRules(), ref, RC));
	}

	/**
	 * <p>
	 * Compares two rules according to their predicate symbols.
	 * </p>
	 * <p>
	 * $Id: AdornmentsTest.java,v 1.7 2007-09-27 14:52:39 bazbishop237 Exp $
	 * </p>
	 * 
	 * @author Richard Pöttler (richard dot poettler at deri dot org)
	 * @version $Revision: 1.7 $
	 */
	private static class RuleComparator implements Comparator<IRule> {
		public int compare(IRule o1, IRule o2) {
			if ((o1 == null) || (o2 == null)) {
				throw new NullPointerException("The rules must not be null");
			}

			int res = 0;
			if ((res = o1.getHeadLenght() - o2.getHeadLenght()) != 0) {
				return res;
			}
			for (final Iterator<ILiteral> i1 = o1.getHeadLiterals().iterator(), i2 = o2
					.getHeadLiterals().iterator(); i1.hasNext();) {
				if ((res = compareLiteral(i1.next(), i2.next())) != 0) {
					return res;
				}
			}
			if ((res = o1.getBodyLenght() - o2.getBodyLenght()) != 0) {
				return res;
			}
			for (final Iterator<ILiteral> i1 = o1.getBodyLiterals().iterator(), i2 = o2
					.getBodyLiterals().iterator(); i1.hasNext();) {
				if ((res = compareLiteral(i1.next(), i2.next())) != 0) {
					return res;
				}
			}
			return 0;
		}

		private static int compareLiteral(final ILiteral l1, final ILiteral l2) {
			if ((l1 == null) || (l2 == null)) {
				throw new NullPointerException("The literals must not be null");
			}
			int res = 0;
			final IPredicate p1 = l1.getPredicate();
			final IPredicate p2 = l2.getPredicate();
			// comparing the predicate symbol
			if ((res = p1.getPredicateSymbol().compareTo(
					p2.getPredicateSymbol())) != 0) {
				return res;
			}
			// comparing the arity
			if ((res = p1.getArity() - p2.getArity()) != 0) {
				return res;
			}
			// comparing the adornments
			if ((p1 instanceof IAdornedPredicate)
					&& !(p2 instanceof IAdornedPredicate)) {
				return 1;
			} else if (!(p1 instanceof IAdornedPredicate)
					&& (p2 instanceof IAdornedPredicate)) {
				return -1;
			} else if ((p1 instanceof IAdornedPredicate)
					&& (p2 instanceof IAdornedPredicate)) {
				final Adornment[] a1 = ((IAdornedPredicate) p1).getAdornment();
				final Adornment[] a2 = ((IAdornedPredicate) p2).getAdornment();
				for (int i = 0; i < a1.length; i++) {
					if ((a1[i] != a2[i]) && (a1[i] == Adornment.BOUND)) {
						return -1;
					} else if ((a1[i] != a2[i]) && (a1[i] == Adornment.FREE)) {
						return 1;
					}
				}
			}
			return 0;
		}

	}

}
