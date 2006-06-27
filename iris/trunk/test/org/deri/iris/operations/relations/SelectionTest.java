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
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.storage.Relation;


/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   31.05.2006 11:34:30
 */
public class SelectionTest extends TestCase {

	
	public static Test suite() {
		return new TestSuite(SelectionTest.class, SelectionTest.class.getSimpleName());
	}

	/**
	 * Select a portion of a relation (tree) based on a condition (tuple) and 
	 * then checks the result against the submitted Collection of tuples.
	 * 
	 * @param p
	 *            the pattern that defines which tuples should be selected
	 *            (see documentation for ISelection.select(..) )
	 * @param e
	 *            the Collection of expected tuples
	 */
	protected static void runSelect(final ITuple p, final Collection<ITuple> e) {
		IRelation<ITuple> relation = new Relation(3);
	
		relation.add(MiscHelper.createTuple("a", "a", "a"));
		relation.add(MiscHelper.createTuple("a", "b", "a"));
		relation.add(MiscHelper.createTuple("a", "b", "b"));
		
		relation.add(MiscHelper.createTuple("d", "d", "d"));
		relation.add(MiscHelper.createTuple("d", "d", "a"));
		relation.add(MiscHelper.createTuple("a", "d", "d"));
		
		// test Select operation handling duplicates
		ISelection selectOperator = RELATION.createSelectionOperator(
				relation, p);
		IRelation result = selectOperator.select();
		assertResults(result, e);
	}

	public void testSelect_aaa() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "a", "a"));
		runSelect(MiscHelper.createTuple("a", "a", "a"), e);
	}
	
	public void testSelect_ana() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "a", "a"));
		e.add(MiscHelper.createTuple("a", "b", "a"));
		runSelect(MiscHelper.createTuple("a", null, "a"), e);
	}
	
	public void testSelect_anb() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "b"));
		runSelect(MiscHelper.createTuple("a", null, "b"), e);
	}
	
	public void testSelect_ndd() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "d", "d"));
		e.add(MiscHelper.createTuple("d", "d", "d"));
		runSelect(MiscHelper.createTuple(null, "d", "d"), e);
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
	protected static void assertResults(final IRelation<ITuple> r,
			final Collection<ITuple> e) {
		Assert.assertEquals("The length of relation and the list of"
				+ " expected tuples must be equal", e.size(), r.size());
		Assert.assertTrue("The relation must contain all expected tuples", r
				.containsAll(e));
	}
}
