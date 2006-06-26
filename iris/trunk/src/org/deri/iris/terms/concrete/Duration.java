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

import org.deri.iris.api.terms.concrete.IDuration;

public class Duration implements IDuration, Cloneable {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss");

	private Calendar cal;

	Duration(int year, int month, int day, int hour, int minute,
			int second) {
		cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		cal.clear();
		cal.set(year, month, day, hour, minute, second);
	}

	Duration(final Calendar cal) {
		this(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
				.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal
				.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof Duration)) {
			return false;
		}
		Duration dx = (Duration) obj;
		return (dx.getYear() == this.getYear())
				&& (dx.getMonth() == this.getMonth())
				&& (dx.getDay() == this.getDay())
				&& (dx.getHour() == this.getHour())
				&& (dx.getMinute() == this.getMinute())
				&& (dx.getSecond() == this.getSecond());
	}

	public Calendar getDateTime() {
		return (Calendar) cal.clone();
	}

	public int getDay() {
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public int getHour() {
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinute() {
		return cal.get(Calendar.MINUTE);
	}

	public int getMonth() {
		return cal.get(Calendar.MONTH);
	}

	public int getSecond() {
		return cal.get(Calendar.SECOND);
	}

	public int getYear() {
		return cal.get(Calendar.YEAR);
	}

	public int hashCode() {
		return cal.hashCode();
	}

	/**
	 * Parses a String to a Duration object. The format must be
	 * "yyyy-MM-dd'T'HH:mm:ss".
	 * 
	 * @param str
	 *            the String to parse
	 * @return the parsed objecrt
	 * @see SimpleDateFormat
	 */
	public static Duration parse(String str) {
		try {
			Calendar cal = new GregorianCalendar();
			cal.setTime(FORMAT.parse(str));
			return new Duration(cal);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Wasn't able to parse: " + str
					+ ". The String must have the format: "
					+ FORMAT.toPattern());
		}
	}

	public String toString() {
		return FORMAT.format(cal.getTime());
	}

	public Object clone() {
		try {
			Duration di = (Duration) super.clone();
			di.cal = (Calendar) cal.clone();
			return di;
		} catch (CloneNotSupportedException e) {
			assert true : "Can not happen";
		}
		return null;
	}

	public int compareTo(IDuration o) {
		if (o == null) {
			throw new NullPointerException("Can not compare with null");
		}
		return cal.compareTo(o.getDateTime());
	}

	public boolean isGround() {
		return true;
	}

	public IDuration getMinValue() {
		return new Duration(0, 0, 0, 0, 0, 0);
	}
}
