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


import java.util.List;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.builtins.IBuiltinAtom;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * An interface that can be used to create set of built-ins supported 
 * by this engine.
 * </p>
 * <p>
 * $Id: IBuiltInsFactory.java,v 1.7 2007-10-12 13:00:42 bazbishop237 Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @date 17.03.2006 11:55:35
 * @version $Revision: 1.7 $
 */
public interface IBuiltinsFactory {

	/**
	 * Creates an add builtin.
	 * 
	 * @param t0
	 *            the first summand
	 * @param t1
	 *            the second summand
	 * @param t2
	 *            the sum
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 */
	public abstract IBuiltinAtom createAddBuiltin(final ITerm t0, final ITerm t1, final ITerm t2);

	/**
	 * Creates a subtract builtin.
	 * @param t0 the minuend
	 * @param t1 the subtrahend
	 * @param t2 the difference
	 * @return the constructed builtin
	 * @throws NullPointerException if one of the terms is <code>null</code>
	 */
	public abstract IBuiltinAtom createSubtractBuiltin(final ITerm t0, final ITerm t1, final ITerm t2);

	/**
	 * Creates a multiply builtin.
	 * @param t0 the first factor
	 * @param t1 the second factor
	 * @param t2 the product
	 * @return the constructed builtin
	 * @throws NullPointerException if one of the terms is <code>null</code>
	 */
	public abstract IBuiltinAtom createMultiplyBuiltin(final ITerm t0, final ITerm t1, final ITerm t2);

	/**
	 * Creates a divide builtin.
	 * @param t0 the dividend
	 * @param t1 the diviso
	 * @param t2 the quotient
	 * @return the constructed builtin
	 * @throws NullPointerException if one of the terms is <code>null</code>
	 */
	public abstract IBuiltinAtom createDivideBuiltin(final ITerm t0, final ITerm t1, final ITerm t2);

	/**
	 * Creates a modulus builtin.
	 * @param t0 the numerator
	 * @param t1 the denominator
	 * @param t2 the result
	 * @return the constructed builtin
	 * @throws NullPointerException if one of the terms is <code>null</code>
	 */
	public abstract IBuiltinAtom createModulusBuiltin(final ITerm t0, final ITerm t1, final ITerm t2);

	/**
	 * Creates an equal builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 */
	public abstract IBuiltinAtom createEqual(final ITerm t0, final ITerm t1);

	/**
	 * Creates an unequal builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 */
	public abstract IBuiltinAtom createUnequal(final ITerm t0, final ITerm t1);

	/**
	 * Create an EXACT_EQUAL built-in.
	 * @param t0 The first term.
	 * @param t1 The second term.
	 * @return The built-in instance
	 */
	IBuiltinAtom createExactEqual(final ITerm t0, final ITerm t1);

	/**
	 * Create a NOT_EXACT_EQUAL built-in.
	 * @param t0 The first term.
	 * @param t1 The second term.
	 * @return The built-in instance
	 */

	IBuiltinAtom createNotExactEqual(final ITerm t0, final ITerm t1);

	/**
	 * Creates a less builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 */
	public abstract IBuiltinAtom createLess(final ITerm t0, final ITerm t1);

	/**
	 * Creates an less-equal builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 */
	public abstract IBuiltinAtom createLessEqual(final ITerm t0, final ITerm t1);

	/**
	 * Creates a greater builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 */
	public abstract IBuiltinAtom createGreater(final ITerm t0, final ITerm t1);

	/**
	 * Creates a greater-equal builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 */
	public abstract IBuiltinAtom createGreaterEqual(final ITerm t0,
			final ITerm t1);

	
	//  SOA4ALL D3.1.4 Defining the features of the WSML-Rule v2.0 language

