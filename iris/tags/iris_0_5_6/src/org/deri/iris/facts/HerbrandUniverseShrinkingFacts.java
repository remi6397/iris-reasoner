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
package org.deri.iris.facts;

import java.util.Collection;
import java.util.Set;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.storage.IRelation;

/**
 * Definition: Unsafe negation
 * A rule has a variable in a negated sub-goal that also occurs in the head, but nowhere else.
 * 
 * Definition: Unsafe
 * A rule has a variable that does not occur in a positive ordinary literal.
 *
 * Create a $UNIVERSE$(?X) relation.
 * 
 * To handle unsafe negation (Gelder, Well-founded semantics, p.24):
 * 	1.	For every rule, extract every occurrence of a non-ground-term, e.g. ?X or f(?Y)
 * 	2.	Substitute a distinct ground term (not found anywhere else) for the variable(s), e.g. '$X', f('$Y')
 * 	3.	Add this term to $UNIVERSE$
 * 
 * To handle all unsafe rules (with free variables in head or built-ins):
 * 	5.	Add all concrete terms that occur anywhere in the program (facts and rules) to $UNIVERSE$
 * 
 * For both:
 * 	6.	Add $UNIVERSE$(?variable) sub-goals for each free variable in each rule
 */
public class HerbrandUniverseShrinkingFacts implements IFacts
{
	public HerbrandUniverseShrinkingFacts( IFacts facts ) //, Collection<IRule> rules )
	{
		mFacts = facts;
		
		mUniverse = mFacts.get( UNIVERSE );
		
		// Copy all known facts in to the universe.
		// Possibly delay this??
		
		Set<IPredicate> startPredicates = mFacts.getPredicates();
		
		for( IPredicate predicate : startPredicates )
		{
			IRelation relation = mFacts.get( predicate );
			addToUniverse( relation );
		}
	}
	
	private void extractFacts( Collection<IRule> rules )
	{
		
	}
	
	private class UniverseAddingRelationAdaptor implements IRelation
	{
		public UniverseAddingRelationAdaptor( IRelation child )
		{
			assert child != null;
			mChild = child;
		}
		
		public boolean add( ITuple tuple )
        {
			boolean result = mChild.add( tuple );

			addToUniverse( tuple );

			return result;
        }

		public boolean addAll( IRelation relation )
        {
			boolean added = false;
			
			for( int t = 0; t < relation.size(); ++t )
			{
				ITuple tuple = relation.get( t );
				if( add( tuple ) )
					added = true;
			}

	        return added;
        }

		public ITuple get( int index )
        {
	        return mChild.get( index );
        }

		public int size()
        {
	        return mChild.size();
        }
		
		private final IRelation mChild;
	}
	
	public IRelation get( IPredicate predicate )
	{
		return new UniverseAddingRelationAdaptor( mFacts.get( predicate ) );
	}

	public Set<IPredicate> getPredicates()
	{
		return mFacts.getPredicates();
	}
	
	private void addToUniverse( IRelation relation )
	{
		for( int t = 0; t < relation.size(); ++t )
			addToUniverse( relation.get( t ) );
	}
	
	private void addToUniverse( ITuple tuple )
	{
		for( ITerm term : tuple )
			addToUniverse( term );
	}

	private void addToUniverse( ITerm term )
	{
		assert !( term instanceof IVariable );
		
		if( term instanceof IConstructedTerm )
		{
			IConstructedTerm constructed = (IConstructedTerm) term;
			for( ITerm param : constructed.getValue() )
				addToUniverse( param );
		}

		// NOTE
		// If term = f(g(h(1))) then 1, h(1), g(h(1)) and f(g(h(1))) will get added to the universe.
		mUniverse.add( Factory.BASIC.createTuple( term ) );
	}

	private final IFacts mFacts;
	
	private final IRelation mUniverse;
	
	public static final IPredicate UNIVERSE = Factory.BASIC.createPredicate( "$UNIVERSE$", 1 );
}
