package at.sti2.streamingiris.builtins.datatype;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * <p>
 * Checks if a term is not a datetime.
 * </p>
 * <p>
 * $Id: IsDateTimeBuiltin.java,v 1.3 2007-10-12 12:40:58 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.3 $
 * @since 0.4
 */
public class IsNotDateTimeBuiltin extends BooleanBuiltin {

	public IsNotDateTimeBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	protected boolean computeResult(ITerm[] terms) {
		return !IsDateTimeBuiltin.isDateTime(terms[0]);
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = at.sti2.streamingiris.factory.Factory.BASIC
			.createPredicate("IS_NOT_DATETIME", 1);

}
