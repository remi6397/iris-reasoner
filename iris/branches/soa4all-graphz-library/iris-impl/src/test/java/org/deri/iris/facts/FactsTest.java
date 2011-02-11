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
	
	final static IPredicate A = createPredicate( "PREDICATE", 1 );
	final static IPredicate B = createPredicate( "PREDICATE", 2 );
	final static IPredicate C = createPredicate( "PREDICATE", 3 );
	
	protected void setUp() throws Exception
	{
		mFacts = new Facts( new SimpleRelationFactory() );
	}
	
	public static void helperTestAdd( IFacts facts )
	{
		ITuple one = createTuple( 1 );
		ITuple two = createTuple( 1, 2 );
		ITuple three = createTuple( 1, 2, 3 );
		
		facts.get( A ).add( one );
		facts.get( B ).add( two );
		facts.get( C ).add( three );
		
		assertEquals( facts.get( A ).size(), 1 );
		assertEquals( facts.get( B ).size(), 1 );
		assertEquals( facts.get( C ).size(), 1 );

		assertEquals( facts.get( A ).get( 0 ), one );
		assertEquals( facts.get( B ).get( 0 ), two );
		assertEquals( facts.get( C ).get( 0 ), three );
	}

	public static void helperTestGetPredicates( IFacts facts )
	{
		facts.get( A );
		facts.get( B );
		facts.get( C );
		
		Set<IPredicate> predicates = facts.getPredicates();
		
		assertEquals( 3, predicates.size() );
		
		assertTrue( predicates.contains( A ) );
		assertTrue( predicates.contains( B ) );
		assertTrue( predicates.contains( C ) );
	}
	
	public void testAdd()
	{
		helperTestAdd( mFacts );
	}
	
	public void testGetPredicates()
	{
		helperTestGetPredicates( mFacts );
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
	
	public static IPredicate createPredicate( String symbol, int arity )
	{
		return Factory.BASIC.createPredicate( symbol, arity );
	}
	
	public static ITuple createTuple( Object ... termObjects )
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
