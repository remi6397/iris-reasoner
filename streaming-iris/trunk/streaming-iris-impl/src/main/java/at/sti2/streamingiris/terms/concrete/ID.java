package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IID;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of ID.
 * </p>
 * 
 * @author Adrian Marte
 */
public class ID extends NCName implements IID {

	/**
	 * Creates a new ID for the specified ID. Does not check for validity of the
	 * specified ID.
	 * 
	 * @param id
	 *            The string representing the ID.
	 */
	public ID(String id) {
		super(id);
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.ID.toUri();
	}

}
