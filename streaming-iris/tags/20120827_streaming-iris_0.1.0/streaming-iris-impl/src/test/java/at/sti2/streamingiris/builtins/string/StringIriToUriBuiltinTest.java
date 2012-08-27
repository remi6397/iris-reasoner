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
package at.sti2.streamingiris.builtins.string;

import junit.framework.TestCase;


import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.string.StringIriToUriBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringIriToUriBuiltin.
 */
public class StringIriToUriBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = Factory.TERM.createVariable("Y");

	public StringIriToUriBuiltinTest(String name) {
		super(name);
	}

	public void testConversion() throws EvaluationException {
		check("http://www.example.com/00/Weather/CA/Los%20Angeles#ocean",
				"http://www.example.com/00/Weather/CA/Los%20Angeles#ocean");
		check("http://www.example.com/~b%C3%A9b%C3%A9",
				"http://www.example.com/~bébé");
	}

	private void check(String expected, String actual)
			throws EvaluationException {
		IStringTerm actualTerm = Factory.TERM.createString(actual);
		ITuple arguments = Factory.BASIC.createTuple(X, Y);

		StringIriToUriBuiltin builtin = new StringIriToUriBuiltin(actualTerm, Y);

		IStringTerm expectedTerm = Factory.TERM.createString(expected);
		ITuple expectedTuple = Factory.BASIC.createTuple(expectedTerm);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
