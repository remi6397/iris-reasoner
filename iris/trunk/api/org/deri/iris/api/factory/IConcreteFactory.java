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
package org.deri.iris.api.factory;

import org.deri.iris.api.terms.concrete.IBase64Binary;
import org.deri.iris.api.terms.concrete.IBooleanTerm;
import org.deri.iris.api.terms.concrete.IDateTerm;
import org.deri.iris.api.terms.concrete.IDateTime;
import org.deri.iris.api.terms.concrete.IDecimalTerm;
import org.deri.iris.api.terms.concrete.IDoubleTerm;
import org.deri.iris.api.terms.concrete.IDuration;
import org.deri.iris.api.terms.concrete.IFloatTerm;
import org.deri.iris.api.terms.concrete.IGDay;
import org.deri.iris.api.terms.concrete.IGMonth;
import org.deri.iris.api.terms.concrete.IGMonthDay;
import org.deri.iris.api.terms.concrete.IGYear;
import org.deri.iris.api.terms.concrete.IGYearMonth;
import org.deri.iris.api.terms.concrete.IHexBinary;
import org.deri.iris.api.terms.concrete.IIntegerTerm;
import org.deri.iris.api.terms.concrete.IIri;
import org.deri.iris.api.terms.concrete.ISqName;
import org.deri.iris.api.terms.concrete.ITime;

/**
 * <p>
 * An interface that can be used to create set of data types 
 * supported by this engine.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision$
 */
public interface IConcreteFactory {
	public IBase64Binary createBase64Binary(final String s);

	public IBooleanTerm createBoolean(final boolean b);

	/**
	 * Creates a datetime object with a given timezone.
	 * @param year the years
	 * @param month the months (starting at <code>0</code>)
	 * @param day day of the month
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute
	 * wheren't both positive, or negative
	 */
	public IDateTime createDateTime(final int year, final int month,
			final int day, final int hour, final int minute, final int second,
			final int tzHour, final int tzMinute);
	/**
	 * Creates a datetime object. The timezone will be set to GMT.
	 * @param year the years
	 * @param month the months (starting at <code>0</code>)
	 * @param day day of the month
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 */
	public IDateTime createDateTime(final int year, final int month,
			final int day, final int hour, final int minute, final int second);

	/**
	 * Creates a time object with a given timezone.
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute
	 * wheren't both positive, or negative
	 */
	public ITime createTime(final int hour, final int minute, final int second, final int tzHour, final int tzMinute);

	/**
	 * Creates a time object. The timezone will be set to GMT.
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 */
	public ITime createTime(final int hour, final int minute, final int second);

	public IDateTerm createDate(final int year, final int month, final int day);

	public IDecimalTerm createDecimal(final double d);

	public IDoubleTerm createDouble(final double d);

	/**
	 * Constructs a new duration.
	 * @param day the dayspan
	 * @param hour the hourspan
	 * @param minute the minutespan
	 * @param second the secondspan
	 */
	public IDuration createDuration(final int day, final int hour, final int minute, final int second);

	/**
	 * Contructs a new duration out of a given amount of milliseconds. The
	 * milliseconds will be round down to the next second.
	 * @param millis the millisecondspan
	 */
	public IDuration createDuration(final long millis);

	public IFloatTerm createFloat(final float f);

	public IGDay createGDay(final int day);

	public IGMonthDay createGMonthDay(final int month, final int day);

	public IGMonth createGMonth(final int month);

	public IGYearMonth createGYearMonth(final int year, final int month);

	public IIntegerTerm createInteger(final int i);

	public IGYear createGYear(final int year);

	public IHexBinary createHexBinary(final String s);

	public IIri createIri(final String s);

	public ISqName createSqName(final String s);

	public ISqName createSqName(final IIri iri, final String name);
}
