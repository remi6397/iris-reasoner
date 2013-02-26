package at.sti2.streamingiris.api.terms.concrete;

import at.sti2.streamingiris.api.terms.IConcreteTerm;

/**
 * A term representing a rdf:XMLLiteral.
 */
public interface IXMLLiteral extends IConcreteTerm {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.deri.iris.api.terms.ITerm#getValue()
	 * 
	 * The first element is the string, the second element is the language
	 * identifier.
	 */
	public String[] getValue();

	/**
	 * Returns the represented XML element content as a string.
	 * 
	 * @return The represented XML element content as a string.
	 */
	public String getString();

	/**
	 * Returns the language identifier for this XML element, or
	 * <code>null</code> if not language identifier is defined. This string
	 * directly corresponds to the xml:lang attribute.
	 * 
	 * @return The language identifier for this XML element, or
	 *         <code>null</code> if not language identifier is defined.
	 */
	public String getLang();

}
