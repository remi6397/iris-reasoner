/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
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

import java.net.URI;

import org.deri.iris.api.terms.concrete.IAnyURI;
import org.deri.iris.api.terms.concrete.IBase64Binary;
import org.deri.iris.api.terms.concrete.IBooleanTerm;
import org.deri.iris.api.terms.concrete.IByteTerm;
import org.deri.iris.api.terms.concrete.IDateTerm;
import org.deri.iris.api.terms.concrete.IDateTime;
import org.deri.iris.api.terms.concrete.IDayTimeDuration;
import org.deri.iris.api.terms.concrete.IDecimalTerm;
import org.deri.iris.api.terms.concrete.IDoubleTerm;
import org.deri.iris.api.terms.concrete.IDuration;
import org.deri.iris.api.terms.concrete.IENTITY;
import org.deri.iris.api.terms.concrete.IFloatTerm;
import org.deri.iris.api.terms.concrete.IGDay;
import org.deri.iris.api.terms.concrete.IGMonth;
import org.deri.iris.api.terms.concrete.IGMonthDay;
import org.deri.iris.api.terms.concrete.IGYear;
import org.deri.iris.api.terms.concrete.IGYearMonth;
import org.deri.iris.api.terms.concrete.IHexBinary;
import org.deri.iris.api.terms.concrete.IID;
import org.deri.iris.api.terms.concrete.IIDREF;
import org.deri.iris.api.terms.concrete.IIntegerTerm;
import org.deri.iris.api.terms.concrete.IIri;
import org.deri.iris.api.terms.concrete.ILanguage;
import org.deri.iris.api.terms.concrete.ILongTerm;
import org.deri.iris.api.terms.concrete.INCName;
import org.deri.iris.api.terms.concrete.INMTOKEN;
import org.deri.iris.api.terms.concrete.IName;
import org.deri.iris.api.terms.concrete.INegativeInteger;
import org.deri.iris.api.terms.concrete.INonNegativeInteger;
import org.deri.iris.api.terms.concrete.INonPositiveInteger;
import org.deri.iris.api.terms.concrete.INormalizedString;
import org.deri.iris.api.terms.concrete.IPlainLiteral;
import org.deri.iris.api.terms.concrete.IPositiveInteger;
import org.deri.iris.api.terms.concrete.IShortTerm;
import org.deri.iris.api.terms.concrete.ISqName;
import org.deri.iris.api.terms.concrete.ITime;
import org.deri.iris.api.terms.concrete.IToken;
import org.deri.iris.api.terms.concrete.IXMLLiteral;
import org.deri.iris.api.terms.concrete.IYearMonthDuration;

/**
 * <p>
 * An interface that can be used to create set of data types supported by this
 * engine.
 * </p>
 */
public interface IConcreteFactory {

	/**
	 * Create a Base64Binary term from a String representing a Base64Binary data
	 * type.
	 * 
	 * @param s The String representing a Base64Binary data type.
	 * @return The Base64Binary term.
	 */
	public IBase64Binary createBase64Binary(String s);

	/**
	 * Create a boolean term from a boolean value.
	 * 
	 * @param b The value of the term
	 * @return The boolean term.
	 */
	public IBooleanTerm createBoolean(boolean b);

	/**
	 * Create a boolean term with a string value.
	 * 
	 * @param value The string value, which must be either 'true' or '1' for
	 *            true, or 'false' or '0' for false.
	 * @return The boolean term.
	 */
	public IBooleanTerm createBoolean(String value);

	/**
	 * Creates a new date object. The timezone will be set to GMT.
	 * 
	 * @param year the year
	 * @param month the mont (1-12)
	 * @param day the day
	 */
	public IDateTerm createDate(int year, int month, int day);

	/**
	 * Creates a new date object within the given timezone.
	 * 
	 * @param year the year
	 * @param month the mont (1-12)
	 * @param day the day
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute wheren't
	 *             both positive, or negative
	 */
	public IDateTerm createDate(int year, int month, int day, int tzHour,
			int tzMinute);

