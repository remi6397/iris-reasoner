package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.ILanguage;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of a language tag.
 * </p>
 * 
 * @author Adrian Marte
 */
public class Language extends Token implements ILanguage {

	/**
	 * Defines the pattern of all conformant language tags.
	 */
	private static String pattern = "[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*";

	/**
	 * Creates a new Language instance for the specified language tag and checks
	 * for validity of the language tag.
	 * 
	 * @param language
	 *            The language tag.
	 * @throws IllegalArgumentException
	 *             If the specified language is no valid language tag.
	 */
	public Language(String language) {
		this(language, true);
	}

	/**
	 * Creates a new Language instance for the specified language tag.
	 * 
	 * @param language
	 *            The language tag.
	 * @param isValidating
	 *            If set to true the specified language is tested for validity.
	 * @throws IllegalArgumentException
	 *             If isValidating is set to true and the specified language is
	 *             no valid language tag.
	 */
	public Language(String language, boolean isValidating)
			throws IllegalArgumentException {
		super(language);

		if (isValidating && !validate(value)) {
			throw new IllegalArgumentException("Invalid language tag");
		}
	}

	public static boolean validate(String language) {
		return language.matches(pattern);
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.LANGUAGE.toUri();
	}

}
