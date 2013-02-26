package at.sti2.streamingiris.api.terms.concrete;

import at.sti2.streamingiris.api.terms.IConcreteTerm;

/**
 * <p>
 * An interface for representing the gYearMonth datatype. (represents a specific
 * gregorian month in a specific gregorian year).
 * </p>
 * <p>
 * Remark: IRIS supports datatypes according to the standard specification for
 * primitive XML Schema datatypes.
 * </p>
 * 
 * @author richi
 * 
 */
public interface IGYearMonth extends IConcreteTerm {
	/**
	 * Return the wrapped type.
	 */
	public Integer[] getValue();

	/**
	 * Returns the year (a gregorian calendar year).
	 * 
	 * @return the year.
	 */
	public abstract int getYear();

	/**
	 * Returns a specific gregorian month (in a gregorian calendar year).
	 * 
	 * @return the month.
	 */
	public abstract int getMonth();
}
