package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.util.TimeZone;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTime;
import at.sti2.streamingiris.api.terms.concrete.ITime;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to Time instances. The following data types are supported:
 * <ul>
 * <li>DateTime</li>
 * </ul>
 */
public class ToTimeBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_TIME", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToTimeBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof ITime) {
			return term;
		} else if (term instanceof IDateTime) {
			return toTime((IDateTime) term);
		}

		return null;
	}

	/**
	 * Converts a DateTime term to a Time term.
	 * 
	 * @param term
	 *            The DateTime term to be converted.
	 * @return A new Time term representing the result of the conversion.
	 */
	public static ITime toTime(IDateTime term) {
		TimeZone timeZone = term.getTimeZone();
		int offset = timeZone.getRawOffset();

		int tzHour = offset / 3600000;
		int tzMinute = (Math.abs(offset) % 3600000) / 60000;

		if (offset < 0) {
			tzMinute *= -1;
		}

		return CONCRETE.createTime(term.getHour(), term.getMinute(),
				term.getSecond(), tzHour, tzMinute);
	}

}
