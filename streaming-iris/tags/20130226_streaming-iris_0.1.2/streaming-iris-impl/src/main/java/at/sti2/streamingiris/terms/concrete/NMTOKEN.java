package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.INMTOKEN;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of NMTOKEN.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NMTOKEN extends Token implements INMTOKEN {

	/**
	 * Creates a new NMTOKEN instance for the specified token. Does not validate
	 * for validity.
	 * 
	 * @param token
	 *            The valid string representing a NMTOKEN.
	 */
	public NMTOKEN(String token) {
		super(token);
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.NMTOKEN.toUri();
	}

}
