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
package org.deri.iris;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.basics.BasicFactory;
import org.deri.iris.compiler.Parser;
import org.deri.iris.factory.Factory;
import junit.framework.TestCase;

public class FunctionalTest extends TestCase
{
	public static final boolean PRINT_RESULTS = false;
	
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
		evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Test that a query with more than one predicate is correctly
	 * evaluated.
	 * TODO Semi-naive with Magic Sets evaluation is expected to fail, because
	 * this strategy can not currently have multiple predicates a rule.
	 * @throws Exception 
	 */
	public void testQueryWithMultiplePredicates() throws Exception
	{
		String program =
			"p( _datetime(2000,1,1,2,2,2) )." +
			"p( _datetime(2000,12,1,2,2,2) )." +
			"?- p( ?X ), p( ?Y ), ?X < ?Y.";
		
		String expectedResults =
			"p( _datetime(2000,1,1,2,2,2), _datetime(2000,12,1,2,2,2) ).";
		
		evaluateWithAllStrategies( program, expectedResults );
	}
		
	/**
	 * Check that a rule containing a negated sub-goal that has a variable that
	 * is not in the rule head can still be evaluated.
	 * 
	 * In this case, the rule:
	 * 
	 * 		bachelor( ?X ) :- male( ?X ), not married( ?X, ?Y )
	 * 
	 * should effectively be rewritten as:
	 * 
	 * 		husband( ?X ) :- married( ?X, ?Y )
	 * 		bachelor( ?X ) :- male( ?X ), not husband( ?X )
	 * 
	 * for the purposes of evaluation.
	 * @throws Exception 
	 */
	public void testRuleRewritingForVariableInNegatedSubGoalThatIsNotInRuleHead() throws Exception
	{
		String program =
			"married( 1, 2 )." +
			"married( 3, 4 )." +
			"married( 5, 6 )." +
			"male( 1 )." +
			"male( 3 )." +
			"male( 5 )." +
			"male( 7 )." +
			"male( 9 )." +
			"bachelor( ?X ) :- male( ?X ),not married( ?X, ?Y )." +
			"?- bachelor( ?X ).";
		
		String expectedResults = "bachelor( 7 ).bachelor( 9 ).";
		evaluateWithAllStrategies( program, expectedResults );
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
		evaluateWithAllStrategies( program, expectedResults );
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
		evaluateWithAllStrategies( program, expectedResults );
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
		
		evaluateWithAllStrategies( program, expectedResults );
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
			
		evaluateWithAllStrategies( program, expectedResults );
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
		
		evaluateWithAllStrategies( program, expectedResults );
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

    	evaluateWithAllStrategies( program, expectedResults );
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

    	evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * A logic program with stratified negation.
	 * @throws Exception
	 */
	public void testStratifiedNegation() throws Exception
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

       	evaluateWithAllStrategies( program, expectedResults );
	}

	/**
	 * This test makes sure that sub-goals are evaluated in the correct order,
	 * i.e. positive literals first, even when the negative literal appears first. 
	 * @throws Exception
	 */
	public void teststratifiedNegationWithNegatedSubGoalFirst() throws Exception
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

       	evaluateWithAllStrategies( program, expectedResults );
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

       	evaluateWithAllStrategies( program, expectedResults );
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

