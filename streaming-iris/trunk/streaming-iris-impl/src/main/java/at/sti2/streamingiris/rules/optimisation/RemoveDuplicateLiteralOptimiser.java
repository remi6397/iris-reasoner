package at.sti2.streamingiris.rules.optimisation;

import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.rules.IRuleOptimiser;
import at.sti2.streamingiris.rules.RuleManipulator;

/**
 * This optimiser removes duplicate literal, e.g. p(?X,?Y) :- q(?X), q(?X),
 * r(?Y), not s(?Y), not s(?Y), ?X = ?Y, ?X = ?Y. ==>> p(?X,?Y) :- q(?X), r(?Y),
 * not s(?Y), ?X = ?Y.
 */
public class RemoveDuplicateLiteralOptimiser implements IRuleOptimiser {
	public IRule optimise(IRule rule) {
		rule = mManipulator.removeDuplicateLiterals(rule);

		return rule;
	}

	private static RuleManipulator mManipulator = new RuleManipulator();
}
