package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.BASIC;


import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IConcreteTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.string.PercentEncoder;
import at.sti2.streamingiris.factory.Factory;

/**
 * Represents the RIF built-in function func:encode-for-uri.
 */
public class EncodeForUriBuiltin extends FunctionalBuiltin {

	/** The predicate for this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"ENCODE_FOR_URI", 2);

	/**
	 * Constructor. Two terms must be passed to the constructor, otherwise an
	 * exception will be thrown. The last term represents the result of this
	 * built-in.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws IllegalArgumentException
	 *             If one of the terms is {@code null}.
	 * @throws IllegalArgumentException
	 *             If the number of terms submitted is not 2.
	 * @throws IllegalArgumentException
	 *             If terms is <code>null</code>.
	 */
	public EncodeForUriBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		if (terms[0] instanceof IConcreteTerm) {
			IConcreteTerm concreteTerm = (IConcreteTerm) terms[0];
			String canonicalString = concreteTerm.toCanonicalString();

			PercentEncoder encoder = new PercentEncoder();
			String encodedString = encoder.encode(canonicalString);

			return Factory.TERM.createString(encodedString);
		}

		return null;
	}

}
