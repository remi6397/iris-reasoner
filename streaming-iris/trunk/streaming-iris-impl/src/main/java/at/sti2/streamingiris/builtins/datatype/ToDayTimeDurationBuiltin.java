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
 * type instances to DayTimeDuration instances. The following data types are
 * supported:
 * <ul>
 * <li>Duration</li>
 * <li>YearMonthDuration</li>
 * </ul>
 */
public class ToDayTimeDurationBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_DAYTIMEDURATION", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToDayTimeDurationBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IDayTimeDuration) {
			return term;
		} else if (term instanceof IDuration) {
			return toDayTimeDuration((IDuration) term);
		} else if (term instanceof IYearMonthDuration) {
			return toDayTimeDuration((IYearMonthDuration) term);
		}

		return null;
	}

	/**
	 * Converts a Duration term to a DayTimeDuration term.
	 * 
	 * @param term
	 *            The Duration term to be converted.
	 * @return A new DayTimeDuration term representing the result of the
	 *         conversion.
	 */
	public static IDayTimeDuration toDayTimeDuration(IDuration term) {
		return CONCRETE.createDayTimeDuration(term.isPositive(), term.getDay(),
				term.getHour(), term.getMinute(), term.getSecond());
	}

	/**
	 * Converts a YearMonthDuration term to a DayTimeDuration term.
	 * 
	 * @param term
	 *            The YearMonthDuration term to be converted.
	 * @return A new DayTimeDuration term representing the result of the
	 *         conversion.
	 */
	public static IDayTimeDuration toDayTimeDuration(IYearMonthDuration term) {
		return CONCRETE.createDayTimeDuration(term.isPositive(), 0, 0, 0, 0);
	}

}
