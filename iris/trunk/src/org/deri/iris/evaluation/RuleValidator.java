/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2007 Digital Enterprise Research Institute (DERI), 
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.deri.iris.RuleUnsafeException;

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
 * Furthermore, variables that only appear as a target of a ternary predicate can be considered
 * limited if the operands of the predicate are themselves limited.
 *
 * These two relaxations of the definition of a safe rule are configurable (on/off).
 */
public class RuleValidator
{
	/**
	 * Constructor. Each instance of this rule can process exactly one rule, after which it must be
	 * discarded.
	 * @param variablesInNegatedSubGoalsNeedNotBeLimited false, if the strict Ullman definition
	 * should be enforced for variables in negated sub-goals.
	 * @param limitedTernaryOperandsImplyLimitedResult false, if the strict Ullman definition
	 * should be enforced for variables in that are targets in ternary built-in predicates.
	 */
	public RuleValidator( 	boolean variablesInNegatedSubGoalsNeedNotBeLimited,
							boolean limitedTernaryOperandsImplyLimitedResult )
	{
		mVariablesInNegatedSubGoalsNeedNotBeLimited = variablesInNegatedSubGoalsNeedNotBeLimited;
		mLimitedTernaryOperandsImplyLimitedResult = limitedTernaryOperandsImplyLimitedResult;
	}
	
	/**
	 * Add a variable that appears in the rule head.
	 * @param variable The variable name.
	 */
	public void addHeadVariable( String variable )
	{
		mHeadVariables.add( variable );
	}
	
	/**
	 * Add a variable that appears in an ordinary predicate.
	 * @param positive true if the predicate is positive, i.e. not negated.
	 * @param variable The variable name.
	 */
	public void addVariableFromOrdinaryPredicate( boolean positive, String variable )
	{
		if ( positive )
			mLimitedVariables.add( variable );
		else
			mNegativeOrdinary.add( variable );
	}

	/**
	 * Add variable from any built-in predicate other than positive equality and ternary arithmetic
	 * in predicates, e.g. the IS<type>() predicates.
	 * e.g.
	 *   ISSTRING( ?X  )
	 * 
	 * @param variable 'X' in this example
	 */
	public void addVariableFromBuiltinPredicate( String variable )
	{
		if ( variable != null )
			mBuiltin.add( variable );
	}

	/**
	 * Add variables from binary built-in predicates, i.e. the comparison/equality predicates:
	 * 	 < <= > >= = !=
	 * e.g.
	 *   ?X < ?Y
	 * 
	 * Pass null values for variables that are ground terms.
	 * 
	 * @param operand1 'X' in this example
	 * @param operand2 'Y' in this example
	 */
	public void addVariablesFromPositiveEqualityPredicate( String operand1, String operand2 )
	{
		// These will have to be limited too.
		if ( operand1 != null )
			mBuiltin.add( operand1 );
		
		if ( operand2 != null )
			mBuiltin.add( operand2 );

		if ( operand1 != null && operand2 != null )
		{
			mEqualityPairs.add( new String[]{ operand1, operand2 } );
		}
		else if ( operand1 != null && operand2 == null )
		{
			mLimitedVariables.add( operand1 );
		}
		else if ( operand1 == null && operand2 != null )
		{
			mLimitedVariables.add( operand2 );
		}
	}

	/**
	 * Add variables from ternary built-in predicates, i.e. the arithmetic predicates.
	 * e.g.
	 *   ?X + ?Y = ?Z
	 * 
	 * Pass null values for variables that are ground terms.
	 * 
	 * @param operand1 'X' in this example
	 * @param operand2 'Y' in this example
	 * @param target 'Z' in this example
	 */
	public void addVariablesFromPositiveArithmeticPredicate( String operand1, String operand2, String target )
	{
		if ( operand1 != null )
			mBuiltin.add( operand1 );
		
		if ( operand2 != null )
			mBuiltin.add( operand2 );

		if ( target != null )
			mTernaryTarget.add( target );
	}

