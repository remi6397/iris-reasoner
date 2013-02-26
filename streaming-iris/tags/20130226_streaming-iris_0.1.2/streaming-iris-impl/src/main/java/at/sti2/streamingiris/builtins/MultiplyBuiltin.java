package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 * <p>
 * Represents a multiply operation. In at the evaluation time there must be only
 * one variable be left for computation, otherwise an exception will be thrown.
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.15 $
 */
public class MultiplyBuiltin extends ArithmeticBuiltin {

	/** The predicate defining this builtin. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"MULTIPLY", 3);

	/**
	 * Construct a new MultiplyBuiltin for the specific predicate and terms.
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
	protected MultiplyBuiltin(IPredicate predicate, ITerm... terms) {
		super(predicate, terms);
	}

	/**
	 * Constructs a builtin. Three terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param terms
	 *            the terms
	 * @throws NullPointerException
	 *             If the predicate or one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the length of the terms and the arity of the predicate do
	 *             not match.
	 */
	public MultiplyBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms)
			throws EvaluationException {
		switch (missingTermIndex) {
		case 0:
			return BuiltinHelper.divide(terms[2], terms[1]);

		case 1:
			return BuiltinHelper.divide(terms[2], terms[0]);

		default:
			return BuiltinHelper.multiply(terms[0], terms[1]);
		}
	}
}
