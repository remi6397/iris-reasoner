package at.sti2.streamingiris.api.terms.concrete;

import at.sti2.streamingiris.api.terms.IStringTerm;

/**
 * <p>
 * Represents the XML Schema datatype xsd:normalizedString.
 * </p>
 * <p>
 * xsd:normalizedString are strings that do not contain the carriage return
 * (#xD), line feed (#xA) nor tab (#x9) characters.
 * </p>
 * 
 * @author Adrian Marte
 */
public interface INormalizedString extends IStringTerm {

	/**
	 * Returns the string representing this xsd:normalizedString.
	 * 
	 * @return The string representing this xsd:normalizedString.
	 */
	public String getValue();

}
