package at.sti2.streamingiris.builtins.datatype;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IGMonthDay;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * Checks if a term is of type 'GMonthDay'.
 */
public class IsGMonthDayBuiltin extends BooleanBuiltin {
	/**
	 * Constructor.
	 * 
	 * @param t
	 *            The list of terms. Must always be of length 1 in this case.
	 */
	public IsGMonthDayBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	protected boolean computeResult(ITerm[] terms) {
		return isGMonthDay(terms[0]);
	}

	public static boolean isGMonthDay(ITerm term) {
		return term instanceof IGMonthDay;
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = at.sti2.streamingiris.factory.Factory.BASIC
			.createPredicate("IS_GMONTHDAY", 1);
}
