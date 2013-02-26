package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDoubleTerm;
import at.sti2.streamingiris.api.terms.concrete.IYearMonthDuration;
import at.sti2.streamingiris.builtins.MultiplyBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:multiply-yearMonthDuration.
 * </p>
 */
public class YearMonthDurationMultiplyBuiltin extends MultiplyBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"YEARMONTHDURATION_MULTIPLY", 3);

	/**
	 * Creates the built-in for the specified terms.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws NullPointerException
	 *             If one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the number of terms submitted is not 3.
	 */
	public YearMonthDurationMultiplyBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms)
			throws EvaluationException {
		if (terms[0] instanceof IYearMonthDuration
				&& terms[1] instanceof IDoubleTerm) {
			return super.computeMissingTerm(missingTermIndex, terms);
		}

		return null;
	}

}
