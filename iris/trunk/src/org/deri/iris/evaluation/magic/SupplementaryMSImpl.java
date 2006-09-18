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

import static org.deri.iris.factory.Factory.BASIC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.evaluation.common.IAdornedRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;

/**
 * <p>
 * Simple Implementation of the &quot;Generalized Supplementary Magic Sets&quot;
 * from Beeri's paper &quot;The Power Of Magic&quot;.
 * </p>
 * <p>
 * $Id: SupplementaryMSImpl.java,v 1.2 2006-09-18 07:52:35 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.2 $
 * @date $Date: 2006-09-18 07:52:35 $
 */
public class SupplementaryMSImpl {

	/** Prefix for the supplementary Magic Predicates. */
	private static final String SUPMAGIC_PREFIX = "supmagic";

	/** Contains all submagic rules. */
	private List<List<IRule>> supMagicRules = new ArrayList<List<IRule>>();

	/** Contains all eht substituted rewriteten rules. */
	private List<IAdornedRule> rewrittenRules = new ArrayList<IAdornedRule>();

	/** Contains all the substituted magic rules. */
	private List<IRule> magicRules = new ArrayList<IRule>();

	/** The seed for this program. */
	private IQuery seed;

	/**
	 * Creates a Supplementary Magic Set out of a magic set.
	 * 
	 * @param ms
	 *            the ms for which to create the supplementary ms
	 * @throws NullPointerException
	 *             if the magic sets is null
	 */
	public SupplementaryMSImpl(final MagicSetImpl ms) {
		if (ms == null) {
			throw new NullPointerException("The magic sets must not be null");
		}

		magicRules.addAll(ms.getMagicRules());

		int ruleCounter = 1;
		for (final IAdornedRule r : ms.getRewrittenRules()) {
			final List<IRule> supRules = new ArrayList<IRule>(
					r.getBodyLenght() - 2);
			supMagicRules.add(supRules);

			final List<ILiteral> sortedBody = new ArrayList<ILiteral>(r
					.getBodyLiterals());
			Collections.sort(sortedBody, r.getSIP().getLiteralComparator());

			// generating the sup magic rules
			int maxLiteral = 0;
			for (final ILiteral l : r.getSIP().getLeafVertices()) {
				maxLiteral = Math.max(maxLiteral, sortedBody.indexOf(l));
			}
			maxLiteral++;

			for (int litCounter = 2; litCounter < maxLiteral; litCounter++) {
				final IRule supmRule = createSupMRule(r, sortedBody,
						litCounter, ruleCounter);
				supRules.add(supmRule);
				// modifying the magic rules
				if (litCounter > 2) {
					for (final ILiteral l : supmRule.getBodyLiterals()) {
						if (l.getPredicate() instanceof AdornedPredicate) {
							findAndSubstituteMagicRule(l, litCounter,
									ruleCounter);
						}
					}
				}
			}
			// rewrite the actual rule
			rewrittenRules.add(modifyRewrittenRule(r, sortedBody, ruleCounter));
			ruleCounter++;
		}
		// adding the query
		seed = ms.getSeed();
	}

