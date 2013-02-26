package at.sti2.streamingiris.builtins.list;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.AbstractBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:intersect.
 * </p>
 * <p>
 * Returns a list which contains exactly those items which are common to all
 * argument lists. The order of the items in the returned list is the same as
 * the order in <code>list_1</code>.
 * </p>
 * see <A HREF=
 * "http://www.w3.org/2005/rules/wiki/DTB#Functions_and_Predicates_on_RIF_Lists"
 * >http://www.w3.org/2005/rules/wiki/DTB#Functions_and_Predicates_on_RIF_Lists
 * 
 * 
 */
public class IntersectBuiltin extends AbstractBuiltin {

	private static final String PREDICATE_STRING = "INTERSECT";
	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			PREDICATE_STRING, -2);

	/**
	 * Creates the built-in for the specified terms.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws NullPointerException
	 *             If one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the number of terms submitted is not at least 2.
	 */
	public IntersectBuiltin(ITerm... terms) {
		super(BASIC.createPredicate(PREDICATE_STRING, terms.length), terms);

		if (terms.length < 2) {
			throw new IllegalArgumentException("The amount of terms <"
					+ terms.length + "> must at least 2");
		}
	}

	protected ITerm evaluateTerms(ITerm[] terms, int[] variableIndexes) {
		assert variableIndexes.length == 0;
		return computeResult(terms);
	}

	protected ITerm computeResult(ITerm... terms) {
		return ListBuiltinHelper.intersect(terms);
	}

}
