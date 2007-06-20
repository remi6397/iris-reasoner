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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.storage.IRelation;

/**
 * <p>
 * Demonstrates use of the SortMergeJoin operator implementation. 
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 12.06.2007 09:52:43
 */
public class SortMergeJoinTest extends TestCase {
	
	static IMixedDatatypeRelation relation_0 = null;
	
	static IMixedDatatypeRelation relation_1 = null;
	
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

	private static void setRelations(){
		relation_0 = RELATION.getMixedRelation(4);
		relation_1 = RELATION.getMixedRelation(4);
		
		relation_0.addAll(Arrays.asList(tups_0));
		relation_1.addAll(Arrays.asList(tups_1));
	}
	
	public void runJoin_0(final int[] idxs, final Collection<ITuple> e) {
		setRelations();
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
