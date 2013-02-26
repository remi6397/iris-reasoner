package at.sti2.streamingiris.api.terms.concrete;

import at.sti2.streamingiris.api.terms.IConcreteTerm;

/**
 * <p>
 * Represents the XML Schema datatype xsd:QName.
 * </p>
 * 
 * @author Adrian Marte
 */
public interface IQName extends IConcreteTerm {

	/**
	 * Returns the namespace name of this QName.
	 * 
	 * @return The namespace name of this QName.
	 */
	public String getNamespaceName();

	/**
	 * Returns the local part of this QName.
	 * 
	 * @return The local part of this QName.
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
