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
package org.deri.iris.builtins;

import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.terms.concrete.IDateTerm;
import org.deri.iris.api.terms.concrete.IDateTime;
import org.deri.iris.api.terms.concrete.IDuration;
import org.deri.iris.api.terms.concrete.ITime;
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * Tests the <code>BuiltinHelper</code>.
 * </p>
 * <p>
 * $Id: BuiltinHelperTest.java,v 1.3 2007-09-13 15:20:37 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.3 $
 */
public class BuiltinHelperTest extends TestCase {

	private static final INumericTerm I_2 = CONCRETE.createInteger(2);
	private static final INumericTerm I_5 = CONCRETE.createInteger(5);
	private static final INumericTerm I_10 = CONCRETE.createInteger(10);

	private static final INumericTerm F_2 = CONCRETE.createFloat(2f);
	private static final INumericTerm F_5 = CONCRETE.createFloat(5f);
	private static final INumericTerm F_55 = CONCRETE.createFloat(5.5f);
	private static final INumericTerm F_10 = CONCRETE.createFloat(10f);

	private static final INumericTerm D_2 = CONCRETE.createDouble(2d);
	private static final INumericTerm D_5 = CONCRETE.createDouble(5d);
	private static final INumericTerm D_55 = CONCRETE.createDouble(5.5d);
	private static final INumericTerm D_10 = CONCRETE.createDouble(10d);

	private static final ITerm S_a = TERM.createString("a");
	private static final ITerm S_b = TERM.createString("b");
	private static final ITerm S_x = TERM.createString("x");
	private static final ITerm S_xy = TERM.createString("xy");

	private static final ITerm A = TERM.createVariable("A");
	private static final ITerm B = TERM.createVariable("B");
	private static final ITerm C = TERM.createVariable("C");
	private static final ITerm D = TERM.createVariable("D");
	private static final ITerm E = TERM.createVariable("E");

	public static Test suite() {
		return new TestSuite(BuiltinHelperTest.class, BuiltinHelperTest.class.getSimpleName());
	}

	/**
	 * Tests the <code>numbersEqual</code> method.
	 */
	public void testNumersEqual() {
		assertTrue(BuiltinHelper.numbersEqual(I_2, I_2));
		assertTrue(BuiltinHelper.numbersEqual(I_2, F_2));
		assertTrue(BuiltinHelper.numbersEqual(I_2, D_2));
		assertTrue(BuiltinHelper.numbersEqual(F_2, F_2));
		assertTrue(BuiltinHelper.numbersEqual(F_2, D_2));
		assertTrue(BuiltinHelper.numbersEqual(D_2, D_2));

		assertTrue(BuiltinHelper.numbersEqual(D_55, D_55));
		assertTrue(BuiltinHelper.numbersEqual(F_55, D_55));
		assertTrue(BuiltinHelper.numbersEqual(F_55, F_55));

		assertFalse(BuiltinHelper.numbersEqual(I_2, I_5));
		assertFalse(BuiltinHelper.numbersEqual(I_2, F_5));
		assertFalse(BuiltinHelper.numbersEqual(I_2, D_5));
		assertFalse(BuiltinHelper.numbersEqual(F_2, F_5));
		assertFalse(BuiltinHelper.numbersEqual(F_2, D_5));
		assertFalse(BuiltinHelper.numbersEqual(D_2, D_5));

		assertFalse(BuiltinHelper.numbersEqual(I_5, F_55));
		assertFalse(BuiltinHelper.numbersEqual(I_5, D_55));
		assertFalse(BuiltinHelper.numbersEqual(F_5, F_55));
		assertFalse(BuiltinHelper.numbersEqual(F_5, D_55));
		assertFalse(BuiltinHelper.numbersEqual(D_5, F_55));
		assertFalse(BuiltinHelper.numbersEqual(D_5, D_55));
	}

