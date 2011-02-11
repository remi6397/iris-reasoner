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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;
import org.deri.iris.storage.IRelation;


public class FactsWithExternalData implements IFacts
{
	/**
	 * Constructor.
	 */
	public FactsWithExternalData( IFacts facts, List<IDataSource> externalDataSources )
	{
		mFacts = facts;
		mExternalDataSources = new ArrayList<IDataSource>( externalDataSources );
	}
	

	public IRelation get( IPredicate predicate )
    {
	    IRelation result = mFacts.get( predicate );
	    
	    // If we haven't got the external data for this predicate yet...
	    if( mExternalPredicatesAlreadyFetched.add( predicate ) )
	    {
	    	// ... then get it now.
	    	
	    	ITuple from = Factory.BASIC.createTuple( new ITerm[ predicate.getArity() ] );
	    	ITuple to   = Factory.BASIC.createTuple( new ITerm[ predicate.getArity() ] );
	    	
	    	for( IDataSource dataSource : mExternalDataSources )
	    		dataSource.get( predicate, from, to, result );

	    }
	    
	    return result;
    }


	public Set<IPredicate> getPredicates()
    {
	    return mFacts.getPredicates();
    }

    /**
     * Return all facts. The format of the resulting string is parse-able.
     * @return a parse-able string containing all facts
     */
    public String toString()
    {
    	return mFacts.toString();
    }

	private final IFacts mFacts;
	
	private final List<IDataSource> mExternalDataSources;
	
	private Set<IPredicate> mExternalPredicatesAlreadyFetched = new HashSet<IPredicate>();
}
