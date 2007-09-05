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
		if ( args.length < 2 )
		{
			System.out.println( "Usage: java org.deri.iris.FunctionalTest <datalog_program> [1|2|3]" );
			System.out.println( "where 1=naive, 2=semi-naive, 3=magic-sets" ); 
		}
		else
		{
			String program = args[ 0 ];
			
			int eval = Integer.parseInt( args[ 1 ] );
			
			try
			{
				Map<IPredicate, IMixedDatatypeRelation> results;
				
				long t = -System.currentTimeMillis();
				
				switch( eval )
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
	}
}
