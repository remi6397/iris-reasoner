package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.FunctionalBuiltin;
import at.sti2.streamingiris.factory.Factory;
import at.sti2.streamingiris.terms.StringTerm;

/**
 * Represents the RIF built-in func:substring-before operation as described in
 * http://www.w3.org/TR/xpath-functions/#func-substring-before. Restricts the
 * value of collation to Unicode code point collation
 * (http://www.w3.org/2005/xpath-functions/collation/codepoint).
 */
public class StringSubstringBeforeWithoutCollationBuiltin extends
		FunctionalBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"STRING_SUBSTRING_BEFORE2", 3);

	/**
	 * Constructor.
	 * 
	 * @param terms
	 *            The terms, where the term at the first position is the
	 *            <code>haystack</code>, the term at the second position is the
	 *            <code>needle</code> and the term at the last position
	 *            represents the result. The <code>haystack</code> is the string
	 *            being searched for the occurrence of the <code>needle</code>.
	 *            The <code>needle</code> is the string to be searched for in
	 *            the <code>haystack</code>.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringSubstringBeforeWithoutCollationBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		String haystack = null;
		String needle = null;

		if (terms[0] instanceof IStringTerm && terms[1] instanceof StringTerm) {
			haystack = ((IStringTerm) terms[0]).getValue();
			needle = ((IStringTerm) terms[1]).getValue();
		} else {
			return null;
		}

		String result = StringSubstringBeforeBuiltin.substring(haystack,
				needle, null);

		if (result != null) {
			return Factory.TERM.createString(result);
		}

		return null;
	}

}
