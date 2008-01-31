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
import org.deri.iris.api.operations.relation.IProjection;
import org.deri.iris.api.storage_old.IMixedDatatypeRelation;
import org.deri.iris.api.storage_old.IRelation;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   17.07.2006 16:19:43
 */
public class ProjectionTest extends TestCase {
	
	private static final ITuple[] tups = new ITuple[]{
		BASIC.createTuple(CONCRETE.createInteger(1), TERM.createString("b"), CONCRETE.createIri("http://aaa"), CONCRETE.createDouble(2d)), 
		BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createIri("http://www.google.com"), CONCRETE.createDouble(10d), CONCRETE.createInteger(8)), 
		BASIC.createTuple(CONCRETE.createInteger(2), TERM.createString("a"), CONCRETE.createIri("http://bbb"), CONCRETE.createDouble(1d)), 
		BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createInteger(3), CONCRETE.createDouble(5d), CONCRETE.createInteger(10)), 
		BASIC.createTuple(TERM.createString("zzzzab"), CONCRETE.createInteger(4), CONCRETE.createDouble(5d), CONCRETE.createInteger(10)),
		BASIC.createTuple(TERM.createString("aaa"), CONCRETE.createInteger(4), TERM.createString("aaa"), CONCRETE.createInteger(4)),
		BASIC.createTuple(CONCRETE.createInteger(4), CONCRETE.createInteger(4), CONCRETE.createInteger(4), CONCRETE.createInteger(4)),
		BASIC.createTuple(CONCRETE.createInteger(4), CONCRETE.createInteger(6), CONCRETE.createInteger(4), CONCRETE.createInteger(4))
	};
	
	public static Test suite() {
		return new TestSuite(ProjectionTest.class, ProjectionTest.class.getSimpleName());
	}

	/**
	 * Project a relation (tree) on set of arguments. 
	 * The arguments are defined by an array.
	 * 
	 * @param p
	 *            the pattern that defines how projection should be 
	 *            performed (see documentation for IProjection.project(..))
	 * @param e
	 *            the Collection of expected tuples
	 */
	protected static void runProjection(IMixedDatatypeRelation rel, 
			final int[] p, final Collection<ITuple> e) {
		
		// test the projection operation handling duplicates
		IProjection projectionOperator = RELATION_OPERATION.createProjectionOperator(
				rel, p);
		IMixedDatatypeRelation result = (IMixedDatatypeRelation)projectionOperator.project();
		assertResults(result, e);
	}

	/**
	 * Projection on the first atribute (column)-
	 * (terms with 0th index in a tuple).
	 */
	public void testProjection_pnnn() {
		IMixedDatatypeRelation rel = RELATION.getMixedRelation(4);
		rel.addAll(Arrays.asList(tups));
		
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(BASIC.createTuple(CONCRETE.createInteger(1)));
		e.add(BASIC.createTuple(CONCRETE.createInteger(2)));
		e.add(BASIC.createTuple(CONCRETE.createInteger(4)));
		e.add(BASIC.createTuple(TERM.createString("zzzz")));
		e.add(BASIC.createTuple(TERM.createString("zzzzab")));
		e.add(BASIC.createTuple(TERM.createString("aaa")));
		
		runProjection(rel, new int[]{0, -1, -1, -1}, e);
	}
	
	public void testProjection_npnn() {
		IMixedDatatypeRelation rel = RELATION.getMixedRelation(4);
		rel.addAll(Arrays.asList(tups));
		
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(BASIC.createTuple(CONCRETE.createInteger(3)));
		e.add(BASIC.createTuple(CONCRETE.createInteger(4)));
		e.add(BASIC.createTuple(CONCRETE.createInteger(6)));
		e.add(BASIC.createTuple(TERM.createString("a")));
		e.add(BASIC.createTuple(TERM.createString("b")));
		e.add(BASIC.createTuple(CONCRETE.createIri("http://www.google.com")));
		
		runProjection(rel, new int[]{-1, 0, -1, -1}, e);
	}
	
	/**
	 * This test is equivalent to the testProjection_npn.
	 * In both test we have a value different than -1 in 
	 * the second index. Thus the projection operation will
	 * be performed on the arguments with the index 1 in 
	 * both cases.
	 * The value for the second index is different, 
	 * but as the projection will be performed only on one
	 * argument in each tuple, the projected tuple will be
	 * of arity 1 and the index of its argument is 0 in 
	 * both cases. 
	 */
	public void testProjection_npnn_1() {
		IMixedDatatypeRelation rel = RELATION.getMixedRelation(4);
		rel.addAll(Arrays.asList(tups));
		
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(BASIC.createTuple(CONCRETE.createInteger(3)));
		e.add(BASIC.createTuple(CONCRETE.createInteger(4)));
		e.add(BASIC.createTuple(CONCRETE.createInteger(6)));
		e.add(BASIC.createTuple(TERM.createString("a")));
		e.add(BASIC.createTuple(TERM.createString("b")));
		e.add(BASIC.createTuple(CONCRETE.createIri("http://www.google.com")));
		
		runProjection(rel, new int[]{-1, 1, -1, -1}, e);
	}
	
	/**
	 * Do projection on the 0th and 1st index and then swap
	 * 0th and 1st argument in each tuple. 
	 */
	public void testProjection_ppnn_reverse() {
		IMixedDatatypeRelation rel = RELATION.getMixedRelation(4);
		rel.addAll(Arrays.asList(tups));
		
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(BASIC.createTuple(TERM.createString("b"), CONCRETE.createInteger(1)));
		e.add(BASIC.createTuple(CONCRETE.createIri("http://www.google.com"), TERM.createString("zzzz")));
		e.add(BASIC.createTuple(TERM.createString("a"), CONCRETE.createInteger(2)));
		e.add(BASIC.createTuple(CONCRETE.createInteger(4), TERM.createString("aaa")));
		e.add(BASIC.createTuple(CONCRETE.createInteger(4), TERM.createString("zzzzab")));
		e.add(BASIC.createTuple(CONCRETE.createInteger(3), TERM.createString("zzzz")));
		e.add(BASIC.createTuple(CONCRETE.createInteger(4), CONCRETE.createInteger(4)));
		e.add(BASIC.createTuple(CONCRETE.createInteger(6), CONCRETE.createInteger(4)));
		
		runProjection(rel, new int[]{1, 0, -1, -1}, e);
	}
	
	/**
	 * Tests the relation against a list of tuples using the assert methods
	 * of JUnit. The length of the relation and the list must be equal, 
	 * and the relation must contain all tuples of the list.
	 * 
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
