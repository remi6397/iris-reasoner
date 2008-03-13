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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.IProgramOptimisation;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

import org.deri.iris.graph.LabeledEdge;
import org.deri.iris.optimisations.magicsets.AdornedProgram;
import org.deri.iris.optimisations.magicsets.AdornedProgram.AdornedPredicate;
import org.deri.iris.optimisations.magicsets.AdornedProgram.AdornedRule;
import org.deri.iris.optimisations.magicsets.Adornment;

/**
 * <p>
 * Simple implementation of the &quot;Generalized Magic Sets&quot; according to
 * the &quot;The Power of Magic&quot; paper.
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri sti2 at)
 */
public final class MagicSets implements IProgramOptimisation {

	/** The prefix for the magic predicates. */
	static final String MAGIC_PREDICATE_PREFIX = "magic";

	/** The prefix for the labeled predicates. */
	static final String MAGIC_LABEL_PREFIX = "label";

	/** An empty list that getBounds() can return. */
	private static final List<ITerm> EMPTY_TERM_LIST = new ArrayList<ITerm>();

	public Result optimise(final Collection<IRule> rules, final IQuery query) {
		if (rules == null) {
			throw new IllegalArgumentException("The rules must not be null");
		}
		if (query == null) {
			throw new IllegalArgumentException("The query must not be null");
		}

		// check, whether the query contains constants
		int constants = 0;
		for (final ILiteral l : query.getLiterals()) {
			final IAtom a = l.getAtom();
			if (!a.isBuiltin()) { // we count only constants in ordinary literals
				for (final ITerm t : a.getTuple()) {
					if (t.isGround()) {
						constants++;
					}
				}
			}
		}
		// if there aren't any constants -> return null
		if (constants == 0) {
			return null;
		}

		final Result result = new Result();
		final AdornedProgram adornedProg = new AdornedProgram(rules, query);

		// setting the query
		result.query = unadornQuery(adornedProg.getQuery());

		// setting the rules
		result.rules = new ArrayList<IRule>();
		for (final AdornedRule r : adornedProg.getAdornedRules()) {
			if (r.getRule().getHead().size() != 1) {
				throw new IllegalArgumentException("At the moment only heads "
						+ "with length of 1 are allowed");
			}

			for (final ILiteral l : r.getRule().getBody()) {
				if (l.getAtom().getPredicate() instanceof AdornedPredicate) {
					// creating a magic rule for the literal
					result.rules.addAll(generateRules(l, r));
				}
			}
			// adding the rewritten rule
			result.rules.add(getRewrittenRule(r));
		}

		// adding the remaining rules
		result.rules.addAll(filterRemainingRules(adornedProg.getNormalRules(),
						adornedProg.getAdornedRules()));
		// adding the rules for the conjunctive query
		result.rules.addAll(createConjunctiveRules(adornedProg.getQuery()));
		// adding the rule for the seed
		final IAtom seed = createSeed(result.query);
		// construct the seed rule
		if (seed != null) {
			result.rules.add(BASIC.createRule(Arrays.asList(BASIC.createLiteral(true, seed)),
						Collections.EMPTY_LIST));
		}

		// unadorn the rules
		result.rules = unadornRules(result.rules);

		return result;
	}

	/**
	 * Computes the magic rules needed to evaluate conjunctive queries.
	 * @param q the query
	 * @return the computed rules
	 */
	private static Set<IRule> createConjunctiveRules(final IQuery q) {
		assert q != null: "The query must not be null";

		final Set<IRule> res = new HashSet<IRule>();
		final List<ILiteral> query = q.getLiterals();
		for (int i = 1, max = query.size(); i < max; i++) {
			final IAtom magicAtom = createBoundAtom(query.get(i).getAtom(), null, MAGIC_PREDICATE_PREFIX, null);
			if (!magicAtom.getTuple().isEmpty()) {
				res.add(BASIC.createRule(Arrays.asList(BASIC.createLiteral(true, magicAtom)),
							slice(query, 0, i)));
			}
		}
		return res;
	}

	/**
	 * Returns a sublist of a list. Using a slice like perl would be too
	 * simple...
	 * @param orig the original list
	 * @param from from where to copy the elements (inclusive)
	 * @param to to which element to copy (exclusive)
	 * @return the sublist
	 */
	private static <Type> List<Type> slice(final List<Type> orig, final int from, final int to) {
		assert orig != null: "The list must not be null";

		// adjust the input variables for the origianl list size
		final int nfrom = (from < 0) ? 0 : from;
		final int nto = ((to < 0) || (to > orig.size())) ? orig.size() : to;

		assert (nto > nfrom): "nfrom (" + nfrom + ") must be smaller than nto(" + nto + ")";

		// copy the range
		final List<Type> res = new ArrayList<Type>(nto - nfrom);
		for (int i = nfrom; i < nto; i++) {
			res.add(orig.get(i));
		}
		return res;
	}

