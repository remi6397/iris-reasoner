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
public class DateTerm implements ITerm<DateTerm> {

	private Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

	public DateTerm(int year, int month, int day) {
		cal.clear();
		cal = new GregorianCalendar(year, month, day);
	}

	public Object clone() {
		try {
			DateTerm dt = (DateTerm) super.clone();
			dt.cal = (Calendar) cal.clone();
			return cal;
		} catch (CloneNotSupportedException e) {
			assert true : "Will never happen";
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

	public int compareTo(DateTerm o) {
		if (o == null) {
			throw new NullPointerException("Can not compare with null");
		}
		return cal.compareTo(o.cal);
	}

	public DateTerm getMinValue() {
		return new DateTerm(0, 0, 0);
	}
}
