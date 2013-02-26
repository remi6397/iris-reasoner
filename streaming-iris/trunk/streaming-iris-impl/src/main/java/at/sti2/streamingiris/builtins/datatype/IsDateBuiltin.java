package at.sti2.streamingiris.builtins.datatype;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTerm;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * <p>
 * Checks whether a term is a date.
 * </p>
 * <p>
 * $Id: IsDateBuiltin.java,v 1.3 2007-10-12 12:40:58 bazbishop237 Exp $
 * </p>
 * 
 * @author Nathalie Steinmetz, DERI Innsbruck
 * @version $Revision: 1.3 $
 * @since 0.4
 */
public class IsDateBuiltin extends BooleanBuiltin {

	public IsDateBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	protected boolean computeResult(ITerm[] terms) {
		return isDate(terms[0]);
	}

	public static boolean isDate(ITerm term) {
		return term instanceof IDateTerm;
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = at.sti2.streamingiris.factory.Factory.BASIC
			.createPredicate("IS_DATE", 1);

}
