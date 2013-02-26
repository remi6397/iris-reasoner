package at.sti2.streamingiris.builtins.numeric;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.AddBuiltin;
import at.sti2.streamingiris.builtins.BuiltinHelper;

/**
 * <p>
 * Represents the RIF built-in function func:numeric-add.
 * </p>
 */
public class NumericAddBuiltin extends AddBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"NUMERIC_ADD", 3);

	public NumericAddBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
		if (!BuiltinHelper.areNumeric(terms, missingTermIndex)) {
			return null;
		}

		return super.computeMissingTerm(missingTermIndex, terms);
	}

}
