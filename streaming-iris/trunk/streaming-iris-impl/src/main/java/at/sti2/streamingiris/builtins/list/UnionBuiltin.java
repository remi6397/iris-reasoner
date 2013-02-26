package at.sti2.streamingiris.builtins.list;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.AbstractBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:make-list.
 * </p>
 * <p>
 * Returns a list containing all the items in <code>list_1, ..., list_n</code>,
 * in the same order, but with all duplicates removed.
 * </p>
 * see <A HREF=
 * "http://www.w3.org/2005/rules/wiki/DTB#Functions_and_Predicates_on_RIF_Lists"
 * >http://www.w3.org/2005/rules/wiki/DTB#Functions_and_Predicates_on_RIF_Lists
 * 
 */
public class UnionBuiltin extends AbstractBuiltin {

	private static final String PREDICATE_STRING = "UNION";
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
	 *             If the number of terms submitted is not 2 or more.
	 */
	public UnionBuiltin(ITerm... terms) {
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
		return ListBuiltinHelper.union(terms);
	}

}