	/**
	 * Creates a datetime object with a given timezone.
	 * 
	 * @param year the years
	 * @param month the months (1-12)
	 * @param day day of the month
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the decimal seconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute wheren't
	 *             both positive, or negative
	 */
	public IDateTime createDateTime(int year, int month, int day, int hour,
			int minute, double second, int tzHour, int tzMinute);

	/**
	 * Creates a datetime object with a given timezone.
	 * 
	 * @param year the years
	 * @param month the months (1-12)
	 * @param day day of the month
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 * @param millisecond the milliseconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute wheren't
	 *             both positive, or negative
	 */
	public IDateTime createDateTime(int year, int month, int day, int hour,
			int minute, int second, int millisecond, int tzHour, int tzMinute);

	/**
	 * Creates a new term representing a xs:dayTimeDuration.
	 * 
	 * @param positive True if this term represents a positive duration, false
	 *            otherwise.
	 * @param day The day.
	 * @param hour The hour.
	 * @param minute The minute.
	 * @param second The second.
	 * @return The new term representing a xs:dayTimeDuration.
	 */
	public IDayTimeDuration createDayTimeDuration(boolean positive, int day,
			int hour, int minute, double second);

	/**
	 * Creates a new term representing a xs:dayTimeDuration.
	 * 
	 * @param positive True if this term represents a positive duration, false
	 *            otherwise.
	 * @param day The day.
	 * @param hour The hour.
	 * @param minute The minute.
	 * @param second The second.
	 * @param millisecond The millisecond.
	 * @return The new term representing a xs:dayTimeDuration.
	 */
	public IDayTimeDuration createDayTimeDuration(boolean positive, int day,
			int hour, int minute, int second, int millisecond);

	/**
	 * Create a new decimal term.
	 * 
	 * @param d The decimal value
	 * @return The new decimal term
	 */
	public IDecimalTerm createDecimal(double d);

	/**
	 * Create a double term.
	 * 
	 * @param d The double values
	 * @return The new term
	 */
	public IDoubleTerm createDouble(double d);

	/**
	 * Create a new Duration term.
	 * 
	 * @param positive true is a positive duration
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public IDuration createDuration(boolean positive, int year, int month,
			int day, int hour, int minute, double second);

	/**
	 * Constructs a new duration.
	 * 
	 * @param positive <code>true</code>if the duration is positive, otherwise
	 *            <code>false</code>
	 * @param year the yearspan
	 * @param month the monthspa (1-12)
	 * @param day the dayspan
	 * @param hour the hourspan
	 * @param minute the minutespan
	 * @param second the secondspan
	 * @param millisecond the millisecondspan
	 */
	public IDuration createDuration(boolean positive, int year, int month,
			int day, int hour, int minute, int second, int millisecond);

	/**
	 * Constructs a new duration out of a given amount of milliseconds. The
	 * milliseconds will be round down to the next second.
	 * 
	 * @param millis the millisecond span
	 */
	public IDuration createDuration(long millis);

	/**
	 * Create a new float term
	 * 
	 * @param f The float value
	 * @return The new float term
	 */
	public IFloatTerm createFloat(float f);

	/**
	 * Create a new day term
	 * 
	 * @param day The day value
	 * @return The new term
	 */
	public IGDay createGDay(int day);

	/**
	 * Create a new month term
	 * 
	 * @param month The month value
	 * @return The new term
	 */
	public IGMonth createGMonth(int month);

	/**
	 * Create a new month/day term
	 * 
	 * @param month The month value
	 * @param day The day value
	 * @return The new term
	 */
	public IGMonthDay createGMonthDay(int month, int day);

	/**
	 * Create a new year term
	 * 
	 * @param year The year value
	 * @return The new term
	 */
	public IGYear createGYear(int year);

