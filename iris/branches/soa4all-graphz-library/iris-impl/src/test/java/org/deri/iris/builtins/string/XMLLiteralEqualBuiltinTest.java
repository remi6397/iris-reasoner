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

import junit.framework.TestCase;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 */
public class XMLLiteralEqualBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = Factory.TERM.createVariable("Y");
	
	private static final ITuple EMPTY_TUPLE = Factory.BASIC.createTuple();

	public XMLLiteralEqualBuiltinTest(String name) {
		super(name);
	}


	public void testBuiltin() throws EvaluationException {
		
		ITerm term1 = Factory.CONCRETE.createXMLLiteral("XML Literal");
		ITerm term2 = Factory.CONCRETE.createXMLLiteral("XML Literal");
		ITerm term3 = Factory.CONCRETE.createXMLLiteral("blabla");
		
		ITuple arguments = Factory.BASIC.createTuple(X, Y);

		XMLLiteralEqualBuiltin builtin = new XMLLiteralEqualBuiltin(term1 ,term1);
		ITuple actualTuple = builtin.evaluate(arguments);
		assertEquals(EMPTY_TUPLE, actualTuple);
		

		builtin = new XMLLiteralEqualBuiltin(term1, term2);
		actualTuple = builtin.evaluate(arguments);
		assertEquals(EMPTY_TUPLE, actualTuple);

		builtin = new XMLLiteralEqualBuiltin(term1, term3);
		actualTuple = builtin.evaluate(arguments);
		assertEquals(null, actualTuple);
	}

}
