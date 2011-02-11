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

import org.deri.iris.api.basics.ITuple;

/**
 * <p>
 * Tests for the unequals builtin.
 * </p>
 * <p>
 * $Id: UnequalBuiltinTest.java,v 1.2 2007-05-10 07:01:18 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.2 $
 */
public class UnequalBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(UnequalBuiltinTest.class, UnequalBuiltinTest.class.getSimpleName());
	}

	public void testEvaluation() throws Exception {
		final ITuple x2 = BASIC.createTuple(TERM.createVariable("X"), TERM.createVariable("X"));
		assertNull("5 shouldn't be unequal to 5", (new NotEqualBuiltin(CONCRETE
				.createInteger(5), CONCRETE.createInteger(5))).evaluate(x2));
		assertNull("5 shouldn't be unequal to 5.0", (new NotEqualBuiltin(CONCRETE
				.createInteger(5), CONCRETE.createDouble(5d))).evaluate(x2));
		assertNotNull("5 should be unequal to 2", (new NotEqualBuiltin(CONCRETE
				.createInteger(2), CONCRETE.createInteger(5))).evaluate(x2));
		assertNotNull("5 should be unequal to a", (new NotEqualBuiltin(CONCRETE
				.createInteger(5), TERM.createString("a"))).evaluate(x2));
	}
}
