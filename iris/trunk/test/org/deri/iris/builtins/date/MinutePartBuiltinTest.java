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

import junit.framework.TestCase;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * Test for MinutePartBuiltin.
 */
public class MinutePartBuiltinTest extends TestCase {

	private static ITerm X = Factory.TERM.createVariable("X");

	public MinutePartBuiltinTest(String name) {
		super(name);
	}

	public void testMinutesFromTime() throws EvaluationException {
		ITerm time = Factory.CONCRETE.createTime(8, 55, 23.5, 0, 0);
		check(55, time);
	}
	
	public void testMinutesFromDateTime() throws EvaluationException {
		ITerm dateTime = Factory.CONCRETE.createDateTime(1999, 5, 31, 13, 20,
				0, 0, 0);
		check(20, dateTime);
	}

	public void testMinutesFromDuration() throws EvaluationException {
		ITerm duration = Factory.CONCRETE.createDuration(true, 0, 0, 3, 10, 0,
				0);
		check(0, duration);

		duration = Factory.CONCRETE.createDuration(false, 0, 0, 5, 12, 30, 0);
		check(-30, duration);
	}

	private void check(int expected, ITerm time) throws EvaluationException {
		MinutePartBuiltin builtin = new MinutePartBuiltin(X);

		ITuple arguments = Factory.BASIC.createTuple(time);
		ITuple expectedTuple = Factory.BASIC.createTuple(Factory.CONCRETE
				.createInteger(expected));
		ITuple actual = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actual);
	}
}
