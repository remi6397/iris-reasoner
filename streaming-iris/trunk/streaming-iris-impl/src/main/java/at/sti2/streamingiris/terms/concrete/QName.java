package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IQName;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * An simple implementation of QName.
 * </p>
 * 
 * @author Adrian Marte
 */
public class QName implements IQName {

	private String namespaceName;

	private String localPart;

	/**
	 * Creates a new QName instance for the given namespace name and local part.
	 * 
	 * @param namespaceName
	 *            The namespace name for this QName. May be null.
	 * @param localPart
	 *            The local part of this QName.
	 * @throws NullPointerException
	 *             If the local part is <code>null</code>.
	 */
	public QName(String namespaceName, String localPart) {
		this.namespaceName = namespaceName;
		this.localPart = localPart;

		if (this.localPart == null) {
			throw new NullPointerException(
					"The local part of a QName must not be null");
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
		return XmlSchemaDatatype.QNAME.toUri();
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
		if (!(thatObject instanceof IQName)) {
			return 1;
		}

		IQName thatQName = (IQName) thatObject;

		return toCanonicalString().compareTo(thatQName.toCanonicalString());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IQName)) {
			return false;
		}

		IQName thatQName = (IQName) obj;

		return namespaceName.equals(thatQName.getNamespaceName())
				&& localPart.equals(thatQName.getLocalPart());
	}

	@Override
	public int hashCode() {
		return namespaceName.hashCode() + 37 * localPart.hashCode();
	}

}
