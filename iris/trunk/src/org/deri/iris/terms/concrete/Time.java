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
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.ITime;

/**
 * <p>
 * Simple implementation of ITime.
 * </p>
 * <p>
 * $Id: Time.java,v 1.6 2007-10-09 20:29:37 bazbishop237 Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.6 $
 */
public class Time implements ITime, Cloneable {

	/** Factory used to create the xml durations. */
	private static final DatatypeFactory FACTORY;

	/** The inner calendar object. */
	private XMLGregorianCalendar time;

	/** Milliseconds per minute. */
	private static final int MILLIS_PER_MINUTE = 1000 * 60;

	/** Milliseconds per hour. */
	private static final int MILLIS_PER_HOUR = MILLIS_PER_MINUTE * 60;

	static {
		// creating the factory
		DatatypeFactory tmp = null;
		try {
			tmp = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new IllegalArgumentException(
					"Couldn't create the factory for the time", e);
		}
		FACTORY = tmp;
	}

	/**
	 * Constructs a new time object. With the <code>tzHour</code> and
	 * <code>tzMinute</code> set to <code>0</code>.
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 */
	Time(int hour, int minute, int second) {
		this(hour, minute, second, 0, 0, 0);
	}

	/**
	 * Constructs a new time object. Within the given timezone.
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute
	 * wheren't both positive, or negative
	 */
	Time(int hour, int minute, int second, final int tzHour, final int tzMinute) {
		this(hour, minute, second, 0, tzHour, tzMinute);
	}

	/**
	 * Constructs a new time object with a given timezone.
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 * @param millisecond the milliseconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute
	 * wheren't both positive, or negative
	 */
	Time(final int hour, final int minute, final int second, final int millisecond, 
			final int tzHour, final int tzMinute) {
		if (((tzHour < 0) && (tzMinute > 0)) || ((tzHour > 0) && (tzMinute < 0))) {
			throw new IllegalArgumentException("Both, the timezone hours and " + 
					"minutes must be negative, or positive, but were " + 
					tzHour + " and " + tzMinute);
		}

		time = FACTORY.newXMLGregorianCalendarTime(hour, 
				minute, 
				second, 
				new BigDecimal(millisecond / 1000l),
				tzHour * 60 + tzMinute);
	}

	public Object clone() {
		try {
			Time dt = (Time) super.clone();
			dt.time = (XMLGregorianCalendar) time.clone();
			return dt;
		} catch (CloneNotSupportedException e) {
			assert false : "Object is always cloneable";
		}
		return null;
	}

	public int compareTo(ITerm o) {
		if (o == null) {
			return 1;
		}
		
		Time t = (Time) o;
		return time.compare(t.getValue());
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof Time)) {
			return false;
		}
		Time dx = (Time) obj;
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
		assert tz != null: "The timezone must not be null";

		return tz.getRawOffset() / MILLIS_PER_HOUR;
	}

	protected static int getTimeZoneMinute(final TimeZone tz) {
		assert tz != null: "The timezone must not be null";

		return (tz.getRawOffset() % MILLIS_PER_HOUR) / MILLIS_PER_MINUTE;
	}

	public boolean isGround() {
		return true;
	}

	public XMLGregorianCalendar getValue() {
		return (XMLGregorianCalendar) time.clone();
	}

	public void setValue(final XMLGregorianCalendar t) {
		// TODO shouldn't a copy be made?
		if (t == null) {
			throw new IllegalArgumentException("The value must not be null");
		}
		time = t;
	}
}
