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

import static org.deri.iris.MiscHelper.createLiteral;
import static org.deri.iris.MiscHelper.createVarList;
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
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
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedRule;
import org.deri.iris.evaluation.magic.SIPImpl;

/**
 * <p>
 * Tests the adornments.
 * </p>
 * <p>
 * $Id: AdornmentsTest.java,v 1.1 2006-11-23 12:46:32 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.1 $
 * @date $Date: 2006-11-23 12:46:32 $
 */
public class AdornmentsTest extends TestCase {

	private IAdornedProgram p0;

	private IAdornedProgram p1;

	private static final Comparator<IAdornedRule> RC = new RuleComparator();

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
			assertEquals("The head literals don't match", r0.getHeadLiterals(),
					r1.getHeadLiterals());
			assertEquals("The body literals don't match", r0.getBodyLiterals(),
					r1.getBodyLiterals());
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
			assertEquals("The head literals don't match", r0.getHeadLiterals(),
					r1.getHeadLiterals());
			assertEquals("The body literals don't match", r0.getBodyLiterals(),
					r1.getBodyLiterals());
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
	private static IRule unadornRule(final IRule r) {
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
	 * Compares two rules according to their predicate symbols.
	 * </p>
	 * <p>
	 * $Id: AdornmentsTest.java,v 1.1 2006-11-23 12:46:32 richardpoettler Exp $
	 * </p>
	 * 
	 * @author richi
	 * @version $Revision: 1.1 $
	 * @date $Date: 2006-11-23 12:46:32 $
	 */
	private static class RuleComparator implements Comparator<IAdornedRule> {
		public int compare(IAdornedRule o1, IAdornedRule o2) {
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
