package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDayTimeDuration;
import at.sti2.streamingiris.api.terms.concrete.IDoubleTerm;
import at.sti2.streamingiris.builtins.MultiplyBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:multiply-dayTimeDuration.
 * </p>
 */
public class DayTimeDurationMultiplyBuiltin extends MultiplyBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"DAYTIMEDURATION_MULTIPLY", 3);

	public DayTimeDurationMultiplyBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms)
			throws EvaluationException {
		if (terms[0] instanceof IDayTimeDuration
				&& terms[1] instanceof IDoubleTerm) {
			return super.computeMissingTerm(missingTermIndex, terms);
		}

		return null;
	}

}
