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
import org.deri.iris.builtins.AbstractBuiltin;
import org.deri.iris.builtins.EqualBuiltin;
import org.deri.iris.builtins.LessBuiltin;
import org.deri.iris.builtins.UnEqualBuiltin;

/**
 * <p>
 * This class offers some miscellaneous operations.
 * </p>
 * <p>
 * $Id: MiscOps.java,v 1.6 2007-04-07 00:27:13 darko_anicic Exp $
 * </p>
 * 
 * @author richi
 * @author graham
 * @version $Revision: 1.6 $
 * @date $Date: 2007-04-07 00:27:13 $
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
	 * Rectifies a rule.
	 * 
	 * @param r
	 *            the rule to rectify
	 * @return the rectified rule
	 * @throws NullPointerException
	 *             if the rules is {@code null}
	 * @throws IllegalArgumentException
	 *             if the length of the head is unequal to 1
	 */
	public static IRule rectify(final IRule r) {
		if (r == null) {
			throw new NullPointerException("The rule must not be null");
		}
		if (r.getHeadLenght() != 1) {
			throw new IllegalArgumentException(
					"There must be only one literal in the head");
		}

		final ILiteral hl = r.getHeadLiteral(0);
		final int arity = hl.getPredicate().getArity();

		final Map<ITerm, IVariable> subs = new HashMap<ITerm, IVariable>();

		final List<ITerm> headTerms = new ArrayList<ITerm>(arity);
		List<ILiteral> bodyLiterals = new ArrayList<ILiteral>(r.getBodyLenght());
		bodyLiterals.addAll(r.getBodyLiterals());
		
		final Iterator<ITerm> terms = hl.getTuple().getTerms().iterator();
		// iterating through the terms of the head
		for (int i = 0; i < arity; i++) {
			final ITerm t = terms.next();
			final IVariable v = TERM.createVariable(VAR_PREFIX + i);
			headTerms.add(v);

			if ((t instanceof IVariable) && !subs.keySet().contains(t)) {
				// if the term is a variable and never where substituted
				bodyLiterals = substituteVar((IVariable) t, v, bodyLiterals);
				subs.put(t, v);
			} else if (subs.keySet().contains(t)) {
				// if the variable where ever substituted
				bodyLiterals.add(BASIC.createLiteral(true, BUILTIN.createEqual(
						v, subs.get(t))));
			} else {
				// by default, create the equal builtin
				bodyLiterals.add(BASIC.createLiteral(true, BUILTIN.createEqual(
						v, t)));
			}
		}
		// assembling the new rectified rule
		final IHead h = BASIC.createHead(BASIC.createLiteral(hl.isPositive(),
				hl.getPredicate(), BASIC.createTuple(headTerms)));
		return BASIC.createRule(h, BASIC.createBody(bodyLiterals));
	}

	/**
	 * Takes a list of literals and substitutes all occurences of a variable
	 * through another one.
	 * 
	 * @param from
	 *            the variable to search for
	 * @param to
	 *            the variable which to replace the found occurences whith
	 * @param l
	 *            the list of literals
	 * @return	  the list of literals with substituted variables
	 * @throws NullPointerException
	 *             if one of the variables or the list is {@code null}, or the
	 *             list contains {@code null}
	 */
	private static List<ILiteral> substituteVar(final IVariable from, final IVariable to,
			final List<ILiteral> lits) {
		if ((from == null) || (to == null) || (lits == null) || lits.contains(null)) {
			throw new NullPointerException(
					"The variables and the list of literals must not be, or contain null");
		}
		final List<ILiteral> bodyLiterals = new ArrayList<ILiteral>(lits.size());
		for (final ILiteral lit : lits) {
			List<ITerm> terms = new ArrayList<ITerm>(lit.getTuple().getTerms().size());
			
			for (final ITerm t : lit.getTuple().getTerms()) {
				if (t.equals(from)) {
					terms.add(to);
				}else{
					terms.add(t);
				}
			}
			/**
			 * TODO: Copy method is required like:
			 * ILiteral l0 = lit.copy(); or
			 * ILiteral l0 = BASIC.createLiteral(
			 *		lit.isPositive(), lit.getAtom().copy());
			 * such that when:
			 * l0.getTuple().setTerms(terms);
			 * is called, the original literal (lit) is not affected. 
			 * 
			 * Note: currently only equality built-in is handled.
			 */
			if(lit.getAtom() instanceof AbstractBuiltin){
				if(lit.getAtom() instanceof EqualBuiltin){
					bodyLiterals.add(BASIC.createLiteral(
							lit.isPositive(), BUILTIN.createEqual(terms.get(0), terms.get(1))));
				}else
				if(lit.getAtom() instanceof UnEqualBuiltin){
					bodyLiterals.add(BASIC.createLiteral(
							lit.isPositive(), BUILTIN.createUnequal(terms.get(0), terms.get(1))));
				}else
				if(lit.getAtom() instanceof LessBuiltin){
					bodyLiterals.add(BASIC.createLiteral(
							lit.isPositive(), BUILTIN.createLess(terms.get(0), terms.get(1))));
				}
			}else{
				bodyLiterals.add(BASIC.createLiteral(
						lit.isPositive(), lit.getPredicate(), BASIC.createTuple(terms)));
			}
		}
		return bodyLiterals;
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
