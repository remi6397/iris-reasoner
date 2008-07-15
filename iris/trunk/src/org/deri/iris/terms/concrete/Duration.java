/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.terms.concrete;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;

import org.deri.iris.api.terms.concrete.IDuration;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * Simple implementation of the IDuration.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class Duration implements IDuration {

	/** Calendar used to calculate the milliseconds. */
	private static final Calendar ZERO;

	/** Factory used to create the xml durations. */
	private static final DatatypeFactory FACTORY;

	/** The inner duration object. */
	private final javax.xml.datatype.Duration duration;

	/** Milliseconds per second. */
	private static final BigDecimal MILLIS_PER_SECOND = new BigDecimal(1000);

	static {
		// creating the calendar
		ZERO = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		ZERO.clear();

		try {
			FACTORY = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new IllegalArgumentException(
					"Couldn't create the factory for the duration", e);
		}
	}

	/**
	 * Constructs a new duration.
	 * @param year the yearspan
	 * @param month the monthspan
	 * @param day the dayspan
	 * @param hour the hourspan
	 * @param minute the minutespan
	 * @param second the secondspan
	 * @throws IllegalArgumentException if the year is negative
	 * @throws IllegalArgumentException if the month is negative
	 * @throws IllegalArgumentException if the day is negative
	 * @throws IllegalArgumentException if the hour is negative
	 * @throws IllegalArgumentException if the minute is negative
	 * @throws IllegalArgumentException if the second is negative
	 */
	Duration(final int year, final int month, final int day, 
			final int hour, final int minute, final int second) {
		this(year, month, day, hour, minute, second, 0);
	}

	/**
	 * Constructs a new duration.
	 * @param year the yearspan
	 * @param month the monthspan
	 * @param day the dayspan
	 * @param hour the hourspan
	 * @param minute the minutespan
	 * @param second the secondspan
	 * @param millisecond the millisecondspan
	 * @throws IllegalArgumentException if the year is negative
	 * @throws IllegalArgumentException if the month is negative
	 * @throws IllegalArgumentException if the day is negative
	 * @throws IllegalArgumentException if the hour is negative
	 * @throws IllegalArgumentException if the minute is negative
	 * @throws IllegalArgumentException if the second is negative
	 * @throws IllegalArgumentException if the millisecond is negative
	 */
	Duration(final int year, final int month, final int day, final int hour, 
			final int minute, final int second, final int millisecond) {
		this(true, year, month, day, hour, minute, second, millisecond);
	}
	/**
	 * Constructs a new duration.
	 * @param positive <code>true</code>if the duration is positive,
	 * otherwise <code>false</code>
	 * @param year the yearspan
	 * @param month the monthspan
	 * @param day the dayspan
	 * @param hour the hourspan
	 * @param minute the minutespan
	 * @param second the secondspan
	 * @param millisecond the millisecondspan
	 * @throws IllegalArgumentException if the year is negative
	 * @throws IllegalArgumentException if the month is negative
	 * @throws IllegalArgumentException if the day is negative
	 * @throws IllegalArgumentException if the hour is negative
	 * @throws IllegalArgumentException if the minute is negative
	 * @throws IllegalArgumentException if the second is negative
	 * @throws IllegalArgumentException if the millisecond is negative
	 */
	Duration(final boolean positive, final int year, final int month, final int day, final int hour, 
			final int minute, final int second, final int millisecond) {
		if (year < 0) {
			throw new IllegalArgumentException("The year must not be negative");
		}
		if (month < 0) {
			throw new IllegalArgumentException("The month must not be negative");
		}
		if (day < 0) {
			throw new IllegalArgumentException("The day must not be negative");
		}
		if (hour < 0) {
			throw new IllegalArgumentException("The hour must not be negative");
		}
		if (minute < 0) {
			throw new IllegalArgumentException("The minute must not be negative");
		}
		if (second < 0) {
			throw new IllegalArgumentException("The second must not be negative");
		}
		if (millisecond < 0) {
			throw new IllegalArgumentException("The millisecond must not be negative");
		}

		// prepare the seconds
		BigDecimal seconds = (new BigDecimal(millisecond)).divide(MILLIS_PER_SECOND).add(new BigDecimal(second));

		duration = FACTORY.newDuration(positive, 
				BigInteger.valueOf((long) year), 
				BigInteger.valueOf((long) month), 
				BigInteger.valueOf((long) day), 
				BigInteger.valueOf((long) hour), 
				BigInteger.valueOf((long) minute), 
				seconds);
	}

	/**
	 * Contructs a new duration out of a given amount of milliseconds. The
	 * milliseconds will be round down to the next second.
	 * @param millis the millisecondspan
	 */
	Duration(final long millis) {
		duration = FACTORY.newDuration(millis);
	}

	public int getYear() {
		return duration.getYears();
	}

	public int getMonth() {
		return duration.getMonths();
	}

	public int getDay() {
		return duration.getDays();
	}

	public int getHour() {
		return duration.getHours();
	}

	public int getMinute() {
		return duration.getMinutes();
	}

	public int getSecond() {
		return duration.getSeconds();
	}

	public int getMillisecond() {
		return Long.valueOf(duration.getTimeInMillis(ZERO) % 1000).intValue();
	}

	public int hashCode() {
		return duration.hashCode();
	}

	/**
	 * <p>
	 * Returns a short string representation of this object. <b>The format
	 * of the returned string is subject to change.</b>
	 * </p>
	 * <p>
	 * The resutl is formatted according to the XML 1.0 specification.
	 * </p>
	 * @return the string representation
	 */
	public String toString() {
		return duration.toString();
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof IDuration)) {
			return false;
		}
		
		javax.xml.datatype.Duration thatDuration = ( (IDuration) obj).getValue();
		
		if( duration.equals( thatDuration ) )
			return true;

		// Else normalise in case we are running with java 1.5 runtime
		javax.xml.datatype.Duration thisDuration = normalise( duration );
		thatDuration = normalise( thatDuration );
		
		return thisDuration.equals( thatDuration );
	}
	
	public int compareTo(ITerm o) {
		if (o == null) {
			return 1;
		}
		
		javax.xml.datatype.Duration thatDuration = ( (IDuration) o).getValue();

		// Do these tests in case we are running with java 1.5 runtime
		javax.xml.datatype.Duration thisDuration = normalise( duration );
		thatDuration = normalise( thatDuration );

		return thisDuration.compare( thatDuration );
	}

	public boolean isGround() {
		return true;
	}

	public javax.xml.datatype.Duration getValue() {
		return duration;
	}

	/**
	 * Java runtime 1.5 is inconsistent with its handling of days in Duration objects.
	 * @param duration
	 * @return
	 */
	private javax.xml.datatype.Duration normalise( javax.xml.datatype.Duration duration )
	{
		final long DAYS_PER_MONTH = 30;
		final long DAYS_PER_YEAR = 365;

		BigInteger days   = (BigInteger) duration.getField( DatatypeConstants.DAYS );
		BigInteger months = (BigInteger) duration.getField( DatatypeConstants.MONTHS );
		BigInteger years  = (BigInteger) duration.getField( DatatypeConstants.YEARS );
		
		BigInteger normalisedDays = years.multiply( BigInteger.valueOf( DAYS_PER_YEAR ) );
		normalisedDays = normalisedDays.add( months.multiply( BigInteger.valueOf( DAYS_PER_MONTH ) ) );
		normalisedDays = normalisedDays.add( days );
		
		BigInteger hours   = (BigInteger) duration.getField( DatatypeConstants.HOURS );
		BigInteger minutes = (BigInteger) duration.getField( DatatypeConstants.MINUTES );
		BigDecimal seconds = (BigDecimal) duration.getField( DatatypeConstants.SECONDS );
		
		boolean positive = duration.getSign() >= 0;
		
		return FACTORY.newDuration( positive, null, null, normalisedDays, hours, minutes, seconds );
	}
}
