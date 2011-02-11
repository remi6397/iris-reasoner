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
package org.deri.iris.evaluation.stratifiedbottomup;

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.IEvaluationStrategy;
import org.deri.iris.evaluation.stratifiedbottomup.naive.NaiveEvaluator;
import org.deri.iris.facts.FiniteUniverseFacts;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rules.RuleHeadEquality;
import org.deri.iris.rules.compiler.ICompiledRule;
import org.deri.iris.rules.compiler.RuleCompiler;
import org.deri.iris.rules.safety.AugmentingRuleSafetyProcessor;
import org.deri.iris.storage.IRelation;
import org.deri.iris.utils.equivalence.IEquivalentTerms;

/**
 * A strategy that uses bottom up evaluation on a stratified rule set.
 */
public class StratifiedBottomUpEvaluationStrategy implements
		IEvaluationStrategy {
	StratifiedBottomUpEvaluationStrategy(IFacts facts, List<IRule> rules,
			IRuleEvaluatorFactory ruleEvaluatorFactory,
			Configuration configuration) throws EvaluationException {
		mConfiguration = configuration;
		mRuleEvaluatorFactory = ruleEvaluatorFactory;
		mFacts = facts;
		mEquivalentTerms = mConfiguration.equivalentTermsFactory
				.createEquivalentTerms();

		List<IRule> allRules = mConfiguration.ruleHeadEqualityPreProcessor
				.process(rules, facts);

		if (mConfiguration.ruleSafetyProcessor instanceof AugmentingRuleSafetyProcessor)
			facts = new FiniteUniverseFacts(facts, allRules);

		EvaluationUtilities utils = new EvaluationUtilities(mConfiguration);

		// Rule safety processing
		List<IRule> safeRules = utils.applyRuleSafetyProcessor(allRules);

		// Stratify
		List<List<IRule>> stratifiedRules = utils.stratify(safeRules);

		RuleCompiler rc = new RuleCompiler(facts, mEquivalentTerms,
				mConfiguration);

		int stratumNumber = 0;
		for (List<IRule> stratum : stratifiedRules) {
			// Re-order stratum
			List<IRule> reorderedRules = utils.reOrderRules(stratum);

			// Rule optimisation
			List<IRule> optimisedRules = utils
					.applyRuleOptimisers(reorderedRules);

			List<ICompiledRule> compiledRules = new ArrayList<ICompiledRule>();

			for (IRule rule : optimisedRules) {
				compiledRules.add(rc.compile(rule));
			}

			// TODO Enable rule head equality support for semi-naive evaluation.
			// Choose the correct evaluation technique for the specified rules and stratum.
			IRuleEvaluator evaluator = chooseEvaluator(stratumNumber,
					optimisedRules, mRuleEvaluatorFactory);

			evaluator.evaluateRules(compiledRules, facts, configuration);

			stratumNumber++;
		}
	}

	private IRuleEvaluator chooseEvaluator(int stratum, List<IRule> rules,
			IRuleEvaluatorFactory factory) {
		if (stratum == 0) {
			for (IRule rule : rules) {
				if (RuleHeadEquality.hasRuleHeadEquality(rule)) {
					return new NaiveEvaluator();
				}
			}
		}

		// Create default evaluator.
		return factory.createEvaluator();
	}

	public IRelation evaluateQuery(IQuery query, List<IVariable> outputVariables)
			throws EvaluationException {
		if (query == null)
			throw new IllegalArgumentException(
					"StratifiedBottomUpEvaluationStrategy.evaluateQuery() - query must not be null.");

		if (outputVariables == null)
			throw new IllegalArgumentException(
					"StratifiedBottomUpEvaluationStrategy.evaluateQuery() - outputVariables must not be null.");

		RuleCompiler compiler = new RuleCompiler(mFacts, mEquivalentTerms,
				mConfiguration);

		ICompiledRule compiledQuery = compiler.compile(query);

		IRelation result = compiledQuery.evaluate();

		outputVariables.clear();
		outputVariables.addAll(compiledQuery.getVariablesBindings());

		return result;
	}

	protected IEquivalentTerms mEquivalentTerms;

	protected final Configuration mConfiguration;

	protected final IFacts mFacts;

	protected final IRuleEvaluatorFactory mRuleEvaluatorFactory;
}
