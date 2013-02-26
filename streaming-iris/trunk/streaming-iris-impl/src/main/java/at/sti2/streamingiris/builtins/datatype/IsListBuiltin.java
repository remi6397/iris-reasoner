package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IList;
import at.sti2.streamingiris.builtins.BooleanBuiltin;
import at.sti2.streamingiris.builtins.list.ListBuiltinHelper;

/**
 * <p>
 * Represents the RIF built-in predicate pred:is-list.
 * </p>
 * <p>
 * Checks if a given {@link ITerm} is an instance of {@link IList}.
 * </p>
 * 
 * @see <a href=
 *      "http://www.w3.org/2005/rules/wiki/DTB#Functions_and_Predicates_on_RIF_Lists"
 *      >http://www.w3.org/2005/rules/wiki/DTB#Functions_and_Predicates_on_RIF_Lists</a>
 * 
 */
public class IsListBuiltin extends BooleanBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"IS_LIST", 1);

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
	public IsListBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected boolean computeResult(ITerm[] terms) {
		return ListBuiltinHelper.isList(terms[0]);
	}

}
