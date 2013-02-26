package at.sti2.streamingiris.builtins;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * <p>
 * Built-in to compare two terms and determine which one is bigger.
 * </p>
 * <p>
 * $Id: GreaterBuiltin.java,v 1.15 2007-10-12 12:40:58 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.15 $
 */
public class GreaterBuiltin extends BooleanBuiltin {

	/**
	 * Construct a new GreaterBuiltin for the specific predicate and terms.
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
	protected GreaterBuiltin(IPredicate predicate, ITerm... terms) {
		super(predicate, terms);
	}

	/**
	 * Constructs a builtin. Two terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws NullPointerException
	 *             If the predicate or one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the length of the terms and the arity of the predicate do
	 *             not match.
	 */
	public GreaterBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	protected boolean computeResult(ITerm[] terms) {
		return BuiltinHelper.less(terms[1], terms[0]);
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = Factory.BASIC.createPredicate(
			"GREATER", 2);
}
