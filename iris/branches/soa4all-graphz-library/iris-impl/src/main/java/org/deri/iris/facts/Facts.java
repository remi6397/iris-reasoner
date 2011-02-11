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
 * A manager for all facts stored in a knowledge-base.
 */
public class Facts implements IFacts
{
	/**
	 * Constructor.
	 */
	public Facts( IRelationFactory relationFactory )
	{
		mRelationFactory = relationFactory;
	}
	
	/**
	 * Construct a Facts object from a predicate-relation map. 
	 * @param rawFacts The facts to add.
	 */
	public Facts( Map<IPredicate,IRelation> rawFacts, IRelationFactory relationFactory )
	{
		mRelationFactory = relationFactory;
		mPredicateRelationMap.putAll( rawFacts );
	}
	
	/* (non-Javadoc)
     * @see org.deri.iris.new_stuff.facts.IFacts#get(org.deri.iris.api.basics.IPredicate)
     */
	public IRelation get( IPredicate predicate )
	{
		IRelation relation = mPredicateRelationMap.get( predicate );
		
		if( relation == null )
		{
			relation = mRelationFactory.createRelation();
			mPredicateRelationMap.put( predicate, relation );
		}
		
		return relation;
	}
	
	/* (non-Javadoc)
     * @see org.deri.iris.new_stuff.facts.IFacts#getPredicates()
     */
	public Set<IPredicate> getPredicates()
	{
		return mPredicateRelationMap.keySet();
	}
	
	@Override
    public String toString()
    {
		StringBuilder result = new StringBuilder();
		
		for( Map.Entry<IPredicate, IRelation> entry : mPredicateRelationMap.entrySet() )
		{
			IRelation relation = entry.getValue();
			IPredicate predicate = entry.getKey();
			
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

	/** The map storing the predicate-relation relationship. */
	protected final Map<IPredicate, IRelation> mPredicateRelationMap = new HashMap<IPredicate, IRelation>();
	
	protected final IRelationFactory mRelationFactory;
}
