package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTime;
import at.sti2.streamingiris.api.terms.concrete.IGYear;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to GYear instances. The following data types are supported:
 * <ul>
 * <li>Date</li>
 * <li>DateTime</li>
 * </ul>
 */
public class ToGYearBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_GYEAR", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToGYearBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IGYear) {
			return term;
		} else if (term instanceof IDateTerm) {
			return toGYear((IDateTerm) term);
		} else if (term instanceof IDateTime) {
			return toGYear((IDateTime) term);
		}

		return null;
	}

	/**
	 * Converts a Date term to a GYear term.
	 * 
	 * @param term
	 *            The Date term to be converted.
	 * @return A new GYear term representing the result of the conversion.
	 */
	public static IGYear toGYear(IDateTerm term) {
		return CONCRETE.createGYear(term.getYear());
	}

	/**
	 * Converts a DateTime term to a GYear term.
	 * 
	 * @param term
	 *            The DateTime term to be converted.
	 * @return A new GYear term representing the result of the conversion.
	 */
	public static IGYear toGYear(IDateTime term) {
		return CONCRETE.createGYear(term.getYear());
	}

}
