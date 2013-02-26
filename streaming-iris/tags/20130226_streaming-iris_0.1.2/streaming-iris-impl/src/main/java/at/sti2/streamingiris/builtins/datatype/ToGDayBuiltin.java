package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTime;
import at.sti2.streamingiris.api.terms.concrete.IGDay;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to GDay instances. The following data types are supported:
 * <ul>
 * <li>Date</li>
 * <li>DateTime</li>
 * </ul>
 */
public class ToGDayBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_GDAY", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToGDayBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IGDay) {
			return term;
		} else if (term instanceof IDateTerm) {
			return toGDay((IDateTerm) term);
		} else if (term instanceof IDateTime) {
			return toGDay((IDateTime) term);
		}

		return null;
	}

	/**
	 * Converts a Date term to a GDay term.
	 * 
	 * @param term
	 *            The Date term to be converted.
	 * @return A new GDay term representing the result of the conversion.
	 */
	public static IGDay toGDay(IDateTerm term) {
		return CONCRETE.createGDay(term.getDay());
	}

	/**
	 * Converts a DateTime term to a GDay term.
	 * 
	 * @param term
	 *            The DateTime term to be converted.
	 * @return A new GDay term representing the result of the conversion.
	 */
	public static IGDay toGDay(IDateTime term) {
		return CONCRETE.createGDay(term.getDay());
	}

}