	/**
	 * Tests the <code>numbersCompare</code> method.
	 */
	public void testNumbersCompare() {
		assertTrue(BuiltinHelper.numbersCompare(I_2, I_2) == 0);
		assertTrue(BuiltinHelper.numbersCompare(I_2, I_5) < 0);
		assertTrue(BuiltinHelper.numbersCompare(I_5, I_2) > 0);

		assertTrue(BuiltinHelper.numbersCompare(I_2, F_2) == 0);
		assertTrue(BuiltinHelper.numbersCompare(I_2, F_5) < 0);
		assertTrue(BuiltinHelper.numbersCompare(I_5, F_2) > 0);

		assertTrue(BuiltinHelper.numbersCompare(I_2, D_2) == 0);
		assertTrue(BuiltinHelper.numbersCompare(I_2, D_5) < 0);
		assertTrue(BuiltinHelper.numbersCompare(I_5, D_2) > 0);

		assertTrue(BuiltinHelper.numbersCompare(F_2, F_2) == 0);
		assertTrue(BuiltinHelper.numbersCompare(F_2, F_5) < 0);
		assertTrue(BuiltinHelper.numbersCompare(F_5, F_2) > 0);

		assertTrue(BuiltinHelper.numbersCompare(F_2, D_2) == 0);
		assertTrue(BuiltinHelper.numbersCompare(F_2, D_5) < 0);
		assertTrue(BuiltinHelper.numbersCompare(F_5, D_2) > 0);

		assertTrue(BuiltinHelper.numbersCompare(D_2, D_2) == 0);
		assertTrue(BuiltinHelper.numbersCompare(D_2, D_5) < 0);
		assertTrue(BuiltinHelper.numbersCompare(D_5, D_2) > 0);

		assertTrue(BuiltinHelper.numbersCompare(I_5, F_55) < 0);
		assertTrue(BuiltinHelper.numbersCompare(I_5, D_55) < 0);
		assertTrue(BuiltinHelper.numbersCompare(F_55, I_5) > 0);
		assertTrue(BuiltinHelper.numbersCompare(D_55, I_5) > 0);

		assertTrue(BuiltinHelper.numbersCompare(F_55, F_55) == 0);
		assertTrue(BuiltinHelper.numbersCompare(F_5, F_55) < 0);
		assertTrue(BuiltinHelper.numbersCompare(F_55, F_5) > 0);

		assertTrue(BuiltinHelper.numbersCompare(F_55, D_55) == 0);
		assertTrue(BuiltinHelper.numbersCompare(F_5, D_55) < 0);
		assertTrue(BuiltinHelper.numbersCompare(F_55, D_5) > 0);

		assertTrue(BuiltinHelper.numbersCompare(D_55, D_55) == 0);
		assertTrue(BuiltinHelper.numbersCompare(D_5, D_55) < 0);
		assertTrue(BuiltinHelper.numbersCompare(D_55, D_5) > 0);
	}

	/**
	 * Tests the <code>compare</code> method.
	 */
	public void testCompare() {
		// do the same test as in testNumbersCompare
		assertTrue(BuiltinHelper.compare(I_2, I_2) == 0);
		assertTrue(BuiltinHelper.compare(I_2, I_5) < 0);
		assertTrue(BuiltinHelper.compare(I_5, I_2) > 0);

		assertTrue(BuiltinHelper.compare(I_2, F_2) == 0);
		assertTrue(BuiltinHelper.compare(I_2, F_5) < 0);
		assertTrue(BuiltinHelper.compare(I_5, F_2) > 0);

		assertTrue(BuiltinHelper.compare(I_2, D_2) == 0);
		assertTrue(BuiltinHelper.compare(I_2, D_5) < 0);
		assertTrue(BuiltinHelper.compare(I_5, D_2) > 0);

		assertTrue(BuiltinHelper.compare(F_2, F_2) == 0);
		assertTrue(BuiltinHelper.compare(F_2, F_5) < 0);
		assertTrue(BuiltinHelper.compare(F_5, F_2) > 0);

		assertTrue(BuiltinHelper.compare(F_2, D_2) == 0);
		assertTrue(BuiltinHelper.compare(F_2, D_5) < 0);
		assertTrue(BuiltinHelper.compare(F_5, D_2) > 0);

		assertTrue(BuiltinHelper.compare(D_2, D_2) == 0);
		assertTrue(BuiltinHelper.compare(D_2, D_5) < 0);
		assertTrue(BuiltinHelper.compare(D_5, D_2) > 0);

		assertTrue(BuiltinHelper.compare(I_5, F_55) < 0);
		assertTrue(BuiltinHelper.compare(I_5, D_55) < 0);
		assertTrue(BuiltinHelper.compare(F_55, I_5) > 0);
		assertTrue(BuiltinHelper.compare(D_55, I_5) > 0);

		assertTrue(BuiltinHelper.compare(F_55, F_55) == 0);
		assertTrue(BuiltinHelper.compare(F_5, F_55) < 0);
		assertTrue(BuiltinHelper.compare(F_55, F_5) > 0);

		assertTrue(BuiltinHelper.compare(F_55, D_55) == 0);
		assertTrue(BuiltinHelper.compare(F_5, D_55) < 0);
		assertTrue(BuiltinHelper.compare(F_55, D_5) > 0);

		assertTrue(BuiltinHelper.compare(D_55, D_55) == 0);
		assertTrue(BuiltinHelper.compare(D_5, D_55) < 0);
		assertTrue(BuiltinHelper.compare(D_55, D_5) > 0);

		// compare other terms
		assertTrue(BuiltinHelper.compare(S_a, S_a) == 0);
		assertTrue(BuiltinHelper.compare(S_a, S_b) < 0);
		assertTrue(BuiltinHelper.compare(S_b, S_a) > 0);

		assertTrue(BuiltinHelper.compare(S_x, S_x) == 0);
		assertTrue(BuiltinHelper.compare(S_x, S_xy) < 0);
		assertTrue(BuiltinHelper.compare(S_xy, S_x) > 0);
	}

