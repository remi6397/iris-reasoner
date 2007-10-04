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

import org.deri.iris.RuleUnsafeException;
import junit.framework.TestCase;

public class RuleSafetyTest extends TestCase
{

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
    public void testSafe_Variable_InNegatedSubGoal_NotInRuleHead_NotInPositiveLiteral() throws Exception
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
    	Helper.evaluateWithAllStrategies( program, expectedResults );
    }

	/**
     * Simple negation.
     * @throws Exception
     */
    public void testSafe_Variable_InNegatedSubGoal_InRuleHead_InPositiveLiteral() throws Exception
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
    
       	Helper.evaluateWithAllStrategies( program, expectedResults );
    }

	/**
     * Rule with unsafe negation.
     * @throws Exception
     */
    public void testUnsafe_Variable_InNegatedSubGoal_InRuleHead_NotInPositiveLiteral() throws Exception
    {
    	String program = "w(?X,?Y) :- s(?X), not p(2,?Y).";

    	Helper.checkFailureWithAllStrategies( program, RuleUnsafeException.class );
    }

	/**
     * Unsafe rule with head variable not in the body.
     * @throws Exception 
     */
    public void testUnsafe_Variable_InHead_NotInBody() throws Exception
    {
    	String program = "p( ?X, ?Y ) :- q( ?X ).";
    	
    	Helper.checkFailureWithAllStrategies( program, RuleUnsafeException.class );
    }

	/**
     * Simple rule with every head variable in a positive, ordinary predicate.
     * @throws Exception 
     */
    public void testSafe_Variable_InHead_InPositiveLiteral() throws Exception
    {
    	String program =
    		"a( 0 )." +
    		"b( 1 )." +
    		"c( 2 )." +
    		"d( 3 )." +
    		"e( 4 )." +
    		"f( 5 )." +
    		"g( 6 )." +
    		"h( 7 )." +
    		"i( 8 )." +
    		"j( 9 )." +
    		"all( ?A, ?B, ?C, ?D, ?E, ?F, ?G, ?H, ?I, ?J ) :- a( ?A ), b( ?B ), c( ?C ), d( ?D ), e( ?E ), f( ?F ), g( ?G ), h( ?H ), i( ?I ), j( ?J )." +
    		"?- all( ?A, ?B, ?C, ?D, ?E, ?F, ?G, ?H, ?I, ?J ).";
    	
    	String expectedResults = "all( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 ).";
    	Helper.evaluateWithAllStrategies( program, expectedResults );
    }

	/**
     * Try to execute a rule with an unsafe use of a built-in operator.
     * 
     * (It is unsafe because every variable in the built-in predicate must appear
     * in a positive, ordinary predicate in the same rule.)
     * 
     * @throws Exception
     */
    public void testUnsafe_Variable_InBuiltin_InRuleHead_NotInPositiveLiteral()
    {
    	String program = "p(?X, ?Y) :- q(?X), ?X < ?Y.";
    
    	Helper.checkFailureWithAllStrategies( program, RuleUnsafeException.class );
    }

	/**
     * A rule with a built-in equality that makes a variable limited
     * (and therefore the rule safe).
     * @throws Exception
     */
    public void testSafe_Variable_InEquality_InRuleHead_NotInPositiveLiteral() throws Exception
    {
    	String program =
    		"id(1)." +
    		"p(?X, ?Y) :- id(?X), ?X = ?Y." +
    		"?- p(?X, ?Y).";

    	String expectedResults = "p( 1, 1 ).";
    
    	Helper.evaluateWithAllStrategies( program, expectedResults );
    }

	/**
     * Unsafe unary predicate.
     * @throws Exception
     */
    public void testUnsafe_Variable_InUnaryBuiltin_NotInPositiveLiteral() throws Exception
    {
    	String program = "w(?X,?Y) :- s(?X), ISSTRING(?Y).";    

    	Helper.checkFailureWithAllStrategies( program, RuleUnsafeException.class );
    }
    
	/**
     * Unsafe unary predicate.
     * @throws Exception
     */
	public void testUnsafe_Variable_InUnaryBuiltin_NotInPositiveLiteral_NotInHead() throws Exception
	{
		String program =
			"man('homer')." +
			"isMale(?x) :- man(?x), ISSTRING(?z)." +
			"?-isMale(?x).";
	
		Helper.checkFailureWithAllStrategies( program, RuleUnsafeException.class );
	}

	/**
     * Unsafe unary predicate.
     * @throws Exception
     */
    public void testSafe_Variable_InUnaryBuiltin_InPositiveLiteral() throws Exception
    {
    	String program =
    		"s(1)." +
    		"p(2)." +
    		"p('s')." +
    		"w(?X,?Y) :- s(?X), p(?Y), ISSTRING(?Y)." +
    		"?- w(?X, ?Y).";    

    	String expectedResults = "w( 1, 's' ).";
        
    	Helper.evaluateWithAllStrategies( program, expectedResults );
    }
}
