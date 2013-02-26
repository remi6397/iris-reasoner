package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * Checks if a term is not of type 'NMTOKEN'.
 * 
 * @author Adrian Marte
 */
public class IsNotNMTOKENBuiltin extends BooleanBuiltin {

	/** The predicate defining this built-in. */
	public static final IPredicate PREDICATE = BASIC.createPredicate(
			"IS_NOT_NMTOKEN", 1);

	/**
	 * Constructor.
	 * 
	 * @param terms
	 *            The list of terms. Must always be of length 1 in this case.
	 */
	public IsNotNMTOKENBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected boolean computeResult(ITerm[] terms) {
		return !IsNMTOKENBuiltin.isNMTOKEN(terms[0]);
	}

}
