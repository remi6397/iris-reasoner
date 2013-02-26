package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IList;
import at.sti2.streamingiris.builtins.BooleanBuiltin;
import at.sti2.streamingiris.builtins.list.ListBuiltinHelper;

/**
 * Checks if a term is not an instance of {@link IList}.
 */
public class IsNotListBuiltin extends BooleanBuiltin {

	/** The predicate for this built-in. */
	public static final IPredicate PREDICATE = BASIC.createPredicate(
			"IS_NOT_LIST", 1);

	/**
	 * Constructor.
	 * 
	 * @param terms
	 *            The list of terms. Must always be of length 1.
	 */
	public IsNotListBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected boolean computeResult(ITerm[] terms) {
		return !ListBuiltinHelper.isList(terms[0]);
	}

}
