package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDuration;
import at.sti2.streamingiris.builtins.NotEqualBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:duration-not-equal.
 * </p>
 */
public class DurationNotEqualBuiltin extends NotEqualBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"DURATION_NOT_EQUAL", 2);

	public DurationNotEqualBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		if (terms[0] instanceof IDuration && terms[1] instanceof IDuration) {
			return super.computeResult(terms);
		}

		return false;
	}

}
