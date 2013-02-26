package at.sti2.streamingiris.api.terms.concrete;

import at.sti2.streamingiris.api.terms.IConcreteTerm;

/**
 * <p>
 * Represents the XML Schema datatype xsd:NOTATION.
 * </p>
 * <p>
 * According to the XML Schema specificaiton, the value space of xsd:NOTATION is
 * the set of QNames of notations declared in a schema.
 * </p>
 * 
 * @author Adrian Marte
 */
public interface INOTATION extends IConcreteTerm {

	/**
	 * Returns the namespace name of this NOTATION.
	 * 
	 * @return The namespace name of this NOTATION.
	 */
	public String getNamespaceName();

	/**
	 * Returns the local part of this NOTATION.
	 * 
	 * @return The local part of this NOTATION.
	 */
	public String getLocalPart();

	/**
	 * Returns an array containing the namespace name (first element) and the
	 * local part (second element).
	 * 
	 * @return An array containing the namespace name (first element) and the
	 *         local part (second element).
	 */
	public String[] getValue();
}
