package at.sti2.streamingiris.builtins.datatype;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * <p>
 * Checks if a term is not a date.
 * </p>
 * <p>
 * $Id: IsDateBuiltin.java,v 1.3 2007-10-12 12:40:58 bazbishop237 Exp $
 * </p>
 * 
 * @author Nathalie Steinmetz, DERI Innsbruck
 * @version $Revision: 1.3 $
 * @since 0.4
 */
public class IsNotDateBuiltin extends BooleanBuiltin {

	public IsNotDateBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	protected boolean computeResult(ITerm[] terms) {
		return !IsDateBuiltin.isDate(terms[0]);
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = at.sti2.streamingiris.factory.Factory.BASIC
			.createPredicate("IS_NOT_DATE", 1);

}
