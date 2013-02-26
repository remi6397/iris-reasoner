package at.sti2.streamingiris.evaluation.topdown.oldt;

import java.util.List;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.evaluation.IEvaluationStrategy;
import at.sti2.streamingiris.evaluation.IEvaluationStrategyFactory;
import at.sti2.streamingiris.facts.IFacts;

/**
 * Factory for OLDT evaluation strategy
 */
public class OLDTEvaluationStrategyFactory implements
		IEvaluationStrategyFactory {
	public IEvaluationStrategy createEvaluator(IFacts facts, List<IRule> rules,
			Configuration configuration) throws EvaluationException {
		return new OLDTEvaluationStrategy(facts, rules, configuration);
	}

}
