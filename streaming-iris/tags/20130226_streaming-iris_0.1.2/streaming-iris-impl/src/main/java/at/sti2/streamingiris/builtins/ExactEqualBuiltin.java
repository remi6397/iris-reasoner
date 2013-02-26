package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 * Built-in to either: a) compare two terms for exact equality, OR b) assign a
 * constant expression to a variable
 * 
 * Two terms are exactly equal if they: a) have exactly the same type, AND b)
 * have the same value
 * 
 * This comparison respects floating point round-off errors.
 */
public class ExactEqualBuiltin extends ArithmeticBuiltin {
	/**
	 * Constructor.
	 * 
	 * @param terms
	 *            The terms, must be two of these
	 */
	public ExactEqualBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
		return terms[missingTermIndex == 0 ? 1 : 0];
	}

	@Override
	protected boolean testForEquality(ITerm t0, ITerm t1) {
		assert t0 != null;
		assert t1 != null;

		return BuiltinHelper.exactlyEqual(t0, t1);
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"EXACT_EQUAL", 2);
}
