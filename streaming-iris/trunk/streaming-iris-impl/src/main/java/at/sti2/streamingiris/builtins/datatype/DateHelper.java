package at.sti2.streamingiris.builtins.datatype;

import java.util.TimeZone;

/**
 * A helper class for handling with dates, time zones, etc.
 */
class DateHelper {

	/**
	 * Returns the hour part of a time zone.
	 * 
	 * @param timeZone
	 *            The time zone.
	 * @return The hour part of the given time zone.
	 */
	static int getHourPart(TimeZone timeZone) {
		int offset = timeZone.getRawOffset();
		int tzHour = offset / 3600000;

		return tzHour;
	}

	/**
	 * Returns the minute part of a time zone.
	 * 
	 * @param timeZone
	 *            The time zone.
	 * @return The minute part of the given time zone.
	 */
	static int getMinutePart(TimeZone timeZone) {
		int offset = timeZone.getRawOffset();
		int tzMinute = (Math.abs(offset) % 3600000) / 60000;

		if (offset < 0) {
			tzMinute *= -1;
		}

		return tzMinute;
	}

}
