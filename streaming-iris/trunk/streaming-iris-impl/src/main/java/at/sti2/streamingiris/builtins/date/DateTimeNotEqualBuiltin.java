package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTime;
import at.sti2.streamingiris.builtins.NotEqualBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:dateTime-not-equal.
 * </p>
 */
public class DateTimeNotEqualBuiltin extends NotEqualBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"DATETIME_NOT_EQUAL", 2);

	public DateTimeNotEqualBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		if (terms[0] instanceof IDateTime && terms[1] instanceof IDateTime) {
			return super.computeResult(terms);
		}

		return false;
	}

}
