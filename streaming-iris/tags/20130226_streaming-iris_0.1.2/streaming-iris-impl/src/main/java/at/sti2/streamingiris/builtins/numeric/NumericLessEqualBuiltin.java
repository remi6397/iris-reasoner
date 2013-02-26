package at.sti2.streamingiris.builtins.numeric;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.LessEqualBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:numeric-less-equal-than.
 * </p>
 */
public class NumericLessEqualBuiltin extends LessEqualBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"NUMERIC_LESS_EQUAL", 2);

	public NumericLessEqualBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		if (terms[0] instanceof INumericTerm
				&& terms[1] instanceof INumericTerm) {
			return super.computeResult(terms);
		}

		return false;
	}

}