	/**
	 * Creates isDatatype builtin. 
	 *
	 * @param terms The terms.
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsDatatype(ITerm ... iTerm);
	
	/**
	 * Creates isNotDatatype builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsNotDatatype(ITerm ... iTerm);

	/**
	 * Creates hasDatatype builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct	 
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createHasDatatype(ITerm ... terms);


	/**
	 * Creates NumericModulus builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct	 
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createNumericModulus(ITerm ... terms);

	/**
	 * Creates NumericModulus builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct	 
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringCompare(ITerm ... terms);

	/**
	 * Creates StringConcat builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct	 
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringConcat(ITerm ... terms);

	/**
	 * Creates StringJoin builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct	 
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringJoin(ITerm ... terms);

	/**
	 * Creates StringSubstring builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not 1
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringSubstring(ITerm ... terms);

	/**
	 * Creates StringLength builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct	 
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringLength(ITerm ... terms);

	/**
	 * Creates StringToUpper builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct	 
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringToUpper(ITerm ... terms);

	/**
	 * Creates StringToLower builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct	 
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringToLower(ITerm ... terms);

	/**
	 * Creates StringUriEncode builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct	 
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringUriEncode(ITerm ... terms);

	/**
	 * Creates StringIriToUri builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringIriToUri(ITerm ... terms);

	/**
	 * Creates StringEscapeHtmlUri builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringEscapeHtmlUri(ITerm ... terms);

	/**
	 * Creates StringSubstringBefore builtin. 
	 *
	 * @param terms The terms.
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringSubstringBefore(ITerm ... terms);

	/**
	 * Creates StringSubstringAfter builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringSubstringAfter(ITerm ... terms);

	/**
	 * Creates StringReplace builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringReplace(ITerm ... terms);

	/**
	 * Creates StringContains builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringContains(ITerm ... terms);

	/**
	 * Creates StringStartsWith builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringStartsWith(ITerm ... terms);

	/**
	 * Creates StringEndsWith builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringEndsWith(ITerm ... terms);

	/**
	 * Creates StringMatches builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringMatches(ITerm ... terms);

	/**
	 * Creates YearPart builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createYearPart(ITerm ... terms);

	/**
	 * Creates MonthPart builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createMonthPart(ITerm ... terms);

	/**
	 * Creates DayPart builtin. 
	 *
	 * @param terms The terms.
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createDayPart(ITerm ... terms);

	/**
	 * Creates HourPart builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createHourPart(ITerm ... terms);

	/**
	 * Creates MinutePart builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createMinutePart(ITerm ... terms);

	/**
	 * Creates SecondPart builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createSecondPart(ITerm ... terms);

	/**
	 * Creates TimezonePart builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createTimezonePart(ITerm ... terms);

	/**
	 * Creates TextFromStringLang builtin. 
	 *
	 * @param terms The terms.
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createTextFromStringLang(ITerm ... terms);
	
	/**
	 * Creates TextFromString builtin. 
	 *
	 * @param terms The terms.
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createTextFromString(ITerm ... terms);

	/**
	 * Creates StringFromText builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createStringFromText(ITerm ... terms);

	
	/**
	 * Creates LangFromText builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createLangFromText(ITerm ... terms);

	/**
	 * Creates TextCompare builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createTextCompare(ITerm ... terms);

	/**
	 * Creates ToBase64Binary builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToBase64Binary(ITerm ... terms);

	/**
	 * Creates ToBoolean builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToBoolean(ITerm ... terms);

	/**
	 * Creates ToDate builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToDate(ITerm ... terms);

	/**
	 * Creates ToDateTime builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToDateTime(ITerm ... terms);

	/**
	 * Creates ToDayTimeDuration builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToDayTimeDuration(ITerm ... terms);

	/**
	 * Creates ToDecimal builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToDecimal(ITerm ... terms);

	/**
	 * Creates ToDouble builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToDouble(ITerm ... terms);

	/**
	 * Creates ToDuration builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToDuration(ITerm ... terms);

	/**
	 * Creates ToFloat builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToFloat(ITerm ... terms);

	/**
	 * Creates ToGDay builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToGDay(ITerm ... terms);

	/**
	 * Creates ToGMonth builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToGMonth(ITerm ... terms);

	/**
	 * Creates ToGMonthDay builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToGMonthDay(ITerm ... terms);

	/**
	 * Creates ToGYear builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToGYear(ITerm ... terms);

	/**
	 * Creates ToGYearMonth builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToGYearMonth(ITerm ... terms);

	/**
	 * Creates ToHexBinary builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToHexBinary(ITerm ... terms);

	/**
	 * Creates ToInteger builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToInteger(ITerm ... terms);

	/**
	 * Creates ToIRI builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToIRI(ITerm ... terms);

	/**
	 * Creates ToString builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToString(ITerm ... terms);

	/**
	 * Creates ToText builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToText(ITerm ... terms);

	/**
	 * Creates ToTime builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToTime(ITerm ... terms);

	/**
	 * Creates ToXMLLiteral builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToXMLLiteral(ITerm ... terms);

	/**
	 * Creates ToYearMonthDuration builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createToYearMonthDuration(ITerm ... terms);

	/**
	 * Creates IsBase64Binary builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsBase64Binary(ITerm ... terms);

	/**
	 * Creates IsBoolean builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsBoolean(ITerm ... terms);
	
	/**
	 * Creates IsDate builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsDate(ITerm ... terms);

	/**
	 * Creates IsDateTime builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsDateTime(ITerm ... terms);

	/**
	 * Creates IsDayTimeDuration builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsDayTimeDuration(ITerm ... terms);

	/**
	 * Creates IsDecimal builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsDecimal(ITerm ... terms);

	/**
	 * Creates IsDuration builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsDuration(ITerm ... terms);

	/**
	 * Creates IsFloat builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsFloat(ITerm ... terms);

	/**
	 * Creates IsGDay builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsGDay(ITerm ... terms);

	/**
	 * Creates IsGMonth builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsGMonth(ITerm ... terms);

	/**
	 * Creates IsGMonthDay builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsGMonthDay(ITerm ... terms);

	/**
	 * Creates IsGYear builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsGYear(ITerm ... terms);

	/**
	 * Creates IsGYearMonth builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsGYearMonth(ITerm ... terms);

	/**
	 * Creates IsHexBinary builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsHexBinary(ITerm ... terms);

	/**
	 * Creates IsInteger builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsInteger(ITerm ... terms);

	/**
	 * Creates IsIRI builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsIRI(ITerm ... terms);

	/**
	 * Creates IsString builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsString(ITerm ... terms);

	/**
	 * Creates IsText builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsText(ITerm ... terms);

	/**
	 * Creates IsTime builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsTime(ITerm ... terms);

	/**
	 * Creates IsXMLLiteral builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsXMLLiteral(ITerm ... terms);

	/**
	 * Creates IsYearMonthDuration builtin. 
	 *
	 * @param terms The terms. 
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not correct
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public abstract IBuiltinAtom createIsYearMonthDuration(ITerm ... terms);

	/**
	 * Creates the builtin representing true.  
	 * 
	 * @return The builtin representing true.
	 */
	public abstract IAtom createTrue();

	/**
	 * Creates the builtin representing false.  
	 *
	 * @return The builtin representing false.  
	 */
	public abstract IAtom createFalse();
	
}

