package at.sti2.streamingiris.builtins;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IBooleanTerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Checks if a boolean has the value 'false'.
 */
public class BooleanNotBuiltin extends BooleanBuiltin {
	/**
	 * Constructs a built-in. Two terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param terms
	 *            the terms
	 */
	public BooleanNotBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected boolean computeResult(ITerm[] terms) {
		if (!(terms[0] instanceof IBooleanTerm)) {
			return false;
		} else {
			IBooleanTerm term = (IBooleanTerm) terms[0];
			return term.getValue().booleanValue() == false;
		}
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = Factory.BASIC.createPredicate(
			"BOOLEAN_NOT", 1);
}
