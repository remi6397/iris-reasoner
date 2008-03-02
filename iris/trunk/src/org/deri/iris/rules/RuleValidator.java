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
package org.deri.iris.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
 * We use the definition of a safe rule as described by Ullman, page 105.
 * 
 * A rule is safe if all variables are limited.
 * 
 * A variable is limited if:
 * 
 * 1) It appears in a positive ordinary predicate
 * 2) It appears in a positive equality with a constant, e.g. ?X = 'a'
 * 3) It appears in a positive equality with another variable known to be limited, e.g. ?X = ?Y, ?Y = 'a'
 * 
 * However, variables that ONLY appear in a negated ordinary predicate (and nowhere else) can
 * still make for a safe rule, as such a rule can be re-written to move the negated sub-goal
 * to a separate rule, see the example in Ullman, page 129-130
 * 
 * Furthermore, variables that appear in arithmetic predicates can also be considered limited if
 * all the remaining variables are limited, e.g.
 *      X + Y = Z, X = 3, Z = 4, implies that Y is also limited
 *
 * These two relaxations of the definition of a safe rule are configurable (on/off).
 */
public class RuleValidator
{
	/**
	 * Constructor. Each instance of this rule can process exactly one rule, after which it must be
	 * discarded.
	 * @param allowNotLimitedVariablesInNegatedSubGoals false, if the strict Ullman definition
	 * should be enforced for variables in negated sub-goals.
	 * @param limitedTernaryOperandsImplyLimitedResult false, if the strict Ullman definition
	 * should be enforced for variables in that are in arithmetic predicates.
	 */
	public RuleValidator( 	IRule rule,
							boolean allowNotLimitedVariablesInNegatedSubGoals,
							boolean allowArithmeticPredicatesToImplyLimited )
	{
		mAllowNotLimitedVariablesInNegatedSubGoals = allowNotLimitedVariablesInNegatedSubGoals;
		mAllowArithmeticPredicatesToImplyLimited = allowArithmeticPredicatesToImplyLimited;

		// Add all the head variables
		for( ILiteral headLiteral : rule.getHead())
			addHeadVariables( extractVariables( headLiteral ) );

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
				
				List<IVariable> variables = extractVariables( lit );
				
				// Do the special handling for built-in predicates
				if( builtin )
				{
					if( positive && isArithmetic( lit.getAtom() ) )
					{
						addVariablesFromPositiveArithmeticPredicate( isEquality( lit.getAtom() ), variables );
					}
					else
					{
						addVariablesFromBuiltinPredicate( variables );
					}
				}
				else
				{
					// Ordinary predicate
					addVariablesFromOrdinaryPredicate( positive, variables );
				}
			}
		}
		
		processBuiltins();
	}
	
	/**
	 * Add variables that appears in the rule head.
	 * @param variables The variable names.
	 */
	private void addHeadVariables( List<IVariable> variables )
	{
		mHeadVariables.addAll( variables );
	}
	
	/**
	 * Add variables that appear in an ordinary predicate.
	 * @param positive true if the predicate is positive, i.e. not negated.
	 * @param variables The variable names.
	 */
	private void addVariablesFromOrdinaryPredicate( boolean positive, List<IVariable> variables )
	{
		if ( positive )
			mLimitedVariables.addAll( variables );
		else
			mNegativeOrdinary.addAll( variables );
	}

	/**
	 * Add variables from any built-in predicate other than positive arithmetic and equality
	 * predicates.
	 * 
	 * @param variables The variable names.
	 */
	private void addVariablesFromBuiltinPredicate( List<IVariable> variables )
	{
		mBuiltin.addAll( variables );
	}

	/**
	 * Add variables from arithmetic or equality predicates
	 * 
	 * @param isEquality true if the predicate is equality.
	 * @param variables The variable names.
	 */
	private void addVariablesFromPositiveArithmeticPredicate( boolean isEquality, List<IVariable> variables )
	{
		mBuiltin.addAll( variables );

		if( isEquality || mAllowArithmeticPredicatesToImplyLimited )
			mArithmeticGroups.add( variables );
	}
	
	public List<IVariable> getAllUnlimitedVariables()
	{
		List<IVariable> result = new ArrayList<IVariable>();
		
		List<IVariable> unlimitedHeadVariables = new ArrayList<IVariable>( mHeadVariables );
		unlimitedHeadVariables.removeAll( mLimitedVariables );

		result.addAll( unlimitedHeadVariables );
		
		List<IVariable> unlimitedBuiltinVariables = new ArrayList<IVariable>( mBuiltin );
		unlimitedBuiltinVariables.removeAll( mLimitedVariables );
		
		result.addAll( unlimitedBuiltinVariables );

		if ( ! mAllowNotLimitedVariablesInNegatedSubGoals )
		{
			List<IVariable> unlimitedNegativeOrdinaryVariables = new ArrayList<IVariable>( mNegativeOrdinary );
			unlimitedNegativeOrdinaryVariables.removeAll( mLimitedVariables );

			result.addAll( unlimitedNegativeOrdinaryVariables );
		}
		
		return result;
	}

	/**
	 * Do the special case handling of built-in predicates.
	 */
	private void processBuiltins()
	{
		// Process the positive arithmetic predicates
		boolean changed = true;
		while( changed )
		{
			changed = false;
			for ( List<IVariable> group : mArithmeticGroups )
			{
				if( group.removeAll( mLimitedVariables ) )
				{
					changed = true;
				}
				
				if( group.size() == 1 )
				{
					mLimitedVariables.add( group.get( 0 ) );
					group.clear();
					changed = true;
				}
			}
		}
	}
	
	private boolean containsConstructedTerms( ITuple tuple )
	{
		for( ITerm term : tuple )
		{
			if( term instanceof IConstructedTerm )
				return true;
		}
		return false;
	}

	/**
	 * Get the variable terms in a literal.
	 * @param literal The literal to be processed.
	 * @return The names of variables.
	 */
	private List<IVariable> extractVariables( ILiteral literal )
	{
		return literal.getAtom().getTuple().getAllVariables();
	}
	
	/**
	 * Utility to check if an atom is an equality built-in
	 * @param atom The atom to check
	 * @return true if it is
	 */
	private boolean isEquality( IAtom atom )
	{
		return atom instanceof EqualBuiltin;
	}

	/**
	 * Utility to check if an atom is one of the ternary arithmetic built-ins
	 * @param atom The atom to check
	 * @return true if it is
	 */
	private boolean isArithmetic( IAtom atom )
	{
		return atom instanceof ArithmeticBuiltin;
	}

	/** Flag to indicate if variables in negated sub goals must be limited or not. */
	private final boolean mAllowNotLimitedVariablesInNegatedSubGoals;
	
	/** Flag to indicate if limited variables as operands of a ternary operator imply that the target is also limited. */
	private final boolean mAllowArithmeticPredicatesToImplyLimited;
	
	/** All head variables. */
	private final Set<IVariable> mHeadVariables = new HashSet<IVariable>();
	
	/** All variables from negated ordinary predicates. */
	private final Set<IVariable> mNegativeOrdinary = new HashSet<IVariable>();
	
	/** All variables from built-in predicates, EXCEPT the targets of positive, ternary predicates. */
	private final Set<IVariable> mBuiltin = new HashSet<IVariable>();
	
	/** All variables that appear in 'variable = variable' positive, equality predicates. */
	private final List<List<IVariable>> mArithmeticGroups = new ArrayList<List<IVariable>>();

	/** All limited variables. */
	private final Set<IVariable> mLimitedVariables = new HashSet<IVariable>();
}
