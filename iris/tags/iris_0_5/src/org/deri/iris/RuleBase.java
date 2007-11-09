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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.ArithmeticBuiltin;
import org.deri.iris.builtins.EqualBuiltin;
import org.deri.iris.evaluation.MiscOps;
import org.deri.iris.evaluation.RuleValidator;
import org.deri.iris.rules.IRuleOptimiser;
import org.deri.iris.rules.IRuleReOrderingOptimiser;
import org.deri.iris.rules.IRuleStratifier;
import org.deri.iris.rules.optimisation.JoinConditionOptimiser;
import org.deri.iris.rules.optimisation.ReOrderLiteralsOptimiser;
import org.deri.iris.rules.optimisation.RemoveDuplicateLiteralOptimiser;
import org.deri.iris.rules.optimisation.ReplaceVariablesWithConstantsOptimiser;
import org.deri.iris.rules.ordering.SimpleReOrdering;
import org.deri.iris.rules.stratification.GlobalStratifier;
import org.deri.iris.rules.stratification.LocalStratifier;
import org.deri.iris.terms.ConstructedTerm;

/**
 * The set of rules and utility methods, i.e. the IDB for a logic program
 */
public class RuleBase
{
	/**
	 * Constructor.
	 */
	public RuleBase()
	{
		mStratifiers.add( new GlobalStratifier() );
		mStratifiers.add( new LocalStratifier() );
		
		mReOrderingOptimisers.add( new SimpleReOrdering() ); 

		mRuleOptimisers.add( new JoinConditionOptimiser() );
		mRuleOptimisers.add( new ReplaceVariablesWithConstantsOptimiser() );
		mRuleOptimisers.add( new ReOrderLiteralsOptimiser() );
		mRuleOptimisers.add( new RemoveDuplicateLiteralOptimiser() );
	}

	/**
	 * Reset back to its newly constructed state. 
	 */
	public void clear()
	{
		mRules.clear();
		mRuleStrata = null;

		mDirtyStratum = true;
		mIsStratified = false;
	}

	/**
	 * Add a rule.
	 * @param rule the rule to add
	 * @return <code>false</code> if the rule was already in the program,
	 * otherwise <code>true</code>
	 * @throws IllegalArgumentException if the rule was <code>null</code>
	 */
	public boolean addRule(final IRule rule) {
		if (rule == null) {
			throw new IllegalArgumentException("The rule must not be null");
		}

		mDirtyStratum = true;

		return mRules.add(rule);
	}

