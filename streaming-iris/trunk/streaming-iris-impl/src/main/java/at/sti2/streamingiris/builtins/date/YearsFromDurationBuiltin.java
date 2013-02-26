package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDuration;
import at.sti2.streamingiris.builtins.BuiltinHelper;
import at.sti2.streamingiris.builtins.FunctionalBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:years-from-duration.
 * </p>
 */
public class YearsFromDurationBuiltin extends FunctionalBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"YEARS_FROM_DURATION", 2);

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
	public YearsFromDurationBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) {
		if (terms[0] instanceof IDuration) {
			return BuiltinHelper.yearPart(terms[0]);
		}

		return null;
	}

}
