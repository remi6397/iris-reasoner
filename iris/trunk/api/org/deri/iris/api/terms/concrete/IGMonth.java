package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.ITerm;


/**
 * 
 * gMonth is a gregorian month that recurs every year.
 * 
 * <pre>
 *   Created on 11.04.2006
 *   Committed by $Author: richardpoettler $
 *   $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/IGMonth.java,v $,
 * </pre>
 * 
 * @author Richard Pöttler
 * 
 * @version $Revision: 1.3 $ $Date: 2006-06-26 12:35:22 $
 */
public interface IGMonth extends ITerm<IGMonth, Integer> {
	/**
	 * Returns the month.
	 * 
	 * @return the month
	 */
	public abstract int getMonth();
}
