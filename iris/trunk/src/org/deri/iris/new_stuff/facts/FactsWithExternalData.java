package org.deri.iris.new_stuff.facts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;
import org.deri.iris.new_stuff.storage.IRelation;


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
	    	
	    	// TODO Change IDataSource to use the correct IRelation type
	    	for( IDataSource dataSource : mExternalDataSources )
	    		; // dataSource.get( predicate, from, to, result );

	    }
	    
	    return result;
    }


	public Set<IPredicate> getPredicates()
    {
	    return mFacts.getPredicates();
    }


	private final IFacts mFacts;
	
	private final List<IDataSource> mExternalDataSources;
	
	private Set<IPredicate> mExternalPredicatesAlreadyFetched = new HashSet<IPredicate>();
}
