package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.FunctionalBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 * Represents the RIF built-in func:encode-html-uri as defined in
 * http://www.w3.org/TR/xpath-functions/#func-escape-html-uri.
 */
public class StringEscapeHtmlUriBuiltin extends FunctionalBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"STRING_ESCAPE_HTML_URI", 2);

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
	public StringEscapeHtmlUriBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		if (terms[0] instanceof IStringTerm) {
			IStringTerm string = (IStringTerm) terms[0];

			PercentEncoder encoder = new PercentEncoder();
			encoder.reserveAll();

			/*
			 * This function escapes all characters except printable characters
			 * of the US-ASCII coded character set, specifically the octets
			 * ranging from 32 to 126.
			 */
			for (char i = 32; i <= 126; i++) {
				encoder.unreserve(i);
			}

			String result = encoder.encode(string.getValue());

			return Factory.TERM.createString(result);
		}

		return null;
	}

}
