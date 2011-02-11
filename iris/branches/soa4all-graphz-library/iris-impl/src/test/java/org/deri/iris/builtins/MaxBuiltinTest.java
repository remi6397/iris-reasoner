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


import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;
import junit.framework.TestCase;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * <p>
 * Tests for the {@code MaxBuiltin}.
 * </p>
 */
public class MaxBuiltinTest extends TestCase {

	private final ITerm X = TERM.createVariable("X");
	private final ITerm Y = TERM.createVariable("Y");
	private final ITerm Z = TERM.createVariable("Z");
	private final ITerm T_1 = CONCRETE.createInteger(1);
	private final ITerm T_5 = CONCRETE.createInteger(5);

	public void testEvaluate() throws Exception {

		// max(1,5) = 5
		MaxBuiltin maxBuiltin = new MaxBuiltin(T_1, T_5, T_5);
		ITuple result = maxBuiltin.evaluate( Factory.BASIC.createTuple(T_1, T_5, T_5) );
		assertNotNull( result );

		// max(5,1) = 5
		maxBuiltin = new MaxBuiltin(T_5, T_1, T_5);
		result = maxBuiltin.evaluate( Factory.BASIC.createTuple(T_5, T_1, T_5) );
		assertNotNull( result );

		// max(1,5) != 1 
		maxBuiltin = new MaxBuiltin(T_1, T_5, T_1);
		result = maxBuiltin.evaluate( Factory.BASIC.createTuple(T_1, T_5, T_1) );
		assertNull( result );

		// max(5,1) != 1
		maxBuiltin = new MaxBuiltin(T_5, T_1, T_1);
		result = maxBuiltin.evaluate( Factory.BASIC.createTuple(T_5, T_1, T_1) );
		assertNull( result );

		maxBuiltin = new MaxBuiltin(X, Y, Z);
		result = maxBuiltin.evaluate(Factory.BASIC.createTuple(T_1, T_5, Z));
		assertEquals( Factory.BASIC.createTuple( T_5 ), result );
	}
	
	public void testWrongNumberOfArgument() throws Exception {
		
		try {
			new MaxBuiltin(T_1, T_5);
		}
		catch( IllegalArgumentException e ) {
			// Failed correctly
		}

		try {
			new MaxBuiltin(T_1, T_5, T_1, T_5);
		}
		catch( IllegalArgumentException e ) {
			// Failed correctly
		}
	}
}
