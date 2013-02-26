package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTime;
import at.sti2.streamingiris.api.terms.concrete.IGYearMonth;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to GYearMonth instances. The following data types are
 * supported:
 * <ul>
 * <li>Date</li>
 * <li>DateTime</li>
 * </ul>
 */
public class ToGYearMonthBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_GYEARMONTH", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToGYearMonthBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IGYearMonth) {
			return term;
		} else if (term instanceof IDateTerm) {
			return toGYearMonth((IDateTerm) term);
		} else if (term instanceof IDateTime) {
			return toGYearMonth((IDateTime) term);
		}

		return null;
	}

	/**
	 * Converts a Date term to a GYearMonth term.
	 * 
	 * @param term
	 *            The Date term to be converted.
	 * @return A new GYearMonth term representing the result of the conversion.
	 */
	public static IGYearMonth toGYearMonth(IDateTerm term) {
		return CONCRETE.createGYearMonth(term.getYear(), term.getMonth());
	}

	/**
	 * Converts a DateTime term to a GYearMonth term.
	 * 
	 * @param term
	 *            The DateTime term to be converted.
	 * @return A new GYearMonth term representing the result of the conversion.
	 */
	public static IGYearMonth toGYearMonth(IDateTime term) {
		return CONCRETE.createGYearMonth(term.getYear(), term.getMonth());
	}

}
