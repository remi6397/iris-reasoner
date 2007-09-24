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

public class Evaluation extends TestCase
{
	/**
	 * Test that a simple logic program of predicate logic is correctly
	 * evaluated.
	 * @throws Exception 
	 */
	public void testPredicateLogic() throws Exception
	{
		String program =
			"p( ?X, ?Y ) :- q( ?X, ?Y ), r( ?Y, ?Z ), s( ?X, ?Z )." +
			"q( 1, 2 )." +
			"r( 2, 3 )." +
			"s( 1, 3 )." +
			"?- p(?X, ?Y).";
		
		String expectedResults = "p( 1, 2 ).";
		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Test that a query with more than one predicate is correctly
	 * evaluated.
	 * TODO Semi-naive with Magic Sets evaluation is expected to fail, because
	 * this strategy can not currently have multiple predicates in a query.
	 * @throws Exception 
	 */
	public void testConjunctiveQuery() throws Exception
	{
		String program =
			"p( _datetime(2000,1,1,2,2,2) )." +
			"p( _datetime(2000,12,1,2,2,2) )." +
			"?- p( ?X ), p( ?Y ), ?X < ?Y.";
		
		String expectedResults =
			"p( _datetime(2000,1,1,2,2,2), _datetime(2000,12,1,2,2,2) ).";
		
		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
		
	/**
	 * Test that logic programs containing only propositional terms
	 * are correctly evaluated.
	 */
	public void testPropositionalLogicTrueOutcome() throws Exception
	{
		String program =
			"p :- a, b, c." +
			"a." +
			"b." +
			"c." +
			"?- p.";
		
		String expectedResults = "p.";
		Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	/**
	 * Test that logic programs containing only propositional terms
	 * are correctly evaluated.
	 */
	public void testPropositionalLogicFalseOutcome() throws Exception
	{
		String program =
			"p :- a, b, c." +
			"a." +
			"c." +
			"?- p.";
		
		String expectedResults = "";
		Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	/**
	 * Test for recursive rules.
	 */
	public void testRecursiveRules() throws Exception
	{
		String program =
			"parent( '1a', '2a' )." +
			"parent( '2a', '3a' )." +

			"parent( '1b', '2b' )." +
			"parent( '1b', '2c' )." +

			"parent( '2b', '3b' )." +
			"parent( '2b', '3c' )." +
			"parent( '2c', '3d' )." +
			"parent( '2c', '3e' )." +
			
			"parent( '3b', '4b' )." +
			"parent( '3e', '4e' )." +

			"sibling(?X,?Y) :- parent(?Z,?X), parent(?Z,?Y), ?X != ?Y." +
			"cousin(?X,?Y) :- parent(?XP,?X), parent(?YP,?Y), sibling(?XP,?YP).\n" +
			"cousin(?X,?Y) :- parent(?XP,?X), parent(?YP,?Y), cousin(?XP,?YP).\n" +

			"?- cousin(?X,?Y).\n";
		
		String expectedResults =	"cousin( '3b', '3d' )." +
									"cousin( '3b', '3e' )." +
									"cousin( '3c', '3d' )." +
									"cousin( '3c', '3e' )." +
									"cousin( '3d', '3b' )." +
									"cousin( '3e', '3b' )." +
									"cousin( '3d', '3c' )." +
									"cousin( '3e', '3c' )." +
									"cousin( '4b', '4e' )." +
									"cousin( '4e', '4b' ).";
		
		Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	/**
	 * Test for recursive rules.
	 */
	public void testRecursiveRules2() throws Exception
	{
		String program =
   			"parent('c','a')." +
			"parent('d','a')." +
			"parent('d','b')." +
			"parent('e','b')." +
			"parent('f','c')." +
			"parent('f','e')." +
			"parent('g','c')." +
			"parent('h','d')." +
			"parent('i','d')." +
			"parent('i','e')." +
			"parent('j','f')." +
			"parent('j','h')." +
			"parent('k','g')." +
			"parent('k','i')." +
			
		    "sibling(?X,?Y) :- parent(?X, ?Z), parent(?Y, ?Z), ?X != ?Y." +
		    "cousin(?X,?Y)  :- parent(?X, ?Xp), parent(?Y, ?Yp), sibling(?Xp,?Yp)." +
		    "cousin(?X,?Y)  :- parent(?X, ?Xp), parent(?Y, ?Yp), cousin(?Xp,?Yp)." +
		    
		    "related(?X,?Y) :- sibling(?X, ?Y)." +
		    "related(?X,?Y) :- related(?X, ?Z), parent(?Y, ?Z)." +
		    "related(?X,?Y) :- related(?Z, ?Y), parent(?X, ?Z)." +
		    
		    "?- sibling(?X,?Y)." +
		    "?- cousin (?X,?Y)."  +
		    "?- related(?X,?Y)." ;
		
		String expectedResults =
    		"sibling('c','d')." +
    		"sibling('d','c')." +
			"sibling('d','e')." +
			"sibling('e','d')." +
			"sibling('f','g')." +
			"sibling('g','f')." +
			"sibling('h','i')." +
			"sibling('i','h')." +
			"sibling('f','i')." +
    		"sibling('i','f')." +
		
    		"cousin('f','h')." +
    		"cousin('h','f')." +
			"cousin('f','i')." +
			"cousin('i','f')." +
			"cousin('i','i')." +
			"cousin('g','h')." +
			"cousin('h','g')." +
			"cousin('g','i')." +
			"cousin('i','g')." +
    		"cousin('h','i')." +
			"cousin('i','h')." +
    		"cousin('j','k')." +
			"cousin('k','j')." +
			"cousin('j','j')." +
			"cousin('k','k')." +
			
    		"related('c','d')." +
    		"related('d','c')." +
			"related('d','e')." +
			"related('e','d')." +
			"related('f','g')." +
			"related('g','f')." +
			"related('h','i')." +
			"related('i','h')." +
			"related('f','i')." +
    		"related('i','f')." +
    		
			"related('d','f')." +
    		"related('f','d')." +
			"related('d','g')." +
			"related('g','d')." +
			"related('c','h')." +
			"related('h','c')." +
			"related('d','i')." +
    		"related('i','d')." +
			"related('c','i')." +
			"related('i','c')." +
			"related('e','h')." +
			"related('h','e')." +
			"related('e','i')." +
			"related('i','e')." +
			"related('g','j')." +
    		"related('j','g')." +
			"related('f','k')." +
    		"related('k','f')." +
			"related('h','k')." +
			"related('k','h')." +
			"related('i','j')." + 
	    	"related('j','i')." +
	    	
			"related('f','h')." +
			"related('h','f')." +
			"related('d','j')." +
			"related('j','d')." +
			"related('g','h')." +
			"related('h','g')." +
			"related('j','k')." +
			"related('k','j')." +
			"related('g','i')." +
			"related('i','g')." +
			"related('d','k')." +
			"related('k','d')." +
			"related('c','j')." +
			"related('j','c')." +
			"related('i','i')." +
			"related('c','k')." +
			"related('k','c')." +
			"related('e','j')." +
			"related('j','e')." +
			"related('e','k')." +
			"related('k','e')." +
			
			"related('f','j')." +
			"related('j','f')." +
			"related('h','j')." +
			"related('j','h')." +
			"related('g','k')." +
			"related('k','g')." +
			"related('i','k')." +
			"related('k','i')." +
			
			"related('j','j')." +
			"related('k','k').";
			
		Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	/**
	 * Test for recursive rules.
	 */
	public void testRecursiveRules3() throws Exception
	{
		String program =
    		"down('g', 'b')." +
    		"down('h', 'c')." +
    		"down('i', 'd')." +
    		"down('l', 'f')." +
    		"down('m', 'f')." +
    		"down('p', 'k')." +
    		
    		"flat('g', 'f')." +
    		"flat('m', 'n')." +
    		"flat('m', 'o')." +
    		"flat('p', 'm')." +
    		
    		"up('a', 'e')." +
    		"up('a', 'f')." +
    		"up('f', 'm')." +
    		"up('g', 'n')." +
    		"up('h', 'n')." +
    		"up('i', 'o')." +
    		"up('j', 'o')." +
    		
		    "rsg(?X, ?Y) :- up(?X, ?W), rsg(?Q, ?W), down(?Q, ?Y)." +
		    "rsg(?X, ?Y) :- flat(?X, ?Y)." +
		    "?- rsg(?X, ?Y).";
		
		String expectedResults =
		    "rsg('a','b')." +
		    "rsg('a','c')." +
		    "rsg('a','d')." +
		    "rsg('f','k')." +
		    "rsg('g','f')." +
		    "rsg('h','f')." +
		    "rsg('i','f')." +
		    "rsg('j','f')." +
		    "rsg('m','n')." +
		    "rsg('m','o')." +
		    "rsg('p','m').";
		
		Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	/**
	 * Test for rules with a cyclic dependency.
	 * @throws Exception If something goes very wrong.
	 */
	public void testRecursionBetweenRules() throws Exception
	{
		String program = 
		    "edge('a', 'b')." +
		    "path('b', 'c')." +
		    "edge('c', 'd')." +
		    
		    "path(?X, ?Y) :- edge(?X, ?Y)." +
		    "edge(?X, ?Y) :- path(?X, ?Y)." +	
		    "path(?X, ?Y) :- edge(?X, ?Z), path(?Z, ?Y)." +
		    "?- path(?X, ?Y).";
		
    	String expectedResults = 
		    "path('a','b')." +
		    "path('a','c')." +
		    "path('a','d')." +
		    "path('b','c')." +
		    "path('b','d')." +
		    "path('c','d').";

    	Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check that rules containing a grounded term in the rule head are
	 * correctly evaluated.
	 * @throws Exception
	 */
	public void testGroundedTermInRuleHead() throws Exception
	{
    	String program = 
		    "r('a', 'a')." +
		    "r('b', 'c')." +
		    "r('c', 'd')." +
		    
		    "p(?X, 'a') :- r(?X, ?Y)." +
		    "?- p(?X, ?Y).";
    	
    	String expectedResults = 
		    "p('a','a')." +
		    "p('b','a')." +
		    "p('c','a').";

    	Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check that rules that make a self join on a relation are correctly evaluated. 
	 * @throws Exception
	 */
	public void testSelfJoin() throws Exception
	{
    	String program = 
		    "s('a', 'a')." +
		    "s('a', 'b')." +
		    "s('a', 'c')." +
		    "s('a', 'd')." +
		    "s('a', 'e')." +
		    "s('a', 'f')." +
		    "s('a', 'g')." +
		    "s('a', 'h')." +
		    "s('a', 'i')." +
		    
		    "s('b', 'a')." +
		    "s('b', 'b')." +
		    "s('b', 'c')." +
		    "s('b', 'd')." +
		    "s('b', 'e')." +
		    "s('b', 'f')." +
		    "s('b', 'g')." +
		    "s('b', 'h')." +
		    "s('b', 'i')." +
		    
		    "s('c', 'a')." +
		    "s('c', 'b')." +
		    "s('c', 'c')." +
		    "s('c', 'd')." +
		    "s('c', 'e')." +
		    "s('c', 'f')." +
		    "s('c', 'g')." +
		    "s('c', 'h')." +
		    "s('c', 'i')." +
		    
		    "s('f', 'f')." +
		    "s('f', 'g')." +
		    "s('f', 'h')." +
		    "s('f', 'i')." +
		    
		    "s('g', 'f')." +
		    "s('g', 'g')." +
		    "s('g', 'h')." +
		    "s('g', 'i')." +
		    
		    "s('h', 'f')." +
		    "s('h', 'g')." +
		    "s('h', 'h')." +
		    "s('h', 'i')." +
		    
		    "p(?X) :- s(?X, ?Y), s(?Y, ?X)." +
		    "?- p(?X).";
        	
       	String expectedResults = 
		    "p('a')." +
		    "p('h')." +
		    "p('f')." +
		    "p('c')." +
		    "p('b')." +
		    "p('g').";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	/**
	 * Check that rules that involve a cartesian product are correctly evaluated. 
	 * @throws Exception
	 */
	public void testCartesianProduct() throws Exception
	{
    	String program = 
   			"s(1)." +
   			"s(2)." +
			
			"p(3)." +
			"p(4)." +
			
		    "w(?X,?Y) :- s(?X), p(?Y)." +
		    "?- w(?Y,?X).";
        	
       	String expectedResults = 
		    "w(1, 3)." +
		    "w(1, 4)." +
		    "w(2, 3)." +
		    "w(2, 4).";

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	public void testTransitiveClosure() throws Exception
	{
		String program = 
		    "edge('a', 'b')." +
		    "path('b', 'c')." +
		    "edge('c', 'd')." +
		    
		    "path(?X, ?Y) :- edge(?X, ?Y)." +	
		    "path(?X, ?Y) :- path(?X, ?Z), path(?Z, ?Y)." +
		    "?- path(?X, ?Y).";
  	
		String expectedResults = 
		    "path('a','b')." +
		    "path('a','c')." +
		    "path('a','d')." +
		    "path('b','c')." +
		    "path('b','d')." +
		    "path('c','d').";
		
		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	public void testGroundedTermInQuery() throws Exception
	{
    	String program = 
		    "in('galway', 'ireland')." +
		    "in('dublin', 'ireland')." +
		    "in('innsbruck', 'austria')." +
		    "in('ireland', 'europe')." +
		    "in('austria', 'europe')." +
		    
		    "in(?X, ?Z) :- in(?X, ?Y), in(?Y, ?Z)." +
		    "?- in('galway', ?Z).";
		
		String expectedResults = 
    		"in('europe')." +
		    "in('ireland').";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check that a long chain of rules can be correctly evaluated, i.e. a implies b,
	 * b implies c, c implies d, etc.
	 * We do this here for a 'chain' of 676 rules.
	 * @throws Exception
	 */
	public void testLongChainOfRules() throws Exception
	{
		StringBuilder buffer = new StringBuilder();
		
		String lastName = "first";

		// Starting facts
		buffer.append( lastName ).append( "('a','b')." ).append( lastName ).append( "(1,2)." );
		
		// Some more facts along the chain.
		buffer.append( "ba" ).append( "(3,4)." );
		buffer.append( "ma" ).append( "(5,6)." );
		buffer.append( "za" ).append( "(7,8)." );
		buffer.append( "zz" ).append( "(9,10)." );
		
		buffer.append( lastName ).append( "('a','b')." ).append( lastName ).append( "(1,2)." );

		for ( char i = 'a'; i <= 'z'; ++i )
		{
			for ( char j = 'a'; j <= 'z'; ++j )
			{
				String nextName = "" + i + j;
				
				buffer.append( nextName ).append( "(?X,?Y ) :- " ).append( lastName ).append( "(?X,?Y )." );
				
				lastName = nextName;
			}
		}
		buffer.append( "?- zz(?x,?y)." );
		
		String program = buffer.toString();
		String expectedResults =
			"zz('a','b')." + 
			"zz(1,2)." +
			"zz(3,4)." +
			"zz(5,6)." +
			"zz(7,8)." +
			"zz(9,10).";
		
		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check for the correct evaluation of a logic program that contains rules that require a great
	 * deal of modification during rectification.
	 * @throws Exception 
	 */
	public void testRuleRectification() throws Exception
	{
		String program =
			"q( 1 )." +
			"r( 2 )." +
			"s( 3 )." +
			"t( 1 )." +
			"u( 4 )." +
			"p( ?X, ?Y, ?Z ) :- q( ?X ), r( ?Y ), s( ?Z )." +
			"p( ?X, ?Y, ?X ) :- q( ?X ), r( ?Y ), t( ?X )." +
			"p( ?X, 3, ?Z ) :- q( ?X ), u( ?Z )." +
			"p( ?X, 5, 5 ) :- q( ?X )." +
			"p( 5, 5, 5 ) :- q( ?X )." +
			"p( ?X, ?X, ?X ) :- q( ?X )." +
			"?- p( ?X, ?Y, ?Z ).";
		
		String expectedResults =
			"p( 1, 2, 3 )." +
			"p( 1, 2, 1 )." +
			"p( 1, 3, 4 )." +
			"p( 1, 5, 5 )." +
			"p( 5, 5, 5 )." +
			"p( 1, 1, 1 ).";
		
		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
}
