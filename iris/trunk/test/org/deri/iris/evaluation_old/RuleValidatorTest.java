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
package org.deri.iris.evaluation_old;

import java.util.ArrayList;
import java.util.List;
import org.deri.iris.RuleUnsafeException;
import org.deri.iris.evaluation_old.RuleValidator;
import junit.framework.TestCase;

public class RuleValidatorTest extends TestCase
{
	@Override
    protected void setUp() throws Exception
    {
    }
	
	private List<String> makeList( String ... tokens )
	{
		List<String> result = new ArrayList<String>();
		
		for( String token : tokens )
			result.add( token );
		
		return result;
	}
	
	public void testPositiveRule()
	{
		RuleValidator rv = new RuleValidator( true, true );
		
		rv.addHeadVariables( makeList( "a" ) );
		assertUnsafe( rv );

		rv.addVariablesFromOrdinaryPredicate( true, makeList( "a" ) );
		assertSafe( rv );

		rv.addVariablesFromOrdinaryPredicate( true, makeList( "b" ) );
		assertSafe( rv );
	}
	
	public void testNegationRelaxed()
	{
		RuleValidator rv = new RuleValidator( true, true );
		
		rv.addHeadVariables( makeList( "a" ) );
		rv.addVariablesFromOrdinaryPredicate( true, makeList( "a" ) );
		rv.addVariablesFromOrdinaryPredicate( false, makeList( "b" ) );
		assertSafe( rv );
	}
	
	public void testNegationNotRelaxed()
	{
		RuleValidator rv = new RuleValidator( false, true );
		
		rv.addHeadVariables( makeList( "a" ) );
		rv.addVariablesFromOrdinaryPredicate( true, makeList( "a" ) );
		rv.addVariablesFromOrdinaryPredicate( false, makeList( "b" ) );
		assertUnsafe( rv );
	}
	
	public void testTernaryRelaxed()
	{
		RuleValidator rv = new RuleValidator( true, true );
		
		rv.addHeadVariables( makeList( "a", "b", "c" ) );

		rv.addVariablesFromOrdinaryPredicate( true, makeList( "a", "b" ) );
		
		rv.addVariablesFromPositiveArithmeticPredicate( false, makeList( "a", "b", "c" ) );

		assertSafe( rv );
	}
	
	public void testTernaryNotRelaxed()
	{
		RuleValidator rv = new RuleValidator( true, false );
		
		rv.addHeadVariables( makeList( "a", "b", "c" ) );

		rv.addVariablesFromOrdinaryPredicate( true, makeList( "a", "b" ) );
		
		rv.addVariablesFromPositiveArithmeticPredicate( false, makeList( "a", "b", "c" ) );

		assertUnsafe( rv );
	}
	
	public void testPositiveEquality()
	{
		RuleValidator rv = new RuleValidator( true, true );
		
		rv.addHeadVariables( makeList( "a", "b" ) );

		rv.addVariablesFromOrdinaryPredicate( true, makeList( "a" ) );
		
		rv.addVariablesFromPositiveArithmeticPredicate( true, makeList( "a", "b" ) );

		assertSafe( rv );
	}
	
	public void testEqualityAndArithmeticTogether()
	{
		RuleValidator rv = new RuleValidator( true, true );
		
		rv.addHeadVariables( makeList( "z" ) );

		rv.addVariablesFromPositiveArithmeticPredicate( false, makeList( "x", "y", "z" ) );
		rv.addVariablesFromPositiveArithmeticPredicate( false, makeList( "p", "x", "q" ) );
		rv.addVariablesFromPositiveArithmeticPredicate( false, makeList( "p", "s", "t" ) );

		rv.addVariablesFromPositiveArithmeticPredicate( true, makeList( "q", "r" ) );
		rv.addVariablesFromPositiveArithmeticPredicate( true, makeList( "y", "s" ) );
		rv.addVariablesFromPositiveArithmeticPredicate( true, makeList( "t" ) );
		rv.addVariablesFromPositiveArithmeticPredicate( true, makeList( "r" ) );
		rv.addVariablesFromPositiveArithmeticPredicate( true, makeList( "p" ) );

		assertSafe( rv );
	}
	
	void assertUnsafe( RuleValidator rv )
	{
		try
		{
			rv.isSafe();
			fail( "RuleValidator incorrectly deduced that the rule is safe." );
		}
		catch( RuleUnsafeException e )
		{
		}
	}

	void assertSafe( RuleValidator rv )
	{
		try
		{
			rv.isSafe();
		}
		catch( RuleUnsafeException e )
		{
			fail( "RuleValidator incorrectly deduced that the rule is not safe." );
		}
	}
}
