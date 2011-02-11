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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.evaluation.topdown.oldt.MemoTable;
import org.deri.iris.factory.Factory;
import org.deri.iris.storage.IRelation;
import org.deri.iris.storage.simple.SimpleRelationFactory;

public class MemoTableTest extends TestCase {

	private static SimpleRelationFactory srf = new SimpleRelationFactory();
	private static ITuple emptyTuple = Factory.BASIC.createTuple();
	private static ITuple a = Factory.BASIC.createTuple(Factory.TERM.createString("a"));
	private static ITuple b = Factory.BASIC.createTuple(Factory.TERM.createString("b"));
	
	
	public void testMemoTableAddEqualAtoms() {

		MemoTable table = new MemoTable();
		// p(?X)
		IAtom a1 = Factory.BASIC.createAtom(
				Factory.BASIC.createPredicate("p", 1),
				Factory.BASIC.createTuple(
						Factory.TERM
						.createVariable("X")));
		// p(?Y)
		IAtom a2 = Factory.BASIC.createAtom(
				Factory.BASIC.createPredicate("p", 1),
				Factory.BASIC.createTuple(
						Factory.TERM
						.createVariable("Y")));

		// p(1)
		IAtom a3 = Factory.BASIC.createAtom(
				Factory.BASIC.createPredicate("p", 1),
				Factory.BASIC.createTuple(
						Factory.TERM
						.createString("1")));
		
		table.add(a1, emptyTuple);
		table.add(a2, emptyTuple);
		table.add(a3, emptyTuple);
		
		Map<IAtom, IRelation> expectedHashMap = new HashMap<IAtom, IRelation>();
		IRelation emptyRelation = srf.createRelation();
		emptyRelation.add(emptyTuple);
		expectedHashMap.put(a1, emptyRelation);
		expectedHashMap.put(a3, emptyRelation);
		
		assertEquals(expectedHashMap.toString(), table.toString());
	}
	
	public void testMemoTableAddIdenticalAtoms() {

		MemoTable table = new MemoTable();
		// p(?X)
		IAtom px = Factory.BASIC.createAtom(
				Factory.BASIC.createPredicate("p", 1),
				Factory.BASIC.createTuple(
						Factory.TERM
						.createVariable("X")));
		// p(?X)
		IAtom px2 = Factory.BASIC.createAtom(
				Factory.BASIC.createPredicate("p", 1),
				Factory.BASIC.createTuple(
						Factory.TERM
						.createVariable("X")));
		
		table.add(px, null); // Initialize memo table (empty answer list)
		table.add(px2, a); // add tuple ('a') to the answer list
		
		Map<IAtom, IRelation> expectedHashMap = new HashMap<IAtom, IRelation>();
		IRelation rel = srf.createRelation();
		rel.add(a);
		expectedHashMap.put(px, rel);
		
		assertEquals(expectedHashMap.toString(), table.toString());
	}
	
	public void testMemoTableAddEquivalentAtoms() {

		MemoTable table = new MemoTable();
		// p(?X)
		IAtom px = Factory.BASIC.createAtom(
				Factory.BASIC.createPredicate("p", 1),
				Factory.BASIC.createTuple(
						Factory.TERM
						.createVariable("X")));
		// p(?A)
		IAtom pa = Factory.BASIC.createAtom(
				Factory.BASIC.createPredicate("p", 1),
				Factory.BASIC.createTuple(
						Factory.TERM
						.createVariable("A")));
		
		table.add(px, null); // Initialize memo table (empty answer list)
		table.add(pa, a); // add tuple ('a') to the answer list
		
		Map<IAtom, IRelation> expectedHashMap = new HashMap<IAtom, IRelation>();
		IRelation rel = srf.createRelation();
		rel.add(a);
		expectedHashMap.put(px, rel);
		
		assertEquals(expectedHashMap.toString(), table.toString());
	}
	
	public void testMemoTableAddMultipleAnswers() {

		MemoTable table = new MemoTable();
		// p(?X)
		IAtom px = Factory.BASIC.createAtom(
				Factory.BASIC.createPredicate("p", 1),
				Factory.BASIC.createTuple(
						Factory.TERM
						.createVariable("X")));
		
		table.add(px, null); // Initialize memo table (empty answer list)
		table.add(px, a); // add tuple ('a') to the answer list
		table.add(px, a); // add tuple ('a') to the answer list a second time (should be ignored)
		table.add(px, b); // add tuple ('b') to the answer list
		
		
		Map<IAtom, IRelation> expectedHashMap = new HashMap<IAtom, IRelation>();
		IRelation rel = srf.createRelation();
		rel.add(a);
		rel.add(b);
		expectedHashMap.put(px, rel);
		
		assertEquals(expectedHashMap.toString(), table.toString());
	}

}
