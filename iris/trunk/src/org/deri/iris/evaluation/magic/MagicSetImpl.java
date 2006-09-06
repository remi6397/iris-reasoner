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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.common.AdornedProgram;
import org.deri.iris.evaluation.common.Adornment;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedRule;
import org.deri.iris.graph.LabeledDirectedEdge;

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
 * @date $Date$
 */
public final class MagicSetImpl {

	/** The prefix for the magic predicates. */
	static final String MAGIC_PREDICATE_PREFIX = "magic_";

	/** The prefix for the labeled predicates. */
	static final String MAGIC_LABEL_PREFIX = "label_";

	/** Cache for all adorned sips. */
	private final Map<AdornedRule, SIPImpl> adornedSipCache = new HashMap<AdornedRule, SIPImpl>();

	/** Holds all magic rules. */
	private Set<IRule> magicRules = new HashSet<IRule>();

	/** Holds all rewritten rules. */
	private Set<AdornedRule> rewrittenRules = new HashSet<AdornedRule>();

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
			throw new NullPointerException("The program must not be null");
		}

		// TODO: maybe a defensive copy should be made

		for (AdornedRule r : program.getAdornedRules()) {
			if (r.getHeadLenght() != 1) {
				throw new IllegalArgumentException("At the moment only heads "
						+ "with length of 1 are allowed");
			}

			for (ILiteral l : r.getBodyLiterals()) {
				if (l.getPredicate() instanceof AdornedPredicate) {
					// creating a magic rule for the literal
					magicRules.addAll(generateRules(l, r));
				}
			}
			// adding the rewritten rule
			rewrittenRules.add(getRewrittenRule(r));
		}
		// creating the seed
		seed = createSeed(program.getQuery());
	}

	/**
	 * <p>
	 * Creates a rewritten rule for the adorned one.
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
	private AdornedRule getRewrittenRule(final AdornedRule r) {
		if (r == null) {
			throw new NullPointerException("The rule must not be null");
		}
		if (r.getHeadLenght() != 1) {
			throw new IllegalArgumentException("At the moment only heads "
					+ "with length of 1 are allowed");
		}

		final ILiteral headL = r.getHeadLiteral(0);

		// computing the sorted body
		final List<ILiteral> temp = new ArrayList<ILiteral>(r.getBodyLiterals());
		Collections.sort(temp, getAdornedSip(r).LITERAL_COMP);
		final List<ILiteral> sortedBody = Collections.unmodifiableList(temp);

		// computing the rewritten body
		final List<ILiteral> rewrittenBody = new ArrayList<ILiteral>(sortedBody);
		final ILiteral magicL = createMagicLiteral(headL);
		rewrittenBody.add(0, magicL);

		// modifying the sip
		final SIPImpl adornedSip = getAdornedSip(r).defensifeCopy();
		final Set<IVariable> boundVars = new HashSet<IVariable>();
		for (ITerm t : getBounds(headL)) {
			if (!(t instanceof IVariable)) {
				// TODO: what to do when it is a constant?
				// TODO: add this exception to the javadoc
				throw new IllegalArgumentException(
						"All bounds of the head must be variables");
			}
			boundVars.add((IVariable) t);
		}

		for (final LabeledDirectedEdge<Set<IVariable>> e : adornedSip
				.getEdgesLeavingLiteral(headL)) {
			adornedSip.removeEdge(e);
			adornedSip
					.updateSip(magicL, (ILiteral) e.getTarget(), e.getLabel());
		}
		adornedSip.updateSip(headL, magicL, boundVars);

		// creating the normal rule
		IRule tmpRule = BASIC.createRule(BASIC.createHead(r.getHeadLiterals()),
				BASIC.createBody(rewrittenBody));

		// creating the adorned rule
		return new AdornedRule(tmpRule, adornedSip);
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
		final ILiteral adornedLiteral = BASIC.createLiteral(queryLiteral
				.isPositive(), new AdornedPredicate(queryLiteral), queryLiteral
				.getTuple());

		return BASIC.createQuery(createMagicLiteral(adornedLiteral));
	}

	/**
	 * Creates a magic rule for the given literal. It takes into account, how
	 * many arcs are entering the specific literal and constructs the
	 * corresponding rules.
	 * 
	 * @param l
	 *            for which to create the rule
	 * @param r
	 *            the original rule containing the given literal
	 * @return the set of generated rules
	 * @throws NullPointerException
	 *             if the literal of the rule is null
	 * @throws IllegalArgumentException
	 *             if the predicate of the given literal isn't adorned
	 * @throws IllegalArgumentException
	 *             if the length of the head is unequal to 1
	 */
	private Set<IRule> generateRules(final ILiteral l, final AdornedRule r) {
		if ((l == null) || (r == null)) {
			throw new NullPointerException(
					"The rule and the literal must not be null");
		}
		if (!(l.getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the literal must be adorned");
		}
		if (r.getHeadLenght() != 1) {
			throw new IllegalArgumentException(
					"At the moment only heads with length 1 are allowed");
		}

		final Set<LabeledDirectedEdge<Set<IVariable>>> enteringEdges = getAdornedSip(
				r).getEdgesEnteringLiteral(l);

		if (enteringEdges.size() == 1) {
			// only on arch is entering this literal
			return Collections.singleton(createMagicRule(l, r));
		} else if (enteringEdges.size() > 1) {
			// multible arcs entering this literal
			final Set<IRule> rules = new HashSet<IRule>(
					enteringEdges.size() + 1);
			int counter = 1;
			for (final LabeledDirectedEdge<Set<IVariable>> e : enteringEdges) {
				rules.add(createLabeledRule(e, r, counter++));
			}
			final Set<ILiteral> bodyLiterals = new HashSet<ILiteral>(rules
					.size());
			for (final IRule rule : rules) {
				bodyLiterals.add(rule.getHeadLiteral(0));
			}
			rules.add(BASIC.createRule(BASIC.createHead(l), BASIC
					.createBody(new ArrayList<ILiteral>(bodyLiterals))));
			return rules;
		} else {
			throw new IllegalArgumentException(
					"There are no arcs entering this literal"
							+ ", so no magics can be created");
		}
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
	private IRule createMagicRule(final ILiteral l, final AdornedRule rule) {
		if ((l == null) || (rule == null)) {
			throw new NullPointerException(
					"The rule and the literal must not be null");
		}
		if (!(l.getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the literal must be adorned");
		}
		if (rule.getHeadLenght() != 1) {
			throw new IllegalArgumentException(
					"At the moment only heads with length 1 are allowed");
		}

		final ILiteral headLiteral = rule.getHeadLiteral(0);

		// create head of the rule
		final IHead head = BASIC.createHead(createMagicLiteral(l));

		// create the body of the rule
		SIPImpl adornedSip = getAdornedSip(rule);
		final List<ILiteral> bodyLiterals = new ArrayList<ILiteral>(adornedSip
				.getDepends(l));
		Collections.sort(bodyLiterals, adornedSip.LITERAL_COMP);

		// correct the literals -> make adorned literals -> magic literals
		for (int counter = 0, size = bodyLiterals.size(); counter < size; counter++) {
			if (bodyLiterals.get(counter).equals(headLiteral)) {
				bodyLiterals.set(counter, createMagicLiteral(bodyLiterals
						.get(counter)));
				break;
			}
		}

		final IBody body = BASIC.createBody(bodyLiterals);

		return BASIC.createRule(head, body);
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
	private IRule createLabeledRule(
			final LabeledDirectedEdge<Set<IVariable>> e, final AdornedRule r,
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

		if (!(targetLiteral.getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the literal must be adorned");
		}

		if (index < 0) {
			throw new IllegalArgumentException("");
		}

		if (r.getHeadLenght() != 1) {
			throw new IllegalArgumentException(
					"At the moment only heads with length 1 are allowed");
		}

		final ILiteral headLiteral = r.getHeadLiteral(0);

		// create head of the rule
		final IHead head = BASIC.createHead(createLabeledLiteral(targetLiteral,
				index));

		// create body of the rule
		final SIPImpl adornedSip = getAdornedSip(r);
		final List<ILiteral> bodyLiterals = new ArrayList<ILiteral>(adornedSip
				.getDepends((ILiteral) sourceLiteral));
		bodyLiterals.add(sourceLiteral);
		Collections.sort(bodyLiterals, adornedSip.LITERAL_COMP);

		// correct the literals -> make adorned literals -> magic literals
		for (int counter = 0, size = bodyLiterals.size(); counter < size; counter++) {
			if (bodyLiterals.get(counter).equals(headLiteral)) {
				bodyLiterals.set(counter, createMagicLiteral(bodyLiterals
						.get(counter)));
				break;
			}
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
		final AdornedPredicate magicPredicate = new AdornedPredicate(
				MAGIC_PREDICATE_PREFIX + p.getPredicateSymbol(), Collections
						.frequency(Arrays.asList(p.getAdornment()),
								Adornment.BOUND), p.getAdornment());
		return BASIC.createLiteral(l.isPositive(), magicPredicate, boundTuple);
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
		if (l == null) {
			throw new NullPointerException("The literal must not be null");
		}
		if (!(l.getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the literal must be adorned");
		}
		if (index < 0) {
			throw new IllegalArgumentException(
					"The index must not be smaller than 0");
		}

		final AdornedPredicate p = (AdornedPredicate) l.getPredicate();
		final ITuple boundTuple = BASIC.createTuple(getBounds(p, l));
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
	private static List<ITerm> getBounds(final ILiteral l) {
		if (l == null) {
			throw new NullPointerException("The literal must not be null");
		}
		if (!(l.getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the literal must be adorned");
		}
		return getBounds((AdornedPredicate) l.getPredicate(), l);
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

		final List<ITerm> bounds = new ArrayList<ITerm>(p.getAdornment().length);
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
	 * Returns a sip for a given adorned rule.
	 * 
	 * @param r
	 *            for which to retrieve the sip
	 * @return the sip
	 */
	private SIPImpl getAdornedSip(final AdornedRule r) {
		SIPImpl sip = null;
		if ((sip = adornedSipCache.get(r)) == null) {
			sip = SipHelper.getAdornedSip(r);
			adornedSipCache.put(r, sip);
		}
		return sip;
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
	public Set<AdornedRule> getRewrittenRules() {
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

	/**
	 * <p>
	 * Returns a short description of this object. The format of the returned
	 * String is undocumented and subject to change.
	 * </p>
	 * <p>
	 * An example return String could be &lt;seed&gt;&lt;blank line&gt;&lt;list
	 * of all magic rules separated by new lines&gt;&lt;blank line&gt;&lt;list
	 * of all rewritten rules separated by new lines&gt;.
	 * </p>
	 * 
	 * @return the String representation of this object
	 */
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