	/**
	 * <p>
	 * Creates a rewritten rule for the adorned one. This method simply
	 * rewrites a rule my adding the "magic_" literal at the beginning of
	 * the rule.
	 * </p>
	 * 
	 * @param r the rule which to rewrite
	 * @return the rewritten rule
	 */
	private static IRule getRewrittenRule(final AdornedRule r) {
		assert r != null: "The rule must not be null";
		assert r.getRule().getHead().size() == 1: 
			"The head must have a length of 1, but was " + r.getRule().getHead().size();

		final ILiteral headL = r.getRule().getHead().get(0);

		// computing the rewritten body
		final List<ILiteral> rewrittenBody = new ArrayList<ILiteral>(r.getRule().getBody());
		Collections.sort(rewrittenBody, r.getSip().getLiteralComparator());

		final ILiteral magicL = createMagicLiteral(headL);
		if (magicL == headL) { // the head literal is not adorned 
			// -> the query was not adorned -> nothing to exchange
			return r.getRule();
		}
		if (magicL.getAtom().getTuple().isEmpty()) { // the literal wouldn't produce 
			// any bindings, so it is better to leave it out (since
			// with the new sip we wouldn't have the rules for this
			// literal constructed anyway
			return r.getRule();
		}
		rewrittenBody.add(0, magicL);

		return BASIC.createRule(r.getRule().getHead(), rewrittenBody);
	}

	/**
	 * <p>
	 * Creates a seed for the given query. 
	 * </p>
	 * <p>
	 * The predicate of the literal will be adorned according to it's bound and 
	 * free terms and the terms of the literal of the query will be limited to 
	 * it's bound terms.
	 * </p>
	 * 
	 * @param q for which to create the seed
	 * @return the seed or <code>null</code> if the query didn't contain any constants
	 */
	private static IAtom createSeed(final IQuery q) {
		assert q != null: "The query must not be null";

		return (q.getLiterals().isEmpty())
			? null
			: createBoundAtom(q.getLiterals().get(0).getAtom(), null, MAGIC_PREDICATE_PREFIX, null);
	}

	/**
	 * Creates a magic rule for the given literal. It takes into account, how
	 * many arcs are entering the specific literal and constructs the
	 * corresponding rules.
	 * 
	 * @param l literal for which to create the rule
	 * @param r the original rule containing the given literal
	 * @return the set of generated rules
	 */
	private static Set<IRule> generateRules(final ILiteral l, final AdornedRule r) {
		assert l != null: "The literal must not be null";
		assert r != null: "The rule must not be null";
		assert l.getAtom().getPredicate() instanceof AdornedPredicate: 
			"The predicate of the literal must be adorned";
		assert r.getRule().getHead().size() == 1: 
			"The head must have a size of 1, but was " + r.getRule().getHead().size();

		final Set<LabeledEdge<ILiteral, Set<IVariable>>> enteringEdges = 
			r.getSip().getEdgesEnteringLiteral(l);

		if (enteringEdges.size() == 1) {
			// only on arch is entering this literal
			return Collections.singleton(createMagicRule(enteringEdges.iterator().next(), r));
		} else if (enteringEdges.size() > 1) {
			// multible arcs entering this literal
			final Set<IRule> rules = new HashSet<IRule>(enteringEdges.size() + 1);
			// creating the labeled rules
			int counter = 1;
			for (final LabeledEdge<ILiteral, Set<IVariable>> e : enteringEdges) {
				rules.add(createLabeledRule(e, r, counter++));
			}
			// computing the body for the magic rule
			final Set<ILiteral> bodyLiterals = new HashSet<ILiteral>(rules.size());
			for (final IRule rule : rules) {
				bodyLiterals.add(rule.getHead().get(0));
			}
			final ILiteral hl = createMagicLiteral(true, l);
			rules.add(BASIC.createRule(Arrays.asList(hl),
						new ArrayList<ILiteral>(bodyLiterals)));
			return rules;
		}
		// there are no edges entering this literal -> all would be free
		return Collections.EMPTY_SET;
	}

	/**
	 * Creates a magic rule for the given literal.
	 * 
	 * @param l for which to create the rule
	 * @param rule the original rule
	 * @return the magic rule
	 */
	private static IRule createMagicRule(final LabeledEdge<ILiteral, Set<IVariable>> e,
			final AdornedRule r) {
		assert e != null: "The edge must not be null";
		assert r != null: "The rule must not be null";

		return BASIC.createRule(Arrays.asList(createMagicLiteral(true, e.getTarget())),
				createRestrictedBody(e.getSource(), r));
	}

