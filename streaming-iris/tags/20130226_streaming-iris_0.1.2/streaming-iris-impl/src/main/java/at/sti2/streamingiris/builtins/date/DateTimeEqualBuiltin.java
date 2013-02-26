package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTime;
import at.sti2.streamingiris.builtins.EqualBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:dateTime-equal.
 * </p>
 */
public class DateTimeEqualBuiltin extends EqualBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"DATETIME_EQUAL", 2);

	public DateTimeEqualBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
		if (checkTypes(missingTermIndex, terms, IDateTime.class)) {
			return super.computeMissingTerm(missingTermIndex, terms);
		}

		return null;
	}

}