	/**
	 * Tests the <code>equal</code> method.
	 */
	public void testEqual() {
		// do the same tests as in testNumersEqual
		assertTrue(BuiltinHelper.equal(I_2, I_2));
		assertTrue(BuiltinHelper.equal(I_2, F_2));
		assertTrue(BuiltinHelper.equal(I_2, D_2));
		assertTrue(BuiltinHelper.equal(F_2, F_2));
		assertTrue(BuiltinHelper.equal(F_2, D_2));
		assertTrue(BuiltinHelper.equal(D_2, D_2));

		assertTrue(BuiltinHelper.equal(D_55, D_55));
		assertTrue(BuiltinHelper.equal(F_55, D_55));
		assertTrue(BuiltinHelper.equal(F_55, F_55));

		assertFalse(BuiltinHelper.equal(I_2, I_5));
		assertFalse(BuiltinHelper.equal(I_2, F_5));
		assertFalse(BuiltinHelper.equal(I_2, D_5));
		assertFalse(BuiltinHelper.equal(F_2, F_5));
		assertFalse(BuiltinHelper.equal(F_2, D_5));
		assertFalse(BuiltinHelper.equal(D_2, D_5));

		assertFalse(BuiltinHelper.equal(I_5, F_55));
		assertFalse(BuiltinHelper.equal(I_5, D_55));
		assertFalse(BuiltinHelper.equal(F_5, F_55));
		assertFalse(BuiltinHelper.equal(F_5, D_55));
		assertFalse(BuiltinHelper.equal(D_5, F_55));
		assertFalse(BuiltinHelper.equal(D_5, D_55));

		// test other terms
		assertTrue(BuiltinHelper.equal(S_a, S_a));

		assertFalse(BuiltinHelper.equal(S_a, S_b));
		assertFalse(BuiltinHelper.equal(S_a, I_2));
	}

	/**
	 * Tests the <code>add</code> method.
	 */
	public void testAdd() {
		final INumericTerm I_3 = CONCRETE.createInteger(3);
		final INumericTerm F_3 = CONCRETE.createFloat(3f);
		final INumericTerm D_3 = CONCRETE.createDouble(3d);

		final INumericTerm F_25 = CONCRETE.createFloat(2.5f);
		final INumericTerm D_25 = CONCRETE.createDouble(2.5d);

		assertEquals(I_5, BuiltinHelper.add(I_2, I_3));
		assertEquals(F_5, BuiltinHelper.add(I_2, F_3));
		assertEquals(D_5, BuiltinHelper.add(I_2, D_3));

		assertEquals(F_5, BuiltinHelper.add(F_2, F_3));
		assertEquals(D_5, BuiltinHelper.add(F_2, D_3));

		assertEquals(D_5, BuiltinHelper.add(D_2, D_3));

		assertEquals(F_55, BuiltinHelper.add(I_3, F_25));
		assertEquals(F_55, BuiltinHelper.add(F_3, F_25));
		assertEquals(D_55, BuiltinHelper.add(D_3, F_25));

		assertEquals(D_55, BuiltinHelper.add(I_3, D_25));
		assertEquals(D_55, BuiltinHelper.add(F_3, D_25));
		assertEquals(D_55, BuiltinHelper.add(D_3, D_25));
	}

