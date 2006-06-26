package org.deri.iris.api.terms.concrete;

import java.util.Calendar;

import org.deri.iris.api.terms.ITerm;

/**
 * 
 * This is a interface to represent durations from seconds up to years.
 *
 * <pre>
 * Created on 11.04.2006
 * Committed by $Author: richardpoettler $
 * $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/IDuration.java,v $,
 * </pre>
 *
 * @author Richard Pöttler
 *
 * @version $Revision: 1.3 $ $Date: 2006-06-26 12:35:22 $
 */
public interface IDuration extends ITerm<IDuration, Calendar> {
	/**
	 * Returns the corresponding Calendar object.
	 * 
	 * @return the Calendar object
	 */
	public abstract Calendar getDateTime();

	/**
	 * Returns the years.
	 * 
	 * @return the years
	 */
	public abstract int getYear();

	/**
	 * Returns the months
	 * 
	 * @return the months
	 */
	public abstract int getMonth();

	/**
	 * Returns the days
	 * 
	 * @return the days
	 */
	public abstract int getDay();

	/**
	 * Returns the hours
	 * 
	 * @return the hours
	 */
	public abstract int getHour();

	/**
	 * returns the minutes
	 * 
	 * @return the minutes
	 */
	public abstract int getMinute();

	/**
	 * Returns the seconds
	 * 
	 * @return the seconds
	 */
	public abstract int getSecond();
}
