package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IPlainLiteral;
import at.sti2.streamingiris.builtins.BuiltinHelper;
import at.sti2.streamingiris.builtins.FunctionalBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 * Represents the RIF built-in function func:PlainLiteral-compare.
 */
public class PlainLiteralCompareBuiltin extends FunctionalBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TEXT_COMPARE", 3);

	/**
	 * Constructor. Three terms must be passed to the constructor, otherwise an
	 * exception will be thrown.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws IllegalArgumentException
	 *             If one of the terms is {@code null}.
	 * @throws IllegalArgumentException
	 *             If the number of terms submitted is not 3.
	 * @throws IllegalArgumentException
	 *             If terms is <code>null</code>.
	 */
	public PlainLiteralCompareBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		if (terms[0] instanceof IPlainLiteral) {
			int result = BuiltinHelper.compare(terms[0], terms[1]);
			return Factory.CONCRETE.createInteger(result);
		}

		return null;
	}

}