	/**
	 * Creates a labeled rule for the given literal.
	 * 
	 * @param e the edge to the literal
	 * @param r the adorned rule which contain the literal
	 * @param index the index of this labeled rule
	 * @return the labeled rule
	 */
	private static IRule createLabeledRule(final LabeledEdge<ILiteral, Set<IVariable>> e,
			final AdornedRule r,
			final int index) {
		assert e != null: "The edge must not be null";
		assert r != null: "The rule must not be null";
		assert index > 0: "The index must be greater than 0";

		return BASIC.createRule(Arrays.asList(createLabeledLiteral(true,
						e.getTarget(),
						e.getLabel(),
						index)),
				createRestrictedBody(e.getSource(), r));
	}

	/**
	 * Creates the body of a magic rule. This method is intended to operate
	 * with the source of a edge entering the literal for which you want to
	 * create the magic rule's body.
	 * @param passer the literal passing the variables
	 * @param rule the rule from where to take the passings
	 * @return the list of body literals for the magic rule
	 */
	private static List<ILiteral> createRestrictedBody(final ILiteral passer, final AdornedRule rule) {
		assert passer != null: "The passing literal must not be null";
		assert rule != null: "The rule must not be null";

		final Set<ILiteral> passers = rule.getSip().getDepends(passer);
		passers.add(passer);
		final ILiteral head = rule.getRule().getHead().get(0);

		// add all passings from the body of the rule
		final List<ILiteral> body = new ArrayList<ILiteral>(passers);
		body.remove(head);
		Collections.sort(body, rule.getSip().getLiteralComparator());

		// if there is a variable passing from the head of the rule, get
		// them from it's magic relation
		if (passers.contains(head)) {
			body.add(0, createMagicLiteral(head));
		}
		return body;
	}

	/**
	 * Constructs a new atom containing only it's bound terms. If bound
	 * variables are given or the predicate of the atom is not adorned the
	 * bound terms of the atom will be determined according to it's ground
	 * terms and given bound variables. Otherwise the adornments of the
	 * predicate are taken.
	 * @param a the atom from which to create the adorned version
	 * @param bound the bound variables (might be <code>null</code>)
	 * @param prefix the prefix for the predicate symbol
	 * @param suffix the suffix for the predicate symbol
	 * @return the newly created atom
	 */
	private static IAtom createBoundAtom(final IAtom a, final Collection<IVariable> bound,
			final String prefix, final String suffix) {
		assert a != null: "The atom must not be null";

		// getting the adorned predicate
		final AdornedPredicate ap = ((bound != null) || !(a.getPredicate() instanceof AdornedPredicate))
			? new AdornedPredicate(a, bound)
			: (AdornedPredicate) a.getPredicate();

		// constructing the tuple

		final ITuple boundTuple = BASIC.createTuple(getBounds(ap, a));

		// constructing the new predicate symbol
		final StringBuilder newPredicateSymbol = new StringBuilder();

		if ((prefix != null) && prefix.length() > 0) {
			newPredicateSymbol.append(prefix).append("_");
		}

		newPredicateSymbol.append(ap.getPredicateSymbol()).append("_");

		if ((suffix != null) && (suffix.length() > 0)) {
			newPredicateSymbol.append(suffix).append("_");
		}

		for (final Adornment ad : ap.getAdornment()) {
			newPredicateSymbol.append(ad);
		}

		// constructing the new predicate
		final IPredicate newPredicate = BASIC.createPredicate(newPredicateSymbol.toString(),
				Collections.frequency(Arrays.asList(ap.getAdornment()), Adornment.BOUND));

		return BASIC.createAtom(newPredicate, boundTuple);
	}

	/**
	 * <p>
	 * Creates a magic literal out of an adorned one. The predicate of the
	 * literal must be adorned. The terms of the literal will only consist of
	 * the bound terms.
	 * </p>
	 * <p>
	 * Note that not adorned literals are taken as if they would have only
	 * bounds.
	 * </p>
	 * 
	 * @param l for which to create the adorned one
	 * @return the magic literal or the same literal again, if the predicate
	 * of the literal wasn't adorned
	 */
	private static ILiteral createMagicLiteral(final ILiteral l) {
		return createMagicLiteral(l.isPositive(), l);
	}

