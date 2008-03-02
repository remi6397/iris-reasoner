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
package org.deri.iris.evaluation.wellfounded;

import java.util.ArrayList;
import java.util.List;
import org.deri.iris.Configuration;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.factory.Factory;
import org.deri.iris.facts.Facts;
import org.deri.iris.facts.IFacts;
import org.deri.iris.facts.OriginalFactsPreservingFacts;
import org.deri.iris.storage.IRelationFactory;

/**
 * Program doubler for well-founded semantics evaluation strategy.
 */
public class ProgramDoubler
{
	public static final String NEGATED_PREDICATE_SUFFIX = "_$PRIMED$";
	
	public ProgramDoubler( List<IRule> rules, IFacts facts, Configuration configuration )
	{
		mOriginalRules = rules;
		mOriginalFacts = facts;
		mConfiguration = configuration;
		
		calculateStartingRuleBase();
		calculateNegativeRuleBase();
		calculatePositiveRuleBase();
	}
	
	public IFacts extractPositiveFacts( IFacts mixed )
	{
		IFacts result = new Facts( mConfiguration.relationFactory );
		
		for( IPredicate predicate : mixed.getPredicates() )
		{
			if( ! predicate.getPredicateSymbol().endsWith( NEGATED_PREDICATE_SUFFIX ) )
				result.get( predicate ).addAll( mixed.get( predicate ) );
		}
		
		return result;
	}
	
	public IFacts extractNegativeFacts( IFacts mixed )
	{
		IFacts result = new Facts( mConfiguration.relationFactory );
		
		for( IPredicate predicate : mixed.getPredicates() )
		{
			if( predicate.getPredicateSymbol().endsWith( NEGATED_PREDICATE_SUFFIX ) )
				result.get( predicate ).addAll( mixed.get( predicate ) );
		}
		
		return result;
	}
	
	public IFacts getPositiveStartingFacts()
	{
		return new OriginalFactsPreservingFacts( mOriginalFacts, mConfiguration.relationFactory );
	}
	
	public IFacts getNegativeStartingFacts()
	{
		return new InvertingFacts(
						new OriginalFactsPreservingFacts( mOriginalFacts, mConfiguration.relationFactory ),
						mConfiguration.relationFactory );
	}
	
	public List<IRule> getStartingRuleBase()
	{
		return mStartingRules;
	}
	
	public List<IRule> getNegativeRuleBase()
	{
		return mNegativeRules;
	}
	
	public List<IRule> getPositiveRuleBase()
	{
		return mPositiveRules;
	}
	
	private void calculateStartingRuleBase()
	{
		for( IRule rule : mOriginalRules )
		{
			boolean containsNegatedLiteral = false;
			
			for( ILiteral literal : rule.getBody() )
			{
				if( ! literal.isPositive() && ! literal.getAtom().isBuiltin() )
				{
					containsNegatedLiteral = true;
					break;
				}
			}
			
			if( ! containsNegatedLiteral )
				mStartingRules.add( rule );
		}
	}

	private void calculateNegativeRuleBase()
	{
		for( IRule rule : mOriginalRules )
		{
			List<ILiteral> newHead = new ArrayList<ILiteral>();
			for( ILiteral literal : rule.getHead() )
			{
				newHead.add( makePrimedLiteral( literal ) );
			}
			
			List<ILiteral> newBody = new ArrayList<ILiteral>();
			for( ILiteral literal : rule.getBody() )
			{
				if( literal.isPositive() && ! literal.getAtom().isBuiltin() )
				{
					newBody.add( makePrimedLiteral( literal ) );
				}
				else
				{
					newBody.add( literal );
				}
			}
			
			mNegativeRules.add( Factory.BASIC.createRule( newHead, newBody ) );
		}
	}
	
	private void calculatePositiveRuleBase()
	{
		for( IRule rule : mOriginalRules )
		{
			List<ILiteral> newBody = new ArrayList<ILiteral>();
			
			for( ILiteral literal : rule.getBody() )
			{
				if( ! literal.isPositive() && ! literal.getAtom().isBuiltin() )
				{
					newBody.add( makePrimedLiteral( literal ) );
				}
				else
				{
					newBody.add( literal );
				}
			}
			
			mPositiveRules.add( Factory.BASIC.createRule( rule.getHead(), newBody ) );
		}
	}

	private static ILiteral makePrimedLiteral( ILiteral literal )
	{
		IPredicate predicate = literal.getAtom().getPredicate();
		ITuple tuple = literal.getAtom().getTuple();
		
		return Factory.BASIC.createLiteral( literal.isPositive(), makePrimedPredicate( predicate ), tuple );
	}

	private static IPredicate makePrimedPredicate( IPredicate predicate )
	{
		String newPredicateSymbol = predicate.getPredicateSymbol() + NEGATED_PREDICATE_SUFFIX;
		return Factory.BASIC.createPredicate( newPredicateSymbol, predicate.getArity() );
	}

	private static class InvertingFacts extends Facts
	{
		InvertingFacts( IFacts positiveFacts, IRelationFactory relationFactory )
		{
			super( relationFactory );

			for( IPredicate predicate : positiveFacts.getPredicates() )
			{
				mPredicateRelationMap.put( makePrimedPredicate( predicate ), positiveFacts.get( predicate ) );
			}
		}
	}
	
	private final List<IRule> mOriginalRules;
	
	private final IFacts mOriginalFacts;
	
	private final Configuration mConfiguration;
	
	private final List<IRule> mStartingRules = new ArrayList<IRule>();
	
	private final List<IRule> mNegativeRules = new ArrayList<IRule>();
	
	private final List<IRule> mPositiveRules = new ArrayList<IRule>();
}
