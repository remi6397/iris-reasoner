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
package org.deri.iris.evaluation.topdown.oldt;

import java.util.List;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.ProgramNotStratifiedException;
import org.deri.iris.RuleUnsafeException;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.IEvaluationStrategy;
import org.deri.iris.evaluation.topdown.ITopDownEvaluator;
import org.deri.iris.facts.IFacts;
import org.deri.iris.storage.IRelation;

public class OLDTEvaluationStrategy implements IEvaluationStrategy {

	/**
	 * Constructor
	 * @param facts Given facts.
	 * @param rules Given rules.
	 * @param configuration Configuration
	 * @throws EvaluationException
	 */
	public OLDTEvaluationStrategy(IFacts facts, List<IRule> rules, Configuration configuration ) throws EvaluationException {
		mFacts = facts;
		mRules = rules;
		mConfiguration = configuration;
	}
	
	/**
	 * Evaluate the query
	 */
	public IRelation evaluateQuery(IQuery query, List<IVariable> outputVariables) throws ProgramNotStratifiedException, RuleUnsafeException, EvaluationException {
		if( query == null )
			throw new IllegalArgumentException( "SLDEvaluationStrategy.evaluateQuery() - query must not be null." ); 
		
		
		ITopDownEvaluator evaluator = new OLDTEvaluator( mFacts, mRules );
		IRelation relation = evaluator.evaluate( query );
		outputVariables = evaluator.getOutputVariables();		
		
		return relation;
	}
	
	protected final IFacts mFacts;
	protected final List<IRule> mRules;
	protected final Configuration mConfiguration;

}
