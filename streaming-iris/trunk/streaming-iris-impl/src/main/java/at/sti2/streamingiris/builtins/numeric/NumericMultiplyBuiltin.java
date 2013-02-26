package at.sti2.streamingiris.builtins.numeric;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BuiltinHelper;
import at.sti2.streamingiris.builtins.MultiplyBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:numeric-multiply.
 * </p>
 */
public class NumericMultiplyBuiltin extends MultiplyBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"NUMERIC_MULTIPLY", 3);

	public NumericMultiplyBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms)
			throws EvaluationException {
		if (!BuiltinHelper.areNumeric(terms, missingTermIndex)) {
			return null;
		}

		return super.computeMissingTerm(missingTermIndex, terms);
	}

}
