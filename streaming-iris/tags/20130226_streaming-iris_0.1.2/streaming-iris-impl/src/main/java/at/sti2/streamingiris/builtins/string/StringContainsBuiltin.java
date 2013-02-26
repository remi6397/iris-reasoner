package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BooleanBuiltin;
import at.sti2.streamingiris.terms.StringTerm;

/**
 * Represents the RIF built-in func:contains as described in
 * http://www.w3.org/TR/xpath-functions/#func-contains. At the moment only
 * Unicode code point collation
 * (http://www.w3.org/2005/xpath-functions/collation/codepoint) is supported.
 */
public class StringContainsBuiltin extends BooleanBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"STRING_CONTAINS3", 3);

	/**
	 * Constructor.
	 * 
	 * @param terms
	 *            The terms, where the term at the first position is the
	 *            <code>haystack</code>, the term at the second position is the
	 *            <code>needle</code> and the term at the third position is the
	 *            collation to be used. The <code>haystack</code> is the string
	 *            being searched for the occurrence of the <code>needle</code>.
	 *            The <code>needle</code> is the string to be searched for in
	 *            the <code>haystack</code>.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringContainsBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		String haystack = null;
		String needle = null;
		String collation = null;

		if (terms[0] instanceof IStringTerm && terms[1] instanceof StringTerm
				&& terms[2] instanceof StringTerm) {
			haystack = ((IStringTerm) terms[0]).getValue();
			needle = ((IStringTerm) terms[1]).getValue();
			collation = ((IStringTerm) terms[2]).getValue();
		} else {
			return false;
		}

		return contains(haystack, needle, collation);
	}

	public static boolean contains(String haystack, String needle,
			String collation) {
		String defaultCollation = "http://www.w3.org/2005/xpath-functions/collation/codepoint";

		// Only "Unicode code point collation" is supported at the moment.
		if (collation != null && !collation.equalsIgnoreCase(defaultCollation)) {
			throw new IllegalArgumentException("Unsupported collation");
		}

		// If the value of haystack is the zero-length string, the function
		// returns false.
		if (haystack.length() == 0) {
			return false;
		}

		return haystack.contains(needle);
	}

}
