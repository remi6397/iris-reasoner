package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IIntTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of Int.
 * </p>
 * 
 * @author Adrian Marte
 */
public class IntTerm extends LongTerm implements IIntTerm {

	/**
	 * Creates a new Int for the specified integer.
	 * 
	 * @param value
	 *            The integer value.
	 */
	public IntTerm(int value) {
		super(value);
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.INT.toUri();
	}

}
