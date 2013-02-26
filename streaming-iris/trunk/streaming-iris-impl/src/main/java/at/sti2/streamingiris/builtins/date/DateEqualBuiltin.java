package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTerm;
import at.sti2.streamingiris.builtins.EqualBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:date-equal.
 * </p>
 */
public class DateEqualBuiltin extends EqualBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"DATE_EQUAL", 2);

	public DateEqualBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
		if (checkTypes(missingTermIndex, terms, IDateTerm.class)) {
			return super.computeMissingTerm(missingTermIndex, terms);
		}

		return null;
	}

}
