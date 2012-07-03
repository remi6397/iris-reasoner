/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package at.sti2.streamingiris.evaluation.stratifiedbottomup;

import java.util.ArrayList;
import java.util.List;


import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.evaluation.IEvaluationStrategy;
import at.sti2.streamingiris.evaluation.stratifiedbottomup.naive.NaiveEvaluator;
import at.sti2.streamingiris.facts.FiniteUniverseFacts;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.rules.RuleHeadEquality;
import at.sti2.streamingiris.rules.compiler.ICompiledRule;
import at.sti2.streamingiris.rules.compiler.RuleCompiler;
import at.sti2.streamingiris.rules.safety.AugmentingRuleSafetyProcessor;
import at.sti2.streamingiris.storage.IRelation;
import at.sti2.streamingiris.utils.equivalence.IEquivalentTerms;

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

		utils = new EvaluationUtilities(mConfiguration);

		// Rule safety processing
		List<IRule> safeRules = utils.applyRuleSafetyProcessor(allRules);

		stratifiedRules = utils.stratify(safeRules);

		rc = new RuleCompiler(facts, mEquivalentTerms, mConfiguration);

		int stratumNumber = 0;
		for (List<IRule> stratum : stratifiedRules) {
			// Re-order stratum
			List<IRule> reorderedRules = utils.reOrderRules(stratum);

			// Rule optimization
			List<IRule> optimisedRules = utils
					.applyRuleOptimisers(reorderedRules);

			List<ICompiledRule> compiledRules = new ArrayList<ICompiledRule>();

			for (IRule rule : optimisedRules) {
				compiledRules.add(rc.compile(rule));
			}

			// TODO Enable rule head equality support for semi-naive evaluation.
			// Choose the correct evaluation technique for the specified rules
			// and stratum.
			IRuleEvaluator evaluator = chooseEvaluator(stratumNumber,
					optimisedRules, mRuleEvaluatorFactory);

			evaluator.evaluateRules(compiledRules, facts, configuration, 0);

			stratumNumber++;
		}
	}

	public void evaluateRules(IFacts facts, long timestamp)
			throws EvaluationException {
		int stratumNumber = 0;
		for (List<IRule> stratum : stratifiedRules) {
			// Re-order stratum
			List<IRule> reorderedRules = utils.reOrderRules(stratum);

			// Rule optimization
			List<IRule> optimisedRules = utils
					.applyRuleOptimisers(reorderedRules);

			List<ICompiledRule> compiledRules = new ArrayList<ICompiledRule>();

			for (IRule rule : optimisedRules) {
				compiledRules.add(rc.compile(rule));
			}

			// TODO Enable rule head equality support for semi-naive evaluation.
			// Choose the correct evaluation technique for the specified rules
			// and stratum.
			IRuleEvaluator evaluator = chooseEvaluator(stratumNumber,
					optimisedRules, mRuleEvaluatorFactory);

			evaluator.evaluateRules(compiledRules, facts, mConfiguration,
					timestamp);

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

	private RuleCompiler rc;

	private List<List<IRule>> stratifiedRules;

	private EvaluationUtilities utils;
}
