/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2007  Digital Enterprise Research Institute (DERI), 
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
package org.deri.iris;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.terms.ConstructedTerm;

/**
 * The set of rules and utility methods, i.e. the IDB for a logic program
 */
public class RuleBase
{
	/** The lowest stratum level. */
	public static final int BOTTOM_STRATUM = 0;

	/**
	 * Constructor.
	 */
	public RuleBase()
	{
	}

	/**
	 * Reset back to its newly constructed state. 
	 */
	public void clear()
	{
		mRules.clear();
		mStrata.clear();

		mDirtyStratum = true;
		mIsStratified = false;
	}

	/**
	 * Add a rule.
	 * @param r the rule to add
	 * @return <code>false</code> if the rule was already in the program,
	 * otherwise <code>true</code>
	 * @throws NullPointerException if the rule was <code>null</code>
	 */
	public boolean addRule(final IRule r) {
		if (r == null) {
			throw new NullPointerException("The rule must not be null");
		}

		mDirtyStratum = true;

		return mRules.add(r);
	}

	/**
	 * Remove a rule. This feature will be removed soon.
	 * TODO deprecate
	 * @param r The rule to remove.
	 * @return True if the rule was removed.
	 */
	public boolean removeRule(IRule r){
		if (r == null) {
			throw new NullPointerException("The rule must not be null");
		}
		
		mDirtyStratum = true;

		return false;
	}
	
	/**
	 * Get all the rules.
	 * @return The rules from this IDB.
	 */
	public Set<IRule> getRules() {
		return Collections.unmodifiableSet(mRules);
	}
	
	/**
	 * Calculates and sets the stratum for every predicate of a program.
	 * @return {@code true} if the program is stratified, otherwise
	 *         {@code false}
	 */
	public boolean stratify()
	{
		if( mDirtyStratum )
		{
			final int ruleCount = mRules.size();
			int highest = BOTTOM_STRATUM;
			boolean change = true;
	
			// Clear the strata map, i.e. set all strata to BOTTOM_STRATUM
			mStrata.clear();
			
			while ((highest <= (ruleCount + BOTTOM_STRATUM) ) && change) {
				change = false;
				for (final IRule r : mRules) {
					for (final ILiteral hl : r.getHead().getLiterals()) {
						final IPredicate hp = hl.getPredicate();
	
						for (final ILiteral bl : r.getBody().getLiterals()) {
							final IPredicate bp = bl.getPredicate();
	
							if (bl.isPositive()) {
								int greater = Math.max(getStratum(hp), 
												getStratum(bp));
								if (getStratum(hp) < greater) {
									setStratum(hp, greater);
									change = true;
								}
								highest = Math.max(highest, greater);
							} else {
								int current = getStratum(bp);
								if (current >= getStratum(hp)) {
									setStratum(hp, current + 1);
									highest = Math.max(highest, current + 1);
									change = true;
								}
							}
						}
					}
				}
			}
			
			mIsStratified = highest <= (ruleCount+BOTTOM_STRATUM);
		}

		return mIsStratified;
	}
	
	/**
	 * Indicates if any rule has negation.
	 * @return true if negation present
	 */
	public boolean hasNegation() {
		for (IRule r : mRules) {
			for (ILiteral l : r.getBody().getLiterals()) {
				if (!l.isPositive()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Indicates if any rule has constructed terms (function symbols).
	 * @return true if constructed terms present.
	 */
	public boolean hasConstructedTerms() {
		for (IRule r : mRules) {
			for (ILiteral l : r.getBody().getLiterals()) {
				for (Object t : l.getTuple()) {
					if (t instanceof ConstructedTerm) {
						return true;
					}
				}
			}
			for (ILiteral l : r.getHead().getLiterals()) {
				for (Object t : l.getTuple()) {
					if (t instanceof ConstructedTerm) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Get the number of rules.
	 * @return The number of rules.
	 */
	public int ruleCount() {
		return mRules.size();
	}
	
	/**
	 * Return the currently known predicates.
	 * @return The predicates.
	 */
	public Set<IPredicate> getPredicates() {
		return Collections.unmodifiableSet(mStrata.keySet());
	}

	/**
	 * Determines (out of a set of literals) all literals whose predicates have a given stratum.
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
	 *             if the stratum is smaller than BOTTOM_STRATUM
	 */
	public Set<IPredicate> getPredicatesOfStratum( final Set<IPredicate> preds, final int s) {
		if (preds == null) {
			throw new NullPointerException("The predicates must not be null");
		}
		if (s < BOTTOM_STRATUM) {
			throw new IllegalArgumentException(s + " is not a valid stratum");
		}

		final Set<IPredicate> predicates = new HashSet<IPredicate>();
		for (final IPredicate pred : preds) {
			if (getStratum(pred) == s) {
				predicates.add(pred);
			}
		}
		return predicates;
	}
	
	/**
	 * Returns the highest stratum of a set of predicates.
	 * 
	 * @param h	The set of idb predicates.
	 * @return 	The highest stratum.
	 * @throws 	NullPointerException
	 *             	if the set of predicates is {@code null}.
	 */
	public int getMaxStratum(final Set<IPredicate> h) {
		if (h == null) {
			throw new NullPointerException("The predicates must not be null");
		}
		int strat = 0;
		for (final IPredicate pred : h) {
			strat = Math.max(strat, getStratum( pred ));
		}
		return strat;
	}
	
	// TODO
	// Evaluation classes can call this rather than getMaxStratum(this.idbMap.keySet());
	/**
	 * Get the highest stratum of all rule predicates.
	 * @return The highest stratum level.
	 */
	public int getMaxStratumForAllRuleHeadLiterals()
	{
		int strat = 0;
		for( IRule rule : mRules )
		{
			for( ILiteral literal : rule.getHead().getLiterals() )
			{
				strat = Math.max(strat, getStratum( literal.getPredicate() ) );
			}
		}
		
		return strat;
	}

	/**
	 * Get the stratum for a particular (rule head) predicate.
	 * @param predicate The rule-head predicate.
	 * @return The stratum level.
	 */
	private int getStratum( final IPredicate predicate )
	{
		assert predicate!= null;
		
		Integer stratum = mStrata.get( predicate );
		
		if( stratum == null )
		{
			stratum = BOTTOM_STRATUM;
			mStrata.put(  predicate, stratum );
		}
		
		
		return stratum;
	}
	
	/**
	 * Set the stratum for a (rule-head) predicate.
	 * @param predicate predicate
	 * @param stratum stratum level
	 */
	private void setStratum(final IPredicate predicate, final int stratum)
	{
		assert predicate != null;
		assert mStrata.keySet().contains(predicate);
		assert stratum >= 0 : "The stratum must not be negative, but was: " + stratum;
		
		mStrata.put(predicate, Integer.valueOf(stratum));
	}
	
	/** The rules of this program. */
	private final Set<IRule> mRules = new HashSet<IRule>();

	/** Map for the strata of the different predicates. */
	private final Map<IPredicate, Integer> mStrata = new HashMap<IPredicate, Integer>();
	
	/** Whether the rules have changed since the latest stratum computation. */ 
	private boolean mDirtyStratum = true;
	
	/** Result of last stratification attempt. */
	private boolean mIsStratified = false;
	}
