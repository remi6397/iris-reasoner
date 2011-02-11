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
package org.deri.iris.topdown;

import junit.framework.TestCase;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserException;
import org.deri.iris.evaluation.topdown.ILiteralSelector;
import org.deri.iris.evaluation.topdown.SafeStandardLiteralSelector;

/**
 * Test class for SafeStandardLiteralSelector
 * 
 * @author gigi
 * @see SafeStandardLiteralSelector
 */
public class SafeStandardLiteralSelectorTest extends TestCase {

	public void testSinglePositive() throws ParserException {
		String program = "?- p(?X).";
		
		Parser parser = new Parser();
		parser.parse(program);
		
		IQuery query = parser.getQueries().get(0);
		
		ILiteralSelector standardSelector = new SafeStandardLiteralSelector();
		ILiteral selectedLiteral = standardSelector.select(query.getLiterals());
		
		assertEquals("p(?X)", selectedLiteral.toString());
	}
	
	public void testSingleNegative() throws ParserException {
		String program = "?- not p(1).";
		
		Parser parser = new Parser();
		parser.parse(program);
		
		IQuery query = parser.getQueries().get(0);
		
		ILiteralSelector standardSelector = new SafeStandardLiteralSelector();
		ILiteral selectedLiteral = standardSelector.select(query.getLiterals());
		
		assertEquals("!p(1)", selectedLiteral.toString());
	}
	
	public void testSingleNegativeNotPossible() throws ParserException {
		String program = "?- not p(?X).";
		
		Parser parser = new Parser();
		parser.parse(program);
		
		IQuery query = parser.getQueries().get(0);
		
		ILiteralSelector standardSelector = new SafeStandardLiteralSelector();
		ILiteral selectedLiteral = standardSelector.select(query.getLiterals());
		
		assertEquals(null, selectedLiteral);
	}
	
}
