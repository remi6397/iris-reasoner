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

import org.deri.iris.api.builtins.IBuiltinAtom;

/**
 * Tests for the not exact equals built-in.
 */
public class NotExactEqualBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(NotExactEqualBuiltinTest.class, NotExactEqualBuiltinTest.class.getSimpleName());
	}

	public void testEvaluation() throws Exception {
		final IBuiltinAtom xy = BuiltinsFactory.getInstance().createNotExactEqual( TERM.createVariable("X"), TERM.createVariable("Y"));

		assertNull("5 should not be not exactly equal to 5", xy.evaluate(
					BASIC.createTuple(CONCRETE.createInteger(5), CONCRETE.createInteger(5))));

		assertNull("10.0d should not be not exactly equal to 10.0d", xy.evaluate(
						BASIC.createTuple(CONCRETE.createDouble(10), CONCRETE.createDouble(10))));
		
		assertNull("10.0f should not be not exactly equal to 10.0f", xy.evaluate(
						BASIC.createTuple(CONCRETE.createFloat(10), CONCRETE.createFloat(10))));
		
		assertNull("+0.0d should not be not exactly equal to -0.0d", xy.evaluate(
						BASIC.createTuple(CONCRETE.createDouble(+0.0d), CONCRETE.createDouble(-0.0d))));
		
		assertNull("+0.0f should not be not exactly equal to -0.0f", xy.evaluate(
						BASIC.createTuple(CONCRETE.createFloat(+0.0f), CONCRETE.createFloat(-0.0f))));
		
		assertNotNull("5 should be not exactly equal to 5.0", xy.evaluate(
					BASIC.createTuple(CONCRETE.createInteger(5), CONCRETE.createDouble(5))));
		
		assertNotNull("5.0f should be not exactly equal to 5.0d", xy.evaluate(
						BASIC.createTuple(CONCRETE.createFloat(5), CONCRETE.createDouble(5))));
			
		assertNotNull("5 should be not exactly equal to 2", xy.evaluate(
					BASIC.createTuple(CONCRETE.createInteger(2), CONCRETE.createInteger(5))));
		
		assertNotNull("5 should be not exactly equal to a", xy.evaluate(
					BASIC.createTuple(CONCRETE.createInteger(5), TERM.createString("a"))));
	}
	
	public void test_isBuiltin() {
		final IBuiltinAtom xy = BuiltinsFactory.getInstance().createNotExactEqual( TERM.createVariable("X"), TERM.createVariable("Y"));
		assertTrue("buitin predicates should be identifiable as builtins", xy.isBuiltin());
	}
}
