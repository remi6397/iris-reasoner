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
package org.deri.iris.rules.stratification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.basics.Tuple;
import org.deri.iris.rules.IRuleStratifier;
import org.deri.iris.rules.RuleManipulator;
import org.deri.iris.rules.stratification.LocalStratificationDecorator.Adornment;
import org.deri.iris.rules.stratification.LocalStratificationDecorator.MatchType;

/**
 * A local stratification algorithm.
 * This algorithm will 'split' rules and so will likely return more rules
 * than were provided.
 * @see org.deri.iris.rules.IRuleStratifier#stratify()
 * @return The stratified list of rules or null if the stratification failed.
 */
public class LocalStratifier implements IRuleStratifier
{
	public LocalStratifier( boolean strict )
	{
		mStrict = strict;
	}
	
	public List<Collection<IRule>> stratify( Collection<IRule> rules )
	{
		mRules.clear();
		
		adornRules( rules );
		splitRules();
		
		int ruleStratum[] = new int[ mRules.size() ];
		
		final int ruleCount = mRules.size();
		int highest = 0;
		boolean change = true;

		while ((highest <= ruleCount ) && change)
		{
			change = false;
			for( int r = 0; r < mRules.size(); ++r )
			{
				IRule currentRule = mRules.get( r ).getRule();
				
				for (final ILiteral bl : currentRule.getBody())
				{
					for( int r2 = 0; r2 < mRules.size(); ++r2 )
					{
						// We even check for a negative dependency on self!
						LocalStratificationDecorator adaptor = mRules.get( r2 );

						if( adaptor.getRule().getHead().get( 0 ).getAtom().getPredicate().equals( bl.getAtom().getPredicate() ) )
						{
							MatchType match = adaptor.match( bl.getAtom().getTuple() );
							if( match != MatchType.NONE )
							{							
								if( bl.isPositive() )
								{
									if( ruleStratum[ r ] < ruleStratum[ r2 ] )
									{
										ruleStratum[ r ] = ruleStratum[ r2 ];
										change = true;
									}
								}
								else
								{
									if( ruleStratum[ r ] <= ruleStratum[ r2 ] )
									{
										ruleStratum[ r ] = ruleStratum[ r2 ] + 1;
										change = true;
									}
								}
								highest = Math.max( ruleStratum[ r ], highest );
							}
						}
					}
				}
			}
		}
		
		if( highest < ruleCount )
		{
			List<Collection<IRule>> result = new ArrayList<Collection<IRule>>();
			
			for( int stratum = 0; stratum <= highest; ++stratum )
				result.add( new HashSet<IRule>() );

			for( int r = 0; r < mRules.size(); ++r )
				result.get( ruleStratum[ r ] ).add(  mRules.get( r ).getRule() );
			
			return result;
		}
		else
			return null;
	}
	
	/**
	 * For every literal of every rule, if the literal is negative and not a built-in:
	 * split any dependent rules
	 */
	private void splitRules()
	{
		boolean changed;
		
		do
		{
			changed = false;

			for( LocalStratificationDecorator ruleAdaptor : mRules )
			{
				IRule rule = ruleAdaptor.getRule();
				
				if( splitRulesForLiteralsFromOneRule( rule ) )
				{
					// At least one rule has been split, so start again at the beginning
					changed = true;
					break;
				}
			}
		} while( changed );
	}

	/**
	 * Split rules 'currentRule' depends on
	 * @param currentRule The rule to examine.
	 * @return true, if any dependent rules were split.
	 */
	private boolean splitRulesForLiteralsFromOneRule( IRule currentRule )
	{
		// For each literal in the body
		for( ILiteral literal : currentRule.getBody() )
		{
			// If the literal is negated
			if( ! literal.isPositive() )
			{
				IAtom atom = literal.getAtom();

				// If it is not a built-in (can't split built-ins!)
				if( ! atom.isBuiltin() )
				{
					ITuple tuple = atom.getTuple();
					
					List<IVariable> variables = tuple.getAllVariables();
					boolean hasConstants = variables.size() != tuple.size();
					
					if( hasConstants )
					{
						if( splitRulesForAtom( atom ) )
							return true;
					}
				}
			}
		}
		
		return false;
	}

	/**
	 * Split rules that can produce tuple for 'atom'.
	 * @param atom The atom to examine.
	 * @return true, if any rules were split.
	 */
	private boolean splitRulesForAtom( IAtom atom )
	{
		boolean somethingSplit = false;
		boolean changed;
		
		do
		{
			changed = false;

			for( int ruleIndex = 0; ruleIndex < mRules.size(); ++ruleIndex )
			{
				LocalStratificationDecorator decorator = mRules.get( ruleIndex );
				IRule rule = decorator.getRule();
				
				// Predicate has same name and arity?
				if( atom.getPredicate().equals( rule.getHead().get( 0 ).getAtom().getPredicate() ) )
				{
					ITuple negatedSubGoalTuple = atom.getTuple();
					
					// If we have a partial match (subset)
					if( decorator.match( negatedSubGoalTuple ) == MatchType.CONSUMES_SUBSET )
					{
						// Remove the current rule
						mRules.remove( ruleIndex );
						
						LocalStratificationDecorator exactMatchRule = makeExactMatchRule( decorator, negatedSubGoalTuple );
						LocalStratificationDecorator noMatchRule = makeNoMatchRule( decorator, negatedSubGoalTuple );
						
						mRules.add( exactMatchRule );
						mRules.add( noMatchRule );
						
						somethingSplit = true;
						changed = true;
						break;
					}
				}
			}
		} while( changed );
		
		return somethingSplit;
	}
	
