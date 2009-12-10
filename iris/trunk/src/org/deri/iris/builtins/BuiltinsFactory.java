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
package org.deri.iris.builtins;

import org.deri.iris.api.builtins.IBuiltinAtom;
import org.deri.iris.api.factory.IBuiltinsFactory;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.datatype.*;
import org.deri.iris.builtins.date.DayPartBuiltin;
import org.deri.iris.builtins.date.HourPartBuiltin;
import org.deri.iris.builtins.date.MinutePartBuiltin;
import org.deri.iris.builtins.date.MonthPartBuiltin;
import org.deri.iris.builtins.date.SecondPartBuiltin;
import org.deri.iris.builtins.date.TimezonePartBuiltin;
import org.deri.iris.builtins.date.YearPartBuiltin;
import org.deri.iris.builtins.string.LangFromPlainLiteralBuiltin;
import org.deri.iris.builtins.string.PlainLiteralCompareBuiltin;
import org.deri.iris.builtins.string.PlainLiteralFromStringBuiltin;
import org.deri.iris.builtins.string.PlainLiteralFromStringLangBuiltin;
import org.deri.iris.builtins.string.PlainLiteralLengthBuiltin;
import org.deri.iris.builtins.string.StringCompareBuiltin;
import org.deri.iris.builtins.string.StringConcatBuiltin;
import org.deri.iris.builtins.string.StringContainsBuiltin;
import org.deri.iris.builtins.string.StringContainsWithoutCollationBuiltin;
import org.deri.iris.builtins.string.StringEndsWithBuiltin;
import org.deri.iris.builtins.string.StringEndsWithWithoutCollationBuiltin;
import org.deri.iris.builtins.string.StringEscapeHtmlUriBuiltin;
import org.deri.iris.builtins.string.StringFromPlainLiteralBuiltin;
import org.deri.iris.builtins.string.StringIriToUriBuiltin;
import org.deri.iris.builtins.string.StringJoinBuiltin;
import org.deri.iris.builtins.string.StringLengthBuiltin;
import org.deri.iris.builtins.string.StringMatchesBuiltin;
import org.deri.iris.builtins.string.StringMatchesWithoutFlagsBuiltin;
import org.deri.iris.builtins.string.StringReplaceBuiltin;
import org.deri.iris.builtins.string.StringReplaceWithoutFlagsBuiltin;
import org.deri.iris.builtins.string.StringStartsWithBuiltin;
import org.deri.iris.builtins.string.StringStartsWithWithoutCollationBuiltin;
import org.deri.iris.builtins.string.StringSubstringAfterBuiltin;
import org.deri.iris.builtins.string.StringSubstringAfterWithoutCollationBuiltin;
import org.deri.iris.builtins.string.StringSubstringBeforeBuiltin;
import org.deri.iris.builtins.string.StringSubstringBeforeWithoutCollationBuiltin;
import org.deri.iris.builtins.string.StringSubstringBuiltin;
import org.deri.iris.builtins.string.StringSubstringUntilEndBuiltin;
import org.deri.iris.builtins.string.StringToLowerBuiltin;
import org.deri.iris.builtins.string.StringToUpperBuiltin;
import org.deri.iris.builtins.string.StringUriEncodeBuiltin;

/**
 * <p>
 * Factory to create all sorts of builtins.
 * </p>
 * <p>
 * $Id: BuiltinsFactory.java,v 1.4 2007-10-12 12:40:58 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.4 $
 */
public class BuiltinsFactory implements IBuiltinsFactory {

	private static IBuiltinsFactory INSTANCE = new BuiltinsFactory();

	private BuiltinsFactory() {
		// this is a singelton
	}

	/**
	 * Returns the singelton instance of this factory.
	 * 
	 * @return a instane of this factory
	 */
	public static IBuiltinsFactory getInstance() {
		return INSTANCE;
	}

	public IBuiltinAtom createAddBuiltin(ITerm t0, ITerm t1, ITerm t2) {
		return new AddBuiltin(t0, t1, t2);
	}

	public IBuiltinAtom createSubtractBuiltin(ITerm t0, ITerm t1, ITerm t2) {
		return new SubtractBuiltin(t0, t1, t2);
	}

	public IBuiltinAtom createMultiplyBuiltin(ITerm t0, ITerm t1, ITerm t2) {
		return new MultiplyBuiltin(t0, t1, t2);
	}

