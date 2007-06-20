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

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.storage.IRelation;

/**
 * <p>
 * Demonstrates use of the SortMergeJoin operator implementation. 
 * </p>
 * <p>
 * $Id: DoingAllAtOnceOperationTest.java,v 1.1 2007-06-20 09:48:03 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 */
public class DoingAllAtOnceOperationTest extends TestCase {
	
	static IMixedDatatypeRelation r0 = null;
	
	static IMixedDatatypeRelation r1 = null;
	
	public static Test suite() {
		return new TestSuite(DoingAllAtOnceOperationTest.class, DoingAllAtOnceOperationTest.class.getSimpleName());
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
	}

	public void testAllAtOnce() {
		// parameters for the selection of r0
		final int[] sidx0 = new int[]{1, -1, -1};
		final JoinCondition[] sc0 = new JoinCondition[]{JoinCondition.GREATER_THAN};
		final ITerm[] sthreshold0 = new ITerm[]{TERM.createString("a")};
		// parameters for the selection of r1
		final int[] sidx1 = new int[]{-1, -1, 1, -1};
		final JoinCondition[] sc1 = new JoinCondition[]{JoinCondition.GREATER_THAN};
		final ITerm[] sthreshold1 = new ITerm[]{TERM.createString("b")};
		// parameters for the join
		final int[] jidx = new int[]{-1, 0, -1};
		final JoinCondition jc = JoinCondition.EQUALS;
		final int semi = -1;
		// parameters for the projection
		final int[] pidx = new int[]{-1, -1, 2, 0, -1, 1, -1};
		// the expected result
		final Set<ITuple> exp = new HashSet<ITuple>();
		exp.add(createTuple("a", "d", "c"));
		exp.add(createTuple("a", "c", "c"));

		// it might be faster, but such a parameter mess is insane...
		final DoingAllAtOnceOperation op = new DoingAllAtOnceOperation(
				r0, sidx0, sthreshold0, sc0, r1, sidx1, sthreshold1, sc1, jidx, jc, semi, pidx);
		assertResults(op.evaluate(), exp);
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
		Assert.assertTrue(e + " expected, but got " + r, r
				.containsAll(e));
	}
}
