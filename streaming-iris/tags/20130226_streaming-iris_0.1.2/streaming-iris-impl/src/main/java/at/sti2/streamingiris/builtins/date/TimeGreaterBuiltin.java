package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.ITime;
import at.sti2.streamingiris.builtins.GreaterBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:time-greater-than.
 * </p>
 */
public class TimeGreaterBuiltin extends GreaterBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TIME_GREATER", 2);

	public TimeGreaterBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		if (terms[0] instanceof ITime && terms[1] instanceof ITime) {
			return super.computeResult(terms);
		}

		return false;
	}

}
