package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IByteTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of Byte.
 * </p>
 * 
 * @author Adrian Marte
 */
public class ByteTerm extends ShortTerm implements IByteTerm {

	/**
	 * Creates a new Byte instance for the specified byte.
	 * 
	 * @param value
	 *            The byte value.
	 */
	public ByteTerm(byte value) {
		super(value);
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.BYTE.toUri();
	}

}
