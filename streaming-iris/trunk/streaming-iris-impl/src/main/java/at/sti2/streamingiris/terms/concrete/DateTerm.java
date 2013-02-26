package at.sti2.streamingiris.terms.concrete;

import java.net.URI;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * Simple implementation of the IDateTerm.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class DateTerm implements IDateTerm {

	/** Factory used to create the xml durations. */
	private static final DatatypeFactory FACTORY;

	/** The inner calendar object. */
	private final XMLGregorianCalendar date;

	static {
		// creating the factory
		DatatypeFactory tmp = null;
		try {
			tmp = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new IllegalArgumentException(
					"Couldn't create the factory for the date", e);
		}
		FACTORY = tmp;
	}

	/**
	 * Constructs a new date within the given timezone.
	 * 
	 * @param year
	 *            the year
	 * @param month
	 *            the month (1-12)
	 * @param day
	 *            the day
	 * @param tzHour
	 *            the timezone hours
	 * @param tzMinute
	 *            the timezone minutes
	 * @throws IllegalArgumentException
	 *             if the tzHour and tzMinute wheren't both positive, or
	 *             negative
	 */
	DateTerm(final int year, final int month, final int day, final int tzHour,
			final int tzMinute) {

		DateTime.checkTimeZone(tzHour, tzMinute);

		date = FACTORY.newXMLGregorianCalendarDate(year, month, day, tzHour
				* 60 + tzMinute);
	}

	/**
	 * Constructs a new date within the GMT timezone.
	 * 
	 * @param year
	 *            the year
	 * @param month
	 *            the month (1-12)
	 * @param day
	 *            the day
	 * @throws IllegalArgumentException
	 *             if the tzHour and tzMinute wheren't both positive, or
	 *             negative
	 */
	DateTerm(final int year, final int month, final int day) {
		this(year, month, day, 0, 0);
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof IDateTerm)) {
			return false;
		}
		IDateTerm dt = (IDateTerm) obj;
		return date.equals(dt.getValue());
	}

	public int hashCode() {
		return date.hashCode();
	}

	public String toString() {
		return date.toString();
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(ITerm o) {
		if (o == null || !(o instanceof IDateTerm)) {
			return 1;
		}

		IDateTerm dt = (IDateTerm) o;
		return date.compare(dt.getValue());
	}

	public int getMonth() {
		return date.getMonth();
	}

	public int getYear() {
		return date.getYear();
	}

	public int getDay() {
		return date.getDay();
	}

	public XMLGregorianCalendar getValue() {
		return (XMLGregorianCalendar) date.clone();
	}

	public TimeZone getTimeZone() {
		return date.getTimeZone(0);
	}

	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.DATE.toUri();
	}

	public String toCanonicalString() {
		return date.toString();
	}
}
