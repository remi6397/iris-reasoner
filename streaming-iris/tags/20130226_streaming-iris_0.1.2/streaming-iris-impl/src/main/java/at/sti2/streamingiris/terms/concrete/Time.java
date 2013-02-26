package at.sti2.streamingiris.terms.concrete;

import java.math.BigDecimal;
import java.net.URI;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.ITime;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * Simple implementation of ITime.
 * </p>
 */
public class Time implements ITime {

	/** Factory used to create the xml durations. */
	private static final DatatypeFactory FACTORY;

	/** The inner calendar object. */
	private final XMLGregorianCalendar time;

	/** Milliseconds per minute. */
	private static final int MILLIS_PER_MINUTE = 1000 * 60;

	/** Milliseconds per hour. */
	private static final int MILLIS_PER_HOUR = MILLIS_PER_MINUTE * 60;

	static {
		// create the data type factory
		try {
			FACTORY = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(
					"Couldn't create the factory for the Time type", e);
		}
	}

	/**
	 * Constructs a new time object with a given timezone.
	 * 
	 * @param hour
	 *            the hours
	 * @param minute
	 *            the minutes
	 * @param second
	 *            the seconds
	 * @param millisecond
	 *            the milliseconds
	 * @param tzHour
	 *            the timezone hours (relative to GMT)
	 * @param tzMinute
	 *            the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException
	 *             if, the tzHour and tzMinute wheren't both positive, or
	 *             negative
	 */
	Time(int hour, int minute, int second, int millisecond, int tzHour,
			int tzMinute) {
		this(hour, minute, second + (millisecond / 1000.0), tzHour, tzMinute);

		if (millisecond < 0 || millisecond >= 1000)
			throw new IllegalArgumentException(
					"Millisecond value is out of range: " + second);

		if (second < 0 || second >= 60)
			throw new IllegalArgumentException("Second value is out of range: "
					+ second);
	}

	/**
	 * Constructs a new time object with a given timezone.
	 * 
	 * @param hour
	 *            the hours
	 * @param minute
	 *            the minutes
	 * @param second
	 *            the seconds
	 * @param millisecond
	 *            the milliseconds
	 * @param tzHour
	 *            the timezone hours (relative to GMT)
	 * @param tzMinute
	 *            the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException
	 *             if, the tzHour and tzMinute wheren't both positive, or
	 *             negative
	 */
	Time(int hour, int minute, double second, int tzHour, int tzMinute) {
		DateTime.checkTimeZone(tzHour, tzMinute);

		int intSeconds = (int) second;
		BigDecimal fractionalSeconds = new BigDecimal(Double.toString(second))
				.subtract(BigDecimal.valueOf(intSeconds));

		time = FACTORY.newXMLGregorianCalendarTime(hour, minute, intSeconds,
				fractionalSeconds, tzHour * 60 + tzMinute);
	}

	public int compareTo(ITerm o) {
		if (o == null || !(o instanceof ITime)) {
			return 1;
		}

		ITime t = (ITime) o;
		return time.compare(t.getValue());
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof ITime)) {
			return false;
		}
		ITime dx = (ITime) obj;
		return time.equals(dx.getValue());
	}

	public int getHour() {
		return time.getHour();
	}

	public int getMinute() {
		return time.getMinute();
	}

	public int getSecond() {
		return time.getSecond();
	}

	public int getMillisecond() {
		return time.getMillisecond();
	}

	public double getDecimalSecond() {
		BigDecimal seconds = time.getFractionalSecond().add(
				BigDecimal.valueOf(time.getSecond()));
		return seconds.doubleValue();
	}

	public TimeZone getTimeZone() {
		return time.getTimeZone(0);
	}

	public int hashCode() {
		return time.hashCode();
	}

	public String toString() {
		return time.toString();
	}

	protected static int getTimeZoneHour(final TimeZone tz) {
		assert tz != null : "The timezone must not be null";

		return tz.getRawOffset() / MILLIS_PER_HOUR;
	}

	protected static int getTimeZoneMinute(final TimeZone tz) {
		assert tz != null : "The timezone must not be null";

		return (tz.getRawOffset() % MILLIS_PER_HOUR) / MILLIS_PER_MINUTE;
	}

	public boolean isGround() {
		return true;
	}

	public XMLGregorianCalendar getValue() {
		return (XMLGregorianCalendar) time.clone();
	}

	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.TIME.toUri();
	}

	public String toCanonicalString() {
		return time.toString();
	}
}
