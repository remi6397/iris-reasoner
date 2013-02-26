package at.sti2.streamingiris.builtins.datatype;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTimeStamp;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * <p>
 * Checks whether a term is not of type dateTimeStamp.
 * </p>
 * 
 * @author Adrian Marte
 */
public class IsNotDateTimeStampBuiltin extends BooleanBuiltin {

	public IsNotDateTimeStampBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected boolean computeResult(ITerm[] terms) {
		return isNotDateTimeStamp(terms[0]);
	}

	public static boolean isNotDateTimeStamp(ITerm term) {
		return !(term instanceof IDateTimeStamp);
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = at.sti2.streamingiris.factory.Factory.BASIC
			.createPredicate("IS_NOT_DATETIMESTAMP", 1);

}