	/**
	 * Perform the safety check and throws an exception if not safe.
	 * @throws RuleUnsafeException If the rule is not safe.
	 */
	public void isSafe() throws RuleUnsafeException
	{
		processBuiltins();
		
		if ( ! mLimitedVariables.containsAll( mHeadVariables ) )
		{
			mHeadVariables.removeAll( mLimitedVariables );
			
			throw new RuleUnsafeException( "Head variables " + toString( mHeadVariables ) + " are not limited." );
		}
		
		if ( ! mLimitedVariables.containsAll( mBuiltin ) )
		{
			mBuiltin.removeAll( mLimitedVariables );
			
			throw new RuleUnsafeException( "Variables " + toString( mBuiltin ) + " from built-in predicates are not limited." );
		}
		
		if ( ! mVariablesInNegatedSubGoalsNeedNotBeLimited )
		{
			if ( ! mLimitedVariables.containsAll( mNegativeOrdinary ) )
			{
				mNegativeOrdinary.removeAll( mLimitedVariables );
				
				throw new RuleUnsafeException( "Variables " + toString( mNegativeOrdinary ) + " from negated sub-goals are not limited." );
			}
		}
		
		if ( ! mLimitedTernaryOperandsImplyLimitedResult )
		{
			if ( ! mLimitedVariables.containsAll( mTernaryTarget ) )
			{
				mTernaryTarget.removeAll( mLimitedVariables );
				
				throw new RuleUnsafeException( "Variables " + toString( mTernaryTarget ) + " from built-in ternary predicates are not limited." );
			}
		}
	}
	
	/**
	 * Do the special case handling of built-in predicates.
	 */
	private void processBuiltins()
	{
		// Process the positive equality predicates
		boolean changed = true;
		while( changed )
		{
			changed = false;
			for ( String[] equalityPair : mEqualityPairs )
			{
				if( mLimitedVariables.contains( equalityPair[ 0 ] ) && ! mLimitedVariables.contains( equalityPair[ 1 ] ) ||
					! mLimitedVariables.contains( equalityPair[ 0 ] ) && mLimitedVariables.contains( equalityPair[ 1 ] ) )
				{
					mLimitedVariables.add( equalityPair[ 0 ] );
					mLimitedVariables.add( equalityPair[ 1 ] );
					
					changed = true;
				}
			}
		}

		// Process the ternary predicates (e.g. ?X + ?Y = ?Z)
		if ( mLimitedTernaryOperandsImplyLimitedResult )
		{
			// Cheat a bit with the ternary predicates!
			// We know that if the operands are not limited this will be caught first.
			// Which means that all ternary targets are automatically limited.
			mLimitedVariables.addAll( mTernaryTarget );
		}
	}
	
	/**
	 * Utility method to convert a collection of IVariable terms to a human-readable string.
	 * @param variables The variables to find the names for.
	 * @return The string-ised names.
	 */
	private String toString( Collection<String> variables )
	{
		StringBuilder buffer = new StringBuilder();

		boolean first = true;
		for ( String v : variables )
		{
			if ( first )
				first = false;
			else
				buffer.append( ", " );
			
			buffer.append( '\'' );
			buffer.append( v );
			buffer.append( '\'' );
		}
		
		return buffer.toString();
	}

	/** Flag to indicate if variables in negated sub goals must be limited or not. */
	private final boolean mVariablesInNegatedSubGoalsNeedNotBeLimited;
	
	/** Flag to indicate if limited variables as operands of a ternary operator imply that the target is also limited. */
	private final boolean mLimitedTernaryOperandsImplyLimitedResult;
	
	/** All head variables. */
	private final Set<String> mHeadVariables = new HashSet<String>();
	
	/** All variables from negated ordinary predicates. */
	private final Set<String> mNegativeOrdinary = new HashSet<String>();
	
	/** All variables from built-in predicates, EXCEPT the targets of positive, ternary predicates. */
	private final Set<String> mBuiltin = new HashSet<String>();
	
	/** All variables that are the targets of positive, built-in ternary predicates. */
	private final Set<String> mTernaryTarget = new HashSet<String>();

	/** All variables that appear in 'variable = variable' positive, equality predicates. */
	private final List<String[]> mEqualityPairs = new ArrayList<String[]>();

	/** All limited variables. */
	private final Set<String> mLimitedVariables = new HashSet<String>();
}
