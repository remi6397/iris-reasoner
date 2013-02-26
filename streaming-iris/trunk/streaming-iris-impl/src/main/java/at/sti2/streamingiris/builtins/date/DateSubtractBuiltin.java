package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTerm;
import at.sti2.streamingiris.builtins.SubtractBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:subtract-dates.
 * </p>
 */
public class DateSubtractBuiltin extends SubtractBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"DATE_SUBTRACT", 3);

	/**
	 * Creates the built-in for the specified terms.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws NullPointerException
	 *             If one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the number of terms submitted is not 3.
	 */
	public DateSubtractBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
		if (terms[0] instanceof IDateTerm && terms[1] instanceof IDateTerm) {
			return super.computeMissingTerm(missingTermIndex, terms);
		}

		return null;
	}

}
