package at.sti2.streamingiris.builtins.numeric;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BuiltinHelper;
import at.sti2.streamingiris.builtins.DivideBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:numeric-divide.
 * </p>
 */
public class NumericDivideBuiltin extends DivideBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"NUMERIC_DIVIDE", 3);

	public NumericDivideBuiltin(ITerm... terms) {
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
