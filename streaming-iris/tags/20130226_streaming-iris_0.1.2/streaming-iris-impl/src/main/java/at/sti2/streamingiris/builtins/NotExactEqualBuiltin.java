package at.sti2.streamingiris.builtins;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Built-in to compare two terms for exact inequality. Two terms are exactly not
 * equal if they either: a) have different types b) have the same type, but have
 * different values. This comparison respects floating point round-off errors.
 */
public class NotExactEqualBuiltin extends BooleanBuiltin {
	/**
	 * Constructs a built-in. Two terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param terms
	 *            the terms
	 */
	public NotExactEqualBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);
		assert terms.length == 2;
	}

	protected boolean computeResult(ITerm[] terms) {
		assert terms.length == 2;

		return !BuiltinHelper.exactlyEqual(terms[0], terms[1]);
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = Factory.BASIC.createPredicate(
			"NOT_EXACT_EQUAL", 2);
}
