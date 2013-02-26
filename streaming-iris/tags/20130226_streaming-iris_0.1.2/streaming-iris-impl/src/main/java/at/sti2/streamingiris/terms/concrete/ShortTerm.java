package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IShortTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of Short.
 * </p>
 * 
 * @author Adrian Marte
 */
public class ShortTerm extends IntTerm implements IShortTerm {

	/**
	 * Creates a new Short for the specified short.
	 * 
	 * @param value
	 *            The short value.
	 */
	public ShortTerm(short value) {
		super(value);
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.SHORT.toUri();
	}

}
