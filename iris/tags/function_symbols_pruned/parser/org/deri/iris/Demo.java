package org.deri.iris;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.builtins.BuiltinRegister;
import org.deri.iris.compiler.Parser;
import org.deri.iris.new_stuff.Configuration;
import org.deri.iris.new_stuff.KnowledgeBaseFactory;
import org.deri.iris.new_stuff.evaluation.bottomup.compiledrules.naive.NaiveEvaluatorFactory;
import org.deri.iris.new_stuff.evaluation.bottomup.compiledrules.seminaive.SemiNaiveEvaluatorFactory;
import org.deri.iris.new_stuff.storage.IRelation;

/**
 * A command line demonstrator for IRIS.
 */
public class Demo
{
	public static String NEW_LINE = "\r\n";
	public static final boolean SHOW_VARIABLE_BINDINGS = true;
	public static final boolean SHOW_QUERY_TIME = true;
	public static final boolean SHOW_ROW_COUNT = true;

	/**
	 * Entry point.
	 * @param args program evaluation_method
	 */
	public static void main( String[] args )
	{
		if ( args.length < 3 )
		{
			System.out.println( "Usage: java org.deri.iris.Demo <datalog_program> [1|2|3] max_evaluation_time(ms)" );
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
			t.join( maxTime );
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
//				Map<IPredicate, IMixedDatatypeRelation> results;
//				
//				long t = -System.currentTimeMillis();
//				
//				IProgram p;
//				
//				switch( evaluationStrategy )
//				{
//				case 1:
//					System.out.println( "Naive evaluation" );
//					p = ExecutionHelper.parseProgram( program );
//					results = ExecutionHelper.evaluateNaive( p );
//					break;
//				case 2:
//				default:
//					System.out.println( "Semi-naive evaluation" );
//					p = ExecutionHelper.parseProgram( program );
//					results = ExecutionHelper.evaluateSeminaive( p );
//					break;
//				case 3:
//					System.out.println( "Semi-naive evaluation with magic sets" );
//					p = ExecutionHelper.parseProgram( program );
//					results = ExecutionHelper.evaluateSeminaiveWithMagicSets( p );
//					break;
//				}
//
//				t += System.currentTimeMillis();
//				System.out.println( "Time: " + t + "ms" );

				Parser parser = new Parser( new BuiltinRegister() );
				parser.parse( program );
				Map<IPredicate,IRelation> facts = parser.getFacts();
				List<IRule> rules = parser.getRules();
				List<IQuery> queries = parser.getQueries();
				
				if( queries.size() > 1 )
				{
					System.out.println( "Only one query at a time" );
					return;
				}
				IQuery query = queries.size() == 1 ? queries.iterator().next() : null;
				
				StringBuilder output = new StringBuilder();
				
				Configuration config = KnowledgeBaseFactory.getDefaultConfiguration();
				
				switch( evaluationStrategy )
				{
				case 0:
					output.append( "Naive evaluation" ).append( NEW_LINE );
					config.evaluationTechnique = new NaiveEvaluatorFactory();
					break;
				
				default:
				case 1:
					output.append( "Semi-naive evaluation" ).append( NEW_LINE );
					config.evaluationTechnique = new SemiNaiveEvaluatorFactory();
					break;
				
				}

				IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.createKnowledgeBase( facts, rules, config );
				
				List<IVariable> variableBindings = new ArrayList<IVariable>();

				// Execute the query
				long queryDuration = -System.currentTimeMillis();
				IRelation results = knowledgeBase.execute( query, variableBindings );
				queryDuration += System.currentTimeMillis();

				if( SHOW_VARIABLE_BINDINGS )
				{
					boolean first = true;
					for( IVariable variable : variableBindings )
					{
						if( first )
							first = false;
						else
							output.append( ", " );
						output.append( variable );
					}
					output.append( NEW_LINE );
				}
				
				formatResults( output, results );

				if( SHOW_ROW_COUNT || SHOW_QUERY_TIME )
					output.append( "-----------------" ).append( NEW_LINE );
				if( SHOW_ROW_COUNT )
					output.append( "Rows: " ).append( results.size() ).append( NEW_LINE );
				if( SHOW_QUERY_TIME )
					output.append( "Time: " ).append( queryDuration ).append( "ms" ).append( NEW_LINE );

				System.out.println( output.toString() );
			}
			catch( Exception e )
			{
				System.out.println( "Evaluation failed with exception: " + e.toString() );
			}
        }
		
		private String program;
		private int evaluationStrategy;
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
