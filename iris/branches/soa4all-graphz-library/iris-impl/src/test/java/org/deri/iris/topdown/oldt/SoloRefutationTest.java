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

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserException;
import org.deri.iris.evaluation.topdown.oldt.MemoTable;
import org.deri.iris.evaluation.topdown.oldt.OLDTEvaluator;
import org.deri.iris.factory.Factory;
import org.deri.iris.facts.Facts;
import org.deri.iris.facts.IFacts;
import org.deri.iris.storage.IRelation;
import org.deri.iris.storage.IRelationFactory;
import org.deri.iris.storage.simple.SimpleRelationFactory;

/**
 * Test class to ensure that the solo refutation works correctly.
 * 
 * <p>Solo refutation is used to update the memo table while evaluating. This
 * pushes correctly derived tuples into the memo table without the need
 * of successful derivation of the whole subgoal.</p> 
 * 
 * @author gigi
 * 
 */
public class SoloRefutationTest extends TestCase {

	public void testSoloRefutationAllFailureNodes() throws ParserException, EvaluationException {

		String program = 
			"p(?X) :- q(?X, ?A), r(?A)." +
			"p(?X) :- p(?X), v(?X)." +
			"q(3,5). r(5). p(1)." +
			"?-p(?X), b(?X, ?Y).";
		
		Parser parser = new Parser();
		parser.parse(program);
		
		Map<IPredicate, IRelation> factsMap = parser.getFacts();
		IRelationFactory rf = new SimpleRelationFactory();
		IFacts facts = new Facts( factsMap, rf ); 
		OLDTEvaluator evaluator = new OLDTEvaluator( facts, parser.getRules() );
		
		evaluator.evaluate( parser.getQueries().get(0) );
		
		MemoTable memoTable = evaluator.getMemoTable();
		
		// p(?X)
		IAtom atom = Factory.BASIC.createAtom(
						Factory.BASIC.createPredicate("p", 1), 
						Factory.BASIC.createTuple(
							Factory.TERM.createVariable("X")
						)
					 );
		
		assertEquals(1, memoTable.size());
		
		ITuple firstEntry = memoTable.get(atom , 0);
		ITuple secondEntry = memoTable.get(atom , 1);
		
		assertNotNull(firstEntry);
		assertNotNull(secondEntry);
		
		assertEquals("(1)", firstEntry.toString());
		assertEquals("(3)", secondEntry.toString());
	}
	
}
