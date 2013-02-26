package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.math.BigDecimal;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IBooleanTerm;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to Boolean instances. The following data types are supported:
 * <ul>
 * <li>Float</li>
 * <li>Double</li>
 * <li>Decimal</li>
 * <li>Integer</li>
 * <li>String</li>
 * </ul>
 */
public class ToBooleanBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_BOOLEAN", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToBooleanBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IBooleanTerm) {
			return term;
		} else if (term instanceof IStringTerm) {
			return toBoolean((IStringTerm) term);
		} else if (term instanceof INumericTerm) {
			return toBoolean((INumericTerm) term);
		}

		return null;
	}

	/**
	 * Converts a String term to a Boolean term.
	 * 
	 * @param term
	 *            The String term to be converted.
	 * @return A new Boolean term representing the result of the conversion.
	 */
	public static IBooleanTerm toBoolean(IStringTerm term) {
		String value = term.getValue();
		boolean result = Boolean.parseBoolean(value);

		return CONCRETE.createBoolean(result);
	}

	/**
	 * Converts a Numeric term to a Boolean term. The Boolean term represents
	 * <code>true</code>, if the numeric term represents any number not equal to
	 * 0.0, false<code>false</code> otherwise.
	 * 
	 * @param term
	 *            The Numeric term to be converted.
	 * @return A new Boolean term representing the result of the conversion.
	 */
	public static IBooleanTerm toBoolean(INumericTerm term) {
		BigDecimal value = term.getValue();

		boolean result = value.compareTo(BigDecimal.ZERO) != 0;

		return CONCRETE.createBoolean(result);
	}

}