	/**
	 * Tests the <code>subtract</code> method.
	 */
	public void testSubtract() {
		final INumericTerm I_3 = CONCRETE.createInteger(3);
		final INumericTerm F_3 = CONCRETE.createFloat(3f);
		final INumericTerm D_3 = CONCRETE.createDouble(3d);

		final INumericTerm F_15 = CONCRETE.createFloat(1.5f);
		final INumericTerm D_15 = CONCRETE.createDouble(1.5d);

		final INumericTerm F_35 = CONCRETE.createFloat(3.5f);
		final INumericTerm D_35 = CONCRETE.createDouble(3.5d);

		assertEquals(I_2, BuiltinHelper.subtract(I_5, I_3));
		assertEquals(F_2, BuiltinHelper.subtract(I_5, F_3));
		assertEquals(D_2, BuiltinHelper.subtract(I_5, D_3));

		assertEquals(F_2, BuiltinHelper.subtract(F_5, F_3));
		assertEquals(D_2, BuiltinHelper.subtract(F_5, D_3));

		assertEquals(D_2, BuiltinHelper.subtract(D_5, D_3));

		assertEquals(F_15, BuiltinHelper.subtract(I_5, F_35));
		assertEquals(F_15, BuiltinHelper.subtract(F_5, F_35));
		assertEquals(D_15, BuiltinHelper.subtract(D_5, F_35));

		assertEquals(D_15, BuiltinHelper.subtract(I_5, D_35));
		assertEquals(D_15, BuiltinHelper.subtract(F_5, D_35));
		assertEquals(D_15, BuiltinHelper.subtract(D_5, D_35));
	}

	/**
	 * Tests the <code>multiply</code> method.
	 */
	public void testMultiply() {
		final INumericTerm F_11 = CONCRETE.createFloat(11f);
		final INumericTerm D_11 = CONCRETE.createDouble(11d);

		assertEquals(I_10, BuiltinHelper.multiply(I_2, I_5));
		assertEquals(F_10, BuiltinHelper.multiply(I_2, F_5));
		assertEquals(D_10, BuiltinHelper.multiply(I_2, D_5));

		assertEquals(F_10, BuiltinHelper.multiply(F_2, F_5));
		assertEquals(D_10, BuiltinHelper.multiply(F_2, D_5));

		assertEquals(D_10, BuiltinHelper.multiply(D_2, D_5));

		assertEquals(F_11, BuiltinHelper.multiply(I_2, F_55));
		assertEquals(F_11, BuiltinHelper.multiply(F_2, F_55));
		assertEquals(D_11, BuiltinHelper.multiply(D_2, F_55));

		assertEquals(D_11, BuiltinHelper.multiply(I_2, D_55));
		assertEquals(D_11, BuiltinHelper.multiply(F_2, D_55));
		assertEquals(D_11, BuiltinHelper.multiply(D_2, D_55));
	}

	/**
	 * Tests the <code>divide</code> method.
	 */
	public void testDivide() {
		final INumericTerm F_25 = CONCRETE.createFloat(2.5f);
		final INumericTerm D_25 = CONCRETE.createDouble(2.5d);

		assertEquals(I_2, BuiltinHelper.divide(I_10, I_5));
		assertEquals(F_2, BuiltinHelper.divide(I_10, F_5));
		assertEquals(D_2, BuiltinHelper.divide(I_10, D_5));

		assertEquals(F_2, BuiltinHelper.divide(F_10, F_5));
		assertEquals(D_2, BuiltinHelper.divide(F_10, D_5));

		assertEquals(D_2, BuiltinHelper.divide(D_10, D_5));

		assertEquals(F_2, BuiltinHelper.divide(I_5, F_25));
		assertEquals(F_2, BuiltinHelper.divide(F_5, F_25));
		assertEquals(D_2, BuiltinHelper.divide(D_5, F_25));

		assertEquals(D_2, BuiltinHelper.divide(I_5, D_25));
		assertEquals(D_2, BuiltinHelper.divide(F_5, D_25));
		assertEquals(D_2, BuiltinHelper.divide(D_5, D_25));
	}

