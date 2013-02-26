package at.sti2.streamingiris.api.terms.concrete;

import at.sti2.streamingiris.api.terms.IConcreteTerm;

/**
 * Represents the rdf:PlainLiteral data type, formerly known as rdf:text.
 * rdf:PlainLiteral is an internationalized string value that contains a
 * language tag indicating it's spoken language, e.g. "Padre de familia@es".
 */
public interface IPlainLiteral extends IConcreteTerm {

	/**
	 * Returns the wrapped type. The first element of this array is the string
	 * and the second is the language.
	 * 
	 * @return The wrapped type.
	 */
	public String[] getValue();

	/**
	 * Returns the string, e.g. "Padre de familia", if this text represents
	 * "Padre de familia@es".
	 * 
	 * @return The text.
	 */
	public String getString();

	/**
	 * Returns the language tag, e.g. "es", if this text represents
	 * "Padre de familia@es".
	 * 
	 * @return The language tag.
	 */
	public String getLang();

}