	/**
	 * Create a rule that will always produce tuples that are an exact match the given rule.
	 * @param decorator The decorated rule.
	 * @param negatedSubGoalTuple The tuple from the dependent rule's negated sub-goal.
	 * @return The new rule.
	 */
	private LocalStratificationDecorator makeExactMatchRule( LocalStratificationDecorator decorator, ITuple negatedSubGoalTuple )
	{
		IRule rule = decorator.getRule();
		List<LocalStratificationDecorator.Adornment> adornments = decorator.getAdornments();
		ITuple ruleHead = rule.getHead().get( 0 ).getAtom().getTuple();

		RuleManipulator rm = new RuleManipulator();
		
		assert ruleHead.size() == adornments.size();
		assert ruleHead.size() == negatedSubGoalTuple.size();
		
		List<LocalStratificationDecorator.Adornment> newAdornments = new ArrayList<LocalStratificationDecorator.Adornment>();
		
		for( int t = 0; t < adornments.size(); ++t )
		{
			ITerm headTerm = ruleHead.get( t );
			ITerm subGoalTerm = negatedSubGoalTuple.get( t );
			LocalStratificationDecorator.Adornment adornment = adornments.get( t );
			
			assert ! headTerm.isGround() || ! subGoalTerm.isGround() || headTerm.equals( subGoalTerm );
			assert 	( adornment.getPositiveConstant() != null &&   headTerm.isGround() ) ||
					( adornment.getPositiveConstant() == null && ! headTerm.isGround() );
			
			if( ! headTerm.isGround() && subGoalTerm.isGround() )
			{
				// Must add VAR = const term to rule and adorn
				IVariable variable = (IVariable) headTerm;
				
				rule = rm.addEquality( rule, variable, subGoalTerm );
				rule = rm.replaceVariablesWithConstants( rule, mStrict );
				rule = rm.removeUnnecessaryEqualityBuiltins( rule );
				
				adornment = adornment.setConstantTerm( subGoalTerm );
			}
			
			newAdornments.add( adornment );
		}

		return new LocalStratificationDecorator( rule, newAdornments );
	}
	
	/**
	 * Create a rule that will never produce tuples that are an exact match the given rule.
	 * @param decorator The decorated rule.
	 * @param negatedSubGoalTuple The tuple from the dependent rule's negated sub-goal.
	 * @return The new rule.
	 */
	private LocalStratificationDecorator makeNoMatchRule( LocalStratificationDecorator adaptor, ITuple negatedSubGoalTuple )
	{
		IRule rule = adaptor.getRule();
		List<LocalStratificationDecorator.Adornment> adornments = adaptor.getAdornments();
		ITuple ruleHead = rule.getHead().get( 0 ).getAtom().getTuple();

		RuleManipulator rm = new RuleManipulator();
		
		assert ruleHead.size() == adornments.size();
		assert ruleHead.size() == negatedSubGoalTuple.size();
		
		List<LocalStratificationDecorator.Adornment> newAdornments = new ArrayList<LocalStratificationDecorator.Adornment>();
		
		for( int t = 0; t < adornments.size(); ++t )
		{
			ITerm headTerm = ruleHead.get( t );
			ITerm subGoalTerm = negatedSubGoalTuple.get( t );
			LocalStratificationDecorator.Adornment adornment = adornments.get( t );
			
			assert ! headTerm.isGround() || ! subGoalTerm.isGround() || headTerm.equals( subGoalTerm );
			assert 	( adornment.getPositiveConstant() != null &&   headTerm.isGround() ) ||
					( adornment.getPositiveConstant() == null && ! headTerm.isGround() );
			
			if( ! headTerm.isGround() && subGoalTerm.isGround() )
			{
				// Must add VAR != const term to rule and adorn
				IVariable variable = (IVariable) headTerm;
				
				rule = rm.addInequality( rule, variable, subGoalTerm );
				
				adornment = adornment.addNegatedConstant( subGoalTerm );
			}
			
			newAdornments.add( adornment );
		}

		return new LocalStratificationDecorator( rule, newAdornments );
	}
	
	@Override
    public String toString()
    {
		StringBuilder buffer = new StringBuilder();
		
		for( LocalStratificationDecorator adaptor : mRules )
		{
			buffer.append( adaptor.toString() ).append( "\r\n" );
		}
		
		return buffer.toString();
    }

	/**
	 * Put the adornment decorations on the rules.
	 * @param rules The rules to stratify.
	 */
	private void adornRules( Collection<IRule> rules )
	{
		RuleManipulator rm = new RuleManipulator();
		
		for( IRule rule : rules )
		{
			IRule r = rm.replaceVariablesWithConstants( rule, mStrict );
			r = rm.replaceVariablesWithVariables( r );
			r = rm.removeUnnecessaryEqualityBuiltins( r );
			r = rm.removeDuplicateLiterals( r );
			
			List<Adornment> adornments = new ArrayList<Adornment>();

			// TODO For the time being, this is only for rules with a single head predicate
			Tuple tuple = (Tuple) r.getHead().get( 0 ).getAtom().getTuple();
			for( ITerm term : tuple )
			{
				Adornment adornment = new Adornment();
				
				if( term.isGround() )
					adornment = adornment.setConstantTerm( term );
				else
				{
					// Term is a variable
					// Scan rule body looking for BIN_OP( variable, constant )
					// (Equality has already been taken care of by pushing constants in to variables)
					// !=, <, >, not =, not >=, not <=, imply variable != constant adornment
					// If constant is numeric, then can add adornment for not every numeric type
					
				}

				adornments.add( adornment );
			}
			
			mRules.add( new LocalStratificationDecorator( rule, adornments ) );
		}
	}
	
	/** The list of rules to process. */
	private final List<LocalStratificationDecorator> mRules = new ArrayList<LocalStratificationDecorator>();
	
	private boolean mStrict;
}
