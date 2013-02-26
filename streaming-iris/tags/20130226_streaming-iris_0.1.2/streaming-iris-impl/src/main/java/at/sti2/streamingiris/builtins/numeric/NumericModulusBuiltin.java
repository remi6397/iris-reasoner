package at.sti2.streamingiris.builtins.numeric;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BuiltinHelper;
import at.sti2.streamingiris.builtins.ModulusBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:numeric-modulus.
 * </p>
 */
public class NumericModulusBuiltin extends ModulusBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"NUMERIC_MODULUS", 3);

	public NumericModulusBuiltin(ITerm... terms) {
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
