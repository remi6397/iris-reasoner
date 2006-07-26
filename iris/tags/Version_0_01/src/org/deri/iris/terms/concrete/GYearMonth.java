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
			assert true : "Can not happen";
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
}
