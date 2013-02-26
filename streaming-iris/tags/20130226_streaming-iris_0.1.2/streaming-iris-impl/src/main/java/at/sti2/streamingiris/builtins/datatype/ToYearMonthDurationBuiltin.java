package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDayTimeDuration;
import at.sti2.streamingiris.api.terms.concrete.IDuration;
import at.sti2.streamingiris.api.terms.concrete.IYearMonthDuration;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to YearMonthDuration instances. The following data types are
 * supported:
 * <ul>
 * <li>Duration</li>
 * <li>DayTimeDuration</li>
 * </ul>
 */
public class ToYearMonthDurationBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_YEARMONTHDURATION", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToYearMonthDurationBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IYearMonthDuration) {
			return term;
		} else if (term instanceof IDuration) {
			return toYearMonthDuration((IDuration) term);
		} else if (term instanceof IDayTimeDuration) {
			return toYearMonthDuration((IDayTimeDuration) term);
		}

		return null;
	}

	/**
	 * Converts a Duration term to a YearMonthDuration term.
	 * 
	 * @param term
	 *            The Duration term to be converted.
	 * @return A new YearMonthDuration term representing the result of the
	 *         conversion.
	 */
	public static IYearMonthDuration toYearMonthDuration(IDuration term) {
		return CONCRETE.createYearMonthDuration(term.isPositive(),
				term.getYear(), term.getMonth());
	}

	/**
	 * Converts a DayTimeDuration term to a YearMonthDuration term.
	 * 
	 * @param term
	 *            The DayTimeDuration term to be converted.
	 * @return A new YearMonthDuration term representing the result of the
	 *         conversion.
	 */
	public static IYearMonthDuration toYearMonthDuration(IDayTimeDuration term) {
		return CONCRETE.createYearMonthDuration(term.isPositive(), 0, 0);
	}

}
