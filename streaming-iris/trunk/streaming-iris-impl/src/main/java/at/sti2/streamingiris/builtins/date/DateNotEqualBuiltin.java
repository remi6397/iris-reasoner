package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTerm;
import at.sti2.streamingiris.builtins.NotEqualBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:date-not-equal.
 * </p>
 */
public class DateNotEqualBuiltin extends NotEqualBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"DATE_NOT_EQUAL", 2);

	public DateNotEqualBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		if (terms[0] instanceof IDateTerm && terms[1] instanceof IDateTerm) {
			return super.computeResult(terms);
		}

		return false;
	}

}
