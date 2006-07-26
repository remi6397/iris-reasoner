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

import org.deri.iris.api.factory.IConcreteFactory;
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

/**
 * @author richi
 * 
 */
public class ConcreteFactory implements IConcreteFactory {

	private static final IConcreteFactory FACTORY = new ConcreteFactory();

	private ConcreteFactory() {
		// this is a singelton
	}

	public static IConcreteFactory getInstance() {
		return FACTORY;
	}

	public IBase64Binary createBase64Binary(final String s) {
		return new Base64Binary(s);
	}

	public IBooleanTerm createBoolean(final boolean b) {
		return new BooleanTerm(b);
	}

	public IDateTerm createDate(final int year, final int month,
			final int day) {
		return new DateTerm(year, month, day);
	}

	public IDateTime createDateTime(final int year, final int month,
			final int day, final int hour, final int minute, final int second,
			final int tzHour, final int tzMinute) {
		return new DateTime(year, month, day, hour, minute, second, tzHour,
				tzMinute);
	}

	public IDateTime createDateTime(final int year, final int month,
			final int day, final int hour, final int minute, final int second) {
		return new DateTime(year, month, day, hour, minute, second);
	}

	public IDecimalTerm createDecimal(final double d) {
		return new DecimalTerm(d);
	}

	public IDoubleTerm createDouble(final double d) {
		return new DoubleTerm(d);
	}

	public IDuration createDuration(final int year, final int month,
			final int day, final int hour, final int minute, final int second) {
		return new Duration(year, month, day, hour, minute, second);
	}

	public IFloatTerm createFloat(final float f) {
		return new FloatTerm(f);
	}

	public IGDay createGDay(final int day) {
		return new GDay(day);
	}

	public IGMonthDay createGMonthDay(final int month, final int day) {
		return new GMonthDay(month, day);
	}

	public IGMonth createGMonth(final int month) {
		return new GMonth(month);
	}

	public IGYearMonth createGYearMonth(final int year, final int month) {
		return new GYearMonth(year, month);
	}

	public IGYear createGYear(final int year) {
		return new GYear(year);
	}

	public IHexBinary createHexBinary(final String s) {
		return new HexBinary(s);
	}

	public IIntegerTerm createInteger(final int i) {
		return new IntegerTerm(i);
	}

	public IIri createIri(final String s) {
		return new Iri(s);
	}

	public ISqName createSqName(final String s) {
		return new SqName(s);
	}

	public ISqName createSqName(final IIri iri, final String name) {
		return new SqName(iri, name);
	}
}
