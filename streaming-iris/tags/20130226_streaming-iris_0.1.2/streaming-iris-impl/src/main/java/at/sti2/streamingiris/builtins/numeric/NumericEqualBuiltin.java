package at.sti2.streamingiris.builtins.numeric;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.EqualBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:numeric-equal.
 * </p>
 */
public class NumericEqualBuiltin extends EqualBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"NUMERIC_EQUAL", 2);

	public NumericEqualBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
		if (checkTypes(missingTermIndex, terms, INumericTerm.class)) {
			return super.computeMissingTerm(missingTermIndex, terms);
		}

		return null;
	}

}
