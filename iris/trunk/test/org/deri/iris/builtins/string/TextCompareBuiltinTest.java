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
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * Test for TextCompareBuiltin.
 */
public class TextCompareBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");
	private static final ITerm Y = Factory.TERM.createVariable("Y");

	public TextCompareBuiltinTest(String name) {
		super(name);
	}

	public void testCompare() throws EvaluationException {
		check(0, "foobar@de", "foobar@en");
		check(0, "foobar@de", "foobar@de");
		check(-1, "a@de", "b@en");
		check(1, "b@de", "a@en");
	}

	private void check(int expected, String string1, String string2)
			throws EvaluationException {
		TextCompareBuiltin compare = new TextCompareBuiltin(X, Y);

		assertEquals(Factory.BASIC.createTuple(Factory.CONCRETE
				.createInteger(expected)), compare.evaluate(Factory.BASIC
				.createTuple(Factory.CONCRETE.createText(string1),
						Factory.CONCRETE.createText(string2))));
	}

}
