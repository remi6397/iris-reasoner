/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
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
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class DateTime implements IDateTime {

	/** Factory used to create the xml durations. */
	private static final DatatypeFactory FACTORY;

	/** The inner calendar object. */
	private XMLGregorianCalendar datetime;

	/** Milliseconds per minute. */
	private static final int MILLIS_PER_MINUTE = 1000 * 60;

	/** Milliseconds per hour. */
	private static final int MILLIS_PER_HOUR = MILLIS_PER_MINUTE * 60;
	
	public static final int MAX_TIMEZONE_HOURS = 14;
	public static final int MAX_TIMEZONE_MINUTES = 59;
	
	/**
	 * Implements the restrictions as detailed in: http://www.w3.org/TR/xmlschema-2/#dateTime
	 * ( |h| < 14 AND |m| <= 59 ) OR ( |h| = 14, m = 0 )
	 *   AND
	 * sign( h ) = sign( m )
	 * @param tzHour The time zone hours
	 * @param tzMinute The time zone minutes
	 */
	public static void checkTimeZone( int tzHour, int tzMinute )
	{
		// Sign of hours and minutes must be the same
		if (((tzHour < 0) && (tzMinute > 0)) || ((tzHour > 0) && (tzMinute < 0)))
			throw new IllegalArgumentException("The timezone hours and " + 
					"minutes must be both positive or both negative, but were " + 
					tzHour + " and " + tzMinute);
		
		// Magnitude of hours must not be greater than MAX_TIMEZONE_HOURS
		if( Math.abs( tzHour ) > MAX_TIMEZONE_HOURS )
			throw new IllegalArgumentException("The timezone hours magnitude can not be greater than " +
							MAX_TIMEZONE_HOURS + ", but was " + tzHour );

		// Magnitude of minutes must not be greater than MAX_TIMEZONE_MINUTES
		if( Math.abs( tzMinute ) > MAX_TIMEZONE_MINUTES )
			throw new IllegalArgumentException("The timezone minutes magnitude can not be greater than " +
							MAX_TIMEZONE_MINUTES + ", but was " + tzMinute );

		// Minutes must be zero if hours are +/- 14
		if( Math.abs( tzHour ) == MAX_TIMEZONE_HOURS )
		{
			if( tzMinute != 0 )
				throw new IllegalArgumentException("The timezone minutes must be zero when the timezone hours magnitude is " +
								MAX_TIMEZONE_HOURS + ", but was " + tzMinute );
		}
		
	}

	static {
		// creating the factory
		DatatypeFactory tmp = null;
		try {
			tmp = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new IllegalArgumentException(
					"Couldn't create the factory for the datetime", e);
		}
		FACTORY = tmp;
	}

	/**
	 * Constructs a new time object. With the <code>tzHour</code> and
	 * <code>tzMinute</code> set to <code>0</code>.
	 * @param year the year
	 * @param month the month (1-12)
	 * @param day day of the month
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if the tzHour and tzMinute
	 * wheren't both positive, or negative
	 */
	DateTime(final int year, final int month, final int day, 
			final int hour, final int minute, final int second,
			final int tzHour, final int tzMinute) {
		this(year, month, day, hour, minute, second, 0, tzHour, tzMinute);
	}

	/**
	 * Constructs a new time object. With the <code>tzHour</code> and
	 * <code>tzMinute</code> set to <code>0</code>.
	 * @param year the year
	 * @param month the month (starting at <code>0</code>)
	 * @param day day of the month
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 */
	DateTime(final int year, final int month, final int day, 
			final int hour, final int minute, final int second) {
		this(year, month, day, hour, minute, second, 0, 0, 0);
	}

	/**
	 * Constructs a new datetime object with a given timezone.
	 * @param year the year
	 * @param month the month (starting at <code>0</code>)
	 * @param day day of the month
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 * @param millisecond the milliseconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if the tzHour and tzMinute
	 * wheren't both positive, or negative
	 */
	DateTime(final int year, final int month, final int day, 
			final int hour, final int minute, final int second, final int millisecond, 
			final int tzHour, final int tzMinute) {

		checkTimeZone( tzHour, tzMinute );

		datetime = FACTORY.newXMLGregorianCalendar(
				BigInteger.valueOf((long) year), 
				month, 
				day, 
				hour, 
				minute, 
				second, 
				new BigDecimal(millisecond / 1000l), 
				tzHour * 60 + tzMinute);
	}

	public int compareTo(ITerm o) {
		if (o == null) {
			return 1;
		}
		
		DateTime dt = (DateTime) o;
		return datetime.compare(dt.getValue());
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof DateTime)) {
			return false;
		}
		DateTime dx = (DateTime) obj;
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

	public TimeZone getTimeZone() {
		return datetime.getTimeZone(0);
	}

	public int getYear() {
		return datetime.getYear();
	}

	public int getMillisecond() {
		return datetime.getMillisecond();
	}

	public int hashCode() {
		return datetime.hashCode();
	}

	public String toString() {
		return datetime.toString();
	}

	protected static int getTimeZoneHour(final TimeZone tz) {
		return tz.getRawOffset() / MILLIS_PER_HOUR;
	}

	protected static int getTimeZoneMinute(final TimeZone tz) {
		return (tz.getRawOffset() % MILLIS_PER_HOUR) / MILLIS_PER_MINUTE;
	}

	public boolean isGround() {
		return true;
	}

	public XMLGregorianCalendar getValue() {
		return (XMLGregorianCalendar) datetime.clone();
	}
}
