/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.facts;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.storage.IRelation;
import org.deri.iris.storage.IRelationFactory;

/**
 * A facts adaptor that allows modification (i.e. adding more facts) without modifying
 * the original facts.
 */
public class OriginalFactsPreservingFacts implements IFacts
{
	/**
	 * Constructor.
	 * Decorate the given facts object.
	 * @param original The original facts that will be preserved, i.e. not altered by this class.
	 * @param relationFactory The relation factory to use.
	 */
	public OriginalFactsPreservingFacts( IFacts original, IRelationFactory relationFactory )
	{
		mOriginalFacts = original;
		mRelationFactory = relationFactory;
	}
	
	public IRelation get( IPredicate predicate )
    {
		IRelation adaptor = mPredicateRelationMap.get( predicate );
		
		if( adaptor == null )
		{
			adaptor = new OriginalPreservingAdaptor( mOriginalFacts.get( predicate ) );
			
			mPredicateRelationMap.put( predicate, adaptor );
		}
		
		return adaptor;
    }

	public Set<IPredicate> getPredicates()
    {
	    return mOriginalFacts.getPredicates();
    }

	@Override
    public String toString()
    {
		StringBuilder result = new StringBuilder();
		
		for( IPredicate predicate : getPredicates() )
		{
			IRelation relation = get( predicate );
			for( int t = 0; t < relation.size(); ++t )
			{
				ITuple tuple = relation.get( t );
				result.append( predicate.getPredicateSymbol() );
				result.append( tuple );
				result.append( '.' );
			}
		}

	    return result.toString();
    }
	
	/**
	 * Original preserving relation adaptor.
	 */
	private class OriginalPreservingAdaptor implements IRelation
	{
		OriginalPreservingAdaptor( IRelation original )
		{
			mOriginal = original;
			mAddedFacts = mRelationFactory.createRelation();
		}

		public boolean add( ITuple tuple )
        {
			if( mOriginal.contains( tuple ) )
				return false;
			
			return mAddedFacts.add( tuple );
        }

		public boolean addAll( IRelation relation )
        {
			boolean changed = false;
			for( int t = 0; t < relation.size(); ++t )
			{
				if( add( relation.get( t ) ) )
					changed = true;
			}
			
	        return changed;
        }

		public ITuple get( int index )
        {
			if( index < mOriginal.size() )
				return mOriginal.get( index );
			
	        return mAddedFacts.get( index - mOriginal.size() );
        }

		public int size()
        {
	        return mOriginal.size() + mAddedFacts.size();
        }
		
		public boolean contains( ITuple tuple )
        {
	        return mOriginal.contains( tuple ) || mAddedFacts.contains( tuple );
        }

		@Override
        public String toString()
        {
	        return mOriginal.toString() + mAddedFacts.toString();
        }

		private final IRelation mOriginal;
		private final IRelation mAddedFacts;
	}

	/** The map storing the predicate-relation relationship. */
	private final Map<IPredicate, IRelation> mPredicateRelationMap = new HashMap<IPredicate, IRelation>();

	/** The relation factory for new relations. */
	private final IRelationFactory mRelationFactory;
	
	/** The decorated original facts. */
	private final IFacts mOriginalFacts;
}
