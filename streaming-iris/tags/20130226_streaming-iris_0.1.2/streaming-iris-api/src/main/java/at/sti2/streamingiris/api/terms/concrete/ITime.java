package at.sti2.streamingiris.api.terms.concrete;

import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import at.sti2.streamingiris.api.terms.IConcreteTerm;

/**
 * <p>
 * This is a time representation.
 * </p>
 * <p>
 * <code>ATTENTION: internally a Calendar is
 * used, so month and hour are zero-based.</code>
 * </p>
 * <p>
 * <code>ATTENTION: set the correct timezone</code>
 * </p>
 */
public interface ITime extends IConcreteTerm {
	/**
	 * Return the wrapped type.
	 */
	public XMLGregorianCalendar getValue();

	/**
	 * Returns the hour of the day.
	 * 
	 * @return the hours (zero-based)
	 */
	public int getHour();

	/**
	 * Returns the minute of the hour.
	 * 
	 * @return the minutes
	 */
	public int getMinute();

	/**
	 * Returns the seconds of the minute.
	 * 
	 * @return the seconds
	 */
	public int getSecond();

	/**
	 * Returns the milliseconds of the second.
	 * 
	 * @return the milliseconds
	 */
	public int getMillisecond();

	/**
	 * Return the complete floating point representation of the seconds
	 * components.
	 * 
	 * @return Decimal seconds
	 */
	public double getDecimalSecond();

	/**
	 * Returns the Timezone.
	 * 
	 * @return the timezone
	 */
	public TimeZone getTimeZone();
}
