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
import org.deri.iris.api.storage_old.IMixedDatatypeRelation;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date 27.09.2006 10:02:43
 */
public class CartesianProductTest extends TestCase {

	public static Test suite() {
		return new TestSuite(CartesianProductTest.class, CartesianProductTest.class.getSimpleName());
	}

	/**
	 * Joins two relations (no duplicates handling) and then checks the 
	 * result against the submitted Collection of tuples. 
	 * 
	 * @param i
	 *            the indexes on which to join (see documentation for
	 *            IJoin.join(..) for the computation of this array)
	 * @param e
	 *            the Collection of expected tuples
	 */
	protected static void runJoin0(final int[] i, final Collection<ITuple> e) {
		IMixedDatatypeRelation relation0 = RELATION.getMixedRelation(3);
		IMixedDatatypeRelation relation1 = RELATION.getMixedRelation(3);
	
		relation0.add(MiscHelper.createTuple("a", "b", "i"));
		relation0.add(MiscHelper.createTuple("a", "b", "j"));	
		
		relation1.add(MiscHelper.createTuple("c", "b", "b"));
		relation1.add(MiscHelper.createTuple("f", "b", "a"));
		relation1.add(MiscHelper.createTuple("e", "b", "b"));
		relation1.add(MiscHelper.createTuple("h", "a", "a"));
		
		IJoin joinOperator = RELATION_OPERATION.createSortMergeJoinOperator(
				relation0, relation1, i, JoinCondition.EQUALS);
		IMixedDatatypeRelation result = joinOperator.join();
		assertResults(result, e);
	}

	protected static void runJoin1(final int[] i, final Collection<ITuple> e) {
		IMixedDatatypeRelation relation0 = RELATION.getMixedRelation(2);
		IMixedDatatypeRelation relation1 = RELATION.getMixedRelation(3);
	
		relation0.add(MiscHelper.createTuple("a", "b"));
		relation0.add(MiscHelper.createTuple("a", "j"));	
		
		relation1.add(MiscHelper.createTuple("c", "b", "b"));
		relation1.add(MiscHelper.createTuple("f", "b", "a"));
		relation1.add(MiscHelper.createTuple("e", "b", "b"));
		relation1.add(MiscHelper.createTuple("h", "a", "a"));
		
		IJoin joinOperator = RELATION_OPERATION.createSortMergeJoinOperator(
				relation0, relation1, i, JoinCondition.EQUALS);
		IMixedDatatypeRelation result = joinOperator.join();
		assertResults(result, e);
	}
	
	/**
	 * Cartesian Product of two relations with SAME arities
	 */
	public void testJoin0_m1m1m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "i", "c", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "i", "f", "b", "a"));
		e.add(MiscHelper.createTuple("a", "b", "i", "e", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "i", "h", "a", "a"));
		
		e.add(MiscHelper.createTuple("a", "b", "j", "c", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "j", "f", "b", "a"));
		e.add(MiscHelper.createTuple("a", "b", "j", "e", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "j", "h", "a", "a"));
		
		runJoin0(new int[] { -1, -1, -1}, e);
	}

	/**
	 * Cartesian Product of two relations with DIFFERENT arities
	 */
	public void testJoin1_m1m1m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "c", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "f", "b", "a"));
		e.add(MiscHelper.createTuple("a", "b", "e", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "h", "a", "a"));
		
		e.add(MiscHelper.createTuple("a", "j", "c", "b", "b"));
		e.add(MiscHelper.createTuple("a", "j", "f", "b", "a"));
		e.add(MiscHelper.createTuple("a", "j", "e", "b", "b"));
		e.add(MiscHelper.createTuple("a", "j", "h", "a", "a"));
		
		runJoin1(new int[] { -1, -1, -1}, e);
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


