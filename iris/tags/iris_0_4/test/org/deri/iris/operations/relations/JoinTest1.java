/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
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
package org.deri.iris.operations.relations;

import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.RELATION_OPERATION;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.MiscHelper;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage.IMixedDatatypeRelation;

/**
 * NOTE: Currently only EQUAL comparison operator (equal join) is supported!
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 21.09.2006 12:11:43
 */
public class JoinTest1 extends TestCase {

	public static Test suite() {
		return new TestSuite(JoinTest1.class, JoinTest1.class.getSimpleName());
	}

	public void testJoinAndJoinSimple_m10() {
		IMixedDatatypeRelation relation0 = RELATION.getMixedRelation(2);
		
		// relation0: add tuples
		relation0.add(MiscHelper.createTuple("a", "b"));
		relation0.add(MiscHelper.createTuple("b", "b"));
		relation0.add(MiscHelper.createTuple("b", "d"));
				
		IJoin joinOperator = RELATION_OPERATION.createSortMergeJoinOperator(
				relation0, relation0, new int[] { -1, 0}, JoinCondition.EQUALS);
		
		IJoin joinSimpleOperator = RELATION_OPERATION.createSortMergeJoinOperator(
				relation0, relation0, new int[] { -1, 0}, JoinCondition.EQUALS);
		
		IMixedDatatypeRelation result0 = joinOperator.join();
		IMixedDatatypeRelation result1 = joinSimpleOperator.join();
		
		Assert.assertEquals("Join and JoinSimple should produce the same result"
						+ ", when two copies of the same relatation are provided as join parameters", 
								result0.size(), result1.size());
	}
	
	/**
	 * Joins two relations (no duplicates handling) and then checks the 
	 * result against the submitted Collection of tuples. 
	 * 
	 * @param i indexes which join will be based on (see documentation for
	 *          IRelationOperationsFactory.createJoinOperator 
	 *          for the computation of this array)
	 * @param e collection of expected tuples
	 */
	protected static void runJoinSimple(final int[] i, final Collection<ITuple> e) {
		IMixedDatatypeRelation relation0 = RELATION.getMixedRelation(2);
		
		// relation0: add tuples
		relation0.add(MiscHelper.createTuple("a", "b"));
		relation0.add(MiscHelper.createTuple("b", "b"));
		relation0.add(MiscHelper.createTuple("b", "d"));
				
		IJoin joinSimpleOperator = RELATION_OPERATION.createSortMergeJoinOperator(
				relation0, relation0, i, JoinCondition.EQUALS);
		
		IMixedDatatypeRelation result = joinSimpleOperator.join();
		
		assertResults(result, e);
	}

	public void testJoinSimple_m10() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "b", "d"));
		e.add(MiscHelper.createTuple("b", "b", "b", "d"));
		e.add(MiscHelper.createTuple("b", "b", "b", "b"));
		
		runJoinSimple(new int[] { -1, 0}, e);
	}
	
	public void testJoinSimple_m1m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "a", "b"));
		e.add(MiscHelper.createTuple("a", "b", "b", "b"));
		e.add(MiscHelper.createTuple("b", "b", "b", "d"));
		
		e.add(MiscHelper.createTuple("b", "b", "a", "b"));
		e.add(MiscHelper.createTuple("b", "b", "b", "b"));
		e.add(MiscHelper.createTuple("b", "b", "b", "d"));
		
		e.add(MiscHelper.createTuple("b", "d", "a", "b"));
		e.add(MiscHelper.createTuple("b", "d", "b", "b"));
		e.add(MiscHelper.createTuple("b", "d", "b", "d"));
		
		runJoinSimple(new int[] { -1, -1}, e);
	}
	
	/**
	 * Tests the relation against a list of tuples using the assert methods of
	 * JUnit. The length of the relation and the list must be equal, and the
	 * relation must contain all tuples of the list.
	 * 
	 * @param r
	 *            the relation to check
	 * @param e
	 *            the Collection containing all expected tuples
	 */
	protected static void assertResults(final IMixedDatatypeRelation r,
			final Collection<ITuple> e) {
		Assert.assertEquals("The length of relation and the list of"
				+ " expected tuples must be equal", e.size(), r.size());
		Assert.assertTrue("The relation must contain all expected tuples", r
				.containsAll(e));
	}
}