	/**
	 * Tests the <code>determineGround</code> method.
	 */
	public void testDetermineGround() {
		assertEquals(Arrays.hashCode(new int[]{1, 3, 5}), 
				Arrays.hashCode(BuiltinHelper.determineGround(
						Arrays.asList(new ITerm[]{A, I_2, B, S_a, C, D_55}))));
		assertEquals(Arrays.hashCode(new int[]{0, 1, 2, 3, 5}), 
				Arrays.hashCode(BuiltinHelper.determineGround(
						Arrays.asList(new ITerm[]{I_2, I_2, F_10, S_a, A, D_55}))));
		assertEquals(Arrays.hashCode(new int[]{}), 
				Arrays.hashCode(BuiltinHelper.determineGround(
						Arrays.asList(new ITerm[]{A, B, C}))));
		assertEquals(Arrays.hashCode(new int[]{0, 1, 2}), 
				Arrays.hashCode(BuiltinHelper.determineGround(
						Arrays.asList(new ITerm[]{I_2, F_5, D_10}))));
		assertEquals(Arrays.hashCode(new int[]{}), 
				Arrays.hashCode(BuiltinHelper.determineGround(
						Arrays.asList(new ITerm[]{}))));
	}

	/**
	 * Tests the <code>determineUnground</code> method.
	 */
	public void testDetermineUnground() {
		assertEquals(Arrays.hashCode(new int[]{0, 2, 4}), 
				Arrays.hashCode(BuiltinHelper.determineUnground(
						Arrays.asList(new ITerm[]{A, I_2, B, S_a, C, D_55}))));
		assertEquals(Arrays.hashCode(new int[]{4}), 
				Arrays.hashCode(BuiltinHelper.determineUnground(
						Arrays.asList(new ITerm[]{I_2, I_2, F_10, S_a, A, D_55}))));
		assertEquals(Arrays.hashCode(new int[]{0, 1, 2}), 
				Arrays.hashCode(BuiltinHelper.determineUnground(
						Arrays.asList(new ITerm[]{A, B, C}))));
		assertEquals(Arrays.hashCode(new int[]{}), 
				Arrays.hashCode(BuiltinHelper.determineUnground(
						Arrays.asList(new ITerm[]{I_2, F_5, D_10}))));
		assertEquals(Arrays.hashCode(new int[]{}), 
				Arrays.hashCode(BuiltinHelper.determineUnground(
						Arrays.asList(new ITerm[]{}))));
	}

	/**
	 * Tests the <code>getIndexes</code> method.
	 */
	public void testGetIndexes() {
		assertEquals(Arrays.hashCode(new ITerm[]{I_5, F_2}), Arrays.hashCode(BuiltinHelper.getIndexes(
						Arrays.asList(new ITerm[]{I_2, I_5, I_10, F_2}), new int[]{1, 3})));
		assertEquals(Arrays.hashCode(new ITerm[]{I_10}), Arrays.hashCode(BuiltinHelper.getIndexes(
						Arrays.asList(new ITerm[]{I_2, I_5, I_10, F_2}), new int[]{2})));
		assertEquals(Arrays.hashCode(new ITerm[]{}), Arrays.hashCode(BuiltinHelper.getIndexes(
						Arrays.asList(new ITerm[]{I_2, I_5, I_10, F_2}), new int[]{})));
	}

	/**
	 * Tests the <code>complement</code> method.
	 */
	public void testComplement() {
		assertEquals(Arrays.hashCode(new int[]{1, 3}), Arrays.hashCode(BuiltinHelper.complement(new int[]{0, 2, 4}, 5)));
		assertEquals(Arrays.hashCode(new int[]{0, 2, 4}), Arrays.hashCode(BuiltinHelper.complement(new int[]{1, 3}, 5)));
		assertEquals(Arrays.hashCode(new int[]{0, 1, 2, 3, 4}), Arrays.hashCode(BuiltinHelper.complement(new int[]{}, 5)));
		assertEquals(Arrays.hashCode(new int[]{}), Arrays.hashCode(BuiltinHelper.complement(new int[]{0, 1, 2, 3, 4}, 5)));
	}

