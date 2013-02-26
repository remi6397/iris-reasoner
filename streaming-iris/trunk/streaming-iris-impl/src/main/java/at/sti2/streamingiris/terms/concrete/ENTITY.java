package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IENTITY;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of Entity.
 * </p>
 * 
 * @author Adrian Marte
 */
public class ENTITY extends NCName implements IENTITY {

	/**
	 * Creates a new Entity for the specified entity. Does not check for
	 * validity of the specified entity.
	 * 
	 * @param entity
	 *            The string representing the entity.
	 */
	public ENTITY(String entity) {
		super(entity);
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.ENTITY.toUri();
	}

}
