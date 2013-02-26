package at.sti2.streamingiris.terms;

import java.net.URI;

import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 * <p>
 * Simple implementation of the IStringTerm.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class StringTerm implements IStringTerm {

	private String value = "";

	StringTerm(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(ITerm o) {
		if (o == null || !(o instanceof IStringTerm)) {
			return 1;
		}

		IStringTerm st = (IStringTerm) o;
		return value.compareTo(st.getValue());
	}

	public int hashCode() {
		return value.hashCode();
	}

	public boolean equals(final Object o) {
		if (!(o instanceof IStringTerm)) {
			return false;
		}
		IStringTerm st = (IStringTerm) o;
		return value.equals(st.getValue());
	}

	/**
	 * Simple toString() method wich only returns the holded value surrounded by
	 * &quot;'&quot;.
	 * 
	 * @return the containing String
	 */
	public String toString() {
		return "'" + value + "'";
	}

	public URI getDatatypeIRI() {
		return URI.create("http://www.w3.org/2001/XMLSchema#string");
	}

	public String toCanonicalString() {
		return new String(value);
	}
}
