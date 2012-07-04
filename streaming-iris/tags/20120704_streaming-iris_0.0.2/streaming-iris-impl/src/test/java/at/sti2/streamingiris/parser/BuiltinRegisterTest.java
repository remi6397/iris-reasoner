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
package at.sti2.streamingiris.parser;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


import at.sti2.streamingiris.api.basics.ILiteral;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.compiler.BuiltinRegister;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.compiler.ParserException;

/**
 * <p>
 * Tests for the <code>BuiltinRegister</code>.
 * </p>
 * <p>
 * $Id: BuiltinRegisterTest.java,v 1.3 2007-10-12 14:34:56 bazbishop237 Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.3 $
 */
public class BuiltinRegisterTest extends TestCase {

	/** The register on which to operate. */
	private BuiltinRegister reg;
	
	/** The parser. */
	private Parser parser;

	public BuiltinRegisterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(BuiltinRegisterTest.class, BuiltinRegisterTest.class.getSimpleName());
	}

	public void setUp() {
		reg = new BuiltinRegister();
		parser = new Parser();
	}

	/**
	 * Checks whether the core builtins gets registered correctly.
	 * @throws ParserException 
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1784947&group_id=167309&atid=985821">bug #1784947: simple possibility to make new core builtins parseable</a>
	 */
	public void testRegisterCoreBuiltins() throws ParserException {
		checkRegisteredBuiltin("ADD", at.sti2.streamingiris.builtins.AddBuiltin.class, 3);
		checkRegisteredBuiltin("SUBTRACT", at.sti2.streamingiris.builtins.SubtractBuiltin.class, 3);
		checkRegisteredBuiltin("MULTIPLY", at.sti2.streamingiris.builtins.MultiplyBuiltin.class, 3);
		checkRegisteredBuiltin("DIVIDE", at.sti2.streamingiris.builtins.DivideBuiltin.class, 3);
		checkRegisteredBuiltin("MODULUS", at.sti2.streamingiris.builtins.ModulusBuiltin.class, 3);

		checkRegisteredBuiltin("EQUAL", at.sti2.streamingiris.builtins.EqualBuiltin.class, 2);
		checkRegisteredBuiltin("NOT_EQUAL", at.sti2.streamingiris.builtins.NotEqualBuiltin.class, 2);

		checkRegisteredBuiltin("LESS", at.sti2.streamingiris.builtins.LessBuiltin.class, 2);
		checkRegisteredBuiltin("LESS_EQUAL", at.sti2.streamingiris.builtins.LessEqualBuiltin.class, 2);
		checkRegisteredBuiltin("GREATER", at.sti2.streamingiris.builtins.GreaterBuiltin.class, 2);
		checkRegisteredBuiltin("GREATER_EQUAL", at.sti2.streamingiris.builtins.GreaterEqualBuiltin.class, 2);

		checkRegisteredBuiltin("IS_NUMERIC", at.sti2.streamingiris.builtins.datatype.IsNumericBuiltin.class, 1);
		checkRegisteredBuiltin("IS_BASE64BINARY", at.sti2.streamingiris.builtins.datatype.IsBase64BinaryBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_BOOLEAN", at.sti2.streamingiris.builtins.datatype.IsBooleanBuiltin.class, 1);
		checkRegisteredBuiltin("IS_DATE", at.sti2.streamingiris.builtins.datatype.IsDateBuiltin.class, 1);
		checkRegisteredBuiltin("IS_DATETIME", at.sti2.streamingiris.builtins.datatype.IsDateTimeBuiltin.class, 1);
		checkRegisteredBuiltin("IS_DECIMAL", at.sti2.streamingiris.builtins.datatype.IsDecimalBuiltin.class, 1);
		checkRegisteredBuiltin("IS_DOUBLE", at.sti2.streamingiris.builtins.datatype.IsDoubleBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_DURATION", at.sti2.streamingiris.builtins.datatype.IsDurationBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_FLOAT", at.sti2.streamingiris.builtins.datatype.IsFloatBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_GDAY", at.sti2.streamingiris.builtins.datatype.IsGDayBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_GMONTH", at.sti2.streamingiris.builtins.datatype.IsGMonthBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_GMONTHDAY", at.sti2.streamingiris.builtins.datatype.IsGMonthDayBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_GYEAR", at.sti2.streamingiris.builtins.datatype.IsGYearBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_GYEARMONTH", at.sti2.streamingiris.builtins.datatype.IsGYearMonthBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_HEXBINARY", at.sti2.streamingiris.builtins.datatype.IsHexBinaryBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_INTEGER", at.sti2.streamingiris.builtins.datatype.IsIntegerBuiltin.class, 1);
		checkRegisteredBuiltin("IS_IRI", at.sti2.streamingiris.builtins.datatype.IsIriBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_SQNAME", at.sti2.streamingiris.builtins.datatype.IsSqNameBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_STRING", at.sti2.streamingiris.builtins.datatype.IsStringBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_TIME", at.sti2.streamingiris.builtins.datatype.IsTimeBuiltin.class, 1 );
		
		// Datatype check builtins for new datatypes DayTimeDuration, YearMonthDuration, Text and XMLLiteral.
		checkRegisteredBuiltin("IS_DAYTIMEDURATION", at.sti2.streamingiris.builtins.datatype.IsDayTimeDurationBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_YEARMONTHDURATION", at.sti2.streamingiris.builtins.datatype.IsYearMonthDurationBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_PLAINLITERAL", at.sti2.streamingiris.builtins.datatype.IsPlainLiteralBuiltin.class, 1 );
		checkRegisteredBuiltin("IS_XMLLITERAL", at.sti2.streamingiris.builtins.datatype.IsXMLLiteralBuiltin.class, 1 );		
		
		checkRegisteredBuiltin("IS_DATATYPE", at.sti2.streamingiris.builtins.datatype.IsDatatypeBuiltin.class, 2 );
		checkRegisteredBuiltin("IS_NOT_DATATYPE", at.sti2.streamingiris.builtins.datatype.IsNotDatatypeBuiltin.class, 2 );
		checkRegisteredBuiltin("SAME_TYPE", at.sti2.streamingiris.builtins.datatype.SameTypeBuiltin.class, 2);
		
		// Datatype conversion builtins.
		checkRegisteredBuiltin("TO_BASE64", at.sti2.streamingiris.builtins.datatype.ToBase64Builtin.class, 2 );
		checkRegisteredBuiltin("TO_BOOLEAN", at.sti2.streamingiris.builtins.datatype.ToBooleanBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_DATE", at.sti2.streamingiris.builtins.datatype.ToDateBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_DATETIME", at.sti2.streamingiris.builtins.datatype.ToDateTimeBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_DAYTIMEDURATION", at.sti2.streamingiris.builtins.datatype.ToDayTimeDurationBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_DECIMAL", at.sti2.streamingiris.builtins.datatype.ToDecimalBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_DOUBLE", at.sti2.streamingiris.builtins.datatype.ToDoubleBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_DURATION", at.sti2.streamingiris.builtins.datatype.ToDurationBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_FLOAT", at.sti2.streamingiris.builtins.datatype.ToFloatBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_GDAY", at.sti2.streamingiris.builtins.datatype.ToGDayBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_GMONTH", at.sti2.streamingiris.builtins.datatype.ToGMonthBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_GMONTHDAY", at.sti2.streamingiris.builtins.datatype.ToGMonthDayBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_GYEAR", at.sti2.streamingiris.builtins.datatype.ToGYearBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_GYEARMONTH", at.sti2.streamingiris.builtins.datatype.ToGYearMonthBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_HEXBINARY", at.sti2.streamingiris.builtins.datatype.ToHexBinaryBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_INTEGER", at.sti2.streamingiris.builtins.datatype.ToIntegerBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_IRI", at.sti2.streamingiris.builtins.datatype.ToIriBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_STRING", at.sti2.streamingiris.builtins.datatype.ToStringBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_TEXT", at.sti2.streamingiris.builtins.datatype.ToPlainLiteralBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_TIME", at.sti2.streamingiris.builtins.datatype.ToTimeBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_XMLLITERAL", at.sti2.streamingiris.builtins.datatype.ToXMLLiteralBuiltin.class, 2 );
		checkRegisteredBuiltin("TO_YEARMONTHDURATION", at.sti2.streamingiris.builtins.datatype.ToYearMonthDurationBuiltin.class, 2 );
		
		// Date builtins.
		checkRegisteredBuiltin("DAY_PART", at.sti2.streamingiris.builtins.date.DayPartBuiltin.class, 2 );
		checkRegisteredBuiltin("HOUR_PART", at.sti2.streamingiris.builtins.date.HourPartBuiltin.class, 2 );
		checkRegisteredBuiltin("MINUTE_PART", at.sti2.streamingiris.builtins.date.MinutePartBuiltin.class, 2 );
		checkRegisteredBuiltin("MONTH_PART", at.sti2.streamingiris.builtins.date.MonthPartBuiltin.class, 2 );
		checkRegisteredBuiltin("SECOND_PART", at.sti2.streamingiris.builtins.date.SecondPartBuiltin.class, 2 );
		checkRegisteredBuiltin("TIMEZONE_PART", at.sti2.streamingiris.builtins.date.TimezonePartBuiltin.class, 2 );
		checkRegisteredBuiltin("YEAR_PART", at.sti2.streamingiris.builtins.date.YearPartBuiltin.class, 2 );
		
		// String builtins.
		checkRegisteredBuiltin("LANG_FROM_TEXT", at.sti2.streamingiris.builtins.string.LangFromPlainLiteralBuiltin.class, 2 );
		checkRegisteredBuiltin("STRING_COMPARE", at.sti2.streamingiris.builtins.string.StringCompareBuiltin.class, 3 );
		checkRegisteredBuiltin("STRING_CONCAT", at.sti2.streamingiris.builtins.string.StringConcatBuiltin.class, 3 );
		checkRegisteredBuiltin("STRING_CONTAINS3", at.sti2.streamingiris.builtins.string.StringContainsBuiltin.class, 3 );
		checkRegisteredBuiltin("STRING_CONTAINS2", at.sti2.streamingiris.builtins.string.StringContainsWithoutCollationBuiltin.class, 2 );
		checkRegisteredBuiltin("STRING_ENDS_WITH3", at.sti2.streamingiris.builtins.string.StringEndsWithBuiltin.class, 3 );
		checkRegisteredBuiltin("STRING_ENDS_WITH2", at.sti2.streamingiris.builtins.string.StringEndsWithWithoutCollationBuiltin.class, 2 );
		checkRegisteredBuiltin("STRING_ESCAPE_HTML_URI", at.sti2.streamingiris.builtins.string.StringEscapeHtmlUriBuiltin.class, 2 );
		checkRegisteredBuiltin("STRING_FROM_TEXT", at.sti2.streamingiris.builtins.string.StringFromPlainLiteralBuiltin.class, 2 );
		checkRegisteredBuiltin("STRING_IRI_TO_URI", at.sti2.streamingiris.builtins.string.StringIriToUriBuiltin.class, 2 );
		checkRegisteredBuiltin("STRING_JOIN", at.sti2.streamingiris.builtins.string.StringJoinBuiltin.class, 4 );
		checkRegisteredBuiltin("STRING_LENGTH", at.sti2.streamingiris.builtins.string.StringLengthBuiltin.class, 2 );
		checkRegisteredBuiltin("STRING_MATCHES2", at.sti2.streamingiris.builtins.string.StringMatchesWithoutFlagsBuiltin.class, 2 );
		checkRegisteredBuiltin("STRING_MATCHES3", at.sti2.streamingiris.builtins.string.StringMatchesBuiltin.class, 3 );
		checkRegisteredBuiltin("STRING_REPLACE3", at.sti2.streamingiris.builtins.string.StringReplaceWithoutFlagsBuiltin.class, 4 );
		checkRegisteredBuiltin("STRING_REPLACE4", at.sti2.streamingiris.builtins.string.StringReplaceBuiltin.class, 5 );
		checkRegisteredBuiltin("STRING_STARTS_WITH3", at.sti2.streamingiris.builtins.string.StringStartsWithBuiltin.class, 3 );
		checkRegisteredBuiltin("STRING_STARTS_WITH2", at.sti2.streamingiris.builtins.string.StringStartsWithWithoutCollationBuiltin.class, 2 );
		checkRegisteredBuiltin("STRING_SUBSTRING_AFTER3", at.sti2.streamingiris.builtins.string.StringSubstringAfterBuiltin.class, 4 );
		checkRegisteredBuiltin("STRING_SUBSTRING_AFTER2", at.sti2.streamingiris.builtins.string.StringSubstringAfterWithoutCollationBuiltin.class, 3 );
		checkRegisteredBuiltin("STRING_SUBSTRING_BEFORE3", at.sti2.streamingiris.builtins.string.StringSubstringBeforeBuiltin.class, 4 );
		checkRegisteredBuiltin("STRING_SUBSTRING_BEFORE2", at.sti2.streamingiris.builtins.string.StringSubstringBeforeWithoutCollationBuiltin.class, 3 );
		checkRegisteredBuiltin("STRING_SUBSTRING2", at.sti2.streamingiris.builtins.string.StringSubstringUntilEndBuiltin.class, 3 );
		checkRegisteredBuiltin("STRING_SUBSTRING3", at.sti2.streamingiris.builtins.string.StringSubstringBuiltin.class, 4 );
		checkRegisteredBuiltin("STRING_TO_LOWER", at.sti2.streamingiris.builtins.string.StringToLowerBuiltin.class, 2 );
		checkRegisteredBuiltin("STRING_TO_UPPER", at.sti2.streamingiris.builtins.string.StringToUpperBuiltin.class, 2 );
		checkRegisteredBuiltin("STRING_URI_ENCODE", at.sti2.streamingiris.builtins.string.StringUriEncodeBuiltin.class, 2 );
		checkRegisteredBuiltin("TEXT_COMPARE", at.sti2.streamingiris.builtins.string.PlainLiteralCompareBuiltin.class, 3 );
		checkRegisteredBuiltin("TEXT_FROM_STRING", at.sti2.streamingiris.builtins.string.PlainLiteralFromStringBuiltin.class, 2 );
		checkRegisteredBuiltin("TEXT_FROM_STRING_LANG", at.sti2.streamingiris.builtins.string.PlainLiteralFromStringLangBuiltin.class, 3 );
		checkRegisteredBuiltin("TEXT_LENGTH", at.sti2.streamingiris.builtins.string.PlainLiteralLengthBuiltin.class, 2 );
	}

	/**
	 * Asserts whether a builtin was registered with the correct name, class
	 * and arity.
	 * @param name the name of the builtin to check (predicate symbol)
	 * @param clazz the class of the builtin to check
	 * @param arity the arity of the builtin to check
	 */
	private void checkRegisteredBuiltin(final String name, final Class<?> clazz, final int arity) throws ParserException {
		assert name != null: "The name must not be null";
		assert clazz != null: "The class must not be null";
		assert arity > 0: "The arity must be greater than 0";

		assertEquals("Could not find the class of " + name, clazz, reg.getBuiltinClass(name));
		assertEquals("Could not find the arity for " + name, arity, reg.getBuiltinArity(name));
		
		// Check if the parser correctly instantiates the builtin. 
		// For this, create a simple formula containing the builtin.
		String program = createFormula(name, arity);
		parser.parse(program);
		
		List<IRule> rules = parser.getRules();
		IRule rule = rules.get(0);
		
		List<ILiteral> body = rule.getBody();
		Class<?> literalClass = body.get(0).getAtom().getClass();
		
		assertEquals("Parser did not correctly instantiate builtin.", clazz, literalClass);
	}

	private String createFormula(String name, int arity) {
		StringBuffer predicate = new StringBuffer();
		
		// Add predicate symbol and opening bracket.
		predicate.append(name + "(");

		// Add n variables, where n = arity.
		for (int i = 0; i < arity; i++) {
			if (i > 0) {
				predicate.append(",");
			}

			predicate.append("?X");
		}

		// Add closing bracket.
		predicate.append(")");

		String formula = "foo(?X) :- " + predicate.toString() + ".";
		
		return formula;
	}

}
