package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IBooleanTerm;
import at.sti2.streamingiris.api.terms.concrete.IUnsignedByte;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link IUnsignedByte} instances. The following data types
 * are supported:
 * <ul>
 * <li>Boolean</li>
 * <li>Numeric</li>
 * <li>String</li>
 * </ul>
 */
public class ToUnsignedByteBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_UNSIGNEDBYTE", 2);

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
	public ToUnsignedByteBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected IUnsignedByte convert(ITerm term) {
		if (term instanceof IUnsignedByte) {
			return (IUnsignedByte) term;
		} else if (term instanceof IBooleanTerm) {
			return toUnsignedByte((IBooleanTerm) term);
		} else if (term instanceof INumericTerm) {
			return toUnsignedByte((INumericTerm) term);
		} else if (term instanceof IStringTerm) {
			return toUnsignedByte((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a {@link IBooleanTerm} term to a {@link IUnsignedByte} term. A
	 * {@link IBooleanTerm} term representing the value "True" is converted to a
	 * {@link IUnsignedByte} term representing "1". A {@link IBooleanTerm} term
	 * representing the value "False" is converted to a {@link IUnsignedByte}
	 * term representing "0".
	 * 
	 * @param term
	 *            The {@link IBooleanTerm} term to be converted.
	 * @return A new {@link IUnsignedByte} term representing the result of the
	 *         conversion.
	 */
	public static IUnsignedByte toUnsignedByte(IBooleanTerm term) {
		if (term.getValue()) {
			return CONCRETE.createUnsignedByte((short) 1);
		}

		return CONCRETE.createUnsignedByte((short) 0);
	}

	/**
	 * Converts a {@link INumericTerm} term to a {@link IUnsignedByte} term.
	 * 
	 * @param term
	 *            The {@link INumericTerm} term to be converted.
	 * @return A new {@link IUnsignedByte} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static IUnsignedByte toUnsignedByte(INumericTerm term) {
		try {
			return CONCRETE.createUnsignedByte(term.getValue().toBigInteger()
					.shortValue());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Converts a {@link IStringTerm} term to a {@link IUnsignedByte} term.
	 * 
	 * @param term
	 *            The {@link IStringTerm} term to be converted.
	 * @return A new {@link IUnsignedByte} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static IUnsignedByte toUnsignedByte(IStringTerm term) {
		try {
			String string = term.getValue();

			int indexOfDot = string.indexOf(".");
			if (indexOfDot > -1) {
				string = string.substring(0, indexOfDot);
			}

			return CONCRETE.createUnsignedByte(Short.valueOf(string));
		} catch (NumberFormatException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
