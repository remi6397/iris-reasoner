package org.deri.iris;

import java.util.Map;
import org.deri.iris.api.IExecutor;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserException;
import org.deri.iris.evaluation.algebra.ExpressionEvaluator;
import org.deri.iris.factory.Factory;

/**
 * Some temporary execution helper methods for evaluating logic programs.
 */
public class ExecutionHelper
{
	/**
	 * Create a program object by parsing the datalog logic program.
	 * @param program The logic program.
	 * @return A concrete program object.
	 * @throws ParserException 
	 */
	public static IProgram parseProgram( String program ) throws ParserException
	{
		IProgram p = Factory.PROGRAM.createProgram();
		p.resetProgram();
		Parser.parse( program, p );

		return p;
	}

	/**
	 * Evaluate with naive evaluation method.
	 * @param program The datalog program.
	 * @return Results of queries
	 * @throws Exception If evaluation fails
	 */
	public static Map<IPredicate, IMixedDatatypeRelation> evaluateNaive( String program ) throws Exception
	{
		IProgram p = parseProgram( program );
		
		IExpressionEvaluator method = new ExpressionEvaluator();

		IExecutor exec = new NaiveExecutor( p, method );

		exec.execute();
		
		return exec.computeSubstitutions();
	}
	
	/**
	 * Evaluate with semi-naive evaluation method.
	 * @param program The datalog program.
	 * @return Results of queries
	 * @throws Exception If evaluation fails
	 */
	public static Map<IPredicate, IMixedDatatypeRelation> evaluateSeminaive( String program ) throws Exception
	{
		IProgram p = parseProgram( program );
		
		IExpressionEvaluator method = new ExpressionEvaluator();

		IExecutor exec = new Executor( p, method );

		exec.execute();
		
		return exec.computeSubstitutions();
	}

	/**
	 * Evaluate with semi-naive evaluation with magic sets method.
	 * @param program The datalog program.
	 * @return Results of queries
	 * @throws Exception If evaluation fails
	 */
	public static Map<IPredicate, IMixedDatatypeRelation> evaluateSeminaiveWithMagicSets( String program ) throws Exception
	{
		IProgram p = parseProgram( program );
		
		IExpressionEvaluator method = new ExpressionEvaluator();

		IExecutor exec = new MagicExecutor( p, method );

		exec.execute();
		
		return exec.computeSubstitutions();
	}

	/**
	 * Format logic program evaluation results as a string.
	 * @param m The map of reults.
	 * @return The human-readable results.
	 */
	public static String resultsTostring( Map<IPredicate, IMixedDatatypeRelation> m )
	{
		StringBuilder result = new StringBuilder();
		
		for( IPredicate pr : m.keySet() )
		{
			result.append( pr.getPredicateSymbol() ).append( "[" ).append( pr.getArity() ).append( "]" ).append( "\r\n" );
			
			for( ITuple t : m.get( pr ) )
			{
				result.append( t.toString() ).append( "\r\n" );
			}
    	}

		return result.toString();
    }
}
