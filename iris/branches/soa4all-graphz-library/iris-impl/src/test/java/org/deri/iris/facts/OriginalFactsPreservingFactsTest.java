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

import junit.framework.TestCase;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.storage.IRelationFactory;
import org.deri.iris.storage.simple.SimpleRelationFactory;

public class OriginalFactsPreservingFactsTest extends TestCase
{
	private IFacts originalFacts;
	private IFacts preservingFacts;

	protected void setUp() throws Exception
	{
		originalFacts = new Facts( mRelationFactory );
		preservingFacts = new OriginalFactsPreservingFacts( originalFacts, mRelationFactory );
	}

	public void testAdd()
	{
		FactsTest.helperTestAdd( preservingFacts );
	}

	public void testGetPredicates()
	{
		FactsTest.helperTestGetPredicates( preservingFacts );
	}
	
	public void testPreserve()
	{
		final IPredicate A = FactsTest.createPredicate( "PREDICATE", 1 );
		final IPredicate B = FactsTest.createPredicate( "PREDICATE", 2 );

		ITuple a1 = FactsTest.createTuple( 1 );
		ITuple b1 = FactsTest.createTuple( 1, 2 );
		originalFacts.get( A ).add( a1 );
		originalFacts.get( B ).add( b1 );

		preservingFacts = new OriginalFactsPreservingFacts( originalFacts, mRelationFactory );

		ITuple a2 = FactsTest.createTuple( 3 );
		ITuple b2 = FactsTest.createTuple( 4, 5 );
		preservingFacts.get( A ).add( a2 );
		preservingFacts.get( B ).add( b2 );
		
		assertEquals( 2, preservingFacts.get( A ).size() );
		assertEquals( 2, preservingFacts.get( B ).size() );
		assertEquals( a1, preservingFacts.get( A ).get( 0 ) );
		assertEquals( a2, preservingFacts.get( A ).get( 1 ) );
		assertEquals( b1, preservingFacts.get( B ).get( 0 ) );
		assertEquals( b2, preservingFacts.get( B ).get( 1 ) );

		assertEquals( 1, originalFacts.get( A ).size() );
		assertEquals( 1, originalFacts.get( B ).size() );
		assertEquals( a1, originalFacts.get( A ).get( 0 ) );
		assertEquals( b1, originalFacts.get( B ).get( 0 ) );
	}

	private static final IRelationFactory mRelationFactory = new SimpleRelationFactory();
}
