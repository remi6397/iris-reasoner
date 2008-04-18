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
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

import org.deri.iris.optimisations.magicsets.ISip;
import org.deri.iris.optimisations.magicsets.LeftToRightSip;

/**
 * <p>
 * This is a simple implementation of an adorned program. <b>NOTE: At the moment
 * this class only works with rules with one literal in the head.</b>
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at sti2 dot at)
 */
public class AdornedProgram {

	// TODO: make a smaller empty-constant-term
	private static final ITerm EMPTY_CONSTANT_TERM = TERM.createString("");

	/**
	 * Set of all derived predicates. Derived predicates are predicates for
	 * which rules exist defining them.
	 */
	private final Set<IPredicate> deriveredPredicates = new HashSet<IPredicate>();

	/** Set of all adorned predicates. */
	private final Set<AdornedPredicate> adornedPredicates = new HashSet<AdornedPredicate>();

	/** Set of all adorned rules. */
	private final Set<AdornedRule> adornedRules = new HashSet<AdornedRule>();

	/** Set of all normal rules. */
	private final Set<IRule> rules;

	/** Query for this program. */
	private final IQuery query;

	/**
	 * Predicate symbol for the temporary literal for conjunctive queries.
	 */
	private static final String TEMP_QUERY_LITERAL_SYMBOL = "TEMP_QUERY_LITERAL";

	/** Temporary query literal for conjunctive queries. */
	private static final ILiteral TEMP_QUERY_LITERAL = BASIC.createLiteral(
			true, 
			BASIC.createAtom(
				BASIC.createPredicate(TEMP_QUERY_LITERAL_SYMBOL, 0),
				BASIC.createTuple(new ArrayList<ITerm>())));

	/** Adorned predicate for the temporary query literal. */
	private static final AdornedPredicate AD_TEMP_QUERY_PREDICATE = 
		new AdornedPredicate(TEMP_QUERY_LITERAL);

	/**
	 * Creates a new adorned program depending on the submitted rules and the
	 * query.
	 * 
	 * @param rules for which to create the program
	 * @param query for the program
	 * @throws IllegalArgumentException if the rules are <code>null</code>
	 * @throws IllegalArgumentException if the queries are <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the size of the head literals, or of the query literals is
	 *             bigger than 1
	 * @throws IllegalArgumentException
	 *             if the list of rules contains null
	 */
	public AdornedProgram(final Collection<IRule> rules, final IQuery query) {
		// check the parameters
		if (rules == null) {
			throw new IllegalArgumentException("The rules must not be null");
		}
		if (query == null) {
			throw new IllegalArgumentException("The query must not be null");
		}
		if (rules.contains(null)) {
			throw new IllegalArgumentException(
					"The list of rules must not contain null");
		}
		for (IRule r : rules) {
			if (r.getHead().size() != 1) {
				throw new IllegalArgumentException("At the moment this class "
						+ "only works with rules with one literal in the head.");
			}
		}

		IQuery newQuery = null; // if we can adorn the query save it here

		if (query.getLiterals().size() > 1) { // if we got a conjunctive query
			// construct the temp query and rule so that we have a
			// query with only one literal in it
			final IRule tmpRule = BASIC.createRule(
					Arrays.asList(new ILiteral[]{TEMP_QUERY_LITERAL}), 
					query.getLiterals());
			final IQuery tmpQuery = BASIC.createQuery(TEMP_QUERY_LITERAL);

			// adorn the new program
			final Set<IRule> modRules = new HashSet<IRule>(rules);
			modRules.add(tmpRule);
			createAdornedRules(modRules, tmpQuery);

			// remove the temp rule again and create the query out
			// of it
			for (final AdornedRule r : adornedRules) {
				if ((r.getRule().getHead().size() == 1) && 
						(r.getRule().getHead().get(0).getAtom().getPredicate().equals(AD_TEMP_QUERY_PREDICATE))) {
					adornedRules.remove(r);
					newQuery = BASIC.createQuery(r.getRule().getBody());
					break;
				}
			}
		} else { // handle non-conjunctive query
			// adorn the original program
			createAdornedRules(rules, query);

			// construct the new query with the adorned predicate
			final ILiteral ql = query.getLiterals().get(0);
			final AdornedPredicate ap = new AdornedPredicate(ql);
			newQuery = BASIC.createQuery(BASIC.createLiteral(
						ql.isPositive(), 
						BASIC.createAtom(ap, BASIC.createTuple(ql.getAtom().getTuple()))));

		}

		// TODO: maybe defensive copy should be made.
		this.rules = new HashSet<IRule>(rules);
		this.query = newQuery;
	}

