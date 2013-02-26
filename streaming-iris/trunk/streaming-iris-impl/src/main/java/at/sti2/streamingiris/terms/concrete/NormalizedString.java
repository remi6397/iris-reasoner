package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.INormalizedString;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of NormalizedString.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NormalizedString implements INormalizedString {

	protected String value;

	private static String[] removePatterns = new String[] { "\\t", "\\n", "\\r" };

	/**
	 * Creates a new NormalizedString instance. The specified string is
	 * normalized if it is not normalized already.
	 * 
	 * @param string
	 *            The normalized or non-normalized string.
	 */
	public NormalizedString(String string) {
		value = normalize(string);
	}

	public static String normalize(String string) {
		String normalizedString = string;

		for (String pattern : removePatterns) {
			normalizedString = normalizedString.replaceAll(pattern, "");
		}

		return normalizedString;
	}

	public String getValue() {
		return value;
	}

	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.NORMALIZED_STRING.toUri();
	}

	public String toCanonicalString() {
		return value;
	}

	@Override
	public String toString() {
		return toCanonicalString();
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(ITerm o) {
		if (!(o instanceof INormalizedString)) {
			return 1;
		}

		INormalizedString thatString = (INormalizedString) o;
		return value.compareTo(thatString.getValue());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof INormalizedString)) {
			return false;
		}

		INormalizedString thatString = (INormalizedString) obj;
		return value.equals(thatString.getValue());
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

}
