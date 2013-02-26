package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IBooleanTerm;
import at.sti2.streamingiris.api.terms.concrete.IIntTerm;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link IIntTerm} instances. The following data types are
 * supported:
 * <ul>
 * <li>Boolean</li>
 * <li>Numeric</li>
 * <li>String</li>
 * </ul>
 */
public class ToIntBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate("TO_INT",
			2);

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
	public ToIntBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected IIntTerm convert(ITerm term) {
		if (term instanceof IIntTerm) {
			return (IIntTerm) term;
		} else if (term instanceof IBooleanTerm) {
			return toInt((IBooleanTerm) term);
		} else if (term instanceof INumericTerm) {
			return toInt((INumericTerm) term);
		} else if (term instanceof IStringTerm) {
			return toInt((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a {@link IBooleanTerm} term to an {@link IIntTerm} term. A
	 * {@link IBooleanTerm} term representing the value "True" is converted to
	 * an {@link IIntTerm} term representing "1". A {@link IBooleanTerm} term
	 * representing the value "False" is converted to an {@link IIntTerm} term
	 * representing "0".
	 * 
	 * @param term
	 *            The {@link IBooleanTerm} term to be converted.
	 * @return A new {@link IIntTerm} term representing the result of the
	 *         conversion.
	 */
	public static IIntTerm toInt(IBooleanTerm term) {
		if (term.getValue()) {
			return CONCRETE.createInt(1);
		}

		return CONCRETE.createInt(0);
	}

	/**
	 * Converts a {@link INumericTerm} term to an {@link IIntTerm} term.
	 * 
	 * @param term
	 *            The {@link INumericTerm} term to be converted.
	 * @return A new {@link IIntTerm} term representing the result of the
	 *         conversion.
	 */
	public static IIntTerm toInt(INumericTerm term) {
		return CONCRETE.createInt(term.getValue().toBigInteger().intValue());
	}

	/**
	 * Converts a {@link IStringTerm} term to an {@link IIntTerm} term.
	 * 
	 * @param term
	 *            The {@link IStringTerm} term to be converted.
	 * @return A new {@link IIntTerm} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static IIntTerm toInt(IStringTerm term) {
		try {
			String string = term.getValue();

			int indexOfDot = string.indexOf(".");
			if (indexOfDot > -1) {
				string = string.substring(0, indexOfDot);
			}

			return CONCRETE.createInt(Integer.valueOf(string));
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
