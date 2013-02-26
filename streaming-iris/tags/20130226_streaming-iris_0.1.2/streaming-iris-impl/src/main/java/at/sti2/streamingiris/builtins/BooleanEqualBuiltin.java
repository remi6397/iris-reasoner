package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IBooleanTerm;

/**
 * <p>
 * Represents the RIF built-in predicate pred:boolean-equal.
 * </p>
 */
public class BooleanEqualBuiltin extends EqualBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"BOOLEAN_EQUAL", 2);

	public BooleanEqualBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
		if (checkTypes(missingTermIndex, terms, IBooleanTerm.class)) {
			return super.computeMissingTerm(missingTermIndex, terms);
		}

		return null;
	}

}