       	evaluateWithAllStrategies( program, expectedResults );
	}

	/**
	 * Test a logic program with simple negation.
	 * @throws Exception
	 */
	public void testNegation() throws Exception
	{
    	String program = 
   			"s(1)." +
			"p(2,2)." +
			"p(3,9)." +
			"r(9)." +
			
			"w(?X,?Y) :- s(?X), r(?Y), not p(2,?Y)." +
		    "?- w(?X,?Y).";
        	
       	String expectedResults = 
		    "w(1, 9).";

       	evaluateWithAllStrategies( program, expectedResults );
	}

	/**
	 * Ensure that this logic program with unsafe negation is not evaluated.
	 * @throws Exception
	 */
	public void testUnsafeNegation() throws Exception
	{
    	String program = 
   			"s(1)." +
			"p(2,2)." +
			"p(3,9)." +
			"r(9)." +
			
		    // Unsafe rule: 
			"w(?X,?Y) :- s(?X), not p(2,?Y)." +
		    "?- w(?X,?Y).";

    	// TODO Pass the expected exception class's Class object,
    	// e.g. EvaluationException.class
		checkFailureWithAllStrategies( program, null );
	}

	/**
	 * Check that tuples with various types happily co-exist in the same relation.
	 */
	public void testMixedDataTypes() throws Exception
	{
		String facts =
			"p( 'a', 'string' )." +
			"p( 'a', 7 )." +
			"p( 'a', _integer( 8 ) )." +
			"p( 'a', -7.123 )." +
			"p( 'a', _decimal( -8.123 ) )." +
			"p( 'a', _float( -9.123 ) )." +
			"p( 'a', 'true' )." +
			"p( 'a', _boolean( 'false' ) )." +
			"p( 'a', _gmonthday( 6, 7 ) )." +
			"p( 'a', _gyearmonth( 4, 5 ) )." +
			"p( 'a', _gyear( 5 ) )." +
			"p( 'a', _gmonth( 4 ) )." +
			"p( 'a', _gday( 3 ) )." +
			"p( 'a', _duration( 1, 2, 3, 4, 5, 6) )." +
			"p( 'a', _time( 1, 1, 1 ) )." +
			"p( 'a', _date( 2001, 8, 1 ) )." +
			"p( 'a', _datetime(2000,1,1,2,2,2) ).";
			
		String program = facts +
			"?- p(?X, ?Y ).";
		
       	String expectedResults = facts;

       	evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Test that the evaluation strategy correctly handles relations with
	 * arguments of different data types occurring at the same position.
	 * 
	 * Need to check that = is always false when comparing arguments
	 * of non-compatible data types, i.e.
	 * any numeric can be tested against any other numeric, otherwise
	 * the datatypes must exactly match.
	 */
	public void testMixedDataTypes_Equality() throws Exception
	{
		String program =
			"p( 'a', 'b' )." +
			"p( 'c', 7 )." +
			"p( 1.23, _datetime(2000,1,1,2,2,2) )." +
			
			"q( ?X, ?Y ) :- p( ?X, ?Y ), ?Y = 7 ." +
			
			"?- q(?X, ?Y ).";
		
       	String expectedResults = 
			"q( 'c', 7 ).";

       	evaluateWithAllStrategies( program, expectedResults );
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
	public void testMixedDataTypes_Inequality() throws Exception
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

       	evaluateWithAllStrategies( program, expectedResults );
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
	public void testMixedDataTypes_NoErrorsWhenComparingDifferingDatatypes() throws Exception
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

       	evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Create (valid) facts using all possible data types.
	 * @throws Exception 
	 */
	public void testValidDataTypes() throws Exception
	{
		String allDataTypes =
			"p( _string( 'a string' ) )." +
			"p( 'literal string' )." +
			
			"p( _decimal( -1.11 ) )." +
			"p( 2.22 )." +
			
			"p( _integer( 333 ) )." +
			"p( -444 )." +
			
			"p( _float( 5.55 ) )." +
			
			"p( _double( 6.66 ) )." +
			
			"p( _iri( 'http://example.org/PersonOntology#Person' ) )." +
			"p( _'http://example.org/PersonOntology#Human' )." +
			
			"p( dc#title )." +
			"p( _sqname( foaf#name ) )." +

			"p( _boolean( 'true' ) )." +
			"p( _boolean( 'false' ) )." +

			"p( _duration( 1970, 1, 1, 23, 15, 30 ) )." +
			"p( _duration( 1970, 1, 1, 23, 15, 29, 99 ) )." +

			"p( _datetime( 1980, 2, 2, 1, 2, 3 ) )." +
			"p( _datetime( 1980, 2, 2, 1, 2, 3, 1, 30 ) )." +
			"p( _datetime( 1980, 2, 2, 1, 2, 3, 99, 1, 30 ) )." +
			
			"p( _date( 1981, 3, 3 ) )." +
			"p( _date( 1982, 4, 4, 13, 30 ) )." +
			
			"p( _time( 1, 2, 3 ) )." +
			"p( _time( 1, 2, 3, 1, 30 ) )." +
			"p( _time( 1, 2, 3, 99, 1, 30 ) )." +
			
			"p( _gyear( 1991 ) )." +
			"p( _gyearmonth( 1992, 2 ) )." +
			"p( _gmonth( 3 ) )." +
			"p( _gmonthday( 2, 28 ) )." +
			"p( _gday( 31 ) )." +
			
			"p( _hexbinary( '0FB7abcd' ) )." +
			"p( _base64binary( 'QmFycnkgQmlzaG9w' ) )." +
			"";

		String program =
			allDataTypes +
			"?- p( ?X ).";
		
       	String expectedResults = allDataTypes;

       	evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidDataTypes()
	{
		checkFailureWithAllStrategies( "p( _string( 'a', 'b' ) ).", null );

		checkFailureWithAllStrategies( "p( _decimal( -1.A1 ) ).", null );
		checkFailureWithAllStrategies( "p( 1.2B ).", null );
		
		checkFailureWithAllStrategies( "p( _integer( -B ) ).", null );
		checkFailureWithAllStrategies( "p( -C ).", null );

		checkFailureWithAllStrategies( "p( _float( 3.r3) ).", null );
		checkFailureWithAllStrategies( "p( _double( -2.3u ) ).", null );

		checkFailureWithAllStrategies( "p( _iri( 'http://example.org/PersonOntology #Person' ) ).", null );
		checkFailureWithAllStrategies( "p( _'http://example.org/ PersonOntology#Human' ).", null );
		
		checkFailureWithAllStrategies( "p( dc #title ).", null );
		checkFailureWithAllStrategies( "p( _sqname( foaf name ) ).", null );
		
		// TODO This should fail
		checkFailureWithAllStrategies( "p( _boolean( 'blah' ) ).", null );

		// Too few parameters
		checkFailureWithAllStrategies( "p( _datetime( 1982, 3, 4, 12, 30 ) ).", null );
		checkFailureWithAllStrategies( "p( _datetime( 1982, 3, 4, 12, 30, 0, 1, 2, 3, 4 ) ).", null );

		// Bad month
		checkFailureWithAllStrategies( "p( _datetime( 1982, 13, 4, 12, 30, 0 ) ).", null );
		checkFailureWithAllStrategies( "p( _datetime( 1982, 0, 4, 12, 30, 0 ) ).", null );

		// Bad day
		checkFailureWithAllStrategies( "p( _datetime( 1982, 12, 32, 12, 30, 0 ) ).", null );
		checkFailureWithAllStrategies( "p( _datetime( 1982, 1, 0, 12, 30, 0 ) ).", null );

		// Bad hour
		checkFailureWithAllStrategies( "p( _datetime( 1982, 12, 31, 24, 30, 0 ) ).", null );
		checkFailureWithAllStrategies( "p( _datetime( 1982, 1, 1, -1, 30, 0 ) ).", null );

		// Bad minute
		checkFailureWithAllStrategies( "p( _datetime( 1982, 12, 31, 23, 60, 0 ) ).", null );
		checkFailureWithAllStrategies( "p( _datetime( 1982, 1, 1, 23, -1, 0 ) ).", null );

		// Bad second, NB There can be leap seconds!
		checkFailureWithAllStrategies( "p( _datetime( 1982, 12, 31, 23, 59, 61 ) ).", null );
		checkFailureWithAllStrategies( "p( _datetime( 1982, 1, 1, 23, 0, -1 ) ).", null );

		// Bad millisecond
		checkFailureWithAllStrategies( "p( _datetime( 1982, 12, 31, 23, 59, 59, 1000 ) ).", null );
		checkFailureWithAllStrategies( "p( _datetime( 1982, 1, 1, 23, 0, 0, -1 ) ).", null );

		// Bad time zone hour
		checkFailureWithAllStrategies( "p( _datetime( 1982, 12, 31, 23, 59, 59, 999, 25, 30 ) ).", null );
		checkFailureWithAllStrategies( "p( _datetime( 1982, 1, 1, 23, 0, 0, 0, -25, 0 ) ).", null );

		// TODO These should fail
		// Bad time zone minute
		checkFailureWithAllStrategies( "p( _datetime( 1982, 12, 31, 23, 59, 59, 999, 1, 60 ) ).", null );
		checkFailureWithAllStrategies( "p( _datetime( 1982, 1, 1, 23, 0, 0, 0, -1, -60 ) ).", null );

		// Wrong number of parameters
		checkFailureWithAllStrategies( "p( _date( 1982, 3 ) ).", null );
		checkFailureWithAllStrategies( "p( _date( 1982, 3, 4, 12, 30, 1 ) ).", null );

		// Wrong number of parameters
		checkFailureWithAllStrategies( "p( _time( 12, 30 ) ).", null );
		checkFailureWithAllStrategies( "p( _time( 12, 30, 0, 99, 13, 0, 1 ) ).", null );
	
		checkFailureWithAllStrategies( "p( _yearmonth( 1980 ) ).", null );
		checkFailureWithAllStrategies( "p( _yearmonth( 1980, 12, 1 ) ).", null );
		checkFailureWithAllStrategies( "p( _yearmonth( 1980, 13 ) ).", null );
		checkFailureWithAllStrategies( "p( _yearmonth( 1980, 0 ) ).", null );

		checkFailureWithAllStrategies( "p( _monthday( 12 ) ).", null );
		checkFailureWithAllStrategies( "p( _monthday( 12, 1, 1 ) ).", null );
		checkFailureWithAllStrategies( "p( _monthday( 13, 1 ) ).", null );
		checkFailureWithAllStrategies( "p( _monthday( 0, 1 ) ).", null );
		checkFailureWithAllStrategies( "p( _monthday( 12, 32 ) ).", null );
		checkFailureWithAllStrategies( "p( _monthday( 1, 0 ) ).", null );
		
		// Invalid hexadecimal
		checkFailureWithAllStrategies( "p( _hexbinary( '0FB7abcdG' ) ).", null );
		
		// Invalid base 64
		checkFailureWithAllStrategies( "p( _base64binary( 'QmFycnkgQmlzaG9wa' ) ).", null );
	}
	
	/**
	 * Assert that every built in predicate functions correctly with every
	 * possible data type.
	 */
	public void testBuiltinPredicates()
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

		evaluateWithAllStrategies( program, expectedResults );
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

		evaluateWithAllStrategies( program, expectedResults );
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

		evaluateWithAllStrategies( program, expectedResults );
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

		evaluateWithAllStrategies( program, expectedResults );
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

		evaluateWithAllStrategies( program, expectedResults );
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

		evaluateWithAllStrategies( program, expectedResults );
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

		evaluateWithAllStrategies( program, expectedResults );
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

		evaluateWithAllStrategies( program, expectedResults );
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

		evaluateWithAllStrategies( program, expectedResults );
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

		evaluateWithAllStrategies( program, expectedResults );
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
		
		evaluateWithAllStrategies( program, expectedResults );
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

		evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Assert that evaluations of logic programs containing globally stratified
	 * negated subgoals give the correct results.
	 * @throws Exception 
	 */
	public void testGloballyStratifiedNegation() throws Exception
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

		evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Assert that evaluations of logic programs containing negated sub goals
	 * that cannot be stratified are detected correctly.
	 */
	public void testDetectNotStratified()
	{
		String program =
			"q( 5 )." +
			"p( ?X ) :- r( ?X ),not q(?X)." +
			"q( ?X ) :- r( ?X ),not p(?X)." +
			"?- q( ?X ).";
		
    	// TODO Pass the expected exception class's Class object.
		checkFailureWithAllStrategies( program, null );
	}
	
	/**
	 * Assert that logic programs that contain unsafe rules are detected.
	 * @throws Exception 
	 */
	public void testDetectUnsafeRules() throws Exception
	{
		String program =
			"p( 5 )." +
			"p( 7 )." +
			"pp( ?X, ?Y ) :- p( ?X )." +
			"?- pp( ?X, ?Y ).";
		
    	// TODO Pass the expected exception class's Class object.
		checkFailureWithAllStrategies( program, null );
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
		
		evaluateWithAllStrategies( program, expectedResults );
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
		
		evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Evaluate a logic program using every combination of evaluation strategy
	 * and optimisation.
	 * Assert that all evaluations produce the same, expected results.
	 * @throws Exception on failure 
	 */
	private void evaluateWithAllStrategies( String program, String expectedResults ) throws Exception
	{
		checkResults( ExecutionHelper.evaluateNaive( program ), expectedResults, "Naive" );
		checkResults( ExecutionHelper.evaluateSeminaive( program ), expectedResults, "Semi-naive" );
		checkResults( ExecutionHelper.evaluateSeminaiveWithMagicSets( program ), expectedResults, "Magic sets" );
	}
	
	/**
	 * Evaluate the given logic program with all evaluation strategies and ensure that
	 * each fails with the expected exception. 
	 * @param program The logic program
	 * @param expectedExceptionClass The exception class object expected or null for an
	 * unknown exception type. 
	 */
	private void checkFailureWithAllStrategies( String program, Class expectedExceptionClass )
	{
		try
		{
			ExecutionHelper.evaluateNaive( program );
			fail( "Naive evaluation did not throw the correct exception." );
		}
		catch( Exception e )
		{
			if ( expectedExceptionClass != null )
			{
				assertTrue( expectedExceptionClass.isInstance( e ) );
			}
		}

		try
		{
			ExecutionHelper.evaluateSeminaive( program );
			fail( "Semi-naive evaluation did not throw the correct exception." );
		}
		catch( Exception e )
		{
			if ( expectedExceptionClass != null )
			{
				assertTrue( expectedExceptionClass.isInstance( e ) );
			}
		}

		try
		{
			ExecutionHelper.evaluateSeminaiveWithMagicSets( program );
			fail( "Magic sets evaluation did not throw the correct exception." );
		}
		catch( Exception e )
		{
			if ( expectedExceptionClass != null )
			{
				assertTrue( expectedExceptionClass.isInstance( e ) );
			}
		}
	}


	/**
	 * Parse the correct facts in to a dummy program, then compare these facts with
	 * what is provided from the result of evaluating a logic program.
	 * @param test
	 * @param correct
	 * @throws Exception
	 */
	public void checkResults( Map<IPredicate, IMixedDatatypeRelation> actual, String expected, String evaluationStrategy ) throws Exception
	{
		Map<IPredicate, IMixedDatatypeRelation> f = new HashMap<IPredicate, IMixedDatatypeRelation>();
		Set<IRule> r = new HashSet<IRule>();
		Set<IQuery> q = new HashSet<IQuery>();

		IProgram p = Factory.PROGRAM.createProgram( f, r, q );
		Parser.parse( expected, p );

		if ( PRINT_RESULTS )
			System.out.println( ExecutionHelper.resultsTostring( actual ) );

		for( IPredicate pr : p.getPredicates() )
		{
			IMixedDatatypeRelation expectedPredicate = p.getFacts( pr );
			
			IMixedDatatypeRelation actualPredicate = actual.get( pr );
			
			// TODO
			// Strange behaviour - the arity of the predicate that indexes the relation
			// that is the result of a query is very hard to predict, i.e. it might
			// not have the same arity as the relations! Try expected or zero...
			
			if ( actualPredicate == null )
			{
				IPredicate tempPred = makePredicate( pr.getPredicateSymbol(), 0 );
			
				actualPredicate = actual.get( tempPred );
			}
			
			if ( expectedPredicate != null && actualPredicate == null )
				fail();

			if ( expectedPredicate == null && actualPredicate != null )
				fail();
			
			if ( expectedPredicate != null )
			{
				assertEquals( evaluationStrategy + ": Each relation must have the same number of tuples",
								expectedPredicate.size(), actualPredicate.size() );
				assertTrue( evaluationStrategy + ": The output relation must contain all expected tuples",
								expectedPredicate.containsAll( actualPredicate ) );
				assertTrue( evaluationStrategy + ": The expected relation must contain all out tuples",
								actualPredicate.containsAll( expectedPredicate ) );
			}
		}
	}

	IPredicate makePredicate( String symbol, int arity )
	{
		return BasicFactory.getInstance().createPredicate( symbol, arity );
	}
}
