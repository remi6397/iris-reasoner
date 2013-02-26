package at.sti2.streamingiris.builtins.datatype;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTime;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * <p>
 * Checks whether a term is a datetime.
 * </p>
 * <p>
 * $Id: IsDateTimeBuiltin.java,v 1.3 2007-10-12 12:40:58 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.3 $
 * @since 0.4
 */
public class IsDateTimeBuiltin extends BooleanBuiltin {

	public IsDateTimeBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	protected boolean computeResult(ITerm[] terms) {
		return isDateTime(terms[0]);
	}

	public static boolean isDateTime(ITerm term) {
		return term instanceof IDateTime;
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = at.sti2.streamingiris.factory.Factory.BASIC
			.createPredicate("IS_DATETIME", 1);

}
