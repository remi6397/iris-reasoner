/*
 * Integrated Rule Inference System (IRIS): An extensible rule inference system
 * for datalog with extensions by built-in predicates, default negation (under
 * well-founded semantics), function symbols and contexts. Copyright (C) 2006
 * Digital Enterprise Research Institute (DERI), Leopold-Franzens-Universitaet
 * Innsbruck, Technikerstrasse 21a, A-6020 Innsbruck. Austria. This library is
 * free software; you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option) any later
 * version. This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */
package org.deri.iris.optimisations;

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
import org.deri.iris.optimisations.AdornedProgram;
import org.deri.iris.optimisations.AdornedProgram.AdornedPredicate;
import org.deri.iris.optimisations.AdornedProgram.AdornedRule;
import org.deri.iris.optimisations.Adornment;
import org.deri.iris.optimisations.ISip;
import org.deri.iris.optimisations.SIPImpl;

/**
 * <p>
 * Simple implementation of the &quot;Generalized Magic Sets&quot; according to
 * the &quot;The Power of Magic&quot; paper.
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public final class MagicSetImpl implements IProgramOptimisation {

	/** The prefix for the magic predicates. */
	static final String MAGIC_PREDICATE_PREFIX = "magic_";

	/** The prefix for the labeled predicates. */
	static final String MAGIC_LABEL_PREFIX = "label_";

	/** An empty list that getBounds() can return. */
	private static final List<ITerm> EMPTY_TERM_LIST = new ArrayList<ITerm>();

	public Result optimise(final Collection<IRule> rules, final IQuery query) {
		if (rules == null) {
			throw new IllegalArgumentException("The rules must not be null");
		}
		if (query == null) {
			throw new IllegalArgumentException("The query must not be null");
		}

		final Result result = new Result();
		final AdornedProgram adornedProg = new AdornedProgram(rules, query);

		// setting the query
		result.query = adornedProg.getQuery();

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
		final IAtom seed = createSeed(adornedProg.getQuery());
		// construct the seed rule
		if (seed != null) {
			result.rules.add(BASIC.createRule(
						Arrays.asList(BASIC.createLiteral(true, seed)), 
						Collections.EMPTY_LIST));
		}

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
			final IAtom magicAtom = createMagicAtom(query.get(i).getAtom());
			if (!magicAtom.getTuple().isEmpty()) {
				res.add(BASIC.createRule(Arrays.asList(
								BASIC.createLiteral(query.get(i).isPositive(), magicAtom)), 
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
		final List<Type> res = new java.util.ArrayList<Type>(nto - nfrom);
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
	 * @param r
	 *            the rule which to rewrite
	 * @return the rewritten rule
	 * @throws NullPointerException
	 *             if the rule is null
	 * @throws IllegalArgumentException
	 *             if the length of the head is unequal to 1
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
	 * @param q
	 *            for which to create the seed
	 * @return the seed or <code>null</code> if the query didn't contain any constants
	 */
	private static IAtom createSeed(final IQuery q) {
		assert q != null: "The query must not be null";

		return (q.getLiterals().isEmpty()) ? 
			null : createMagicAtom(q.getLiterals().get(0).getAtom());
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
			return Collections.singleton(createMagicRule(l, r));
		} else if (enteringEdges.size() > 1) {
			// multible arcs entering this literal
			final Set<IRule> rules = new HashSet<IRule>(
					enteringEdges.size() + 1);
			// creating the labeled rules
			int counter = 1;
			for (final LabeledEdge<ILiteral, Set<IVariable>> e : enteringEdges) {
				rules.add(createLabeledRule(e, r, counter++));
			}
			// computing the body for the magic rule
			final Set<ILiteral> bodyLiterals = new HashSet<ILiteral>(rules
					.size());
			for (final IRule rule : rules) {
				bodyLiterals.add(rule.getHead().get(0));
			}
			final ILiteral hl = createMagicLiteral(true, l);
			rules.add(BASIC.createRule(Arrays.asList(new ILiteral[]{hl}),
					new ArrayList<ILiteral>(bodyLiterals)));
			return rules;
		}
		// there are no edges entering this literal -> all would be free
		return Collections.EMPTY_SET;
	}

	/**
	 * Creates a magic rule for the given literal.
	 * 
	 * @param l
	 *            for which to create the rule
	 * @param rule
	 *            the original rule
	 * @return the magic rule
	 * @throws NullPointerException
	 *             if the literal or the rule is null
	 * @throws IllegalArgumentException
	 *             if the predicate of the literal isn't adorned
	 * @throws IllegalArgumentException
	 *             if the length of the head of the rule is unequal 1
	 */
	private static IRule createMagicRule(final ILiteral l, final AdornedRule rule) {
		if ((l == null) || (rule == null)) {
			throw new NullPointerException(
					"The rule and the literal must not be null");
		}
		if (!(l.getAtom().getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the literal must be adorned");
		}
		if (rule.getRule().getHead().size() != 1) {
			throw new IllegalArgumentException(
					"At the moment only heads with length 1 are allowed");
		}

		// create head of the rule
		final ILiteral hl = createMagicLiteral(true, l);

		// create the body of the rule
		final List<ILiteral> body = new ArrayList<ILiteral>(rule.getSip().getDepends(l));
		Collections.sort(body, rule.getSip().getLiteralComparator());

		// correct the literals -> make adorned literals -> magic literals
		// if the head literal wasn't adorned (only happens if the query hasn't any constants
		// skip the exchange of the literals, because there isn't anything to exchage, and 
		// remove the first literal of the body (which is the headliteral)
		final ILiteral headLiteral = rule.getRule().getHead().get(0);
		if ((headLiteral.getAtom().getPredicate() instanceof AdornedPredicate)) {
			for (int i = 0, max = body.size(); i < max; i++) {
				if (body.get(i).equals(headLiteral)) {
					body.set(i, createMagicLiteral(body .get(i)));
					break;
				}
			}
		} else {
			body.remove(0);
		}

		return BASIC.createRule(Arrays.asList(new ILiteral[]{hl}), body);
	}

	/**
	 * Creates a labeled rule for the given literal.
	 * 
	 * @param e
	 *            the edge to the literal
	 * @param r
	 *            the adorned rule which contain the literal
	 * @param index
	 *            the index of this labeled rule
	 * @return the labeled rule
	 * @throws NullPointerException
	 *             if the rule or the edge is null
	 * @throws IllegalArgumentException
	 *             if the source or the target of the edge aren't literals
	 * @throws IllegalArgumentException
	 *             if the predicate of the source of the edge isn't adorned
	 * @throws IllegalArgumentException
	 *             if the index is smaller than 0
	 * @throws IllegalArgumentException
	 *             if the headlength of the rule is unequal to 1
	 */
	private static IRule createLabeledRule(
			final LabeledEdge<ILiteral, Set<IVariable>> e, final AdornedRule r,
			final int index) {
		if ((e == null) || (r == null)) {
			throw new NullPointerException(
					"The edge and the rule must not be null");
		}

		if (!(e.getSource() instanceof ILiteral)
				|| !(e.getTarget() instanceof ILiteral)) {
			throw new IllegalArgumentException(
					"The source and the target of the edge must be literals");
		}

		final ILiteral targetLiteral = (ILiteral) e.getTarget();
		final ILiteral sourceLiteral = (ILiteral) e.getSource();

		if (!(targetLiteral.getAtom().getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the literal must be adorned");
		}

		if (index < 0) {
			throw new IllegalArgumentException("The index must not be negative");
		}

		if (r.getRule().getHead().size() != 1) {
			throw new IllegalArgumentException(
					"At the moment only heads with length 1 are allowed");
		}

		final ILiteral headLiteral = r.getRule().getHead().get(0);

		// create head of the rule
		final ILiteral hl = createLabeledLiteral(true, targetLiteral, index);

		// create body of the rule
		final List<ILiteral> body = 
			new ArrayList<ILiteral>(r.getSip().getDepends((ILiteral) sourceLiteral));
		body.add(sourceLiteral);
		Collections.sort(body, r.getSip().getLiteralComparator());

		// correct the literals -> make adorned literals -> magic literals
		for (int counter = 0, size = body.size(); counter < size; counter++) {
			if (body.get(counter).equals(headLiteral)) {
				body.set(counter, createMagicLiteral(body.get(counter)));
				break;
			}
		}

		return BASIC.createRule(Arrays.asList(new ILiteral[]{hl}), body);
	}

	/**
	 * Creates a magic atom. The magic atom will be prefixed with
	 * &quot;magic_&quot; and only contain the bound terms according to it's
	 * adornments. If the predicate of the input atom is not adorned, it
	 * will be adorned according to it's ground terms.
	 * @param a the atom for which to create the magic atom
	 * @return the magic atom
	 */
	private static IAtom createMagicAtom(final IAtom a) {
		assert a != null: "The atom must not be null";

		final AdornedPredicate p = (a.getPredicate() instanceof AdornedPredicate) ? 
			(AdornedPredicate) a.getPredicate() :
			new AdornedPredicate(a);
		final ITuple boundTuple = BASIC.createTuple(getBounds(p, a));
		final AdornedPredicate magicPredicate = new AdornedPredicate(
				MAGIC_PREDICATE_PREFIX + p.getPredicateSymbol(), Collections
						.frequency(Arrays.asList(p.getAdornment()),
								Adornment.BOUND), p.getAdornment());
		return BASIC.createAtom(magicPredicate, boundTuple);
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
	 * @param l
	 *            for which to create the adorned one
	 * @return the magic literal or the same literal again, if the 
	 * 	predicate of the literal wasn't adorned
	 * @throws NullPointerException
	 *             if the literal is <code>null</code>
	 */
	private static ILiteral createMagicLiteral(final ILiteral l) {
		return (createMagicLiteral(l.isPositive(), l));
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
	 * @param l
	 *            for which to create the adorned one
	 * @return the magic literal or the same literal again, if the 
	 * 	predicate of the literal wasn't adorned
	 * @throws NullPointerException
	 *             if the literal is <code>null</code>
	 */
	private static ILiteral createMagicLiteral(final boolean positive, final ILiteral l) {
		if (l == null) {
			throw new NullPointerException("The literal must not be null");
		}
		// if the literal isn't adorned then there isn't anything to do.
		if (!(l.getAtom().getPredicate() instanceof AdornedPredicate)) {
			return l;
		}
		return BASIC.createLiteral(positive, createMagicAtom(l.getAtom()));
	}

	/**
	 * Creates a labeled literal out of an adorned one. The predicate of the
	 * literal must be adorned. The terms of the literal will only consist of
	 * the bound terms.
	 * 
	 * @param l
	 *            for which to create the labeled literal
	 * @param index
	 *            to append to the literal
	 * @return the labeled literal
	 * @throws NullPointerException
	 *             if the literal is null
	 * @throws IllegalArgumentException
	 *             if the predicate of the literal isn't adorned
	 * @throws IllegalArgumentException
	 *             if the index is smaller than 0
	 */
	private static ILiteral createLabeledLiteral(final ILiteral l,
			final int index) {
		return createLabeledLiteral(l.isPositive(), l, index);
	}

	/**
	 * Creates a labeled literal out of an adorned one. The predicate of the
	 * literal must be adorned. The terms of the literal will only consist of
	 * the bound terms.
	 * 
	 * @param positive <code>true</code> the resulting literal should be 
	 * positive, otherwise <code>false</code>
	 * @param l
	 *            for which to create the labeled literal
	 * @param index
	 *            to append to the literal
	 * @return the labeled literal
	 * @throws NullPointerException
	 *             if the literal is null
	 * @throws IllegalArgumentException
	 *             if the predicate of the literal isn't adorned
	 * @throws IllegalArgumentException
	 *             if the index is smaller than 0
	 */
	private static ILiteral createLabeledLiteral(final boolean positive, 
			final ILiteral l, final int index) {
		if (l == null) {
			throw new NullPointerException("The literal must not be null");
		}
		if (!(l.getAtom().getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the literal must be adorned");
		}
		if (index < 0) {
			throw new IllegalArgumentException(
					"The index must not be smaller than 0");
		}

		final AdornedPredicate p = (AdornedPredicate) l.getAtom().getPredicate();
		final ITuple boundTuple = BASIC.createTuple(getBounds(p, l.getAtom()));
		final AdornedPredicate labeledPredicatre = new AdornedPredicate(
				MAGIC_LABEL_PREFIX + p.getPredicateSymbol() + "_" + index,
				Collections.frequency(Arrays.asList(p.getAdornment()),
						Adornment.BOUND), p.getAdornment());
		return BASIC.createLiteral(l.isPositive(), labeledPredicatre,
				boundTuple);
	}

	/**
	 * Returns the list of bound terms of the literal according to the bounds of
	 * the adorned predicate of the literak. The order of the terms won't be
	 * changed.
	 * 
	 * @param l
	 *            containing all the terms
	 * @return the list of bound terms
	 * @throws NullPointerException
	 *             if the literal is null
	 * @throws IllegalArgumentException
	 *             if the predicate of the literal isn't adorned
	 * @throws IllegalArgumentException
	 *             if the signature of the adorned predicate doesn't match the
	 *             signature of the literal predicate
	 */
	static List<ITerm> getBounds(final ILiteral l) {
		if (l == null) {
			throw new NullPointerException("The literal must not be null");
		}
		if (!(l.getAtom().getPredicate() instanceof AdornedPredicate)) {
			return EMPTY_TERM_LIST;
		}
		return getBounds((AdornedPredicate) l.getAtom().getPredicate(), l.getAtom());
	}

	/**
	 * Returns the list of bound terms of the literal according to the bounds of
	 * the adornment of the predicate. The order of the terms won't be changed.
	 * 
	 * @param p
	 *            wher to take the adornments from
	 * @param l
	 *            containing all the terms
	 * @return the list of bound terms
	 * @throws NullPointerException
	 *             if the adroned predicate or the literal are null
	 * @throws IllegalArgumentException
	 *             if the signature of the adorned predicate doesn't match the
	 *             signature of the literal predicate
	 */
	 private static List<ITerm> getBounds(final AdornedPredicate p,
			final IAtom a) {
		if (p == null) {
			throw new NullPointerException("The adorned predicate must not be null");
		}
		if (a == null) {
			throw new NullPointerException("The atom must not be null");
		}
		if (!p.hasSameSignature(a.getPredicate())) {
			throw new IllegalArgumentException(
					"The signatures of the headliteral (" + a.getPredicate()
							+ ") and the adorned " + "predicate (" + p
							+ ") doesn't match");
		}

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
	 * Extracts all variables out of a collection of terms.
	 * 
	 * @param c
	 *            the collecion from where to extract the variables
	 * @return the extracted variables
	 * @throws NullPointerException
	 *             if the collection is {@code null}
	 */
	private static Set<IVariable> getVariables(final Collection<ITerm> c) {
		if (c == null) {
			throw new NullPointerException(
					"The collection of terms must not be null");
		}

		final Set<IVariable> v = new HashSet<IVariable>();
		for (ITerm t : c) {
			if (t instanceof IConstructedTerm) {
				v.addAll(((IConstructedTerm) t).getVariables());
			} else if (t instanceof IVariable) {
				v.add((IVariable) t);
			}
		}
		return v;
	}

	/**
	 * Filters the given normal (unadorned ones) and removes every occurency
	 * where an adorned one (with or without a guardian literal) exiest.
	 * 
	 * @param nr
	 *            the normal rules to filter
	 * @param ar
	 *            the adorned rules
	 * @return a set of filtered rules
	 * @throws NullPointerException
	 *             if one of the collections is {@code null}
	 */
	private static Set<IRule> filterRemainingRules(final Collection<IRule> nr,
			final Collection<AdornedRule> ar) {
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
	 * adorned (with or withoud a guardian literal) version of the other the
	 * method will return {@code true}.
	 * 
	 * @param r0
	 *            the first rule to compare
	 * @param r1
	 *            the second rule to compare
	 * @return {@code true} if the rules express the same
	 */
	private static boolean isSameRule(final IRule r0, final IRule r1) {
		if ((r0 == null) || (r1 == null)) {
			throw new NullPointerException("The rules must not be null");
		}

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
	 * @param l0
	 *            the first literal to compare
	 * @param l1
	 *            the second literal to compare
	 * @return {@code true} if the literals are the same
	 * @throws NullPointerException
	 *             a literal is {@code null}
	 */
	private static boolean isSameLiteral(final ILiteral l0, final ILiteral l1) {
		if ((l0 == null) || (l1 == null)) {
			throw new NullPointerException("The literals must not be null");
		}
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
	 * @throws NullPointerException if one of the predicates is
	 * <code>null</code>
	 */
	private static boolean isSamePredicate(final IPredicate p0, final IPredicate p1) {
		if ((p0 == null) || (p1 == null)) {
			throw new NullPointerException("The predicates must not be null");
		}
		return p0.getPredicateSymbol().equals(p1.getPredicateSymbol()) && (p0.getArity() == p1.getArity());
	}
}
