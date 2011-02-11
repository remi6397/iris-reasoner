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
package org.deri.iris.builtins;

import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;

import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.concrete.IBase64Binary;
import org.deri.iris.api.terms.concrete.IBooleanTerm;
import org.deri.iris.api.terms.concrete.IDateTerm;
import org.deri.iris.api.terms.concrete.IDateTime;
import org.deri.iris.api.terms.concrete.IDayTimeDuration;
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
import org.deri.iris.api.terms.concrete.IPlainLiteral;
import org.deri.iris.api.terms.concrete.ISqName;
import org.deri.iris.api.terms.concrete.ITime;
import org.deri.iris.api.terms.concrete.IXMLLiteral;
import org.deri.iris.api.terms.concrete.IYearMonthDuration;

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
