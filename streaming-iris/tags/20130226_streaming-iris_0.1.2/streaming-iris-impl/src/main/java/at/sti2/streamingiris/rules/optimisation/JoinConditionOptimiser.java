package at.sti2.streamingiris.rules.optimisation;

import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.rules.IRuleOptimiser;
import at.sti2.streamingiris.rules.RuleManipulator;

/**
 * This optimiser attempts to use the same variable for join conditions. e.g.
 * t(?X,?Y,?Z) :- p(?X), q(?Y), r(?Z), ?X = ?Y, ?Y = ?Z. ==>> t(?X,?X,?X) :-
 * p(?X), q(?X), r(?X).
 */
public class JoinConditionOptimiser implements IRuleOptimiser {
	public IRule optimise(IRule rule) {
		rule = mManipulator.replaceVariablesWithVariables(rule);
		rule = mManipulator.removeUnnecessaryEqualityBuiltins(rule);

		return rule;
	}

	private static RuleManipulator mManipulator = new RuleManipulator();
}
