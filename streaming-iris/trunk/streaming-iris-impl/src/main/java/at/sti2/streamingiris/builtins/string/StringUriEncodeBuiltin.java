package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.EncodeForUriBuiltin;
import at.sti2.streamingiris.builtins.FunctionalBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 * Represents a string URI encode operation.
 * 
 * @deprecated Replaced by {@link EncodeForUriBuiltin}.
 */
@Deprecated
public class StringUriEncodeBuiltin extends FunctionalBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"STRING_URI_ENCODE", 2);

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
	public StringUriEncodeBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		if (terms[0] instanceof IStringTerm) {
			IStringTerm string = (IStringTerm) terms[0];

			/*
			 * We use our own URL encoding mechanism, since there are a few
			 * problems with the URLEncoder of the Java API.
			 */
			PercentEncoder encoder = new PercentEncoder();
			String result = encoder.encode(string.getValue());

			return Factory.TERM.createString(result);
		}

		return null;
	}

}
