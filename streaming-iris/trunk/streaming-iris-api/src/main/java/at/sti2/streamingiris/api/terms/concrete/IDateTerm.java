package at.sti2.streamingiris.api.terms.concrete;

import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import at.sti2.streamingiris.api.terms.IConcreteTerm;

/**
 * <p>
 * An interface for representing the date datatype.
 * </p>
 * <p>
 * Remark: IRIS supports datatypes according to the standard specification for
 * primitive XML Schema datatypes.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public interface IDateTerm extends IConcreteTerm {

	/**
	 * Return the wrapped type.
	 */
	XMLGregorianCalendar getValue();

	/**
	 * Returns the month of the year.
	 * 
	 * @return the month.
	 */
	int getMonth();

	/**
	 * Returns the year.
	 * 
	 * @return the year.
	 */
	int getYear();

	/**
	 * Returns the day of the month.
	 * 
	 * @return the day
	 */
	int getDay();

	/**
	 * Returns the Timezone
	 * 
	 * @return the timezone
	 */
	TimeZone getTimeZone();
}
