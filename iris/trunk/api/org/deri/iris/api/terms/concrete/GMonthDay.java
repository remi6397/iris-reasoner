package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.ITerm;

/**
 * 
 * gMonthDay is a gregorian date that recurs, specifically a day of the year
 * such as the third of May.
 * 
 * <pre>
 *    Created on 11.04.2006
 *    Committed by $Author: darko $
 *    $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/GMonthDay.java,v $,
 * </pre>
 * 
 * @author Richard Pöttler
 * 
 * @version $Revision: 1.1 $ $Date: 2006-05-17 14:08:06 $
 */
public interface GMonthDay extends ITerm, Cloneable, Comparable<GMonthDay> {
	/**
	 * Returns the month.
	 * 
	 * @return the month
	 */
	public abstract int getMonth();

	/**
	 * Returns the day.
	 * 
	 * @return the day
	 */
	public abstract int getDay();
}
