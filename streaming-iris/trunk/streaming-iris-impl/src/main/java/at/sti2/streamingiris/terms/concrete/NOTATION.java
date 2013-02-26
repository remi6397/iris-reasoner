package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.INOTATION;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * An simple implementation of NOTATION.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NOTATION implements INOTATION {

	private String namespaceName;

	private String localPart;

	/**
	 * Creates a new NOTATION instance for the given namespace name and local
	 * part.
	 * 
	 * @param namespaceName
	 *            The namespace name for this NOTATION. May be null.
	 * @param localPart
	 *            The local part of this NOTATION.
	 * @throws NullPointerException
	 *             If the local part is <code>null</code>.
	 */
	public NOTATION(String namespaceName, String localPart) {
		this.namespaceName = namespaceName;
		this.localPart = localPart;

		if (this.localPart == null) {
			throw new NullPointerException(
					"The local part of a NOTATION must not be null");
		}
	}

	public String getNamespaceName() {
		return namespaceName;
	}

	public String getLocalPart() {
		return localPart;
	}

	public String[] getValue() {
		return new String[] { namespaceName, localPart };
	}

	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.NOTATION.toUri();
	}

	public String toCanonicalString() {
		String result = "";

		if (namespaceName != null) {
			result += namespaceName + ":";
		}

		result += localPart;

		return result;
	}

	@Override
	public String toString() {
		return toCanonicalString();
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(ITerm thatObject) {
		if (!(thatObject instanceof INOTATION)) {
			return 1;
		}

		INOTATION thatNotation = (INOTATION) thatObject;

		return toCanonicalString().compareTo(thatNotation.toCanonicalString());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof INOTATION)) {
			return false;
		}

		INOTATION thatNotation = (INOTATION) obj;

		return namespaceName.equals(thatNotation.getNamespaceName())
				&& localPart.equals(thatNotation.getLocalPart());
	}

	@Override
	public int hashCode() {
		return namespaceName.hashCode() + 37 * localPart.hashCode();
	}

}
