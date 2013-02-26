package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.math.BigInteger;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IPositiveInteger;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link IPositiveInteger} instances. The following data
 * types are supported:
 * <ul>
 * <li>Numeric</li>
 * <li>String</li>
 * </ul>
 */
public class ToPositiveIntegerBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_POSITIVEINTEGER", 2);

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
	public ToPositiveIntegerBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IPositiveInteger) {
			return (IPositiveInteger) term;
		} else if (term instanceof INumericTerm) {
			return toPositiveInteger((INumericTerm) term);
		} else if (term instanceof IStringTerm) {
			return toPositiveInteger((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a {@link INumericTerm} term to a {@link IPositiveInteger} term.
	 * 
	 * @param term
	 *            The {@link INumericTerm} term to be converted.
	 * @return A new {@link IPositiveInteger} term representing the result of
	 *         the conversion, or <code>null</code> if the conversion fails.
	 */
	public static IPositiveInteger toPositiveInteger(INumericTerm term) {
		try {
			return CONCRETE.createPositiveInteger(term.getValue()
					.toBigInteger());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Converts a {@link IStringTerm} term to a {@link IPositiveInteger} term.
	 * 
	 * @param term
	 *            The {@link IStringTerm} term to be converted.
	 * @return A new {@link IPositiveInteger} term representing the result of
	 *         the conversion, or <code>null</code> if the conversion fails.
	 */
	public static IPositiveInteger toPositiveInteger(IStringTerm term) {
		try {
			String string = term.getValue();

			int indexOfDot = string.indexOf(".");
			if (indexOfDot > -1) {
				string = string.substring(0, indexOfDot);
			}

			return CONCRETE.createPositiveInteger(new BigInteger(string));
		} catch (NumberFormatException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
