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
import org.deri.iris.api.operations.relation.IUnion;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.storage.Relation;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date 21.09.2006
 */
public class UnionTest extends TestCase {

	public static Test suite() {
		return new TestSuite(UnionTest.class, UnionTest.class.getSimpleName());
	}

	/**
	 * Joins two relations (no duplicates handling) and then checks the 
	 * result against the submitted Collection of tuples. 
	 * 
	 * @param i
	 *            the indexes on which to join (see documentation for
	 *            IJoin.join(..) for the computation of this array)
	 * @param e
	 *            the Collection of expected tuples
	 */
	protected static void runJoin(final Collection<ITuple> e) {
		IRelation<ITuple> relation0 = new Relation(3);
		IRelation<ITuple> relation1 = new Relation(3);
		IRelation<ITuple> relation2 = new Relation(3);
		
		// relation0: add tuples
		relation0.add(MiscHelper.createTuple("a", "b", "b"));
		relation0.add(MiscHelper.createTuple("a", "b", "d"));
		relation0.add(MiscHelper.createTuple("e", "e", "e"));
		relation0.add(MiscHelper.createTuple("h", "h", "h"));
		relation0.add(MiscHelper.createTuple("h", "g", "h"));
		
		relation0.add(MiscHelper.createTuple("a", "b", "i"));
		relation0.add(MiscHelper.createTuple("a", "b", "i"));
		
		relation0.add(MiscHelper.createTuple("f", "g", "k"));
		relation0.add(MiscHelper.createTuple("x", "x", "x"));
		relation0.add(MiscHelper.createTuple("a", "b", "c"));
		
		relation0.add(MiscHelper.createTuple("a", "b", "b"));
		
		relation0.add(MiscHelper.createTuple("f", "g", "h"));
		relation0.add(MiscHelper.createTuple("h", "g", "f"));
		relation0.add(MiscHelper.createTuple("h", "g", "a"));

		
		// relation1: add tuples
		relation1.add(MiscHelper.createTuple("c", "b", "b"));
		relation1.add(MiscHelper.createTuple("c", "b", "a"));
		relation1.add(MiscHelper.createTuple("a", "b", "c"));
		relation1.add(MiscHelper.createTuple("c", "b", "b"));
		
		relation1.add(MiscHelper.createTuple("c", "b", "a"));
		
		
		// relation2: add tuples
		relation2.add(MiscHelper.createTuple("w", "b", "b"));
		relation2.add(MiscHelper.createTuple("s", "b", "a"));
		relation2.add(MiscHelper.createTuple("x", "b", "c"));
		relation2.add(MiscHelper.createTuple("r", "b", "b"));
		relation2.add(MiscHelper.createTuple("f", "b", "a"));
		relation2.add(MiscHelper.createTuple("v", "b", "c"));
		
		IUnion unionOperator = RELATION.createUnionOperator(
				relation0, relation1, relation2);
		IRelation result = unionOperator.union();
		assertResults(result, e);
	}
	
	public void testUnion() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "d"));
		e.add(MiscHelper.createTuple("a", "b", "c"));
		e.add(MiscHelper.createTuple("c", "b", "b"));
		e.add(MiscHelper.createTuple("c", "b", "a"));
		e.add(MiscHelper.createTuple("e", "e", "e"));
		e.add(MiscHelper.createTuple("h", "h", "h"));
		e.add(MiscHelper.createTuple("h", "g", "h"));
		e.add(MiscHelper.createTuple("a", "b", "i"));
		e.add(MiscHelper.createTuple("f", "g", "k"));
		e.add(MiscHelper.createTuple("x", "x", "x"));
		e.add(MiscHelper.createTuple("f", "g", "h"));
		e.add(MiscHelper.createTuple("h", "g", "f"));
		e.add(MiscHelper.createTuple("h", "g", "a"));
		e.add(MiscHelper.createTuple("w", "b", "b"));
		e.add(MiscHelper.createTuple("s", "b", "a"));
		e.add(MiscHelper.createTuple("x", "b", "c"));
		e.add(MiscHelper.createTuple("r", "b", "b"));
		e.add(MiscHelper.createTuple("f", "b", "a"));
		e.add(MiscHelper.createTuple("v", "b", "c"));
		
		runJoin(e);
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

