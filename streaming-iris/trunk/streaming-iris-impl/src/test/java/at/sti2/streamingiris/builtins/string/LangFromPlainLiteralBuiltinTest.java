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

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;


import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.string.LangFromPlainLiteralBuiltin;

/**
 * Test for LangFromPlainLiteralBuiltin.
 */
public class LangFromPlainLiteralBuiltinTest extends TestCase {

	private static final ITerm X = TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public LangFromPlainLiteralBuiltinTest(String name) {
		super(name);
	}

	public void testEvaluation() throws EvaluationException {
		check("de", "foobar", "de");
	}

	private void check(String expected, String text, String lang)
			throws EvaluationException {
		ITerm expectedTerm = TERM.createString(expected);
		ITuple expectedTuple = BASIC.createTuple(expectedTerm);

		ITerm textTerm = CONCRETE.createPlainLiteral(text, lang);
		ITuple arguments = BASIC.createTuple(X, Y);

		LangFromPlainLiteralBuiltin builtin = new LangFromPlainLiteralBuiltin(
				textTerm, X);
		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
