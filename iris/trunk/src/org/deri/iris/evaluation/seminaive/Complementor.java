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
package org.deri.iris.evaluation.seminaive;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.MiscOps;
import org.deri.iris.factory.Factory;
import org.deri.iris.storage.Relation;

/**
 * <p>Computes the complement of a relation</p>
 * <p>$Id: Complementor.java,v 1.9 2007-02-28 14:35:06 poettler_ric Exp $</p>
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.9 $
 */
public class Complementor {

	/** prefix for the negative literals. */
	public static final String NOT_PREFIX = "NOT_";

	/** The edb for the evaluation */
	private final IProgram p;

	/** Map with all different types as keys and all corresponding possibilities as values. */
	private Map<Class, IRelation> cons;

	public Complementor(final IProgram p) {
		if (p == null) {
			throw new NullPointerException("The edb must not be null");
		}
		if (p.getRules().contains(null)) {
			throw new NullPointerException("The rules must not contain null");
		}
		// TODO: Replcae this check together with the stratification check
		//		 to avoid looping through the ruleset more than once
		// TODO: remove this. this hasn't anything to do with the complement!
		for (final IRule rule : p.getRules()) {
			if (rule.getHeadLenght() != 1) {
				throw new IllegalArgumentException(
						"The length of the head must be 1, but was "
								+ rule.getHeadLenght());
			}
		}
		this.p = p;

		// Stratify rules
		// TODO: remove this. this hasn't anything to do with the complement!
		if (!MiscOps.stratify(p)) {
			throw new RuntimeException("Rules are unstratifiable");
		}
		updateClean();
	}

	/**
	 * Computes the complement of a relaiton.
	 * @param pr the predicate for which to compute the complement
	 * @throws NullPointerException if the predicate is <code>null</code>
	 */
	public IRelation getComplement(final IPredicate pr) {
		if (pr == null) {
			throw new NullPointerException("The literal must not be null");
		}
		final IRelation facts = p.getFacts(pr);
		if ((facts == null) || facts.isEmpty()) {
			// maybe another exception should be thrown
			throw new IllegalArgumentException("Can't determine the datatypes " + 
					"if there aren't any facts for the predicate (" + pr + ")");
		}

		IRelation r = null;

		for (final ITerm t : facts.first().getTerms()) {
			if (r == null) {
				r = relationForTerm(t);
			} else {
				r = Factory.RELATION_OPERATION.createJoinOperator(r, relationForTerm(t), 
						new int[] { -1, -1 }).join();
			}
		}

		r.removeAll(facts);
		return r;
	}

	private IRelation relationForTerm(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (cons.get(t.getClass()) == null) {
			return new Relation(1);
		}
		return cons.get(t.getClass());
	}

	/**
	 * Sets all new constants to the constants map (deletes the old ones).
	 */
	private void updateClean() {
		cons = new HashMap<Class, IRelation>();
		update();
	}

	/**
	 * Adds all new constants to the constants map.
	 */
	private void update() {
		// adding all constants of the rules
		for (final IRule r : p.getRules()) {
			for (final ITerm t : getConstants(r)) {
				putTerm(t);
			}
		}
		// adding all constants of the relations
		for (final IRelation r : p.getFacts().values()) {
			for (final ITuple t : r) {
				for (ITerm term : t.getTerms()) {
					putTerm(term);
				}
			}
		}
	}

	/**
	 * Adds a term to the map of constants and creates a new relation if needed.
	 * @param t the term to add
	 * @throws NullPointerException if the term is <code>null</code>
	 */
	private void putTerm(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}

		IRelation r = cons.get(t.getClass());
		if (r == null) {
			r = new Relation(1);
			cons.put(t.getClass(), r);
		}
		r.add(Factory.BASIC.createTuple(t));
	}

	/**
	 * Determines and returns all constants of a rule.
	 * 
	 * @param r
	 *            the rule with the possible constants
	 * @return the set of constants
	 * @throws NullPointerException
	 *             if the rule is {@code null}
	 */
	private static Set<ITerm> getConstants(final IRule r) {
		if (r == null) {
			throw new NullPointerException("The rule must not be null");
		}

		final Set<ITerm> c = new HashSet<ITerm>();
		for (final ILiteral l : r.getHeadLiterals()) {
			c.addAll(getConstants(l.getTuple()));
		}
		for (final ILiteral l : r.getBodyLiterals()) {
			c.addAll(getConstants(l.getTuple()));
		}
		return c;
	}

	/**
	 * Determines all constants of a tuple and returns them.
	 * 
	 * @param t
	 *            the tuple with the possible constants
	 * @return the set of constants
	 * @throws NullPointerException
	 *             if the tuple is {@code null}
	 */
	private static Set<ITerm> getConstants(final ITuple t) {
		if (t == null) {
			throw new NullPointerException("The literal must not be null");
		}

		final Set<ITerm> c = new HashSet<ITerm>();
		for (final ITerm e : t.getTerms()) {
			c.addAll(getConstants(e));
		}
		return c;
	}
	
	/**
	 * Determines all constants of a term (might be also a constructed term and returns them.
	 * @param t the term
	 * @return all constants of this term
	 * @throws NullPointerException if the term is <code>null</code>
	 */
	private static Set<ITerm> getConstants(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}

		if (!t.isGround()) {
			return Collections.EMPTY_SET;
		} else if (t instanceof IConstructedTerm) {
			final Set<ITerm> c = new HashSet<ITerm>();
			c.add(t);
			// getting out all constatnts of this constructed term
			for (final ITerm e : ((IConstructedTerm) t).getParameters()) {
				c.addAll(getConstants(e));
			}
			return c;
		}
		return Collections.singleton(t);
	}

	/**
	 * Returns the highest stratum of a set of predicates.
	 * 
	 * @param h	The set of idb predicates.
	 * @return 	The highest stratum.
	 * @throws 	NullPointerException
	 *             	if the set of predicates is {@code null}.
	 * @throws NullPointerException
	 *             if the set contains {@code null}.
	 */
	protected static int getMaxStratum(final Set<IPredicate> h) {
		if (h == null) {
			throw new NullPointerException("The predicates must not be null");
		}
		int strat = 0;
		for (final IPredicate pred : h) {
			strat = Math.max(strat, pred.getStratum());
		}
		return strat;
	}
	
	/**
	 * Determines (out of a set of literals) all literals whose predicats have a given stratum.
	 * 
	 * @param preds
	 *            the set of predicates.
	 * @param s
	 *            the stratum to look for
	 * @return the set of predicates at the given stratum
	 * @throws NullPointerException
	 *             if the set of predicates is {@code null}
	 * @throws NullPointerException
	 *             if the set of predicates contains {@code null}
	 * @throws IllegalArgumentException
	 *             if the stratum is smaller than 0
	 */
	protected static Set<IPredicate> getPredicatesOfStratum(
			final Set<IPredicate> preds, final int s) {
		if (preds == null) {
			throw new NullPointerException("The predicates must not be null");
		}
		if (s <= 0) {
			throw new IllegalArgumentException(s + " is not a valid stratum");
		}

		final Set<IPredicate> predicates = new HashSet<IPredicate>();
		for (final IPredicate p : preds) {
			if (p.getStratum() == s) {
				predicates.add(p);
			}
		}
		return predicates;
	}
}