	/**
	 * <p>
	 * Creates a magic literal out of an adorned one. The predicate of the
	 * literal must be adorned. The terms of the literal will only consist of
	 * the bound terms.
	 * </p>
	 * <p>
	 * Note that not adorned literals are taken as if they would have only
	 * bounds.
	 * </p>
	 * 
	 * @param positive whether the resulting literal should be positive, or
	 * not
	 * @param l for which to create the adorned one
	 * @return the magic literal or the same literal again, if the predicate
	 * of the literal wasn't adorned
	 */
	private static ILiteral createMagicLiteral(final boolean positive, final ILiteral l) {
		assert l != null: "The literal must not be null";

		// if the literal isn't adorned then there isn't anything to do.
		if (!(l.getAtom().getPredicate() instanceof AdornedPredicate)) {
			return l;
		}
		return BASIC.createLiteral(positive,
				createBoundAtom(l.getAtom(), null, MAGIC_PREDICATE_PREFIX, null));
	}

	/**
	 * Creates a labeled literal out of an adorned one. The predicate of the
	 * literal must be adorned. The terms of the literal will only consist of
	 * the bound terms.
	 * 
	 * @param positive <code>true</code> the resulting literal should be 
	 * positive, otherwise <code>false</code>
	 * @param l for which to create the labeled literal
	 * @param passings the variables passed by this edge
	 * @param index to append to the literal
	 * @return the labeled literal
	 */
	private static ILiteral createLabeledLiteral(final boolean positive, 
			final ILiteral l, final Set<IVariable> passings, final int index) {
		assert l != null: "The literal must not be null";
		assert passings != null: "The passings must not be null";
		assert index >= 0 : "The index must not be negative";

		return BASIC.createLiteral(positive,
				createBoundAtom(l.getAtom(), passings, MAGIC_LABEL_PREFIX, Integer.toString(index)));
	}

	/**
	 * Returns the list of bound terms of the literal according to the bounds of
	 * the adorned predicate of the literal. The order of the terms won't be
	 * changed.
	 * 
	 * @param l containing all the terms
	 * @return the list of bound terms
	 */
	private static List<ITerm> getBounds(final ILiteral l) {
		assert l != null: "The literal must not be null";

		if (!(l.getAtom().getPredicate() instanceof AdornedPredicate)) {
			return EMPTY_TERM_LIST;
		}
		return getBounds((AdornedPredicate) l.getAtom().getPredicate(), l.getAtom());
	}

	/**
	 * Returns the list of bound terms of the literal according to the bounds of
	 * the adornment of the predicate. The order of the terms won't be changed.
	 * 
	 * @param p where to take the adornments from
	 * @param l containing all the terms
	 * @return the list of bound terms
	 */
	private static List<ITerm> getBounds(final AdornedPredicate p,
			final IAtom a) {
		assert p != null: "The predicate must not be null";
		assert a != null: "The atom must not be null";
		assert p.hasSameSignature(a.getPredicate()):
			"The signature of the predicate and the predicate of the atom must match";

		final List<ITerm> bounds = new ArrayList<ITerm>(p.getAdornment().length);
		final Iterator<ITerm> terms = a.getTuple().iterator();
		for (Adornment ad : p.getAdornment()) {
			final ITerm t = terms.next();
			if (ad == Adornment.BOUND) {
				bounds.add(t);
			}
		}
		return bounds;
	}

	/**
	 * Filters the given normal (unadorned ones) and removes every occurrence
	 * where an adorned one (with or without a guardian literal) exist.
	 * 
	 * @param nr
	 *            the normal rules to filter
	 * @param ar
	 *            the adorned rules
	 * @return a set of filtered rules
	 */
	private static Set<IRule> filterRemainingRules(final Collection<IRule> nr,
			final Collection<AdornedRule> ar) {
		assert nr != null: "The normal rules must not be null";
		assert ar != null: "The adorned rules must not be null";

		final Set<IRule> rem = new HashSet<IRule>();
		for (final IRule n : nr) {
			boolean add = true;
			for (final AdornedRule a : ar) {
				if (isSameRule(n, a.getRule())) {
					add = false;
					break;
				}
			}
			if (add) {
				rem.add(n);
			}
		}
		return rem;
	}