	public IBuiltinAtom createDivideBuiltin(ITerm t0, ITerm t1, ITerm t2) {
		return new DivideBuiltin(t0, t1, t2);
	}

	public IBuiltinAtom createModulusBuiltin(ITerm t0, ITerm t1, ITerm t2) {
		return new ModulusBuiltin(t0, t1, t2);
	}

	public IBuiltinAtom createEqual(ITerm t0, ITerm t1) {
		return new EqualBuiltin(t0, t1);
	}

	public IBuiltinAtom createGreater(ITerm t0, ITerm t1) {
		return new GreaterBuiltin(t0, t1);
	}

	public IBuiltinAtom createGreaterEqual(ITerm t0, ITerm t1) {
		return new GreaterEqualBuiltin(t0, t1);
	}

	public IBuiltinAtom createLess(ITerm t0, ITerm t1) {
		return new LessBuiltin(t0, t1);
	}

	public IBuiltinAtom createLessEqual(ITerm t0, ITerm t1) {
		return new LessEqualBuiltin(t0, t1);
	}

	public IBuiltinAtom createUnequal(ITerm t0, ITerm t1) {
		return new NotEqualBuiltin(t0, t1);
	}

	public IBuiltinAtom createExactEqual(ITerm t0, ITerm t1) {
		return new ExactEqualBuiltin(t0, t1);
	}

	public IBuiltinAtom createNotExactEqual(ITerm t0, ITerm t1) {
		return new NotExactEqualBuiltin(t0, t1);
	}

	// check D3.1.4 Defining the features of the WSML-Rule v2.0 language

	public IBuiltinAtom createNumericModulus(ITerm... terms) {
		return new ModulusBuiltin(terms);
	}

	public IBuiltinAtom createStringCompare(ITerm... terms) {
		return new StringCompareBuiltin(terms);
	}

	public IBuiltinAtom createStringConcat(ITerm... terms) {
		return new StringConcatBuiltin(terms);
	}

	public IBuiltinAtom createStringJoin(ITerm... terms) {
		return new StringJoinBuiltin(terms);
	}

	public IBuiltinAtom createStringSubstring(ITerm... terms) {
		if (terms.length <= 3) {
			return new StringSubstringUntilEndBuiltin(terms);
		} else {
			return new StringSubstringBuiltin(terms);
		}
	}

	public IBuiltinAtom createStringLength(ITerm... terms) {
		return new StringLengthBuiltin(terms);
	}

	public IBuiltinAtom createStringToUpper(ITerm... terms) {
		return new StringToUpperBuiltin(terms);
	}

	public IBuiltinAtom createStringToLower(ITerm... terms) {
		return new StringToLowerBuiltin(terms);
	}

	public IBuiltinAtom createStringUriEncode(ITerm... terms) {
		return new StringUriEncodeBuiltin(terms);
	}

	public IBuiltinAtom createStringIriToUri(ITerm... terms) {
		return new StringIriToUriBuiltin(terms);
	}

	public IBuiltinAtom createStringEscapeHtmlUri(ITerm... terms) {
		return new StringEscapeHtmlUriBuiltin(terms);
	}

	public IBuiltinAtom createStringSubstringBefore(ITerm... terms) {
		if (terms.length <= 3) {
			return new StringSubstringBeforeWithoutCollationBuiltin(terms);
		} else {
			return new StringSubstringBeforeBuiltin(terms);
		}
	}

	public IBuiltinAtom createStringSubstringAfter(ITerm... terms) {
		if (terms.length <= 3) {
			return new StringSubstringAfterWithoutCollationBuiltin(terms);
		} else {
			return new StringSubstringAfterBuiltin(terms);
		}
	}

	public IBuiltinAtom createStringReplace(ITerm... terms) {
		if (terms.length <= 4) {
			return new StringReplaceWithoutFlagsBuiltin(terms);
		} else {
			return new StringReplaceBuiltin(terms);
		}
	}

	public IBuiltinAtom createStringContains(ITerm... terms) {
		if (terms.length <= 2) {
			return new StringContainsWithoutCollationBuiltin(terms);
		} else {
			return new StringContainsBuiltin(terms);
		}
	}

	public IBuiltinAtom createStringStartsWith(ITerm... terms) {
		if (terms.length <= 2) {
			return new StringStartsWithWithoutCollationBuiltin(terms);
		} else {
			return new StringStartsWithBuiltin(terms);
		}
	}

