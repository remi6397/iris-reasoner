package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.math.BigInteger;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IBooleanTerm;
import at.sti2.streamingiris.api.terms.concrete.IUnsignedLong;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link IUnsignedLong} instances. The following data types
 * are supported:
 * <ul>
 * <li>Boolean</li>
 * <li>Numeric</li>
 * <li>String</li>
 * </ul>
 */
public class ToUnsignedLongBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_UNSIGNEDLONG", 2);

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
	public ToUnsignedLongBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected IUnsignedLong convert(ITerm term) {
		if (term instanceof IUnsignedLong) {
			return (IUnsignedLong) term;
		} else if (term instanceof IBooleanTerm) {
			return toUnsignedLong((IBooleanTerm) term);
		} else if (term instanceof INumericTerm) {
			return toUnsignedLong((INumericTerm) term);
		} else if (term instanceof IStringTerm) {
			return toUnsignedLong((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a {@link IBooleanTerm} term to a {@link IUnsignedLong} term. A
	 * {@link IBooleanTerm} term representing the value "True" is converted to a
	 * {@link IUnsignedLong} term representing "1". A {@link IBooleanTerm} term
	 * representing the value "False" is converted to a {@link IUnsignedLong}
	 * term representing "0".
	 * 
	 * @param term
	 *            The {@link IBooleanTerm} term to be converted.
	 * @return A new {@link IUnsignedLong} term representing the result of the
	 *         conversion.
	 */
	public static IUnsignedLong toUnsignedLong(IBooleanTerm term) {
		if (term.getValue()) {
			return CONCRETE.createUnsignedLong(BigInteger.valueOf(1));
		}

		return CONCRETE.createUnsignedLong(BigInteger.valueOf(0));
	}

	/**
	 * Converts a {@link INumericTerm} term to a {@link IUnsignedLong} term.
	 * 
	 * @param term
	 *            The {@link INumericTerm} term to be converted.
	 * @return A new {@link IUnsignedLong} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static IUnsignedLong toUnsignedLong(INumericTerm term) {
		try {
			return CONCRETE.createUnsignedLong(term.getValue().toBigInteger());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Converts a {@link IStringTerm} term to a {@link IUnsignedLong} term.
	 * 
	 * @param term
	 *            The {@link IStringTerm} term to be converted.
	 * @return A new {@link IUnsignedLong} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static IUnsignedLong toUnsignedLong(IStringTerm term) {
		try {
			String string = term.getValue();

			int indexOfDot = string.indexOf(".");
			if (indexOfDot > -1) {
				string = string.substring(0, indexOfDot);
			}

			return CONCRETE.createUnsignedLong(new BigInteger(string));
		} catch (NumberFormatException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
