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
import org.deri.iris.compiler.Parser;
import org.deri.iris.factory.Factory;
import junit.framework.TestCase;

public class FunctionalTest extends TestCase
{
	protected void setUp() throws Exception
	{
	}

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
	 * Check that tuples with varying types happily co-exist in the same relation.
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
	 * Assert that the <, <=, =, !=, >, >= operators function as expected for all
	 * data types.
	 */
	public void testBuiltInDataTypes()
	{
		fail( "Not implemented yet." );
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
	 * Check that a long chain of rules can be correctly evaluated.
	 * @throws Exception
	 */
	public void testLongChainOfRules() throws Exception
	{
		String program =
			"a('a','b')." +
			"b(?x,?y) :- a(?x,?y)." +
			"c(?x,?y) :- b(?x,?y)." +
			"d(?x,?y) :- c(?x,?y)." +
			"e(?x,?y) :- d(?x,?y)." +
			"f(?x,?y) :- e(?x,?y)." +
			"g(?x,?y) :- f(?x,?y)." +
			"h(?x,?y) :- g(?x,?y)." +
			"i(?x,?y) :- h(?x,?y)." +
			"j(?x,?y) :- i(?x,?y)." +
			"k(?x,?y) :- j(?x,?y)." +
			"l(?x,?y) :- k(?x,?y)." +
			"m(?x,?y) :- l(?x,?y)." +
			"n(?x,?y) :- m(?x,?y)." +
			"o(?x,?y) :- n(?x,?y)." +
			"p(?x,?y) :- o(?x,?y)." +
			"q(?x,?y) :- p(?x,?y)." +
			"r(?x,?y) :- q(?x,?y)." +
			"s(?x,?y) :- r(?x,?y)." +
			"t(?x,?y) :- s(?x,?y)." +
			"u(?x,?y) :- t(?x,?y)." +
			"v(?x,?y) :- u(?x,?y)." +
			"w(?x,?y) :- v(?x,?y)." +
			"x(?x,?y) :- w(?x,?y)." +
			"y(?x,?y) :- x(?x,?y)." +
			"z(?x,?y) :- y(?x,?y)." +
			"?-z(?x,?y).";
		String expectedResults = "z('a','b').";

		fail( "This program takes a VERY LONG TIME to evaluate with anything other than 'naive' evaluation." );
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
	 * @param expectedExceptionClass The exception class object expected. 
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
	public void checkResults( Map<IPredicate, IMixedDatatypeRelation> test, String correct, String evaluationStrategy ) throws Exception
	{
		Map<IPredicate, IMixedDatatypeRelation> f = new HashMap<IPredicate, IMixedDatatypeRelation>();
		Set<IRule> r = new HashSet<IRule>();
		Set<IQuery> q = new HashSet<IQuery>();

		IProgram p = Factory.PROGRAM.createProgram( f, r, q );
		Parser.parse( correct, p );

		IMixedDatatypeRelation r0 = null;
		IMixedDatatypeRelation r1 = null;
		
		for( IPredicate pr : p.getPredicates() )
		{
			r0 = p.getFacts().get( pr );
			r1 = test.get( pr );
			
			if ( r0 != null && r1 == null )
				fail();

			if ( r0 == null && r1 != null )
				fail();
			
			if ( r0 != null )
			{
				assertEquals( evaluationStrategy + ": Each relation must have the same number of tuples",
								r0.size(), r1.size() );
				assertTrue( evaluationStrategy + ": The output relation must contain all expected tuples",
								r0.containsAll( r1 ) );
				assertTrue( evaluationStrategy + ": The expected relation must contain all out tuples",
								r1.containsAll( r0 ) );
			}
		}
	}
}
