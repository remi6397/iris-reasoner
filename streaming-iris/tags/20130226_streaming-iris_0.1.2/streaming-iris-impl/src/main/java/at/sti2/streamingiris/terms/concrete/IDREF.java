package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IIDREF;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of IDREF.
 * </p>
 * 
 * @author Adrian Marte
 */
public class IDREF extends NCName implements IIDREF {

	/**
	 * Creates a new IDREF for the specified IDREF. Does not check for validity
	 * of the specified IDRef.
	 * 
	 * @param idRef
	 *            The string representing the IDREF.
	 */
	public IDREF(String idRef) {
		super(idRef);
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.IDREF.toUri();
	}

}
