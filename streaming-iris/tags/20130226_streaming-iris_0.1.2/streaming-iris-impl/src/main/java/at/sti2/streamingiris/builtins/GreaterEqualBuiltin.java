package at.sti2.streamingiris.builtins;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * <p>
 * Built-in to compare two terms and determine which one is bigger or if they
 * are equal.
 * </p>
 * <p>
 * $Id: GreaterEqualBuiltin.java,v 1.15 2007-10-12 12:40:58 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.15 $
 */
public class GreaterEqualBuiltin extends BooleanBuiltin {
	/**
	 * Construct a new GreaterEqualBuiltin for the specific predicate and terms.
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
	protected GreaterEqualBuiltin(IPredicate predicate, ITerm... terms) {
		super(predicate, terms);
	}

	/**
	 * Constructs a built-in. Two terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param t
	 *            the terms
	 * @throws NullPointerException
	 *             if one of the terms is null
	 * @throws IllegalArgumentException
	 *             if the number of terms submitted is not 2
	 * @throws NullPointerException
	 *             if t is <code>null</code>
	 */
	public GreaterEqualBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	protected boolean computeResult(ITerm[] terms) {
		return BuiltinHelper.lessEquals(terms[1], terms[0]);
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = Factory.BASIC.createPredicate(
			"GREATER_EQUAL", 2);
}
