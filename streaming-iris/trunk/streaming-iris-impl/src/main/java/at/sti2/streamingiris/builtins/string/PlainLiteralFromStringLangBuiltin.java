package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.FunctionalBuiltin;

/**
 * Represents the RIF built-in function func:PlainLiteral-from-string-lang.
 */
public class PlainLiteralFromStringLangBuiltin extends FunctionalBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TEXT_FROM_STRING_LANG", 3);

	/**
	 * Constructor. Three terms must be passed to the constructor, otherwise an
	 * exception will be thrown.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws IllegalArgumentException
	 *             If one of the terms is {@code null}.
	 * @throws IllegalArgumentException
	 *             If the number of terms submitted is not 3.
	 * @throws IllegalArgumentException
	 *             If terms is <code>null</code>.
	 */
	public PlainLiteralFromStringLangBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		if (terms[0] instanceof IStringTerm && terms[1] instanceof IStringTerm) {
			String text = ((IStringTerm) terms[0]).getValue();

			// http://www.w3.org/TR/rdf-plain-literal/#plfn:PlainLiteral-from-string-lang
			// Note that, since in the value space of rdf:PlainLiteral language
			// tags are in lowercase, this function converts $arg2 to lowercase
			String lang = ((IStringTerm) terms[1]).getValue().toLowerCase();

			return CONCRETE.createPlainLiteral(text, lang);
		}

		return null;
	}

}
