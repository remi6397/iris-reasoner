package at.sti2.streamingiris.api.terms.concrete;

import javax.xml.datatype.Duration;

import at.sti2.streamingiris.api.terms.IConcreteTerm;

/**
 * 
 * <p>
 * This is a interface to represent durations from seconds up to years.
 * </p>
 * <p>
 * Remark: IRIS supports datatypes according to the standard specification for
 * primitive XML Schema datatypes.
 * </p>
 */
public interface IDuration extends IConcreteTerm {
	/**
	 * Return the wrapped type.
	 */
	Duration getValue();

	/**
	 * Returns <code>true</code> if this is a positive duration,
	 * <code>false</code> otherwise. Also returns <code>true</code> if this is a
	 * duration of length 0.
	 * 
	 * @return <code>true</code> if this is a positive duration,
	 *         <code>false</code> otherwise.
	 */
	boolean isPositive();

	/**
	 * Returns the years.
	 * 
	 * @return the years
	 */
	int getYear();

	/**
	 * Returns the months
	 * 
	 * @return the months
	 */
	int getMonth();

	/**
	 * Returns the days
	 * 
	 * @return the days
	 */
	int getDay();

	/**
	 * Returns the hours
	 * 
	 * @return the hours
	 */
	int getHour();

	/**
	 * returns the minutes
	 * 
	 * @return the minutes
	 */
	int getMinute();

	/**
	 * Returns the seconds
	 * 
	 * @return the seconds
	 */
	int getSecond();

	/**
	 * Returns the milliseconds.
	 * 
	 * @return the milliseconds
	 */
	int getMillisecond();

	/**
	 * Return the complete floating point representation of the seconds
	 * components.
	 * 
	 * @return Decimal seconds
	 */
	double getDecimalSecond();
}