	/**
	 * Iterates over all the rules an checks what can be adorned. <b>The
	 * query must only contain one literal!</b>
	 * @param rules the rules to adorn
	 * @param query the query from where to take the bounds from
	 */
	private void createAdornedRules(final Collection<IRule> rules, final IQuery query) {
		assert rules != null: "The rules must not be null";
		assert !rules.contains(null): "The rules must not contain null";
		assert query != null: "The query must not be null";
		assert query.getLiterals().size() == 1: "The query must only contain one literal";

		deriveredPredicates.addAll(updateDerivedPredicates(rules)); // TODO: i think this should go

		// creating an adorned predicate out of the query, and add it to the
		// predicate sets
		final AdornedPredicate qa = new AdornedPredicate(query.getLiterals().get(0));

		final Set<AdornedPredicate> predicatesToProcess = new HashSet<AdornedPredicate>();
		predicatesToProcess.add(qa);
		adornedPredicates.add(qa);

		// iterating through all predicates in the todolist
		while (!predicatesToProcess.isEmpty()) {
			final AdornedPredicate ap = predicatesToProcess.iterator().next();
			predicatesToProcess.remove(ap);

			for (final IRule r : rules) {
				final ILiteral lh = r.getHead().get(0);
				final IPredicate ph = lh.getAtom().getPredicate();

				// if the headliteral and the adorned predicate have the
				// same signature
				if (ap.hasSameSignature(ph)) {
					// creating a sip for the actual rule and the ap
					final ISip sip = new LeftToRightSip(r, createQueryForAP(ap, lh));
					AdornedRule ra = (new AdornedRule(r, sip)).replaceHeadLiteral(lh, ap);

					// iterating through all body literals of the rule
					for (final ILiteral l : r.getBody()) {
						final AdornedPredicate newAP = checkDerivedLiteral(l, ra);
						if (newAP != null) {
							// replacing the literal in the rule
							ra = ra.replaceBodyLiteral(l, newAP);
							// adding the adorned predicate to the sets
							if (adornedPredicates.add(newAP)) {
								predicatesToProcess.add(newAP);
							}
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
		StringBuilder buffer = new StringBuilder();
		for (AdornedRule r : adornedRules) {
			buffer.append(r).append(System.getProperty("line.separator"));
		}
		buffer.append(System.getProperty("line.separator"));
		for (IRule r : rules) {
			buffer.append(r).append(System.getProperty("line.separator"));
		}
		buffer.append(System.getProperty("line.separator"));
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
	 * Checks whether the predicate of a literal is a derived one. If the
	 * literal is derived it will return the adorned predicate. The adorned
	 * predicate will be adorned according to it's bound and frees of the
	 * sip of the submitted rule. If the predicate is not adorned
	 * <code>null</code> will be returned.
	 * </p>
	 * @param l the literal to process
	 * @param r the adorned rule containing the literal
	 * @return the adorned predicate for this literal corresponding to the
	 *         passed variables of the adorned rule, or {@code null}, if the
	 *         predicate of the literal wasn't derived.
	 */
	private AdornedPredicate checkDerivedLiteral(final ILiteral l,
			final AdornedRule r) {
		assert l != null: "The literal must not be null";
		assert r != null: "The rule must not be null";

		AdornedPredicate ap = null;
		final IAtom a = l.getAtom();
		if (deriveredPredicates.contains(a.getPredicate())) {
			ap = new AdornedPredicate(a, r.getSip().getBoundVariables(l));
		}
		return ap;
	}

	/**
	 * Determines all derived predicates of the program. Derived predicates
	 * are predicates for which rules exist defining them.
	 * @param rules rules from where to check which predicates are derived
	 * @return the derived predicates
	 */
	private static Set<IPredicate> updateDerivedPredicates(final Collection<IRule> rules) {
		assert rules != null: "The rules must not be null";
		assert !rules.contains(null): "The rules must not contain null";

		final Set<IPredicate> derived = new HashSet<IPredicate>();
		for (final IRule r : rules) {
			for (final ILiteral l : r.getHead()) {
				derived.add(l.getAtom().getPredicate());
			}
		}
		return derived;
	}

	/**
	 * Creates a query out of an adorned predicate and a literal. Therefore it
	 * creates a literal with the specified arity, puts constant terms at
	 * the positions of the bound arguments and for the free arguments the
	 * terms of the literal at the corresponding position.
	 * 
	 * @param ap the adorned predicate from where to take the bound's and
	 * free's
	 * @param hl the literal for which to create the query
	 * @return the query
	 */
	private static IQuery createQueryForAP(final AdornedPredicate ap,
			final ILiteral hl) {
		assert hl != null: "The head literal must not be null";
		assert ap != null: "The adorned predicate must not be null";
		assert hl.getAtom().getPredicate().getArity() == ap.getArity():
			"The arity of the predicate of the literal and the adorned predicate be equal";

		ITerm[] terms = new ITerm[ap.getArity()];
		int iCounter = 0;
		for (Adornment a : ap.getAdornment()) {
			switch (a) {
			case BOUND:
				terms[iCounter] = EMPTY_CONSTANT_TERM;
				break;
			case FREE:
				terms[iCounter] = hl.getAtom().getTuple().get(iCounter);
				break;
			default:
				assert false: "Only BOUND and FREE are allowed as adornments";
			}
			iCounter++;
		}
		return BASIC.createQuery(BASIC.createLiteral(hl.isPositive(), ap, BASIC
				.createTuple(terms)));
	}

	/**
	 * <p>
	 * Represents an adorned predicate. A adorned predicate is a predicate
	 * with an array of adornments attached to it denoting the bound and
	 * free arguments of it's tuple's terms
	 * </p>
	 * 
	 * @author Richard Pöttler (richard dot poettler at sti2 dot at)
	 */
	public static class AdornedPredicate implements IPredicate {
		/** The base predicate which is represented as adorned one. */
		private final IPredicate predicate;

		/** The adornment of the predicate */
		private final Adornment[] adornment;

		/**
		 * Constructs an adorned predicate.
		 * 
		 * @param symbol symbol for the predicate
		 * @param adornment the array of bound and frees
		 * @throws IllegalArgumentException if the adornment is
		 * <code>null</code>
		 * @throws IllegalArgumentException if the predicate symbol is
		 * <code>null</code>
		 * @throws IllegalArgumentException if the adornment contains
		 * <code>null</code>
		 */
		public AdornedPredicate(final String symbol, final Adornment[] adornment) {
			this(symbol, adornment.length, adornment);
		}

		/**
		 * Constructs an adorned predicate.
		 * 
		 * @param symbol symbol for the predicate
		 * @param arity the arity of the predicate.
		 * @param adornment the array of bound and frees
		 * @throws IllegalArgumentException if the adornment is
		 * <code>null</code>
		 * @throws IllegalArgumentException if the predicate symbol is
		 * <code>null</code>
		 * @throws IllegalArgumentException if the adornment contains
		 * <code>null</code>
		 * @throws IllegalArgumentException if the arity of the
		 * predicate is bigger than the length of the adornment
		 */
		public AdornedPredicate(final String symbol, final int arity,
				final Adornment[] adornment) {
			if (adornment == null) {
				throw new IllegalArgumentException("The adornment must not be null");
			}
			if (symbol == null) {
				throw new IllegalArgumentException("The predicate symbol must not be null");
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
			this.predicate = BASIC.createPredicate(symbol, arity);
			this.adornment = new Adornment[adornment.length];
			System.arraycopy(adornment, 0, this.adornment, 0, adornment.length);
		}

		/**
		 * Constructs an adorned predicate out of an atom and its bound
		 * variables. All occurrences in the atom of the bound variables will
		 * be marked as bound in the adornment.
		 * 
		 * @param a the atom
		 * @param bounds collection of all bound variables of the atom
		 * @throws IllegalArgumentException if the atom is <code>null</code>
		 */
		public AdornedPredicate(final IAtom a, final Collection<IVariable> bounds) {
			if (a == null) {
				throw new IllegalArgumentException("The Atom must not be null");
			}

			predicate = a.getPredicate();

			final Collection<IVariable> boundVars = (bounds == null)
				? Collections.<IVariable>emptySet()
				: bounds;

			// computing the adornment
			adornment = new Adornment[predicate.getArity()];
			int i = 0;
			for (final ITerm t : a.getTuple()) {
				if (isBound(t, boundVars)) {
					adornment[i] = Adornment.BOUND;
				} else {
					adornment[i] = Adornment.FREE;
				}
				i++;
			}
		}

		/**
		 * Constructs an adorned predicate out of a literal. All ground terms
		 * will be marked as bound.
		 * 
		 * @param l for which to construct the adorned predicate
		 * @throws IllegalArgumentException if literal's the atom is <code>null</code>
		 */
		public AdornedPredicate(final ILiteral l) {
			this(l.getAtom());
		}

		/**
		 * Constructs an adorned predicate out of an atom. All ground terms
		 * will be marked as bound.
		 * 
		 * @param a the atom
		 * @throws IllegalArgumentException if the atom is <code>null</code>
		 */
		public AdornedPredicate(final IAtom a) {
			this(a, null);
		}

		/**
		 * Checks whether a term is bound with a submitted collection of bound
		 * variables. This method also checks whether all variables are covered
		 * with the variables of the bound collection.
		 * 
		 * @param t
		 *            the term to check
		 * @param b
		 *            the collection of bound variables to check against
		 * @return {@code true} if the term is bound using the bound collection,
		 *         otherwise {@code false}
		 */
		private static boolean isBound(final ITerm t,
				final Collection<IVariable> b) {
			assert t != null: "The term must not be null";
			assert b != null: "The collection of bound variables must not be null";

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
				throw new IllegalArgumentException("The predicate must not be null");
			}
			return (pred.getArity() == predicate.getArity())
					&& (pred.getPredicateSymbol()
							.equals(predicate.getPredicateSymbol()));
		}

		public IPredicate getUnadornedPredicate() {
			return predicate;
		}

		public Adornment[] getAdornment() {
			Adornment[] copy = new Adornment[adornment.length];
			System.arraycopy(adornment, 0, copy, 0, adornment.length);
			return copy;
		}

		public int getArity() {
			return predicate.getArity();
		}

		public String getPredicateSymbol() {
			return predicate.getPredicateSymbol();
		}

		public int compareTo(IPredicate o) {
			return predicate.compareTo(o);
		}

		public int hashCode() {
			int result = 17;
			result = result * 37 + predicate.hashCode();
			result = result * 37 + Arrays.hashCode(adornment);
			return result;
		}

		public String toString() {
			final StringBuilder buffer = new StringBuilder();
			buffer.append(predicate).append("^");
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
			return predicate.equals(p.predicate) && Arrays.equals(adornment, p.adornment);
		}
	}

	/**
	 * <p>
	 * Simple representation of an adorned rule. The only difference to an
	 * ordinary rule is, that it has a sip attached, and that you can exchange
	 * literals.
	 * </p>
	 * <b>ATTENTION: the replaceHeadLiteral and replaceBodyLiteral are slow,
	 * because they copy the head and body for each invocation.</b>
	 * </p>
	 * 
	 * @author Richard Pöttler (richard dot poettler at sti2 dot at)
	 */
	public static class AdornedRule { //implements IRule {
		/** The inner rule represented by this object */
		private final IRule rule;

		/** The sip for this rule */
		private final ISip sip;

		/**
		 * Constructs a new adorned rule.
		 * 
		 * @param r the rule
		 * @param s the sip for this rule
		 * @throws IllegalArgumentException if the rule is
		 * <code>null</code>
		 * @throws IllegalArgumentException if the sip is
		 * <code>null</code>
		 */
		public AdornedRule(final IRule r, final ISip s) {
			if (r == null) {
				throw new IllegalArgumentException("The rule must not be null");
			}
			if (s == null) {
				throw new IllegalArgumentException("The sip must not be null");
			}
			rule = r;
			sip = s;
		}
		
		/**
		 * Returns the sip associated with this rule.
		 * @return the rule
		 */
		public ISip getSip() {
			return sip;
		}

		/**
		 * Returns the plain rule object.
		 * @return the plain rule object
		 */
		public IRule getRule() {
			return rule;
		}

		/**
		 * Replaces the predicate of a given head literal with another
		 * one. This method doesn't change the object itself, but
		 * creates another one, applies the changes and returns the
		 * modified rule object.
		 * @param l the literal, for which to exchange the predicate
		 * @param p the new predicate which should be set
		 * @return a new rule with the applied changes, or the same rule
		 * again, if the literal couldn't be found
		 * @throws IllegalArgumentException if the literal is <code>null</code>
		 * @throws IllegalArgumentException if the predicate is <code>null</code>
		 * @throws IllegalArgumentException if the arity of the
		 * predicate of the literal and the new predicate doesn't match.
		 */
		public AdornedRule replaceHeadLiteral(final ILiteral l, final IPredicate p) {
			if (l == null) {
				throw new IllegalArgumentException("The literal must not be null");
			}
			if (p == null) {
				throw new IllegalArgumentException("The predicate must not be null");
			}
			if (l.getAtom().getPredicate().getArity() != p.getArity()) {
				throw new IllegalArgumentException(
						"The arities of the predicate of the literal "
								+ "and the new predicate doesn't match.");
			}

			final List<ILiteral> head = new ArrayList<ILiteral>(rule.getHead());

			final int index = head.indexOf(l);
			if (index == -1) {
				return this;
			}

			head.set(index, BASIC .createLiteral(l.isPositive(), p, l.getAtom().getTuple()));

			final IRule r = BASIC.createRule(head, rule.getBody());
			final ISip s = new LeftToRightSip(r);
			return new AdornedRule(r, s);
		}

		/**
		 * Replaces the predicate of a given body literal with another
		 * one. This method doesn't change the object itself, but
		 * creates another one, applies the changes and returns the
		 * modified rule object.
		 * @param l the literal, for which to exchange the predicate
		 * @param p the new predicate which should be set
		 * @return a new rule with the applied changes, or the same rule
		 * again, if the literal couldn't be found
		 * @throws IllegalArgumentException if the literal is <code>null</code>
		 * @throws IllegalArgumentException if the predicate is <code>null</code>
		 * @throws IllegalArgumentException if the arity of the
		 * predicate of the literal and the new predicate doesn't match.
		 */
		public AdornedRule replaceBodyLiteral(final ILiteral l, final IPredicate p) {
			if (l == null) {
				throw new IllegalArgumentException("The literal must not be null");
			}
			if (p == null) {
				throw new IllegalArgumentException("The predicate must not be null");
			}
			if (l.getAtom().getPredicate().getArity() != p.getArity()) {
				throw new IllegalArgumentException(
						"The arities of the predicate of the literal "
								+ "and the new predicate doesn't match.");
			}

			final List<ILiteral> body = new ArrayList<ILiteral>(rule.getBody());

			final int index = body.indexOf(l);
			if (index == -1) {
				return this;
			}

			body.set(index, BASIC .createLiteral(l.isPositive(), p, l.getAtom().getTuple()));

			final IRule r = BASIC.createRule(rule.getHead(), body);
			final ISip s = new LeftToRightSip(r);
			return new AdornedRule(r, s);
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
	}
}
