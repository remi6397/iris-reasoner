package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BooleanBuiltin;
import at.sti2.streamingiris.terms.StringTerm;

/**
 * Represents the RIF built-in func:ends-with as described in
 * http://www.w3.org/TR/xpath-functions/#func-ends-with. Restricts the value of
 * collation to Unicode code point collation
 * (http://www.w3.org/2005/xpath-functions/collation/codepoint).
 */
public class StringEndsWithWithoutCollationBuiltin extends BooleanBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"STRING_ENDS_WITH2", 2);

	/**
	 * Constructor.
	 * 
	 * @param terms
	 *            The terms, where the term at the first position is the
	 *            <code>haystack</code> and the term at the second position is
	 *            the <code>needle</code>. The <code>haystack</code> is the
	 *            string being searched for the occurrence of the
	 *            <code>needle</code>. The <code>needle</code> is the string to
	 *            be searched for in the <code>haystack</code>.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringEndsWithWithoutCollationBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		String haystack = null;
		String needle = null;

		if (terms[0] instanceof IStringTerm && terms[1] instanceof StringTerm) {
			haystack = ((IStringTerm) terms[0]).getValue();
			needle = ((IStringTerm) terms[1]).getValue();
		} else {
			return false;
		}

		return StringEndsWithBuiltin.endsWith(haystack, needle, null);
	}

}
