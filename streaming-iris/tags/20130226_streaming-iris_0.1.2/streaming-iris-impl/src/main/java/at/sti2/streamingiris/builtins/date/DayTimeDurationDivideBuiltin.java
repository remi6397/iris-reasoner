package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDayTimeDuration;
import at.sti2.streamingiris.api.terms.concrete.IDoubleTerm;
import at.sti2.streamingiris.builtins.DivideBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:divide-dayTimeDuration.
 * </p>
 */
public class DayTimeDurationDivideBuiltin extends DivideBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"DAYTIMEDURATION_DIVIDE", 3);

	public DayTimeDurationDivideBuiltin(ITerm... terms) {
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
