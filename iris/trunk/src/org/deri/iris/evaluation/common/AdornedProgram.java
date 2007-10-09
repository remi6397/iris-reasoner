/*
 * Integrated Rule Inference System (IRIS): An extensible rule inference system
 * for datalog with extensions by built-in predicates, default negation (under
 * well-founded semantics), function symbols and contexts.
 * 
 * Copyright (C) 2006 Digital Enterprise Research Institute (DERI),
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, A-6020
 * Innsbruck. Austria.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.deri.iris.evaluation.common;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.evaluation.magic.ISip;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.magic.SIPImpl;

/**
 * <p>
 * This is a simple implementation of an adorned program. <b>NOTE: At the moment
 * this class only works with rules with one literal in the head.</b>
 * </p>
 * <p>
 * $Id: AdornedProgram.java,v 1.27 2007-10-09 07:45:31 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.27 $
 */
public class AdornedProgram {

	// TODO: make a smaller empty-constant-term
	private static final ITerm EMPTY_CONSTANT_TERM = TERM.createString("");

	/** Set of all derived predicates. */
	private final Set<IPredicate> deriveredPredicates = new HashSet<IPredicate>();

	/** Set of all adorned predicates. */
	private final Set<AdornedPredicate> adornedPredicates = new HashSet<AdornedPredicate>();

	/** Set of all adorned rules. */
	private final Set<AdornedRule> adornedRules = new HashSet<AdornedRule>();

	/** Set of all normal rules. */
	private final Set<IRule> rules;

	/** Query for this program. */
	private final IQuery query;

	/** Temporary query literal for conjunctive queries. */
	private static final ILiteral TEMP_QUERY_LITERAL = BASIC.createLiteral(
			true, 
			BASIC.createAtom(
				BASIC.createPredicate("TEMP_QUERY_LITERAL", 0), 
				BASIC.createTuple(Collections.EMPTY_LIST)));

	/** Adorned predicate for the temporary query literal. */
	private static final AdornedPredicate AD_TEMP_QUERY_PREDICATE = 
		new AdornedPredicate(TEMP_QUERY_LITERAL);

	/**
	 * Creates a new adorned program depending on the submitted rules and the
	 * query.
	 * 
	 * @param rules
	 *            for which to create the program
	 * @param query
	 *            for the program
	 * @throws NullPointerException
	 *             if the rule, or the query are null
	 * @throws IllegalArgumentException
	 *             if the size of the head literals, or of the query literals is
	 *             bigger than 1
	 * @throws IllegalArgumentException
	 *             if the list of rules contains null
	 */
	public AdornedProgram(final Collection<IRule> rules, final IQuery query) {
		// check the parameters
		if ((rules == null) || (query == null)) {
			throw new NullPointerException(
					"The rules and the query must not be null");
		}
		if (rules.contains(null)) {
			throw new IllegalArgumentException(
					"The list of rules must not contain null");
		}
		for (IRule r : rules) {
			if (r.getHeadLiterals().size() != 1) {
				throw new IllegalArgumentException("At the moment this class "
						+ "only works with rules with one literal in the head.");
			}
		}

		IQuery newQuery = null; // if we can adorn the query save it here

		if (query.getQueryLiterals().size() > 1) { // if we got a conjunctive query
			// construct the temp query and rule
			final IRule tmpRule = BASIC.createRule(
					BASIC.createHead(TEMP_QUERY_LITERAL), 
					BASIC.createBody(query.getQueryLiterals()));
			final IQuery tmpQuery = BASIC.createQuery(TEMP_QUERY_LITERAL);

			// adorn it
			final Set<IRule> modRules = new HashSet<IRule>(rules);
			modRules.add(tmpRule);
			createAdornedRules(modRules, tmpQuery);

			// remove the temp rule again and create the query out
			// of it
			for (final IRule r : adornedRules) {
				if ((r.getHeadLiterals().size() == 1) && 
						(r.getHeadLiterals().get(0).getPredicate().equals(AD_TEMP_QUERY_PREDICATE))) {
					adornedRules.remove(r);
					newQuery = BASIC.createQuery(r.getBodyLiterals());
					break;
				}
			}
		} else { // handle non-conjunctive query
			createAdornedRules(rules, query);

			final ILiteral ql = query.getQueryLiterals().get(0);
			final AdornedPredicate ap = new AdornedPredicate(ql);
			if (Collections.frequency(Arrays.asList(ap.getAdornment()), Adornment.FREE) == 
					ap.getAdornment().length) {
				newQuery = BASIC.createQuery(BASIC.createLiteral(
							ql.isPositive(), 
							BASIC.createAtom(ap, BASIC.createTuple(ql.getTuple().getTerms()))));
			}

		}

		// TODO: maybe defensive copy should be made.
		this.rules = new HashSet<IRule>(rules);
		this.query = (newQuery != null) ? newQuery : query;
	}

