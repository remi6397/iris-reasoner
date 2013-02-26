package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IQName;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to {@link IQName} instances. The following data types are
 * supported:
 * <ul>
 * <li>QName</li>
 * </ul>
 */
public class ToQNameBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_QNAME", 2);

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
	public ToQNameBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected IQName convert(ITerm term) {
		if (term instanceof IQName) {
			return (IQName) term;
		}

		return null;
	}

}
