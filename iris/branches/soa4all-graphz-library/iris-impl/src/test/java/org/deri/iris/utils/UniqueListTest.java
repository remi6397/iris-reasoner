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
package org.deri.iris.utils;

import java.util.Iterator;

import junit.framework.TestCase;

public class UniqueListTest extends TestCase
{
	UniqueList<Integer> mList;

	@Override
    protected void setUp() throws Exception
    {
		mList = new UniqueList<Integer>();
    }

	public void testAdd()
	{
		mList.add( 0 );
		mList.add( 1 );
		mList.add( 2 );
		
		assertEquals( mList.size(), 3 );
		assertEquals( mList.get( 0 ), new Integer( 0 ) );
		assertEquals( mList.get( 1 ), new Integer( 1 ) );
		assertEquals( mList.get( 2 ), new Integer( 2 ) );
		
		mList.add( 1 );
		assertEquals( mList.size(), 3 );

		mList.add( 1, 3 );
		assertEquals( mList.size(), 4 );
		assertEquals( mList.get( 0 ), new Integer( 0 ) );
		assertEquals( mList.get( 1 ), new Integer( 3 ) );
		assertEquals( mList.get( 2 ), new Integer( 1 ) );
		assertEquals( mList.get( 3 ), new Integer( 2 ) );
	}
	
	public void testIterator()
	{
		mList.add( 0 );
		mList.add( 1 );
		
		Iterator<Integer> it = mList.iterator();

		assertTrue( it.hasNext() );
		assertEquals( it.next().intValue(), 0 );

		assertTrue( it.hasNext() );
		assertEquals( it.next().intValue(), 1 );

		assertFalse( it.hasNext() );
	}
}
