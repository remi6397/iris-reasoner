package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.FunctionalBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 * Represents a string substring operation, but restricts the endIndex to
 * <code>string.length - 1</code>.
 * 
 * @author gigi
 * @author Adrian Marte
 */
public class StringSubstringUntilEndBuiltin extends FunctionalBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"STRING_SUBSTRING2", 3);

	/**
	 * Constructor.
	 * 
	 * @param terms
	 *            The terms, where the first term is the string, the second term
	 *            is the begin index and the third term represents the result.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringSubstringUntilEndBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		// FIXME value spaces of xs:integer, xs:double, xs:float and xs:decimal
		// for numeric terms

		if (terms[0] instanceof IStringTerm && terms[1] instanceof INumericTerm) {
			IStringTerm string = (IStringTerm) terms[0];
			INumericTerm beginIndex = (INumericTerm) terms[1];

			String substring = string.getValue().substring(
					beginIndex.getValue().intValue());

			return Factory.TERM.createString(substring);
		}

		return null;
	}

}
