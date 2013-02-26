package at.sti2.streamingiris.builtins.datatype;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * Checks whether two terms have exactly the same type.
 */
public class SameTypeBuiltin extends BooleanBuiltin {
	/**
	 * Constructor.
	 * 
	 * @param terms
	 *            List of exactly one term.
	 */
	public SameTypeBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected boolean computeResult(ITerm[] terms) {
		return terms[0].getClass() == terms[1].getClass();
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = at.sti2.streamingiris.factory.Factory.BASIC
			.createPredicate("SAME_TYPE", 2);
}
