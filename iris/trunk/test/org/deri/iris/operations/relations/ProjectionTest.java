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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.MiscHelper;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IProjection;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.storage.Relation;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   17.07.2006 16:19:43
 */
public class ProjectionTest extends TestCase {
	
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
	protected static void runProjection(
			final int[] p, final Collection<ITuple> e) {
		IRelation<ITuple> relation = new Relation(3);
	
		relation.add(MiscHelper.createTuple("a", "a", "a"));
		relation.add(MiscHelper.createTuple("a", "b", "a"));
		relation.add(MiscHelper.createTuple("a", "b", "b"));
		
		relation.add(MiscHelper.createTuple("d", "d", "d"));
		relation.add(MiscHelper.createTuple("d", "d", "a"));
		relation.add(MiscHelper.createTuple("a", "d", "c"));
		
		// test the projection operation handling duplicates
		IProjection projectionOperator = RELATION.createProjectionOperator(
				relation, p);
		IRelation result = projectionOperator.project();
		assertResults(result, e);
	}

	/**
	 * Projection on the tuple arguments in the first column 
	 * (those with index 0th).
	 */
	public void testProjection_pnn() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a"));
		e.add(MiscHelper.createTuple("d"));
		runProjection(new int[]{0, -1, -1}, e);
	}
	
	public void testProjection_npn() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a"));
		e.add(MiscHelper.createTuple("d"));
		e.add(MiscHelper.createTuple("b"));
		runProjection(new int[]{-1, 0, -1}, e);
	}
	
	/**
	 * This test is equivalent to the testProjection_npn.
	 * In both test we have a value different of -1 in 
	 * the second index. Thus the projection operation will
	 * be performed on the arguments with the index 1 in 
	 * both cases.
	 * The value for the second index is different, 
	 * but as the projection will be performed only on one
	 * argument in each tuple, the projected tuple will be
	 * of arity 1 and the index of its argument is 0 in 
	 * both cases. 
	 */
	public void testProjection_npn_1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a"));
		e.add(MiscHelper.createTuple("d"));
		e.add(MiscHelper.createTuple("b"));
		runProjection(new int[]{-1, 1, -1}, e);
	}
	
	/**
	 * Do projection on the 0th and 1st index and then swap
	 * 0th and 1st argument in each tuple. 
	 */
	public void testProjection_pnp_reverse() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "a"));
		e.add(MiscHelper.createTuple("b", "a"));
		e.add(MiscHelper.createTuple("d", "d"));
		e.add(MiscHelper.createTuple("a", "d"));
		e.add(MiscHelper.createTuple("c", "a"));
		runProjection(new int[]{1, -1, 0}, e);
	}
	
	/**
	 * For the following indexes: [0, 1, 2] nothing will 
	 * be changed after performing the projection.
	 */
	public void testProjection_ppp() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "a", "a"));
		e.add(MiscHelper.createTuple("a", "b", "a"));
		e.add(MiscHelper.createTuple("a", "b", "b"));
		e.add(MiscHelper.createTuple("d", "d", "d"));
		e.add(MiscHelper.createTuple("d", "d", "a"));
		e.add(MiscHelper.createTuple("a", "d", "c"));
		runProjection(new int[]{0, 1, 2}, e);
	}
	
	/**
	 * Do not do projection, only swap attributes with
	 * 0th index with those with 1st index.
	 */
	public void testProjection_ppp_reverse() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "a", "a"));
		e.add(MiscHelper.createTuple("a", "b", "a"));
		e.add(MiscHelper.createTuple("b", "b", "a"));
		e.add(MiscHelper.createTuple("d", "d", "d"));
		e.add(MiscHelper.createTuple("a", "d", "d"));
		e.add(MiscHelper.createTuple("c", "d", "a"));
		runProjection(new int[]{2, 1, 0}, e);
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
	protected static void assertResults(final IRelation<ITuple> r,
			final Collection<ITuple> e) {
		Assert.assertEquals("The length of relation and the list of"
				+ " expected tuples must be equal", e.size(), r.size());
		Assert.assertTrue("The relation must contain all expected tuples", r
				.containsAll(e));
	}
}
