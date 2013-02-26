package at.sti2.streamingiris.builtins;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Built-in to do regular expression matching.
 */
public class RegexBuiltin extends BooleanBuiltin {
	/**
	 * Constructs a built-in. Two terms must be passed to the constructor, the
	 * second one must be a string with the regular expression pattern.
	 * 
	 * @param terms
	 *            the terms
	 */
	public RegexBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);

		if (terms.length != 2)
			throw new IllegalArgumentException(
					getClass().getSimpleName()
							+ ": Constructor requires exactly two parameters (ITerm term, IStringTerm pattern)");

		if (!(terms[1] instanceof IStringTerm))
			throw new IllegalArgumentException(
					getClass().getSimpleName()
							+ ": The second argument of the constructor must be a string pattern");

		String pattern = (String) terms[1].getValue();

		mPattern = Pattern.compile(pattern);
	}

	protected boolean computeResult(ITerm[] terms) {
		assert terms.length == 2;

		if (terms[0] instanceof IStringTerm) {
			String testString = (String) terms[0].getValue();
			Matcher m = mPattern.matcher(testString);
			boolean result = m.matches();

			return result;
		} else
			return false;
	}

	private final Pattern mPattern;

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = Factory.BASIC.createPredicate(
			"REGEX", 2);
}
