package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IToken;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of Token.
 * </p>
 * 
 * @author Adrian Marte
 */
public class Token extends NormalizedString implements IToken {

	private static String[] removePatterns = new String[] { "[\\x20]{2,}" };

	/**
	 * Creates a new Token for the specified string. The string is normalized if
	 * it is not normalized already.
	 * 
	 * @param string
	 *            The normalized or non-normalized string.
	 */
	public Token(String string) {
		super(string);
		value = normalize(value);
	}

	public static String normalize(String string) {
		// Remove carriage-returns, line-feeds and tabs.
		String normalizedString = NormalizedString.normalize(string);

		// Remove any sequence of two or more spaces
		for (String pattern : removePatterns) {
			normalizedString = normalizedString.replaceAll(pattern, " ");
		}

		// Remove leading or trailing spaces.
		return normalizedString.trim();
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.TOKEN.toUri();
	}

}
