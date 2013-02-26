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
 * type instances to Duration instances. The following data types are supported:
 * <ul>
 * <li>DayTimeDuration</li>
 * <li>YearMonthDuration</li>
 * </ul>
 */
public class ToDurationBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_DURATION", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToDurationBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		return toDuration(term);
	}

	/**
	 * Converts the specified term to a Duration term, if possible.
	 * 
	 * @param term
	 *            The term to be converted.
	 * @return A new Duration term representing the result of the conversion, or
	 *         <code>null</code> if the term can not be converted.
	 */
	public static IDuration toDuration(ITerm term) {
		if (term instanceof IDuration) {
			return (IDuration) term;
		} else if (term instanceof IDayTimeDuration) {
			return toDuration((IDayTimeDuration) term);
		} else if (term instanceof IYearMonthDuration) {
			return toDuration((IYearMonthDuration) term);
		}

		return null;
	}

	/**
	 * Converts a DayTimeDuration term to a Duration term.
	 * 
	 * @param term
	 *            The DayTimeDuration term to be converted.
	 * @return A new Duration term representing the result of the conversion.
	 */
	public static IDuration toDuration(IDayTimeDuration term) {
		return CONCRETE.createDuration(term.isPositive(), 0, 0, term.getDay(),
				term.getHour(), term.getMinute(), term.getDecimalSecond());
	}

	/**
	 * Converts a YearMonthDuration term to a Duration term.
	 * 
	 * @param term
	 *            The YearMonthDuration term to be converted.
	 * @return A new Duration term representing the result of the conversion.
	 */
	public static IDuration toDuration(IYearMonthDuration term) {
		return CONCRETE.createDuration(term.isPositive(), term.getYear(),
				term.getMonth(), 0, 0, 0, 0);
	}

}
