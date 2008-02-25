/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Digital Enterprise Research Institute (DERI), 
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

import org.deri.iris.evaluation.naive.NaiveEvaluatorFactory;
import org.deri.iris.evaluation.wellfounded.WellFoundedEvaluationStrategyFactory;
import org.deri.iris.optimisations.MagicSetImpl;

/**
 * A command line demonstrator for IRIS.
 */
public class Demo
{
	/**
	 * Entry point.
	 * @param args program evaluation_method
	 */
	public static void main( String[] args )
	{
		if ( args.length < 1 )
		{
			System.out.println( "Usage: java org.deri.iris.Demo <datalog_program> [1|2] max_evaluation_time(ms) [s|w] [m]" );
			System.out.println( "where 1=naive, 2=semi-naive*" ); 
			System.out.println( "      s=stratified*, w=well-founded semantics" ); 
			System.out.println( "      m=magic sets" ); 
			System.out.println( "*=default" ); 
		}
		else
		{
			// Arg 0 - program
			String program = args[ 0 ];
			
			Configuration configuration = KnowledgeBaseFactory.getDefaultConfiguration();
			
			// Arg 1 - rule evaluator
			if( args.length >= 2 )
			{
				int eval = Integer.parseInt( args[ 1 ] );
				if( eval == 1 )
					configuration.ruleEvaluatorFactory = new NaiveEvaluatorFactory();
			}

			// Arg 2 - time out
			int maxTime = 30000;
			if( args.length >= 3 )
			{
				maxTime = Integer.parseInt( args[ 2 ] );
			
				if ( maxTime < 1 )
					maxTime = 10000;
			}
			configuration.evaluationTimeoutMilliseconds = maxTime;
			
			// Arg 3 - strategy
			if( args.length >= 4 )
			{
				if( args[ 3 ].compareToIgnoreCase( "w" ) == 0 )
					configuration.evaluationStrategyFactory = new WellFoundedEvaluationStrategyFactory();
			}
			
			// Arg 4 - strategy
			if( args.length >= 5 )
			{
				if( args[ 4 ].compareToIgnoreCase( "m" ) == 0 )
					configuration.programOptmimisers.add( new MagicSetImpl() );
			}

			execute( program, configuration );
		}
	}
	
	public static void execute( String program, Configuration configuration )
	{
		Thread t = new Thread( new ExecutionTask( program, configuration ), "Evaluation task" );

		t.setPriority( Thread.MIN_PRIORITY );
		t.start();
		
		try
		{
			t.join( configuration.evaluationTimeoutMilliseconds );
		}
		catch( InterruptedException e )
		{
		}
		
		if ( t.isAlive() )
		{
			t.stop();
			System.out.println( "Timeout exceeded: " + configuration.evaluationTimeoutMilliseconds + "ms" );
		}
	}
	
	static class ExecutionTask implements Runnable
	{
		ExecutionTask( String program, Configuration configuration )
		{
			this.program = program;
			this.configuration = configuration;
		}
		
//		@Override
        public void run()
        {
        	ProgramExecutor executor = new ProgramExecutor( program, configuration );
			System.out.println( executor.getOutput() );
        }
		
		private String program;
		private Configuration configuration;
	}
}
