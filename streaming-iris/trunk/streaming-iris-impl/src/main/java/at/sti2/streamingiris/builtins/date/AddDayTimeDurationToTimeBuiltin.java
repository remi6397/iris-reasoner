package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDayTimeDuration;
import at.sti2.streamingiris.api.terms.concrete.ITime;
import at.sti2.streamingiris.builtins.AddBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:add-dayTimeDuration-to-time.
 * </p>
 */
public class AddDayTimeDurationToTimeBuiltin extends AddBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"ADD_DAYTIMEDURATION_TO_TIME", 3);

	public AddDayTimeDurationToTimeBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
		if (terms[0] instanceof ITime && terms[1] instanceof IDayTimeDuration) {
			return super.computeMissingTerm(missingTermIndex, terms);
		}

		return null;
	}

}
