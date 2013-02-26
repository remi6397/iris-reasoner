package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IGDay;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * Simple implementation of the IGDay.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class GDay implements IGDay {

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
					"Couldn't create the factory for the year", e);
		}
		FACTORY = tmp;
	}

	/**
	 * Creates a new day. The timezone will be set to GMT.
	 * 
	 * @param day
	 *            the day
	 */
	GDay(final int day) {
		this(day, 0, 0);
	}

	/**
	 * Creates a new day within the given timezone.
	 * 
	 * @param day
	 *            the day
	 * @param tzHour
	 *            the timezone hours (relative to GMT)
	 * @param tzMinute
	 *            the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException
	 *             if the tzHour and tzMinute wheren't both positive, or
	 *             negative
	 */
	GDay(final int day, final int tzHour, final int tzMinute) {
		if (((tzHour < 0) && (tzMinute > 0))
				|| ((tzHour > 0) && (tzMinute < 0))) {
			throw new IllegalArgumentException("Both, the timezone hours and "
					+ "minutes must be negative, or positive, but were "
					+ tzHour + " and " + tzMinute);
		}

		date = FACTORY.newXMLGregorianCalendarDate(
				DatatypeConstants.FIELD_UNDEFINED,
				DatatypeConstants.FIELD_UNDEFINED, day, tzHour * 60 + tzMinute);
	}

	public int compareTo(ITerm o) {
		if (o == null) {
			return 1;
		}

		GDay gd = (GDay) o;
		return getDay() - gd.getValue();
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof IGDay)) {
			return false;
		}
		IGDay gi = (IGDay) obj;
		return getDay() == gi.getDay();
	}

	public int getDay() {
		return date.getDay();
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
		return date.getDay();
	}

	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.GDAY.toUri();
	}

	public String toCanonicalString() {
		return date.toString();
	}
}
