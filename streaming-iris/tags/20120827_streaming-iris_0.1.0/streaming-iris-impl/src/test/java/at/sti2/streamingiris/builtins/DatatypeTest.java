/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2009 Semantic Technology Institute (STI) Innsbruck, 
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
package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import static at.sti2.streamingiris.factory.Factory.TERM;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;


import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.concrete.IBase64Binary;
import at.sti2.streamingiris.api.terms.concrete.IBooleanTerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTime;
import at.sti2.streamingiris.api.terms.concrete.IDayTimeDuration;
import at.sti2.streamingiris.api.terms.concrete.IDecimalTerm;
import at.sti2.streamingiris.api.terms.concrete.IDoubleTerm;
import at.sti2.streamingiris.api.terms.concrete.IDuration;
import at.sti2.streamingiris.api.terms.concrete.IFloatTerm;
import at.sti2.streamingiris.api.terms.concrete.IGDay;
import at.sti2.streamingiris.api.terms.concrete.IGMonth;
import at.sti2.streamingiris.api.terms.concrete.IGMonthDay;
import at.sti2.streamingiris.api.terms.concrete.IGYear;
import at.sti2.streamingiris.api.terms.concrete.IGYearMonth;
import at.sti2.streamingiris.api.terms.concrete.IHexBinary;
import at.sti2.streamingiris.api.terms.concrete.IIntegerTerm;
import at.sti2.streamingiris.api.terms.concrete.IIri;
import at.sti2.streamingiris.api.terms.concrete.IPlainLiteral;
import at.sti2.streamingiris.api.terms.concrete.ISqName;
import at.sti2.streamingiris.api.terms.concrete.ITime;
import at.sti2.streamingiris.api.terms.concrete.IXMLLiteral;
import at.sti2.streamingiris.api.terms.concrete.IYearMonthDuration;

/**
 * A helper class for test cases which use various data values. Subclasses of
 * this class do not have to explicitly instantiate such data values.
 * 
 * @author Adrian Marte
 */
public abstract class DatatypeTest extends TestCase {

	public DatatypeTest(String name) {
		super(name);
	}

	protected IBase64Binary base64BinaryTerm = CONCRETE
			.createBase64Binary("c3VyZS4=");

	protected IDateTerm dateTerm = CONCRETE.createDate(2009, 04, 27);

	protected IBooleanTerm booleanTerm = CONCRETE.createBoolean(true);

	protected IDateTime dateTimeTerm = CONCRETE.createDateTime(2009, 04, 27,
			10, 10, 0, -5, 0);

	protected IDayTimeDuration dayTimeDurationTerm = CONCRETE
			.createDayTimeDuration(false, 2, 2, 3, 0);

	protected IDecimalTerm decimalTerm = CONCRETE
			.createDecimal(new BigDecimal(
					"123423429340239420342341029412412410294812094124.234209435692034092394020934209"));

	protected IDoubleTerm doubleTerm = CONCRETE.createDouble(1.337);

	protected IDuration durationTerm = CONCRETE.createDuration(true, 2, 1, 0,
			5, 4, 2.3);

	protected IFloatTerm floatTerm = CONCRETE.createFloat(1.337f);

	protected IGDay gDayTerm = CONCRETE.createGDay(27);

	protected IGMonth gMonthTerm = CONCRETE.createGMonth(4);

	protected IGMonthDay gMonthDayTerm = CONCRETE.createGMonthDay(4, 27);

	protected IGYear gYearTerm = CONCRETE.createGYear(2009);

	protected IGYearMonth gYearMonthTerm = CONCRETE.createGYearMonth(2009, 4);

	protected IHexBinary hexBinaryTerm = CONCRETE.createHexBinary("0FB7");

	protected IIntegerTerm integerTerm = CONCRETE.createInteger(new BigInteger(
			"123423429340239420342341029412412410294812094124"));

	protected IIri iriTerm = CONCRETE
			.createIri("http://www.w3.org/2007/rif#iri");

	protected ISqName sqNameTerm = CONCRETE.createSqName(CONCRETE
			.createIri("http://www.w3.org/2002/07/owl#"), "owl");

	protected IStringTerm stringTerm = TERM.createString("abcd");

	protected IPlainLiteral plainLiteralTerm = CONCRETE
			.createPlainLiteral("Ein Text@de");

	protected ITime timeTerm = CONCRETE.createTime(12, 45, 0, 0, 0);

	protected IXMLLiteral xmlLiteralTerm = CONCRETE.createXMLLiteral(
			"<quote>Bam!</quote>", "de");

	protected IYearMonthDuration yearMonthDurationTerm = CONCRETE
			.createYearMonthDuration(true, 2009, 4);

}
