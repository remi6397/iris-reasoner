/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2007 Digital Enterprise Research Institute (DERI), 
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
package org.deri.iris.functional;

import junit.framework.TestCase;

/**
 * Tests for arithmetic operators.
 */
public class ArithmeticTest extends TestCase
{
	public void testAddTwoVariables() throws Exception
	{
		String program =
			"p( 0 )." +
			"p( 1 )." +
			"p( 2 )." +
			
			"q( 2 )." +
			"q( 3 )." +
			"q( 4 )." +
			
			// Rule with operator predicate
			"r( ?X, ?Y ) :- p( ?X ), q( ?Y ), ?X + ?Y = 3." +

			// Same rule with named predicate
			"s( ?X, ?Y ) :- p( ?X ), q( ?Y ), ADD( ?X, ?Y, 3 )." +
			
			// Same rule with all variables
			"t( ?X, ?Y ) :- p( ?X ), q( ?Y ), ?X + ?Y = ?Z, ?Z = 3." +

			// Combine all the rules
			"a( ?X, ?Y ) :- r( ?X, ?Y ), s( ?X, ?Y ), t( ?X, ?Y )." +
			
			"?- a(?X, ?Y ).";
		
       	String expectedResults = 
			"a( 1, 2 )." +
			"a( 0, 3 ).";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	public void testAddAllVariables() throws Exception
	{
		String program =
			"p( 0 )." +
			"p( 1 )." +
			
			"q( 2 )." +
			"q( 3 )." +
			
			// Rule with operator predicate
			"r( ?Z ) :- p( ?X ), q( ?Y ), ?X + ?Y = ?Z." +

			"?- r( ?Z ).";
		
       	String expectedResults = 
			"r( 2 )." +
			"r( 3 )." +
			"r( 4 ).";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	public void testAddAllConstantsTrue() throws Exception
	{
		String program =
			"p( 0 )." +
			"p( 1 )." +
			
			// Rule with operator predicate
			"r( ?X ) :- p( ?X ), 1 + 2 = 3." +

			"?- r( ?Z ).";
		
       	String expectedResults = 
			"r( 0 )." +
			"r( 1 ).";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	public void testAddAllConstantsFalse() throws Exception
	{
		String program =
			"p( 0 )." +
			"p( 1 )." +
			
			// Rule with operator predicate
			"r( ?X ) :- p( ?X ), 1 + 2 = 2." +

			"?- r( ?Z ).";
		
       	String expectedResults = "";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	public void testAddConstantOperandSecond() throws Exception
	{
		String program =
			"p( 0 )." +
			"p( 1 )." +
			"p( 2 )." +
			
			"q( 2 )." +
			"q( 3 )." +
			"q( 4 )." +
			
			// Rule with operator predicate
			"r( ?X, ?Y ) :- p( ?X ), q( ?Y ), ?X + 3 = ?Y." +

			// Same rule with named predicate
			"s( ?X, ?Y ) :- p( ?X ), q( ?Y ), ADD( ?X, 3, ?Y )." +
			
			// Same rule with all variables
			"t( ?X, ?Y ) :- p( ?X ), q( ?Y ), ?X + ?Z = ?Y, ?Z = 3." +

			// Combine both the rules
			"a( ?X, ?Y ) :- r( ?X, ?Y ), s( ?X, ?Y ), t( ?X, ?Y )." +
			
			"?- a(?X, ?Y ).";
		
       	String expectedResults = 
			"a( 0, 3 )." +
			"a( 1, 4 ).";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	public void testAddConstantOperandFirst() throws Exception
	{
		String program =
			"p( 0 )." +
			"p( 1 )." +
			"p( 2 )." +
			
			"q( 2 )." +
			"q( 3 )." +
			"q( 4 )." +
			
			// Rule with operator predicate
			"r( ?X, ?Y ) :- p( ?X ), q( ?Y ), 3 + ?X = ?Y." +

			// Same rule with named predicate
			"s( ?X, ?Y ) :- p( ?X ), q( ?Y ), ADD( 3, ?X, ?Y )." +
			
			// Same rule with all variables
			"t( ?X, ?Y ) :- p( ?X ), q( ?Y ), ADD( ?Z, ?X, ?Y ), ?Z = 3." +
			
			// Combine all the rules
			"a( ?X, ?Y ) :- r( ?X, ?Y ), s( ?X, ?Y ), t( ?X, ?Y )." +
			
			"?- a(?X, ?Y ).";
		
       	String expectedResults = 
			"a( 0, 3 )." +
			"a( 1, 4 ).";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	public void testAddRearrangeVariables() throws Exception
	{
		String program =
			"one( 1 )." +
			"nine( 9 )." +
			"ten( 10 )." +
			
			"r( ?Z ) :- one( ?X ), ten( ?Y ), ?X + ?Y = ?Z." +
			"s( ?Z ) :- one( ?X ), nine( ?Z ), ten( ?Y ), ?Z + ?X = ?Y." +
			"t( ?Z ) :- nine( ?X ), one( ?Z ), ten( ?Y ), ?X + ?Z = ?Y." +
			
			"?- r(?X)." +
			"?- s(?X)." +
			"?- t(?X).";
		
       	String expectedResults = 
			"r( 11 )." +
			"s( 9 )." +
			"t( 1 ).";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	public void testSubtractTwoVariables() throws Exception
	{
		String program =
			"p( 3 )." +
			"p( 4 )." +
			"p( 5 )." +
			
			"q( 1 )." +
			"q( 2 )." +
			"q( 3 )." +
			
			// Rule with operator predicate
			"r( ?X, ?Y ) :- p( ?X ), q( ?Y ), ?X - ?Y = 3." +
			
			// Same rule with named predicate
			"s( ?X, ?Y ) :- p( ?X ), q( ?Y ), SUBTRACT( ?X, ?Y, 3 )." +
			
			// Same rule with all variables
			"t( ?X, ?Y ) :- p( ?X ), q( ?Y ), SUBTRACT( ?X, ?Y, ?Z ), ?Z = 3." +
			
			// Combine all the rules
			"a( ?X, ?Y ) :- r( ?X, ?Y ), s( ?X, ?Y ), t( ?X, ?Y )." +
			
			"?- a(?X, ?Y ).";
		
       	String expectedResults = 
			"a( 4, 1 )." +
			"a( 5, 2 ).";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	public void testSubtractAllVariables() throws Exception
	{
		String program =
			"p( 0 )." +
			"p( 1 )." +
			
			"q( 2 )." +
			"q( 3 )." +
			
			// Rule with operator predicate
			"r( ?Z ) :- p( ?X ), q( ?Y ), ?X - ?Y = ?Z." +

			"?- r( ?Z ).";
		
       	String expectedResults = 
			"r( -3 )." +
			"r( -2 )." +
			"r( -1 ).";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	public void testSubtractConstantOperandSecond() throws Exception
	{
		String program =
			"p( 3 )." +
			"p( 4 )." +
			"p( 5 )." +
			
			"q( 1 )." +
			"q( 2 )." +
			"q( 3 )." +
			
			// Rule with operator predicate
			"r( ?X, ?Y ) :- p( ?X ), q( ?Y ), ?X - 3 = ?Y." +
			
			// Same rule with named predicate
			"s( ?X, ?Y ) :- p( ?X ), q( ?Y ), SUBTRACT( ?X, 3, ?Y )." +
			
			// Same rule with all variables
			"t( ?X, ?Y ) :- p( ?X ), q( ?Y ), SUBTRACT( ?X, ?Z, ?Y ), ?Z = 3." +
			
			// Combine both the rules
			"a( ?X, ?Y ) :- r( ?X, ?Y ), s( ?X, ?Y ), t( ?X, ?Y )." +
			
			"?- a(?X, ?Y ).";
		
       	String expectedResults = 
			"a( 4, 1 )." +
			"a( 5, 2 ).";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	public void testSubtractConstantOperandFirst() throws Exception
	{
		String program =
			"p( -1 )." +
			"p( 0 )." +
			"p( 1 )." +
			
			"q( 1 )." +
			"q( 2 )." +
			"q( 3 )." +
			
			// Rule with operator predicate
			"r( ?X, ?Y ) :- p( ?X ), q( ?Y ), 3 - ?X = ?Y." +
			
			// Same rule with named predicate
			"s( ?X, ?Y ) :- p( ?X ), q( ?Y ), SUBTRACT( 3, ?X, ?Y )." +
			
			// Same rule with named predicate
			"t( ?X, ?Y ) :- p( ?X ), q( ?Y ), SUBTRACT( ?Z, ?X, ?Y ), ?Z = 3." +
			
			// Combine both the rules
			"a( ?X, ?Y ) :- r( ?X, ?Y ), s( ?X, ?Y ), t( ?X, ?Y )." +
			
			"?- a(?X, ?Y ).";
		
       	String expectedResults = 
			"a( 0, 3 )." +
			"a( 1, 2 ).";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}
}
