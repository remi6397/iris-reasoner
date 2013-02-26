package at.sti2.streamingiris.builtins.list;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.AbstractBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:sublist.
 * </p>
 * <p>
 * Returns a list, containing (in order) the items starting at position
 * <code>start</code> and continuing up to, but not including, the
 * <code>stop</code> position, if <code>start</code> is before <code>stop</code>
 * . The <code>stop</code> position may be omitted, in which case it defaults to
 * the length of the list.
 * </p>
 * see <A HREF=
 * "http://www.w3.org/2005/rules/wiki/DTB#Functions_and_Predicates_on_RIF_Lists"
 * >http://www.w3.org/2005/rules/wiki/DTB#Functions_and_Predicates_on_RIF_Lists
 * 
 */
public class SubListFromToBuiltin extends AbstractBuiltin {

	private static final String PREDICATE_STRING = "SUBLIST";
	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			PREDICATE_STRING, 3);

	/**
	 * Creates the built-in for the specified terms.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws NullPointerException
	 *             If one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the number of terms submitted is not 3.
	 */
	public SubListFromToBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm evaluateTerms(ITerm[] terms, int[] variableIndexes) {
		assert variableIndexes.length == 0;
		return computeResult(terms);
	}

	protected ITerm computeResult(ITerm... terms) {
		return ListBuiltinHelper.subList(terms);
	}

}
