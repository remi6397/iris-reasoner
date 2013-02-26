package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IGMonth;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * Simple implementation of the IGMonth.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class GMonth implements IGMonth {

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
					"Couldn't create the factory for the month", e);
		}
		FACTORY = tmp;
	}

	/**
	 * Creates a new month. The timezone will be set to GMT.
	 * 
	 * @param month
	 *            the month
	 */
	GMonth(final int month) {
		this(month, 0, 0);
	}

	/**
	 * Creates a new month withing the given timezone.
	 * 
	 * @param month
	 *            the month (1-12)
	 * @param tzHour
	 *            the timezone hours (relative to GMT)
	 * @param tzMinute
	 *            the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException
	 *             if the tzHour and tzMinute wheren't both positive, or
	 *             negative
	 */
	GMonth(final int month, final int tzHour, final int tzMinute) {
		if (((tzHour < 0) && (tzMinute > 0))
				|| ((tzHour > 0) && (tzMinute < 0))) {
			throw new IllegalArgumentException("Both, the timezone hours and "
					+ "minutes must be negative, or positive, but were "
					+ tzHour + " and " + tzMinute);
		}

		date = FACTORY.newXMLGregorianCalendarDate(
				DatatypeConstants.FIELD_UNDEFINED, month,
				DatatypeConstants.FIELD_UNDEFINED, tzHour * 60 + tzMinute);
	}

	public int compareTo(ITerm o) {
		if (o == null) {
			return 1;
		}

		GMonth gm = (GMonth) o;
		return getMonth() - gm.getValue();
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof IGMonth)) {
			return false;
		}
		IGMonth gm = (IGMonth) obj;
		return (gm.getMonth() == getMonth());
	}

	public int getMonth() {
		return date.getMonth();
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

	public Integer getValue() {
		return date.getMonth();
	}

	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.GMONTH.toUri();
	}

	public String toCanonicalString() {
		return date.toString();
	}
}
