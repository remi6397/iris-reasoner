package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTime;
import at.sti2.streamingiris.api.terms.concrete.IGMonthDay;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to GMonthDay instances. The following data types are
 * supported:
 * <ul>
 * <li>Date</li>
 * <li>DateTime</li>
 * </ul>
 */
public class ToGMonthDayBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_GMONTHDAY", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToGMonthDayBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IGMonthDay) {
			return term;
		} else if (term instanceof IDateTerm) {
			return toGMonthDay((IDateTerm) term);
		} else if (term instanceof IDateTime) {
			return toGMonthDay((IDateTime) term);
		}

		return null;
	}

	/**
	 * Converts a Date term to a GMonthDay term.
	 * 
	 * @param term
	 *            The Date term to be converted.
	 * @return A new GMonthDay term representing the result of the conversion.
	 */
	public static IGMonthDay toGMonthDay(IDateTerm term) {
		return CONCRETE.createGMonthDay(term.getMonth(), term.getDay());
	}

	/**
	 * Converts a DateTime term to a GMonthDay term.
	 * 
	 * @param term
	 *            The DateTime term to be converted.
	 * @return A new GMonthDay term representing the result of the conversion.
	 */
	public static IGMonthDay toGMonthDay(IDateTime term) {
		return CONCRETE.createGMonthDay(term.getMonth(), term.getDay());
	}

}
