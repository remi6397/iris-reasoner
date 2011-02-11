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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.rules.compiler.Helper;
import org.deri.iris.rules.compiler.Utils;
import org.deri.iris.rules.compiler.View;
import org.deri.iris.storage.IRelation;

public class TestSimpleIndex extends TestCase
{
	IRelation mRelation;
	View mView;
	SimpleIndex mIndex;

	protected void setUp() throws Exception
	{
		mRelation = new SimpleRelation();
		
		mRelation.add( Helper.createTuple( 1, 1, 1 ) );
		mRelation.add( Helper.createTuple( 1, 1, 2 ) );
		mRelation.add( Helper.createTuple( 1, 1, 3 ) );

		mRelation.add( Helper.createTuple( 1, 2, 1 ) );
		mRelation.add( Helper.createTuple( 2, 2, 2 ) );
		
		ITuple viewCriteria = Helper.createTuple( "X", "Y", "Z" );
		
		mView = new View( mRelation, viewCriteria, new SimpleRelationFactory() );
		
		mIndex = new SimpleIndex( mView, 0, 1 );
	}
	
	private static List<ITerm> makeKey( Object ... objects )
	{
		List<ITerm> key = new ArrayList<ITerm>( objects.length );
		
		for( Object o : objects )
		{
			ITerm term = Helper.createTerm( o );
			key.add( term );
		}
		
		return key;
	}

	public void testGet()
	{
		ITuple foreignTuple = Helper.createTuple( 3, 2, 1, 1 );
		
		List<ITuple> matchingTuples = mIndex.get( Utils.makeKey( foreignTuple, new int[] { 2, 3 } ) );
		
		assertNotNull( matchingTuples );
		assertEquals( 3, matchingTuples.size() );

		
		matchingTuples = mIndex.get( makeKey( 1, 2 ) );
		
		assertNotNull( matchingTuples );
		assertEquals( 1, matchingTuples.size() );

	
		matchingTuples = mIndex.get( makeKey( 2, 1 ) );
		
		assertEquals( 0, matchingTuples.size() );
	}
}
