/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.deri.iris.Configuration;
import org.deri.iris.KnowledgeBaseFactory;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.compiler.Parser;
import org.deri.iris.storage.IRelation;

/**
 * Helper for the demo applications.
 */
public class ProgramExecutor
{
	/** The new line separator to use when formatting output. */
	public static final String NEW_LINE = System.getProperty( "line.separator" );
	
	/** Output helper. */
	public static final String BAR = "----------------------------------";

	/** Flag for how to format the output. */
	public static final boolean SHOW_VARIABLE_BINDINGS = true;

	/** Flag for how to format the output. */
	public static final boolean SHOW_QUERY_TIME = true;

	/** Flag for how to format the output. */
	public static final boolean SHOW_ROW_COUNT = true;
	
	/**
	 * Constructor.
	 * This is where the program is actually evaluated.
	 * @param program The Datalog program to evaluate.
	 * @param configuration The configuration object.
	 */
	public ProgramExecutor( String program, Configuration configuration )
	{
		try
		{
			Parser parser = new Parser();
			parser.parse( program );
			Map<IPredicate,IRelation> facts = parser.getFacts();
			List<IRule> rules = parser.getRules();
			
			StringBuilder output = new StringBuilder();
			
			long duration = -System.currentTimeMillis();
			IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.createKnowledgeBase( facts, rules, configuration );
			duration += System.currentTimeMillis();
			
			if( SHOW_QUERY_TIME )
			{
				output.append( "Init time: " ).append( duration ).append( "ms" ).append( NEW_LINE );
			}
			
			List<IVariable> variableBindings = new ArrayList<IVariable>();

			for( IQuery query : parser.getQueries() )
			{
				// Execute the query
				duration = -System.currentTimeMillis();
				IRelation results = knowledgeBase.execute( query, variableBindings );
				duration += System.currentTimeMillis();

				output.append( BAR ).append( NEW_LINE );
				output.append( "Query:      " ).append( query );
				if( SHOW_ROW_COUNT )
				{
					output.append( " ==>> " ).append( results.size() );
					if( results.size() == 1 )
						output.append( " row" );
					else
						output.append( " rows" );
				}
				if( SHOW_QUERY_TIME )
					output.append( " in " ).append( duration ).append( "ms" );
				
				output.append( NEW_LINE );
				
				if( SHOW_VARIABLE_BINDINGS )
				{
					output.append( "Variables:  " );
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
			}
			
			mOutput = output.toString();
		}
		catch( Exception e )
		{
			mOutput = e.toString();
		}
	}
	
	/**
	 * Get the formatted program output.
	 * @return The formatted program output.
	 */
	public String getOutput()
	{
		return mOutput;
	}
	
	/**
	 * Format the actual query results (tuples).
	 * @param builder
	 * @param m
	 */
	private void formatResults( StringBuilder builder, IRelation m )
	{
		for(int t = 0; t < m.size(); ++t )
		{
			ITuple tuple = m.get( t );
			builder.append( tuple.toString() ).append( NEW_LINE );
		}
    }
	
	/** The output (or error) from the program execution. */
	private String mOutput;
}
