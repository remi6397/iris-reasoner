package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.ITime;
import at.sti2.streamingiris.builtins.LessBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:time-less-than.
 * </p>
 */
public class TimeLessBuiltin extends LessBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TIME_LESS", 2);

	public TimeLessBuiltin(ITerm... terms) {
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
