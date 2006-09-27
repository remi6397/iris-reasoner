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

/**
 * 
 * imple of integer
 * 
 * Created on 26.04.2006 Committed by $Author$ $Source$,
 * 
 * @author Holger Lausen
 * 
 * @version $Revision$ $Date$
 */
public class DateTerm implements IDateTerm, Cloneable {

	private Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

	DateTerm(int year, int month, int day) {
		cal.clear();
		cal.set(year, month, day);
	}

	public Object clone() {
		try {
			DateTerm dt = (DateTerm) super.clone();
			dt.cal = (Calendar) cal.clone();
			return dt;
		} catch (CloneNotSupportedException e) {
			assert false : "Object is always cloneable";
		}
		return false;
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof DateTerm)) {
			return false;
		}
		DateTerm dt = (DateTerm) obj;
		return cal.equals(dt.cal);
	}

	public int hashCode() {
		return cal.hashCode();
	}

	public String toString() {
		return getClass().getName() + "[year=" + cal.get(Calendar.YEAR)
				+ ",month=" + cal.get(Calendar.MONTH) + ",day="
				+ Calendar.DAY_OF_MONTH + "]";
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(IDateTerm o) {
		if (o == null) {
			throw new NullPointerException("Can not compare with null");
		}
		int result = 0;
		if ((result = getYear() - o.getYear()) != 0) {
			return result;
		}
		if ((result = getMonth() - o.getMonth()) != 0) {
			return result;
		}
		return getDay() - o.getDay();
	}

	public DateTerm getMinValue() {
		return new DateTerm(0, Calendar.JANUARY, 1);
	}

	public int getMonth() {
		return cal.get(Calendar.MONTH);
	}

	public int getYear() {
		return cal.get(Calendar.YEAR);
	}

	public int getDay() {
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public Calendar getValue() {
		// TODO: shouldn't a copy be returned?
		return cal;
	}

	public void setValue(Calendar t) {
		if (t == null) {
			throw new NullPointerException("The value must not be null");
		}
		cal.set(Calendar.YEAR, t.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, t.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, t.get(Calendar.DAY_OF_MONTH));
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
	public IDateTerm add(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof IDateTerm) {
			final IDateTerm d = (IDateTerm) t;
			return new DateTerm(getYear() + d.getYear(), getMonth()
					+ d.getMonth(), getDay() + d.getDay());
		} else if (t instanceof IDateTime) {
			final IDateTime d = (IDateTime) t;
			return new DateTerm(getYear() + d.getYear(), getMonth()
					+ d.getMonth(), getDay() + d.getDay());
		} else if (t instanceof IDuration) {
			final IDuration d = (IDuration) t;
			return new DateTerm(getYear() + d.getYear(), getMonth()
					+ d.getMonth(), getDay() + d.getDay());
		} else if (t instanceof IGDay) {
			final IGDay d = (IGDay) t;
			return new DateTerm(getYear(), getMonth(), getDay() + d.getDay());
		} else if (t instanceof IGMonth) {
			final IGMonth d = (IGMonth) t;
			return new DateTerm(getYear(), getMonth() + d.getMonth(), getDay());
		} else if (t instanceof IGYear) {
			final IGYear d = (IGYear) t;
			return new DateTerm(getYear() + d.getYear(), getMonth(), getDay());
		} else if (t instanceof IGMonthDay) {
			final IGMonthDay d = (IGMonthDay) t;
			return new DateTerm(getYear(), getMonth() + d.getMonth(), getDay()
					+ d.getDay());
		} else if (t instanceof IGYearMonth) {
			final IGYearMonth d = (IGYearMonth) t;
			return new DateTerm(getYear() + d.getYear(), getMonth()
					+ d.getMonth(), getDay());
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
	public IDateTerm divide(final ITerm t) {
		throw new UnsupportedOperationException(
				"Can't perform this operation on that term");
	}

	/**
	 * <b>This operation is not supported by this term.</b>
	 * 
	 * @throws UnsupportedOperationException
	 *             this operation is not supported
	 */
	public IDateTerm multiply(final ITerm t) {
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
	public IDateTerm subtract(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof IDateTerm) {
			final IDateTerm d = (IDateTerm) t;
			return new DateTerm(getYear() - d.getYear(), getMonth()
					- d.getMonth(), getDay() - d.getDay());
		} else if (t instanceof IDateTime) {
			final IDateTime d = (IDateTime) t;
			return new DateTerm(getYear() - d.getYear(), getMonth()
					- d.getMonth(), getDay() - d.getDay());
		} else if (t instanceof IDuration) {
			final IDuration d = (IDuration) t;
			return new DateTerm(getYear() - d.getYear(), getMonth()
					- d.getMonth(), getDay() - d.getDay());
		} else if (t instanceof IGDay) {
			final IGDay d = (IGDay) t;
			return new DateTerm(getYear(), getMonth(), getDay() - d.getDay());
		} else if (t instanceof IGMonth) {
			final IGMonth d = (IGMonth) t;
			return new DateTerm(getYear(), getMonth() - d.getMonth(), getDay());
		} else if (t instanceof IGYear) {
			final IGYear d = (IGYear) t;
			return new DateTerm(getYear() - d.getYear(), getMonth(), getDay());
		} else if (t instanceof IGMonthDay) {
			final IGMonthDay d = (IGMonthDay) t;
			return new DateTerm(getYear(), getMonth() - d.getMonth(), getDay()
					- d.getDay());
		} else if (t instanceof IGYearMonth) {
			final IGYearMonth d = (IGYearMonth) t;
			return new DateTerm(getYear() - d.getYear(), getMonth()
					- d.getMonth(), getDay());
		}
		throw new IllegalArgumentException(
				"Can perform this task only with date like terms, but was "
						+ t.getClass());
	}
}
