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

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * <p>Computes the complement of a relation</p>
 * <p>$Id: Complementor.java,v 1.14 2007-04-04 21:54:57 darko_anicic Exp $</p>
 * 
 * @deprecated Functionality of Complementor is replace by JoinComplement. 
 * Methods related to the stratification: getMaxStratum, 
 * getPredicatesOfStratum, were moved from this class to 
 * org.deri.iris.evaluation.MiscOps.
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.14 $
 */
public class Complementor {

	/** prefix for the negative literals. */
	public static final String NOT_PREFIX = "NOT_";

	/** The edb for the evaluation */
	private final IProgram p;

	/** Map with all different types as keys and all corresponding possibilities as values. */
	private Map<Class, IRelation> cons;

	/**
	 * Creates the complementor for a given program.
	 * @param p the program from where to take the constans
	 * @throws NullPointerException if the program is <code>null</code>
	 */
	public Complementor(final IProgram p) {
		if (p == null) {
			throw new NullPointerException("The edb must not be null");
		}
		if (p.getRules().contains(null)) {
			throw new NullPointerException("The rules must not contain null");
		}
		this.p = p;
		updateClean();
	}

	/**
	 * Computes the complement of a relaiton.
	 * @param pr the predicate to compute the complement for
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
						new int[] { -1, -1 }).join(); //getInitIndexes
			}
		}

		r.removeAll(facts);
		return r;
	}

	/**
	 * Returns the relation with all possible constants for the given term. E.g. if the 
	 * term is an integer term it will return a relation with all possible integers found in
	 * the edb.
	 * @param t the term for which to return all constants
	 * @return the constants
	 * @throws NullPointerException if the term is <code>null</code>
	 */
	private IRelation relationForTerm(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		final IRelation r = cons.get(t.getClass());
		if (r == null) {
			return Factory.RELATION.getRelation(1);
		}
		return r;
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
			r =  Factory.RELATION.getRelation(1);
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

}
