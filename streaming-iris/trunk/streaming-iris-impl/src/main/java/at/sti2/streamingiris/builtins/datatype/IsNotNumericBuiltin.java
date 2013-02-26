package at.sti2.streamingiris.builtins.datatype;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * Checks whether a term is not of any numeric type (integer, float, double,
 * decimal).
 */
public class IsNotNumericBuiltin extends BooleanBuiltin {
	/**
	 * Constructor.
	 * 
	 * @param terms
	 *            List of exactly one term.
	 */
	public IsNotNumericBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected boolean computeResult(ITerm[] terms) {
		return !IsNumericBuiltin.isNumeric(terms[0]);
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = at.sti2.streamingiris.factory.Factory.BASIC
			.createPredicate("IS_NOT_NUMERIC", 1);
}
