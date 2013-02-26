package at.sti2.streamingiris.rules.optimisation;

import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.rules.IRuleOptimiser;
import at.sti2.streamingiris.rules.RuleManipulator;

/**
 * Replace variables with constants where possible: e.g. p(?X,?Y) :- q(?X, ?Z),
 * ?Z = 't' ==>> p(?X,?Y) :- q(?X, 't')
 * 
 * This should have the effect of pushing selection criteria in to the
 * evaluation of a relation, such that fewer tuples are processed.
 * 
 * Problem! p(?X), ?X = 2 is not the same as p(2), at least at the moment,
 * because with p(2) the evaluator looks for only p( integer( 2 ) ) in the
 * relation, whereas p(?X), ?X = 2 will return the entire relation and filter
 * out what equals 2, i.e. any numeric value.
 * 
 * Not sure how to fix this, but it will involve changing MixedDatatypeRelation.
 */
public class ReplaceVariablesWithConstantsOptimiser implements IRuleOptimiser {
	public IRule optimise(IRule rule) {
		rule = mManipulator.replaceVariablesWithConstants(rule, true);
		rule = mManipulator.removeDuplicateLiterals(rule);
		rule = mManipulator.removeUnnecessaryEqualityBuiltins(rule);

		return rule;
	}

	private static RuleManipulator mManipulator = new RuleManipulator();
}
