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
package org.deri.iris.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.deri.iris.Configuration;
import org.deri.iris.ProgramNotStratifiedException;
import org.deri.iris.RuleUnsafeException;
import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.builtins.ArithmeticBuiltin;
import org.deri.iris.builtins.EqualBuiltin;

/**
 * The set of rules and utility methods, i.e. the IDB for a logic program
 */
public class RuleBase
{
	/**
	 * Constructor.
	 * @param configuration the configuration object for this instance of owning knowledge-base. 
	 * @param rules The rules (IDB) of the knowledge-base.
	 */
	public RuleBase( Configuration configuration, List<IRule> rules )
	{
		mConfiguration = configuration;
		
		mOriginalRules.addAll( rules );
	}

	/**
	 * Get all the rules (as provided in the constructor and unmodified).
	 * @return The rules from this IDB.
	 */
	public Set<IRule> getRules()
	{
		return Collections.unmodifiableSet(mOriginalRules);
	}
	
	/**
	 * Called prior to evaluation to apply optimisation and stratification techniques.
	 * @throws ProgramNotStratifiedException If the rule-set can not be stratified.
	 * @throws RuleUnsafeException If an unsafe rule is detected.
	 */
	public void initialise() throws ProgramNotStratifiedException, RuleUnsafeException
	{
		// Stratify
		List<IRule> rules = new ArrayList<IRule>( mOriginalRules );
		List<List<IRule>> stratifiedRules = stratify( rules );
		
		mRuleStrata = new ArrayList<List<IRule>>();
		
		for( List<IRule> stratum : stratifiedRules )
		{
			// Re-order stratum
			List<IRule> reorderedRules = reOrderRules( stratum );
			
			// Rule safety processing
			List<IRule> safeRules = checkRuleSafety( reorderedRules );
			
			// Rule optimisation
			List<IRule> optimisedRules = optimiseRules( safeRules );
		
			mRuleStrata.add( optimisedRules );
		}
	}
	
	/**
	 * Apply each of the stratifiers in turn until stratification is achieved. 
	 * @param rules The input rules.
	 * @return A collection of collections of rules arranged in to strata.
	 * The rules in each stratum might be different from the imput rules. 
	 * @throws ProgramNotStratifiedException If none of the stratification algorithms successfully
	 * stratified the rule set.
	 */
	private List<List<IRule>> stratify( List<IRule> rules ) throws ProgramNotStratifiedException
	{
		for( IRuleStratifier stratifier : mConfiguration.mStratifiers )
		{
			List<List<IRule>> ruleStrata = stratifier.stratify( rules );
			if( ruleStrata != null )
				return ruleStrata;
		}

		throw new ProgramNotStratifiedException( "The input program is not stratified" );
	}
	
	/**
	 * Attempt to re-order the rules of a stratum such that the dependent rules are evaluated first.
	 * @param rules The strata of rules to re-order.
	 * @return The re-ordered rules.
	 */
	private List<IRule> reOrderRules( List<IRule> rules )
	{
		for( IRuleReOrderingOptimiser optimiser : mConfiguration.mReOrderingOptimisers )
			rules = optimiser.reOrder( rules );
		
		return rules;
	}
	
	/**
	 * Optimise a collection of rules.
	 * @param rules A collection of rules to optimise.
	 * @return A list of optimised rules.
	 */
	private List<IRule> optimiseRules( final List<IRule> rules )
	{
		List<IRule> optimisedRules = new ArrayList<IRule>();
		
		for( IRule rule : rules )
			optimisedRules.add( optimise( rule ) );
			
		return optimisedRules;
	}
	
	/**
	 * Apply each of the rule optimisations a rule in turn.
	 * @param rule
	 * @return
	 */
	private IRule optimise( final IRule inputRule )
	{
		IRule outputRule = inputRule;
		
		for( IRuleOptimiser optimiser : mConfiguration.mRuleOptimisers )
			outputRule = optimiser.optimise( outputRule );
		
		return outputRule;
	}
	
