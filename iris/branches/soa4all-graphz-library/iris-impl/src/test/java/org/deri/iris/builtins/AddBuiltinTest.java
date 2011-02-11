/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.builtins;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * Tests for the {@code AddBuiltin}.
 * </p>
 * <p>
 * $Id: AddBuiltinTest.java,v 1.4 2007-05-10 09:02:29 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.4 $
 */
public class AddBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(AddBuiltinTest.class, AddBuiltinTest.class
				.getSimpleName());
	}

	public void testEvaluate() throws Exception {
		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final ITerm Z = TERM.createVariable("Z");
		final ITerm T_1 = CONCRETE.createInteger(1);
		final ITerm T_5 = CONCRETE.createInteger(5);
		final ITerm T_6 = CONCRETE.createInteger(6);

		// X + 5 = 6
		final AddBuiltin b_x56 = new AddBuiltin(X, T_5, T_6);
		assertEquals(BASIC.createTuple(T_1), b_x56.evaluate(BASIC.createTuple(X, X, X)));
		// 1 + X = 6
		final AddBuiltin b_1x6 = new AddBuiltin(T_1, X, T_6);
		assertEquals(BASIC.createTuple(T_5), b_1x6.evaluate(BASIC.createTuple(X, X, X)));
		// 1 + 5 = X
		final AddBuiltin b_15x = new AddBuiltin(T_1, T_5, X);
		assertEquals(BASIC.createTuple(T_6), b_15x.evaluate(BASIC.createTuple(X, X, X)));
		// 1 + X = Y
		final AddBuiltin b_1xy = new AddBuiltin(T_1, X, Y);
		assertEquals(BASIC.createTuple(T_5), b_1xy.evaluate(BASIC.createTuple(X, X, T_6)));
		assertEquals(BASIC.createTuple(T_6), b_1xy.evaluate(BASIC.createTuple(X, T_5, X)));
		// X + 5 = Y
		final AddBuiltin b_x5y = new AddBuiltin(X, T_5, Y);
		assertEquals(BASIC.createTuple(T_1), b_x5y.evaluate(BASIC.createTuple(X, X, T_6)));
		assertEquals(BASIC.createTuple(T_6), b_x5y.evaluate(BASIC.createTuple(T_1, X, X)));
		// X + Y = 6
		final AddBuiltin b_xy6 = new AddBuiltin(X, Y, T_6);
		assertEquals(BASIC.createTuple(T_1), b_xy6.evaluate(BASIC.createTuple(X, T_5, X)));
		assertEquals(BASIC.createTuple(T_5), b_xy6.evaluate(BASIC.createTuple(T_1, X, X)));
		// X + Y = Z
		final AddBuiltin b_xyz = new AddBuiltin(X, Y, Z);
		assertEquals(BASIC.createTuple(T_1), b_xyz.evaluate(BASIC.createTuple(X, T_5, T_6)));
		assertEquals(BASIC.createTuple(T_5), b_xyz.evaluate(BASIC.createTuple(T_1, X, T_6)));
		assertEquals(BASIC.createTuple(T_6), b_xyz.evaluate(BASIC.createTuple(T_1, T_5, X)));

		// test the checking for correctness
		assertNotNull(b_x56.evaluate(BASIC.createTuple(T_1, T_5, T_6)));
		assertNotNull(b_1x6.evaluate(BASIC.createTuple(T_1, T_5, T_6)));
		assertNotNull(b_15x.evaluate(BASIC.createTuple(T_1, T_5, T_6)));
		assertNotNull(b_1xy.evaluate(BASIC.createTuple(T_1, T_5, T_6)));
		assertNotNull(b_x5y.evaluate(BASIC.createTuple(T_1, T_5, T_6)));
		assertNotNull(b_xy6.evaluate(BASIC.createTuple(T_1, T_5, T_6)));
		assertNotNull(b_xyz.evaluate(BASIC.createTuple(T_1, T_5, T_6)));

		assertNull(b_x56.evaluate(BASIC.createTuple(T_5, T_6, T_1)));
		assertNull(b_1x6.evaluate(BASIC.createTuple(T_5, T_6, T_1)));
		assertNull(b_15x.evaluate(BASIC.createTuple(T_5, T_6, T_1)));
		assertNull(b_1xy.evaluate(BASIC.createTuple(T_5, T_6, T_1)));
		assertNull(b_x5y.evaluate(BASIC.createTuple(T_5, T_6, T_1)));
		assertNull(b_xy6.evaluate(BASIC.createTuple(T_5, T_6, T_1)));
		assertNull(b_xyz.evaluate(BASIC.createTuple(T_5, T_6, T_1)));
	}
}