	/**
	 * Tests the <code>concat</code> method.
	 */
	public void testConcat() {
		assertEquals(Arrays.hashCode(new ITerm[]{I_2, I_5, I_10, F_2}), 
				Arrays.hashCode(BuiltinHelper.concat(new int[]{0, 3}, new ITerm[]{I_2, F_2}, new ITerm[]{I_5, I_10})));
		assertEquals(Arrays.hashCode(new ITerm[]{I_2, F_2, I_5, I_10}), 
				Arrays.hashCode(BuiltinHelper.concat(new int[]{0, 1}, new ITerm[]{I_2, F_2}, new ITerm[]{I_5, I_10})));
		assertEquals(Arrays.hashCode(new ITerm[]{I_5, I_10, I_2, F_2}), 
				Arrays.hashCode(BuiltinHelper.concat(new int[]{2, 3}, new ITerm[]{I_2, F_2}, new ITerm[]{I_5, I_10})));
		assertEquals(Arrays.hashCode(new ITerm[]{I_5, I_10}), 
				Arrays.hashCode(BuiltinHelper.concat(new int[]{}, new ITerm[]{}, new ITerm[]{I_5, I_10})));
		assertEquals(Arrays.hashCode(new ITerm[]{I_2, F_2}), 
				Arrays.hashCode(BuiltinHelper.concat(new int[]{0, 1}, new ITerm[]{I_2, F_2}, new ITerm[]{})));
	}

	/**
	 * Tests the subtraction of dates.
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1773182&group_id=167309&atid=842437">bug# 1773182: Add subtraction builtin for dateTime</a>
	 */
	public void testDatesSubtract() {
		final IDuration d1 = CONCRETE.createDuration(0, 0, 1, 0, 0, 0);
		final IDuration h1 = CONCRETE.createDuration(0, 0, 0, 1, 0, 0);
		final IDuration m1 = CONCRETE.createDuration(0, 0, 0, 0, 1, 0);
		final IDuration s1 = CONCRETE.createDuration(0, 0, 0, 0, 0, 1);
		final IDuration h1m1s1 = CONCRETE.createDuration(0, 0, 0, 1, 1, 1);

		final IDateTime y2000m3d5h12m15s10 = CONCRETE.createDateTime(2000, 3, 5, 12, 15, 10);
		final IDateTime y2000m3d4h12m15s10 = CONCRETE.createDateTime(2000, 3, 4, 12, 15, 10);
		assertEquals("something wrong with datetime - duration.", y2000m3d4h12m15s10, 
				BuiltinHelper.subtract(y2000m3d5h12m15s10, d1));
		assertEquals("something wrong with datetime - datetime.", d1, 
				BuiltinHelper.subtract(y2000m3d5h12m15s10, y2000m3d4h12m15s10));

		final IDateTerm y2000m3d5 = CONCRETE.createDate(2000, 3, 5);
		final IDateTerm y2000m3d4 = CONCRETE.createDate(2000, 3, 4);
		assertEquals("something wrong with date - duration.", y2000m3d4, 
				BuiltinHelper.subtract(y2000m3d5, d1));
		assertEquals("something wrong with date - date.", d1, 
				BuiltinHelper.subtract(y2000m3d5, y2000m3d4));

		final ITime h12m15s10 = CONCRETE.createTime(12, 15, 10);
		final ITime h13m16s11 = CONCRETE.createTime(13, 16, 11);
		assertEquals("something wrong with time - duration.", h12m15s10, 
				BuiltinHelper.subtract(h13m16s11, h1m1s1));
		assertEquals("something wrong with time - time.", h1m1s1, 
				BuiltinHelper.subtract(h13m16s11, h12m15s10));

		assertEquals("somethond wrong with duration - duration.", 
				CONCRETE.createDuration(0, 0, 0, 22, 58, 59), BuiltinHelper.subtract(d1, h1m1s1));

		// test taking from the next bigger position
		final IDateTime fromDT = CONCRETE.createDateTime(2000, 1, 1, 0, 0, 0);
		assertEquals("Shifting of the possitions with one second works not properly with datetimes",
				CONCRETE.createDateTime(1999, 12, 31, 23, 59, 59), 
				BuiltinHelper.subtract(fromDT, s1));
		assertEquals("Shifting of the possitions with one minute works not properly with datetimes",
				CONCRETE.createDateTime(1999, 12, 31, 23, 59, 00), 
				BuiltinHelper.subtract(fromDT, m1));
		assertEquals("Shifting of the possitions with one hour works not properly with datetimes",
				CONCRETE.createDateTime(1999, 12, 31, 23, 00, 00), 
				BuiltinHelper.subtract(fromDT, h1));
		assertEquals("Shifting of the possitions with one day works not properly with datetimes", 
				CONCRETE.createDateTime(1999, 12, 31, 00, 00, 00), 
				BuiltinHelper.subtract(fromDT, d1));

		final IDateTerm fromD = CONCRETE.createDate(2000, 1, 1);
		assertEquals("Shifting of the possitions with one day works not properly with dates", 
				CONCRETE.createDate(1999, 12, 31), 
				BuiltinHelper.subtract(fromD, d1));

		final ITime fromT = CONCRETE.createTime(0, 0, 0);
		assertEquals("Shifting of the possitions with one second works not properly with times",
				CONCRETE.createTime(23, 59, 59), BuiltinHelper.subtract(fromT, s1));
		assertEquals("Shifting of the possitions with one minute works not properly with times",
				CONCRETE.createTime(23, 59, 00), BuiltinHelper.subtract(fromT, m1));
		assertEquals("Shifting of the possitions with one hour works not properly with times",
				CONCRETE.createTime(23, 00, 00), BuiltinHelper.subtract(fromT, h1));
	}