	/**
	 * Checks two rules whether they might be the same. If one rule is the
	 * adorned (with or without a guardian literal) version of the other the
	 * method will return {@code true}.
	 * 
	 * @param r0 the first rule to compare
	 * @param r1 the second rule to compare
	 * @return {@code true} if the rules express the same
	 */
	private static boolean isSameRule(final IRule r0, final IRule r1) {
		assert r0 != null: "The first rule must not be null";
		assert r1 != null: "The second rule must not be null";

		// comparing the head literals
		final Iterator<ILiteral> h0 = r0.getHead().iterator();
		final Iterator<ILiteral> h1 = r1.getHead().iterator();
		while (h0.hasNext() && h1.hasNext()) {
			if (!isSameLiteral(h0.next(), h1.next())) {
				return false;
			}
		}
		if (h0.hasNext() || h1.hasNext()) {
			return false;
		}

		// comparing the body literals
		final Iterator<ILiteral> b0 = r0.getBody().iterator();
		final Iterator<ILiteral> b1 = r1.getBody().iterator();
		while (b0.hasNext() && b1.hasNext()) {
			ILiteral l0 = b0.next();
			while (l0.getAtom().getPredicate().getPredicateSymbol().startsWith(
					MAGIC_PREDICATE_PREFIX)
					&& b0.hasNext()) {
				l0 = b0.next();
			}
			ILiteral l1 = b1.next();
			while (l1.getAtom().getPredicate().getPredicateSymbol().startsWith(
					MAGIC_PREDICATE_PREFIX)
					&& b1.hasNext()) {
				l1 = b1.next();
			}
			if (!isSameLiteral(l0, l1)) {
				return false;
			}
		}
		if (b0.hasNext() || b1.hasNext()) {
			return false;
		}
		return true;
	}

	/**
	 * Checks whether two literals are the same. The possible adornments won't
	 * be taken into account for equality.
	 * 
	 * @param l0 the first literal to compare
	 * @param l1 the second literal to compare
	 * @return {@code true} if the literals are the same
	 */
	private static boolean isSameLiteral(final ILiteral l0, final ILiteral l1) {
		assert l0 != null: "The first literal must not be null";
		assert l1 != null: "The second literal must not be null";

		if (!isSamePredicate(l0.getAtom().getPredicate(), l1.getAtom().getPredicate())) {
			return false;
		}
		// compare the terms
		return l0.getAtom().getTuple().equals(l1.getAtom().getTuple());
	}

	/**
	 * Checks whether two predicates are the same only according to their
	 * symbol and arity.
	 * @param p0 the first predicate to check
	 * @param p1 the second predicate to check
	 * @return <code>true</code> if the symbol and the arity matches,
	 * otherswise <code>false</code>
	 */
	private static boolean isSamePredicate(final IPredicate p0, final IPredicate p1) {
		assert p0 != null: "The first predicate must not be null";
		assert p1 != null: "The second predicate must not be null";

		return p0.getPredicateSymbol().equals(p1.getPredicateSymbol()) && (p0.getArity() == p1.getArity());
	}

	/**
	 * Converts a list of rules containing adorned predicates to rules
	 * consisting only of basic iris objects.
	 * @param rules the rules to unadorn
	 * @return the unadorned rules
	 * @see #unadornPredicate(IPredicate)
	 */
	private static List<IRule>unadornRules(final Collection<IRule> rules) {
		assert rules != null: "The rules must not be null";

		final List<IRule> result = new ArrayList<IRule>(rules.size());
		for (final IRule r : rules) {
			result.add(BASIC.createRule(unadornLiterals(r.getHead()),
						unadornLiterals(r.getBody())));
		}
		return result;
	}

	/**
	 * Converts a query containing adorned predicates to a query
	 * consisting only of basic iris objects.
	 * @param q the query to unadorn
	 * @return the unadorned query
	 * @see #unadornPredicate(IPredicate)
	 */
	private static IQuery unadornQuery(final IQuery q) {
		assert q != null: "The rule must not be null";

		return BASIC.createQuery(unadornLiterals(q.getLiterals()));
	}

	/**
	 * Converts a list of literals containing adorned predicates to a
	 * list of literals consisting only of basic iris objects.
	 * @param lits the literals to unadorn
	 * @return the unadorned literals
	 * @see #unadornPredicate(IPredicate)
	 */
	private static List<ILiteral> unadornLiterals(final Collection<ILiteral> lits) {
		assert lits != null: "The literals must not be null";

		final List<ILiteral> result = new ArrayList<ILiteral>(lits.size());
		for (final ILiteral l : lits) {
			result.add(BASIC.createLiteral(l.isPositive(),
					unadornPredicate(l.getAtom().getPredicate()),
					l.getAtom().getTuple()));
		}
		return result;
	}

	/**
	 * Converts a adorned predicate to a basic one.
	 * @param p the predicate to unadorn
	 * @return the unadorned predicate
	 */
	private static IPredicate unadornPredicate(final IPredicate p) {
		assert p != null: "The predicate must not be null";

		if (p instanceof AdornedPredicate) {
			return BASIC.createPredicate(p.getPredicateSymbol(), p.getArity());
		}
		return p;
	}
}
