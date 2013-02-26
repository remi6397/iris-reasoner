package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.FunctionalBuiltin;
import at.sti2.streamingiris.builtins.datatype.ToStringBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 * Represents the RIF built-in func:concat as defined in http://www.w3.org/2005
 * /rules/wiki/DTB#func:concat_.28adapted_from_fn:concat.29.
 */
public class StringConcatBuiltin extends FunctionalBuiltin {

	private static final String PREDICATE_STRING = "STRING_CONCAT";
	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			PREDICATE_STRING, -1);

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
	public StringConcatBuiltin(ITerm... terms) {
		// FIXME dw2ad: correct?
		// accept any amount of terms, at least 3
		super(BASIC.createPredicate(PREDICATE_STRING, terms.length), terms);

		if (terms.length < 3) {
			throw new IllegalArgumentException("The amount of terms <"
					+ terms.length + "> must at least 3");
		}
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {

		StringBuilder buffer = new StringBuilder();

		int endIndex = terms.length - 1;
		for (int i = 0; i < endIndex; i++) {
			IStringTerm string = ToStringBuiltin.toString(terms[i]);

			if (string == null)
				return null;

			buffer.append(string.getValue());
		}

		return Factory.TERM.createString(buffer.toString());
	}

}
