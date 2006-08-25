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
package org.deri.iris.evaluation.magic;

import static org.deri.iris.factory.Factory.BASIC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.evaluation.common.AdornedProgram;
import org.deri.iris.evaluation.common.Adornment;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedRule;

/**
 * <p>
 * Simple implementation of the &quot;Generalized Magic Sets&quot; according to
 * the &quot;The Power of Magic&quot; paper.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author richi
 * @version $Revision$
 */
public class MagicSetImpl {

	/** The prefix for the magic predicates. */
	private static final String MAGIC_PREDICATE_PREFIX = "magic_";

	/** The prefix for the labeled predicates. */
	private static final String MAGIC_LABEL_PREFIX = "label_";

	/** Holds all magic rules. */
	private Set<IRule> magicRules = new HashSet<IRule>();

	/** Holds all rewritten rules. */
	private Set<IRule> rewrittenRules = new HashSet<IRule>();

	/** The seed for this magic set. */
	private IQuery seed;

	/**
	 * Construcs a MagicSet using the submitted programm.
	 * 
	 * @param program
	 *            the adorned program to construct the magics
	 * @throws NullPointerException
	 *             if the program is null
	 * @throws IllegalArgumentException
	 *             if one of the rule got a head with length unequal 1
	 */
	public MagicSetImpl(final AdornedProgram program) {
		if (program == null) {
			throw new NullPointerException();
		}

		// TODO: two arcs entering an literal

		// TODO: maybe a defensive copy should be made

		for (AdornedRule r : program.getAdornedRules()) {
			if (r.getHeadLenght() != 1) {
				throw new IllegalArgumentException("At the moment only heads "
						+ "with length of 1 are allowed");
			}
			final List<ILiteral> sortedBody =
					new ArrayList<ILiteral>(r.getBodyLiterals());
			Collections.sort(sortedBody, r.getSIP().LITERAL_COMP);

			final List<ILiteral> rewrittenBody =
					new ArrayList<ILiteral>(sortedBody);
			rewrittenBody.add(0, createMagicLiteral(r.getHeadLiteral(0)));

			for (ILiteral l : r.getBodyLiterals()) {
				if (l.getPredicate() instanceof AdornedPredicate) {
					// creating a magic rule for the literal
					final IRule magicRule =
							createMagicRule(l, r, Collections
									.unmodifiableList(sortedBody));
					magicRules.add(magicRule);
				}
			}

			// adding the rewritten rule
			rewrittenRules.add(BASIC.createRule(BASIC.createHead(r
					.getHeadLiterals()), BASIC.createBody(rewrittenBody)));

			// creating the seed
			seed = createSeed(program.getQuery());
		}
	}

	/**
	 * Creates a seed for the given query. </br></br>The predicate of the
	 * literal will be adorned according to it's bound and free terms and the
	 * terms of the literal of the query will be limited to it's bound terms.
	 * 
	 * @param q
	 *            for which to create the seed
	 * @return the seed
	 * @throws NullPointerException
	 *             if the query is null
	 * @throws IllegalArgumentException
	 *             if the length of the query is unequal 1.
	 */
	private static IQuery createSeed(final IQuery q) {
		if (q == null) {
			throw new NullPointerException();
		}
		if (q.getQueryLenght() != 1) {
			throw new IllegalArgumentException(
					"At the moment only queries with length 1 are allowed");
		}

		final ILiteral queryLiteral = q.getQueryLiteral(0);
		final ILiteral adornedLiteral =
				BASIC.createLiteral(queryLiteral.isPositive(),
						new AdornedPredicate(queryLiteral), queryLiteral
								.getTuple());

		return BASIC.createQuery(createMagicLiteral(adornedLiteral));
	}

