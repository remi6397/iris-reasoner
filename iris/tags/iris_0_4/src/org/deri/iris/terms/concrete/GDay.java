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

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.deri.iris.api.terms.concrete.IGDay;

/**
 * <p>
 * Simple implementation of the IGDay.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class GDay implements IGDay, Cloneable {

	/** Factory used to create the xml durations. */
	private static final DatatypeFactory FACTORY;

	/** The inner calendar object. */
	private XMLGregorianCalendar date;

	static {
		// creating the factory
		DatatypeFactory tmp = null;
		try {
			tmp = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new IllegalArgumentException(
					"Couldn't create the factory for the year", e);
		}
		FACTORY = tmp;
	}

	/**
	 * Creates a new day. The timezone will be set to GMT.
	 * @param day the day
	 */
	GDay(final int day) {
		this(day, 0, 0);
	}

	/**
	 * Creates a new day within the given timezone.
	 * @param day the day
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if the tzHour and tzMinute
	 * wheren't both positive, or negative
	 */
	GDay(final int day, final int tzHour, final int tzMinute) {
		if (((tzHour < 0) && (tzMinute > 0)) || ((tzHour > 0) && (tzMinute < 0))) {
			throw new IllegalArgumentException("Both, the timezone hours and " + 
					"minutes must be negative, or positive, but were " + 
					tzHour + " and " + tzMinute);
		}

		date = FACTORY.newXMLGregorianCalendarDate(DatatypeConstants.FIELD_UNDEFINED,
				DatatypeConstants.FIELD_UNDEFINED, 
				day, 
				tzHour * 60 + tzMinute);
	}

	public Object clone() {
		try {
			GDay gi = (GDay) super.clone();
			gi.date = (XMLGregorianCalendar) date.clone();
			return gi;
		} catch (CloneNotSupportedException e) {
			assert false : "Object is always cloneable";
		}
		return null;
	}

	public int compareTo(IGDay o) {
		if (o == null) {
			return 1;
		}
		return getDay() - o.getDay();
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof IGDay)) {
			return false;
		}
		IGDay gi = (IGDay) obj;
		return getDay() == gi.getDay();
	}

	public int getDay() {
		return date.getDay();
	}

	public int hashCode() {
		return date.hashCode();
	}

	public String toString() {
		return date.toString();
	}

	public boolean isGround() {
		return true;
	}

	public Integer getValue() {
		return date.getDay();
	}

	public void setValue(Integer t) {
		if (t == null) {
			throw new IllegalArgumentException("The value must not be null");
		}
		date.setDay(t);
	}
}
