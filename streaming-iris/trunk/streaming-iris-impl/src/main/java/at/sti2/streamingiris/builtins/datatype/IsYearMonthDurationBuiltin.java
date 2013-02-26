package at.sti2.streamingiris.builtins.datatype;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IYearMonthDuration;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * Checks if a term is of type 'YearMonthDuration'.
 */
public class IsYearMonthDurationBuiltin extends BooleanBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = at.sti2.streamingiris.factory.Factory.BASIC
			.createPredicate("IS_YEARMONTHDURATION", 1);

	/**
	 * Constructor.
	 * 
	 * @param terms
	 *            The list of terms. Must always be of length 1 in this case.
	 */
	public IsYearMonthDurationBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected boolean computeResult(ITerm[] terms) {
		return isYearMonthDuration(terms[0]);
	}

	public static boolean isYearMonthDuration(ITerm term) {
		return term instanceof IYearMonthDuration;
	}

}
