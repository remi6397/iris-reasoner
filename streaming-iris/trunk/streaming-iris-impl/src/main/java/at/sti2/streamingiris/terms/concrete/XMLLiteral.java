package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IXMLLiteral;
import at.sti2.streamingiris.api.terms.concrete.RdfDatatype;

/**
 * Implementation of the rdf:XMLLiteral data-type.
 * 
 * @author gigi
 */
public class XMLLiteral implements IXMLLiteral {

	/**
	 * The string representing the XML element content.
	 */
	private String string;

	/**
	 * The language identifier for this XML element.
	 */
	private String lang;

	/**
	 * Creates a new term representing a XMLLiteral.
	 * 
	 * @param string
	 *            The string.
	 * @param lang
	 *            The language.
	 * @throws NullPointerException
	 *             If the value of <code>string</code> or <code>lang</code> is
	 *             <code>null</code>.
	 */
	XMLLiteral(String string, String lang) {
		if (string == null) {
			throw new NullPointerException("String value must not be null");
		}

		if (lang == null) {
			throw new NullPointerException("Language tag must not be null");
		}

		this.string = string;
		this.lang = lang;
	}

	/**
	 * Creates a new term representing a XMLLiteral.
	 * 
	 * @param string
	 *            The string.
	 * @throws NullPointerException
	 *             If the value of <code>string</code> is <code>null</code>.
	 */
	XMLLiteral(String string) {
		this(string, "");
	}

	public String toString() {
		return toCanonicalString();
	}

	public URI getDatatypeIRI() {
		return RdfDatatype.XML_LITERAL.toUri();
	}

	public String getString() {
		return string;
	}

	public String getLang() {
		return lang;
	}

	public String[] getValue() {
		return new String[] { string, lang };
	}

	public boolean isGround() {
		return true;
	}

	public boolean equals(Object thatObject) {
		if (!(thatObject instanceof IXMLLiteral)) {
			return false;
		}

		return compareTo((ITerm) thatObject) == 0;
	}

	public int hashCode() {
		int result = string.hashCode();

		if (lang != null) {
			result = result * 37 + lang.hashCode();
		}

		return result;
	}

	public int compareTo(ITerm thatObject) {
		if (thatObject == null) {
			return 1;
		}

		IXMLLiteral thatLiteral = (IXMLLiteral) thatObject;

		return this.getString().compareTo(thatLiteral.getString());
	}

	public String toCanonicalString() {
		return new String(string);
	}
}
