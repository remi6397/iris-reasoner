package at.sti2.streamingiris.api.terms.concrete;

import at.sti2.streamingiris.api.terms.IConcreteTerm;

/**
 * <p>
 * An interface for representing the gYear datatype (gYear represents a
 * gregorian calendar year).
 * </p>
 * <p>
 * Remark: IRIS supports datatypes according to the standard specification for
 * primitive XML Schema datatypes.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 22.01.2007 16:20:59
 */
public interface IGYear extends IConcreteTerm {
	/**
	 * Return the wrapped type.
	 */
	public Integer getValue();

	/**
	 * Returns the year (a gregorian calendar year).
	 * 
	 * @return the year.
	 */
	public abstract int getYear();
}
