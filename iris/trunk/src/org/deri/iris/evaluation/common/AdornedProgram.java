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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.magic.SIPImpl;

/**
 * This is a simple implementation of an adorned program. <b>NOTE: At the moment
 * this class only works with rules with one literal in the head.</b><br/><br/>
 * 
 * @author richi $Id: AdornedProgram.java,v 1.7 2006-08-08 08:32:06 darko Exp $
 * @version $Revision: 1.7 $
 */
public class AdornedProgram {

	// TODO: make a smaller empty-constant-term
	private static final ITerm EMPTY_CONSTANT_TERM = TERM.createConstant(TERM
			.createString(""));

	/** Set of all derived predicates */
	private final Set<IPredicate> deriveredPredicates = new HashSet<IPredicate>();;

	/** Set of all adorned predicates */
	private final Set<AdornedPredicate> adornedPredicates = new HashSet<AdornedPredicate>();

	/** Set of all adorned rules */
	private final Set<AdornedRule> adornedRules = new HashSet<AdornedRule>();

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
	 */
	public AdornedProgram(final Set<IRule> rules, final IQuery query) {
		// check the parameters
		if ((rules == null) || (query == null)) {
			throw new NullPointerException();
		}
		for (IRule r : rules) {
			if (r.getHeadLiterals().size() > 1) {
				throw new IllegalArgumentException("At the moment this class "
						+ "only works with rules with one literal in the head.");
			}
		}
		if (query.getQueryLenght() > 1) {
			throw new IllegalArgumentException("At the moment there are only "
					+ "queries with one literal supported");
		}

		// TODO: maybe defensive copy should be made.
		this.rules = rules;
		this.query = query;

		updateDerivedPredicates();

		// creating an adored predicate out of the query, and add it to the
		// predicate sets
		AdornedPredicate qa = new AdornedPredicate(query);
		Set<AdornedPredicate> predicatesToProcess = new HashSet<AdornedPredicate>();
		predicatesToProcess.add(qa);
		adornedPredicates.add(qa);

		// iterating through all predicates in the todolist
		while (!predicatesToProcess.isEmpty()) {
			AdornedPredicate ap = predicatesToProcess.iterator().next();
			predicatesToProcess.remove(ap);

			for (IRule r : rules) {
				ILiteral lh = r.getHeadLiteral(0);
				IPredicate ph = lh.getPredicate();

				// if the headliteral and the adorned predicate have the
				// same signature
				if (hasSameSignature(ap, ph)) {
					// creating a sip for the actual rule and the ap
					SIPImpl sip = new SIPImpl(r, createQueryForAP(ap, lh));
					AdornedRule ra = new AdornedRule(r, sip);
					ra.replaceHeadLiteral(lh, ap);
					adornedRules.add(ra);

					// iterating through all bodyliterals of the
					for (ILiteral l : r.getBodyLiterals()) {
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
	 * This returns a simple string representation of this program. <b>The
	 * subject of this representation is to change.</b> The return of this
	 * method will look something like:<br/>&lt;list of all adorned rules
	 * separated by newlines&gt;<br/><code>newline</code><br/>&lt;list of
	 * all rules separated by newlines&gt;<br/><code>newline</code><br/>&lt;the
	 * query&gt;
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
		// TODO: only the submitted query and rules are compared, because all
		// other member variables should then be the same
		return query.equals(ap.query) && (rules.size() == ap.rules.size())
				&& rules.containsAll(ap.rules);
	}

	/**
	 * Return a set of all adorned rules
	 * 
	 * @return the set of adorned rules
	 */
	public Set<AdornedRule> getAdornedRules() {
		return Collections.unmodifiableSet(adornedRules);
	}

	public Set<IRule> getNormalRules() {
		return Collections.unmodifiableSet(rules);
	}

	/**
	 * Returns a set of all adorned predicates of this program.
	 * 
	 * @return the set of adorned predicates
	 */
	public Set<AdornedPredicate> getAdornedPredicates() {
		return Collections.unmodifiableSet(adornedPredicates);
	}

	public Set<IPredicate> getDerivedPredicates() {
		return Collections.unmodifiableSet(deriveredPredicates);
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
			final AdornedRule r) {
		if ((l == null) || (r == null)) {
			throw new NullPointerException();
		}
		AdornedPredicate ap = null;
		if (deriveredPredicates.contains(l.getPredicate())) {
			ap = new AdornedPredicate(l, r.getSIP().getBoundVariables(l));
			r.replaceBodyLiteral(l, ap);
		}
		return ap;
	}

	/**
	 * Determines, whether the signature of two predicates matches. Therefore it
	 * will compare the arities and the symbold of the predicates.
	 * 
	 * @param p0
	 *            the first predicate to compare
	 * @param p1
	 *            the second predicate to compare
	 * @return true if the signature (arity and symbol) of the predicates
	 *         matches, otherwise false
	 */
	private static boolean hasSameSignature(final IPredicate p0,
			final IPredicate p1) {
		if ((p0 == null) || (p1 == null)) {
			throw new NullPointerException();
		}
		return p0.getPredicateSymbol().equals(p1.getPredicateSymbol())
				&& (p0.getArity() == p1.getArity());
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
	 *             predicate desn't match
	 */
	private static IQuery createQueryForAP(final AdornedPredicate ap,
			final ILiteral hl) {
		if ((hl == null) || (ap == null)) {
			throw new NullPointerException();
		}
		if (hl.getPredicate().getArity() != ap.getArity()) {
			throw new IllegalArgumentException("The arity of the predicate of "
					+ "the literal and the adorned predicate must match");
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
					assert true : "onyl BOUND and FREE are supported";
					break;
			}
			iCounter++;
		}
		return BASIC.createQuery(BASIC.createLiteral(hl.isPositive(), ap, BASIC
				.createTuple(terms)));
	}

	public static class AdornedPredicate implements IPredicate, AdornedElement {
		/** The predicate which should be adorned */
		private final IPredicate p;

		/** The adornment of the predicate */
		private final Adornment[] adornment;

		public AdornedPredicate(final String symbol, final int arity,
				final Adornment[] adornment) {
			if ((adornment == null) || (symbol == null)) {
				throw new NullPointerException();
			}
			if (adornment.length != arity) {
				throw new IllegalArgumentException(
						"The length of the adornment "
								+ "and the arity of the predicate doesn't match.");
			}
			this.p = BASIC.createPredicate(symbol, arity);
			this.adornment = new Adornment[adornment.length];
			System.arraycopy(adornment, 0, this.adornment, 0, adornment.length);

			// prechecking the adornmetns
			for (Adornment a : this.adornment) {
				if (a == null) {
					throw new IllegalArgumentException("The adornment array "
							+ "was corrupted (contained null).");
				}
			}
		}

		public AdornedPredicate(final ILiteral l, final Set<IVariable> bounds) {
			// TODO: maybe a defensive copy should be made
			p = l.getPredicate();

			// checking the arguments
			if ((p == null) || (bounds == null)) {
				throw new NullPointerException();
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

		public AdornedPredicate(final IQuery q) {
			// TODO: maybe a defensive copy should be made
			final ILiteral literal = q.getQueryLiteral(0);
			this.p = literal.getPredicate();

			// checking the submitted values
			if (p == null) {
				throw new NullPointerException();
			}

			int iCounter = 0;
			// computing the adornment
			adornment = new Adornment[p.getArity()];
			for (Object t : literal.getTuple().getTerms()) {
				if (t instanceof IVariable) {
					adornment[iCounter] = Adornment.FREE;
				} else {
					adornment[iCounter] = Adornment.BOUND;
				}
				iCounter++;
			}
		}

		public Adornment[] getAdornment() {
			return adornment;
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
		 */
		public void setBuiltIn(boolean arg) {
			throw new UnsupportedOperationException();
		}

		/**
		 * This method is not meant to be invoked.
		 * 
		 * @throws UnsupportedOperationException
		 *             will be thrown every time.
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
	}

	/**
	 * @deprecated this is a dirty hack - IRule should be cloneable
	 * @author richi
	 */
	public static class AdornedRule implements IRule {
		// TODO: implement hashCode and equals

		private final IRule originalRule;

		private final SIPImpl sip;

		private final Map<Integer, ILiteral> headReplacements = new HashMap<Integer, ILiteral>();

		private final Map<Integer, ILiteral> bodyReplacements = new HashMap<Integer, ILiteral>();

		public AdornedRule(final IRule r, final SIPImpl s) {
			// checking arguments
			if ((r == null) || (s == null)) {
				throw new NullPointerException();
			}
			// TODO: a defensive copy should be made
			originalRule = r;
			sip = s;
		}

		public SIPImpl getSIP() {
			return sip;
		}

		public void replaceHeadLiteral(final ILiteral l, final IPredicate p) {
			replaceHeadLiteral(originalRule.getHeadLiterals().indexOf(l), p);
		}

		public void replaceBodyLiteral(final ILiteral l, final IPredicate p) {
			replaceBodyLiteral(originalRule.getBodyLiterals().indexOf(l), p);
		}

		public void replaceHeadLiteral(final int pos, final IPredicate p) {
			ILiteral l = getHeadLiteral(pos);
			headReplacements.put(pos, BASIC.createLiteral(l.isPositive(), p,
					BASIC.createTuple(l.getTuple().getTerms())));
		}

		public void replaceBodyLiteral(final int pos, final IPredicate p) {
			ILiteral l = getBodyLiteral(pos);
			bodyReplacements.put(pos, BASIC.createLiteral(l.isPositive(), p,
					BASIC.createTuple(l.getTuple().getTerms())));
		}

		public String toString() {
			StringBuilder buffer = new StringBuilder();
			for (ILiteral l : getHeadLiterals()) {
				buffer.append(l).append(" ");
			}
			buffer.append(":- ");
			for (ILiteral l : getBodyLiterals()) {
				buffer.append(l).append(" ");
			}
			return buffer.substring(0, buffer.length() - 1);
		}

		public boolean isBuiltIn() {
			return originalRule.isBuiltIn();
		}

		public boolean isCycled() {
			return originalRule.isCycled();
		}

		public boolean isFact() {
			return originalRule.isFact();
		}

		public boolean isRectified() {
			return originalRule.isRectified();
		}

		public boolean isSafe() {
			return originalRule.isSafe();
		}

		public int getHeadLenght() {
			return originalRule.getHeadLenght();
		}

		public ILiteral getHeadLiteral(int arg) {
			ILiteral l = null;
			if ((l = headReplacements.get(arg)) != null) {
				return l;
			}
			return originalRule.getHeadLiteral(arg);
		}

		public List<ILiteral> getHeadLiterals() {
			List<ILiteral> l = new ArrayList<ILiteral>(originalRule
					.getHeadLenght());
			for (int iCounter = 0; iCounter < originalRule.getHeadLenght(); iCounter++) {
				l.add(iCounter, getHeadLiteral(iCounter));
			}
			return l;
		}

		public List<IVariable> getHeadVariables() {
			return originalRule.getHeadVariables();
		}

		public int getBodyLenght() {
			return originalRule.getBodyLenght();
		}

		public ILiteral getBodyLiteral(int arg) {
			ILiteral l = null;
			if ((l = bodyReplacements.get(arg)) != null) {
				return l;
			}
			return originalRule.getBodyLiteral(arg);
		}

		public List<ILiteral> getBodyLiterals() {
			List<ILiteral> l = new ArrayList<ILiteral>(originalRule
					.getBodyLenght());
			for (int iCounter = 0; iCounter < originalRule.getBodyLenght(); iCounter++) {
				l.add(iCounter, getBodyLiteral(iCounter));
			}
			return l;
		}

		public List<IVariable> getBodyVariables() {
			return originalRule.getBodyVariables();
		}

	}
}
