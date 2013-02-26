package at.sti2.streamingiris.rules;

import java.util.List;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.facts.IFacts;

/**
 * This pre-processor throws an exception if the specified list of rules
 * contains a rule with rule head equality.
 * 
 * @author Adrian Marte
 */
public class DisallowRuleHeadEquality implements IRuleHeadEqualityPreProcessor {

	public List<IRule> process(List<IRule> rules, IFacts facts)
			throws EvaluationException {
		// Check if any rule with rule head equality is present and, if found,
		// throw an exception.
		for (IRule rule : rules) {
			if (RuleHeadEquality.hasRuleHeadEquality(rule)) {
				throw new EvaluationException(
						"Rules with rule head equality are not supported.");
			}
		}

		// Otherwise, return the rules as they are.
		return rules;
	}

}
