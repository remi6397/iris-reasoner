package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IBooleanTerm;
import at.sti2.streamingiris.api.terms.concrete.ILongTerm;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link ILongTerm} instances. The following data types are
 * supported:
 * <ul>
 * <li>Boolean</li>
 * <li>Numeric</li>
 * <li>String</li>
 * </ul>
 */
public class ToLongBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_LONG", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            An array of terms, where first one is the term to convert and
	 *            the last term represents the result of this data type
	 *            conversion.
	 * @throws NullPointerException
	 *             If <code>terms</code> is <code>null</code>.
	 * @throws NullPointerException
	 *             If the terms contain a <code>null</code> value.
	 * @throws IllegalArgumentException
	 *             If the length of the terms and the arity of the predicate do
	 *             not match.
	 */
	public ToLongBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ILongTerm convert(ITerm term) {
		if (term instanceof ILongTerm) {
			return (ILongTerm) term;
		} else if (term instanceof IBooleanTerm) {
			return toLong((IBooleanTerm) term);
		} else if (term instanceof INumericTerm) {
			return toLong((INumericTerm) term);
		} else if (term instanceof IStringTerm) {
			return toLong((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a {@link IBooleanTerm} term to a {@link ILongTerm} term. A
	 * {@link IBooleanTerm} term representing the value "True" is converted to a
	 * {@link ILongTerm} term representing "1". A {@link IBooleanTerm} term
	 * representing the value "False" is converted to a {@link ILongTerm} term
	 * representing "0".
	 * 
	 * @param term
	 *            The {@link IBooleanTerm} term to be converted.
	 * @return A new {@link ILongTerm} term representing the result of the
	 *         conversion.
	 */
	public static ILongTerm toLong(IBooleanTerm term) {
		if (term.getValue()) {
			return CONCRETE.createLong(1);
		}

		return CONCRETE.createLong(0);
	}

	/**
	 * Converts a {@link INumericTerm} term to an {@link ILongTerm} term.
	 * 
	 * @param term
	 *            The {@link INumericTerm} term to be converted.
	 * @return A new {@link ILongTerm} term representing the result of the
	 *         conversion.
	 */
	public static ILongTerm toLong(INumericTerm term) {
		return CONCRETE.createLong(term.getValue().toBigInteger().longValue());
	}

	/**
	 * Converts a {@link IStringTerm} term to an {@link ILongTerm} term.
	 * 
	 * @param term
	 *            The {@link IStringTerm} term to be converted.
	 * @return A new {@link ILongTerm} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static ILongTerm toLong(IStringTerm term) {
		try {
			String string = term.getValue();

			int indexOfDot = string.indexOf(".");
			if (indexOfDot > -1) {
				string = string.substring(0, indexOfDot);
			}

			return CONCRETE.createLong(Long.valueOf(string));
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
