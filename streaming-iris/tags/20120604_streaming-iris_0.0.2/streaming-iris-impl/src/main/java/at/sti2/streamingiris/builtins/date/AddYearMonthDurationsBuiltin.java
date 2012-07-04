package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;


import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IYearMonthDuration;
import at.sti2.streamingiris.builtins.AddBuiltin;

/**
 * <p>
 * Represents the RIF built-in function func:add-yearMonthDurations.
 * </p>
 */
public class AddYearMonthDurationsBuiltin extends AddBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"ADD_YEARMONTHDURATIONS", 3);

	public AddYearMonthDurationsBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
		if (terms[0] instanceof IYearMonthDuration
				&& terms[1] instanceof IYearMonthDuration) {
			return super.computeMissingTerm(missingTermIndex, terms);
		}

		return null;
	}

}
