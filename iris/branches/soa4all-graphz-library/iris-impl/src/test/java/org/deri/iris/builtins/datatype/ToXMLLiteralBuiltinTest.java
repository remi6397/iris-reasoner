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
package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;
import junit.framework.TestCase;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.ISqName;

/**
 * Test for ToXMLLiteralBuiltin.
 */
public class ToXMLLiteralBuiltinTest extends TestCase {

	private static final ITerm X = TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public ToXMLLiteralBuiltinTest(String name) {
		super(name);
	}

	public void testBase64() throws EvaluationException {
		fails(CONCRETE.createBase64Binary("c3VyZS4="));
	}

	public void testBoolean() throws EvaluationException {
		fails(CONCRETE.createBoolean(true));
	}

	public void testDate() throws EvaluationException {
		fails(CONCRETE.createDate(2009, 4, 28));
	}

	public void testDateTime() throws EvaluationException {
		fails(CONCRETE.createDateTime(2009, 04, 28, 8, 36, 25, -3, -33));
	}

	public void testDayTimeDuration() throws EvaluationException {
		fails(CONCRETE.createDayTimeDuration(false, 2, 2, 3, 0));
	}

	public void testDecimal() throws EvaluationException {
		fails(CONCRETE.createDecimal(1.337));
	}

	public void testDouble() throws EvaluationException {
		fails(CONCRETE.createDouble(1.3));
	}

	public void testDuration() throws EvaluationException {
		fails(CONCRETE.createDuration(true, 2, 1, 0, 5, 4, 2.3));
	}

	public void testFloat() throws EvaluationException {
		fails(CONCRETE.createFloat(1.3f));
	}

	public void testGDay() throws EvaluationException {
		fails(CONCRETE.createGDay(27));
	}

	public void testGMonth() throws EvaluationException {
		fails(CONCRETE.createGMonth(4));
	}

	public void testGMonthDay() throws EvaluationException {
		fails(CONCRETE.createGMonthDay(4, 27));
	}

	public void testGYear() throws EvaluationException {
		fails(CONCRETE.createGYear(2009));
	}

	public void testGYearMonth() throws EvaluationException {
		fails(CONCRETE.createGYearMonth(2009, 4));
	}

	public void testHexBinary() throws EvaluationException {
		fails(CONCRETE.createHexBinary("0FB7"));
	}

	public void testInteger() throws EvaluationException {
		fails(CONCRETE.createInteger(1337));
	}

	public void testIri() throws EvaluationException {
		fails(CONCRETE.createIri("http://www.w3.org/2007/rif#iri"));
	}

	public void testSqName() throws EvaluationException {
		ISqName name = CONCRETE.createSqName(CONCRETE
				.createIri("http://www.w3.org/2002/07/owl#"), "owl");
		fails(name);
	}

	public void testString() throws EvaluationException {
		equals(CONCRETE.createXMLLiteral("<tag>Text</tag>"), TERM
				.createString("<tag>Text</tag>"));
	}

	public void testText() throws EvaluationException {
		fails(CONCRETE.createPlainLiteral("Ein Text@de"));
	}

	public void testTime() throws EvaluationException {
		fails(CONCRETE.createTime(12, 45, 0, 0, 0));
	}

	public void testXMLLiteral() throws EvaluationException {
		equals(CONCRETE.createXMLLiteral("<quote>Bam!</quote>", "de"), CONCRETE
				.createXMLLiteral("<quote>Bam!</quote>", "de"));
	}

	public void testYearMonthDuration() throws EvaluationException {
		fails(CONCRETE.createYearMonthDuration(true, 2009, 4));
	}

	private void equals(ITerm expected, ITerm term) throws EvaluationException {
		ITuple expectedTuple = BASIC.createTuple(expected);
		ITuple actualTuple = compute(term);

		assertEquals(expectedTuple, actualTuple);
	}

	private void fails(ITerm term) throws EvaluationException {
		assertNull(compute(term));
	}

	private ITuple compute(ITerm term) throws EvaluationException {
		ToXMLLiteralBuiltin builtin = new ToXMLLiteralBuiltin(term, Y);

		ITuple arguments = BASIC.createTuple(X, Y);
		ITuple actualTuple = builtin.evaluate(arguments);

		return actualTuple;
	}

}
