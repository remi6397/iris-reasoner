package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * Represents the RIF built-in func:matches as described in
 * http://www.w3.org/TR/xpath-functions/#func-matches., but restricts the flags
 * to empty flags.
 */
public class StringMatchesWithoutFlagsBuiltin extends BooleanBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"STRING_MATCHES2", 2);

	/**
	 * Constructor.
	 * 
	 * @param terms
	 *            The terms, where the term at the first position is the string,
	 *            the terms at the second position is the pattern and the term
	 *            at the third position represents the flags. The string is the
	 *            string the regular expression is being matched against. The
	 *            patterns is the string representing the regular expression.
	 *            The flags are the flags as described in
	 *            http://www.w3.org/TR/xpath-functions/#flags.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringMatchesWithoutFlagsBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		String string = null;
		String pattern = null;
		String flags = "";

		if (terms[0] instanceof IStringTerm && terms[1] instanceof IStringTerm) {
			string = ((IStringTerm) terms[0]).getValue();
			pattern = ((IStringTerm) terms[1]).getValue();
		} else {
			return false;
		}

		return StringMatchesBuiltin.matches(string, pattern, flags);
	}

}
