package at.sti2.streamingiris.evaluation;

import java.util.List;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.facts.IFacts;

/**
 * Interface of all evaluation strategy factories.
 */
public interface IEvaluationStrategyFactory {
	/**
	 * Create a new evaluation strategy.
	 * 
	 * @param facts
	 *            The facts to be used for evaluation.
	 * @param rules
	 *            The rule-set to be used for evaluation.
	 * @return The new evaluator instance.
	 */
	IEvaluationStrategy createEvaluator(IFacts facts, List<IRule> rules,
			Configuration configuration) throws EvaluationException;
}
