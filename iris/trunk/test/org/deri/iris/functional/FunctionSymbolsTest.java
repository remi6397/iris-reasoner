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

public class FunctionSymbolsTest extends TestCase
{
	public void testSimpleFunctionSymbols() throws Exception
	{
		String program = 
			"p ( f ( h( 'a' ) , 'b' ), g( 'c' , 'd' ) )."+
			"p(g(2))." +
			
			"?-p(?X,?Y).";
		
		String expectedResults =
			"p( f( h('a'), 'b'), g('c', 'd') ).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}	

	public void testTermMatching() throws Exception
	{
		String program =
			"p( 1, f(1), g(1,1), h(j(1,1)) )." +
			"p( 1, f(1), g(1,2), h(j(2,3)) )." +
			"p( 3, f(3), g(3,4), h(j(4,5)) )." +
			"p( 3, f(3), g(3,'a'), h(j('a',5)) )." +
			"p( 1, f(2), g(1,2), h(j(2,3)) )." +
			"p( 1, f(1), g(1,2), h(j(3,3)) )." +
			"p( 7, f(7), g(7,8), h(k(8,9)) )." +
			
			"?-p( ?X,f(?X), g(?X, ?Y), h(j(?Y,?Z)) ).";
		
		String expectedResults = 
			"dummy( 1, 1, 1 )." +
			"dummy( 1, 2, 3 )." +
			"dummy( 3, 'a', 5 )." +
			"dummy( 3, 4, 5 ).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}	

	public void testFunctionSymbolsInRuleBodyRuleHeadAndQuery() throws Exception
	{
		String program =
			"p(g(?X,?Y)) :- q(f(?X,?Y))." +
			
			"q(i(1,h(2)))." +
			"q(f(3,h(4)))." +
			"q(f(5,h(6)))." +
			
			"?-p( g( ?X, h(?Y) ) ).";
		
		String expectedResults = 
			"dummy(3,4)." +
			"dummy(5,6).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}	

	public void testSubstitution() throws Exception
	{
		String program =
			"p( 1, 2 )." +
			
			"q( 'a', ?x, f(?x), f(g(?x)), h(j(4,k(?x,r(?y),5))) ) :- p(?x,?y)." +
			
			"?- q(?x, ?y, ?z, ?v, ?w).";
		
		String expectedResults = 
			"dummy( 'a', 1, f(1), f(g(1)), h(j(4,k(1,r(2),5)) )).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}	

	public void testSuccession10() throws Exception
	{
		String program =
			"p(1,1)." +
			"p( s(?x),?n1 ) :- p( ?x, ?n ), ?n + 1 = ?n1, ?n1 <= 10." +
			"?- p( ?x,?n).";
		
		String expectedResults = 
			"dummy(1, 1)." +
			"dummy(s(1), 2)." +
			"dummy(s(s(1)), 3)." +
			"dummy(s(s(s(1))), 4)." +
			"dummy(s(s(s(s(1)))), 5)." +
			"dummy(s(s(s(s(s(1))))), 6)." +
			"dummy(s(s(s(s(s(s(1)))))), 7)." +
			"dummy(s(s(s(s(s(s(s(1))))))), 8)." +
			"dummy(s(s(s(s(s(s(s(s(1)))))))), 9)." +
			"dummy(s(s(s(s(s(s(s(s(s(1))))))))), 10).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	public void testRecession10() throws Exception
	{
		String program =
			"p(s(s(s(s(s(s(s(s(s(1))))))))), 10)." +
			"p( ?x,?n1 ) :- p( s(?x), ?n ), ?n - 1 = ?n1." +
			"?- p( ?x,?n).";
		
		String expectedResults = 
			"dummy(s(s(s(s(s(s(s(s(s(1))))))))), 10)." +
			"dummy(s(s(s(s(s(s(s(s(1)))))))), 9)." +
			"dummy(s(s(s(s(s(s(s(1))))))), 8)." +
			"dummy(s(s(s(s(s(s(1)))))), 7)." +
			"dummy(s(s(s(s(s(1))))), 6)." +
			"dummy(s(s(s(s(1)))), 5)." +
			"dummy(s(s(s(1))), 4)." +
			"dummy(s(s(1)), 3)." +
			"dummy(s(1), 2)." +
			"dummy(1, 1).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	/**
	 * Uncomment the 'OR' parts after support for unsafe rules is added.
	 * @throws Exception
	 */
	public void testLogicalExpressionsAsFunctionSymbol() throws Exception
	{
		String kb =
			"and_(?x,?y) :- true(?x), true(?y)." +
//			"or_(?x,?y) :- true(?x)." +
//			"or_(?x,?y) :- true(?y)." +

			"true('a')." +
			"true('b').";
		
		Helper.evaluateWithAllStrategies( kb + "?- and_('a', 'b').", "dummy()." );	// TRUE
		Helper.evaluateWithAllStrategies( kb + "?- and_('a', 'c').", "" );	// FALSE
		Helper.evaluateWithAllStrategies( kb + "?- and_('d', 'b').", "" );
		Helper.evaluateWithAllStrategies( kb + "?- and_('e', 'f').", "" );
		
//		Helper.evaluateWithAllStrategies( kb + "?- or_('a', 'b').", "dummy()." );
//		Helper.evaluateWithAllStrategies( kb + "?- or_('a', 'c').", "dummy()." );
//		Helper.evaluateWithAllStrategies( kb + "?- or_('d', 'b').", "dummy()." );
//		Helper.evaluateWithAllStrategies( kb + "?- or_('e', 'f').", "" );
	}	
}
