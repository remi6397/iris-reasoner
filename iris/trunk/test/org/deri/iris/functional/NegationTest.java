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

import org.deri.iris.ProgramNotStratifiedException;
import junit.framework.TestCase;

public class NegationTest extends TestCase
{
	/**
	 * Assert that evaluations of logic programs containing globally stratified
	 * negated subgoals give the correct results.
	 * @throws Exception 
	 */
	public void testGloballyStratified1() throws Exception
	{
		String program = 
 			"s('d')." +
		    "s('b')." +
		    "s('a')." +
		    "s('q')." +
		    
		    "r('d')." +
		    "r('c')." +
		    
		    "p('b')." +
		    "p('e')." +
		    
		    "t('a')." +
		    
		    "q(?X) :- s(?X), not p(?X)." +
		    "p(?X) :- r(?X)." +
		    "r(?X) :- t(?X)." +
		    "?- q(?X).";
		
		String expectedResults = "q('q').";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * A logic program with stratified negation.
	 * @throws Exception
	 */
	public void testGloballyStratified2() throws Exception
	{
    	String program = 
 			"u('d')." +
		    "u('b')." +
		    "u('a')." +
		    "u('q')." +
		    
		    "s('d')." +
		    "s('c')." +
		    
		    "p('b')." +
		    "p('e')." +
		    
		    "q('a')." +
		    
		    "p(?X) :- q(?X), not r(?X)." +
		    "r(?X) :- s(?X), not t(?X)." +
		    "t(?X) :- u(?X)." +
		    "?- p(?X).";
        	
       	String expectedResults = 
		    "p('a')." +
		    "p('b')." +
		    "p('e').";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	/**
	 * This test makes sure that sub-goals are evaluated in the correct order,
	 * i.e. positive literals first, even when the negative literal appears first. 
	 * @throws Exception
	 */
	public void testNegatedSubGoalFirst() throws Exception
	{
    	String program = 
		    "r('a', 'a')." +
		    "r('b', 'a')." +
		    
		    "s('a', 'a')." +
		    "s('b', 'b')." +
		    
		    "p(?X) :- not s(?X, 'a'), r(?X, ?Y)." +
		    "?- p(?X).";
        	
       	String expectedResults = 
		    "p('b').";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	/**
	 * Assert that evaluations of logic programs containing non-stratified negation
	 * are detected correctly.
	 */
	public void testNotStratified()
	{
		String program =
			"q( 5 )." +
			"p( ?X ) :- r( ?X ),not q(?X)." +
			"q( ?X ) :- r( ?X ),not p(?X)." +
			"?- q( ?X ).";
		
		Helper.checkFailureWithAllStrategies( program, ProgramNotStratifiedException.class );
	}

	/**
	 * Simple negation of a builtin binary predicate.
	 * @throws Exception
	 */
    public void testNegatedLess() throws Exception
    {
    	String program = 
    		"r(0)." +
    		"r(1)." +
    		"r(2)." +
    		"r(3)." +
    		"r(4)." +
    		
    		"v(?X) :- r(?X), not  ?X > 2." +
    		"w(?X) :- r(?X),      ?X > 2." +
    	    "?- v(?X)." +
    	    "?- w(?X).";
        	
       	String expectedResults = 
    	    "v(0)." +
    	    "v(1)." +
    	    "v(2)." +
    	    "w(3)." +
    	    "w(4).";
    
       	Helper.evaluateWithAllStrategies( program, expectedResults );
    }

	/**
	 * Simple negation of a builtin unary predicate.
	 * @throws Exception
	 */
    public void testNegatedIsString() throws Exception
    {
    	String program = 
    		"r(1)." +
    		"r(2)." +
    		"r('a')." +
    		"r('b')." +
    		
    		"v(?X) :- r(?X), not  IS_STRING( ?X )." +
    		"w(?X) :- r(?X),      IS_STRING( ?X )." +
    	    "?- v(?X)." +
    	    "?- w(?X).";
        	
       	String expectedResults = 
    	    "v(1)." +
    	    "v(2)." +
    	    "w('a')." +
    	    "w('b').";
    
       	Helper.evaluateWithAllStrategies( program, expectedResults );
    }

	/**
	 * Simple negation of a builtin unary predicate.
	 * @throws Exception
	 */
    public void testNegatedAdd() throws Exception
    {
    	String program = 
    		"r(1)." +
    		"r(2)." +
    		"r(3)." +
    		"r(4)." +
    		
    		"s(5)." +
    		"s(6)." +
    		"s(7)." +
    		"s(8)." +
    		
    		"v(?X,?Y) :- r(?X), s(?Y),     ?X + ?Y = 7." +
    		"w(?X,?Y) :- r(?X), s(?Y), not ?X + ?Y = 7." +
    	    "?- v(?X,?Y)." +
    	    "?- w(?X,?Y).";
        	
       	String expectedResults = 
    	    "v(1,6)." +
    	    "v(2,5)." +
    	    
    	    "w(1,5)." +
    	    "w(1,7)." +
    	    "w(1,8)." +
    	    "w(2,6)." +
    	    "w(2,7)." +
    	    "w(2,8)." +
    	    "w(3,5)." +
    	    "w(3,6)." +
    	    "w(3,7)." +
    	    "w(3,8)." +
    	    "w(4,5)." +
    	    "w(4,6)." +
    	    "w(4,7)." +
    	    "w(4,8).";
    
       	Helper.evaluateWithAllStrategies( program, expectedResults );
    }
}
