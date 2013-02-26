package at.sti2.streamingiris.rules;

import at.sti2.streamingiris.RuleUnsafeException;
import at.sti2.streamingiris.api.basics.IRule;

/**
 * Represents all classes that perform rule-safety processing.
 */
public interface IRuleSafetyProcessor {
	/**
	 * Process the rule.
	 * 
	 * @param rule
	 *            The rule to process.
	 * @return The processed rule.
	 */
	IRule process(IRule rule) throws RuleUnsafeException;
}
