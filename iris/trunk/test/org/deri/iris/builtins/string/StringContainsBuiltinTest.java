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
package org.deri.iris.builtins.string;

import junit.framework.TestCase;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.string.StringContainsBuiltin;
import org.deri.iris.factory.Factory;

/**
 * Test for StringContainsBuiltin.
 */
public class StringContainsBuiltinTest extends TestCase {

	public static ITerm X = Factory.TERM.createVariable("X");
	public static ITerm Y = Factory.TERM.createVariable("Y");
	public static ITerm Z = Factory.TERM.createVariable("Z");

	public StringContainsBuiltinTest(String name) {
		super(name);
	}

	public void testContains1() throws EvaluationException {
		check(true, "tattoo", "t");
		check(false, "tattoo", "ttt");
		check(false, "", "t");
		check(true, "tattoo", "");
	}

	public void testContains2() throws EvaluationException {
		try {
			check(true, "tattoo", "t", "Minimum Match");
			check(false, "tattoo", "ttt", "Minimum Match");
			check(false, "", "t", "Minimum Match");
			check(true, "tattoo", "", "Minimum Match");
		} catch (IllegalArgumentException iae) {
			fail("Minimal Match collation not supported");
		}
	}

	private void check(boolean expected, String haystack, String needle)
			throws EvaluationException {
		StringContainsBuiltin contains = new StringContainsBuiltin(X, Y);

		ITuple result = contains.evaluate(Factory.BASIC.createTuple(
				Factory.TERM.createString(haystack), Factory.TERM
						.createString(needle)));

		if (expected) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}
	}

	private void check(boolean expected, String haystack, String needle,
			String collation) throws EvaluationException,
			IllegalArgumentException {
		StringContainsBuiltin contains = new StringContainsBuiltin(X, Y);

		ITuple result = contains.evaluate(Factory.BASIC.createTuple(
				Factory.TERM.createString(haystack), Factory.TERM
						.createString(needle), Factory.TERM
						.createString(collation)));

		if (expected) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}
	}

}
