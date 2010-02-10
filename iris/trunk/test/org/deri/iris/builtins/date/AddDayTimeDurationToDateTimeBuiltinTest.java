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
public class AddDayTimeDurationToDateTimeBuiltinTest extends
		AbstractDateBuiltinTest {

	public AddDayTimeDurationToDateTimeBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm dayTimeDuration = Factory.CONCRETE.createDayTimeDuration(true, 3, 2, 3, 45, 23);
		ITerm date1 = Factory.CONCRETE.createDateTime(2010, 4, 26, 1, 12, 12, 12, 1);
		ITerm result = Factory.CONCRETE.createDateTime(1000, 2, 1, 2, 1, 1, 1, 12);

		AddBuiltin builtin = new AddDayTimeDurationToDateTimeBuiltin(date1, dayTimeDuration, result);
		
		System.out.println(date1 + " ; " + dayTimeDuration.getValue() + " ; " + result);
		
		args = Factory.BASIC.createTuple(X, Y, Z);
		actual = builtin.evaluate(args);
		
		System.out.println(date1 + " ; " + dayTimeDuration.getValue() + " ; " + result);
		
		// TODO mp not right ?
		assertEquals(null, actual );
		fail();

	}

}