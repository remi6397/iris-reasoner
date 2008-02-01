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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.storage.IRelation;
import org.deri.iris.storage.IRelationFactory;
import org.deri.iris.storage.simple.SimpleRelationFactory;

/**
 * A manager for all facts stored in a knowledge-base.
 */
public class Facts implements IFacts
{
	/**
	 * Constructor.
	 */
	public Facts()
	{
		mRelationFactory = new SimpleRelationFactory();
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
	
	/** The map storing the predicate-relation relationship. */
	private final Map<IPredicate, IRelation> mPredicateRelationMap = new HashMap<IPredicate, IRelation>();
	
	private final IRelationFactory mRelationFactory;
}
