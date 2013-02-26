package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.INonNegativeInteger;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * Checks if a term is of type 'NonNegativeInteger'.
 * 
 * @author Adrian Marte
 */
public class IsNonNegativeIntegerBuiltin extends BooleanBuiltin {

	/** The predicate defining this built-in. */
	public static final IPredicate PREDICATE = BASIC.createPredicate(
			"IS_NONNEGATIVEINTEGER", 1);

	/**
	 * Constructor.
	 * 
	 * @param terms
	 *            The list of terms. Must always be of length 1 in this case.
	 */
	public IsNonNegativeIntegerBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected boolean computeResult(ITerm[] terms) {
		return isNonNegativeInteger(terms[0]);
	}

	public static boolean isNonNegativeInteger(ITerm term) {
		return term instanceof INonNegativeInteger;
	}

}
