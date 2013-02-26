package at.sti2.streamingiris.builtins.datatype;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * Checks whether a term is of any numeric type (integer, float, double,
 * decimal).
 */
public class IsNumericBuiltin extends BooleanBuiltin {
	/**
	 * Constructor.
	 * 
	 * @param terms
	 *            List of exactly one term.
	 */
	public IsNumericBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected boolean computeResult(ITerm[] terms) {
		return isNumeric(terms[0]);
	}

	public static boolean isNumeric(ITerm term) {
		return term instanceof INumericTerm;
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = at.sti2.streamingiris.factory.Factory.BASIC
			.createPredicate("IS_NUMERIC", 1);
}
