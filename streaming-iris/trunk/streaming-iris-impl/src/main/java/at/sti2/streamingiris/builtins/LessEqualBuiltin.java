package at.sti2.streamingiris.builtins;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Builtin to compare two terms.
 */
public class LessEqualBuiltin extends BooleanBuiltin {
	/**
	 * Construct a new LessEqualBuiltin for the specific predicate and terms.
	 * 
	 * @param predicate
	 *            The predicate of the built-in.
	 * @param terms
	 *            The terms.
	 * @throws NullPointerException
	 *             If the predicate or one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the length of the terms and the arity of the predicate do
	 *             not match.
	 */
	protected LessEqualBuiltin(IPredicate predicate, ITerm... terms) {
		super(predicate, terms);
	}

	/**
	 * Constructor.
	 * 
	 * @param terms
	 *            The terms, must be of length 2
	 * @throws NullPointerException
	 *             If the predicate or one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the length of the terms and the arity of the predicate do
	 *             not match.
	 */
	public LessEqualBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected boolean computeResult(ITerm[] terms) {
		return BuiltinHelper.lessEquals(terms[0], terms[1]);
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = Factory.BASIC.createPredicate(
			"LESS_EQUAL", 2);
}
