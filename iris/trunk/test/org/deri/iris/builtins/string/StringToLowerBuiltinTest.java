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
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.string.StringToLowerBuiltin;
import org.deri.iris.factory.Factory;

/**
 * Test for StringToLowerBuiltin.
 */
public class StringToLowerBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	public StringToLowerBuiltinTest(String name) {
		super(name);
	}

	public void testLowerCase() throws EvaluationException {
		check("foobar", "FOOBAR");
		check("f00b4r", "F00B4R");
	}

	private void check(String expected, String actual)
			throws EvaluationException {
		StringToLowerBuiltin length = new StringToLowerBuiltin(X);

		IStringTerm actualTerm = Factory.TERM.createString(actual);
		ITuple arguments = Factory.BASIC.createTuple(actualTerm);

		IStringTerm expectedTerm = Factory.TERM.createString(expected);
		ITuple expectedTuple = Factory.BASIC.createTuple(expectedTerm);

		ITuple actualTuple = length.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
