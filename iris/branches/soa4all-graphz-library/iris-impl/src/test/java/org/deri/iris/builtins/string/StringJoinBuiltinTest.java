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
package org.deri.iris.builtins.string;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;

/**
 * Test for StringJoinBuiltin.
 */
public class StringJoinBuiltinTest extends TestCase {

	private static final IVariable X = Factory.TERM.createVariable("X");

	private static final IVariable Y = Factory.TERM.createVariable("Y");

	private static final IVariable Z = Factory.TERM.createVariable("Z");

	private static final IVariable R = Factory.TERM.createVariable("R");

	public StringJoinBuiltinTest(String name) {
		super(name);
	}

	public void testJoin() throws EvaluationException {
		check("foo,bar", "foo", "bar", ",");
		check("a/*/*c", "a", "", "c", "/*");
	}

	private void check(String expected, String... actual)
			throws EvaluationException {
		ITerm[] terms = new ITerm[actual.length + 1];
		for (int i = 0; i < actual.length; i++) {
			terms[i] = Factory.TERM.createString(actual[i]);
		}
		terms[actual.length] = R;
		
		List<IVariable> vars = new ArrayList<IVariable>();
		for (int i = 0; i < actual.length; i++) {
			vars.add(Factory.TERM.createVariable("var" + i));
		}
		vars.add(R);
		
		ITuple arguments = Factory.BASIC.createTuple(vars.toArray(new ITerm[] {}));

		StringJoinBuiltin length = new StringJoinBuiltin(terms);

		IStringTerm expectedTerm = Factory.TERM.createString(expected);
		ITuple expectedTuple = Factory.BASIC.createTuple(expectedTerm);

		ITuple actualTuple = length.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
