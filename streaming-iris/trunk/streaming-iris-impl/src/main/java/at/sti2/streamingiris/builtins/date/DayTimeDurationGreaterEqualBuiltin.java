package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDayTimeDuration;
import at.sti2.streamingiris.builtins.GreaterEqualBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate
 * pred:dayTimeDuration-greater-equal-than.
 * </p>
 */
public class DayTimeDurationGreaterEqualBuiltin extends GreaterEqualBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"DAYTIMEDURATION_GREATER_EQUAL", 2);

	public DayTimeDurationGreaterEqualBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		if (terms[0] instanceof IDayTimeDuration
				&& terms[1] instanceof IDayTimeDuration) {
			return super.computeResult(terms);
		}

		return false;
	}

}
