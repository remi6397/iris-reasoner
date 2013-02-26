package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.INCName;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of NCName.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NCName extends Name implements INCName {

	/**
	 * Creates a new NCName instance for the given NCName. Does not check for
	 * validity of the specified NCName.
	 * 
	 * @param name
	 *            The string representing the NCName.
	 */
	public NCName(String name) {
		super(name);
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.NCNAME.toUri();
	}

}
