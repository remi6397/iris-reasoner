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
package org.deri.iris.topdown.oldt;

import java.util.Map;

import junit.framework.TestCase;

import org.deri.iris.RuleUnsafeException;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserException;
import org.deri.iris.evaluation.topdown.TopDownHelper;

/**
 * Test class for TopDownHelper.getVariableMapForVariableRenaming(rule, query)
 * which is used by SLDNF and OLDT evaluation strategies.
 * 
 * @author gigi
 * @see TopDownHelper.getVariableMapForVariableRenaming(IRule rule, IQuery query)
 */
public class VariableRenamingTest extends TestCase {

	/**
	 * Tests if variables get substituted correctly when the same variable appears
	 * in the rule head and the current evaluation body.
	 * 
	 * @throws ParserException on failure
	 * @throws RuleUnsafeException when the program contains unsafe rules
	 */
	public void testVariableClash() throws ParserException, RuleUnsafeException {
		
		String program = 
			"p(?x, ?y) :- ?x > ?y." +
			"?- 2 + ?y = ?x, p(2, ?x).";
		
		Parser parser = new Parser();
		parser.parse(program);
		IQuery query = parser.getQueries().get(0);
		IRule rule = parser.getRules().get(0);
		
		Map<IVariable, ITerm> map = TopDownHelper.getVariableMapForVariableRenaming(rule, query);
		
		System.out.println(map);
		assertEquals(false, map.isEmpty());
	}
	
}
