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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.deri.iris.ExecutionHelper;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.basics.BasicFactory;
import org.deri.iris.compiler.Parser;
import org.deri.iris.factory.Factory;

public class Helper
{
	public static final boolean PRINT_RESULTS = false;
	public static final boolean PRINT_TIMINGS = false;
	
	static class Timer
	{
		Timer()
		{
			mTime = System.currentTimeMillis();
		}
		
		void show( String event )
		{
			long span = System.currentTimeMillis() - mTime;
			
			if( PRINT_TIMINGS )
				System.out.println( event + ": " + span + "ms" );
			
			mTime = System.currentTimeMillis();
		}
		
		long mTime;
	}
	
	/**
	 * Evaluate a logic program using every combination of evaluation strategy
	 * and optimisation.
	 * Assert that all evaluations produce the same, expected results.
	 * @throws Exception on failure 
	 */
	public static void evaluateWithAllStrategies( String program, String expectedResults ) throws Exception
	{
		Timer timer = new Timer();

		IProgram p = ExecutionHelper.parseProgram( program );
		timer.show( "Parsing" );
		
		Map<IPredicate, IMixedDatatypeRelation> results = ExecutionHelper.evaluateNaive( p );
		timer.show( "Naive evaluation" );

		checkResults( results, expectedResults, "Naive" );
		
		// =========================================================================================
		timer = new Timer();

		p = ExecutionHelper.parseProgram( program );
		
		results = ExecutionHelper.evaluateSeminaive( p );
		timer.show( "Semi-naive evaluation" );

		checkResults( results, expectedResults, "Semi-naive" );

		// =========================================================================================
		timer = new Timer();

		p = ExecutionHelper.parseProgram( program );
		
		results = ExecutionHelper.evaluateSeminaiveWithMagicSets( p );
		timer.show( "Magic sets" );

		checkResults( results, expectedResults, "Magic sets" );
	}
	
	/**
	 * Evaluate the given logic program with all evaluation strategies and ensure that
	 * each fails with the expected exception. 
	 * @param program The logic program
	 * @param expectedExceptionClass The exception class object expected or null for an
	 * unknown exception type. 
	 */
	public  static void checkFailureWithAllStrategies( String program, Class<?> expectedExceptionClass )
	{
		try
		{
			IProgram p = ExecutionHelper.parseProgram( program );
			ExecutionHelper.evaluateNaive( p );
			junit.framework.Assert.fail( "Naive evaluation did not throw the correct exception." );
		}
		catch( Exception e )
		{
			if ( expectedExceptionClass != null )
			{
				junit.framework.Assert.assertTrue( expectedExceptionClass.isInstance( e ) );
			}
		}

		try
		{
			IProgram p = ExecutionHelper.parseProgram( program );
			ExecutionHelper.evaluateSeminaive( p );
			junit.framework.Assert.fail( "Semi-naive evaluation did not throw the correct exception." );
		}
		catch( Exception e )
		{
			if ( expectedExceptionClass != null )
			{
				junit.framework.Assert.assertTrue( expectedExceptionClass.isInstance( e ) );
			}
		}

		try
		{
			IProgram p = ExecutionHelper.parseProgram( program );
			ExecutionHelper.evaluateSeminaiveWithMagicSets( p );
			junit.framework.Assert.fail( "Magic sets evaluation did not throw the correct exception." );
		}
		catch( Exception e )
		{
			if ( expectedExceptionClass != null )
			{
				junit.framework.Assert.assertTrue( expectedExceptionClass.isInstance( e ) );
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
	public static void checkResults( Map<IPredicate, IMixedDatatypeRelation> actual, String expected, String evaluationStrategy ) throws Exception
	{
		Map<IPredicate, IMixedDatatypeRelation> f = new HashMap<IPredicate, IMixedDatatypeRelation>();
		Set<IRule> r = new HashSet<IRule>();
		Set<IQuery> q = new HashSet<IQuery>();

		IProgram pExpected = Factory.PROGRAM.createProgram( f, r, q );
		Parser.parse( expected, pExpected );

		if ( PRINT_RESULTS )
			System.out.println( ExecutionHelper.resultsTostring( actual ) );

		Map<IPredicate, IMixedDatatypeRelation> expectedFacts = pExpected.getFacts();

		for( Map.Entry<IPredicate, IMixedDatatypeRelation> entry : expectedFacts.entrySet() )
		{
			IPredicate pr = entry.getKey();
			IMixedDatatypeRelation expectedPredicate = entry.getValue();
			
			IMixedDatatypeRelation actualPredicate = actual.get( pr );
			
			// TODO - see bug 1794659
			// Strange behaviour - the arity of the predicate that indexes the relation
			// that is the result of a query is very hard to predict, i.e. it might
			// not have the same arity as the relations! Try expected or zero or others...
			if ( actualPredicate == null )
			{
				for( int arity = 0; arity < 10; ++arity )
				{
					IPredicate tempPred = makePredicate( pr.getPredicateSymbol(), arity );
			
					actualPredicate = actual.get( tempPred );
					if ( actualPredicate != null )
						break;
				}
			}
			
			if ( expectedPredicate != null && actualPredicate == null )
				junit.framework.Assert.fail();

			if ( expectedPredicate == null && actualPredicate != null )
				junit.framework.Assert.fail();
			
			if ( expectedPredicate != null )
			{
				junit.framework.Assert.assertEquals( evaluationStrategy + ": The relation did not have the expected number of tuples",
								expectedPredicate.size(), actualPredicate.size() );
				junit.framework.Assert.assertTrue( evaluationStrategy + ": The relation did not contain all the expected tuples",
								expectedPredicate.containsAll( actualPredicate ) );
				junit.framework.Assert.assertTrue( evaluationStrategy + ": The relation contains tuples that were not expected",
								actualPredicate.containsAll( expectedPredicate ) );
			}
		}
	}

	public static IPredicate makePredicate( String symbol, int arity )
	{
		return BasicFactory.getInstance().createPredicate( symbol, arity );
	}
}
