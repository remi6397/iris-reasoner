package org.deri.iris;

import java.util.Map;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.storage.IMixedDatatypeRelation;

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
		if ( args.length < 3 )
		{
			System.out.println( "Usage: java org.deri.iris.FunctionalTest <datalog_program> [1|2|3] max_evaluation_time(ms)" );
			System.out.println( "where 1=naive, 2=semi-naive, 3=magic-sets" ); 
		}
		else
		{
			String program = args[ 0 ];
			
			int eval = Integer.parseInt( args[ 1 ] );
			int maxTime = Integer.parseInt( args[ 2 ] );
			
			if ( maxTime < 1 )
				maxTime = 1000;
			
			execute( program, eval, maxTime );
		}
	}
	
	private static void execute( String program, int evaluationStrategy, int maxTime )
	{
		Thread t = new Thread( new ExecutionTask( program, evaluationStrategy ), "Evaluation task" );

		t.setPriority( Thread.MIN_PRIORITY );
		t.start();
		
		try
		{
			Thread.sleep( maxTime );
		}
		catch( InterruptedException e )
		{
		}
		
		if ( t.isAlive() )
		{
			t.stop();
			System.out.println( "Timeout exceeded: " + maxTime + "ms" );
		}
	}
	
	static class ExecutionTask implements Runnable
	{
		ExecutionTask( String program, int evaluationStrategy )
		{
			this.program = program;
			this.evaluationStrategy = evaluationStrategy;
		}
		
//		@Override
        public void run()
        {
			try
			{
				Map<IPredicate, IMixedDatatypeRelation> results;
				
				long t = -System.currentTimeMillis();
				
				switch( evaluationStrategy )
				{
				case 1:
					System.out.println( "Naive evaluation" );
					results = ExecutionHelper.evaluateNaive( program );
					break;
				case 2:
				default:
					System.out.println( "Semi-naive evaluation" );
					results = ExecutionHelper.evaluateSeminaive( program );
					break;
				case 3:
					System.out.println( "Semi-naive evaluation with magic sets" );
					results = ExecutionHelper.evaluateSeminaiveWithMagicSets( program );
					break;
				}

				t += System.currentTimeMillis();
				System.out.println( "Time: " + t + "ms" );
		
				System.out.println( ExecutionHelper.resultsTostring( results ) );
			}
			catch( Exception e )
			{
				System.out.println( "Evaluation failed with exception: " + e.toString() );
			}
        }
		
		private String program;
		private int evaluationStrategy;
	}
}
