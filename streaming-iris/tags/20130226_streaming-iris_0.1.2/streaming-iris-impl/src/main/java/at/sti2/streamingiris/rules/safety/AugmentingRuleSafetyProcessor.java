package at.sti2.streamingiris.rules.safety;

import java.util.ArrayList;
import java.util.List;

import at.sti2.streamingiris.RuleUnsafeException;
import at.sti2.streamingiris.api.basics.ILiteral;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.factory.Factory;
import at.sti2.streamingiris.facts.FiniteUniverseFacts;
import at.sti2.streamingiris.rules.IRuleSafetyProcessor;
import at.sti2.streamingiris.rules.RuleValidator;

/**
 * Uses the trick of augmenting rules to artificially limit variables. If any
 * head variable is found that does not occur in a positive ordinary predicate
 * then a literal is added to the rule body $UNIVERSE$( variable ). This has the
 * effect of binding the variable to the 'universe' of known ground terms.
 */
public class AugmentingRuleSafetyProcessor implements IRuleSafetyProcessor {
	public IRule process(IRule rule) throws RuleUnsafeException {
		RuleValidator validator = new RuleValidator(rule, true, true);

		List<IVariable> unlimitedVariables = validator
				.getAllUnlimitedVariables();

		if (unlimitedVariables.size() > 0) {
			List<ILiteral> body = new ArrayList<ILiteral>();

			for (ILiteral literal : rule.getBody())
				body.add(literal);

			for (IVariable variable : unlimitedVariables) {
				ILiteral newLiteral = Factory.BASIC.createLiteral(true,
						FiniteUniverseFacts.UNIVERSE,
						Factory.BASIC.createTuple(variable));
				body.add(newLiteral);
			}

			return Factory.BASIC.createRule(rule.getHead(), body);
		} else
			return rule;
	}
}
