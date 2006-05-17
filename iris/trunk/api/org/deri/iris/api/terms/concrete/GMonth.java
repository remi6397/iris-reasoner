package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.ITerm;


/**
 * 
 * gMonth is a gregorian month that recurs every year.
 * 
 * <pre>
 *   Created on 11.04.2006
 *   Committed by $Author: darko $
 *   $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/GMonth.java,v $,
 * </pre>
 * 
 * @author Richard Pöttler
 * 
 * @version $Revision: 1.1 $ $Date: 2006-05-17 14:08:06 $
 */
public interface GMonth extends ITerm, Cloneable, Comparable<GMonth> {
	/**
	 * Returns the month.
	 * 
	 * @return the month
	 */
	public abstract int getMonth();
}
