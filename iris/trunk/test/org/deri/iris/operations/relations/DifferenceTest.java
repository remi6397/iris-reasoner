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
import org.deri.iris.api.operations.relation.IDifference;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.storage.Relation;

/**
 * @author Joachim Adi Schuetz, DERI Innsbruck
 * @author Darko Anicic, DERI Innsbruck
 * @date 06.10.2006
 */
public class DifferenceTest extends TestCase {

	public static Test suite() {
		return new TestSuite(DifferenceTest.class, DifferenceTest.class.getSimpleName());
	}

	/**
	 * Computes the difference of two relations (no duplicates handling) 
	 * and then checks the result against the submitted Collection of tuples. 
	 * e = realtion0 - relation1
	 * 
	 * @param relation0
	 *            the first relation
	 * @param relation1
	 *            the second relation
	 * @param e
	 *            the Collection of expected tuples
	 */
	protected static void runDifference(final IRelation<ITuple> relation0, final IRelation<ITuple> relation1, final Collection<ITuple> e) {

		IDifference differenceOperator = RELATION_OPERATION.createDifferenceOperator(
				relation0, relation1);
		IRelation result = differenceOperator.difference();
		assertResults(result, e);
	}
	
	/**
	 * simplest situation: 2 relations with same arity ... result ok!?
	 *
	 */
	public void testDifference() {
		final IRelation<ITuple> rel0 = new Relation(3);
		final IRelation<ITuple> rel1 = new Relation(3);
		
		// relation0: add tuples
		rel0.add(MiscHelper.createTuple("a", "b", "c"));
		rel0.add(MiscHelper.createTuple("a", "a", "a"));
		rel0.add(MiscHelper.createTuple("a", "c", "b"));
		
		// relation1: add tuples
		rel1.add(MiscHelper.createTuple("a", "a", "a"));

		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "c"));
		e.add(MiscHelper.createTuple("a", "c", "b"));

		runDifference(rel0, rel1, e);
	}

	/**
	 * are duplets handled correctly!?
	 *
	 */
	public void testDifference_duplet0() {
		final IRelation<ITuple> rel0 = new Relation(3);
		final IRelation<ITuple> rel1 = new Relation(3);
		
		// relation0: add tuples
		rel0.add(MiscHelper.createTuple("a", "b", "c"));
		rel0.add(MiscHelper.createTuple("a", "a", "a"));
		rel0.add(MiscHelper.createTuple("a", "a", "a"));
		
		// relation1: add tuples
		rel1.add(MiscHelper.createTuple("a", "a", "a"));

		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "c"));

		runDifference(rel0, rel1, e);
	}
	public void testDifference_duplet1() {
		final IRelation<ITuple> rel0 = new Relation(3);
		final IRelation<ITuple> rel1 = new Relation(3);
		
		// relation0: add tuples
		rel0.add(MiscHelper.createTuple("a", "b", "c"));
		rel0.add(MiscHelper.createTuple("a", "a", "a"));
		
		// relation1: add tuples
		rel1.add(MiscHelper.createTuple("a", "a", "a"));
		rel1.add(MiscHelper.createTuple("a", "a", "a"));

		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "c"));

		runDifference(rel0, rel1, e);
	}

	/**
	 * empty result
	 *
	 */
	public void testDifference_zero() {
		
		final IRelation<ITuple> rel0 = new Relation(3);
		final IRelation<ITuple> rel1 = new Relation(3);

		// relation0: add tuples
		rel0.add(MiscHelper.createTuple("a", "b", "c"));
		
		// relation1: add tuples
		rel1.add(MiscHelper.createTuple("a", "b", "c"));

		final List<ITuple> e = new ArrayList<ITuple>();

		runDifference(rel0, rel1, e);
	}

	/**
	 * first parameter relations is empty
	 *
	 */
	public void testDifference_emptyrel0() {
	
		final IRelation<ITuple> rel0 = new Relation(3);
		final IRelation<ITuple> rel1 = new Relation(3);

		// relation0: add tuples
		
		// relation1: add tuples
		rel1.add(MiscHelper.createTuple("a", "b", "c"));

		final List<ITuple> e = new ArrayList<ITuple>();

		runDifference(rel0, rel1, e);
	}

	/**
	 * second parameter relations is empty
	 *
	 */
	public void testDifference_emptyrel1() {
		
		final IRelation<ITuple> rel0 = new Relation(3);
		final IRelation<ITuple> rel1 = new Relation(3);

		// relation0: add tuples
		rel0.add(MiscHelper.createTuple("a", "b", "c"));
		
		// relation1: add tuples

		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "c"));

		runDifference(rel0, rel1, e);
	}

	/**
	 * both parameter relations are empty
	 *
	 */
	public void testDifference_emptyrels() {
		
		final IRelation<ITuple> rel0 = new Relation(3);
		final IRelation<ITuple> rel1 = new Relation(3);

		// relation0: add tuples
		
		// relation1: add tuples

		final List<ITuple> e = new ArrayList<ITuple>();

		runDifference(rel0, rel1, e);
	}

	/**
	 * both parameter are the null-value -> should raise an IllegalArgumentException
	 *
	 */
	public void testDifference_paramsnull() {
		try {
			IDifference differenceOperator = RELATION_OPERATION.createDifferenceOperator(
				null, null);
			fail ("should have raised an java.lang.IllegalArgumentException");
		} catch(java.lang.IllegalArgumentException e)  {}
	}

	/**
	 * parameter have different arity -> should raise an IllegalArgumentException
	 *
	 */
	public void testDifference_aritydoesnotmatch0() {
		IRelation<ITuple> rel0 = new Relation(3);
		IRelation<ITuple> rel1 = new Relation(4);
		
		// relation0: add tuples
		rel0.add(MiscHelper.createTuple("a", "b", "c"));
		rel0.add(MiscHelper.createTuple("a", "a", "a"));
		rel0.add(MiscHelper.createTuple("a", "c", "b"));
		
		// relation1: add tuples
		rel1.add(MiscHelper.createTuple("a", "a", "a", "a"));

		try {
			IDifference differenceOperator = RELATION_OPERATION.createDifferenceOperator(
				rel0, rel1);
			fail ("should have raised an java.lang.IllegalArgumentException");
		} catch(java.lang.IllegalArgumentException e)  {}
	}
	public void testDifference_aritydoesnotmatch1() {
		IRelation<ITuple> rel0 = new Relation(4);
		IRelation<ITuple> rel1 = new Relation(3);
		
		// relation0: add tuples
		rel0.add(MiscHelper.createTuple("a", "b", "c", "d"));
		rel0.add(MiscHelper.createTuple("a", "a", "a", "a"));
		rel0.add(MiscHelper.createTuple("a", "c", "b", "d"));
		
		// relation1: add tuples
		rel1.add(MiscHelper.createTuple("a", "a", "a"));

		try {
			IDifference differenceOperator = RELATION_OPERATION.createDifferenceOperator(
				rel0, rel1);
			fail ("should have raised an java.lang.IllegalArgumentException");
		} catch(java.lang.IllegalArgumentException e)  {}
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
