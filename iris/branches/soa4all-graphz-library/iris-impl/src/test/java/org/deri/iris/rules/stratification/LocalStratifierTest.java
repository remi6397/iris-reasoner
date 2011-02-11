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
package org.deri.iris.rules.stratification;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.EqualBuiltin;
import org.deri.iris.builtins.LessBuiltin;
import org.deri.iris.builtins.NotEqualBuiltin;

public class LocalStratifierTest extends TestCase
{
	ILiteral createLiteral( boolean positive, String predicateName, Object ... termObjects )
	{
		List<ITerm> terms = new ArrayList<ITerm>();

		for( Object o : termObjects )
		{
			if( o instanceof Integer )
				terms.add( CONCRETE.createInteger( (Integer) o ) );
			else if( o instanceof String )
				terms.add( TERM.createVariable( (String) o ) );
		}
		
		if( predicateName.equals( "=" ) )
			return BASIC.createLiteral( positive,
							new EqualBuiltin( terms.toArray( new ITerm[ 2 ] ) ) );
		
		if( predicateName.equals( "!=" ) )
			return BASIC.createLiteral( positive,
							new NotEqualBuiltin( terms.toArray( new ITerm[ 2 ] ) ) );
		
		if( predicateName.equals( "<" ) )
			return BASIC.createLiteral( positive,
							new LessBuiltin( terms.toArray( new ITerm[ 2 ] ) ) );
		
		return BASIC.createLiteral( positive, 
									BASIC.createAtom(
										BASIC.createPredicate( predicateName, terms.size() ), 
										BASIC.createTuple( terms )
								)
							);
	}
	
	IRule createRule( ILiteral head, ILiteral ... body )
	{
		List<ILiteral> h = new ArrayList<ILiteral>();
		h.add( head );
		
		List<ILiteral> b = new ArrayList<ILiteral>();
		for( ILiteral l : body )
			b.add( l );

		return BASIC.createRule( h, b );
	}

	public void testStratify1()
	{
		List<IRule> rules = new ArrayList<IRule>();
		
		IRule p = createRule( 	createLiteral( true, "p", 1, "Y" ),
								createLiteral( true, "q", "X", "Y" ),
								createLiteral( false, "p", 2, "Y" )
								);
		
		rules.add( p );
		
		LocalStratifier ls = new LocalStratifier( true );
		
		List<List<IRule>> sRules = ls.stratify( rules );
		
		// This rule is already locally stratified
		assertNotNull( sRules );
		assertEquals( sRules.size(), 1 );

		assertEquals( sRules.get( 0 ).iterator().next(), p );
	}

	public void testStratify2()
	{
		List<IRule> rules = new ArrayList<IRule>();
		
		IRule p = createRule( 	createLiteral( true, "p", 1, "Y" ),
								createLiteral( true, "r", "Y" ),
								createLiteral( false, "q", 2, "Y" )
								);
		
		IRule q = createRule( 	createLiteral( true, "q", "X", "Y" ),
								createLiteral( true, "p", "X", "Y" )
								);
		rules.add( p );
		rules.add( q );
		
		LocalStratifier ls = new LocalStratifier( true );
		
		List<List<IRule>> sRules = ls.stratify( rules );
		
		// One of these rules needs splitting
		assertNotNull( sRules );
		assertEquals( sRules.size(), 2 );

		assertEquals( sRules.get( 0 ).size(), 1 );
		assertEquals( sRules.get( 1 ).size(), 2 );
	}

	public void testNoNeedToSplit_NotEqualImpliedByLess()
	{
		List<IRule> rules = new ArrayList<IRule>();
		
		IRule p = createRule( 	createLiteral( true, "p", "X", "Y" ),
								createLiteral( true, "r", "X", "Y" ),
								createLiteral( false, "p", 2, "Y" ),
								createLiteral( true, "<", "X", 2 )
								);
		
		rules.add( p );
		
		LocalStratifier ls = new LocalStratifier( true );
		
		List<List<IRule>> sRules = ls.stratify( rules );
		
		// This rule does not need splitting
		assertNotNull( sRules );
		assertEquals( sRules.size(), 1 );

		assertEquals( sRules.get( 0 ).size(), 1 );
	}

	public void testNotStratified()
	{
		List<IRule> rules = new ArrayList<IRule>();
		
		IRule p = createRule( 	createLiteral( true, "p", 2, "Y" ),
								createLiteral( true, "r", "Y" ),
								createLiteral( false, "q", 2, "Y" )
								);
		
		IRule q = createRule( 	createLiteral( true, "q", "X", "Y" ),
								createLiteral( true, "p", "X", "Y" )
								);
		rules.add( p );
		rules.add( q );
		
		LocalStratifier ls = new LocalStratifier( true );
		
		List<List<IRule>> sRules = ls.stratify( rules );
		
		// Can not be stratified, so result is null
		assertNull( sRules );
	}
}