	/**
	 * Searches for the magic rule of the given literal and tries to substitute
	 * it.
	 * 
	 * @param l
	 *            the original magic literal for which the rule was created
	 * @param literalIndex
	 *            the literal index of the literal in the original rule
	 * @param ruleIndex
	 *            the rule index of the original rule of this literal
	 * @return true if there was a substitution, otherwise false
	 * @throws NullPointerException
	 *             if the literal is null
	 * @throws IllegalArgumentException
	 *             if the prediacte of the literal isn't adorned
	 * @throws IllegalArgumentException
	 *             if the literal index is smaller than 3
	 * @throws IllegalArgumentException
	 *             if the rule index is smaller than 1
	 */
	private boolean findAndSubstituteMagicRule(final ILiteral l,
			final int literalIndex, final int ruleIndex) {
		if (l == null) {
			throw new NullPointerException(
					"The literal and the rules collection must not be null");
		}
		if (!(l.getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the literal must be adorned");
		}
		if (literalIndex < 3) {
			throw new IllegalArgumentException(
					"The literal index must be bigger than 2");
		}
		if (ruleIndex < 1) {
			throw new IllegalArgumentException(
					"The rule index must be bigger than 0");
		}
		final IRule toSubstitute = searchForMagicRule(l, magicRules);
		final IRule substitute = createSubstituteForMagicRule(toSubstitute, l,
				literalIndex, ruleIndex);
		if (substitute != null) {
			magicRules.set(magicRules.indexOf(toSubstitute), substitute);
			return true;
		}
		return false;
	}

	/**
	 * Searches for possible substitutions for the given magic literal, creates
	 * and returns it. At the moment, if the rule contains only label literals,
	 * the rule won't be substituted and null will be returned.
	 * 
	 * @param r
	 *            which to substitute
	 * @param l
	 *            the original magic literal for which the rule was created
	 * @param literalIndex
	 *            the literal index of the literal in the original rule
	 * @param ruleIndex
	 *            the rule index of the original rule of this literal
	 * @return the substituted rule or null, if the rule was null or the rule's
	 *         body contained labeled literals
	 * @throws NullPointerException
	 *             if the literal is null
	 * @throws IllegalArgumentException
	 *             if the prediacte of the literal isn't adorned
	 * @throws IllegalArgumentException
	 *             if the literal index is smaller than 3
	 * @throws IllegalArgumentException
	 *             if the rule index is smaller than 1
	 */
	private IRule createSubstituteForMagicRule(final IRule r, final ILiteral l,
			final int literalIndex, final int ruleIndex) {
		if (r == null) {
			return null;
		}
		for (final ILiteral bodyL : r.getBodyLiterals()) {
			if (bodyL.getPredicate().getPredicateSymbol().startsWith(
					MagicSetImpl.MAGIC_LABEL_PREFIX)) {
				return null;
			}
		}
		if (l == null) {
			throw new NullPointerException(
					"The literal and the rules collection must not be null");
		}
		if (!(l.getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the literal must be adorned");
		}
		if (literalIndex < 3) {
			throw new IllegalArgumentException(
					"The literal index must be bigger than 2");
		}
		if (ruleIndex < 1) {
			throw new IllegalArgumentException(
					"The rule index must be bigger than 0");
		}

		return BASIC.createRule(BASIC.createHead(r.getHeadLiterals()), BASIC
				.createBody(supMagicRules.get(ruleIndex - 1).get(
						literalIndex - 3).getHeadLiterals()));

	}

	/**
	 * Searches for the magic rule for the given adorned literal.
	 * 
	 * @param l
	 *            the magic literal for which to search the magic rule
	 * @param magicRules
	 *            the collection of magic rules where to search
	 * @return the magic rule, or null, if it couldn't be found
	 * @throws NullPointerException
	 *             if the literal or the collection of rules is null
	 * @throws IllegalArgumentException
	 *             if the collection of rules contains null
	 * @throws IllegalArgumentException
	 *             if the predicate of the literal isn't an AdornedPredicate
	 */
	private static IRule searchForMagicRule(final ILiteral l,
			final Collection<IRule> magicRules) {
		if ((magicRules == null) || (l == null)) {
			throw new NullPointerException(
					"The magic rules and the bounds must not be null");
		}
		if (!(l.getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The the predicate of the literal must be adorned");
		}
		if (magicRules.contains(null)) {
			throw new IllegalArgumentException(
					"The magic rules must not contain null");
		}

		final List<ITerm> bounds = MagicSetImpl.getBounds(l);
		for (final IRule magicRule : magicRules) {
			final ILiteral headLiteral = magicRule.getHeadLiteral(0);
			final List<ITerm> terms = headLiteral.getTuple().getTerms();
			if ((headLiteral.getPredicate().getPredicateSymbol()
					.startsWith(MagicSetImpl.MAGIC_PREDICATE_PREFIX
							+ l.getPredicate().getPredicateSymbol()))
					&& (terms.size() == bounds.size())
					&& bounds.containsAll(terms)) {
				return magicRule;
			}
		}
		return null;
	}

	/**
	 * Modifies a rule and tries to substitute as much as possible from it's
	 * body through a supplementary rule.
	 * 
	 * @param r
	 *            the rule to modify
	 * @param sortedBody
	 *            the body of the rule sorted according to the sip of the rule
	 * @param ruleIndex
	 *            the index of the rule. The index starts at 1.
	 * @return the substituted rule
	 * @throws NullPointerException
	 *             if the rule or the sortedbody is <code>null</code>
	 */
	private IAdornedRule modifyRewrittenRule(final IAdornedRule r,
			final List<ILiteral> sortedBody, final int ruleIndex) {
		if ((r == null) || (sortedBody == null)) {
			throw new NullPointerException(
					"The rule and the sortedBody must not be null");
		}
		if (sortedBody.contains(null)) {
			throw new IllegalArgumentException(
					"The sortedBody must not contain null");
		}
		if (ruleIndex < 1) {
			throw new IllegalArgumentException(
					"The rule index must not be smaller than 1, but was "
							+ ruleIndex);
		}

		if (supMagicRules.get(ruleIndex - 1).isEmpty()) {
			return r;
		}

		final IRule lastRule = supMagicRules.get(ruleIndex - 1).get(
				supMagicRules.get(ruleIndex - 1).size() - 1);

		final List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(lastRule.getHeadLiteral(0));
		for (int i = sortedBody.indexOf(lastRule.getBodyLiteral(1)) + 1, m = sortedBody
				.size(); i < m; i++) {
			body.add(sortedBody.get(i));
		}
		return SipHelper.getAdornedRule(BASIC.createRule(BASIC.createHead(r
				.getHeadLiterals()), BASIC.createBody(body)));
	}

	/**
	 * <p>
	 * Creates a supplementary magic rule with the given indexes.
	 * </p>
	 * <p>
	 * <b>ATTENTION: the sortedBody is assumed to contain the magic predicate of
	 * the head of the rule.</b>
	 * </p>
	 * 
	 * @param r
	 *            the rule for which to create the supplementatry magic rule
	 * @param sortedBody
	 *            the body of the rule sorted according to the sip of the rule
	 * @param literalIndex
	 *            the index of the literal
	 * @param ruleIndex
	 *            the index of the rule. The index starts at 1.
	 * @return the generated supplementary magic rule
	 * @throws NullPointerException
	 *             if the rule or the sortedBody is null
	 * @throws IllegalArgumentException
	 *             if the sortedBody contains null
	 * @throws IllegalArgumentException
	 *             if the literalIndex is smaller than 2
	 * @throws IllegalArgumentException
	 *             if the ruleIndex is smalller than 1
	 */
	private IRule createSupMRule(final IAdornedRule r,
			final List<ILiteral> sortedBody, final int literalIndex,
			final int ruleIndex) {
		if ((r == null) || (sortedBody == null)) {
			throw new NullPointerException(
					"The rule and the sortedBody must not be null");
		}
		if (sortedBody.contains(null)) {
			throw new IllegalArgumentException(
					"The sortedBody must not contain null");
		}
		if (literalIndex < 2) {
			throw new IllegalArgumentException(
					"The literal index must be bigger than 2, but was "
							+ literalIndex);
		}
		if (ruleIndex < 1) {
			throw new IllegalArgumentException(
					"The rule index must be bigger than 1, but was "
							+ literalIndex);
		}

		// compute body of the rule
		final ILiteral[] body = new ILiteral[2];
		if (literalIndex == 2) { // adding the magic head as first literal
			body[0] = sortedBody.get(0);
			body[1] = sortedBody.get(1);
		} else { // adding the head of the previous supm rule as first
			// literal
			body[0] = supMagicRules.get(ruleIndex - 1).get(literalIndex - 3)
					.getHeadLiteral(0);
			body[1] = sortedBody.get(literalIndex - 1);
		}

		// compute head of the rule
		// gathering all possible variables
		final Set<ITerm> headVars = new HashSet<ITerm>();
		headVars.addAll(getAllVariables(body[0].getTuple().getTerms()));
		headVars.addAll(getAllVariables(body[1].getTuple().getTerms()));
		// cleaning up the variables
		headVars.retainAll(getNotDiscardedVars(literalIndex, r, sortedBody));
		// computing the predicate
		final IPredicate p = BASIC.createPredicate(SUPMAGIC_PREFIX + "["
				+ ruleIndex + "][" + literalIndex + "]", headVars.size());
		final ILiteral headLiteral = BASIC.createLiteral(true, p, BASIC
				.createTuple(new ArrayList<ITerm>(headVars)));

		// putting all together
		return BASIC.createRule(BASIC.createHead(headLiteral), BASIC
				.createBody(body));
	}

	/**
	 * <p>
	 * Determines the variables which should not be deleted out of the head
	 * predicate of the supplementary rule with the given index.
	 * </p>
	 * <p>
	 * <b>ATTENTION: the sortedBody is assumed to contain the magic predicate of
	 * the head of the rule.</b>
	 * </p>
	 * 
	 * @param index
	 *            the index of the rule for which to get the variables. The
	 *            index starts at 1.
	 * @param r
	 *            Adorned rule for which the supplementary rule is created
	 * @param sortedBody
	 *            the body of the rule sorted according to the sip of the rule
	 * @return the variables which should not be discarted from the head of the
	 *         supplementary rule
	 * @throws NullPointerException
	 *             if the rule or the sortedBody is null
	 * @throws IllegalArgumentException
	 *             if the sortedBody contains null
	 * @throws IllegalArgumentException
	 *             if the index is smaller than 2
	 */
	private static Set<IVariable> getNotDiscardedVars(final int index,
			final IAdornedRule r, final List<ILiteral> sortedBody) {
		if (index < 2) {
			throw new IllegalArgumentException(
					"The literalIndex must be at least 2, but was " + index);
		}
		if ((r == null) || (sortedBody == null)) {
			throw new NullPointerException(
					"The rule and the sortedBody must not be null");
		}
		if (sortedBody.contains(null)) {
			throw new IllegalArgumentException(
					"The sortedBody must not contain null");
		}

		final Set<IVariable> gotToStay = new HashSet<IVariable>();
		gotToStay.addAll(r.getHeadVariables());
		for (int counter = index, max = sortedBody.size(); counter < max; counter++) {
			gotToStay.addAll(getAllVariables(sortedBody.get(counter).getTuple()
					.getTerms()));
		}
		return gotToStay;
	}

	/**
	 * Returns a set of all variables of a collection of terms.
	 * 
	 * @param terms
	 *            the collection of terms
	 * @return the set of variables in the collection
	 * @throws NullPointerException
	 *             if the collection was null
	 */
	private static Set<IVariable> getAllVariables(final Collection<ITerm> terms) {
		if (terms == null) {
			throw new NullPointerException(
					"The collections of terms must not be null");
		}
		final Set<IVariable> vars = new HashSet<IVariable>(terms.size());
		for (final ITerm t : terms) {
			if (t instanceof IVariable) {
				vars.add((IVariable) t);
			}
		}
		return vars;
	}

	/**
	 * Returns the seed of this program.
	 * 
	 * @return the seed
	 */
	public IQuery getSeed() {
		return seed;
	}

	/**
	 * Returns the magic rules substituted as much as possible by the
	 * Supplementary Magic Sets. The returned list is unmodifiable.
	 * 
	 * @return the magic rules
	 */
	public List<IRule> getMagicRules() {
		return Collections.unmodifiableList(magicRules);
	}

	/**
	 * Returns the rewritten rules substituted as much as possible by the
	 * Supplementary Magic Sets. The returned list is unmodifiable.
	 * 
	 * @return the rewritten rules
	 */
	public List<IAdornedRule> getRewrittenRules() {
		return Collections.unmodifiableList(rewrittenRules);
	}

	/**
	 * Returns a copy of all Supplementary Magic Rules.
	 * 
	 * @return the Supplementary Magic Rules
	 */
	public List<IRule> getSupplementaryRules() {
		List<IRule> rules = new ArrayList<IRule>();
		for (final List<IRule> l : supMagicRules) {
			rules.addAll(l);
		}
		return rules;
	}

	/**
	 * <p>
	 * Returns a summary of this object. <b>The format of the returned String is
	 * undocumented and subject to change.</b>
	 * </p>
	 * <p>
	 * The returned string might be composed as follows: the seed, blank line,
	 * the Supplementary Magic Rules with one line for each rule, a blank line,
	 * the rewritten rules with one line for each rule, a blank line and then
	 * the magic rules with one line for each rule.
	 * </p>
	 * 
	 * @return the String summary
	 */
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		final String NEwLINE = System.getProperty("line.separator");
		buffer.append(seed).append(NEwLINE).append(NEwLINE);
		for (final List<IRule> rules : supMagicRules) {
			for (final IRule r : rules) {
				buffer.append(r).append(NEwLINE);
			}
		}
		buffer.append(NEwLINE);
		for (final IAdornedRule r : rewrittenRules) {
			buffer.append(r).append(NEwLINE);
		}
		buffer.append(NEwLINE);
		for (final IRule r : magicRules) {
			buffer.append(r).append(NEwLINE);
		}
		return buffer.toString();
	}
}
