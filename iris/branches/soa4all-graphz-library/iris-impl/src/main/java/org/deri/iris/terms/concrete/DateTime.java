/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.terms.concrete;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IDateTime;

/**
 * <p>
 * Simple implementation of the IDateTime.
 * </p>
 */
public class DateTime implements IDateTime {

	/** Factory used to create the xml durations. */
	private static final DatatypeFactory FACTORY;

	/** The inner calendar object. */
	private final XMLGregorianCalendar datetime;

	/** Milliseconds per minute. */
	private static final int MILLIS_PER_MINUTE = 1000 * 60;

	/** Milliseconds per hour. */
	private static final int MILLIS_PER_HOUR = MILLIS_PER_MINUTE * 60;

	public static final int MAX_TIMEZONE_HOURS = 14;
	public static final int MAX_TIMEZONE_MINUTES = 59;

	/**
	 * Implements the restrictions as detailed in:
	 * http://www.w3.org/TR/xmlschema-2/#dateTime ( |h| < 14 AND |m| <= 59 ) OR
	 * ( |h| = 14, m = 0 ) AND sign( h ) = sign( m )
	 * 
	 * @param tzHour
	 *            The time zone hours
	 * @param tzMinute
	 *            The time zone minutes
	 */
	public static void checkTimeZone(int tzHour, int tzMinute) {
		// Sign of hours and minutes must be the same
		if (((tzHour < 0) && (tzMinute > 0))
				|| ((tzHour > 0) && (tzMinute < 0)))
			throw new IllegalArgumentException(
					"The timezone hours and "
							+ "minutes must be both positive or both negative, but were "
							+ tzHour + " and " + tzMinute);

		// Magnitude of hours must not be greater than MAX_TIMEZONE_HOURS
		if (Math.abs(tzHour) > MAX_TIMEZONE_HOURS)
			throw new IllegalArgumentException(
					"The timezone hours magnitude can not be greater than "
							+ MAX_TIMEZONE_HOURS + ", but was " + tzHour);

		// Magnitude of minutes must not be greater than MAX_TIMEZONE_MINUTES
		if (Math.abs(tzMinute) > MAX_TIMEZONE_MINUTES)
			throw new IllegalArgumentException(
					"The timezone minutes magnitude can not be greater than "
							+ MAX_TIMEZONE_MINUTES + ", but was " + tzMinute);

		// Minutes must be zero if hours are +/- 14
		if (Math.abs(tzHour) == MAX_TIMEZONE_HOURS) {
			if (tzMinute != 0)
				throw new IllegalArgumentException(
						"The timezone minutes must be zero when the timezone hours magnitude is "
								+ MAX_TIMEZONE_HOURS + ", but was " + tzMinute);
		}

	}

	static {
		// create the data type factory
		try {
			FACTORY = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(
					"Couldn't create the factory for the DateTime type", e);
		}
	}

	/**
	 * Constructs a new datetime object with a given timezone.
	 * 
	 * @param year
	 *            the year
	 * @param month
	 *            the month (starting at <code>0</code>)
	 * @param day
	 *            day of the month
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
	 *             if the tzHour and tzMinute wheren't both positive, or
	 *             negative
	 */
	DateTime(int year, int month, int day, int hour, int minute, int second,
			int millisecond, int tzHour, int tzMinute) {
		this(year, month, day, hour, minute, second + (millisecond / 1000.0),
				tzHour, tzMinute);

		if (millisecond < 0 || millisecond >= 1000)
			throw new IllegalArgumentException(
					"Millisecond value is out of range: " + second);

		if (second < 0 || second >= 60)
			throw new IllegalArgumentException("Second value is out of range: "
					+ second);
	}

	/**
	 * Constructs a new datetime object with a given timezone.
	 * 
	 * @param year
	 *            the year
	 * @param month
	 *            the month (starting at <code>0</code>)
	 * @param day
	 *            day of the month
	 * @param hour
	 *            the hours
	 * @param minute
	 *            the minutes
	 * @param second
	 *            the seconds
	 * @param tzHour
	 *            the timezone hours (relative to GMT)
	 * @param tzMinute
	 *            the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException
	 *             if the tzHour and tzMinute wheren't both positive, or
	 *             negative
	 */
	DateTime(int year, int month, int day, int hour, int minute, double second,
			int tzHour, int tzMinute) {

		checkTimeZone(tzHour, tzMinute);

		int intSeconds = (int) second;
		BigDecimal fractionalSeconds = new BigDecimal(Double.toString(second))
				.subtract(BigDecimal.valueOf(intSeconds));

		datetime = FACTORY.newXMLGregorianCalendar(BigInteger
				.valueOf((long) year), month, day, hour, minute, intSeconds,
				fractionalSeconds, tzHour * 60 + tzMinute);
	}

	public int compareTo(ITerm o) {
		if (o == null || !(o instanceof IDateTime)) {
			return 1;
		}

		IDateTime dt = (IDateTime) o;
		return datetime.compare(dt.getValue());
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof IDateTime)) {
			return false;
		}
		IDateTime dx = (IDateTime) obj;
		return datetime.equals(dx.getValue());
	}

	public int getDay() {
		return datetime.getDay();
	}

	public int getHour() {
		return datetime.getHour();
	}

	public int getMinute() {
		return datetime.getMinute();
	}

	public int getMonth() {
		return datetime.getMonth();
	}

	public int getSecond() {
		return datetime.getSecond();
	}

	public int getMillisecond() {
		return datetime.getMillisecond();
	}

	public double getDecimalSecond() {
		BigDecimal seconds = datetime.getFractionalSecond().add(
				BigDecimal.valueOf(datetime.getSecond()));
		return seconds.doubleValue();
	}

	public TimeZone getTimeZone() {
		return datetime.getTimeZone(0);
	}

	public int getYear() {
		return datetime.getYear();
	}

	public int hashCode() {
		return datetime.hashCode();
	}

	public String toString() {
		return datetime.toString();
	}

	// protected static int getTimeZoneHour(final TimeZone tz) {
	// return tz.getRawOffset() / MILLIS_PER_HOUR;
	// }
	//
	// protected static int getTimeZoneMinute(final TimeZone tz) {
	// return (tz.getRawOffset() % MILLIS_PER_HOUR) / MILLIS_PER_MINUTE;
	// }
	//
	public boolean isGround() {
		return true;
	}

	public XMLGregorianCalendar getValue() {
		return (XMLGregorianCalendar) datetime.clone();
	}

	public URI getDatatypeIRI() {
		return URI.create("http://www.w3.org/2001/XMLSchema#dateTime");
	}

	public String toCanonicalString() {
		return datetime.toString();
	}
}
