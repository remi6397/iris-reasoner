package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IName;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link IName} instances. The following data types are
 * supported:
 * <ul>
 * <li>String</li>
 * </ul>
 */
public class ToNameBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_NAME", 2);

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
	public ToNameBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected IName convert(ITerm term) {
		if (term instanceof IName) {
			return (IName) term;
		} else if (term instanceof IStringTerm) {
			return toName((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a {@link IStringTerm} term to a {@link IName} term.
	 * 
	 * @param term
	 *            The {@link IStringTerm} term to be converted.
	 * @return A new {@link IName} term representing the result of the
	 *         conversion, or <code>null</code> if the conversion fails.
	 */
	public static IName toName(IStringTerm term) {
		return CONCRETE.createName(term.toCanonicalString());
	}

}
