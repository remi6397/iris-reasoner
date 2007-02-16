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
package org.deri.iris.storage;

import static org.deri.iris.MiscHelper.createTuple;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import junit.framework.TestCase;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IRelation;

/**
 * <p>
 * Provides generic tests which should be passed by all relation
 * implementations.
 * </p>
 * <p>
 * $Id: GenericRelationTest.java,v 1.1 2007-02-16 09:02:38 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.1 $
 */
public abstract class GenericRelationTest<Type extends IRelation> extends
		TestCase {

	private static final ITuple[] tup = new ITuple[] {
			createTuple("a", "b", "c"), createTuple("b", "c", "a"),
			createTuple("c", "a", "b"), createTuple("x", "y", "z"),
			createTuple("a", "x", "w") };

	protected Type r;

	/**
	 * <p>
	 * In this method the field &quot;{@code r}&quot; of
	 * {@code GenericRelationTest} must be set.
	 * </p>
	 * <p>
	 * e.g.: <code>r = new IndexingOnTheFlyRelation()</code>
	 * </p>
	 */
	public abstract void setUp();

	public void testPutGetSingle() {
		for (final ITuple t : tup) {
			r.add(t);
		}
		assertEquals(
				"The size of the array and the stored tuples mut be equal",
				tup.length, r.size());
		for (final ITuple t : tup) {
			assertTrue("Couldn't find the tuple " + t, r.contains(t));
		}
	}

	public void testPutGetAll() {
		r.addAll(Arrays.asList(tup));
		assertEquals(
				"The size of the array and the stored tuples mut be equal",
				tup.length, r.size());
		for (final ITuple t : tup) {
			assertTrue("Couldn't find the tuple " + t, r.contains(t));
		}
	}

	public void testTailSet() {
		r.addAll(Arrays.asList(tup));
		// set containing all suples wich are larger than (c, a, a) when the
		// relation is sorted on the first field
		final Set<ITuple> tailRef = new HashSet<ITuple>();
		tailRef.add(createTuple("c", "a", "b"));
		tailRef.add(createTuple("x", "y", "z"));
		final Set<ITuple> tail = new HashSet<ITuple>(r.tailSet(createTuple("c",
				"a", "a")));
		assertEquals("The tails must be equal", tailRef, tail);
	}

	public void testHeadSet() {
		r.addAll(Arrays.asList(tup));
		// set containing all suples wich are smaller than (b, c, b) when the
		// relation is sorted on the first field
		final Set<ITuple> tailRef = new HashSet<ITuple>();
		tailRef.add(createTuple("a", "b", "c"));
		tailRef.add(createTuple("a", "x", "w"));
		tailRef.add(createTuple("b", "c", "a"));
		final Set<ITuple> tail = new HashSet<ITuple>(r.headSet(createTuple("b",
				"c", "d")));
		assertEquals("The tails must be equal", tailRef, tail);
	}

	public void testIndexOn() {
		final Set<ITuple> allTup = new HashSet<ITuple>();
		allTup.add(createTuple("a", "b", "k", "l"));
		allTup.add(createTuple("k", "l", "c", "d"));
		allTup.add(createTuple("a", "b", "x", "y"));
		allTup.add(createTuple("x", "y", "c", "d"));
		allTup.add(createTuple("k", "l", "x", "y"));
		allTup.add(createTuple("x", "y", "k", "l"));
		allTup.add(createTuple("x", "y", "a", "b"));

		r.addAll(allTup);

		final SortedSet<ITuple> rel_0132 = r
				.indexOn(new Integer[] { 0, 1, 3, 2 });
		assertEquals("The first tuple of relation 0132 must be (a, b, k, l)",
				createTuple("a", "b", "k", "l"), rel_0132.first());
		final SortedSet<ITuple> rel_3412 = r
				.indexOn(new Integer[] { 3, 4, 1, 2 });
		assertEquals("The first tuple of relation 0132 must be (x, y, a, b)",
				createTuple("x", "y", "a", "b"), rel_3412.first());
		assertEquals(
				"The first tuple of relation 0132 must be still (a, b, k, l)",
				createTuple("a", "b", "k", "l"), rel_0132.first());

		r.add(createTuple("a", "a", "a", "a"));

		assertEquals(
				"The first tuple of relation 0132 now must be (a, a, a, a)",
				createTuple("a", "a", "a", "a"), rel_3412.first());
		assertEquals(
				"The first tuple of relation 0132 now must still (a, a, a, a)",
				createTuple("a", "a", "a", "a"), rel_0132.first());
	}
}