	public IBuiltinAtom createStringEndsWith(ITerm... terms) {
		if (terms.length <= 2) {
			return new StringEndsWithWithoutCollationBuiltin(terms);
		} else {
			return new StringEndsWithBuiltin(terms);
		}
	}

	public IBuiltinAtom createStringMatches(ITerm... terms) {
		if (terms.length <= 2) {
			return new StringMatchesWithoutFlagsBuiltin(terms);
		} else {
			return new StringMatchesBuiltin(terms);
		}
	}

	public IBuiltinAtom createYearPart(ITerm... terms) {
		return new YearPartBuiltin(terms);
	}

	public IBuiltinAtom createMonthPart(ITerm... terms) {
		return new MonthPartBuiltin(terms);
	}

	public IBuiltinAtom createDayPart(ITerm... terms) {
		return new DayPartBuiltin(terms);
	}

	public IBuiltinAtom createHourPart(ITerm... terms) {
		return new HourPartBuiltin(terms);
	}

	public IBuiltinAtom createMinutePart(ITerm... terms) {
		return new MinutePartBuiltin(terms);
	}

	public IBuiltinAtom createSecondPart(ITerm... terms) {
		return new SecondPartBuiltin(terms);
	}

	public IBuiltinAtom createTimezonePart(ITerm... terms) {
		return new TimezonePartBuiltin(terms);
	}

	public IBuiltinAtom createTextFromStringLang(ITerm... terms) {
		return new PlainLiteralFromStringLangBuiltin(terms);
	}

	public IBuiltinAtom createStringFromText(ITerm... terms) {
		return new StringFromPlainLiteralBuiltin(terms);
	}

	public IBuiltinAtom createLangFromText(ITerm... terms) {
		return new LangFromPlainLiteralBuiltin(terms);
	}

	public IBuiltinAtom createTextFromString(ITerm... terms) {
		return new PlainLiteralFromStringBuiltin(terms);
	}

	public IBuiltinAtom createTextCompare(ITerm... terms) {
		return new PlainLiteralCompareBuiltin(terms);
	}

	public IBuiltinAtom createTextLength(ITerm... terms) {
		return new PlainLiteralLengthBuiltin(terms);
	}

	public IBuiltinAtom createFalse() {
		return new FalseBuiltin(new ITerm[0]);
	}

	public IBuiltinAtom createTrue() {
		return new TrueBuiltin(new ITerm[0]);
	}

	public IBuiltinAtom createToBase64Binary(ITerm... terms) {
		return new ToBase64Builtin(terms);
	}

	public IBuiltinAtom createToBoolean(ITerm... terms) {
		return new ToBooleanBuiltin(terms);
	}

	public IBuiltinAtom createToDate(ITerm... terms) {
		return new ToDateBuiltin(terms);
	}

	public IBuiltinAtom createToDateTime(ITerm... terms) {
		return new ToDateTimeBuiltin(terms);
	}

	public IBuiltinAtom createToDayTimeDuration(ITerm... terms) {
		return new ToDayTimeDurationBuiltin(terms);
	}

	public IBuiltinAtom createToDecimal(ITerm... terms) {
		return new ToDecimalBuiltin(terms);
	}

	public IBuiltinAtom createToDouble(ITerm... terms) {
		return new ToDoubleBuiltin(terms);
	}

	public IBuiltinAtom createToDuration(ITerm... terms) {
		return new ToDurationBuiltin(terms);
	}

	public IBuiltinAtom createToFloat(ITerm... terms) {
		return new ToFloatBuiltin(terms);
	}

	public IBuiltinAtom createToGDay(ITerm... terms) {
		return new ToGDayBuiltin(terms);
	}

	public IBuiltinAtom createToGMonth(ITerm... terms) {
		return new ToGMonthBuiltin(terms);
	}

	public IBuiltinAtom createToGMonthDay(ITerm... terms) {
		return new ToGMonthDayBuiltin(terms);
	}

	public IBuiltinAtom createToGYear(ITerm... terms) {
		return new ToGYearBuiltin(terms);
	}

	public IBuiltinAtom createToGYearMonth(ITerm... terms) {
		return new ToGYearMonthBuiltin(terms);
	}

