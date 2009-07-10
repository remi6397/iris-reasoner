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
import org.deri.iris.builtins.datatype.IsBase64BinaryBuiltin;
import org.deri.iris.builtins.datatype.IsBooleanBuiltin;
import org.deri.iris.builtins.datatype.IsDatatypeBuiltin;
import org.deri.iris.builtins.datatype.IsDateBuiltin;
import org.deri.iris.builtins.datatype.IsDateTimeBuiltin;
import org.deri.iris.builtins.datatype.IsDayTimeDurationBuiltin;
import org.deri.iris.builtins.datatype.IsDecimalBuiltin;
import org.deri.iris.builtins.datatype.IsDurationBuiltin;
import org.deri.iris.builtins.datatype.IsFloatBuiltin;
import org.deri.iris.builtins.datatype.IsGDayBuiltin;
import org.deri.iris.builtins.datatype.IsGMonthBuiltin;
import org.deri.iris.builtins.datatype.IsGYearBuiltin;
import org.deri.iris.builtins.datatype.IsGYearMonthBuiltin;
import org.deri.iris.builtins.datatype.IsHexBinaryBuiltin;
import org.deri.iris.builtins.datatype.IsIntegerBuiltin;
import org.deri.iris.builtins.datatype.IsIriBuiltin;
import org.deri.iris.builtins.datatype.IsNotDatatypeBuiltin;
import org.deri.iris.builtins.datatype.IsStringBuiltin;
import org.deri.iris.builtins.datatype.IsTextBuiltin;
import org.deri.iris.builtins.datatype.IsTimeBuiltin;
import org.deri.iris.builtins.datatype.IsXMLLiteralBuiltin;
import org.deri.iris.builtins.datatype.IsYearMonthDurationBuiltin;
import org.deri.iris.builtins.datatype.ToBase64Builtin;
import org.deri.iris.builtins.datatype.ToBooleanBuiltin;
import org.deri.iris.builtins.datatype.ToDateBuiltin;
import org.deri.iris.builtins.datatype.ToDateTimeBuiltin;
import org.deri.iris.builtins.datatype.ToDayTimeDurationBuiltin;
import org.deri.iris.builtins.datatype.ToDecimalBuiltin;
import org.deri.iris.builtins.datatype.ToDoubleBuiltin;
import org.deri.iris.builtins.datatype.ToDurationBuiltin;
import org.deri.iris.builtins.datatype.ToFloatBuiltin;
import org.deri.iris.builtins.datatype.ToGDayBuiltin;
import org.deri.iris.builtins.datatype.ToGMonthBuiltin;
import org.deri.iris.builtins.datatype.ToGMonthDayBuiltin;
import org.deri.iris.builtins.datatype.ToGYearBuiltin;
import org.deri.iris.builtins.datatype.ToGYearMonthBuiltin;
import org.deri.iris.builtins.datatype.ToHexBinaryBuiltin;
import org.deri.iris.builtins.datatype.ToIntegerBuiltin;
import org.deri.iris.builtins.datatype.ToIriBuiltin;
import org.deri.iris.builtins.datatype.ToStringBuiltin;
import org.deri.iris.builtins.datatype.ToTextBuiltin;
import org.deri.iris.builtins.datatype.ToTimeBuiltin;
import org.deri.iris.builtins.datatype.ToXMLLiteralBuiltin;
import org.deri.iris.builtins.datatype.ToYearMonthDurationBuiltin;
import org.deri.iris.builtins.date.DayPartBuiltin;
import org.deri.iris.builtins.date.HourPartBuiltin;
import org.deri.iris.builtins.date.MinutePartBuiltin;
import org.deri.iris.builtins.date.MonthPartBuiltin;
import org.deri.iris.builtins.date.SecondPartBuiltin;
import org.deri.iris.builtins.date.TimezonePartBuiltin;
import org.deri.iris.builtins.date.YearPartBuiltin;
import org.deri.iris.builtins.string.LangFromTextBuiltin;
import org.deri.iris.builtins.string.StringCompareBuiltin;
import org.deri.iris.builtins.string.StringConcatBuiltin;
import org.deri.iris.builtins.string.StringContainsBuiltin;
import org.deri.iris.builtins.string.StringEndsWithBuiltin;
import org.deri.iris.builtins.string.StringEscapeHtmlUriBuiltin;
import org.deri.iris.builtins.string.StringFromTextBuiltin;
import org.deri.iris.builtins.string.StringIriToUriBuiltin;
import org.deri.iris.builtins.string.StringJoinBuiltin;
import org.deri.iris.builtins.string.StringLengthBuiltin;
import org.deri.iris.builtins.string.StringMatchesBuiltin;
import org.deri.iris.builtins.string.StringReplaceBuiltin;
import org.deri.iris.builtins.string.StringStartsWithBuiltin;
import org.deri.iris.builtins.string.StringSubstringAfterBuiltin;
import org.deri.iris.builtins.string.StringSubstringBeforeBuiltin;
import org.deri.iris.builtins.string.StringSubstringBuiltin;
import org.deri.iris.builtins.string.StringToLowerBuiltin;
import org.deri.iris.builtins.string.StringToUpperBuiltin;
import org.deri.iris.builtins.string.StringUriEncodeBuiltin;
import org.deri.iris.builtins.string.TextCompareBuiltin;
import org.deri.iris.builtins.string.TextFromStringBuiltin;
import org.deri.iris.builtins.string.TextFromStringLangBuiltin;

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

	private static final IBuiltinsFactory INSTANCE = new BuiltinsFactory();

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

	public IBuiltinAtom createAddBuiltin(final ITerm t0, final ITerm t1, final ITerm t2){
		return new AddBuiltin(t0, t1, t2);
	}

	public IBuiltinAtom createSubtractBuiltin(final ITerm t0, final ITerm t1, final ITerm t2){
		return new SubtractBuiltin(t0, t1, t2);
	}

	public IBuiltinAtom createMultiplyBuiltin(final ITerm t0, final ITerm t1, final ITerm t2){
		return new MultiplyBuiltin(t0, t1, t2);
	}

	public IBuiltinAtom createDivideBuiltin(final ITerm t0, final ITerm t1, final ITerm t2){
		return new DivideBuiltin(t0, t1, t2);
	}

	public IBuiltinAtom createModulusBuiltin(final ITerm t0, final ITerm t1, final ITerm t2){
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
	
	public IBuiltinAtom createExactEqual(final ITerm t0, final ITerm t1)
	{
		return new ExactEqualBuiltin(t0, t1);
	}

	public IBuiltinAtom createNotExactEqual(final ITerm t0, final ITerm t1)
	{
		return new NotExactEqualBuiltin(t0, t1);
	}
	
	// check D3.1.4 Defining the features of the WSML-Rule v2.0 language

	public IBuiltinAtom createIsDatatype(final ITerm ... terms) {
		return new IsDatatypeBuiltin(terms);
	}

	public IBuiltinAtom createIsNotDatatype(ITerm... terms) {
		return new IsNotDatatypeBuiltin(terms);
	}

	public IBuiltinAtom createHasDatatype(ITerm... terms) {
//		return new HasDatatypeBuiltin(terms);
//		TODO check 
		return null;
	}

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
		return new StringSubstringBuiltin(terms);
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
		return new StringSubstringBeforeBuiltin(terms);
	}

	public IBuiltinAtom createStringSubstringAfter(ITerm... terms) {
		return new StringSubstringAfterBuiltin(terms);
	}

	public IBuiltinAtom createStringReplace(ITerm... terms) {
		return new StringReplaceBuiltin(terms);
	}

	public IBuiltinAtom createStringContains(ITerm... terms) {
		return new StringContainsBuiltin(terms);
	}

	public IBuiltinAtom createStringStartsWith(ITerm... terms) {
		return new StringStartsWithBuiltin(terms);
	}

	public IBuiltinAtom createStringEndsWith(ITerm... terms) {
		return new StringEndsWithBuiltin(terms);
	}
	
	public IBuiltinAtom createStringMatches(ITerm... terms) {
		return new StringMatchesBuiltin(terms);
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
		return new TextFromStringLangBuiltin(terms);
	}

	public IBuiltinAtom createStringFromText(ITerm... terms) {
		return new StringFromTextBuiltin(terms);
	}

	public IBuiltinAtom createLangFromText(ITerm... terms) {
		return new LangFromTextBuiltin(terms);
	}

	public IBuiltinAtom createTextFromString(ITerm... terms) {
		return new TextFromStringBuiltin(terms);
	}

	public IBuiltinAtom createTextCompare(ITerm... terms) {
		return new TextCompareBuiltin(terms);
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
		return new ToTextBuiltin(terms);
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
		return new IsTextBuiltin(terms);
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
	
	
}
