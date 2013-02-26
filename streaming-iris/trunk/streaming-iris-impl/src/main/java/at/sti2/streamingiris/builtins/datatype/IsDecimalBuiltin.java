package at.sti2.streamingiris.builtins.datatype;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDecimalTerm;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * <p>
 * Checks whether a term is a decimal.
 * </p>
 * <p>
 * $Id: IsDecimalBuiltin.java,v 1.2 2007-10-12 12:40:58 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.2 $
 * @since 0.4
 */
public class IsDecimalBuiltin extends BooleanBuiltin {

	public IsDecimalBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	protected boolean computeResult(ITerm[] terms) {
		return isDecimal(terms[0]);
	}

	public static boolean isDecimal(ITerm term) {
		return term instanceof IDecimalTerm;
	}

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = at.sti2.streamingiris.factory.Factory.BASIC
			.createPredicate("IS_DECIMAL", 1);
}
