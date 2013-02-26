package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IDateTimeStamp;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of DateTimeStamp.
 * </p>
 * 
 * @author Adrian Marte
 */
public class DateTimeStamp extends DateTime implements IDateTimeStamp {

	/**
	 * Creates a new DateTimeStamp for the specified values.
	 * 
	 * @param year
	 *            The year fragment.
	 * @param month
	 *            The month fragment.
	 * @param day
	 *            The Day of month fragment.
	 * @param hour
	 *            The hour fragment.
	 * @param minute
	 *            The minute fragment.
	 * @param second
	 *            The second fragment.
	 * @param tzHour
	 *            The timezone hour (relative to GMT) fragment.
	 * @param tzMinute
	 *            The timezone minute (relative to GMT) fragment.
	 * @throws IllegalArgumentException
	 *             If not both the tzHour and tzMinute are positive or negative.
	 */
	public DateTimeStamp(int year, int month, int day, int hour, int minute,
			double second, int tzHour, int tzMinute) {
		super(year, month, day, hour, minute, second, tzHour, tzMinute);
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.DATETIMESTAMP.toUri();
	}

}
