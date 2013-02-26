package at.sti2.streamingiris.evaluation.stratifiedbottomup;

import java.util.List;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.rules.compiler.ICompiledRule;

/**
 * Interface for compiled rule evaluators.
 */
public interface IRuleEvaluator {
	/**
	 * Evaluate rules.
	 * 
	 * @param rules
	 *            The collection of compiled rules.
	 * @param facts
	 *            Where to store the newly deduced tuples.
	 * @param configuration
	 *            The knowledge-base configuration object.
	 * @param timestamp
	 *            The time when the new facts become obsolete.
	 * @throws EvaluationException
	 */
	void evaluateRules(List<ICompiledRule> rules, IFacts facts,
			Configuration configuration, long timestamp)
			throws EvaluationException;
}
