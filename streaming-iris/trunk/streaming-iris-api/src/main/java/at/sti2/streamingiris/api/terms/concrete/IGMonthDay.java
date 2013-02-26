package at.sti2.streamingiris.api.terms.concrete;

import at.sti2.streamingiris.api.terms.IConcreteTerm;

/**
 * 
 * gMonthDay is a gregorian date that recurs, specifically a day of the year
 * such as the third of May.
 * <p>
 * Remark: IRIS supports datatypes according to the standard specification for
 * primitive XML Schema datatypes.
 * </p>
 * 
 * <pre>
 *    Created on 11.04.2006
 *    Committed by $Author: bazbishop237 $
 *    $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/IGMonthDay.java,v $,
 * </pre>
 * 
 * @author Richard Pöttler
 * 
 * @version $Revision: 1.5 $ $Date: 2007-10-09 20:21:21 $
 */
public interface IGMonthDay extends IConcreteTerm {
	/**
	 * Return the wrapped type.
	 */
	public Integer[] getValue();

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
