package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IName;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of Name.
 * </p>
 * 
 * @author Adrian Marte
 */
public class Name extends Token implements IName {

	/**
	 * Creates a new Name instance for the specified name. Does not check for
	 * validity of the specified name.
	 * 
	 * @param name
	 *            The string representing a valid name.
	 */
	public Name(String name) {
		super(name);
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.NAME.toUri();
	}

}
