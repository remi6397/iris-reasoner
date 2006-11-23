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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.evaluation.common.IAdornedPredicate;
import org.deri.iris.api.evaluation.common.IAdornedProgram;
import org.deri.iris.api.evaluation.common.IAdornedRule;
import org.deri.iris.api.evaluation.magic.ISip;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.magic.SIPImpl;

/**
 * <p>
 * This is a simple implementation of an adorned program. <b>NOTE: At the moment
 * this class only works with rules with one literal in the head.</b>
 * </p>
 * <p>
 * $Id: AdornedProgram.java,v 1.17 2006-11-23 12:45:05 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.17 $
 * @date $Date: 2006-11-23 12:45:05 $
 */
public class AdornedProgram implements IAdornedProgram {

	// TODO: make a smaller empty-constant-term
	private static final ITerm EMPTY_CONSTANT_TERM = TERM.createString("");

	/** Set of all derived predicates */
	private final Set<IPredicate> deriveredPredicates = new HashSet<IPredicate>();

	/** Set of all adorned predicates */
	private final Set<IAdornedPredicate> adornedPredicates = new HashSet<IAdornedPredicate>();

	/** Set of all adorned rules */
	private final Set<IAdornedRule> adornedRules = new HashSet<IAdornedRule>();

	/** Set of all normal rules */
	private final Set<IRule> rules;

	/** Query for this program */
	private final IQuery query;

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
	public AdornedProgram(final Set<IRule> rules, final IQuery query) {
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
		if (query.getQueryLenght() != 1) {
			throw new IllegalArgumentException("At the moment there are only "
					+ "queries with one literal supported");
		}

		// TODO: maybe defensive copy should be made.
		this.rules = rules;
		this.query = query;

		updateDerivedPredicates();

		// creating an adored predicate out of the query, and add it to the
		// predicate sets
		AdornedPredicate qa = new AdornedPredicate(query.getQueryLiteral(0));
		Set<AdornedPredicate> predicatesToProcess = new HashSet<AdornedPredicate>();
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
					adornedRules.add(ra);

					// iterating through all bodyliterals of the
					for (final ILiteral l : r.getBodyLiterals()) {
						final AdornedPredicate newAP = processLiteral(l, ra);
						// adding the adorned predicate to the sets
						if ((newAP != null) && (adornedPredicates.add(newAP))) {
							predicatesToProcess.add(newAP);
						}
					}
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
		for (IAdornedRule r : adornedRules) {
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
		// TODO: only the submitted query and rules are taken into account,
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
		// only the submitted query and rules are compared, because all
		// other member variables should then be the same, because the're
		// computed out of them
		return query.equals(ap.query) && rules.equals(ap.rules);
	}

	public Set<IAdornedRule> getAdornedRules() {
		return Collections.unmodifiableSet(adornedRules);
	}

	public Set<IRule> getNormalRules() {
		return Collections.unmodifiableSet(rules);
	}

	public Set<IAdornedPredicate> getAdornedPredicates() {
		return Collections.unmodifiableSet(adornedPredicates);
	}

	public IQuery getQuery() {
		return query;
	}

	/**
	 * Processes a literal of a given adorned rule.<br/> This method will frist
	 * determine, whether the predicate of this literal is derived, and if it is
	 * the predicate will be adorned with the variables passed in from the sip
	 * of the adorned rule. The predicate of the literal of the adorned rule
	 * passed in will be replaced by the adorned one. If the predicate was
	 * derived, the adorned predicate will be returned, otherwise null.
	 * 
	 * @param l
	 *            the literal to process
	 * @param r
	 *            the adorned rule containing the literal
	 * @return the adorned predicate for this literal corresponding to the
	 *         passed variables of the adorned rule, or null, if the predicate
	 *         of the literal wasn't derived.
	 * @throws NullPointerException
	 *             if the rule, or the literal are null
	 */
	private AdornedPredicate processLiteral(final ILiteral l,
			final IAdornedRule r) {
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
	 */
	private void updateDerivedPredicates() {
		for (IRule r : rules) {
			deriveredPredicates.add(r.getHeadLiteral(0).getPredicate());
		}
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
	public static class AdornedPredicate implements IAdornedPredicate {
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
		 * @throw nullpointer exception if the literal, the predicate of the
		 *        literal or the bounds is null.
		 */
		public AdornedPredicate(final ILiteral l, final Set<IVariable> bounds) {
			// TODO: maybe a defensive copy should be made
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
			for (Object t : l.getTuple().getTerms()) {
				if (bounds.contains(t)) {
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
			if (l == null) {
				throw new NullPointerException("The literal must not be null");
			}

			// TODO: maybe a defensive copy should be made
			this.p = l.getPredicate();

			// checking the submitted values
			if (p == null) {
				throw new NullPointerException(
						"The predicate of the literal must not be null");
			}

			int iCounter = 0;
			// computing the adornment
			adornment = new Adornment[p.getArity()];
			for (ITerm t : (List<ITerm>) l.getTuple().getTerms()) {
				if (t.isGround()) {
					adornment[iCounter] = Adornment.BOUND;
				} else {
					adornment[iCounter] = Adornment.FREE;
				}
				iCounter++;
			}
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

		public boolean isBuiltIn() {
			return p.isBuiltIn();
		}

		/**
		 * This method is not meant to be invoked.
		 * 
		 * @throws UnsupportedOperationException
		 *             will be thrown every time.
		 * @see #isBuiltIn()
		 */
		public void setBuiltIn(boolean arg) {
			throw new UnsupportedOperationException();
		}

		/**
		 * This method is not meant to be invoked.
		 * 
		 * @throws UnsupportedOperationException
		 *             will be thrown every time.
		 * @see #getPredicateSymbol()
		 */
		public void setPredicateSymbol(String name) {
			throw new UnsupportedOperationException();
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

		public int getStratum() {
			return p.getStratum();
		}

		public int setStratum(int s) {
			return p.setStratum(s);
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
	 * @version $Revision: 1.17 $
	 * @date $Date: 2006-11-23 12:45:05 $
	 */
	public static class AdornedRule implements IAdornedRule {
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

		public boolean isBuiltIn() {
			return rule.isBuiltIn();
		}

		public boolean isCycled() {
			return rule.isCycled();
		}

		public boolean isFact() {
			return rule.isFact();
		}

		public boolean isRectified() {
			return rule.isRectified();
		}

		public boolean isSafe() {
			return rule.isSafe();
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
