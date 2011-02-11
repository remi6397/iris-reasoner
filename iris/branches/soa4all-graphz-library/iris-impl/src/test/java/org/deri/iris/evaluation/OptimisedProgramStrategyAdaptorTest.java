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
package org.deri.iris.evaluation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.ProgramNotStratifiedException;
import org.deri.iris.RuleUnsafeException;
import org.deri.iris.api.IProgramOptimisation;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.facts.Facts;
import org.deri.iris.facts.IFacts;
import org.deri.iris.facts.OriginalFactsPreservingFacts;
import org.deri.iris.storage.IRelation;
import org.deri.iris.storage.simple.SimpleRelationFactory;

public class OptimisedProgramStrategyAdaptorTest extends TestCase
{
	private IRule createRule() {
		return Factory.BASIC.createRule( new ArrayList<ILiteral>(), new ArrayList<ILiteral>() );
	}
	
	private List<IRule> createRules() {
		List<IRule> result = new ArrayList<IRule>();
		
		for( int r = 0; r < 4; ++r )
			result.add( createRule() );
		
		return result;
	}
	
	private IQuery createQuery() {
		return Factory.BASIC.createQuery();
	}
	
	private IFacts createFacts() {
		return new Facts( new SimpleRelationFactory() );
	}
	
	private IRelation createRelation() {
		return new SimpleRelationFactory().createRelation();
	}
	
	class Strategy implements IEvaluationStrategy {

		public Strategy( IFacts facts, List<IRule> rules ) {
			mGivenFacts = facts;
			mGivenRules = rules;
		}
		
		public IRelation evaluateQuery( IQuery query, List<IVariable> outputVariables )
                        throws ProgramNotStratifiedException, RuleUnsafeException, EvaluationException
        {
			mGivenQuery = query;
	        return createRelation();
        }
		
		IFacts mGivenFacts;
		List<IRule> mGivenRules;
		IQuery mGivenQuery;
	}
	
	class StrategyFactory implements IEvaluationStrategyFactory {

		public IEvaluationStrategy createEvaluator( IFacts facts, List<IRule> rules, Configuration configuration ) throws EvaluationException
        {
			mStrategy = new Strategy( facts, rules );
			return mStrategy;
        }
		
		Strategy mStrategy;
	}
	
	public void testNoOptimisers() throws Exception{
		Configuration configuration = new Configuration();
		
		StrategyFactory factory = new StrategyFactory();
		
		configuration.programOptmimisers.clear();
		configuration.evaluationStrategyFactory = factory;

		IFacts facts = createFacts();
		List<IRule> rules = createRules();
		IQuery query = createQuery();
		
		OptimisedProgramStrategyAdaptor adaptor = new OptimisedProgramStrategyAdaptor( facts, rules, configuration );
		
		adaptor.evaluateQuery( query, null );
		
		assertTrue( facts == factory.mStrategy.mGivenFacts );	// Same object
		assertTrue( rules == factory.mStrategy.mGivenRules );	// Same object
		assertTrue( query == factory.mStrategy.mGivenQuery );	// Same object
	}

	class NotOptimiser implements IProgramOptimisation {

		public Result optimise( Collection<IRule> rules, IQuery query ) {
	        return null;
        }
	}

	public void testNoOptimisationsPossible() throws Exception {
		
		Configuration configuration = new Configuration();
		
		StrategyFactory factory = new StrategyFactory();
		
		configuration.evaluationStrategyFactory = factory;
		
		configuration.programOptmimisers.clear();
		configuration.programOptmimisers.add( new NotOptimiser() );
		configuration.programOptmimisers.add( new NotOptimiser() );

		IFacts facts = createFacts();
		List<IRule> rules = createRules();
		IQuery query = createQuery();
		
		OptimisedProgramStrategyAdaptor adaptor = new OptimisedProgramStrategyAdaptor( facts, rules, configuration );
		
		adaptor.evaluateQuery( query, null );
		
		assertTrue( facts == factory.mStrategy.mGivenFacts );	// Same object
		assertTrue( rules == factory.mStrategy.mGivenRules );	// Same object
		assertTrue( query == factory.mStrategy.mGivenQuery );	// Same object
	}
	
	class Optimiser1 implements IProgramOptimisation {

		Optimiser1() {
			rulesOutput.add( createRule() );
			queryOutput = createQuery();
		}
		
		public Result optimise( Collection<IRule> rulesInput, IQuery queryInput ) {
			this.rulesInput = rulesInput;
			this.queryInput = queryInput;
	        return new Result( rulesOutput, queryOutput );
        }

		Collection<IRule> rulesInput;
		IQuery queryInput;

		final List<IRule> rulesOutput = new ArrayList<IRule>();
		final IQuery queryOutput;
	}

	public void testOptimised1() throws Exception {
		
		Configuration configuration = new Configuration();
		
		StrategyFactory factory = new StrategyFactory();
		
		configuration.evaluationStrategyFactory = factory;

		configuration.programOptmimisers.clear();
		Optimiser1 optimiser = new Optimiser1();
		configuration.programOptmimisers.add( optimiser );

		IFacts facts = createFacts();
		List<IRule> rules = createRules();
		IQuery query = createQuery();

		OptimisedProgramStrategyAdaptor adaptor = new OptimisedProgramStrategyAdaptor( facts, rules, configuration );
		
		adaptor.evaluateQuery( query, null );

		assertTrue( factory.mStrategy.mGivenFacts instanceof OriginalFactsPreservingFacts );
		
		assertTrue( optimiser.rulesOutput == factory.mStrategy.mGivenRules );	// Same object
		assertTrue( optimiser.queryOutput == factory.mStrategy.mGivenQuery );	// Same object
		
		assertTrue( optimiser.rulesInput == rules );
		assertTrue( optimiser.queryInput == query );
	}

	public void testOptimisedN() throws Exception {
		
		Configuration configuration = new Configuration();
		
		StrategyFactory factory = new StrategyFactory();
		
		configuration.evaluationStrategyFactory = factory;

		configuration.programOptmimisers.clear();

		final int N = 9;
		for( int i = 0; i < N; ++i )
			configuration.programOptmimisers.add( new Optimiser1() );

		IFacts facts = createFacts();
		List<IRule> rules = createRules();
		IQuery query = createQuery();

		OptimisedProgramStrategyAdaptor adaptor = new OptimisedProgramStrategyAdaptor( facts, rules, configuration );
		
		adaptor.evaluateQuery( query, null );

		Optimiser1 o1 = (Optimiser1) configuration.programOptmimisers.get( 0 );
		
		assertTrue( o1.rulesInput == rules );	// Same object
		assertTrue( o1.queryInput == query );	// Same object
		
		for( int n = 1; n < N; ++n ) {
			Optimiser1 oN_1 = (Optimiser1) configuration.programOptmimisers.get( n - 1);
			Optimiser1 oN   = (Optimiser1) configuration.programOptmimisers.get( n );
			assertTrue( oN.rulesInput == oN_1.rulesOutput );
			assertTrue( oN.queryInput == oN_1.queryOutput );
		}
		
		Optimiser1 oN = (Optimiser1) configuration.programOptmimisers.get( N - 1 );

		assertTrue( oN.rulesOutput == factory.mStrategy.mGivenRules );	// Same object
		assertTrue( oN.queryOutput == factory.mStrategy.mGivenQuery );	// Same object
	}
}
