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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.IEDB;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.storage.Relation;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @author richi
 * 
 * @date 27.11.2006 19:07:43
 */
public class Complementor {

	/** prefix for the negative literals. */
	public static final String NOT_PREFIX = "NOT_";

	/**
	 * DOM is a universe created as the union of the symbols appearing in the
	 * EDB relations and in the rules themselves.
	 */
	private final IRelation DOM;

	/** The edb for the evaluation */
	private final IEDB e;

	private Map<Integer, IRelation> cacheMap = null;

	public Complementor(final IEDB e) {
		if (e == null) {
			throw new NullPointerException("The edb must not be null");
		}
		if (e.getRules().contains(null)) {
			throw new NullPointerException("The rules must not contain null");
		}
		for (final IRule rule : e.getRules()) {
			if (rule.getHeadLenght() != 1) {
				throw new IllegalArgumentException(
						"The length of the head must be 1, but was "
								+ rule.getHeadLenght());
			}
		}
		this.e = e;

		// Stratify rules
		if (!MiscOps.stratify(e)) {
			throw new RuntimeException("Rules are unstratifiable");
		}
		this.DOM = createDom(e);
		this.cacheMap = new HashMap<Integer, IRelation>();
	}

	public IRelation getComplement(final IPredicate p) {
		if (p == null) {
			throw new NullPointerException("The literal must not be null");
		}
		final int arity = p.getArity();
		IRelation r = null;

		if (this.cacheMap.containsKey(arity)) {
			r = this.cacheMap.get(arity);
		} else {
			r = DOM;
			for (int i = 1; i < arity; i++) {
				r = Factory.RELATION_OPERATION.createJoinOperator(r, DOM,
						new int[] { -1, -1 }).join();
			}
			this.cacheMap.put(arity, r);
		}
		final IRelation facts = e.getFacts(p);
		if (facts != null) {
			r.removeAll(facts);
		}
		return r;
	}

	/**
	 * Creates the DOM for the given edb.
	 * 
	 * @param e
	 *            the edb
	 * @return the DOM relation
	 * @throws NullPointerException
	 *             if the edb is {@code null}
	 */
	private static IRelation createDom(final IEDB e) {
		if (e == null) {
			throw new NullPointerException("The edb must not be null");
		}
		IRelation d = new Relation(1);

		// adding all constants of the rules
		for (final IRule rule : e.getRules()) {
			for (ITerm term : getConstantsOfRule(rule)) {
				d.add(Factory.BASIC.createTuple(term));
			}
		}

		// adding all constants of the relations
		for (final IRelation rel : e.getFacts().values()) {
			for (final ITuple t : rel) {
				for (ITerm term : t.getTerms()) {
					d.add(Factory.BASIC.createTuple(term));
				}
			}
		}
		return d;
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
	private static Set<ITerm> getConstantsOfRule(final IRule r) {
		if (r == null) {
			throw new NullPointerException("The rule must not be null");
		}

		final Set<ITerm> c = new HashSet<ITerm>();
		for (final ILiteral l : r.getHeadLiterals()) {
			c.addAll(getConstantsOfTuple(l.getTuple()));
		}
		for (final ILiteral l : r.getBodyLiterals()) {
			c.addAll(getConstantsOfTuple(l.getTuple()));
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
	private static Set<ITerm> getConstantsOfTuple(final ITuple t) {
		if (t == null) {
			throw new NullPointerException("The literal must not be null");
		}

		final Set<ITerm> c = new HashSet<ITerm>();
		for (final ITerm term : t.getTerms()) {
			if (!(term instanceof IVariable)) {
				c.add(term);
			}
		}
		return c;
	}
}