	/**
	 * Iterates over all the rules an checks what can be adorned. <b>The
	 * query must only contain one literal!</b>
	 * @param rules the rules to adorn
	 * @param query the the query from where to take the bounds from
	 */
	private void createAdornedRules(final Collection<IRule> rules, final IQuery query) {
		assert rules != null: "The rules must not be null";
		assert !rules.contains(null): "The rules must not contain null";
		assert query != null: "The query must not be null";
		assert query.getQueryLiterals().size() == 1: "The query must only contain one literal";

		deriveredPredicates.addAll(updateDerivedPredicates(rules)); // TODO: i think this should go

		// creating an adored predicate out of the query, and add it to the
		// predicate sets
		final AdornedPredicate qa = new AdornedPredicate(query
				.getQueryLiteral(0));

		final Set<AdornedPredicate> predicatesToProcess = new HashSet<AdornedPredicate>();
		predicatesToProcess.add(qa);
		adornedPredicates.add(qa);

		// iterating through all predicates in the todolist
		while (!predicatesToProcess.isEmpty()) {
			final AdornedPredicate ap = predicatesToProcess.iterator().next();
			predicatesToProcess.remove(ap);

			for (final IRule r : rules) {
				final ILiteral lh = r.getHeadLiteral(0);
				final IPredicate ph = lh.getPredicate();

				// if the headliteral and the adorned predicate have the
				// same signature
				if (ap.hasSameSignature(ph)) {
					// creating a sip for the actual rule and the ap
					final ISip sip = new SIPImpl(r, createQueryForAP(ap, lh));
					final AdornedRule ra = new AdornedRule(r, sip);
					ra.replaceHeadLiteral(lh, ap);

					// iterating through all bodyliterals of the
					for (final ILiteral l : r.getBodyLiterals()) {
						final AdornedPredicate newAP = processLiteral(l, ra);
						// adding the adorned predicate to the sets
						if ((newAP != null) && (adornedPredicates.add(newAP))) {
							predicatesToProcess.add(newAP);
						}
					}
					adornedRules.add(ra);
				}
			}
		}
	}

	/**
	 * <p>
	 * This returns a simple string representation of this program. <b>The
	 * subject of this representation is to change.</b>
	 * </p>
	 * <p>
	 * The return of this method will look something like: all adorned rules
	 * with one line for each rule, blank line, all normal rules with one line
	 * for each rule, blank line, the query.
	 * </p>
	 * 
	 * @return the string representation
	 */
	public String toString() {
		final String NEWLINE = System.getProperty("line.separator");
		StringBuilder buffer = new StringBuilder();
		for (AdornedRule r : adornedRules) {
			buffer.append(r).append(NEWLINE);
		}
		buffer.append(NEWLINE);
		for (IRule r : rules) {
			buffer.append(r).append(NEWLINE);
		}
		buffer.append(NEWLINE);
		buffer.append(query);
		return buffer.toString();
	}

