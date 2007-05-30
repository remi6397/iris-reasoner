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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;

import java.util.Arrays;
import java.util.SortedSet;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ITuple;

/**
 * <p>
 * Tests the MixedDatatypeRelation relation.
 * </p>
 * <p>
 * Here we are only testing some special cases (handling of various datatypes), 
 * since the main functionality should be tested in the 
 * <code>GenericRelationTest</code>.
 * </p>
 * <p>
 * $Id: MixedDatatypeRelationTest.java,v 1.1 2007-05-30 08:51:53 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.1 $
 */
public class MixedDatatypeRelationTest extends
		GenericRelationTest<MixedDatatypeRelation> {

	private static final ITuple[] tups = new ITuple[]{
		BASIC.createTuple(CONCRETE.createInteger(1), TERM.createString("b"), CONCRETE.createIri("http://aaa"), CONCRETE.createDouble(2d)), 
		BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createIri("http://www.google.com"), CONCRETE.createDouble(10d), CONCRETE.createInteger(8)), 
		BASIC.createTuple(CONCRETE.createInteger(2), TERM.createString("a"), CONCRETE.createIri("http://bbb"), CONCRETE.createDouble(1d)), 
		BASIC.createTuple(TERM.createString("asdf"), CONCRETE.createIri("http://www.zzz.com"), CONCRETE.createDouble(5d), CONCRETE.createInteger(10)), 
	};

	public static Test suite() {
		return new TestSuite(MixedDatatypeRelationTest.class,
				MixedDatatypeRelationTest.class.getSimpleName());
	}

	@Override
	public void setUp() {
		r = new MixedDatatypeRelation(4);
	}

	public void testMixedAdd() {
		// should work without an exception
		r.addAll(Arrays.asList(tups));
	}

	public void testTypedTailSet() {
		r.addAll(Arrays.asList(tups));

		// should contain both <int, string, iri, double> terms
		final SortedSet<ITuple> isid0 = r.tailSet(BASIC.createTuple(
					CONCRETE.createInteger(1), 
					TERM.createString("a"), 
					CONCRETE.createIri("http://aaa"), 
					CONCRETE.createDouble(100d)));
		assertEquals("The size of the isid0 set doesn't match", 2, isid0.size());
		assertTrue("The isid set must contain " + tups[0], isid0.contains(tups[0]));
		assertTrue("The isid set must contain " + tups[2], isid0.contains(tups[2]));

		// should contain only the last <int, string, iri, double> term
		final SortedSet<ITuple> isid1 = r.tailSet(BASIC.createTuple(
					CONCRETE.createInteger(2), 
					TERM.createString("a"), 
					CONCRETE.createIri("http://aaa"), 
					CONCRETE.createDouble(100d)));
		assertEquals("The size of the isid1 set doesn't match", 1, isid1.size());
		assertTrue("The isid set must contain " + tups[2], isid1.contains(tups[2]));
	}
}
