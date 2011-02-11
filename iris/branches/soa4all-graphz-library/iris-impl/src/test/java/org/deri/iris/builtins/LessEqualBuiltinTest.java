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

/**
 * <p>
 * Tests for the less equal builtin.
 * </p>
 * <p>
 * $Id: LessEqualBuiltinTest.java,v 1.4 2007-10-12 12:51:54 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.4 $
 */
public class LessEqualBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(LessEqualBuiltinTest.class,
				LessEqualBuiltinTest.class.getSimpleName());
	}

	public void testEvaluation() throws Exception{
		final LessEqualBuiltin xy = new LessEqualBuiltin(TERM.createVariable("X"), TERM.createVariable("Y"));

		assertNotNull("5 should be less-equal to 5", xy.evaluate(
					BASIC.createTuple(CONCRETE.createInteger(5), CONCRETE.createInteger(5))));
		assertNotNull("5 should be less-equal to 5.0", xy.evaluate(
					BASIC.createTuple(CONCRETE.createInteger(5), CONCRETE.createDouble(5d))));

		assertNotNull("2 should be less than 5.0", xy.evaluate(
					BASIC.createTuple(CONCRETE.createInteger(2), CONCRETE.createDouble(5d))));
		assertNull("5 shouldn't be less than 2", xy.evaluate(
					BASIC.createTuple(CONCRETE.createInteger(5), CONCRETE.createInteger(2))));

		assertNotNull("a should be less than b", xy.evaluate(
					BASIC.createTuple(TERM.createString("a"), TERM.createString("b"))));
		assertNotNull("a should be less-equal to a", xy.evaluate(
					BASIC.createTuple(TERM.createString("a"), TERM.createString("a"))));

		assertEquals(null, xy.evaluate(BASIC.createTuple(CONCRETE.createInteger(5), TERM.createString("a"))) );
	}
}