	public IBuiltinAtom createToHexBinary(ITerm... terms) {
		return new ToHexBinaryBuiltin(terms);
	}

	public IBuiltinAtom createToInteger(ITerm... terms) {
		return new ToIntegerBuiltin(terms);
	}

	public IBuiltinAtom createToIRI(ITerm... terms) {
		return new ToIriBuiltin(terms);
	}

	public IBuiltinAtom createToString(ITerm... terms) {
		return new ToStringBuiltin(terms);
	}

	public IBuiltinAtom createToText(ITerm... terms) {
		return new ToPlainLiteralBuiltin(terms);
	}

	public IBuiltinAtom createToTime(ITerm... terms) {
		return new ToTimeBuiltin(terms);
	}

	public IBuiltinAtom createToXMLLiteral(ITerm... terms) {
		return new ToXMLLiteralBuiltin(terms);
	}

	public IBuiltinAtom createToYearMonthDuration(ITerm... terms) {
		return new ToYearMonthDurationBuiltin(terms);
	}

	public IBuiltinAtom createIsBase64Binary(ITerm... terms) {
		return new IsBase64BinaryBuiltin(terms);
	}

	public IBuiltinAtom createIsBoolean(ITerm... terms) {
		return new IsBooleanBuiltin(terms);
	}

	public IBuiltinAtom createIsDate(ITerm... terms) {
		return new IsDateBuiltin(terms);
	}

	public IBuiltinAtom createIsDateTime(ITerm... terms) {
		return new IsDateTimeBuiltin(terms);
	}

	public IBuiltinAtom createIsDayTimeDuration(ITerm... terms) {
		return new IsDayTimeDurationBuiltin(terms);
	}

	public IBuiltinAtom createIsDecimal(ITerm... terms) {
		return new IsDecimalBuiltin(terms);
	}

	public IBuiltinAtom createIsDouble(ITerm... terms) {
		return new IsDoubleBuiltin(terms);
	}

	public IBuiltinAtom createIsDuration(ITerm... terms) {
		return new IsDurationBuiltin(terms);
	}

	public IBuiltinAtom createIsFloat(ITerm... terms) {
		return new IsFloatBuiltin(terms);
	}

	public IBuiltinAtom createIsGDay(ITerm... terms) {
		return new IsGDayBuiltin(terms);
	}

	public IBuiltinAtom createIsGMonth(ITerm... terms) {
		return new IsGMonthBuiltin(terms);
	}

	public IBuiltinAtom createIsGMonthDay(ITerm... terms) {
		return new IsGMonthBuiltin(terms);
	}

	public IBuiltinAtom createIsGYear(ITerm... terms) {
		return new IsGYearBuiltin(terms);
	}

	public IBuiltinAtom createIsGYearMonth(ITerm... terms) {
		return new IsGYearMonthBuiltin(terms);
	}

	public IBuiltinAtom createIsHexBinary(ITerm... terms) {
		return new IsHexBinaryBuiltin(terms);
	}

	public IBuiltinAtom createIsInteger(ITerm... terms) {
		return new IsIntegerBuiltin(terms);
	}

	public IBuiltinAtom createIsIRI(ITerm... terms) {
		return new IsIriBuiltin(terms);
	}

	public IBuiltinAtom createIsString(ITerm... terms) {
		return new IsStringBuiltin(terms);
	}

	public IBuiltinAtom createIsText(ITerm... terms) {
		return new IsPlainLiteralBuiltin(terms);
	}

	public IBuiltinAtom createIsTime(ITerm... terms) {
		return new IsTimeBuiltin(terms);
	}

	public IBuiltinAtom createIsXMLLiteral(ITerm... terms) {
		return new IsXMLLiteralBuiltin(terms);
	}

	public IBuiltinAtom createIsYearMonthDuration(ITerm... terms) {
		return new IsYearMonthDurationBuiltin(terms);
	}

	public IBuiltinAtom createIsAnyURI(ITerm... terms) {
		return new IsAnyURIBuiltin(terms);
	}

	public IBuiltinAtom createIsByte(ITerm... terms) {
		return new IsByteBuiltin(terms);
	}

	public IBuiltinAtom createIsFalse(ITerm... terms) {
		return new IsFalseBuiltin(terms);
	}

	public IBuiltinAtom createIsLanguage(ITerm... terms) {
		return new IsLanguageBuiltin(terms);
	}