	/**
	 * Create a new year/month term
	 * 
	 * @param year The year value
	 * @param month The month value
	 * @return The new term
	 */
	public IGYearMonth createGYearMonth(int year, int month);

	/**
	 * Create a new HexBinary term
	 * 
	 * @param s The hex binary value
	 * @return The new term
	 */
	public IHexBinary createHexBinary(String s);

	/**
	 * Create a new integer term
	 * 
	 * @param i The integer value
	 * @return The new term
	 */
	public IIntegerTerm createInteger(int i);

	/**
	 * Create a new IRI term
	 * 
	 * @param s The IRI value
	 * @return The new term
	 */
	public IIri createIri(String s);

	/**
	 * Create a new SQName term
	 * 
	 * @param iri The IRI value
	 * @param s The SQName value
	 * @return The new term
	 */
	public ISqName createSqName(IIri iri, String name);

	/**
	 * Create a new SQName term
	 * 
	 * @param s The SQName value
	 * @return The new term
	 */
	public ISqName createSqName(String s);

	/**
	 * Creates a new PlainLiteral term. The string passed to this method is of
	 * the form "text@lang" and contains at least one "@".
	 * 
	 * @param string A string of the form "text@lang", where "lang" is the
	 *            language of the text. Must contain at least one "@" character.
	 * @return The PlainLiteral for the specified string.
	 */
	public IPlainLiteral createPlainLiteral(String string);

	/**
	 * Creates a new PlainLiteral term with the specified string and language
	 * tag.
	 * 
	 * @param string The string.
	 * @param language The language tag.
	 * @return The PlainLiteral term for the specified string and language tag.
	 */
	public IPlainLiteral createPlainLiteral(String string, String language);

	/**
	 * Creates a time object with a given timezone.
	 * 
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the decimal seconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute wheren't
	 *             both positive, or negative
	 */
	public ITime createTime(int hour, int minute, double second, int tzHour,
			int tzMinute);

	/**
	 * Creates a time object with a given timezone.
	 * 
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 * @param millisecond the milliseconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute wheren't
	 *             both positive, or negative
	 */
	public ITime createTime(int hour, int minute, int second, int millisecond,
			int tzHour, int tzMinute);

	/**
	 * Creates a new term representing a rdf:XMLLiteral.
	 * 
	 * @param string A string representing a XML element.
	 * @return The new term representing the rdf:XMLLiteral.
	 */
	public IXMLLiteral createXMLLiteral(String string);

	/**
	 * Creates a new term representing a rdf:XMLLiteral.
	 * 
	 * @param string A string representing a XML element.
	 * @param lang The language of the XML element.
	 * @return The new term representing the rdf:XMLLiteral.
	 */
	public IXMLLiteral createXMLLiteral(String string, String lang);

	/**
	 * Creates a new term representing a xs:yearMonthDuration.
	 * 
	 * @param positive True if this term represents a positive duration, false
	 *            otherwise.
	 * @param year The year.
	 * @param month The month.
	 * @return The new term representing a xs:yearMonthDuration.
	 */
	public IYearMonthDuration createYearMonthDuration(boolean positive,
			int year, int month);

	public IAnyURI createAnyURI(URI uri);

	public IByteTerm createByte(byte value);

	public IENTITY createEntity(String entity);

	public IID createID(String id);

	public IIDREF createIDREF(String idRef);

	public ILanguage createLanguage(String language);

	public ILongTerm createLong(int value);

	public IName createName(String name);

	public INCName createNCName(String name);

	public INegativeInteger createNegativeInteger(int value);

	public INMTOKEN createNMTOKEN(String token);

	public INonNegativeInteger createNonNegativeInteger(int value);

	public INonPositiveInteger createNonPositiveInteger(int value);

	public INormalizedString createNormalizedString(String string);

	public IPositiveInteger createPositiveInteger(int value);

	public IShortTerm createShort(short value);

	public IToken createToken(String token);
}