	/**
	 * Get the number of rule strata.
	 * @return The number of strata.
	 */
	public int getRuleStrataSize()
	{
		return mRuleStrata.size();
	}
	
	/**
	 * Get the collection of rules for the requested stratum.
	 * @param stratum The stratum requested, must be between 0 (the lowest) and getRuleStrataSize() - 1
	 * @return The rules for this stratum
	 */
	public List<IRule> getRulesOfStratum( int stratum )
	{
		return mRuleStrata.get( stratum );
	}

	@Override
    public String toString()
    {
		StringBuilder result = new StringBuilder();
		
		for( IRule rule : mOriginalRules )
			result.append( rule.toString() );
	    
		return result.toString();
    }

	/**
	 * Check the given rules for rule-safety (unbound variables).
	 * In the future, this method will be allowed to modify the input rules.
	 * @param rules The rules to check.
	 * @return The collection of safe rules.
	 * @throws RuleUnsafeException
	 */
	private List<IRule> checkRuleSafety( List<IRule> rules ) throws RuleUnsafeException
	{
		List<IRule> safeRules = new ArrayList<IRule>();
		
		for( IRule rule : rules )
			safeRules.add( checkRuleIsSafe( rule ) );
		
		return safeRules;
	}
	
	/**
	 * Check the rule for safeness based on the current configuration for rule-safety.
	 * @param rule The rule to be checked
	 * @throws RuleUnsafeException If the rule is not safe.
	 */
	private IRule checkRuleIsSafe( IRule rule ) throws RuleUnsafeException
	{
		RuleValidator validator = new RuleValidator(
						mConfiguration.ruleSafetyAllowUnlimitedVariablesInNegatedOrdinaryPredicates, 
						mConfiguration.ruleSafetyTernaryTargetsImplyLimited );
		
		// Add all the head variables
		for( ILiteral headLiteral : rule.getHead())
			validator.addHeadVariables( extractVariableNames( headLiteral ) );

		// Then for each literal in the rule
		for( ILiteral lit : rule.getBody())
		{
			// If it has any variables at all
			if ( ! lit.getAtom().isGround() )
			{
				boolean builtin = lit.getAtom().isBuiltin();
				boolean positive = lit.isPositive();
				
				// Treat built-ins with constructed terms like ordinaries
				if( containsConstructedTerms( lit.getAtom().getTuple() ) )
					builtin = false;
				
				List<String> variables = extractVariableNames( lit );
				
				// Do the special handling for built-in predicates
				if( builtin )
				{
					if( positive && isArithmetic( lit.getAtom() ) )
					{
						validator.addVariablesFromPositiveArithmeticPredicate( isEquality( lit.getAtom() ), variables );
					}
					else
					{
						validator.addVariablesFromBuiltinPredicate( variables );
					}
				}
				else
				{
					// Ordinary predicate
					validator.addVariablesFromOrdinaryPredicate( positive, variables );
				}
			}
		}
		
		// Throws if not safe!
		validator.isSafe();
		
		return rule;
	}
	
	private static boolean containsConstructedTerms( ITuple tuple )
	{
		for( ITerm term : tuple )
		{
			if( term instanceof IConstructedTerm )
				return true;
		}
		return false;
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
			if( term instanceof IConstructedTerm )
			{
				IConstructedTerm constructed = (IConstructedTerm) term;
				
				for( IVariable variable : constructed.getVariables() )
					variables.add( variable.getValue() );
			}
			else if( term instanceof IVariable )
			{
				IVariable variable = (IVariable) term;
				variables.add( variable.getValue() );
			}
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
		return atom instanceof ArithmeticBuiltin;
	}
	
	/** The original rules of this program. */
	private final Set<IRule> mOriginalRules = new HashSet<IRule>();
	
	/** The stratified (and optimised) rules. */
	private List<List<IRule>> mRuleStrata;
	
	/** The configuration object for the owning knowledge-base. */
	private final Configuration mConfiguration;
}