	public IBuiltinAtom createIsLong(ITerm... terms) {
		return new IsLongBuiltin(terms);
	}

	public IBuiltinAtom createIsNCName(ITerm... terms) {
		return new IsNCNameBuiltin(terms);
	}

	public IBuiltinAtom createIsNMTOKEN(ITerm... terms) {
		return new IsNMTOKENBuiltin(terms);
	}

	public IBuiltinAtom createIsName(ITerm... terms) {
		return new IsNameBuiltin(terms);
	}

	public IBuiltinAtom createIsNegativeInteger(ITerm... terms) {
		return new IsNegativeIntegerBuiltin(terms);
	}

	public IBuiltinAtom createIsNonNegativeInteger(ITerm... terms) {
		return new IsNonNegativeIntegerBuiltin(terms);
	}

	public IBuiltinAtom createIsNonPositiveInteger(ITerm... terms) {
		return new IsNonPositiveIntegerBuiltin(terms);
	}

	public IBuiltinAtom createIsNormalizedString(ITerm... terms) {
		return new IsNormalizedStringBuiltin(terms);
	}

	public IBuiltinAtom createIsPositiveInteger(ITerm... terms) {
		return new IsPositiveIntegerBuiltin(terms);
	}

	public IBuiltinAtom createIsShort(ITerm... terms) {
		return new IsShortBuiltin(terms);
	}

	public IBuiltinAtom createIsToken(ITerm... terms) {
		return new IsTokenBuiltin(terms);
	}

	public IBuiltinAtom createIsUnsignedByte(ITerm... terms) {
		return new IsTokenBuiltin(terms);
	}

	public IBuiltinAtom createIsUnsignedInt(ITerm... terms) {
		return new IsUnsignedIntBuiltin(terms);
	}

	public IBuiltinAtom createIsUnsignedLong(ITerm... terms) {
		return new IsUnsignedLongBuiltin(terms);
	}

	public IBuiltinAtom createIsUnsignedShort(ITerm... terms) {
		return new IsUnsignedShortBuiltin(terms);
	}

	public IBuiltinAtom createIsInt(ITerm... terms) {
		return new IsIntBuiltin(terms);
	}

	public IBuiltinAtom createIntegerDivide(ITerm... terms) {
		return new IntegerDivideBuiltin(terms);
	}

	public IBuiltinAtom createIsNotAnyURI(ITerm... terms) {
		return new IsNotAnyURIBuiltin(terms);
	}

	public IBuiltinAtom createIsNotBase64Binary(ITerm... terms) {
		return new IsNotBase64BinaryBuiltin(terms); 
	}

	public IBuiltinAtom createIsNotBoolean(ITerm... terms) {
		return new IsNotBooleanBuiltin(terms);
	}

	public IBuiltinAtom createIsNotByte(ITerm... terms) {
		return new IsNotByteBuiltin(terms);
	}

	public IBuiltinAtom createIsNotDate(ITerm... terms) {
		return new IsNotDateBuiltin(terms);
	}

	public IBuiltinAtom createIsNotDateTime(ITerm... terms) {
		return new IsNotDateTimeBuiltin(terms);
	}

	public IBuiltinAtom createIsNotDayTimeDuration(ITerm... terms) {
		return new IsNotDayTimeDurationBuiltin(terms);
	}

	public IBuiltinAtom createIsNotDecimal(ITerm... terms) {
		return new IsNotDecimalBuiltin(terms);
	}

	public IBuiltinAtom createIsNotDouble(ITerm... terms) {
		return new IsNotDoubleBuiltin(terms);
	}

	public IBuiltinAtom createIsNotDuration(ITerm... terms) {
		return new IsNotDurationBuiltin(terms);
	}

	public IBuiltinAtom createIsNotFloat(ITerm... terms) {
		return new IsNotFloatBuiltin(terms);
	}

	public IBuiltinAtom createIsNotGDay(ITerm... terms) {
		return new IsNotGDayBuiltin(terms);
	}

	public IBuiltinAtom createIsNotGMonth(ITerm... terms) {
		return new IsNotGMonthBuiltin(terms);
	}

	public IBuiltinAtom createIsNotGMonthDay(ITerm... terms) {
		return new IsNotGMonthDayBuiltin(terms);
	}

	public IBuiltinAtom createIsNotGYear(ITerm... terms) {
		return new IsNotGYearBuiltin(terms);
	}

