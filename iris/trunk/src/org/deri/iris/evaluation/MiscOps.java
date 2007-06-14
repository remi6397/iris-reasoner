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
package org.deri.iris.evaluation;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.BUILTIN;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.basics.seminaive.ConstLiteral;

/**
 * <p>
 * This class offers some miscellaneous operations.
 * </p>
 * <p>
 * $Id: MiscOps.java,v 1.10 2007-06-14 21:28:34 darko_anicic Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @author graham
 * @author Darko Anicic, DERI Innsbruck
 * 
 * @version $Revision: 1.10 $
 * @date $Date: 2007-06-14 21:28:34 $
 */
public class MiscOps {

	/** prefix for the variables in rectified rules. */
	private static final String VAR_PREFIX = "?X_";

	private MiscOps() {
		// prevent subclassing
	}

	/**
	 * Rectifies a collectin of rules.
	 * 
	 * @param r
	 *            the rules to rectify
	 * @return a set of rectified rules corresponding to the given rules
	 * @throws NullPointerException
	 *             if the collection is {@code null}
	 * @see MiscOps#rectify(IRule)
	 */
	public static Set<IRule> rectify(final Collection<IRule> r) {
		if (r == null) {
			throw new NullPointerException("The rules must not be null");
		}
		final Set<IRule> rules = new HashSet<IRule>(r.size());
		for (final IRule rule : r) {
			rules.add(rectify(rule));
		}
		return rules;
	}

	/**
	 * <p>
	 * Rectifies a rule.
	 * </p>
	 * <p>
	 * Algorithm has been implemented from the book: "Principles of Database and 
	 * Knowledge-Base Systems Vol. 1", Jeffrey D. Ullman (page: 111.)
	 * </p>
	 * <p>
	 * Remark: This method cannot be used when function symbols (constructed terms) 
	 * in use. In such a case a modification of the  current implementation is 
	 * required. Only safe rules can be rectified.
	 * </p>
	 * @param r	The rule to rectify.
	 * @return 	The rectified rule.
	 * @throws 	NullPointerException
	 *             if the rule is {@code null}.
	 * @throws IllegalArgumentException
	 *             if the length of the head is unequal to 1.
	 */
	public static IRule rectify(final IRule r) {
		if (r == null) {
			throw new NullPointerException("The rule must not be null.");
		}
		if (r.getHeadLenght() != 1) {
			throw new IllegalArgumentException(
					"There must be only one literal in the head.");
		}
		final ILiteral hl = r.getHeadLiteral(0);
		final int arity = hl.getPredicate().getArity();
		final List<ITerm> headTerms = new ArrayList<ITerm>(arity);
		final List<ILiteral> eqSubGoals = new ArrayList<ILiteral>(r.getBodyLenght());
		final Map<IVariable, List<ILiteral>> headVarsMap = new HashMap<IVariable, List<ILiteral>>();
		
		// iterating through the terms of the head
		final Iterator<ITerm> terms = hl.getTuple().getTerms().iterator();
		for (int i = 0; i < arity; i++) {
			final ITerm t = terms.next();
			// Introduce a new variable for each of the arguments of the head predicate.
			final IVariable newVar = TERM.createVariable(VAR_PREFIX + i);
			headTerms.add(newVar);
			// Create a ConstLiteral (i.e. {a}(?X) represents ?X='a') whenever an 
			// argument of the head predicate is a ground term.
			if(t.isGround()){
				eqSubGoals.add(BASIC.createLiteral(true, new ConstLiteral(true, t, newVar)));
			}else{
				final IVariable v = (IVariable) t;
				// If some of the arguments of the head predicate are equal (and they are 
				// variables) a map is created so we can unify such subgolas later on. For 
				// instance subgoals: U=X and V=X will be unified in U=V and X will be 
				// replaced from all body literals with U. 
				if(headVarsMap.containsKey(v)){
					headVarsMap.get(v).add(
							BASIC.createLiteral(true, BUILTIN.createEqual(t, newVar)));
				}else{
					final List<ILiteral> eqLiterals = new ArrayList<ILiteral>();
					eqLiterals.add(
							BASIC.createLiteral(true, BUILTIN.createEqual(t, newVar)));
					headVarsMap.put(v, eqLiterals);
				}
			}
		}
		// Substitute all body variables with new variables and unify introduced subgolas 
		// when possible.
		final List<ILiteral> bodyLiterals = new ArrayList<ILiteral>(r.getBodyLenght());
		for (final ILiteral l: r.getBodyLiterals()) {
			final List<ITerm> litTerms = new ArrayList<ITerm>(l.getPredicate().getArity());
			for (final ITerm t : l.getTuple().getTerms()) {
				if(! t.isGround()){
					final List<ILiteral> eqLiterals = headVarsMap.get(t);
					if (eqLiterals != null) { // if this var was substituted in the head
						litTerms.add(eqLiterals.get(0).getTuple().getTerm(1));
					} else {
						litTerms.add(t);
					}
				} else {
					litTerms.add(t);
				}
			}
			l.getTuple().setTerms(litTerms);
			bodyLiterals.add(l);
		}
		// Assembling the new rectified rule.
		// adding the constant substitutions for the head
		bodyLiterals.addAll(eqSubGoals);
		// adding the variable substitutions for the head
		for (final List<ILiteral> equals : headVarsMap.values()) {
			if (equals.size() > 1) { // if there where more occurences of this 
				// variable in the head -> reap out the origial var
				ITerm last = null;
				for (final ILiteral l : equals) {
					final ITerm actual = l.getTuple().getTerm(1);
					if (last != null) {
						bodyLiterals.add(BASIC.createLiteral(true, BUILTIN.createEqual(last, actual)));
					}
					last = actual;
				}
			}
		}
		final IHead h = BASIC.createHead(BASIC.createLiteral(hl.isPositive(),
				hl.getPredicate(), BASIC.createTuple(headTerms)));
		return BASIC.copyRule(h, BASIC.createBody(bodyLiterals));
	}
	
