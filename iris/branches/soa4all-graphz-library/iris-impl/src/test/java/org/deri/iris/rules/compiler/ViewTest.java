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
package org.deri.iris.rules.compiler;

import java.util.List;

import junit.framework.TestCase;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.api.terms.concrete.IIntegerTerm;
import org.deri.iris.storage.IRelation;
import org.deri.iris.storage.simple.SimpleRelationFactory;

public class ViewTest extends TestCase
{
	IRelation mRelation;
	View mView;
	
	protected void setUp() throws Exception
	{
		mRelation = new SimpleRelationFactory().createRelation();
		
		// f(1,2,g(2,3))
		ITuple t1 = Helper.createTuple( 1, 2, Helper.createConstructedTerm( "g", 2, 3 ) );
		mRelation.add( t1 );

		// f(1,2,g(2,1))
		ITuple t2 = Helper.createTuple( 1, 2, Helper.createConstructedTerm( "g", 2, 1 ) );
		mRelation.add( t2 );
		
		// f(X,Y,g(Y,X))
		ITuple viewCriteria = Helper.createTuple( "X", "Y", Helper.createConstructedTerm( "g", "Y", "X" ) );
		
		mView = new View( mRelation, viewCriteria, new SimpleRelationFactory() );
	}

	public void testVariables()
	{
		List<IVariable> variables = mView.variables();
		
		assertEquals( variables.size(), 2 );
		
		assertEquals( variables.get( 0 ).getValue(), "X" );
		assertEquals( variables.get( 1 ).getValue(), "Y" );
	}
	
	public void testView()
	{
		IRelation v = mView;
		
		assertEquals( 1, v.size());
		
		ITuple tuple = v.get( 0 );
		
		assertEquals( ( (IIntegerTerm) tuple.get( 0 ) ).getValue().intValue(), 1 );
		assertEquals( ( (IIntegerTerm) tuple.get( 1 ) ).getValue().intValue(), 2 );
		
//		Iterator<ITuple> it = mView.iterator();
//		
//		assertTrue( it.hasNext() );
//		ITuple tuple = it.next();
//	
//		assertEquals( tuple.size(), 2 );
//		assertEquals( ( (IIntegerTerm) tuple.get( 0 ) ).getValue().intValue(), 1 );
//		assertEquals( ( (IIntegerTerm) tuple.get( 1 ) ).getValue().intValue(), 2 );
//		
//		assertFalse( it.hasNext() );
	}
}
