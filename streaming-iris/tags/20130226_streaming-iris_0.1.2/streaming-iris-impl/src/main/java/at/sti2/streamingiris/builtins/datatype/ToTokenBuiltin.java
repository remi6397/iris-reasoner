package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IToken;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link IToken} instances. The following data types are
 * supported:
 * <ul>
 * <li>Numeric</li>
 * <li>String</li>
 * </ul>
 */
public class ToTokenBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_TOKEN", 2);

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
	public ToTokenBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected IToken convert(ITerm term) {
		if (term instanceof IToken) {
			return (IToken) term;
		} else if (term instanceof INumericTerm) {
			return toToken((INumericTerm) term);
		} else if (term instanceof IStringTerm) {
			return toToken((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a {@link INumericTerm} term to a {@link IToken} term.
	 * 
	 * @param term
	 *            The {@link INumericTerm} term to be converted.
	 * @return A new {@link IToken} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static IToken toToken(INumericTerm term) {
		return CONCRETE.createToken(term.toCanonicalString());
	}

	/**
	 * Converts a {@link IStringTerm} term to a {@link IToken} term.
	 * 
	 * @param term
	 *            The {@link IStringTerm} term to be converted.
	 * @return A new {@link IToken} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static IToken toToken(IStringTerm term) {
		return CONCRETE.createToken(term.toCanonicalString());
	}

}
