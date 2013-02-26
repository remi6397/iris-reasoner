package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 * <p>
 * Represents an x = max(y,z) operation. At evaluation time only the result can
 * be unknown.
 * </p>
 * <p>
 * The syntax in Datalog will be, e.g. p(?max) :- q(?x,?y), MAX(?x, ?y, ?max).
 * </p>
 */
public class MaxBuiltin extends ArithmeticBuiltin {
	/**
	 * Constructor. Three terms must be passed to the constructor, otherwise an
	 * exception will be thrown.
	 * 
	 * @param t
	 *            the terms
	 */
	public MaxBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);

		if (terms.length != 3)
			throw new IllegalArgumentException(getClass().getSimpleName()
					+ ": Constructor requires exactly three parameters");
	}

	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
		switch (missingTermIndex) {
		case 0:
			return null;

		case 1:
			return null;

		default:
			if (BuiltinHelper.less(terms[0], terms[1]))
				return terms[1];
			else
				return terms[0];
		}
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate("MAX", 3);
}
