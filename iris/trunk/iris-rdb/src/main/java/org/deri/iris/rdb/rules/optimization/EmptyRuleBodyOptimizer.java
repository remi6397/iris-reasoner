package org.deri.iris.rdb.rules.optimization;

import java.util.Collections;
import java.util.List;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.factory.Factory;
import org.deri.iris.rdb.facts.RdbFacts;
import org.deri.iris.rules.IRuleOptimiser;

/**
 * A {@link IRuleOptimiser} that adds a literal "true" to the body of a rule,
 * that has no literals in it's body.
 */
public class EmptyRuleBodyOptimizer implements IRuleOptimiser {

	@Override
	public IRule optimise(IRule rule) {
		if (rule == null || rule.getBody().size() > 0) {
			return rule;
		}

		IPredicate truePredicate = RdbFacts.TRUE_PREDICATE;
		ITuple tuple = Factory.BASIC.createTuple();
		ILiteral literal = Factory.BASIC.createLiteral(true, truePredicate,
				tuple);

		List<ILiteral> newBody = Collections.singletonList(literal);

		return Factory.BASIC.createRule(rule.getHead(), newBody);
	}
	
}
