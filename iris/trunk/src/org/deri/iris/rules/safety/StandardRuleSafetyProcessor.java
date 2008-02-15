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
package org.deri.iris.rules.safety;

import java.util.ArrayList;
import java.util.List;
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
import org.deri.iris.rules.IRuleSafetyProcessor;
import org.deri.iris.rules.RuleValidator;

/**
 * A standard rule-safety processor that checks if all variables are limited, a la Ullman.
 * If not, a rule unsafe exception is thrown.
 */
public class StandardRuleSafetyProcessor implements IRuleSafetyProcessor
{
	/**
	 * Constructor.
	 * @param allowUnlimitedVariablesInNegatedOrdinaryPredicates Indicates if a rule can still be
	 * considered safe if one or more variables occur
	 * in negative ordinary predicates and nowhere else, e.g.
	 * p(X) :- q(X), not r(Y)
	 * if true, the above rule would be safe
	 * @param ternaryTargetsImplyLimited Indicates if ternary arithmetic built-ins can be
	 * used to deduce limited variables, e.g.
	 * p(Z) :- q(X, Y), X + Y = Z
	 * if true, then Z would be considered limited.
	 */
	public StandardRuleSafetyProcessor(
					boolean allowUnlimitedVariablesInNegatedOrdinaryPredicates,
					boolean ternaryTargetsImplyLimited )
	{
		mAllowUnlimitedVariablesInNegatedOrdinaryPredicates = allowUnlimitedVariablesInNegatedOrdinaryPredicates;
		mTernaryTargetsImplyLimited = ternaryTargetsImplyLimited;
	}

	public IRule process( IRule rule ) throws RuleUnsafeException
	{
		RuleValidator validator = new RuleValidator(
						mAllowUnlimitedVariablesInNegatedOrdinaryPredicates, 
						mTernaryTargetsImplyLimited );
		
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
	 * Get the variable names of variable terms in a literal.
	 * @param literal The literal to be processed.
	 * @return The names of variables.
	 */
	private List<String> extractVariableNames( ILiteral literal )
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

	private final boolean mAllowUnlimitedVariablesInNegatedOrdinaryPredicates;
	private final boolean mTernaryTargetsImplyLimited;
}
