package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IBooleanTerm;
import at.sti2.streamingiris.api.terms.concrete.IUnsignedInt;
import at.sti2.streamingiris.api.terms.concrete.IUnsignedLong;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link IUnsignedInt} instances. The following data types
 * are supported:
 * <ul>
 * <li>Boolean</li>
 * <li>Numeric</li>
 * <li>String</li>
 * </ul>
 */
public class ToUnsignedIntBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_UNSIGNEDINT", 2);

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
	public ToUnsignedIntBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected IUnsignedInt convert(ITerm term) {
		if (term instanceof IUnsignedInt) {
			return (IUnsignedInt) term;
		} else if (term instanceof IBooleanTerm) {
			return toUnsignedInt((IBooleanTerm) term);
		} else if (term instanceof INumericTerm) {
			return toUnsignedInt((INumericTerm) term);
		} else if (term instanceof IStringTerm) {
			return toUnsignedInt((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a {@link IBooleanTerm} term to a {@link IUnsignedInt} term. A
	 * {@link IBooleanTerm} term representing the value "True" is converted to a
	 * {@link IUnsignedInt} term representing "1". A {@link IBooleanTerm} term
	 * representing the value "False" is converted to a {@link IUnsignedInt}
	 * term representing "0".
	 * 
	 * @param term
	 *            The {@link IBooleanTerm} term to be converted.
	 * @return A new {@link IUnsignedLong} term representing the result of the
	 *         conversion.
	 */
	public static IUnsignedInt toUnsignedInt(IBooleanTerm term) {
		if (term.getValue()) {
			return CONCRETE.createUnsignedInt(1);
		}

		return CONCRETE.createUnsignedInt(0);
	}

	/**
	 * Converts a {@link INumericTerm} term to a {@link IUnsignedInt} term.
	 * 
	 * @param term
	 *            The {@link INumericTerm} term to be converted.
	 * @return A new {@link IUnsignedInt} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static IUnsignedInt toUnsignedInt(INumericTerm term) {
		try {
			return CONCRETE.createUnsignedInt(term.getValue().toBigInteger()
					.longValue());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Converts a {@link IStringTerm} term to a {@link IUnsignedInt} term.
	 * 
	 * @param term
	 *            The {@link IStringTerm} term to be converted.
	 * @return A new {@link IUnsignedInt} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static IUnsignedInt toUnsignedInt(IStringTerm term) {
		try {
			String string = term.getValue();

			int indexOfDot = string.indexOf(".");
			if (indexOfDot > -1) {
				string = string.substring(0, indexOfDot);
			}

			return CONCRETE.createUnsignedInt(Long.valueOf(string));
		} catch (NumberFormatException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
