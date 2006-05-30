package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.ITerm;

/**
 * 
 * gDay is a gregorian day that recurs, specifically a day of the month such as
 * the 5th of the month.
 * 
 * <pre>
 *    Created on 11.04.2006
 *    Committed by $Author: richardpoettler $
 *    $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/IGDay.java,v $,
 * </pre>
 * 
 * @author Richard Pöttler
 * 
 * @version $Revision: 1.2 $ $Date: 2006-05-30 14:56:18 $
 */
public interface IGDay extends ITerm<IGDay> {
	/**
	 * Returns the day.
	 * 
	 * @return the day
	 */
	public abstract int getDay();
}
