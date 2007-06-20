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

import org.deri.iris.api.terms.concrete.IGMonthDay;

/**
 * <p>
 * Simple implementation of the IGMonthDay.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision$
 */
public class GMonthDay implements IGMonthDay, Cloneable {

	private Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

	GMonthDay(final Calendar calendar) {
		this(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
	}

	GMonthDay(final int month, final int day) {
		cal.clear();
		setMonthDay(month, day);
	}

	public Object clone() {
		try {
			GMonthDay clone = (GMonthDay) super.clone();
			clone.cal = (Calendar) cal.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			assert false : "Object is always cloneable";
		}
		return null;
	}

	public int compareTo(IGMonthDay o) {
		if (o == null) {
			throw new NullPointerException("Can not compare with null");
		}
		int iResult = getMonth() - o.getMonth();
		if (iResult != 0) {
			return iResult;
		}
		return getDay() - o.getDay();
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof GMonthDay)) {
			return false;
		}
		GMonthDay monthday = (GMonthDay) obj;
		return ((monthday.getDay() == getDay()) && (monthday.getMonth() == getMonth()));
	}

	public int getDay() {
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public int getMonth() {
		return cal.get(Calendar.MONTH);
	}

	public int hashCode() {
		return cal.hashCode();
	}

	protected void setMonthDay(int month, int day) {
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.MONTH, month);
	}

	public String toString() {
		return getClass().getName() + "[month=" + getMonth() + ",day="
				+ getDay() + "]";
	}

	public boolean isGround() {
		return true;
	}

	public IGMonthDay getMinValue() {
		return new GMonthDay(Calendar.JANUARY, 1);
	}

	public Integer[] getValue() {
		return new Integer[] { cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH) };
	}

	public void setValue(Integer[] t) {
		if (t == null) {
			throw new IllegalArgumentException("The value must not be null");
		}
		if (t.length < 2) {
			throw new IllegalArgumentException(
					"The array must contain at least 2 fields");
		}
		cal.set(Calendar.MONTH, t[0]);
		cal.set(Calendar.DAY_OF_MONTH, t[1]);
	}
}
