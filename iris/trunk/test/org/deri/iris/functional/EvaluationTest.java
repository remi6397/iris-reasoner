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

public class EvaluationTest extends TestCase
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
	 * Check that IRIS copes with predicates with the same name, but different arities.
	 * @throws Exception 
	 */
	public void testPredicateWithSeveralArities() throws Exception
	{
		String facts =
			"p(8,9,10,11)." +
			"p(12,13,14,15)." +
			"p(5,6,7)." +
			"p(6,6,8)." +
			"p(3,4)." +
			"p(1)." +
			"p(2)." +
			"p.";
		
		String program = facts +
			"?-p(?x,?y,?z,?a)." +
			"?-p(?x,?y,?z)." +
			"?-p(?x,?y)." +
			"?- p(?x)." +
			"?- p.";
		
		String expectedResults = facts;
			
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
	
	/**
	 * This was supposed to be a test involving relations with thousands of tuples, but as
	 * it turns out, IRIS runs out of heap when there are 100 rows in each predicate.
	 * About 50 rows takes about 20 seconds to evaluate with naive.
	 * 
	 * @throws Exception
	 */
	public void testJoinWithLargeDataSets() throws Exception
	{
		StringBuilder p = new StringBuilder();
		StringBuilder r = new StringBuilder();
		
		final int MAX = 50;
		
		for( int i = 0; i < MAX; ++i )
		{
			p.append( "p(" + i + ")." );
			p.append( "q(" + i + ")." );
			p.append( "r(" + i + ")." );
			
			r.append( "t(" + i + "," + i + "," + i + ")." );
		}
		
		p.append( "t(?X,?Y,?Z) :- p(?X), q(?Y), r(?Z), ?X = ?Y, ?Y = ?Z." );
		p.append( "?- t( ?X, ?Y, ?Z )." );
		
		String program = p.toString();
		String expectedResults = r.toString();

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}

	public void testQueryForUnknownPredicate() throws Exception
	{
    	String program = 
		    "p(1)." +
		    "p(2)." +
		    
		    "q(?X) :- p(?X)." +
		    "?- r(?x).";
		
		String expectedResults = "";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Assert that the evaluation of 3 rules that have a unified head predicate evaluate correctly.
	 * Internally, the 3 rules for p(X) will be converted to relational algebra with a union.
	 */
	public void testUnion() throws Exception
	{
		String program = 
 			"r(1)." +
		    "r(2)." +
		    
 			"s(3)." +
		    "s(4)." +
		    
 			"t(5)." +
		    "t(6)." +
		    
		    "p(?X) :- r(?X)." +
		    "p(?X) :- s(?X)." +
		    "p(?X) :- t(?X)." +
		    "?- p(?X).";
		
		String expectedResults =
			"p(1)." +
			"p(2)." +
			"p(3)." +
			"p(4)." +
			"p(5)." +
			"p(6).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}	

	/**
	 * Assert that the evaluation of 3 rules that have a unified head predicate evaluate correctly.
	 * Internally, the 3 rules for p(X) will be converted to relational algebra with a union.
	 */
	public void testFibonacci() throws Exception
	{
		String program = 
			"fib(0,0)." +
			"fib(1,1)." +

			"fib(?n, ?f ) :- ?n > 1," +
			                "?n1 + 1 = ?n," +
			                "?n2 + 2 = ?n," +
			                "fib( ?n1, ?f1 )," +
			                "fib( ?n2, ?f2 )," +
			                "?f1 + ?f2 = ?f," +
			                "?f < 1000000000," +  // To add a higher limit
			                "?f > 0." +           // To catch integer overflow
			"?- fib(?n,?f).";

		String expectedResults =
			"fib(0, 0)." +
			"fib(1, 1)." +
			"fib(2, 1)." +
			"fib(3, 2)." +
			"fib(4, 3)." +
			"fib(5, 5)." +
			"fib(6, 8)." +
			"fib(7, 13)." +
			"fib(8, 21)." +
			"fib(9, 34)." +
			"fib(10, 55)." +
			"fib(11, 89)." +
			"fib(12, 144)." +
			"fib(13, 233)." +
			"fib(14, 377)." +
			"fib(15, 610)." +
			"fib(16, 987)." +
			"fib(17, 1597)." +
			"fib(18, 2584)." +
			"fib(19, 4181)." +
			"fib(20, 6765)." +
			"fib(21, 10946)." +
			"fib(22, 17711)." +
			"fib(23, 28657)." +
			"fib(24, 46368)." +
			"fib(25, 75025)." +
			"fib(26, 121393)." +
			"fib(27, 196418)." +
			"fib(28, 317811)." +
			"fib(29, 514229)." +
			"fib(30, 832040)." +
			"fib(31, 1346269)." +
			"fib(32, 2178309)." +
			"fib(33, 3524578)." +
			"fib(34, 5702887)." +
			"fib(35, 9227465)." +
			"fib(36, 14930352)." +
			"fib(37, 24157817)." +
			"fib(38, 39088169)." +
			"fib(39, 63245986)." +
			"fib(40, 102334155)." +
			"fib(41, 165580141)." +
			"fib(42, 267914296)." +
			"fib(43, 433494437)." +
			"fib(44, 701408733).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}	

	/**
	 * This program is known to cause the magic sets evaluation to give incorrect results
	 * if the Facts class does not hash the predicate names correctly.
	 * See bug 1822055
	 */
	public void testMagic1() throws Exception
	{
		String program = 
			"p(?X,?Y) :- b(?X,?Y)." +
			"p(?X,?Y) :- b(?X,?U), p(?U,?Y)." +

			"e(?X,?Y) :- g(?X,?Y)." +
			"e(?X,?Y) :- g(?X,?U), e(?U,?Y)." +

			"a(?X,?Y) :- e(?X,?Y), not p(?X,?Y)." +

			"b(1,2)." +
			"b(2,1)." +
			"g(2,3)." +
			"g(3,2)." +
			"?- a(2,?Y).";
		
		String expectedResults =
			"a(3).";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}	

	/**
	 * This reproduces bug: 1829204 Repeated literal in query fails with magic sets
	 * @throws Exception
	 */
	public void testMagic2() throws Exception
	{
		String program = 
			"p(1)."+
			"?-p(1),p(1).";
		
		String expectedResults =
			"p.";

		Helper.evaluateWithAllStrategies( program, expectedResults );
	}	

}

