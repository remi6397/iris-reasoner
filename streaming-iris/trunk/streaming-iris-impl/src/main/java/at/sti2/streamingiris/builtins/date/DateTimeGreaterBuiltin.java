package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTime;
import at.sti2.streamingiris.builtins.GreaterBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:dateTime-greater-than.
 * </p>
 */
public class DateTimeGreaterBuiltin extends GreaterBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"DATETIME_GREATER", 2);

	public DateTimeGreaterBuiltin(ITerm... terms) {
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
