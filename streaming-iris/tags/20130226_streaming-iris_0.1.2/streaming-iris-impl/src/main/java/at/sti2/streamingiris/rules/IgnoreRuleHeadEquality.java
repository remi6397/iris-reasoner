package at.sti2.streamingiris.rules;

import java.util.List;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.facts.IFacts;

/**
 * This pre-processor ignores any rule with rule head equality.
 * 
 * @author Adrian Marte
 */
public class IgnoreRuleHeadEquality implements IRuleHeadEqualityPreProcessor {

	public List<IRule> process(List<IRule> rules, IFacts facts)
			throws EvaluationException {
		// Return the rules as they are.
		return rules;
	}

}
