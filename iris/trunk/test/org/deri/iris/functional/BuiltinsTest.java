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
 * Tests for IRIS built-in predicates.
 */
public class BuiltinsTest extends TestCase
{
	/**
	 * Test that the evaluation strategy correctly handles relations with
	 * arguments of different data types occurring at the same position.
	 * 
	 * Need to check that = is always false when comparing arguments
	 * of non-compatible data types, i.e.
	 * any numeric can be tested against any other numeric, otherwise
	 * the datatypes must exactly match.
	 */
	public void testMixedDataTypes_IncompatibleDataTypesNeverEqual() throws Exception
	{
		String program =
			"p( 'a', 'b' )." +
			"p( 'c', 7 )." +
			"p( 1.23, _datetime(2000,1,1,2,2,2) )." +
			
			"q( ?X, ?Y ) :- p( ?X, ?Y ), ?Y = 7 ." +
			
			"?- q(?X, ?Y ).";
		
       	String expectedResults = 
			"q( 'c', 7 ).";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Test that the evaluation strategy correctly handles relations with
	 * arguments with different data types occurring at the same position.
	 * 
	 * Need to check that != is always true when comparing arguments
	 * of non-compatible data types, i.e.
	 * any numeric can be tested against any other numeric, otherwise
	 * the datatypes must exactly match.
	 */
	public void testMixedDataTypes_IncompatibleDataTypesAlwaysNotEqual() throws Exception
	{
		String program =
			"p( 'a', 'b' )." +
			"p( 'c', 7 )." +
			"p( 1.23, _datetime(2000,1,1,2,2,2) )." +
			
			"q( ?X, ?Y ) :- p( ?X, ?Y ), ?Y != 7 ." +
			
			"?- q( ?X, ?Y ).";
		
       	String expectedResults = 
			"q( 'a', 'b' )." +
			"q( 1.23, _datetime(2000,1,1,2,2,2) ).";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Test that the evaluation strategy correctly handles relations with
	 * arguments with different data types occurring at the same position.
	 * 
	 * Need to check the behaviour when using <, <=, >, >= when comparing arguments
	 * with different data types.
	 * 
	 * I would expect <, <=, >, >= to only work with arguments which are both numeric and the
	 * comparison is performed with the types promoted to the highest precision 'containing' type.
	 */
	public void testMixedDataTypes_ComparingDifferingDatatypes() throws Exception
	{
		String program =
			"p( 'a', 'b' )." +
			"p( 'b', 5 )." +
			"p( 'c', 1.23 )." +
			"p( 'd', _datetime(2000,1,1,2,2,2) )." +
			
			"q( ?X, ?Y ) :- p(?X, ?Y ), ?Y < 7, ?Y <= 6, ?Y > 3, ?Y >= 4 ." +
			
			"?- q( ?X, ?Y ).";
		
       	String expectedResults =
       		"q( 'b', 5 ).";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Assert that every built in predicate functions correctly with every
	 * possible data type.
	 */
	public void testBuiltinForAllDataTypes()
	{
		fail( "Not implemented yet." );
	}
	
	/**
	 * Check that a program containing the built-in equality predicate is
	 * correctly evaluated.
	 * @throws Exception
	 */
	public void testBuiltIn_Equality() throws Exception
	{
		String program = 
 			"s('d')." +
		    "s('b')." +
		    "s('a')." +
		    
		    "r('d')." +
		    
		    "p('b')." +
		    
		    "p(?X) :- r(?X)." +
		    "w(?X) :- s(?X), p(?X), ?X='d'." +
		    "?- w(?X).";

		String expectedResults = "w('d').";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check that a program containing the built-in inequality predicate is
	 * correctly evaluated.
	 * @throws Exception
	 */
	public void testBuiltIn_Inequality() throws Exception
	{
		String program = 
   			"s('d')." +
		    "s('b')." +
		    "s('a')." +
		    
		    "p('b')." +
		    "p('d')." +
		    
		    // Inequality built-in:
		    "w(?X) :- s(?X), p(?X), ?X != 'd'." +
		    "?- w(?X).";

		String expectedResults = "w('b').";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check that a program containing the built-in predicate is
	 * correctly evaluated.
	 * @throws Exception
	 */
	public void testBuiltIn_Less() throws Exception
	{
		String program = 
			"s(1)." +
			"s(2)." +
			"s(3)." +
			"s(4)." +
			
			"p(2)." +
			"p(3)." +
			
		    "w(?X) :- s(?X), p(?Y), ?X < ?Y." +
		    "?- w(?X).";

		String expectedResults =
		    "w(1)." +
		    "w(2).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check that a program containing the built-in predicate is
	 * correctly evaluated.
	 * @throws Exception
	 */
	public void testBuiltIn_LessEqual() throws Exception
	{
		String program = 
			"s(1)." +
			"s(2)." +
			"s(3)." +
			"s(4)." +
			
			"p(2)." +
			"p(3)." +
			
		    "w(?X) :- s(?X), p(?Y), ?X <= ?Y." +
		    "?- w(?X).";

		String expectedResults =
		    "w(1)." +
		    "w(2)." +
		    "w(3).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check that a program containing the built-in predicate is
	 * correctly evaluated.
	 * @throws Exception
	 */
	public void testBuiltIn_Greater() throws Exception
	{
		String program = 
			"s(1)." +
			"s(2)." +
			"s(3)." +
			"s(4)." +
			
			"p(2)." +
			"p(3)." +
			
		    "w(?X) :- s(?X), p(?Y), ?X > ?Y." +
		    "?- w(?X).";

		String expectedResults =
		    "w(3)." +
		    "w(4).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check that a program containing the built-in predicate is
	 * correctly evaluated.
	 * @throws Exception
	 */
	public void testBuiltIn_GreaterEqual() throws Exception
	{
		String program = 
			"s(1)." +
			"s(2)." +
			"s(3)." +
			"s(4)." +
			
			"p(2)." +
			"p(3)." +
			
		    "w(?X) :- s(?X), p(?Y), ?X >= ?Y." +
		    "?- w(?X).";

		String expectedResults =
		    "w(2)." +
		    "w(3)." +
		    "w(4).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check that a program containing the built-in predicate is
	 * correctly evaluated.
	 * @throws Exception
	 */
	public void testBuiltIn_Add() throws Exception
	{
		String program = 
			"s(1)." +
			
			"p(2)." +
			"p(3)." +
			
		    "w(?X,?Z) :- s(?X), p(?Y), ?X + ?Y = ?Z." +
		    "?- w(?X,?Z).";

		String expectedResults =
		    "w(1, 3)." +
		    "w(1, 4).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check that a program containing the built-in predicate is
	 * correctly evaluated.
	 * @throws Exception
	 */
	public void testBuiltIn_Subtract() throws Exception
	{
		String program = 
			"s(1)." +
			
			"p(2)." +
			"p(3)." +
			
		    "w(?X,?Z) :- s(?X), p(?Y), ?X - ?Y = ?Z." +
		    "?- w(?X,?Z).";

		String expectedResults =
		    "w(1, -1)." +
		    "w(1, -2).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check that a program containing the built-in predicate is
	 * correctly evaluated.
	 * @throws Exception
	 */
	public void testBuiltIn_Multiply() throws Exception
	{
		String program = 
			"s(7)." +
			
			"p(2)." +
			"p(3)." +
			
		    "w(?X,?Z) :- s(?X), p(?Y), ?X * ?Y = ?Z." +
		    "?- w(?X,?Z).";

		String expectedResults =
		    "w(7, 14)." +
		    "w(7, 21).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check that a program containing the built-in predicate is
	 * correctly evaluated.
	 * @throws Exception
	 */
	public void testBuiltIn_Divide() throws Exception
	{
		String program = 
			"s(12)." +
			
			"p(2)." +
			"p(3)." +
			
		    "w(?X,?Z) :- s(?X), p(?Y), ?X / ?Y = ?Z." +
		    "?- w(?X,?Z).";

		String expectedResults =
		    "w(12, 6)." +
		    "w(12, 4).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
}
