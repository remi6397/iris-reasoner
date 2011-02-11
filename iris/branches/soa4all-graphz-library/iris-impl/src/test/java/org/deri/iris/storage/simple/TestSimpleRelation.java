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
package org.deri.iris.storage.simple;

import junit.framework.TestCase;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.rules.compiler.Helper;
import org.deri.iris.storage.IRelation;

public class TestSimpleRelation extends TestCase
{
	IRelation mRelation;
	
	protected void setUp() throws Exception
	{
		mRelation = new SimpleRelation();
	}
	
	public void testAll()
	{
		// Ensure the relation is empty
		assertEquals( mRelation.size(), 0 );
		
		// Insert a new tuple
		ITuple t1 = Helper.createTuple( 2, 1 );
		mRelation.add( t1 );
		assertEquals( mRelation.size(), 1 );
		assertEquals( mRelation.get( 0 ), t1 );

		// Try adding same tuple again and it should not accept it
		mRelation.add( t1 );
		assertEquals( mRelation.size(), 1 );
		assertEquals( mRelation.get( 0 ), t1 );

		// Add a new tuple
		ITuple t2 = Helper.createTuple( 2, 2 );
		mRelation.add( t2 );
		assertEquals( mRelation.size(), 2 );
		assertEquals( mRelation.get( 0 ), t1 );
		assertEquals( mRelation.get( 1 ), t2 );

		// Create a new relation and check that addAll() works.
		IRelation r2 = new SimpleRelation();
		r2.addAll( mRelation );
		assertEquals( r2.size(), 2 );
		assertEquals( r2.get( 0 ), t1 );
		assertEquals( r2.get( 1 ), t2 );
		
		// Now check that it is not possible to add t1 and t2 to the new relation.
		r2.add( t1 );
		r2.add( t2 );
		assertEquals( r2.size(), 2 );
	}
}
