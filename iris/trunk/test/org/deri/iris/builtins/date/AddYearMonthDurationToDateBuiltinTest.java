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
import org.deri.iris.builtins.AddBuiltin;
import org.deri.iris.factory.Factory;

/**
 */
public class AddYearMonthDurationToDateBuiltinTest extends
		AbstractDateBuiltinTest {

	public AddYearMonthDurationToDateBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm date0 = Factory.CONCRETE.createYearMonthDuration(true, 1976,2);
		ITerm date1 = Factory.CONCRETE.createDate(10, 4, 2);
		ITerm result = Factory.CONCRETE.createDate(2, 2, 1);

		AddBuiltin builtin = new AddYearMonthDurationToDateBuiltin(date1, date0, result);

		System.out.println(date0.getValue() + " ; " + date1 + " ; " + result);

		args = Factory.BASIC.createTuple(X, Y, Z);
		actual = builtin.evaluate(args);

		System.out.println(date0.getValue() + " ; " + date1 + " ; " + result);
		
		// TODO mp not right ?
		assertEquals(null, actual);
		fail();
	}
}