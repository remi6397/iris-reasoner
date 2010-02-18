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
package org.deri.iris.builtins.date;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 */
public class DateSubtractBuiltinTest extends AbstractDateBuiltinTest {

	public DateSubtractBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm date1 = Factory.CONCRETE.createDate(2011, 5, 27);
		ITerm date2 = Factory.CONCRETE.createDate(2010, 4, 1);
		ITerm result = Factory.CONCRETE.createDate(2010, 4, 1);

		DateSubtractBuiltin builtin = new DateSubtractBuiltin(X, Y, Z);

		args = Factory.BASIC.createTuple(date1, date2, result);
		actual = builtin.evaluate(args);
		expected = Factory.BASIC.createTuple(Factory.CONCRETE.createDuration(
				true, 1, 1, 26, 0, 0, 0, 0));

		
		
		assertEquals(expected, result);
		assertEquals(null, actual);

		// builtin = new DateSubtractBuiltin(date2, date3, Y);
		// args = Factory.BASIC.createTuple(X, X, X);
		// expected =
		// Factory.BASIC.createTuple(Factory.CONCRETE.createDuration(true, 0, 0,
		// 0, 0, 0, 0, 0));
		// actual = builtin.evaluate(args);
		//		
		// assertEquals(expected, actual );
		//		
		// builtin = new DateSubtractBuiltin(date3, date1, Y);
		// args = Factory.BASIC.createTuple(X, Y, Z);
		// expected =
		// Factory.BASIC.createTuple(Factory.CONCRETE.createDuration(false, 1,
		// 1, 26, 0, 0, 0, 0));
		// actual = builtin.evaluate(args);

		assertEquals(expected, actual);
	}

}
