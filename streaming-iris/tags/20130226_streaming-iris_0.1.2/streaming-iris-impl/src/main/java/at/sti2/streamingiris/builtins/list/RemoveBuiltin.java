package at.sti2.streamingiris.builtins.list;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.AbstractBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:remove.
 * </p>
 * <p>
 * Returns a list which is <code>list</code> except that the item at the given
 * <code>position</code> has been removed.
 * </p>
 * see <A HREF=
 * "http://www.w3.org/2005/rules/wiki/DTB#Functions_and_Predicates_on_RIF_Lists"
 * >http://www.w3.org/2005/rules/wiki/DTB#Functions_and_Predicates_on_RIF_Lists
 * 
 */
public class RemoveBuiltin extends AbstractBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate("REMOVE",
			2);

	/**
	 * Creates the built-in for the specified terms.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws NullPointerException
	 *             If one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the number of terms submitted is not 2.
	 */
	public RemoveBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm evaluateTerms(ITerm[] terms, int[] variableIndexes) {
		assert variableIndexes.length == 0;
		return computeResult(terms);
	}

	protected ITerm computeResult(ITerm... terms) {
		return ListBuiltinHelper.remove(terms);
	}

}
