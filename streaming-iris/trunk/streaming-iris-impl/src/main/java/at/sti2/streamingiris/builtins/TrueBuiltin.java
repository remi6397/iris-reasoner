package at.sti2.streamingiris.builtins;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 * Always TRUE.
 */
public class TrueBuiltin extends BooleanBuiltin {
	/**
	 * Constructor.
	 * 
	 * @param t
	 *            The list of terms. Must always be of length 0 in this case.
	 */
	public TrueBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);
		assert terms.length == 0;
	}

	protected boolean computeResult(ITerm[] terms) {
		assert terms.length == 0;
		return true;
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = at.sti2.streamingiris.factory.Factory.BASIC
			.createPredicate("TRUE", 0);
}