	/**
	 * Tests the addition of dates.
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1773182&group_id=167309&atid=842437">bug# 1773182: Add subtraction builtin for dateTime</a>
	 */
	public void testDatesAdd() {
		final IDuration d1 = CONCRETE.createDuration(0, 0, 1, 0, 0, 0);
		final IDuration h1 = CONCRETE.createDuration(0, 0, 0, 1, 0, 0);
		final IDuration m1 = CONCRETE.createDuration(0, 0, 0, 0, 1, 0);
		final IDuration s1 = CONCRETE.createDuration(0, 0, 0, 0, 0, 1);
		final IDuration h1m1s1 = CONCRETE.createDuration(0, 0, 0, 1, 1, 1);

		final IDateTime y2000m3d5h12m15s10 = CONCRETE.createDateTime(2000, 3, 5, 12, 15, 10);
		final IDateTime y2000m3d4h12m15s10 = CONCRETE.createDateTime(2000, 3, 4, 12, 15, 10);
		assertEquals("something wrong with datetime + duration.", y2000m3d5h12m15s10, 
				BuiltinHelper.add(y2000m3d4h12m15s10, d1));
		assertEquals("something wrong with duration + datetime.", y2000m3d5h12m15s10, 
				BuiltinHelper.add(d1, y2000m3d4h12m15s10));

		final IDateTerm y2000m3d5 = CONCRETE.createDate(2000, 3, 5);
		final IDateTerm y2000m3d4 = CONCRETE.createDate(2000, 3, 4);
		assertEquals("something wrong with date + duration.", y2000m3d5, 
				BuiltinHelper.add(y2000m3d4, d1));
		assertEquals("something wrong with duration + date.", y2000m3d5, 
				BuiltinHelper.add(d1, y2000m3d4));

		final ITime h12m15s10 = CONCRETE.createTime(12, 15, 10);
		final ITime h13m16s11 = CONCRETE.createTime(13, 16, 11);
		assertEquals("something wrong with time + duration.", h13m16s11, 
				BuiltinHelper.add(h12m15s10, h1m1s1));
		assertEquals("something wrong with duration + time.", h13m16s11, 
				BuiltinHelper.add(h1m1s1, h12m15s10));

		assertEquals("somethond wrong with duration + duration.", 
				CONCRETE.createDuration(0, 0, 1, 1, 1, 1), BuiltinHelper.add(d1, h1m1s1));

		// test shifting of the next bigger position
		final IDateTime resultDT = CONCRETE.createDateTime(2000, 1, 1, 0, 0, 0);
		assertEquals("Shifting of the possitions with one second works not properly with datetimes",
				resultDT, BuiltinHelper.add(
					CONCRETE.createDateTime(1999, 12, 31, 23, 59, 59), s1));
		assertEquals("Shifting of the possitions with one minute works not properly with datetimes",
				resultDT, BuiltinHelper.add(
					CONCRETE.createDateTime(1999, 12, 31, 23, 59, 00), m1));
		assertEquals("Shifting of the possitions with one hour works not properly with datetimes",
				resultDT, BuiltinHelper.add(
					CONCRETE.createDateTime(1999, 12, 31, 23, 00, 00), h1));
		assertEquals("Shifting of the possitions with one day works not properly with datetimes", 
				resultDT, BuiltinHelper.add(
					CONCRETE.createDateTime(1999, 12, 31, 00, 00, 00), d1));

		final IDateTerm resultD = CONCRETE.createDate(2000, 1, 1);
		assertEquals("Shifting of the possitions with one day works not properly with dates", 
				resultD, BuiltinHelper.add(
					CONCRETE.createDate(1999, 12, 31), d1));

		final ITime resultT = CONCRETE.createTime(0, 0, 0);
		assertEquals("Shifting of the possitions with one second works not properly with times",
				resultT, BuiltinHelper.add(CONCRETE.createTime(23, 59, 59), s1));
		assertEquals("Shifting of the possitions with one minute works not properly with times",
				resultT, BuiltinHelper.add(CONCRETE.createTime(23, 59, 00), m1));
		assertEquals("Shifting of the possitions with one hour works not properly with times",
				resultT, BuiltinHelper.add(CONCRETE.createTime(23, 00, 00), h1));
	}

