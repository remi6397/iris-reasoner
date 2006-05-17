package org.deri.iris.api.terms.concrete;

import java.util.Calendar;
import java.util.TimeZone;

import org.deri.iris.api.terms.ITerm;

/**
 * This is a representation of the builtin datatype DateTime.
 * 
 * <br />
 * <code>ATTENTION: internally a Calendar is
 * used, so setting month and hour is zero-based.</code>
 * <br />
 * <code>ATTENTION: set the correct timezone</code>
 * 
 * <pre>
 *       Created on 06.04.2006
 *       Committed by $Author: darko $
 *       $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/DateTime.java,v $,
 * </pre>
 * 
 * @author Richard Pöttler
 * 
 * @version $Revision: 1.1 $ $Date: 2006-05-17 14:08:06 $
 */
public interface DateTime extends ITerm, Comparable<DateTime>, Cloneable {
	/**
	 * Returns a copy of this Calendar object.
	 * 
	 * @return the Calendar object
	 */
	public abstract Calendar getDateTime();

	/**
	 * Returns the year.
	 * 
	 * @return the year
	 */
	public abstract int getYear();

	/**
	 * Returns the month of the year.
	 * 
	 * @return the month (zero-based)
	 */
	public abstract int getMonth();

	/**
	 * Returns the day of the month.
	 * 
	 * @return the day
	 */
	public abstract int getDay();

	/**
	 * Returns the hour of the day.
	 * 
	 * @return the hours (zero-based)
	 */
	public abstract int getHour();

	/**
	 * Returns the minute of the hour
	 * 
	 * @return the minutes
	 */
	public abstract int getMinute();

	/**
	 * Returns the seconds of the minute.
	 * 
	 * @return the seconds
	 */
	public abstract int getSecond();

	/**
	 * Returns the Timezone
	 * 
	 * @return the timezone
	 */
	public abstract TimeZone getTimeZone();

}
