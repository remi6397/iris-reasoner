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
package org.deri.iris.evaluation.basics;

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
 * this class only works with rules with one literal in the head.</b> </br></br>$Id: AdornedProgram.java,v 1.1 2006-07-19 08:22:34 darko Exp $
 * 
 * @author richi
 * @version $Revision: 1.1 $
 */
public class AdornedProgram {

	// TODO: make a smaller empty-constant-term
	private static final ITerm EMPTY_CONSTANT_TERM = TERM.createConstant(TERM
			.createString(""));

	private final Set<IPredicate> deriveredPredicates = new HashSet<IPredicate>();;

	private final Set<AdornedPredicate> adornedPredicates = new HashSet<AdornedPredicate>();

	private final Set<AdornedRule> adornedRules = new HashSet<AdornedRule>();

	private final Set<IRule> rules;

	private final IQuery query;

	public AdornedProgram(final Set<IRule> rules, final IQuery query) {

		// check the parameters
		if (rules == null) {
			throw new IllegalArgumentException("The rules must not be null");
		}
		if (query == null) {
			throw new IllegalArgumentException("The query must not be null");
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
						AdornedPredicate newAP = processLiteral(l, ra);
						// adding the adorned predicate to the sets
						if ((newAP != null) && (adornedPredicates.add(newAP))) {
							predicatesToProcess.add(newAP);
						}
					}
				}
			}
		}
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		for (AdornedRule r : adornedRules) {
			buffer.append(r).append("\n");
		}
		buffer.append("\n");
		for (IRule r : rules) {
			buffer.append(r).append("\n");
		}
		buffer.append("\n");
		buffer.append(query);
		return buffer.toString();
	}

	public int hashCode() {
		// TODO: only the submitted query and rules are taken into account,
		// because all other member variables should then be the same
		int result = 17;
		result = result * 37 + query.hashCode();
		for (IRule r : rules) {
			result = result * 37 + r.hashCode();
		}
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
	
	public Set<AdornedRule> getAdornedRules() {
		return Collections.unmodifiableSet(adornedRules);
	}
	
	public Set<IRule> getNormalRules() {
		return Collections.unmodifiableSet(rules);
	}
	
	public Set<AdornedPredicate> getAdornedPredicates() {
		return Collections.unmodifiableSet(adornedPredicates);
	}

	public Set<IPredicate> getDerivedPredicates() {
		return Collections.unmodifiableSet(deriveredPredicates);
	}
	
	public IQuery getQuery() {
		return query;
	}
	
	private AdornedPredicate processLiteral(final ILiteral l,
			final AdornedRule r) {
		AdornedPredicate ap = null;
		if (deriveredPredicates.contains(l.getPredicate())) {
			ap = new AdornedPredicate(l, r.getSIP().getBoundVariables(l));
			r.replaceBodyLiteral(l, ap);
		}
		return ap;
	}

	private static boolean hasSameSignature(final IPredicate p0,
			final IPredicate p1) {
		return p0.getPredicateSymbol().equals(p1.getPredicateSymbol())
				&& (p0.getArity() == p1.getArity());
	}

	private void updateDerivedPredicates() {
		for (IRule r : rules) {
			deriveredPredicates.add(r.getHeadLiteral(0).getPredicate());
		}
	}

	private static IQuery createQueryForAP(final AdornedPredicate ap,
			final ILiteral hl) {
		ITerm[] terms = new ITerm[ap.getArity()];
		int iCounter = 0;
		for (Adornment a : ap.getAdornment()) {
			switch (a) {
				case BOUND:
					terms[iCounter] = EMPTY_CONSTANT_TERM;
					break;
				case FREE:
					terms[iCounter] = hl.getTerm(iCounter);
					break;
				default:
					break;
			}
			iCounter++;
		}
		return BASIC.createQuery(BASIC
				.createLiteral(hl.isPositive(), ap, terms));
	}

	public static class AdornedPredicate implements IPredicate, AdornedElement {
		/** The predicate which should be adorned */
		private final IPredicate p;

		/** The adornment of the predicate */
		private final Adornment[] adornment;

		public AdornedPredicate(final String symbol, final int arity,
				final Adornment[] adornment) {
			if (adornment == null) {
				throw new IllegalArgumentException(
						"The adornment must not be null");
			}
			this.p = BASIC.createPredicate(symbol, arity);
			// TODO: maybe a defensive copy should be made
			this.adornment = adornment;
		}

		public AdornedPredicate(final ILiteral l, final Set<IVariable> bounds) {
			int iCoutner = 0;

			// checking the arguments
			if (l == null) {
				throw new IllegalArgumentException(
						"The literal must not be null");
			}
			if (bounds == null) {
				throw new IllegalArgumentException("The bound variables"
						+ " must not be null");
			}

			// TODO: maybe a defensive copy should be made
			p = l.getPredicate();
			adornment = new Adornment[p.getArity()];

			// computing the adornment
			for (Object t : l.getTerms()) {
				if (bounds.contains(t)) {
					adornment[iCoutner] = Adornment.BOUND;
				} else {
					adornment[iCoutner] = Adornment.FREE;
				}
				iCoutner++;
			}
		}

		public AdornedPredicate(final IQuery q) {
			final ILiteral literal;
			int iCounter = 0;

			// checking the submitted values
			if (q == null) {
				throw new IllegalArgumentException("The query must not be null");
			}
			if ((literal = q.getQueryLiteral(0)) == null) {
				throw new IllegalArgumentException("There must be at least one"
						+ " literal in the query");
			}

			// TODO: maybe a defensive copy should be made
			this.p = literal.getPredicate();

			// computing the adornment
			adornment = new Adornment[p.getArity()];
			for (Object t : literal.getTerms()) {
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

		public void setBuiltIn(boolean arg) {
			// the puropse of this predicate is not to change
		}

		public void setPredicateSymbol(String name) {
			// the puropse of this predicate is not to change
		}

		public int compareTo(IPredicate o) {
			return p.compareTo(o);
		}

		public int hashCode() {
			int result = 17;
			result = result * 37 + p.hashCode();
			for (Adornment a : adornment) {
				result = result * 37 + a.hashCode();
			}
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
			if (r == null) {
				throw new IllegalArgumentException("The rule must not be null");
			}
			if (s == null) {
				throw new IllegalArgumentException("The sip must not be null");
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
			headReplacements.put(pos, BASIC.createLiteral(l.isPositive(), p, l
					.getTerms()));
		}

		public void replaceBodyLiteral(final int pos, final IPredicate p) {
			ILiteral l = getBodyLiteral(pos);
			bodyReplacements.put(pos, BASIC.createLiteral(l.isPositive(), p, l
					.getTerms()));
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
