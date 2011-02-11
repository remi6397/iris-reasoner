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
package org.deri.iris.evaluation.equivalence;

import java.util.List;

import org.deri.iris.Configuration;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.evaluation.EvaluationTest;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rules.IgnoreRuleHeadEquality;
import org.deri.iris.rules.RuleHeadEqualityRewriter;
import org.deri.iris.rules.safety.AugmentingRuleSafetyProcessor;
import org.deri.iris.utils.equivalence.IgnoreTermEquivalenceFactory;
import org.deri.iris.utils.equivalence.TermEquivalenceFactory;

/**
 * Test that compares the evaluation of a set of rules containing rules with
 * head equality using different evaluation techniques.
 * 
 * @author Adrian Marte
 */
public class EquivalenceStressTest extends EvaluationTest {

	private RandomProgramBuilder builder;
	
	private Program program;
	
	public EquivalenceStressTest(String name) {
		super(name);

		builder = new RandomProgramBuilder();
	}

	@Override
	protected void setUp() throws Exception {
		program = builder.build();
		
		super.setUp();
	}
	
	@Override
	protected IFacts createFacts() {
		return program.getFacts();
	}
	
	@Override
	protected List<IQuery> createQueries() {
		return program.getQueries();
	}
	
	@Override
	protected List<IRule> createRules() {
		return program.getRules();
	}
	
	public void testQuery1() throws Exception {
		executeQuery(queries.get(0));
	}

	private void executeQuery(IQuery query) throws Exception {
		Configuration config = new Configuration();

		// Use rewriting technique.
		config.equivalentTermsFactory = new IgnoreTermEquivalenceFactory();
		config.ruleHeadEqualityPreProcessor = new RuleHeadEqualityRewriter();
		config.ruleSafetyProcessor = new AugmentingRuleSafetyProcessor();

		evaluate(query, config);
		System.out.println("Rewriter: " + getDuration());

		config = new Configuration();

		// Use integrated rule head equality support.
		config.equivalentTermsFactory = new TermEquivalenceFactory();
		config.ruleHeadEqualityPreProcessor = new IgnoreRuleHeadEquality();

		evaluate(query, config);
		System.out.println("Integrated: " + getDuration());
	}


}
