package at.sti2.streamingiris.evaluation.stratifiedbottomup;

/**
 * Interface of all evaluator factories.
 */
public interface IRuleEvaluatorFactory {
	/**
	 * Create a new evaluator.
	 */
	IRuleEvaluator createEvaluator();
}
