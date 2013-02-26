package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTime;
import at.sti2.streamingiris.api.terms.concrete.IGMonth;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to GMonth instances. The following data types are supported:
 * <ul>
 * <li>Date</li>
 * <li>DateTime</li>
 * </ul>
 */
public class ToGMonthBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_GMONTH", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToGMonthBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IGMonth) {
			return term;
		} else if (term instanceof IDateTerm) {
			return toGMonth((IDateTerm) term);
		} else if (term instanceof IDateTime) {
			return toGMonth((IDateTime) term);
		}

		return null;
	}

	/**
	 * Converts a Date term to a GMonth term.
	 * 
	 * @param term
	 *            The Date term to be converted.
	 * @return A new GMonth term representing the result of the conversion.
	 */
	public static IGMonth toGMonth(IDateTerm term) {
		return CONCRETE.createGMonth(term.getMonth());
	}

	/**
	 * Converts a DateTime term to a GMonth term.
	 * 
	 * @param term
	 *            The DateTime term to be converted.
	 * @return A new GMonth term representing the result of the conversion.
	 */
	public static IGMonth toGMonth(IDateTime term) {
		return CONCRETE.createGMonth(term.getMonth());
	}

}
