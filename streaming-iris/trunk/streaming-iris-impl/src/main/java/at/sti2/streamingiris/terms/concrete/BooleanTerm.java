package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IBooleanTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * Simple implementation of the IBooleanTerm.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @author Darko Anicic, DERI Innsbruck
 * @version $Revision$
 */
public class BooleanTerm implements IBooleanTerm {

	/** The boolean value represented by this object */
	private final Boolean value;

	/** One of the legal values. */
	private static final String TRUE = "true";

	/** One of the legal values. */
	private static final String FALSE = "false";

	/** One of the legal values. */
	private static final String ONE = "1";

	/** One of the legal values. */
	private static final String ZERO = "0";

	/**
	 * Constructs a boolean with the given value.
	 * 
	 * @param value
	 *            the boolean to which to set the value to
	 */
	BooleanTerm(final boolean value) {
		this.value = value;
	}

	/**
	 * Constructs a boolean with the given value according to
	 * http://www.w3.org/TR/xmlschema-2/#boolean
	 * 
	 * @param value
	 *            String representation of the boolean, one of {true, false, 1,
	 *            0}
	 * @throws IllegalArgumentException
	 *             If the string is null
	 * @throws IllegalArgumentException
	 *             If the string does not contain one of the legal values {true,
	 *             false, 1, 0}
	 */
	BooleanTerm(final String strValue) {
		if (strValue == null)
			throw new IllegalArgumentException(
					"Constructor parameter 'value' must not be null");

		if (strValue.equalsIgnoreCase(TRUE) || strValue.equalsIgnoreCase(ONE))
			value = true;
		else if (strValue.equalsIgnoreCase(FALSE)
				|| strValue.equalsIgnoreCase(ZERO))
			value = false;
		else
			throw new IllegalArgumentException(
					"Constructor parameter 'value' must be one of {" + TRUE
							+ ", " + FALSE + ", " + ONE + ", " + ZERO + "}");
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof IBooleanTerm)) {
			return false;
		}
		IBooleanTerm bt = (IBooleanTerm) obj;
		return value.equals(bt.getValue());
	}

	public Boolean getValue() {
		return value;
	}

	public int hashCode() {
		return value.hashCode();
	}

	public String toString() {
		return value.toString();
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(ITerm o) {
		if (o == null || !(o instanceof IBooleanTerm)) {
			return 1;
		}

		IBooleanTerm b = (IBooleanTerm) o;
		return value.compareTo(b.getValue());
	}

	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.BOOLEAN.toUri();
	}

	public String toCanonicalString() {
		return value.toString();
	}
}
