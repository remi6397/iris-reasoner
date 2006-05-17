package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.ITerm;

/**
 * 
 * gDay is a gregorian day that recurs, specifically a day of the month such as
 * the 5th of the month.
 * 
 * <pre>
 *    Created on 11.04.2006
 *    Committed by $Author: darko $
 *    $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/GDay.java,v $,
 * </pre>
 * 
 * @author Richard Pöttler
 * 
 * @version $Revision: 1.1 $ $Date: 2006-05-17 14:08:06 $
 */
public interface GDay extends ITerm, Cloneable, Comparable<GDay> {
	/**
	 * Returns the day.
	 * 
	 * @return the day
	 */
	public abstract int getDay();
}
