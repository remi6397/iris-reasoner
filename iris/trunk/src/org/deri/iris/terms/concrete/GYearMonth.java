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

public class GYearMonth implements IGYearMonth, Cloneable {

	private Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

	GYearMonth(final Calendar calendar) {
		this(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
	}

	GYearMonth(final int year, final int month) {
		cal.clear();
		setYearMonth(year, month);
	}

	public Object clone() {
		try {
			GYearMonth gm = (GYearMonth) super.clone();
			gm.cal = (Calendar) cal.clone();
			return gm;
		} catch (CloneNotSupportedException e) {
			assert false : "Object is always cloneable";
		}
		return null;
	}

	public int compareTo(IGYearMonth o) {
		if (o == null) {
			throw new NullPointerException("Can not compare with null");
		}
		int iResult = getYear() - o.getYear();
		if (iResult != 0) {
			return iResult;
		}
		return getMonth() - o.getMonth();
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof GYearMonth)) {
			return false;
		}
		GYearMonth monthyear = (GYearMonth) obj;
		return ((monthyear.getMonth() == getMonth()) && (monthyear.getYear() == getYear()));
	}

	public int getMonth() {
		return cal.get(Calendar.MONTH);
	}

	public int getYear() {
		return cal.get(Calendar.YEAR);
	}

	public int hashCode() {
		return cal.hashCode();
	}

	protected void setYearMonth(final int year, final int month) {
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
	}

	public String toString() {
		return getClass().getName() + "[year=" + getYear() + ",month="
				+ getMonth() + "]";
	}

	public boolean isGround() {
		return true;
	}

	public IGYearMonth getMinValue() {
		// TODO: maybe year should be negative
		return new GYearMonth(1, 1);
	}

	public Integer[] getValue() {
		return new Integer[] { cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) };
	}

	public void setValue(Integer[] t) {
		if (t == null) {
			throw new IllegalArgumentException("The value must not be null");
		}
		if (t.length < 2) {
			throw new IllegalArgumentException(
					"The array must contain at least 2 fields");
		}
		cal.set(Calendar.YEAR, t[0]);
		cal.set(Calendar.MONTH, t[1]);
	}

	/**
	 * Constructs a term which migth represent the sum of <code>this</code> and
	 * the other term. The submitted term must be a term which represents a
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
	public IGYearMonth add(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof IDateTerm) {
			final IDateTerm d = (IDateTerm) t;
			return new GYearMonth(getYear() + d.getYear(), getMonth()
					+ d.getMonth());
		} else if (t instanceof IDateTime) {
			final IDateTime d = (IDateTime) t;
			return new GYearMonth(getYear() + d.getYear(), getMonth()
					+ d.getMonth());
		} else if (t instanceof IDuration) {
			final IDuration d = (IDuration) t;
			return new GYearMonth(getYear() + d.getYear(), getMonth()
					+ d.getMonth());
		} else if (t instanceof IGDay) {
			return new GYearMonth(getYear(), getMonth());
		} else if (t instanceof IGMonth) {
			final IGMonth d = (IGMonth) t;
			return new GYearMonth(getYear(), getMonth() + d.getMonth());
		} else if (t instanceof IGYear) {
			final IGYear d = (IGYear) t;
			return new GYearMonth(getYear() + d.getYear(), getMonth());
		} else if (t instanceof IGMonthDay) {
			final IGMonthDay d = (IGMonthDay) t;
			return new GYearMonth(getYear(), getMonth() + d.getMonth());
		} else if (t instanceof IGYearMonth) {
			final IGYearMonth d = (IGYearMonth) t;
			return new GYearMonth(getYear() + d.getYear(), getMonth()
					+ d.getMonth());
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
	public IGYearMonth divide(final ITerm t) {
		throw new UnsupportedOperationException(
				"Can't perform this operation on that term");
	}

	/**
	 * <b>This operation is not supported by this term.</b>
	 * 
	 * @throws UnsupportedOperationException
	 *             this operation is not supported
	 */
	public IGYearMonth multiply(final ITerm t) {
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
	public IGYearMonth subtract(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof IDateTerm) {
			final IDateTerm d = (IDateTerm) t;
			return new GYearMonth(getYear() - d.getYear(), getMonth()
					- d.getMonth());
		} else if (t instanceof IDateTime) {
			final IDateTime d = (IDateTime) t;
			return new GYearMonth(getYear() - d.getYear(), getMonth()
					- d.getMonth());
		} else if (t instanceof IDuration) {
			final IDuration d = (IDuration) t;
			return new GYearMonth(getYear() - d.getYear(), getMonth()
					- d.getMonth());
		} else if (t instanceof IGDay) {
			return new GYearMonth(getYear(), getMonth());
		} else if (t instanceof IGMonth) {
			final IGMonth d = (IGMonth) t;
			return new GYearMonth(getYear(), getMonth() - d.getMonth());
		} else if (t instanceof IGYear) {
			final IGYear d = (IGYear) t;
			return new GYearMonth(getYear() - d.getYear(), getMonth());
		} else if (t instanceof IGMonthDay) {
			final IGMonthDay d = (IGMonthDay) t;
			return new GYearMonth(getYear(), getMonth() - d.getMonth());
		} else if (t instanceof IGYearMonth) {
			final IGYearMonth d = (IGYearMonth) t;
			return new GYearMonth(getYear() - d.getYear(), getMonth()
					- d.getMonth());
		}
		throw new IllegalArgumentException(
				"Can perform this task only with date like terms, but was "
						+ t.getClass());
	}
}
