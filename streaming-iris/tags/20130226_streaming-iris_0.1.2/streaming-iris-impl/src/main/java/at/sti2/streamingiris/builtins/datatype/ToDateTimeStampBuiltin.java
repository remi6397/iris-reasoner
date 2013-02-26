package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.util.TimeZone;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTime;
import at.sti2.streamingiris.api.terms.concrete.IDateTimeStamp;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link IDateTimeStamp} instances. The following data types
 * are supported:
 * <ul>
 * <li>Date</li>
 * </ul>
 */
public class ToDateTimeStampBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_DATETIMESTAMP", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data value to be converted.
	 */
	public ToDateTimeStampBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IDateTimeStamp) {
			return term;
		} else if (term instanceof IDateTime) {
			toDateTimeStamp((IDateTime) term);
		} else if (term instanceof IDateTerm) {
			return toDateTimeStamp((IDateTerm) term);
		}

		return null;
	}

	/**
	 * Converts a DateTime term to a DateTimeStamp term.
	 * 
	 * @param term
	 *            The DateTime term to be converted.
	 * @return A new DateTimeStamp term representing the result of the
	 *         conversion.
	 */
	public static IDateTime toDateTimeStamp(IDateTime term) {
		TimeZone timeZone = term.getTimeZone();
		int offset = timeZone.getRawOffset();

		int tzHour = offset / 3600000;
		int tzMinute = (Math.abs(offset) % 3600000) / 60000;

		if (offset < 0) {
			tzMinute *= -1;
		}

		return CONCRETE.createDateTimeStamp(term.getYear(), term.getMonth(),
				term.getDay(), 0, 0, 0, tzHour, tzMinute);
	}

	/**
	 * Converts a Date term to a DateTime term.
	 * 
	 * @param term
	 *            The Date term to be converted.
	 * @return A new DateTime term representing the result of the conversion.
	 */
	public static IDateTime toDateTimeStamp(IDateTerm term) {
		TimeZone timeZone = term.getTimeZone();
		int offset = timeZone.getRawOffset();

		int tzHour = offset / 3600000;
		int tzMinute = (Math.abs(offset) % 3600000) / 60000;

		if (offset < 0) {
			tzMinute *= -1;
		}

		return CONCRETE.createDateTimeStamp(term.getYear(), term.getMonth(),
				term.getDay(), 0, 0, 0, tzHour, tzMinute);
	}

}