	/**
	 * Tests some behaviours of the date operations, which don't have to
	 * make sense.
	 */
	public void testWeirdDurationOperations() {
		final IDuration y1 = CONCRETE.createDuration(1, 0, 0, 0, 0, 0);
		final IDuration y1m3 = CONCRETE.createDuration(1, 3, 0, 0, 0, 0);

		final IDateTerm y2004m2d28 = CONCRETE.createDate(2004, 2, 28);
		final IDateTerm y2004m2d29 = CONCRETE.createDate(2004, 2, 29);
		final IDateTerm y2005m2d28 = CONCRETE.createDate(2005, 2, 28);
		final IDateTerm y2005m5d31 = CONCRETE.createDate(2005, 5, 31);

		// add one year to 2004-02-29 -> 2005-02-28
		assertEquals("2004-02-29 + 1 year is wrong", y2005m2d28, 
				BuiltinHelper.add(y2004m2d29, y1));
		// subtract one year from 2005-02-28 -> 2004-02-28
		assertEquals("2005-02-28 - 1 year is wrong", y2004m2d28, 
				BuiltinHelper.subtract(y2005m2d28, y1));

		// add 1year, 3months to 2004-02-29 -> 2005-05-29
		assertEquals("2004-02-29 + 1 year, 3 months is wrong", 
				CONCRETE.createDate(2005, 5, 29), 
				BuiltinHelper.add(y2004m2d29, y1m3));
		// subtract 1year, 3months from 2005-05-31 -> 2004-02-29
		assertEquals("2005-05-31 - 1 year, 3 months is wrong", y2004m2d29, 
				BuiltinHelper.subtract(y2005m5d31, y1m3));

		// 2005-05-31 - 2004-02-28 = 458 days
		assertEquals("2005-05-31 - 2004-02-28 is wrong", 
				CONCRETE.createDuration(0, 0, 458, 0, 0, 0), 
				BuiltinHelper.subtract(y2005m5d31, y2004m2d28));

		// 2005-02-28 - 2004-02-29 -> 365 days
		final IDuration diff = 
			(IDuration) BuiltinHelper.subtract(y2005m2d28, y2004m2d29);
		assertEquals("2005-02-28 - 2004-02-29 is wrong", 
				CONCRETE.createDuration(0, 0, 365, 0, 0, 0), diff);
		// subtract the gained duration from 2005-02-28 -> 2004-02-29
		assertEquals("subtracting the gained duration from 2005-02-28 again is wrong", 
				y2004m2d29, BuiltinHelper.subtract(y2005m2d28, diff));
	}
}
