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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.deri.iris.Configuration;
import org.deri.iris.KnowledgeBaseFactory;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.compiler.Parser;
import org.deri.iris.evaluation.IEvaluatorFactory;
import org.deri.iris.evaluation.bottomup.compiledrules.naive.NaiveEvaluatorFactory;
import org.deri.iris.evaluation.bottomup.compiledrules.seminaive.SemiNaiveEvaluatorFactory;
import org.deri.iris.storage.IRelation;

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
		executeAndCheckResults( program, expectedResults, new NaiveEvaluatorFactory(), "Naive" );
		executeAndCheckResults( program, expectedResults, new SemiNaiveEvaluatorFactory(), "Semi-Naive" );
	}
	
	private static void executeAndCheckResults( String program, String expected, IEvaluatorFactory factory, String evaluatioName ) throws Exception
	{
		Parser parser = new Parser();
		parser.parse( program );
		List<IQuery> queries = parser.getQueries();

		assert queries.size() <= 1;
		
		IQuery query = null;
		if( queries.size() == 1 )
			query = queries.get( 0 );
		
		// Execute the program with naive

		Configuration config = KnowledgeBaseFactory.getDefaultConfiguration();
		config.evaluationTechnique = factory;
		IKnowledgeBase kb = KnowledgeBaseFactory.createKnowledgeBase( parser.getFacts(), parser.getRules(), config );

		Timer timer = new Timer();
		
		IRelation actualResults = kb.execute( query );
		
		timer.show( evaluatioName + " evaluation" );

		checkResults( expected, actualResults );
	}
	
	private static void checkResults( String expected, IRelation actualResults ) throws Exception
	{
		if ( PRINT_RESULTS )
			if( actualResults != null )
				System.out.println( resultsTostring( actualResults ) );

		if( expected == null || expected.trim().length() == 0 )
		{
			junit.framework.Assert.assertEquals( 0, actualResults == null ? 0 : actualResults.size() );
		}
		else
		{
			junit.framework.Assert.assertNotNull( actualResults );

			Parser parser = new Parser();
			parser.parse( expected );

			Map<IPredicate,IRelation> expectedFacts = parser.getFacts();
			Set<IPredicate> predicates = expectedFacts.keySet();
			
			assert predicates.size() == 1;
			IRelation expectedResults = expectedFacts.get( predicates.iterator().next() );
			junit.framework.Assert.assertTrue( same( actualResults, expectedResults ) );
		}
	}

	private static boolean same( IRelation actualResults, IRelation expectedResults )
	{
		Set<ITuple> actual = new HashSet<ITuple>();
		Set<ITuple> expected = new HashSet<ITuple>();
		
		for( int t = 0; t < actualResults.size(); ++t )
			actual.add( actualResults.get( t ) );
		
		for( int t = 0; t < expectedResults.size(); ++t )
			expected.add( expectedResults.get( t ) );
		
		return actual.equals( expected );
	}
	
	/**
	 * Evaluate the given logic program with all evaluation strategies and ensure that
	 * each fails with the expected exception. 
	 * @param program The logic program
	 * @param expectedExceptionClass The exception class object expected or null for an
	 * unknown exception type. 
	 */
	public static void checkFailureWithAllStrategies( String program, Class<?> expectedExceptionClass )
	{
		checkFailure( program, expectedExceptionClass, new NaiveEvaluatorFactory(), "Naive" );
		checkFailure( program, expectedExceptionClass, new SemiNaiveEvaluatorFactory(), "Semi-naive" );
	}
	
	/**
	 * Evaluate the given logic program with all evaluation strategies and ensure that
	 * each fails with the expected exception. 
	 * @param program The logic program
	 * @param expectedExceptionClass The exception class object expected or null for an
	 * unknown exception type. 
	 */
	public static void checkFailure( String knowledgeBase, Class<?> expectedExceptionClass, IEvaluatorFactory factory, String evaluatio )
	{
		try
		{
			// Parse the program (facts and rules)
			Parser parser = new Parser();
			parser.parse( knowledgeBase );
			
			Map<IPredicate,IRelation> facts = parser.getFacts();
			List<IRule> rules = parser.getRules();
			List<IQuery> queries = parser.getQueries();

			assert queries.size() <= 1;
			
			IQuery query = null;
			if( queries.size() == 1 )
				query = queries.get( 0 );
			
			Configuration config = KnowledgeBaseFactory.getDefaultConfiguration();
			config.evaluationTechnique = factory;
			IKnowledgeBase kb = KnowledgeBaseFactory.createKnowledgeBase( facts, rules );

			kb.execute( query, null );

			junit.framework.Assert.fail( "Naive evaluation did not throw the correct exception." );
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
	 * Format logic program evaluation results as a string.
	 * @param results The map of results.
	 * @return The human-readable results.
	 */
	public static String resultsTostring( IRelation results )
	{
		StringBuilder result = new StringBuilder();

		formatResults( result, results );

		return result.toString();
    }

	public static void formatResults( StringBuilder builder, IRelation m )
	{
		for(int t = 0; t < m.size(); ++t )
		{
			ITuple tuple = m.get( t );
			builder.append( tuple.toString() ).append( "\r\n" );
		}
    }
}
