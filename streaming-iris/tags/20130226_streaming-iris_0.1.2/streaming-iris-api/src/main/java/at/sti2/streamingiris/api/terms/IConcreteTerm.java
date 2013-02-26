package at.sti2.streamingiris.api.terms;

import java.net.URI;

/**
 * <p>
 * An interface representing a concrete term. A concrete term has a
 * corresponding data type URI. For instance, a term representing a double data
 * type should return the URI "http://www.w3.org/2001/XMLSchema#double".
 * </p>
 * <p>
 * Remark: IRIS supports data types according to the standard specification for
 * primitive XML Schema data types and additional data types required in RIF.
 * </p>
 * 
 * @see <a href="http://www.w3.org/TR/xmlschema-2/">XML Schema: Datatypes</a>
 * @see <a href="http://www.w3.org/2005/rules/wiki/DTB">RIF Datatypes and
 *      Built-Ins</a>
 */
public interface IConcreteTerm extends ITerm {

	/**
	 * Returns the fully qualified identifier for the data type corresponding to
	 * this term. For instance, a terms representing a double data type should
	 * return the URI "http://www.w3.org/2001/XMLSchema#double".
	 * 
	 * @return The fully qualified identifier for the data type corresponding to
	 *         this term.
	 */
	public URI getDatatypeIRI();

	/**
	 * Returns a canonical string representation of this term.
	 * 
	 * @return A canonical string representation of this term.
	 */
	public String toCanonicalString();

}
