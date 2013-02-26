package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IAnyURI;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * An simple implementation of anyURI.
 * </p>
 * 
 * @author Adrian Marte
 */
public class AnyURI implements IAnyURI {

	private URI uri;

	/**
	 * Creates a new AnyURI instance for the given URI.
	 * 
	 * @param uri
	 *            The URI with which the AnyURI is created.
	 */
	public AnyURI(URI uri) {
		this.uri = uri;
	}

	public URI getValue() {
		return uri;
	}

	public URI getDatatypeIRI() {
		return URI.create(XmlSchemaDatatype.ANYURI.getUri());
	}

	public String toCanonicalString() {
		return uri.toString();
	}

	@Override
	public String toString() {
		return toCanonicalString();
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(ITerm o) {
		if (!(o instanceof IAnyURI)) {
			return 1;
		}

		IAnyURI thatUri = (IAnyURI) o;

		return uri.compareTo(thatUri.getValue());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IAnyURI)) {
			return false;
		}

		IAnyURI thatAnyURI = (IAnyURI) obj;
		return uri.equals(thatAnyURI.getValue());
	}

	@Override
	public int hashCode() {
		return uri.hashCode();
	}

}
