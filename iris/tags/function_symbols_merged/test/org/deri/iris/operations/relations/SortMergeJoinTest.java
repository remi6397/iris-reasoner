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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.RELATION_OPERATION;
import static org.deri.iris.factory.Factory.TERM;
import static org.deri.iris.MiscHelper.createTuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage_old.IMixedDatatypeRelation;
import org.deri.iris.api.storage_old.IRelation;

/**
 * <p>
 * Demonstrates use of the SortMergeJoin operator implementation. 
 * </p>
 * <p>
 * $Id: SortMergeJoinTest.java,v 1.2 2007-06-20 09:46:29 poettler_ric Exp $
 * </p>
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 */
public class SortMergeJoinTest extends TestCase {
	
	static IMixedDatatypeRelation relation_0 = null;
	
	static IMixedDatatypeRelation relation_1 = null;
	
	static IMixedDatatypeRelation r0 = null;
	
	static IMixedDatatypeRelation r1 = null;
	
	// Tuples from the first relation to be joined.
	private static final ITuple[] tups_0 = new ITuple[]{
		BASIC.createTuple(CONCRETE.createInteger(1), TERM.createString("b"), CONCRETE.createIri("http://aaa"), CONCRETE.createDouble(2d)), 
		BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createIri("http://www.google.com"), CONCRETE.createDouble(10d), CONCRETE.createInteger(8)), 
		BASIC.createTuple(CONCRETE.createInteger(2), TERM.createString("a"), CONCRETE.createIri("http://bbb"), CONCRETE.createDouble(1d)), 
		BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createInteger(3), CONCRETE.createDouble(5d), CONCRETE.createInteger(10)), 
		BASIC.createTuple(TERM.createString("zzzzab"), CONCRETE.createInteger(4), CONCRETE.createDouble(5d), CONCRETE.createInteger(10)), 
	};
	// Tuples from the second relation to be joined.
	private static final ITuple[] tups_1 = new ITuple[]{
		BASIC.createTuple(CONCRETE.createInteger(1), TERM.createString("b"), CONCRETE.createIri("http://aaa"), CONCRETE.createDouble(2d)), 
		BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createIri("http://www.google.com"), CONCRETE.createDouble(10d), CONCRETE.createInteger(8)), 
		BASIC.createTuple(CONCRETE.createInteger(2), TERM.createString("aa"), CONCRETE.createIri("http://bbb"), CONCRETE.createDouble(1d)), 
		BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createInteger(33), CONCRETE.createDouble(5d), CONCRETE.createInteger(10)), 
		BASIC.createTuple(TERM.createString("zzzzab"), CONCRETE.createInteger(44), CONCRETE.createDouble(5d), CONCRETE.createInteger(10)), 
	};
	
	public static Test suite() {
		return new TestSuite(SortMergeJoinTest.class, SortMergeJoinTest.class.getSimpleName());
	}

	public void setUp() {
		// fill r0
		r0 = RELATION.getMixedRelation(3);
		r0.add(createTuple("a", "a", "c"));
		r0.add(createTuple("b", "a", "c"));
		r0.add(createTuple("c", "b", "b"));

		r0.add(BASIC.createTuple(TERM.createString("r"), TERM.createString("x"), CONCRETE.createInteger(1)));
		r0.add(BASIC.createTuple(TERM.createString("r"), TERM.createString("y"), CONCRETE.createInteger(1)));
		r0.add(BASIC.createTuple(TERM.createString("s"), TERM.createString("x"), CONCRETE.createInteger(2)));
		r0.add(BASIC.createTuple(TERM.createString("s"), TERM.createString("y"), CONCRETE.createInteger(2)));

		// fill r1
		r1 = RELATION.getMixedRelation(4);
		r1.add(createTuple("a", "b", "d", "c"));
		r1.add(createTuple("a", "c", "c", "c"));
		r1.add(createTuple("c", "d", "b", "a"));
		r1.add(createTuple("c", "e", "a", "a"));

		r1.add(BASIC.createTuple(TERM.createString("a"), TERM.createString("x"), CONCRETE.createInteger(1), CONCRETE.createInteger(8)));
		r1.add(BASIC.createTuple(TERM.createString("a"), TERM.createString("y"), CONCRETE.createInteger(2), CONCRETE.createInteger(7)));
		r1.add(BASIC.createTuple(TERM.createString("b"), TERM.createString("x"), CONCRETE.createInteger(3), CONCRETE.createInteger(6)));
		r1.add(BASIC.createTuple(TERM.createString("b"), TERM.createString("y"), CONCRETE.createInteger(4), CONCRETE.createInteger(5)));

		// adding darkos relations -> do it automatically
		relation_0 = RELATION.getMixedRelation(4);
		relation_1 = RELATION.getMixedRelation(4);
		
		relation_0.addAll(Arrays.asList(tups_0));
		relation_1.addAll(Arrays.asList(tups_1));
	}

	public void runJoin_0(final int[] idxs, final Collection<ITuple> e) {
		IJoin sortMergeJoinOperator 
			= RELATION_OPERATION.createSortMergeJoinOperator(
					relation_0,relation_1, idxs, JoinCondition.EQUALS);
			
		IRelation result = sortMergeJoinOperator.join();
		assertResults(result, e);
	}
	
	public void testJoin_0() {
		// Join indexes
		int[] idxs = new int[]{ -1, 1, -1, -1};
		
		// Result relation
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(BASIC.createTuple(
				CONCRETE.createInteger(1), TERM.createString("b"), CONCRETE.createIri("http://aaa"), CONCRETE.createDouble(2d),
				CONCRETE.createInteger(1), TERM.createString("b"), CONCRETE.createIri("http://aaa"), CONCRETE.createDouble(2d)));
		e.add(BASIC.createTuple(
				TERM.createString("zzzz"), CONCRETE.createIri("http://www.google.com"), CONCRETE.createDouble(10d), CONCRETE.createInteger(8),
				TERM.createString("zzzz"), CONCRETE.createIri("http://www.google.com"), CONCRETE.createDouble(10d), CONCRETE.createInteger(8)));
		
		runJoin_0(idxs, e);
	}

	public void testJoin1() {
		final SortMergeJoin j = new SortMergeJoin(r0, r1, new int[]{0, -1, -1}, null);
		final Set<ITuple> exp = new HashSet<ITuple>();
		exp.add(createTuple("a", "a", "c", "a", "b", "d", "c"));
		exp.add(createTuple("a", "a", "c", "a", "c", "c", "c"));
		exp.add(createTuple("c", "b", "b", "c", "d", "b", "a"));
		exp.add(createTuple("c", "b", "b", "c", "e", "a", "a"));
		exp.add(BASIC.createTuple(TERM.createString("a"), TERM.createString("a"), TERM.createString("c"), 
					TERM.createString("a"), TERM.createString("x"), CONCRETE.createInteger(1), CONCRETE.createInteger(8)));
		exp.add(BASIC.createTuple(TERM.createString("a"), TERM.createString("a"), TERM.createString("c"), 
					TERM.createString("a"), TERM.createString("y"), CONCRETE.createInteger(2), CONCRETE.createInteger(7)));
		exp.add(BASIC.createTuple(TERM.createString("b"), TERM.createString("a"), TERM.createString("c"), 
					TERM.createString("b"), TERM.createString("x"), CONCRETE.createInteger(3), CONCRETE.createInteger(6)));
		exp.add(BASIC.createTuple(TERM.createString("b"), TERM.createString("a"), TERM.createString("c"), 
					TERM.createString("b"), TERM.createString("y"), CONCRETE.createInteger(4), CONCRETE.createInteger(5)));
		assertResults(j.evaluate(), exp);
	}

	public void testJoin2() {
		final SortMergeJoin j = new SortMergeJoin(r0, r1, new int[]{-1, -1, 2}, null);
		final Set<ITuple> exp = new HashSet<ITuple>();
		exp.add(BASIC.createTuple(TERM.createString("r"), TERM.createString("x"), CONCRETE.createInteger(1), 
					TERM.createString("a"), TERM.createString("x"), CONCRETE.createInteger(1), CONCRETE.createInteger(8)));
		exp.add(BASIC.createTuple(TERM.createString("r"), TERM.createString("y"), CONCRETE.createInteger(1), 
					TERM.createString("a"), TERM.createString("x"), CONCRETE.createInteger(1), CONCRETE.createInteger(8)));
		exp.add(BASIC.createTuple(TERM.createString("s"), TERM.createString("x"), CONCRETE.createInteger(2), 
					TERM.createString("a"), TERM.createString("y"), CONCRETE.createInteger(2), CONCRETE.createInteger(7)));
		exp.add(BASIC.createTuple(TERM.createString("s"), TERM.createString("y"), CONCRETE.createInteger(2), 
					TERM.createString("a"), TERM.createString("y"), CONCRETE.createInteger(2), CONCRETE.createInteger(7)));
		exp.add(createTuple("a", "a", "c", "a", "c", "c", "c"));
		exp.add(createTuple("b", "a", "c", "a", "c", "c", "c"));
		exp.add(createTuple("c", "b", "b", "c", "d", "b", "a"));
		assertResults(j.evaluate(), exp);
	}

	public void testJoin3() {
		final SortMergeJoin j = new SortMergeJoin(r0, r1, new int[]{-1, 1, 2}, JoinCondition.LESS_THAN);
		final Set<ITuple> exp = new HashSet<ITuple>();
		exp.add(BASIC.createTuple(TERM.createString("s"), TERM.createString("y"), CONCRETE.createInteger(2), 
					TERM.createString("a"), TERM.createString("x"), CONCRETE.createInteger(1), CONCRETE.createInteger(8)));
		assertResults(j.evaluate(), exp);
	}

	public void testJoin4() {
		final SortMergeJoin j = new SortMergeJoin(r0, r1, new int[]{-1, 1, 2}, JoinCondition.GREATER_THAN);
		final Set<ITuple> exp = new HashSet<ITuple>();
		exp.add(BASIC.createTuple(TERM.createString("r"), TERM.createString("x"), CONCRETE.createInteger(1), 
					TERM.createString("a"), TERM.createString("y"), CONCRETE.createInteger(2), CONCRETE.createInteger(7)));
		exp.add(BASIC.createTuple(TERM.createString("r"), TERM.createString("x"), CONCRETE.createInteger(1), 
					TERM.createString("b"), TERM.createString("y"), CONCRETE.createInteger(4), CONCRETE.createInteger(5)));
		exp.add(BASIC.createTuple(TERM.createString("s"), TERM.createString("x"), CONCRETE.createInteger(2), 
					TERM.createString("b"), TERM.createString("y"), CONCRETE.createInteger(4), CONCRETE.createInteger(5)));
		exp.add(createTuple("a", "a", "c", "a", "b", "d", "c"));
		exp.add(createTuple("b", "a", "c", "a", "b", "d", "c"));
		exp.add(createTuple("c", "b", "b", "a", "c", "c", "c"));
		assertResults(j.evaluate(), exp);
	}
	
	/**
	 * <p>
	 * Tests the relation against a list of tuples using the assert methods
	 * of JUnit. The length of the relation and the list must be equal, 
	 * and the relation must contain all tuples of the list.
	 * </p>
	 * @param r
	 *            the relation to check
	 * @param e
	 *            the Collection containing all expected tuples
	 */
	protected static void assertResults(final IRelation r,
			final Collection<ITuple> e) {
		Assert.assertEquals("The length of relation and the list of"
				+ " expected tuples must be equal", e.size(), r.size());
		Assert.assertTrue("The relation must contain all expected tuples", r
				.containsAll(e));
	}
}
