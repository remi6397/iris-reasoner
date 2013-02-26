package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.ITime;
import at.sti2.streamingiris.builtins.BuiltinHelper;
import at.sti2.streamingiris.builtins.FunctionalBuiltin;

/**
 * Represents the RIF built-in function func:timezone-from-time.
 */
public class TimezoneFromTimeBuiltin extends FunctionalBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TIMEZONE_FROM_TIME", 2);

	/**
	 * Creates the built-in for the specified terms.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws NullPointerException
	 *             If one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the number of terms submitted is not 2.
	 */
	public TimezoneFromTimeBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		if (terms[0] instanceof ITime) {
			return BuiltinHelper.timezonePart(terms[0]);
		}

		return null;
	}

}
