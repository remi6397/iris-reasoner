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
public class JoinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(JoinTest.class, JoinTest.class.getSimpleName());
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
	protected static void runJoin(final int[] i, final Collection<ITuple> e) {
		IMixedDatatypeRelation relation0 = RELATION.getMixedRelation(3);
		IMixedDatatypeRelation relation1 = RELATION.getMixedRelation(4);

		// relation0: add tuples
		relation0.add(MiscHelper.createTuple("a", "b", "b"));
		relation0.add(MiscHelper.createTuple("a", "b", "d"));
		relation0.add(MiscHelper.createTuple("e", "e", "e"));
		relation0.add(MiscHelper.createTuple("h", "h", "h"));
		relation0.add(MiscHelper.createTuple("h", "g", "h"));
		
		relation0.add(MiscHelper.createTuple("a", "b", "i")); // Duplicate!
		relation0.add(MiscHelper.createTuple("a", "b", "i"));
		relation0.add(MiscHelper.createTuple("f", "g", "k"));
		relation0.add(MiscHelper.createTuple("x", "x", "x"));
		
		
		// relation1: add tuples
		relation1.add(MiscHelper.createTuple("c", "b", "b", "x"));
		relation1.add(MiscHelper.createTuple("f", "b", "a", "x"));
		
		relation1.add(MiscHelper.createTuple("e", "b", "b", "x"));
		relation1.add(MiscHelper.createTuple("h", "a", "a", "a"));
		relation1.add(MiscHelper.createTuple("h", "a", "a", "b"));

		relation1.add(MiscHelper.createTuple("j", "b", "b", "x"));
		relation1.add(MiscHelper.createTuple("l", "b", "a", "x"));
		relation1.add(MiscHelper.createTuple("x", "x", "x", "x"));
		relation1.add(MiscHelper.createTuple("x", "e", "x", "x"));
		
		IJoin joinOperator = RELATION_OPERATION.createSortMergeJoinOperator(
				relation0, relation1, i, JoinCondition.EQUALS);
		
		IMixedDatatypeRelation result = joinOperator.join();
		assertResults(result, e);
	}

	/**
	 * @param i  indexes on which to join (see documentation for
	 *           IRelationOperationsFactory.createJoinOperator
	 *           for the computation of this array)
	 * @param pi project indexes
	 * @param e  collection of expected tuples
	 */
	/*protected static void runJoinWithProjection(final int[] i, final int[] pi, final Collection<ITuple> e) {
		IMixedDatatypeRelation relation0 = RELATION.getMixedRelation(3);
		IMixedDatatypeRelation relation1 = RELATION.getMixedRelation(4);

		// relation0: add tuples
		relation0.add(MiscHelper.createTuple("a", "b", "b"));
		relation0.add(MiscHelper.createTuple("a", "b", "d"));
		
		// relation1: add tuples
		relation1.add(MiscHelper.createTuple("c", "b", "b", "x"));
		relation1.add(MiscHelper.createTuple("f", "b", "a", "x"));
		
		IJoin joinOperator = RELATION_OPERATION.createSortMergeJoinOperator(
				relation0, relation1,
				i, JoinCondition.EQUALS, pi);
		IMixedDatatypeRelation result = joinOperator.join();
		assertResults(result, e);
	}*/
	
	/**
	 * This is an example of inconsistent indexes. Namely 3rd column 
	 * of relation1 cannot be joined with 3rd column of relation0 
	 * (relation0 has arity 3 which means column: 0th, 1st and 2nd,
	 * so 3rd column does not exist)!   
	 * 
	 * int[] { 0, 1, -1, 3}
	 * 
	 */
	
	/**
	 * JoinSimpleExtended:
	 * 0th column of relation1 with 2nd column of relation0.
	 */
	public void testJoin_m1m10m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("e", "e", "e", "e", "b", "b", "x"));
		e.add(MiscHelper.createTuple("h", "h", "h", "h", "a", "a", "a"));
		e.add(MiscHelper.createTuple("h", "h", "h", "h", "a", "a", "b"));
		e.add(MiscHelper.createTuple("h", "g", "h", "h", "a", "a", "a"));
		e.add(MiscHelper.createTuple("h", "g", "h", "h", "a", "a", "b"));
		e.add(MiscHelper.createTuple("x", "x", "x", "x", "x", "x", "x"));
		e.add(MiscHelper.createTuple("x", "x", "x", "x", "e", "x", "x"));
		
		runJoin(new int[] { -1, -1, 0, -1}, e);
	}

	/**
	 * JoinSimpleExtended:
	 * 1st column of relation1 with 0th column of relation0 and
	 * 3rd column of relation1 with 1st column of relation0.
	 */
	public void testJoin_p1p3m1m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "b", "h", "a", "a", "b"));
		e.add(MiscHelper.createTuple("a", "b", "i", "h", "a", "a", "b"));
		e.add(MiscHelper.createTuple("a", "b", "d", "h", "a", "a", "b"));
		e.add(MiscHelper.createTuple("x", "x", "x", "x", "x", "x", "x"));
		
		runJoin(new int[] { 1, 3, -1, -1}, e);
	}
	
	/**
	 * JoinSimpleExtended:
	 * 0th column of relation1 with 0th column of relation0 and
	 * 1st column of relation1 with 1st column of relation0 and
	 * 2nd column of relation1 with 2nd column of relation0.
	 */
	public void testJoin_012m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("x", "x", "x", "x", "x", "x", "x"));
		
		runJoin(new int[] { 0, 1, 2, -1}, e);
	}
	
	/**
	 * JoinSimpleExtended:
	 * 1st column of relation1 with 0th column of relation0 and
	 * 1st column of relation1 with 1st column of relation0 and
	 * 1st column of relation1 with 2nd column of relation0.
	 */
	public void testJoin_p1p1p1m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("x", "x", "x", "x", "x", "x", "x"));
		e.add(MiscHelper.createTuple("e", "e", "e", "x", "e", "x", "x"));
		
		runJoin(new int[] { 1, 1, 1, -1}, e);
	}
	
	/**
	 * join on:
	 * 1st column of relation1 with 1st column of relation0 and
	 * 2nd column of relation1 with 2nd column of relation0.
	 * project on:
	 * 7th and 2nd and 1st column (in this order) 
	 */
	/*public void testJoinWithProjection_m1p1p2m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("x", "b", "a"));
		
		runJoinWithProjection(new int[] { -1, 1, 2, -1}, new int[]{2, -1, 1, -1, -1, -1, 0}, e);
	}*/
	
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