	public IBuiltinAtom createIsNotGYearMonth(ITerm... terms) {
		return new IsNotGYearMonthBuiltin(terms);
	}

	public IBuiltinAtom createIsNotHexBinary(ITerm... terms) {
		return new IsNotHexBinaryBuiltin(terms);
	}

	public IBuiltinAtom createIsNotIRI(ITerm... terms) {
		return new IsNotIriBuiltin(terms);
	}

	public IBuiltinAtom createIsNotInt(ITerm... terms) {
		return new IsNotIntBuiltin(terms);
	}

	public IBuiltinAtom createIsNotInteger(ITerm... terms) {
		return new IsNotIntegerBuiltin(terms);
	}

	public IBuiltinAtom createIsNotLanguage(ITerm... terms) {
		return new IsNotLanguageBuiltin(terms);
	}

	public IBuiltinAtom createIsNotLong(ITerm... terms) {
		return new IsNotLongBuiltin(terms);
	}

	public IBuiltinAtom createIsNotNCName(ITerm... terms) {
		return new IsNotNCNameBuiltin(terms);
	}

	public IBuiltinAtom createIsNotNMTOKEN(ITerm... terms) {
		return new IsNotNMTOKENBuiltin(terms);
	}

	public IBuiltinAtom createIsNotName(ITerm... terms) {
		return new IsNotNameBuiltin(terms);
	}

	public IBuiltinAtom createIsNotNegativeInteger(ITerm... terms) {
		return new IsNotNegativeIntegerBuiltin(terms);
	}

	public IBuiltinAtom createIsNotNonNegativeInteger(ITerm... terms) {
		return new IsNonNegativeIntegerBuiltin(terms);
	}

	public IBuiltinAtom createIsNotNonPositiveInteger(ITerm... terms) {
		return new IsNotNonPositiveIntegerBuiltin(terms);
	}

	public IBuiltinAtom createIsNotNormalizedString(ITerm... terms) {
		return new IsNotNormalizedStringBuiltin(terms);
	}

	public IBuiltinAtom createIsNotPositiveInteger(ITerm... terms) {
		return new IsNotPositiveIntegerBuiltin(terms);
	}

	public IBuiltinAtom createIsNotShort(ITerm... terms) {
		return new IsNotShortBuiltin(terms);
	}

	public IBuiltinAtom createIsNotString(ITerm... terms) {
		return new IsNotStringBuiltin(terms);
	}

	public IBuiltinAtom createIsNotText(ITerm... terms) {
		return new IsNotPlainLiteralBuiltin(terms);
	}

	public IBuiltinAtom createIsNotTime(ITerm... terms) {
		return new IsNotTimeBuiltin(terms);
	}

	public IBuiltinAtom createIsNotToken(ITerm... terms) {
		return new IsNotTokenBuiltin(terms);
	}

	public IBuiltinAtom createIsNotUnsignedByte(ITerm... terms) {
		return new IsNotUnsignedByteBuiltin(terms);
	}

	public IBuiltinAtom createIsNotUnsignedInt(ITerm... terms) {
		return new IsNotUnsignedIntBuiltin(terms);
	}

	public IBuiltinAtom createIsNotUnsignedLong(ITerm... terms) {
		return new IsNotUnsignedLongBuiltin(terms);
	}

	public IBuiltinAtom createIsNotUnsignedShort(ITerm... terms) {
		return new IsNotUnsignedShortBuiltin(terms);
	}

	public IBuiltinAtom createIsNotXMLLiteral(ITerm... terms) {
		return new IsNotXMLLiteralBuiltin(terms);
	}

	public IBuiltinAtom createIsNotYearMonthDuration(ITerm... terms) {
		return new IsNotYearMonthDurationBuiltin(terms);
	}

	public IBuiltinAtom createIsDatatype(ITerm... terms) {
		return new IsDatatypeBuiltin(terms);
	}

	public IBuiltinAtom createIsNotDatatype(ITerm... terms) {
		return new IsNotDatatypeBuiltin(terms);
	}

	public IBuiltinAtom createIsDateTimeStamp(ITerm... terms) {
		return new IsDateTimeStampBuiltin(terms);
	}

	public IBuiltinAtom createIsNotDateTimeStamp(ITerm... terms) {
		return new IsNotDateTimeStampBuiltin(terms);
	}

}
