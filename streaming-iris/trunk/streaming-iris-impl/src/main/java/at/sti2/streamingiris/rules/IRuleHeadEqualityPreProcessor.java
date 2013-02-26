package at.sti2.streamingiris.rules;

import java.util.List;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.facts.IFacts;

/**
 * An interface for rule head equality pre-processors.
 * 
 * @author Adrian Marte
 */
public interface IRuleHeadEqualityPreProcessor {

	/**
	 * Pre-process the given rules and facts.
	 * 
	 * @param rules
	 *            The rule to pre-process.
	 * @param facts
	 *            The facts to pre-process.
	 * @throws EvaluationException
	 *             If an error occurs, or rule head equality is not supported.
	 * @return The resulting rules after pre-processing.
	 */
	public List<IRule> process(List<IRule> rules, IFacts facts)
			throws EvaluationException;

}
