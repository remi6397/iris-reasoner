package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.FunctionalBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 * Represents the RIF built-in func:iri-to-uri operation as defined in
 * http://www.w3.org/2005/
 * rules/wiki/DTB#func:iri-to-uri_.28adapted_from_fn:iri-to-uri.29.
 */
public class StringIriToUriBuiltin extends FunctionalBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"STRING_IRI_TO_URI", 2);

	/**
	 * Constructor. Two terms must be passed to the constructor, otherwise an
	 * exception will be thrown.
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
	public StringIriToUriBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		if (terms[0] instanceof IStringTerm) {
			String string = ((IStringTerm) terms[0]).getValue();

			PercentEncoder encoder = new PercentEncoder();
			encoder.unreserve(new char[] { '%', '/', '#', ':' });

			String result = encoder.encode(string);
			return Factory.TERM.createString(result);
		}

		return null;
	}

}
