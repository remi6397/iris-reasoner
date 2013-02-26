package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.FunctionalBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 * Represents the RIF built-in func:replace as described in
 * http://www.w3.org/TR/xpath-functions/#func-replace, but restricts the flags
 * to empty flags.
 */
public class StringReplaceWithoutFlagsBuiltin extends FunctionalBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"STRING_REPLACE3", 4);

	/**
	 * Constructor.
	 * 
	 * @param term
	 *            The terms, where the term at the first position is the string,
	 *            the term at the second position is the regex, the term at the
	 *            third position is the replacement and the term at the fourth
	 *            position represents the result. The string is the string to
	 *            apply the replace operation on. The regex is the regular
	 *            expression. The replacement is the replacement for the
	 *            matching substrings.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringReplaceWithoutFlagsBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		String string = null;
		String regex = null;
		String replacement = null;
		String flags = "";

		if (terms[0] instanceof IStringTerm && terms[1] instanceof IStringTerm
				&& terms[2] instanceof IStringTerm) {
			string = ((IStringTerm) terms[0]).getValue();
			regex = ((IStringTerm) terms[1]).getValue();
			replacement = ((IStringTerm) terms[2]).getValue();
		} else {
			return null;
		}

		String result = StringReplaceBuiltin.replace(string, regex,
				replacement, flags);

		if (result != null) {
			return Factory.TERM.createString(result);
		}

		return null;
	}

}
