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
 * @author Richard Pöttler
 * @author Darko Anicic, DERI Innsbruck
 * @date 26.05.2006 13:59:43
 */
public class JoinSimpleExtendedTest extends TestCase {
	private static IJoin joinOperator = null;
	private static IMixedDatatypeRelation result = null;
	
	public static Test suite() {
		return new TestSuite(JoinSimpleExtendedTest.class, JoinSimpleExtendedTest.class.getSimpleName());
	}

	/**
	 * Joins two relations and then checks the result against the submitted
	 * Collection of tuples.
	 * 
	 * @param i
	 *            the indexes on which to join (see documentation for
	 *            IJoin.join(..) for the computation of this array)
	 * @param e
	 *            the Collection of expected tuples
	 */
	protected static void runJoin(final int[] i, final Collection<ITuple> e) {
		IMixedDatatypeRelation relation0 = RELATION.getMixedRelation(3);
		IMixedDatatypeRelation relation1 = RELATION.getMixedRelation(3);

		relation0.add(MiscHelper.createTuple("a", "b", "c"));
		relation0.add(MiscHelper.createTuple("a", "b", "b"));
		relation0.add(MiscHelper.createTuple("f", "g", "h"));
		relation0.add(MiscHelper.createTuple("h", "g", "f"));
		relation0.add(MiscHelper.createTuple("h", "g", "a"));
		
		relation1.add(MiscHelper.createTuple("c", "b", "b"));
		relation1.add(MiscHelper.createTuple("c", "b", "a"));
		relation1.add(MiscHelper.createTuple("a", "b", "c"));

		// test join operation handling duplicates
		joinOperator = RELATION_OPERATION.createSortMergeJoinOperator(
				relation0, relation1, i, JoinCondition.EQUALS);
		result = joinOperator.join();
		assertResults(result, e);
	}
	
	/**
	 * Thae same as runJoin.
	 * 
	 * @param i
	 * @param e
	 * @param projectIndexes
	 * 						define indexes which the projection operation
	 * 						will be applied on.
	 */
	/*protected static void runJoin_projection(final int[] i, 
			final Collection<ITuple> e, final int[] projectIndexes) {
		IMixedDatatypeRelation relation0 = RELATION.getMixedRelation(3);
		IMixedDatatypeRelation relation1 = RELATION.getMixedRelation(3);

		relation0.add(MiscHelper.createTuple("a", "b", "c"));
		relation0.add(MiscHelper.createTuple("a", "b", "b"));
		relation0.add(MiscHelper.createTuple("f", "g", "h"));
		relation0.add(MiscHelper.createTuple("h", "g", "f"));
		relation0.add(MiscHelper.createTuple("h", "g", "a"));
		
		relation1.add(MiscHelper.createTuple("c", "b", "b"));
		relation1.add(MiscHelper.createTuple("c", "b", "a"));
		relation1.add(MiscHelper.createTuple("a", "b", "c"));

		// test join operation handling duplicates
		joinOperator = RELATION_OPERATION.createSortMergeJoinOperator(
				relation0, relation1, i, 
					JoinCondition.EQUALS, projectIndexes);
		result = joinOperator.join();
		assertResults(result, e);
	}*/

	public void testJoin_m1m10() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "c", "c", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "c", "c", "b", "a"));
		e.add(MiscHelper.createTuple("h", "g", "a", "a", "b", "c"));
		runJoin(new int[] { -1, -1, 0 }, e);
	}

	public void testJoin_210() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "c", "c", "b", "a"));
		runJoin(new int[] { 2, 1, 0 }, e);
	}
	
	public void testJoin_m12m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "c", "c", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "b", "c", "b", "b"));
		runJoin(new int[] { -1, 2, -1 }, e);
	}
	
	public void testJoin_m1m1m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "c", "c", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "c", "c", "b", "a"));
		e.add(MiscHelper.createTuple("a", "b", "c", "a", "b", "c"));
		e.add(MiscHelper.createTuple("a", "b", "b", "c", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "b", "c", "b", "a"));
		e.add(MiscHelper.createTuple("a", "b", "b", "a", "b", "c"));
		e.add(MiscHelper.createTuple("f", "g", "h", "c", "b", "b"));
		e.add(MiscHelper.createTuple("f", "g", "h", "c", "b", "a"));
		e.add(MiscHelper.createTuple("f", "g", "h", "a", "b", "c"));
		e.add(MiscHelper.createTuple("h", "g", "f", "c", "b", "b"));
		e.add(MiscHelper.createTuple("h", "g", "f", "c", "b", "a"));
		e.add(MiscHelper.createTuple("h", "g", "f", "a", "b", "c"));
		e.add(MiscHelper.createTuple("h", "g", "a", "c", "b", "b"));
		e.add(MiscHelper.createTuple("h", "g", "a", "c", "b", "a"));
		e.add(MiscHelper.createTuple("h", "g", "a", "a", "b", "c"));
		// -1, -1, -1 means join all with all
		runJoin(new int[] { -1, -1, -1 }, e);
	}
	
	public void testJoin_111() {
		final List<ITuple> e = new ArrayList<ITuple>();
		runJoin(new int[] { 1, 1, 1 }, e);
	}
	
	/**
	 * Demonstration of use the projection operation after 
	 * the join operation has been performed.
	 * 
	 * After the join operation we have two produced tuples:
	 * 
	 * <a,b,c,c,b,b>
	 * <a,b,b,c,b,b>
	 * 
	 * Then we perform the projection on attributes with 
	 * values different from -1 (from the projectIndexes array).
	 * This means we want the projection on attributes with 
	 * indexes: 0th, 3rd, 5th. Thus we will get:
	 * 
	 * <a,c,b>
	 */
	/*public void testJoin_projection() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "c", "b"));
		runJoin_projection(new int[] { -1, 2, -1 }, e, 
				new int[] {1, -1, -1, 1, -1, 1});
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