	/**
	 * Creates a magic rule for the given literal.
	 * 
	 * @param l
	 *            for which to create the rule
	 * @param rule
	 *            the original rule
	 * @param sortedBody
	 *            body literals of the rule sorted according to the sip of the
	 *            rule
	 * @return the magic rule
	 * @throws NullPointerException
	 *             if the literal, rule or the sorted body is null
	 * @throws IllegalArgumentException
	 *             if the predicate of the literal isn't adorned
	 * @throws IllegalArgumentException
	 *             if sorted body contains null
	 * @throws IllegalArgumentException
	 *             if the length of the head of the rule is unequal 1
	 */
	private static IRule createMagicRule(final ILiteral l, final IRule rule,
			final List<ILiteral> sortedBody) {
		if ((l == null) || (rule == null) || (sortedBody == null)) {
			throw new NullPointerException();
		}
		if (!(l.getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the literal must be adorned");
		}
		if (sortedBody.contains(null)) {
			throw new IllegalArgumentException(
					"The sortedBody must not contain null");
		}
		if (rule.getHeadLenght() != 1) {
			throw new IllegalArgumentException(
					"At the moment only heads with length 1 are allowed");
		}

		final ILiteral headLiteral = rule.getHeadLiteral(0);

		// create head of the rule
		final IHead head = BASIC.createHead(createMagicLiteral(l));

		// create the body of the rule
		final List<ILiteral> bodyLiterals =
				new ArrayList<ILiteral>(sortedBody.size());

		bodyLiterals.add(createMagicLiteral(headLiteral));

		for (int counter = 0, index = sortedBody.indexOf(l); counter < index; counter++) {
			bodyLiterals.add(sortedBody.get(counter));
		}
		final IBody body = BASIC.createBody(bodyLiterals);

		return BASIC.createRule(head, body);
	}

	/**
	 * Creates a magic literal out of an adorned one. The predicate of the
	 * literal must be adorned. The terms of the literal will only consist of
	 * the bound terms.
	 * 
	 * @param l
	 *            for which to create the adorned one
	 * @return the magic literal
	 * @throws NullPointerException
	 *             if the literal is null
	 * @throws IllegalArgumentException
	 *             if the predicate of the literal isn't adorned
	 */
	private static ILiteral createMagicLiteral(final ILiteral l) {
		if (l == null) {
			throw new NullPointerException("The literal must not be null");
		}
		if (!(l.getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the literal must be adorned");
		}

		final AdornedPredicate p = (AdornedPredicate) l.getPredicate();

		final ITuple boundTuple = BASIC.createTuple(getBounds(p, l));
		final AdornedPredicate magicPredicate =
				new AdornedPredicate(MAGIC_PREDICATE_PREFIX
						+ p.getPredicateSymbol(), Collections.frequency(Arrays
						.asList(p.getAdornment()), Adornment.BOUND), p
						.getAdornment());
		return BASIC.createLiteral(l.isPositive(), magicPredicate, boundTuple);
	}

	/**
	 * Returns the list of bound terms of the literal according to the bounds of
	 * the adorned predicate. The order of the terms won't be changed.
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
			final ILiteral l) {
		if ((p == null) || (l == null)) {
			throw new NullPointerException(
					"The adorned predicate or the literal must not be null");
		}
		if (!p.hasSameSignature(l.getPredicate())) {
			throw new IllegalArgumentException(
					"The signatures of the headliteral and the adorned "
							+ "predicate doesn't match");
		}

		final List<ITerm> bounds =
				new ArrayList<ITerm>(p.getAdornment().length);
		final Iterator<ITerm> terms = l.getTuple().getTerms().iterator();
		for (Adornment a : p.getAdornment()) {
			final ITerm t = terms.next();
			if (a == Adornment.BOUND) {
				bounds.add(t);
			}
		}
		return bounds;
	}

	/**
	 * Returns the set of generated magic rules.
	 * 
	 * @return the unmodifiable set of rules
	 */
	public Set<IRule> getMagicRules() {
		return Collections.unmodifiableSet(magicRules);
	}

	/**
	 * Returns the set of rewritten rules.
	 * 
	 * @return the unmodifiable set of rules
	 */
	public Set<IRule> getRewrittenRules() {
		return Collections.unmodifiableSet(rewrittenRules);
	}

	/**
	 * Returns the seed of this magic set.
	 * 
	 * @return the seed
	 */
	public IQuery getSeed() {
		return seed;
	}

	public String toString() {
		final String NEWLINE = System.getProperty("line.separator");
		StringBuilder buffer = new StringBuilder();
		buffer.append(seed).append(NEWLINE);
		for (IRule r : magicRules) {
			buffer.append(r).append(NEWLINE);
		}
		buffer.append(NEWLINE);
		for (IRule r : rewrittenRules) {
			buffer.append(r).append(NEWLINE);
		}
		return buffer.toString();
	}
}
