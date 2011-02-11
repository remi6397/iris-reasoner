/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.terms.concrete;

import java.net.URI;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IGMonthDay;

/**
 * <p>
 * Simple implementation of the IGMonthDay.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class GMonthDay implements IGMonthDay {

	/** Factory used to create the xml durations. */
	private static final DatatypeFactory FACTORY;

	/** The inner calendar object. */
	private final XMLGregorianCalendar date;

	static {
		// creating the factory
		DatatypeFactory tmp = null;
		try {
			tmp = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new IllegalArgumentException(
					"Couldn't create the factory for the monthday", e);
		}
		FACTORY = tmp;
	}

	/**
	 * Creates a new monthday. The timezone will be GMT.
	 * @param month the month (1-12)
	 * @param day the day
	 */
	GMonthDay(final int month, final int day) {
		this(month, day, 0, 0);
	}

	/**
	 * Creates a new monthday within the given timezone.
	 * @param month the month (1-12)
	 * @param day the day
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if the tzHour and tzMinute
	 * wheren't both positive, or negative
	 */
	GMonthDay(final int month, final int day, final int tzHour, final int tzMinute) {
		if (((tzHour < 0) && (tzMinute > 0)) || ((tzHour > 0) && (tzMinute < 0))) {
			throw new IllegalArgumentException("Both, the timezone hours and " + 
					"minutes must be negative, or positive, but were " + 
					tzHour + " and " + tzMinute);
		}

		date = FACTORY.newXMLGregorianCalendarDate(
				DatatypeConstants.FIELD_UNDEFINED, 
				month, 
				day, 
				tzHour * 60 + tzMinute);
	}

	public int compareTo(ITerm o) {
		if (o == null) {
			return 1;
		}
		
		GMonthDay gmd = (GMonthDay) o;
		int iResult = getMonth() - gmd.getMonth();
		if (iResult != 0) {
			return iResult;
		}
		return getDay() - gmd.getDay();
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof IGMonthDay)) {
			return false;
		}
		IGMonthDay monthday = (IGMonthDay) obj;
		return ((monthday.getDay() == getDay()) && (monthday.getMonth() == getMonth()));
	}

	public int getDay() {
		return date.getDay();
	}

	public int getMonth() {
		return date.getMonth();
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

	public Integer[] getValue() {
		return new Integer[]{date.getMonth(), date.getDay()};
	}

	public URI getDatatypeIRI() {
		return URI.create("http://www.w3.org/2001/XMLSchema#gMonthDay");
	}

	public String toCanonicalString() {
		return date.toString();
	}
}
