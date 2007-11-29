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
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import junit.framework.TestCase;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IRelation;

/**
 * <p>
 * Provides generic tests which should be passed by all relation
 * implementations. All tests will be done with tuples of arity 4.
 * </p>
 * <p>
 * $Id: AbstractRelationTest.java,v 1.1 2007-07-17 10:12:56 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.1 $
 */
public abstract class AbstractRelationTest<Type extends IRelation> extends
		TestCase {

	private static final ITuple[] allTup = new ITuple[] {
		createTuple("a", "a", "a", "a"),
		createTuple("b", "b", "b", "b"),
		createTuple("c", "c", "c", "c"),
		createTuple("d", "d", "d", "d"),
		createTuple("e", "e", "e", "e"),
		createTuple("f", "f", "f", "f"),
		createTuple("g", "g", "g", "g"),
		createTuple("h", "h", "h", "h"),
		createTuple("i", "i", "i", "i"),
		createTuple("j", "j", "j", "j"),
		createTuple("k", "k", "k", "k"),
		createTuple("l", "l", "l", "l"),
		createTuple("m", "m", "m", "m") };

	protected Type r;

	/**
	 * <p>
	 * In this method the field &quot;{@code r}&quot; of
	 * {@code AbstractRelationTest} must be set. Note: all tests will be done
	 * with tuples of arity 4.
	 * </p>
	 * <p>
	 * e.g.: <code>r = new IndexingOnTheFlyRelation()</code>
	 * </p>
	 */
	public abstract void setUp();

	public void testPutGetSingle() {
		for (final ITuple t : allTup) {
			assertTrue("Adding of " + t + " to r must return true", r.add(t));
		}
		assertEquals(
				"The size of the array and the stored tuples mut be equal",
				allTup.length, r.size());
		for (final ITuple t : allTup) {
			assertTrue("Couldn't find the tuple " + t, r.contains(t));
		}
	}

	public void testPutGetAll() {
		assertTrue("Adding of tuples must return true", r.addAll(Arrays.asList(allTup)));
		assertEquals(
				"The size of the array and the stored tuples mut be equal",
				allTup.length, r.size());
		for (final ITuple t : allTup) {
			assertTrue("Couldn't find the tuple " + t, r.contains(t));
		}
	}

	public void testRemoveSingle() {
		r.addAll(Arrays.asList(allTup));

		final ITuple cccc = createTuple("c", "c", "c", "c");
		final Set<ITuple> remaining = new HashSet<ITuple>(Arrays.asList(allTup));
		remaining.remove(cccc);
		assertTrue("Removing of " + cccc + " should return true", r.remove(cccc));
		assertEquals("Only " + cccc + " should be removed", remaining, r);
	}

	public void testRemoveAll() {
		r.addAll(Arrays.asList(allTup));

		final Set<ITuple> toRemove = new HashSet<ITuple>(Arrays.asList(allTup));
		toRemove.add(createTuple("a", "a", "a", "a"));
		toRemove.add(createTuple("c", "c", "c", "c"));
		toRemove.add(createTuple("e", "e", "e", "e"));
		toRemove.add(createTuple("g", "g", "g", "g"));
		final Set<ITuple> remaining = new HashSet<ITuple>(Arrays.asList(allTup));
		remaining.removeAll(toRemove);
		assertTrue("Removing of " + toRemove + " should return true", r.removeAll(toRemove));
		assertEquals("Only " + toRemove + " should be removed", remaining, r);
	}

	public void testTailSet() {
		r.addAll(Arrays.asList(allTup));
		final Set<ITuple> tailRef = new HashSet<ITuple>();
		tailRef.add(createTuple("k", "k", "k", "k"));
		tailRef.add(createTuple("l", "l", "l", "l"));
		tailRef.add(createTuple("m", "m", "m", "m"));
		final Set<ITuple> tail = new HashSet<ITuple>(r.tailSet(createTuple("k", "k", "k", "k")));
		assertEquals("The tails must be equal", tailRef, tail);
	}

	public void testHeadSet() {
		r.addAll(Arrays.asList(allTup));
		final Set<ITuple> headRef = new HashSet<ITuple>();
		headRef.add(createTuple("a", "a", "a", "a"));
		headRef.add(createTuple("b", "b", "b", "b"));
		headRef.add(createTuple("c", "c", "c", "c"));
		final Set<ITuple> head = new HashSet<ITuple>(r.headSet(createTuple("d", "d", "d", "d")));
		assertEquals("The heads must be equal", headRef, head);
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

		final SortedSet<ITuple> rel_0132 = r.indexOn(new int[] { 0, 1, 3, 2 });
		final ITuple abkl = createTuple("a", "b", "k", "l");
		assertEquals("The first tuple of relation 0132 must be " + abkl, abkl, rel_0132.first());

		// test whether the order of the previous set remains the same
		final SortedSet<ITuple> rel_3412 = r.indexOn(new int[] { 3, 4, 1, 2 });
		final ITuple xyab = createTuple("x", "y", "a", "b");
		assertEquals("The first tuple of relation 0132 must be " + xyab, xyab, rel_3412.first());
		assertEquals("The first tuple of relation 0132 must be still " + abkl, abkl, rel_0132.first());
	}

	public void testIndexOnAdd() {
		r.addAll(Arrays.asList(allTup));

		final SortedSet<ITuple> rel_0123 = r.indexOn(new int[] { 0, 1, 2, 3 });
		final SortedSet<ITuple> rel_3210 = r.indexOn(new int[] { 3, 2, 1, 0 });

		// test whether adding to r would have an effect to the index
		// relations
		final ITuple abbb = createTuple("a", "b", "b", "b");
		assertTrue("Adding of " + abbb + " must return true", r.add(abbb));
		assertTrue("Couldn't find " + abbb + " again in r", r.contains(abbb));
		assertTrue("Couldn't find " + abbb + " again in rel_0132", rel_0123.contains(abbb));
		assertTrue("Couldn't find " + abbb + " again in rel_3412", rel_3210.contains(abbb));

		// test whether adding to an index relation would have an affect
		// on the other relations
		final ITuple accc = createTuple("a", "c", "c", "c");
		assertTrue("Adding of " + accc + " must return true", rel_0123.add(accc));
		assertTrue("Couldn't find " + accc + " again in r", r.contains(accc));
		assertTrue("Couldn't find " + accc + " again in rel_0132", rel_0123.contains(accc));
		assertTrue("Couldn't find " + accc + " again in rel_3412", rel_3210.contains(accc));
	}

	public void testIndexOnRemove() {
		r.addAll(Arrays.asList(allTup));

		final SortedSet<ITuple> rel_0123 = r.indexOn(new int[] { 0, 1, 2, 3 });
		final SortedSet<ITuple> rel_3210 = r.indexOn(new int[] { 3, 2, 1, 0 });

		// test whether removing to r would have an effect to the index
		// relations
		final ITuple aaaa = createTuple("a", "a", "a", "a");
		assertTrue("Removing of " + aaaa + " must return true", r.remove(aaaa));
		assertFalse("Could find " + aaaa + " again in r", r.contains(aaaa));
		assertFalse("Could find " + aaaa + " again in rel_0132", rel_0123.contains(aaaa));
		assertFalse("Could find " + aaaa + " again in rel_3412", rel_3210.contains(aaaa));

		// test whether removing to an index relation would have an affect
		// on the other relations
		final ITuple bbbb = createTuple("b", "b", "b", "b");
		assertTrue("Removing of " + bbbb + " must return true", rel_0123.remove(bbbb));
		assertFalse("Could find " + bbbb + " again in r", r.contains(bbbb));
		assertFalse("Could find " + bbbb + " again in rel_0132", rel_0123.contains(bbbb));
		assertFalse("Could find " + bbbb + " again in rel_3412", rel_3210.contains(bbbb));
	}

	public void testHeadSetAdd() {
		r.addAll(Arrays.asList(allTup));

		final SortedSet<ITuple> rel_eeee = r.headSet(createTuple("e", "e", "e", "e"));
		final SortedSet<ITuple> rel_ffff = r.headSet(createTuple("f", "f", "f", "f"));

		final ITuple abbb = createTuple("a", "b", "b", "b");
		assertTrue("The adding of " + abbb + " to r should return true", r.add(abbb));
		assertTrue("Couldn't find " + abbb + " again in r", r.contains(abbb));
		assertTrue("Couldn't find " + abbb + " again in rel_eeee", rel_eeee.contains(abbb));
		assertTrue("Couldn't find " + abbb + " again in rel_ffff", rel_ffff.contains(abbb));

		final ITuple accc = createTuple("a", "c", "c", "c");
		assertTrue("The adding of " + accc + " to rel_eeee should return true", rel_eeee.add(accc));
		assertTrue("Couldn't find " + accc + " again in r", r.contains(accc));
		assertTrue("Couldn't find " + accc + " again in rel_eeee", rel_eeee.contains(accc));
		assertTrue("Couldn't find " + accc + " again in rel_ffff", rel_ffff.contains(accc));

		final SortedSet<ITuple> rel_sub_bbbb = rel_eeee.headSet(createTuple("b", "b", "b", "b"));
		final ITuple addd = createTuple("a", "d", "d", "d");
		assertTrue("The adding of " + addd + " to r should return true", r.add(addd));
		assertTrue("Couldn't find " + addd + " again in r", r.contains(addd));
		assertTrue("Couldn't find " + addd + " again in rel_eeee", rel_eeee.contains(addd));
		assertTrue("Couldn't find " + addd + " again in rel_ffff", rel_ffff.contains(addd));
		assertTrue("Couldn't find " + addd + " again in rel_sub_bbbb", rel_sub_bbbb.contains(addd));

		final ITuple aeee = createTuple("a", "e", "e", "e");
		assertTrue("The adding of " + aeee + " to rel_sub_bbbb should return true", rel_sub_bbbb.add(aeee));
		assertTrue("Couldn't find " + aeee + " again in r", r.contains(aeee));
		assertTrue("Couldn't find " + aeee + " again in rel_eeee", rel_eeee.contains(aeee));
		assertTrue("Couldn't find " + aeee + " again in rel_ffff", rel_ffff.contains(aeee));
		assertTrue("Couldn't find " + aeee + " again in rel_sub_bbbb", rel_sub_bbbb.contains(aeee));

		// testing the adding of at tuple wich should be only visible
		// for a certain set of subrelations
		final ITuple ezzz = createTuple("e", "z", "z", "z");
		assertTrue("The adding of " + ezzz + " to r should return true", r.add(ezzz));
		assertTrue("Couldn't find " + ezzz + " again in r", r.contains(ezzz));
		assertFalse("Could find " + ezzz + " again in rel_eeee", rel_eeee.contains(ezzz));
		assertTrue("Couldn't find " + ezzz + " again in rel_ffff", rel_ffff.contains(ezzz));
		assertFalse("Could find " + ezzz + " again in rel_sub_bbbb", rel_sub_bbbb.contains(ezzz));

		// testing the adding of a tuple out of range
		final ITuple zfff = createTuple("z", "f", "f", "f");
		boolean exceptionThrown = false;
		try {
			rel_sub_bbbb.add(zfff);
		} catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue("It must not be possible to add " + zfff + " to a headset from " + 
				createTuple("b", "b", "b", "b"), exceptionThrown); 
	}

	public void testTailSetAdd() {
		r.addAll(Arrays.asList(allTup));

		final SortedSet<ITuple> rel_cccc = r.tailSet(createTuple("c", "c", "c", "c"));
		final SortedSet<ITuple> rel_dddd = r.tailSet(createTuple("d", "d", "d", "d"));

		final ITuple zaaa = createTuple("z", "a", "a", "a");
		assertTrue("The adding of " + zaaa + " to r should return true", r.add(zaaa));
		assertTrue("Couldn't find " + zaaa + " again in r", r.contains(zaaa));
		assertTrue("Couldn't find " + zaaa + " again in rel_cccc", rel_cccc.contains(zaaa));
		assertTrue("Couldn't find " + zaaa + " again in rel_dddd", rel_dddd.contains(zaaa));

		final ITuple zbbb = createTuple("z", "b", "b", "b");
		assertTrue("The adding of " + zbbb + " to rel_cccc should return true", rel_cccc.add(zbbb));
		assertTrue("Couldn't find " + zbbb + " again in r", r.contains(zbbb));
		assertTrue("Couldn't find " + zbbb + " again in rel_cccc", rel_cccc.contains(zbbb));
		assertTrue("Couldn't find " + zbbb + " again in rel_dddd", rel_dddd.contains(zbbb));

		final SortedSet<ITuple> rel_sub_gggg = rel_cccc.tailSet(createTuple("g", "g", "g", "g"));
		final ITuple zccc = createTuple("z", "c", "c", "c");
		assertTrue("The adding of " + zccc + " to r should return true", r.add(zccc));
		assertTrue("Couldn't find " + zccc + " again in r", r.contains(zccc));
		assertTrue("Couldn't find " + zccc + " again in rel_cccc", rel_cccc.contains(zccc));
		assertTrue("Couldn't find " + zccc + " again in rel_dddd", rel_dddd.contains(zccc));
		assertTrue("Couldn't find " + zccc + " again in rel_sub_gggg", rel_dddd.contains(zccc));

		final ITuple zddd = createTuple("z", "d", "d", "d");
		assertTrue("The adding of " + zddd + " to rel_sub_gggg should return true", rel_sub_gggg.add(zddd));
		assertTrue("Couldn't find " + zddd + " again in r", r.contains(zddd));
		assertTrue("Couldn't find " + zddd + " again in rel_cccc", rel_cccc.contains(zddd));
		assertTrue("Couldn't find " + zddd + " again in rel_dddd", rel_dddd.contains(zddd));
		assertTrue("Couldn't find " + zddd + " again in rel_sub_gggg", rel_sub_gggg.contains(zddd));

		// testing the adding of at tuple wich should be only visible
		// for a certain set of subrelations
		final ITuple czzz = createTuple("c", "z", "z", "z");
		assertTrue("The adding of " + czzz + " to r should return true", r.add(czzz));
		assertTrue("Couldn't find " + czzz + " again in r", r.contains(czzz));
		assertTrue("Couldn't find " + czzz + " again in rel_cccc", rel_cccc.contains(czzz));
		assertFalse("Could find " + czzz + " again in rel_dddd", rel_dddd.contains(czzz));
		assertFalse("Could find " + czzz + " again in rel_sub_gggg", rel_sub_gggg.contains(czzz));

		// testing the adding of a tuple out of range
		final ITuple aaaa = createTuple("a", "a", "a", "a");
		boolean exceptionThrown = false;
		try {
			rel_sub_gggg.add(aaaa);
		} catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue("It must not be possible to add " + aaaa + " to a tailset to " + 
				createTuple("g", "g", "g", "g"), exceptionThrown); 
	}

	public void testSubSetAdd() {
		r.addAll(Arrays.asList(allTup));

		final SortedSet<ITuple> rel_cccc_eeee = r.subSet(createTuple("c", "c", "c", "c"), 
				createTuple("e", "e", "e", "e"));
		final SortedSet<ITuple> rel_bbbb_ffff = r.subSet(createTuple("b", "b", "b", "b"), 
				createTuple("f", "f", "f", "f"));

		final ITuple dbbb = createTuple("d", "b", "b", "b");
		assertTrue("The adding of " + dbbb + " to r should return true", r.add(dbbb));
		assertTrue("Couldn't find " + dbbb + " again in r", r.contains(dbbb));
		assertTrue("Couldn't find " + dbbb + " again in rel_cccc_eeee", rel_cccc_eeee.contains(dbbb));
		assertTrue("Couldn't find " + dbbb + " again in rel_bbbb_ffff", rel_bbbb_ffff.contains(dbbb));

		final ITuple dccc = createTuple("d", "c", "c", "c");
		assertTrue("The adding of " + dccc + " to rel_cccc_eeee should return true", rel_cccc_eeee.add(dccc));
		assertTrue("Couldn't find " + dccc + " again in r", r.contains(dccc));
		assertTrue("Couldn't find " + dccc + " again in rel_cccc_eeee", rel_cccc_eeee.contains(dccc));
		assertTrue("Couldn't find " + dccc + " again in rel_bbbb_ffff", rel_bbbb_ffff.contains(dccc));

		final SortedSet<ITuple> rel_sub_czzz = rel_cccc_eeee.subSet(createTuple("c", "z", "z", "z"), 
				createTuple("e", "e", "e", "e"));
		final ITuple ddde = createTuple("d", "d", "d", "e");
		assertTrue("The adding of " + ddde + " to r should return true", r.add(ddde));
		assertTrue("Couldn't find " + ddde + " again in r", r.contains(ddde));
		assertTrue("Couldn't find " + ddde + " again in rel_cccc_eeee", rel_cccc_eeee.contains(ddde));
		assertTrue("Couldn't find " + ddde + " again in rel_bbbb_ffff", rel_bbbb_ffff.contains(ddde));
		assertTrue("Couldn't find " + ddde + " again in rel_sub_bbbb", rel_sub_czzz.contains(ddde));

		final ITuple deee = createTuple("d", "e", "e", "e");
		assertTrue("The adding of " + deee + " to r should return true", rel_sub_czzz.add(deee));
		assertTrue("Couldn't find " + deee + " again in r", r.contains(deee));
		assertTrue("Couldn't find " + deee + " again in rel_cccc_eeee", rel_cccc_eeee.contains(deee));
		assertTrue("Couldn't find " + deee + " again in rel_bbbb_ffff", rel_bbbb_ffff.contains(deee));
		assertTrue("Couldn't find " + deee + " again in rel_sub_czzz", rel_sub_czzz.contains(deee));

		// testing the adding of at tuple wich should be only visible
		// for a certain set of subrelations
		final ITuple bzzz = createTuple("b", "z", "z", "z");
		assertTrue("The adding of " + bzzz + " to r should return true", r.add(bzzz));
		assertTrue("Couldn't find " + bzzz + " again in r", r.contains(bzzz));
		assertFalse("Could find " + bzzz + " again in rel_cccc_eeee", rel_cccc_eeee.contains(bzzz));
		assertTrue("Couldn't find " + bzzz + " again in rel_bbbb_ffff", rel_bbbb_ffff.contains(bzzz));
		assertFalse("Could find " + bzzz + " again in rel_sub_gggg", rel_sub_czzz.contains(bzzz));

		// testing the adding of a tuple out of range
		final ITuple zfff = createTuple("z", "f", "f", "f");
		boolean exceptionThrown = false;
		try {
			rel_sub_czzz.add(zfff);
		} catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue("It must not be possible to add " + zfff + " to a headset from " + 
				createTuple("b", "b", "b", "b"), exceptionThrown); 
	}

	public void testHeadSetRemove() {
		r.addAll(Arrays.asList(allTup));

		final SortedSet<ITuple> rel_gggg = r.headSet(createTuple("g", "g", "g", "g"));
		final SortedSet<ITuple> rel_hhhh = r.headSet(createTuple("h", "h", "h", "h"));

		final ITuple aaaa = createTuple("a", "a", "a", "a");
		assertTrue("The remove of " + aaaa + " from r should return true", r.remove(aaaa));
		assertFalse("Could find " + aaaa + " again in r", r.contains(aaaa));
		assertFalse("Could find " + aaaa + " again in rel_gggg", rel_gggg.contains(aaaa));
		assertFalse("Could find " + aaaa + " again in rel_hhhh", rel_hhhh.contains(aaaa));

		final ITuple bbbb = createTuple("b", "b", "b", "b");
		assertTrue("The remove of " + bbbb + " from rel_gggg should return true", rel_gggg.remove(bbbb));
		assertFalse("Could find " + bbbb + " again in r", r.contains(bbbb));
		assertFalse("Could find " + bbbb + " again in rel_gggg", rel_gggg.contains(bbbb));
		assertFalse("Could find " + bbbb + " again in rel_hhhh", rel_hhhh.contains(bbbb));

		final SortedSet<ITuple> rel_sub_ffff = rel_gggg.headSet(createTuple("f", "f", "f", "f"));
		final ITuple cccc = createTuple("c", "c", "c", "c");
		assertTrue("The remove of " + cccc + " from r should return true", r.remove(cccc));
		assertFalse("Could find " + cccc + " again in r", r.contains(cccc));
		assertFalse("Could find " + cccc + " again in rel_gggg", rel_gggg.contains(cccc));
		assertFalse("Could find " + cccc + " again in rel_hhhh", rel_hhhh.contains(cccc));
		assertFalse("Could find " + cccc + " again in rel_sub_bbbb", rel_sub_ffff.contains(cccc));

		final ITuple dddd = createTuple("d", "d", "d", "d");
		assertTrue("The remove of " + dddd + " from rel_sub_ffff should return true", rel_sub_ffff.remove(dddd));
		assertFalse("Could find " + dddd + " again in r", r.contains(dddd));
		assertFalse("Could find " + dddd + " again in rel_gggg", rel_gggg.contains(dddd));
		assertFalse("Could find " + dddd + " again in rel_hhhh", rel_hhhh.contains(dddd));
		assertFalse("Could find " + dddd + " again in rel_sub_ffff", rel_sub_ffff.contains(dddd));

		// test removing from the iterator
		final ITuple eeee = createTuple("e", "e", "e", "e");
		final Iterator<ITuple> i = rel_sub_ffff.iterator();
		assertTrue("There must be at least one element in rel_sub_ffff", i.hasNext());
		i.next();
		i.remove();
		assertFalse("Could find " + eeee + " again in r", r.contains(eeee));
		assertFalse("Could find " + eeee + " again in rel_gggg", rel_gggg.contains(eeee));
		assertFalse("Could find " + eeee + " again in rel_hhhh", rel_hhhh.contains(eeee));
		assertFalse("Could find " + eeee + " again in rel_sub_ffff", rel_sub_ffff.contains(eeee));
	}

	public void testTailSetRemove() {
		r.addAll(Arrays.asList(allTup));

		final SortedSet<ITuple> rel_bbbb = r.tailSet(createTuple("b", "b", "b", "b"));
		final SortedSet<ITuple> rel_cccc = r.tailSet(createTuple("c", "c", "c", "c"));

		final ITuple cccc = createTuple("c", "c", "c", "c");
		assertTrue("The remove of " + cccc + " from r should return true", r.remove(cccc));
		assertFalse("Could find " + cccc + " again in r", r.contains(cccc));
		assertFalse("Could find " + cccc + " again in rel_bbbb", rel_bbbb.contains(cccc));
		assertFalse("Could find " + cccc + " again in rel_cccc", rel_cccc.contains(cccc));

		final ITuple dddd = createTuple("d", "d", "d", "d");
		assertTrue("The remove of " + dddd + " from rel_bbbb should return true", rel_bbbb.remove(dddd));
		assertFalse("Could find " + dddd + " again in r", r.contains(dddd));
		assertFalse("Could find " + dddd + " again in rel_bbbb", rel_bbbb.contains(dddd));
		assertFalse("Could find " + dddd + " again in rel_cccc", rel_cccc.contains(dddd));

		final SortedSet<ITuple> rel_sub_eeee = rel_bbbb.tailSet(createTuple("e", "e", "e", "e"));
		final ITuple eeee = createTuple("e", "e", "e", "e");
		assertTrue("The remove of " + eeee + " from r should return true", r.remove(eeee));
		assertFalse("Could find " + eeee + " again in r", r.contains(eeee));
		assertFalse("Could find " + eeee + " again in rel_bbbb", rel_bbbb.contains(eeee));
		assertFalse("Could find " + eeee + " again in rel_cccc", rel_cccc.contains(eeee));
		assertFalse("Could find " + eeee + " again in rel_sub_eeee", rel_sub_eeee.contains(eeee));

		final ITuple ffff = createTuple("f", "f", "f", "f");
		assertTrue("The remove of " + ffff + " from rel_sub_eeee should return true", rel_sub_eeee.remove(ffff));
		assertFalse("Could find " + ffff + " again in r", r.contains(ffff));
		assertFalse("Could find " + ffff + " again in rel_bbbb", rel_bbbb.contains(ffff));
		assertFalse("Could find " + ffff + " again in rel_cccc", rel_cccc.contains(ffff));
		assertFalse("Could find " + ffff + " again in rel_sub_eeee", rel_sub_eeee.contains(ffff));

		// test removing from the iterator
		final ITuple gggg = createTuple("g", "g", "g", "g");
		final Iterator<ITuple> i = rel_sub_eeee.iterator();
		assertTrue("There must be at least one element in rel_sub_ffffe", i.hasNext());
		i.next();
		i.remove();
		assertFalse("Could find " + gggg + " again in r", r.contains(gggg));
		assertFalse("Could find " + gggg + " again in rel_bbbb", rel_bbbb.contains(gggg));
		assertFalse("Could find " + gggg + " again in rel_cccc", rel_cccc.contains(gggg));
		assertFalse("Could find " + gggg + " again in rel_sub_eeee", rel_sub_eeee.contains(gggg));
	}

	public void testSubSetRemove() {
		r.addAll(Arrays.asList(allTup));

		final SortedSet<ITuple> rel_cccc_kkkk = r.subSet(createTuple("c", "c", "c", "c"), createTuple("k", "k", "k", "k"));
		final SortedSet<ITuple> rel_dddd_jjjj = r.subSet(createTuple("d", "d", "d", "d"), createTuple("j", "j", "j", "j"));

		final ITuple eeee = createTuple("e", "e", "e", "e");
		assertTrue("The remove of " + eeee + " from r should return true", r.remove(eeee));
		assertFalse("Could find " + eeee + " again in r", r.contains(eeee));
		assertFalse("Could find " + eeee + " again in rel_cccc_kkkk", rel_cccc_kkkk.contains(eeee));
		assertFalse("Could find " + eeee + " again in rel_dddd_jjjj", rel_dddd_jjjj.contains(eeee));

		final ITuple ffff = createTuple("f", "f", "f", "f");
		assertTrue("The remove of " + ffff + " from rel_cccc_kkkk should return true", rel_cccc_kkkk.remove(ffff));
		assertFalse("Could find " + ffff + " again in r", r.contains(ffff));
		assertFalse("Could find " + ffff + " again in rel_cccc_kkkk", rel_cccc_kkkk.contains(ffff));
		assertFalse("Could find " + ffff + " again in rel_dddd_jjjj", rel_dddd_jjjj.contains(ffff));

		final SortedSet<ITuple> rel_sub_gggg = rel_cccc_kkkk.subSet(createTuple("g", "g", "g", "g"), createTuple("j", "j", "j", "j"));
		final ITuple gggg = createTuple("g", "g", "g", "g");
		assertTrue("The remove of " + gggg + " from r should return true", r.remove(gggg));
		assertFalse("Could find " + gggg + " again in r", r.contains(gggg));
		assertFalse("Could find " + gggg + " again in rel_cccc_kkkk", rel_cccc_kkkk.contains(gggg));
		assertFalse("Could find " + gggg + " again in rel_dddd_jjjj", rel_dddd_jjjj.contains(gggg));
		assertFalse("Could find " + gggg + " again in rel_sub_ffff", rel_sub_gggg.contains(gggg));

		final ITuple hhhh = createTuple("h", "h", "h", "h");
		assertTrue("The remove of " + hhhh + " from rel_sub_gggg should return true", rel_sub_gggg.remove(hhhh));
		assertFalse("Could find " + hhhh + " again in r", r.contains(hhhh));
		assertFalse("Could find " + hhhh + " again in rel_cccc_kkkk", rel_cccc_kkkk.contains(hhhh));
		assertFalse("Could find " + hhhh + " again in rel_dddd_jjjj", rel_dddd_jjjj.contains(hhhh));
		assertFalse("Could find " + hhhh + " again in rel_sub_gggg", rel_sub_gggg.contains(hhhh));

		// test removing from the iterator
		final ITuple iiii = createTuple("i", "i", "i", "i");
		final Iterator<ITuple> i = rel_sub_gggg.iterator();
		assertTrue("There must be at least one element in rel_sub_ffffe", i.hasNext());
		i.next();
		i.remove();
		assertFalse("Could find " + iiii + " again in r", r.contains(iiii));
		assertFalse("Could find " + iiii + " again in rel_cccc_kkkk", rel_cccc_kkkk.contains(iiii));
		assertFalse("Could find " + iiii + " again in rel_dddd_jjjj", rel_dddd_jjjj.contains(iiii));
		assertFalse("Could find " + iiii + " again in rel_sub_gggg", rel_sub_gggg.contains(iiii));
	}

	public void testIterator() {
		r.addAll(Arrays.asList(allTup));
		
		// testing the size of the iterator and whether the iterator is
		// modifiable
		int count = 0;
		for (final Iterator<ITuple> i = r.iterator(); i.hasNext(); ) {
			i.next();
			i.remove();
			count++;
		}
		assertEquals("The number of tuples in the relation must be the same as in the iterator", allTup.length, count);
		
		r.addAll(Arrays.asList(allTup));
		// testing whether only the wanted tuple will be deleted
		final ITuple dddd = createTuple("d", "d", "d", "d");
		for (final Iterator<ITuple> i = r.iterator(); i.hasNext(); ) {
			if (i.next().equals(dddd)) {
				i.remove();
				break;
			}
		}
		assertEquals("Only one tuple should be deleted, but where " + (allTup.length - r.size()), allTup.length - 1, r.size());
		assertFalse(dddd + " must not be found in r again", r.contains(dddd));
	}
}