	/**
	 * Remove a rule. This feature will be removed soon.
	 * TODO deprecate
	 * @param rule The rule to remove.
	 * @return True if the rule was removed.
	 */
	public boolean removeRule(IRule rule){
		if (rule == null) {
			throw new IllegalArgumentException("The rule must not be null");
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
	
	public boolean stratify()
	{
		if( mDirtyStratum )
		{
			for( IRuleStratifier stratifier : mStratifiers )
			{
				mRuleStrata = stratifier.stratify( mRules );
				if( mRuleStrata != null )
					break;
			}

			mIsStratified = mRuleStrata != null;
			
			// Stratification might involve re-writing the rules, so reset our rule collection.
			if( mIsStratified )
			{
				final List<Collection<IRule>> rectifiedRuleStrata = new ArrayList<Collection<IRule>>();

				mRules.clear();

				for( Collection<IRule> stratum : mRuleStrata )
				{
					final Set<IRule> rectifiedStratum = new HashSet<IRule>();
					
					for( IRule rule : stratum )
					{
						IRule r = optimise( rule ); 
						r = MiscOps.rectify( r );
						rectifiedStratum.add( r );
						mRules.add( r );
					}

					rectifiedRuleStrata.add( reOrderRules( rectifiedStratum ) );
				}
				mRuleStrata = rectifiedRuleStrata;
			}
		}

		return mIsStratified;
	}
	
	/**
	 * Attempt to re-order rules such that dependent rules are evaluated first.
	 * @param rules The strata of rules to re-order.
	 * @return The re-ordered rules.
	 */
	private Collection<IRule> reOrderRules( Collection<IRule> rules )
	{
		for( IRuleReOrderingOptimiser optimiser : mReOrderingOptimisers )
			rules = optimiser.reOrder( rules );
		
		return rules;
	}
	
	/**
	 * Perform some rule optimisations.
	 * @param rule The rule to optimise.
	 * @return The optimised rule.
	 */
	private IRule optimise( IRule rule )
	{
		for( IRuleOptimiser optimiser : mRuleOptimisers )
			rule = optimiser.optimise( rule );
		
		return rule;
	}
	
	/**
	 * Indicates if any rule has negation.
	 * @return true if negation present
	 */
	public boolean hasNegation() {
		for (IRule r : mRules) {
			for (ILiteral l : r.getBody()) {
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
			for (ILiteral l : r.getBody()) {
				for (Object t : l.getAtom().getTuple()) {
					if (t instanceof ConstructedTerm) {
						return true;
					}
				}
			}
			for (ILiteral l : r.getHead()) {
				for (Object t : l.getAtom().getTuple()) {
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
	public Set<IPredicate> getPredicates()
	{
		Set<IPredicate> result = new HashSet<IPredicate>();
		
		for( IRule rule : mRules )
			result.add( rule.getHead().get( 0 ).getAtom().getPredicate() );
		
		return result;
	}
	
	// TODO
	// Evaluation classes can call this rather than getMaxStratum(this.idbMap.keySet());
	/**
	 * Get the highest stratum of all rule predicates.
	 * @return The highest stratum level.
	 */
	public int getRuleStrataSize()
	{
		return mRuleStrata.size();
	}
	
	public Collection<IRule> getRulesOfStratum( int stratum )
	{
		return mRuleStrata.get( stratum );
	}

	/**
	 * Indicates if all the rules in the rule base are safe.
	 * @return true If all the rules are safe.
	 */
	public boolean checkAllRulesSafe()
	{
		try
		{
			for( IRule rule : mRules )
			{
				checkRuleIsSafe( rule );
			}
		}
		catch( RuleUnsafeException e )
		{
			return false;
		}
		
		return true;
	}

	/**
	 * Check the rule for safeness based on the current configuration for rule-safety.
	 * @param rule The rule to be checked
	 * @throws RuleUnsafeException If the rule is not safe.
	 */
	private void checkRuleIsSafe( IRule rule ) throws RuleUnsafeException
	{
		// Eventually the parameter values for this validator instance need to be obtained from
		// some IRIS-wide configuration.
		RuleValidator rs = new RuleValidator(	mAllowNotLimitedVariablesInNegatedSubGoals, 
												mAllowArithmeticPredicatesToImplyLimited );
		
		// Add all the head variables
		for( ILiteral headLiteral : rule.getHead())
			rs.addHeadVariables( extractVariableNames( headLiteral ) );

		// Then for each literal in the rule
		for( ILiteral lit : rule.getBody())
		{
			// If it has any variables at all
			if ( ! lit.getAtom().isGround() )
			{
				boolean builtin = lit.getAtom().isBuiltin();
				boolean positive = lit.isPositive();
				
				List<String> variables = extractVariableNames( lit );
				
				// Do the special handling for built-in predicates
				if( builtin )
				{
					if( positive && isArithmetic( lit.getAtom() ) )
					{
						rs.addVariablesFromPositiveArithmeticPredicate( isEquality( lit.getAtom() ), variables );
					}
					else
					{
						rs.addVariablesFromBuiltinPredicate( variables );
					}
				}
				else
				{
					// Ordinary predicate
					rs.addVariablesFromOrdinaryPredicate( positive, variables );
				}
			}
		}
		
		// Throws if not safe!
		rs.isSafe();
	}
	
	/**
	 * Get the variable names of variable terms in a literal.
	 * @param literal The literal to be processed.
	 * @return The names of variables.
	 */
	private static List<String> extractVariableNames( ILiteral literal )
	{
		List<String> variables = new ArrayList<String>();
		
		for( ITerm term : literal.getAtom().getTuple() )
		{
			if( ! term.isGround() )
				variables.add( term.toString() );
		}
		
		return variables;
	}
	
	/**
	 * Utility to check if an atom is an equality built-in
	 * @param atom The atom to check
	 * @return true if it is
	 */
	private static boolean isEquality( IAtom atom )
	{
		return atom instanceof EqualBuiltin;
	}

	/**
	 * Utility to check if an atom is one of the ternary arithmetic built-ins
	 * @param atom The atom to check
	 * @return true if it is
	 */
	private static boolean isArithmetic( IAtom atom )
	{
		return  atom instanceof ArithmeticBuiltin;
	}
	
	private final List<IRuleStratifier> mStratifiers = new ArrayList<IRuleStratifier>();
	
	private final List<IRuleOptimiser> mRuleOptimisers = new ArrayList<IRuleOptimiser>();
	
	private final List<IRuleReOrderingOptimiser> mReOrderingOptimisers = new ArrayList<IRuleReOrderingOptimiser>();

	/** The rules of this program. */
	private final Set<IRule> mRules = new HashSet<IRule>();
	
	private List<Collection<IRule>> mRuleStrata;

	/** Whether the rules have changed since the latest stratum computation. */ 
	private boolean mDirtyStratum = true;
	
	/** Result of last stratification attempt. */
	private boolean mIsStratified = false;

	/** Flag to indicate if variables in negated sub goals must be limited or not. */
	private final boolean mAllowNotLimitedVariablesInNegatedSubGoals = true;
	
	/** Flag to indicate if limited variables as operands of a ternary operator imply that the target is also limited. */
	private final boolean mAllowArithmeticPredicatesToImplyLimited = true;
}
