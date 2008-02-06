/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;
import org.deri.iris.storage.IRelation;
import org.deri.iris.storage.simple.SimpleRelationFactory;

public class FactsTest extends TestCase
{
	IFacts mFacts;
	
	final IPredicate A = createPredicate( "PREDICATE", 1 );
	final IPredicate B = createPredicate( "PREDICATE", 2 );
	final IPredicate C = createPredicate( "PREDICATE", 3 );
	
	protected void setUp() throws Exception
	{
		mFacts = new Facts( new SimpleRelationFactory() );
	}

	public void testAdd()
	{
		ITuple one = createTuple( 1 );
		ITuple two = createTuple( 1, 2 );
		ITuple three = createTuple( 1, 2, 3 );
		
		mFacts.get( A ).add( one );
		mFacts.get( B ).add( two );
		mFacts.get( C ).add( three );
		
		assertEquals( mFacts.get( A ).size(), 1 );
		assertEquals( mFacts.get( B ).size(), 1 );
		assertEquals( mFacts.get( C ).size(), 1 );

		assertEquals( mFacts.get( A ).get( 0 ), one );
		assertEquals( mFacts.get( B ).get( 0 ), two );
		assertEquals( mFacts.get( C ).get( 0 ), three );
	}
	
	public void testGetPredicates()
	{
		mFacts.get( A );
		mFacts.get( B );
		mFacts.get( C );
		
		Set<IPredicate> predicates = mFacts.getPredicates();
		
		assertEquals( 3, predicates.size() );
		
		assertTrue( predicates.contains( A ) );
		assertTrue( predicates.contains( B ) );
		assertTrue( predicates.contains( C ) );
	}
	
	public void testConstructor()
	{
		final ITuple tuple = createTuple( 1, 2, 3 );
		
		final IRelation relation = new SimpleRelationFactory().createRelation();
		relation.add( tuple );
		
		final Map<IPredicate,IRelation> startMap = new HashMap<IPredicate,IRelation>();
		
		startMap.put( C, relation );
		
		final IFacts newFacts = new Facts( startMap, new SimpleRelationFactory() );
		
		// Check that the starting facts made it in to the Facts object.
		assertEquals( tuple, newFacts.get( C ).get( 0 ) );

		// Now check the changing the original map of facts doesn't affect it.
		startMap.put( B, new SimpleRelationFactory().createRelation() );
		assertEquals( 1, newFacts.getPredicates().size() );
	}
	
	private IPredicate createPredicate( String symbol, int arity )
	{
		return Factory.BASIC.createPredicate( symbol, arity );
	}
	
	private ITuple createTuple( Object ... termObjects )
	{
		List<ITerm> terms = new ArrayList<ITerm>();

		for( Object o : termObjects )
		{
			if( o instanceof Integer )
				terms.add( CONCRETE.createInteger( (Integer) o ) );
			else if( o instanceof String )
				terms.add( TERM.createString( (String) o ) );
		}
		
		return BASIC.createTuple( terms );
	}


}
