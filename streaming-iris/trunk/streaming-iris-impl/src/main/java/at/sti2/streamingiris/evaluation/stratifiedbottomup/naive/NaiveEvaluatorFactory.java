package at.sti2.streamingiris.evaluation.stratifiedbottomup.naive;

import at.sti2.streamingiris.evaluation.stratifiedbottomup.IRuleEvaluator;
import at.sti2.streamingiris.evaluation.stratifiedbottomup.IRuleEvaluatorFactory;

/**
 * Factory for naive rules evaluator.
 */
public class NaiveEvaluatorFactory implements IRuleEvaluatorFactory {
	public IRuleEvaluator createEvaluator() {
		return new NaiveEvaluator();
	}
}
