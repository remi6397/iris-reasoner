package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IYearMonthDuration;
import at.sti2.streamingiris.builtins.GreaterBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:yearMonthDuration-greater-than.
 * </p>
 */
public class YearMonthDurationGreaterBuiltin extends GreaterBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"YEARMONTHDURATION_GREATER", 2);

	public YearMonthDurationGreaterBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		if (terms[0] instanceof IYearMonthDuration
				&& terms[1] instanceof IYearMonthDuration) {
			return super.computeResult(terms);
		}

		return false;
	}

}