	public int hashCode() {
		// only the submitted query and rules are taken into account,
		// because all other member variables should then be the same
		int result = 17;
		result = result * 37 + query.hashCode();
		result = result * 37 + rules.hashCode();
		return result;
	}

	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof AdornedProgram)) {
			return false;
		}
		AdornedProgram ap = (AdornedProgram) o;
		// only the submitted query and rules are taken into account,
		// because all other member variables should then be the same
		return query.equals(ap.query) && rules.equals(ap.rules);
	}

	public Set<AdornedRule> getAdornedRules() {
		return Collections.unmodifiableSet(adornedRules);
	}

	public Set<IRule> getNormalRules() {
		return Collections.unmodifiableSet(rules);
	}

	public Set<AdornedPredicate> getAdornedPredicates() {
		return Collections.unmodifiableSet(adornedPredicates);
	}

	public IQuery getQuery() {
		return query;
	}

	/**
	 * <p>
	 * Processes a literal of a given adorned rule.
	 * </p>
	 * <p>
	 * This method will frist
	 * determine, whether the predicate of this literal is derived, and if it is
	 * the predicate will be adorned with the variables passed in from the sip
	 * of the adorned rule. The predicate of the literal of the adorned rule
	 * passed in will be replaced by the adorned one. If the predicate was
	 * derived, the adorned predicate will be returned, otherwise null.
	 * </p>
	 * 
	 * @param l
	 *            the literal to process
	 * @param r
	 *            the adorned rule containing the literal
	 * @return the adorned predicate for this literal corresponding to the
	 *         passed variables of the adorned rule, or {@code null}, if the
	 *         predicate of the literal wasn't derived, or if the adornment only
	 *         consists of free's.
	 * @throws NullPointerException
	 *             if the rule, or the literal are {@code null}
	 */
	private AdornedPredicate processLiteral(final ILiteral l,
			final AdornedRule r) {
		if ((l == null) || (r == null)) {
			throw new NullPointerException(
					"The literal and rule must not be null");
		}
		AdornedPredicate ap = null;
		if (deriveredPredicates.contains(l.getPredicate())) {
			ap = new AdornedPredicate(l, r.getSIP().getBoundVariables(l));
			r.replaceBodyLiteral(l, ap);
		}
		return ap;
	}

	/**
	 * Determines all derived predicates of the program and adds them to the
	 * derivedPredicates set.
	 * @param rules rules from where to check which predicates are derived
	 * @return the derived predicates
	 */
	private static Set<IPredicate> updateDerivedPredicates(final Collection<IRule> rules) {
		assert rules != null: "The rules must not be null";
		assert !rules.contains(null): "The rules must not contain null";

		final Set<IPredicate> derived = new HashSet<IPredicate>();
		for (final IRule r : rules) {
			//deriveredPredicates.add(r.getHeadLiteral(0).getPredicate());
			for (final ILiteral l : r.getHeadLiterals()) {
				derived.add(l.getPredicate());
			}
		}
		return derived;
	}

	/**
	 * Creates a query out of an adored predicate and a literal. Therefore it
	 * creates a literal with the specified arity, and puts constant terms for
	 * the position of the bound's and the variables of the literals for the
	 * free's in the terms.
	 * 
	 * @param ap
	 *            the adored predivate from where to take the bound's and free's
	 * @param hl
	 *            the literal for which to create the query
	 * @return the query
	 * @throws NullPointerException
	 *             if the literal or the adored predicate are null
	 * @throws IllegalArgumentException
	 *             if the arity of the predicate of the literal and the adored
	 *             predicate isn't equal
	 * @throws IllegalArgumentException
	 *             if the adornment of the predicate contains something else
	 *             than BOUND or FREE.
	 */
	private static IQuery createQueryForAP(final AdornedPredicate ap,
			final ILiteral hl) {
		if ((hl == null) || (ap == null)) {
			throw new NullPointerException(
					"The predicate and literal must not be null");
		}
		if (hl.getPredicate().getArity() != ap.getArity()) {
			throw new IllegalArgumentException("The arity of the predicate of "
					+ "the literal and the adorned predicate be equal");
		}
		ITerm[] terms = new ITerm[ap.getArity()];
		int iCounter = 0;
		for (Adornment a : ap.getAdornment()) {
			switch (a) {
			case BOUND:
				terms[iCounter] = EMPTY_CONSTANT_TERM;
				break;
			case FREE:
				terms[iCounter] = hl.getTuple().getTerm(iCounter);
				break;
			default:
				throw new IllegalArgumentException(
						"Only BOUND and FREE are allowed as adornments");
			}
			iCounter++;
		}
		return BASIC.createQuery(BASIC.createLiteral(hl.isPositive(), ap, BASIC
				.createTuple(terms)));
	}

	/**
	 * Represents an adorned predicate.
	 * 
	 * @author richi
	 */
	public static class AdornedPredicate implements IPredicate {
		/** The predicate which should be adorned */
		private final IPredicate p;

		/** The adornment of the predicate */
		private final Adornment[] adornment;

		/**
		 * Constructs an adorned predicate.
		 * 
		 * @param symbol
		 *            symbol for the predicate
		 * @param adornment
		 *            the array of bound and frees
		 * @throws NullPointerException
		 *             if the adornment of the symbol is null
		 * @throws IllegalArgumentException
		 *             if the adornment contains null
		 */
		public AdornedPredicate(final String symbol, final Adornment[] adornment) {
			this(symbol, adornment.length, adornment);
		}

		/**
		 * Constructs an adorned predicate. The length of the adornment must be
		 * at least as long as the arity.
		 * 
		 * @param symbol
		 *            symbol for the predicate
		 * @param arity
		 *            the arity of the predicate.
		 * @param adornment
		 *            the array of bound and frees
		 * @throws NullPointerException
		 *             if the adornment of the symbol is null
		 * @throws IllegalArgumentException
		 *             if the adornment contains null
		 * @throws IllegalArgumentException
		 *             if the arity is bigger than the length of the adornment
		 */
		public AdornedPredicate(final String symbol, final int arity,
				final Adornment[] adornment) {
			if ((adornment == null) || (symbol == null)) {
				throw new NullPointerException(
						"The symbol and the adornment must not be null");
			}
			if (Arrays.asList(adornment).contains(null)) {
				throw new IllegalArgumentException(
						"The adornments must not contain null");
			}
			if (adornment.length < arity) {
				throw new IllegalArgumentException(
						"The length of the adornment "
								+ "and the arity of the predicate doesn't match.");
			}
			this.p = BASIC.createPredicate(symbol, arity);
			this.adornment = new Adornment[adornment.length];
			System.arraycopy(adornment, 0, this.adornment, 0, adornment.length);
		}

		/**
		 * Constructs an adorned predicate out of a literal and its bound
		 * variables. All occurences in the literal of the bound variables will
		 * be marked as bound in the adornment.
		 * 
		 * @param l
		 *            the literal where to take the predicate and the variables
		 *            from
		 * @param bounds
		 *            set of all bound variables of the literal
		 * @throws nullpointer exception if the literal, the predicate of the
		 *        literal or the bounds is null.
		 */
		public AdornedPredicate(final ILiteral l,
				final Collection<IVariable> bounds) {
			if (l == null) {
				throw new NullPointerException("The literal must not be null");
			}
			p = l.getPredicate();

			if ((p == null) || (bounds == null)) {
				throw new NullPointerException(
						"The predicate or set of bound variables must not be null");
			}

			int iCoutner = 0;
			adornment = new Adornment[p.getArity()];

			// computing the adornment
			for (final ITerm t : l.getTuple().getTerms()) {
				if (isBound(t, bounds)) {
					adornment[iCoutner] = Adornment.BOUND;
				} else {
					adornment[iCoutner] = Adornment.FREE;
				}
				iCoutner++;
			}
		}

		/**
		 * Constructs an adorned predicate out of a literal. All ground terms
		 * will be marked as bound.
		 * 
		 * @param l
		 *            for which to construct the adorned predicate
		 * @throws NullPointerException
		 *             if the literal of the predicate of the literal is null
		 */
		public AdornedPredicate(final ILiteral l) {
			this(l.getAtom());
		}

		/**
		 * Constructs an adorned predicate out of a atom. All ground terms
		 * will be marked as bound.
		 * 
		 * @param a
		 *            atom for which to construct the adorned predicate
		 * @throws NullPointerException
		 *             if the literal of the predicate of the literal is null
		 */
		public AdornedPredicate(final IAtom a) {
			if (a == null) {
				throw new NullPointerException("The atom must not be null");
			}

			this.p = a.getPredicate();

			// checking the submitted values
			if (p == null) {
				throw new NullPointerException(
						"The predicate of the literal must not be null");
			}

			int iCounter = 0;
			// computing the adornment
			adornment = new Adornment[p.getArity()];
			for (ITerm t : (List<ITerm>) a.getTuple().getTerms()) {
				if (t.isGround()) {
					adornment[iCounter] = Adornment.BOUND;
				} else {
					adornment[iCounter] = Adornment.FREE;
				}
				iCounter++;
			}
		}

		/**
		 * Checks whether a term is bound with a submitted collection of bound
		 * variables. This method also checks whether all variables are covered
		 * with the variables of the bound collection.
		 * 
		 * @param t
		 *            the term to check
		 * @param b
		 *            the collection of bound variables to check agains
		 * @return {@code true} if the term is bound using the bound collection,
		 *         otherwise {@code false}
		 */
		private static boolean isBound(final ITerm t,
				final Collection<IVariable> b) {
			if (t == null) {
				throw new NullPointerException("The term must not be null");
			}
			if (b == null) {
				throw new NullPointerException(
						"The bound collection must not be null");
			}

			if (t.isGround()) {
				return true;
			}
			if (t instanceof IConstructedTerm) {
				return b.containsAll(((IConstructedTerm) t).getVariables());
			}
			return b.contains(t);
		}

		public boolean hasSameSignature(final IPredicate pred) {
			if (pred == null) {
				throw new NullPointerException("The predicate must not be null");
			}
			return (pred.getArity() == p.getArity())
					&& (pred.getPredicateSymbol()
							.equals(p.getPredicateSymbol()));
		}

		public IPredicate getUnadornedPredicate() {
			return p;
		}

		public Adornment[] getAdornment() {
			Adornment[] copy = new Adornment[adornment.length];
			System.arraycopy(adornment, 0, copy, 0, adornment.length);
			return copy;
		}

		public int getArity() {
			return p.getArity();
		}

		public String getPredicateSymbol() {
			return p.getPredicateSymbol();
		}

		public int compareTo(IPredicate o) {
			return p.compareTo(o);
		}

		public int hashCode() {
			int result = 17;
			result = result * 37 + p.hashCode();
			result = result * 37 + Arrays.hashCode(adornment);
			return result;
		}

		public String toString() {
			StringBuilder buffer = new StringBuilder();
			buffer.append(p).append("^");
			for (Adornment a : adornment) {
				buffer.append(a);
			}
			return buffer.toString();
		}

		public boolean equals(final Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof AdornedPredicate)) {
				return false;
			}
			AdornedPredicate p = (AdornedPredicate) o;
			return this.p.equals(p.p) && Arrays.equals(adornment, p.adornment);
		}
	}

	/**
	 * <p>
	 * Simple representation of an adorned rule. The only difference to an
	 * odinary rule is, that it has a sip attached, and that you can exchange
	 * literals.
	 * </p>
	 * <b>ATTENTION: the replaceHeadLiterla and replaceBodyLiteral are slow,
	 * because they copy the head and body for each invocation.</b>
	 * </p>
	 * <p>
	 * Id
	 * </p>
	 * 
	 * @author richi
	 * @version $Revision: 1.27 $
	 */
	public static class AdornedRule implements IRule {
		/** The inner rule represented by this object */
		private IRule rule;

		/** The sip for this rule */
		private final ISip sip;

		/**
		 * Constructs a new adorned rule.
		 * 
		 * @param r
		 *            the rule
		 * @param s
		 *            the sip for this rule
		 * @throws NullPointerException
		 *             if the rule or the sip is null
		 */
		public AdornedRule(final IRule r, final ISip s) {
			// checking arguments
			if ((r == null) || (s == null)) {
				throw new NullPointerException(
						"The rule and the sip must not be null");
			}
			rule = BASIC.createRule(BASIC.createHead(new ArrayList<ILiteral>(r
					.getHeadLiterals())), BASIC
					.createBody(new ArrayList<ILiteral>(r.getBodyLiterals())));
			sip = s.defensifeCopy();
		}

		public ISip getSIP() {
			return sip;
		}

		public void replaceHeadLiteral(final ILiteral l, final IPredicate p) {
			if ((l == null) || (p == null)) {
				throw new NullPointerException(
						"The literal and the predcate must not be null");
			}
			if (l.getPredicate().getArity() != p.getArity()) {
				throw new IllegalArgumentException(
						"The arities of the predicate of the literal "
								+ "and the new predicate doesn't match.");
			}

			final List<ILiteral> head = new ArrayList<ILiteral>(rule
					.getHeadLiterals());

			final int index = head.indexOf(l);
			if (index == -1) {
				throw new IllegalArgumentException("The literal (" + l
						+ ") couldn't be found in head (" + head + ")");
			}

			head.set(index, BASIC
					.createLiteral(l.isPositive(), p, l.getTuple()));
			rule = BASIC.createRule(BASIC.createHead(head), BASIC
					.createBody(rule.getBodyLiterals()));
		}

		public void replaceBodyLiteral(final ILiteral l, final IPredicate p) {
			if ((l == null) || (p == null)) {
				throw new NullPointerException(
						"The literal and the predcate must not be null");
			}
			if (l.getPredicate().getArity() != p.getArity()) {
				throw new IllegalArgumentException(
						"The arities of the predicate of the literal "
								+ "and the new predicate doesn't match.");
			}

			final List<ILiteral> body = new ArrayList<ILiteral>(rule
					.getBodyLiterals());

			final int index = body.indexOf(l);
			if (index == -1) {
				throw new IllegalArgumentException("The literal (" + l
						+ ") couldn't be found in body (" + body + ")");
			}

			body.set(index, BASIC
					.createLiteral(l.isPositive(), p, l.getTuple()));
			rule = BASIC.createRule(BASIC.createHead(rule.getHeadLiterals()),
					BASIC.createBody(body));
		}

		public String toString() {
			return rule.toString();
		}

		public boolean equals(final Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof AdornedRule)) {
				return false;
			}
			AdornedRule r = (AdornedRule) o;
			return rule.equals(r.rule) && sip.equals(r.sip);
		}

		public int hashCode() {
			int res = 17;
			res = res * 37 + rule.hashCode();
			res = res * 37 + sip.hashCode();
			return res;
		}

		public boolean isRectified() {
			return rule.isRectified();
		}

		public int getHeadLenght() {
			return rule.getHeadLenght();
		}

		public ILiteral getHeadLiteral(int arg) {
			return rule.getHeadLiteral(arg);
		}

		public List<ILiteral> getHeadLiterals() {
			return Collections.unmodifiableList(rule.getHeadLiterals());
		}

		public List<IVariable> getHeadVariables() {
			return Collections.unmodifiableList(rule.getHeadVariables());
		}

		public int getBodyLenght() {
			return rule.getBodyLenght();
		}

		public ILiteral getBodyLiteral(int arg) {
			return rule.getBodyLiteral(arg);
		}

		public List<ILiteral> getBodyLiterals() {
			return Collections.unmodifiableList(rule.getBodyLiterals());
		}

		public List<IVariable> getBodyVariables() {
			return Collections.unmodifiableList(rule.getBodyVariables());
		}
	}
}
