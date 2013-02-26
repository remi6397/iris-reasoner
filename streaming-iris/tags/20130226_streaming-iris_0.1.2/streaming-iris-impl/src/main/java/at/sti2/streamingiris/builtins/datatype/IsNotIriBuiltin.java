package at.sti2.streamingiris.builtins.datatype;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * Checks if a term is not of type 'IRI'.
 */
public class IsNotIriBuiltin extends BooleanBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = at.sti2.streamingiris.factory.Factory.BASIC
			.createPredicate("IS_NOT_IRI", 1);

	/**
	 * Constructor.
	 * 
	 * @param t
	 *            The list of terms. Must always be of length 1 in this case.
	 */
	public IsNotIriBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	protected boolean computeResult(ITerm[] terms) {
		return !IsIriBuiltin.isIri(terms[0]);
	}

}
