package at.sti2.streamingiris.terms.concrete;

import java.net.URI;
import java.util.regex.Pattern;

import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IBase64Binary;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * Simple implementation of the IBase64Binary.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class Base64Binary implements IBase64Binary {

	public static final Pattern PATTERN = Pattern.compile("([a-zA-Z0-9/+]{4})*"
			+ "(([a-zA-Z0-9/+]{2}[AEIMQUYcgkosw048]=)|"
			+ "([a-zA-Z0-9/+]{1}[AQgw]==))?");

	private String content = "";

	Base64Binary() {
	}

	Base64Binary(final String content) {
		this();
		_setValue(content);
	}

	public int compareTo(ITerm o) {
		if (o == null)
			return 1;

		Base64Binary b64 = (Base64Binary) o;
		return content.compareTo(b64.getValue());
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof Base64Binary)) {
			return false;
		}
		Base64Binary b64 = (Base64Binary) obj;
		return content.equals(b64.content);
	}

	public String getValue() {
		return content;
	}

	public int hashCode() {
		return content.hashCode();
	}

	private void _setValue(final String content) {
		if (PATTERN.matcher(content).matches()) {
			this.content = content;
		} else {
			throw new IllegalArgumentException("Couldn't parse " + content
					+ " to a valid Base64Binary");
		}
	}

	public String toString() {
		return getClass().getSimpleName() + "(" + getValue() + ")";
	}

	public boolean isGround() {
		return true;
	}

	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.BASE64BINARY.toUri();
	}

	public String toCanonicalString() {
		return new String(getValue());
	}
}
