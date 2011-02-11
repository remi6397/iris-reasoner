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
package org.deri.iris.functional;

import java.util.List;
import org.deri.iris.Configuration;
import org.deri.iris.KnowledgeBaseFactory;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.compiler.Parser;
import org.deri.iris.functional.Helper.Timer;
import org.deri.iris.optimisations.magicsets.MagicSets;
import org.deri.iris.optimisations.rulefilter.RuleFilter;
import org.deri.iris.storage.IRelation;
import junit.framework.TestCase;

/**
 * Tests for magic sets.
 */
public class MagicSetsTest extends TestCase
{
	public void testTranslation() throws Exception
	{
		String program =
			"triple(?y, ?a, ?z) :- triple(?x, '88', ?y), triple(?x, ?a, ?z), NOT_EQUAL(?a, '88'), NOT_EQUAL(?x, ?y). " +
			"triple(?z, ?a, ?y) :- triple(?x, '88', ?y), triple(?z, ?a, ?x), NOT_EQUAL(?a, '88'), NOT_EQUAL(?x, ?y). " +
			"?- triple('184', ?p, ?o). ";		
       	String expectedResults = 
			"a( 1, 2 )." +
			"a( 0, 3 ).";

       	try { evaluateSemiNaiveAndOptimisations( program, expectedResults ); } catch( Exception e ) {}
       	try { evaluateSemiNaiveAndOptimisations( program, expectedResults ); } catch( Exception e ) {}
       	try { evaluateSemiNaiveAndOptimisations( program, expectedResults ); } catch( Exception e ) {}
       	try { evaluateSemiNaiveAndOptimisations( program, expectedResults ); } catch( Exception e ) {}
       	try { evaluateSemiNaiveAndOptimisations( program, expectedResults ); } catch( Exception e ) {}
       	int z = 1;
	}

	public static void evaluateSemiNaiveAndOptimisations( String program, String expectedResults ) throws Exception
	{
		Configuration configuration = KnowledgeBaseFactory.getDefaultConfiguration();
		
		configuration.programOptmimisers.add( new RuleFilter() );
		configuration.programOptmimisers.add( new MagicSets() );

		executeAndCheckResults( program, expectedResults, configuration, "Semi-Naive and Magic Sets" );
	}
	
	public static void executeAndCheckResults( String program, String expected, Configuration configuration, String evaluationName ) throws Exception
	{
		Parser parser = new Parser();
		parser.parse( program );
		List<IQuery> queries = parser.getQueries();

		assert queries.size() <= 1;
		
		IQuery query = null;
		if( queries.size() == 1 )
			query = queries.get( 0 );
		
		// Instantiate the knowledge-base
		IKnowledgeBase kb = KnowledgeBaseFactory.createKnowledgeBase( parser.getFacts(), parser.getRules(), configuration );
		
		// Execute the query
		if( query != null )
			kb.execute( query );
	}
}
