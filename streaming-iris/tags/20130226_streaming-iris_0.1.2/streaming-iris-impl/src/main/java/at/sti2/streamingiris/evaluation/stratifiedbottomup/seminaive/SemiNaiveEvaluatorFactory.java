package at.sti2.streamingiris.evaluation.stratifiedbottomup.seminaive;

import at.sti2.streamingiris.evaluation.stratifiedbottomup.IRuleEvaluator;
import at.sti2.streamingiris.evaluation.stratifiedbottomup.IRuleEvaluatorFactory;

/**
 * Factory for semi-naive rules evaluator.
 */
public class SemiNaiveEvaluatorFactory implements IRuleEvaluatorFactory {
	public IRuleEvaluator createEvaluator() {
		return new SemiNaiveEvaluator();
	}
}
