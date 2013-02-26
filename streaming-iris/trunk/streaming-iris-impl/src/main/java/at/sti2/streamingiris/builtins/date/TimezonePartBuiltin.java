package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BuiltinHelper;
import at.sti2.streamingiris.builtins.FunctionalBuiltin;

/**
 * Represents the RIF built-in functions func:timezone-from-dateTime and
 * func:timezone-from-date.
 */
public class TimezonePartBuiltin extends FunctionalBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TIMEZONE_PART", 2);

	/**
	 * Constructor. Two terms must be passed to the constructor, otherwise an
	 * exception will be thrown.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws IllegalArgumentException
	 *             If one of the terms is {@code null}.
	 * @throws IllegalArgumentException
	 *             If the number of terms submitted is not 2.
	 * @throws IllegalArgumentException
	 *             If terms is <code>null</code>.
	 */
	public TimezonePartBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		return BuiltinHelper.timezonePart(terms[0]);
	}

}
