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

import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IDateTerm;
import org.deri.iris.api.terms.concrete.IDateTime;
import org.deri.iris.api.terms.concrete.IDuration;
import org.deri.iris.api.terms.concrete.IGDay;
import org.deri.iris.api.terms.concrete.IGMonth;
import org.deri.iris.api.terms.concrete.IGMonthDay;
import org.deri.iris.api.terms.concrete.IGYear;
import org.deri.iris.api.terms.concrete.IGYearMonth;

public class Duration implements IDuration, Cloneable {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss");

	private Calendar cal;

	Duration(int year, int month, int day, int hour, int minute, int second) {
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
			assert false : "Object is always cloneable";
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

	public Calendar getValue() {
		return cal;
	}

	public void setValue(Calendar t) {
		if (t == null) {
			throw new IllegalArgumentException("The value must not be null");
		}
		cal.set(Calendar.YEAR, t.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, t.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, t.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, t.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, t.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, t.get(Calendar.SECOND));
	}

	/**
	 * Constructs a term which migth represent the sum of <code>this</code>
	 * and the other term. The submitted term must be a term which represents a
	 * date.
	 * 
	 * @param t
	 *            the other summand
	 * @return the sum of both terms
	 * @throws NullPointerException
	 *             if the term is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the term isn't a <code>DateTerm</code>
	 */
	public IDuration add(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof IDateTerm) {
			final IDateTerm d = (IDateTerm) t;
			return new Duration(getYear() + d.getYear(), getMonth()
					+ d.getMonth(), getDay() + d.getDay(), getHour(),
					getMinute(), getSecond());
		} else if (t instanceof IDateTime) {
			final IDateTime d = (IDateTime) t;
			return new Duration(getYear() + d.getYear(), getMonth()
					+ d.getMonth(), getDay() + d.getDay(), getHour()
					+ d.getHour(), getMinute() + d.getMinute(), getSecond()
					+ d.getSecond());
		} else if (t instanceof IDuration) {
			final IDuration d = (IDuration) t;
			return new Duration(getYear() + d.getYear(), getMonth()
					+ d.getMonth(), getDay() + d.getDay(), getHour()
					+ d.getHour(), getMinute() + d.getMinute(), getSecond()
					+ d.getSecond());
		} else if (t instanceof IGDay) {
			final IGDay d = (IGDay) t;
			return new Duration(getYear(), getMonth(), getDay() + d.getDay(),
					getHour(), getMinute(), getSecond());
		} else if (t instanceof IGMonth) {
			final IGMonth d = (IGMonth) t;
			return new Duration(getYear(), getMonth() + d.getMonth(), getDay(),
					getHour(), getMinute(), getSecond());
		} else if (t instanceof IGYear) {
			final IGYear d = (IGYear) t;
			return new Duration(getYear() + d.getYear(), getMonth(), getDay(),
					getHour(), getMinute(), getSecond());
		} else if (t instanceof IGMonthDay) {
			final IGMonthDay d = (IGMonthDay) t;
			return new Duration(getYear(), getMonth() + d.getMonth(), getDay()
					+ d.getDay(), getHour(), getMinute(), getSecond());
		} else if (t instanceof IGYearMonth) {
			final IGYearMonth d = (IGYearMonth) t;
			return new Duration(getYear() + d.getYear(), getMonth()
					+ d.getMonth(), getDay(), getHour(), getMinute(),
					getSecond());
		}
		throw new IllegalArgumentException(
				"Can perform this task only with date like terms, but was "
						+ t.getClass());
	}

	/**
	 * <b>This operation is not supported by this term.</b>
	 * 
	 * @throws UnsupportedOperationException
	 *             this operation is not supported
	 */
	public IDuration divide(final ITerm t) {
		throw new UnsupportedOperationException(
				"Can't perform this operation on that term");
	}

	/**
	 * <b>This operation is not supported by this term.</b>
	 * 
	 * @throws UnsupportedOperationException
	 *             this operation is not supported
	 */
	public IDuration multiply(final ITerm t) {
		throw new UnsupportedOperationException(
				"Can't perform this operation on that term");
	}

	/**
	 * Constructs a term which migth represent the difference of
	 * <code>this</code> and the other term. The submitted term must be a term
	 * which represents a date.
	 * 
	 * @param t
	 *            the subtrahend
	 * @return the difference of both terms
	 * @throws NullPointerException
	 *             if the term is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the term isn't a <code>DateTerm</code>
	 */
	public IDuration subtract(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof IDateTerm) {
			final IDateTerm d = (IDateTerm) t;
			return new Duration(getYear() - d.getYear(), getMonth()
					- d.getMonth(), getDay() - d.getDay(), getHour(),
					getMinute(), getSecond());
		} else if (t instanceof IDateTime) {
			final IDateTime d = (IDateTime) t;
			return new Duration(getYear() - d.getYear(), getMonth()
					- d.getMonth(), getDay() - d.getDay(), getHour()
					- d.getHour(), getMinute() - d.getMinute(), getSecond()
					- d.getSecond());
		} else if (t instanceof IDuration) {
			final IDuration d = (IDuration) t;
			return new Duration(getYear() - d.getYear(), getMonth()
					- d.getMonth(), getDay() - d.getDay(), getHour()
					- d.getHour(), getMinute() - d.getMinute(), getSecond()
					- d.getSecond());
		} else if (t instanceof IGDay) {
			final IGDay d = (IGDay) t;
			return new Duration(getYear(), getMonth(), getDay() - d.getDay(),
					getHour(), getMinute(), getSecond());
		} else if (t instanceof IGMonth) {
			final IGMonth d = (IGMonth) t;
			return new Duration(getYear(), getMonth() - d.getMonth(), getDay(),
					getHour(), getMinute(), getSecond());
		} else if (t instanceof IGYear) {
			final IGYear d = (IGYear) t;
			return new Duration(getYear() - d.getYear(), getMonth(), getDay(),
					getHour(), getMinute(), getSecond());
		} else if (t instanceof IGMonthDay) {
			final IGMonthDay d = (IGMonthDay) t;
			return new Duration(getYear(), getMonth() - d.getMonth(), getDay()
					- d.getDay(), getHour(), getMinute(), getSecond());
		} else if (t instanceof IGYearMonth) {
			final IGYearMonth d = (IGYearMonth) t;
			return new Duration(getYear() - d.getYear(), getMonth()
					- d.getMonth(), getDay(), getHour(), getMinute(),
					getSecond());
		}
		throw new IllegalArgumentException(
				"Can perform this task only with date like terms, but was "
						+ t.getClass());
	}
}
