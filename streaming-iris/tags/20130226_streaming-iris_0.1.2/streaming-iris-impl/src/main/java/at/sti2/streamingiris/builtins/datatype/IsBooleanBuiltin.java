package at.sti2.streamingiris.builtins.datatype;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IBooleanTerm;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * Checks a term for being boolean type.
 */
public class IsBooleanBuiltin extends BooleanBuiltin {
	/**
	 * Constructor.
	 * 
	 * @param t
	 *            The list of terms. Must always be of length 1 in this case.
	 */
	public IsBooleanBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	protected boolean computeResult(ITerm[] terms) {
		return isBoolean(terms[0]);
	}

	public static boolean isBoolean(ITerm term) {
		return term instanceof IBooleanTerm;
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = at.sti2.streamingiris.factory.Factory.BASIC
			.createPredicate("IS_BOOLEAN", 1);
}
