package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IBooleanTerm;

/**
 * <p>
 * Represents the RIF built-in predicate pred:boolean-less-than.
 * </p>
 */
public class BooleanLessBuiltin extends LessBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"BOOLEAN_LESS", 2);

	public BooleanLessBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		if (terms[0] instanceof IBooleanTerm
				&& terms[1] instanceof IBooleanTerm) {
			return super.computeResult(terms);
		}

		return false;
	}

}
