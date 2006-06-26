package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.ITerm;

/**
 * 
 * gMonthDay is a gregorian date that recurs, specifically a day of the year
 * such as the third of May.
 * 
 * <pre>
 *    Created on 11.04.2006
 *    Committed by $Author: richardpoettler $
 *    $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/IGMonthDay.java,v $,
 * </pre>
 * 
 * @author Richard Pöttler
 * 
 * @version $Revision: 1.3 $ $Date: 2006-06-26 12:35:22 $
 */
public interface IGMonthDay extends ITerm<IGMonthDay, Integer[]> {
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
