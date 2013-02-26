package at.sti2.streamingiris.rules.safety;

import java.util.List;

import at.sti2.streamingiris.RuleUnsafeException;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.rules.IRuleSafetyProcessor;
import at.sti2.streamingiris.rules.RuleValidator;

/**
 * A standard rule-safety processor that checks if all variables are limited, a
 * la Ullman. If not, a rule unsafe exception is thrown.
 */
public class StandardRuleSafetyProcessor implements IRuleSafetyProcessor {
	/**
	 * Default constructor. Initialises with most flexible rule-safety
	 * parameters.
	 */
	public StandardRuleSafetyProcessor() {
		this(true, true);
	}

	/**
	 * Constructor.
	 * 
	 * @param allowUnlimitedVariablesInNegatedOrdinaryPredicates
	 *            Indicates if a rule can still be considered safe if one or
	 *            more variables occur in negative ordinary predicates and
	 *            nowhere else, e.g. p(X) :- q(X), not r(Y) if true, the above
	 *            rule would be safe
	 * @param ternaryTargetsImplyLimited
	 *            Indicates if ternary arithmetic built-ins can be used to
	 *            deduce limited variables, e.g. p(Z) :- q(X, Y), X + Y = Z if
	 *            true, then Z would be considered limited.
	 */
	public StandardRuleSafetyProcessor(
			boolean allowUnlimitedVariablesInNegatedOrdinaryPredicates,
			boolean ternaryTargetsImplyLimited) {
		mAllowUnlimitedVariablesInNegatedOrdinaryPredicates = allowUnlimitedVariablesInNegatedOrdinaryPredicates;
		mTernaryTargetsImplyLimited = ternaryTargetsImplyLimited;
	}

	public IRule process(IRule rule) throws RuleUnsafeException {
		RuleValidator validator = new RuleValidator(rule,
				mAllowUnlimitedVariablesInNegatedOrdinaryPredicates,
				mTernaryTargetsImplyLimited);

		List<IVariable> unsafeVariables = validator.getAllUnlimitedVariables();

		if (unsafeVariables.size() > 0) {
			StringBuilder buffer = new StringBuilder();
			buffer.append(rule).append(" contains unlimited variable(s): ");

			boolean first = true;
			for (IVariable variable : unsafeVariables) {
				if (first)
					first = false;
				else
					buffer.append(", ");
				buffer.append(variable);
			}

			throw new RuleUnsafeException(buffer.toString());
		}

		return rule;
	}

	private final boolean mAllowUnlimitedVariablesInNegatedOrdinaryPredicates;
	private final boolean mTernaryTargetsImplyLimited;
}
