package at.sti2.streamingiris.evaluation.stratifiedbottomup;

import java.util.List;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.evaluation.IEvaluationStrategy;
import at.sti2.streamingiris.evaluation.IEvaluationStrategyFactory;
import at.sti2.streamingiris.facts.IFacts;

/**
 * Factory for StratifiedBottomUpEvaluationStrategy.
 */
public class StratifiedBottomUpEvaluationStrategyFactory implements
		IEvaluationStrategyFactory {
	public StratifiedBottomUpEvaluationStrategyFactory(
			IRuleEvaluatorFactory ruleEvaluatorFactory) {
		mRuleEvaluatorFactory = ruleEvaluatorFactory;
	}

	public IEvaluationStrategy createEvaluator(IFacts facts, List<IRule> rules,
			Configuration configuration) throws EvaluationException {
		return new StratifiedBottomUpEvaluationStrategy(facts, rules,
				mRuleEvaluatorFactory, configuration);
	}

	private final IRuleEvaluatorFactory mRuleEvaluatorFactory;
}
