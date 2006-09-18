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

import org.deri.iris.api.terms.concrete.IGDay;

public class GDay implements IGDay, Cloneable {

	private Calendar cal = new GregorianCalendar(TimeZone
			.getTimeZone("GMT"));;

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
			gi.cal = (Calendar)cal.clone();
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

}
