package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTime;
import at.sti2.streamingiris.builtins.SubtractBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:subtract-dateTimes.
 * </p>
 */
public class DateTimeSubtractBuiltin extends SubtractBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"DATETIME_SUBTRACT", 3);

	public DateTimeSubtractBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
		if (terms[0] instanceof IDateTime && terms[1] instanceof IDateTime) {
			return super.computeMissingTerm(missingTermIndex, terms);
		}

		return null;
	}

}
