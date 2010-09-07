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
package org.deri.iris.builtins.list;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.concrete.IList;

public class IsListBuiltinTest extends AbstractListBuiltinTest {

	private IsListBuiltin builtin;

	private IList list_1, list_2;

	public void testBuiltin() throws EvaluationException {
		try {
			builtin = new IsListBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if builtin has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}
		builtin = new IsListBuiltin(EMPTY_LIST);
		assertEquals(true, builtin.computeResult(EMPTY_LIST));
		list_1 = new org.deri.iris.terms.concrete.List();
		list_2 = new org.deri.iris.terms.concrete.List();

		// External(pred:is-list(1)) will evaluate to f in any interpretation.
		assertEquals(false, builtin.computeResult(ONE));
		assertEquals(false, builtin.computeResult(ONE, list_1));

		list_1.add(ONE);
		assertFalse(list_1.isEmpty());
		assertEquals(true, builtin.computeResult(list_1));

		list_2.add(ONE);
		list_2.add(ONE);
		list_2.add(TWO);
		list_2.add(list_1);

		assertEquals(true, builtin.computeResult(list_2));
		assertFalse(list_2.equals(list_1));
		assertEquals(builtin.computeResult(list_2), builtin
				.computeResult(list_1));

	}
}
