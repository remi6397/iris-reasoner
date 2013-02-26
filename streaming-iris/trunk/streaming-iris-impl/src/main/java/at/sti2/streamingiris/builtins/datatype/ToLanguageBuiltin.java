package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.ILanguage;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link ILanguage} instances. The following data types are
 * supported:
 * <ul>
 * <li>String</li>
 * </ul>
 */
public class ToLanguageBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_LANGUAGE", 2);

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
	public ToLanguageBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ILanguage convert(ITerm term) {
		if (term instanceof ILanguage) {
			return (ILanguage) term;
		} else if (term instanceof IStringTerm) {
			return toLanguage((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a {@link IStringTerm} term to a {@link ILanguage} term.
	 * 
	 * @param term
	 *            The {@link IStringTerm} term to be converted.
	 * @return A new {@link ILanguage} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static ILanguage toLanguage(IStringTerm term) {
		try {
			return CONCRETE.createLanguage(term.getValue());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
