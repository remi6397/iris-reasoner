package at.sti2.streamingiris.builtins.list;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:list-contains.
 * </p>
 * <p>
 * Checks if a given <code>Object</code> is in a <code>List</code>
 * </p>
 * see <A HREF=
 * "http://www.w3.org/2005/rules/wiki/DTB#Functions_and_Predicates_on_RIF_Lists"
 * >http://www.w3.org/2005/rules/wiki/DTB#Functions_and_Predicates_on_RIF_Lists
 * 
 */
public class ListContainsBuiltin extends BooleanBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"LIST_CONTAINS", 2);

	/**
	 * Creates the built-in for the specified terms.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws NullPointerException
	 *             If one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the number of terms submitted is not 1.
	 */
	public ListContainsBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected boolean computeResult(ITerm[] terms) {
		return ListBuiltinHelper.listContains(terms);
	}

}