	/**	 TODO: 
	 * 	1. Store the result somewhere!
	 *	2. Use hashMap with an integer as a key or in getPredicates()
	 *	   us a comparator so that are predicates sorted dependent on
	 *	   the stratification! Then correct getPredicatesOfStratum()
	 *	   from org.deri.iris.evaluation.seminaive.Complementor 
	*/
	
	/**
	 * <p>
	 * Calculates and sets the stratum for every predicate of a program.
	 * </p>
	 * 
	 * @param e
	 *            the program for which to set the stratum
	 * @return {@code true} if the program is stratified, otherwise
	 *         {@code false}
	 */
	public static boolean stratify(final IProgram e) {
		if (e == null) {
			throw new NullPointerException("The program must not be null");
		}
		int max = 1;
		int total = e.getPredicates().size();
		Collection<IRule> rules = e.getRules();
		boolean change = true;

		// set all strata to 1
		for (final IPredicate p : e.getPredicates()) {
			p.setStratum(1);
		}
		while ((total >= max) && change) {
			change = false;
			for (final IRule r : rules) {
				for (final ILiteral hl : r.getHeadLiterals()) {
					final IPredicate hp = hl.getPredicate();

					for (final ILiteral bl : r.getBodyLiterals()) {
						final IPredicate bp = bl.getPredicate();

						if (!bl.isPositive()) {
							int current = bp.getStratum();
							if (current >= hp.getStratum()) {
								hp.setStratum(current + 1);
								max = Math.max(max, current + 1);
								change = true;
							}
						} else {
							int greater = Math.max(hp.getStratum(), bp
									.getStratum());
							if (hp.getStratum() < greater) {
								hp.setStratum(greater);
								change = true;
							}
							max = Math.max(max, greater);
						}
					}
				}
			}
		}
		if (total >= max) {
			return true;
		}
		return false;
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
	public static int getMaxStratum(final Set<IPredicate> h) {
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
	public static Set<IPredicate> getPredicatesOfStratum(
			final Set<IPredicate> preds, final int s) {
		if (preds == null) {
			throw new NullPointerException("The predicates must not be null");
		}
		if (s < 0) {
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
