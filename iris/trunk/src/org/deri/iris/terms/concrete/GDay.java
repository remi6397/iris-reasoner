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

public class GDay implements IGDay, Cloneable {

	private Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));;

	GDay(final Calendar calendar) {
		this(calendar.get(Calendar.DAY_OF_MONTH));
	}

	GDay(final int day) {
		cal.clear();
		setDay(day);
	}

	public Object clone() {
		try {
			GDay gi = (GDay) super.clone();
			gi.cal = (Calendar) cal.clone();
			return gi;
		} catch (CloneNotSupportedException e) {
			assert false : "Object is always cloneable";
		}
		return null;
	}

	public int compareTo(IGDay o) {
		if (o == null) {
			throw new NullPointerException("Can not compare with null");
		}
		return getDay() - o.getDay();
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof GDay)) {
			return false;
		}
		GDay gi = (GDay) obj;
		return getDay() == gi.getDay();
	}

	public int getDay() {
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public int hashCode() {
		return cal.hashCode();
	}

	protected void setDay(int day) {
		cal.set(Calendar.DAY_OF_MONTH, day);
	}

	public String toString() {
		return getClass().getName() + "[day=" + getDay() + "]";
	}

	public boolean isGround() {
		return true;
	}

	public IGDay getMinValue() {
		return new GDay(1);
	}

	public Integer getValue() {
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public void setValue(Integer t) {
		if (t == null) {
			throw new IllegalArgumentException("The value must not be null");
		}
		cal.set(Calendar.DAY_OF_MONTH, t);
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
	public IGDay add(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof IDateTerm) {
			final IDateTerm d = (IDateTerm) t;
			return new GDay(getDay() + d.getDay());
		} else if (t instanceof IDateTime) {
			final IDateTime d = (IDateTime) t;
			return new GDay(getDay() + d.getDay());
		} else if (t instanceof IDuration) {
			final IDuration d = (IDuration) t;
			return new GDay(getDay() + d.getDay());
		} else if (t instanceof IGDay) {
			final IGDay d = (IGDay) t;
			return new GDay(getDay() + d.getDay());
		} else if (t instanceof IGMonth) {
			return new GDay(getDay());
		} else if (t instanceof IGYear) {
			return new GDay(getDay());
		} else if (t instanceof IGMonthDay) {
			final IGMonthDay d = (IGMonthDay) t;
			return new GDay(getDay() + d.getDay());
		} else if (t instanceof IGYearMonth) {
			return new GDay(getDay());
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
	public IGDay divide(final ITerm t) {
		throw new UnsupportedOperationException(
				"Can't perform this operation on that term");
	}

	/**
	 * <b>This operation is not supported by this term.</b>
	 * 
	 * @throws UnsupportedOperationException
	 *             this operation is not supported
	 */
	public IGDay multiply(final ITerm t) {
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
	public IGDay subtract(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof IDateTerm) {
			final IDateTerm d = (IDateTerm) t;
			return new GDay(getDay() - d.getDay());
		} else if (t instanceof IDateTime) {
			final IDateTime d = (IDateTime) t;
			return new GDay(getDay() - d.getDay());
		} else if (t instanceof IDuration) {
			final IDuration d = (IDuration) t;
			return new GDay(getDay() - d.getDay());
		} else if (t instanceof IGDay) {
			final IGDay d = (IGDay) t;
			return new GDay(getDay() - d.getDay());
		} else if (t instanceof IGMonth) {
			return new GDay(getDay());
		} else if (t instanceof IGYear) {
			return new GDay(getDay());
		} else if (t instanceof IGMonthDay) {
			final IGMonthDay d = (IGMonthDay) t;
			return new GDay(getDay() - d.getDay());
		} else if (t instanceof IGYearMonth) {
			return new GDay(getDay());
		}
		throw new IllegalArgumentException(
				"Can perform this task only with date like terms, but was "
						+ t.getClass());
	}
}
