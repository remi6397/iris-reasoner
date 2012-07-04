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
import at.sti2.streamingiris.api.builtins.IBuiltinAtom;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.string.StringMatchesWithoutFlagsBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringMatchesBuiltin.
 */
public class StringMatchesBuiltinTest extends TestCase {

	public static ITerm X = Factory.TERM.createVariable("X");

	public static ITerm Y = Factory.TERM.createVariable("Y");

	public static ITerm Z = Factory.TERM.createVariable("Z");

	public StringMatchesBuiltinTest(String name) {
		super(name);
	}

	public void testMatches() throws EvaluationException {
		check(true, "abracadabra", "bra");
		check(true, "abracadabra", "^a.*a$");
		check(false, "abracadabra", "^bra");
	}

	private void check(boolean expected, String string, String pattern)
			throws EvaluationException {
		IBuiltinAtom matches = new StringMatchesWithoutFlagsBuiltin(X, Y);

		ITuple result = matches.evaluate(Factory.BASIC.createTuple(Factory.TERM
				.createString(string), Factory.TERM.createString(pattern)));

		if (expected) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}
	}

}
