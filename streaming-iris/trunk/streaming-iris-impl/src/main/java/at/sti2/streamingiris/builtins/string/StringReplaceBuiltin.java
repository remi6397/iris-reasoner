package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.FunctionalBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 * Represents the RIF built-in func:replace as described in
 * http://www.w3.org/TR/xpath-functions/#func-replace.
 */
public class StringReplaceBuiltin extends FunctionalBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"STRING_REPLACE4", 5);

	/**
	 * Constructor.
	 * 
	 * @param term
	 *            The terms, where the term at the first position is the string,
	 *            the term at the second position is the regex, the term at the
	 *            third position is the replacement and the term at the fourth
	 *            position are the flags and the term at the fifth position
	 *            represents the result. The string is the string to apply the
	 *            replace operation on. The regex is the regular expression. The
	 *            replacement is the replacement for the matching substrings.
	 *            The flags are the flags as described in
	 *            http://www.w3.org/TR/xpath-functions/#flags.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringReplaceBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		String string = null;
		String regex = null;
		String replacement = null;
		String flags = "";

		if (terms[0] instanceof IStringTerm && terms[1] instanceof IStringTerm
				&& terms[2] instanceof IStringTerm
				&& terms[3] instanceof IStringTerm) {
			string = ((IStringTerm) terms[0]).getValue();
			regex = ((IStringTerm) terms[1]).getValue();
			replacement = ((IStringTerm) terms[2]).getValue();
			flags = ((IStringTerm) terms[3]).getValue();
		} else {
			return null;
		}

		String result = replace(string, regex, replacement, flags);

		if (result != null) {
			return Factory.TERM.createString(result);
		}

		return null;
	}

	static String replace(String string, String regex, String replacement,
			String flags) {
		int flag = 0;

		// See http://www.w3.org/TR/xpath-functions/#flags.
		if (flags.contains("s")) {
			flag |= Pattern.DOTALL;
		}

		if (flags.contains("m")) {
			flag |= Pattern.MULTILINE;
		}

		if (flags.contains("i")) {
			flag |= Pattern.UNICODE_CASE;
		}

		if (flags.contains("x")) {
			/*
			 * Naive approach would be to use Pattern.COMMENTS but whit this
			 * flag set, whitespace elimination in character class expression
			 * may lead to errors, e.g. the pattern hello[ ]world with COMMENT
			 * mode on results in a character class with no elements and
			 * therefore results in an exception.
			 */

			// Sort this array in order to enable binary search.
			int[] whitespaces = { 0x9, 0xA, 0xD, 0x20 };
			StringBuffer buffer = new StringBuffer();

			int brackets = 0;

			for (int i = 0; i < regex.length(); i++) {
				char character = regex.charAt(i);

				/*
				 * With this mechanism brackets is 0 if and only if the current
				 * character is outside a character class.
				 */
				if (character == '[') {
					brackets++;
				} else if (character == ']') {
					brackets--;
				}

				if (Arrays.binarySearch(whitespaces, character) >= 0
						&& brackets == 0) {
				} else {
					buffer.append(character);
				}
			}

			regex = buffer.toString();
		}

		Pattern pattern = Pattern.compile(regex, flag);
		Matcher matcher = pattern.matcher(string);

		return matcher.replaceAll(replacement);
	}

}
