/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package at.sti2.streamingiris.topdown.oldt;

import java.util.Map;

import junit.framework.TestCase;


import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IAtom;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.compiler.ParserException;
import at.sti2.streamingiris.evaluation.topdown.oldt.MemoTable;
import at.sti2.streamingiris.evaluation.topdown.oldt.OLDTEvaluator;
import at.sti2.streamingiris.factory.Factory;
import at.sti2.streamingiris.facts.Facts;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.storage.IRelation;
import at.sti2.streamingiris.storage.IRelationFactory;
import at.sti2.streamingiris.storage.simple.SimpleRelationFactory;

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
