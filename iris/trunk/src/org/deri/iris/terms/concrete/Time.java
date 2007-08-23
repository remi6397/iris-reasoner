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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.deri.iris.api.terms.concrete.ITime;

/**
 * <p>
 * Simple implementation of ITime.
 * </p>
 * <p>
 * $Id: Time.java,v 1.4 2007-08-23 09:48:10 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.4 $
 */
public class Time implements ITime, Cloneable {

	/** SimpleDateFormat to parse the time. */
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"HH:mm:ssz");

	/** Format for the {@link #toString()} method. The format is 'hh:mm:ss&lt;timezondediff&gt;'. */
	private static final String TOSTRING_FORMAT = "%tT%tz";

	/** Milliseconds per hour. */
	private static final int MILLIS_PER_HOUR = 1000 * 60 * 60;

	/** Milliseconds per minute. */
	private static final int MILLIS_PER_MINUTE = 1000 * 60;

	/** The internal calendar holding the time. */
	private Calendar cal;

	/**
	 * Constructs a new time object taking all needed parameters out of a
	 * given calendar.
	 * @param cal the calendar holding the data
	 */
	Time(final Calendar cal) {
		this(cal.get(Calendar.HOUR_OF_DAY), cal
			.get(Calendar.MINUTE), cal.get(Calendar.SECOND),
			getTimeZoneHour(cal.getTimeZone()),
			getTimeZoneMinute(cal .getTimeZone()));
	}

	/**
	 * Constructs a new time object. With the <code>tzHour</code> and
	 * <code>tzMinute</code> set to <code>0</code>.
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 */
	Time(int hour, int minute, int second) {
		this(hour, minute, second, 0, 0);
	}

	/**
	 * Constructs a new time object with a given timezone.
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute
	 * wheren't both positive, or negative
	 */
	Time(int hour, int minute, int second, int tzHour, int tzMinute) {
		if (((tzHour < 0) && (tzMinute > 0)) || ((tzHour > 0) && (tzMinute < 0))) {
			throw new IllegalArgumentException("Both, the timezone hours and " + 
					"minutes must be negative, or positive, but were " + 
					tzHour + " and " + tzMinute);
		}

		final String timezone = "GMT" + 
			(((tzHour >= 0) && (tzMinute >= 0)) ? "+" : "-") + Math.abs(tzHour) + ":" + 
			((Math.abs(tzMinute) < 10) ? "0" : "") + Math.abs(tzMinute);

		cal = new GregorianCalendar(TimeZone.getTimeZone(timezone));
		cal.clear();
		cal.set(0, 0, 0, hour, minute, second);
	}

	public Object clone() {
		try {
			Time dt = (Time) super.clone();
			dt.cal = (Calendar) cal.clone();
			return dt;
		} catch (CloneNotSupportedException e) {
			assert false : "Object is always cloneable";
		}
		return null;
	}

	public int compareTo(ITime o) {
		if (o == null) {
			return 1;
		}
		return cal.compareTo(o.getTime());
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof Time)) {
			return false;
		}
		Time dx = (Time) obj;
		return (dx.getHour() == this.getHour())
				&& (dx.getMinute() == this.getMinute())
				&& (dx.getSecond() == this.getSecond())
				&& (dx.getTimeZone().getRawOffset() == this.getTimeZone()
						.getRawOffset());
	}

	public Calendar getTime() {
		return (Calendar) cal.clone();
	}

	public int getHour() {
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinute() {
		return cal.get(Calendar.MINUTE);
	}

	public int getSecond() {
		return cal.get(Calendar.SECOND);
	}

	public TimeZone getTimeZone() {
		return cal.getTimeZone();
	}

	public int hashCode() {
		return cal.hashCode();
	}

	public String toString() {
		return String.format(TOSTRING_FORMAT, cal, cal);
	}

	protected static int getTimeZoneHour(final TimeZone tz) {
		return tz.getRawOffset() / MILLIS_PER_HOUR;
	}

	protected static int getTimeZoneMinute(final TimeZone tz) {
		return (tz.getRawOffset() % MILLIS_PER_HOUR) / MILLIS_PER_MINUTE;
	}

	/**
	 * Parses a String to a Date object. The format must be
	 * "HH:mm:ssz".
	 * 
	 * @param str
	 *            the String to parse
	 * @see SimpleDateFormat
	 */
	public static Time parse(String str) {
		try {
			Calendar cal = new GregorianCalendar();
			cal.setTime(FORMAT.parse(str));
			return new Time(cal);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Wasn't able to parse: " + str
					+ ". The String must have the format: "
					+ FORMAT.toPattern());
		}
	}

	public boolean isGround() {
		return true;
	}

	public Calendar getValue() {
		// TODO shouldn't a copy be returned?
		return cal;
	}

	public void setValue(Calendar t) {
		// TODO shouldn't a copy be made?
		if (t == null) {
			throw new IllegalArgumentException("The value must not be null");
		}
		cal = t;
	}
}
