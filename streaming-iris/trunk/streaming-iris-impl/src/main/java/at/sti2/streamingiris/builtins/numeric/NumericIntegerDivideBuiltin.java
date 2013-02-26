package at.sti2.streamingiris.builtins.numeric;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.math.BigDecimal;
import java.math.BigInteger;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.DivideBuiltin;

/**
 * <p>
 * Represents a integer divide operation, i.e. divides the first argument by the
 * second, and returns the integer obtained by truncating the fractional part of
 * the result. At the evaluation time there must only be one variable left for
 * computation, otherwise an exception will be thrown.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NumericIntegerDivideBuiltin extends DivideBuiltin {

	/** The predicate defining this builtin. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"NUMERIC_INTEGER_DIVIDE", 3);

	/**
	 * Constructs a builtin. Three terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws NullPointerException
	 *             If one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the number of terms submitted is not 3.
	 * @throws NullPointerException
	 *             If <code>t</code> is <code>null</code>.
	 */
	public NumericIntegerDivideBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms)
			throws EvaluationException {
		ITerm result = super.computeMissingTerm(missingTermIndex, terms);

		// Truncate the fractional part of the result.
		if (result != null && result instanceof INumericTerm) {
			BigDecimal value = ((INumericTerm) result).getValue();
			BigInteger truncatedResult = value.toBigInteger();
			result = CONCRETE.createInteger(truncatedResult);
		}

		return result;
	}
}
