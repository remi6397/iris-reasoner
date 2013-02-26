package at.sti2.streamingiris.rules;

import at.sti2.streamingiris.api.basics.IRule;

/**
 * Represents all classes that optimise a single rule.
 */
public interface IRuleOptimiser {
	/**
	 * Optimise the rule.
	 * 
	 * @param rule
	 *            The rule to optimise.
	 * @return The optimised rule.
	 */
	IRule optimise(IRule rule);
}
