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

import java.util.Arrays;
import java.util.Collection;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IUnion;
import org.deri.iris.api.storage_old.IMixedDatatypeRelation;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date 21.09.2006
 */
public class UnionTest extends TestCase {

	private static final ITuple[] tups_0 = new ITuple[]{
		BASIC.createTuple(CONCRETE.createInteger(1), TERM.createString("b")), 
		BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createIri("http://www.google.com")), 
		BASIC.createTuple(CONCRETE.createInteger(2), TERM.createString("a")), 
	};
	
	private static final ITuple[] tups_1 = new ITuple[]{
		BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createInteger(3)), 
		BASIC.createTuple(TERM.createString("zzzzab"), CONCRETE.createInteger(4)),
		BASIC.createTuple(TERM.createString("aaa"), CONCRETE.createInteger(4)),
	};
	
	private static final ITuple[] tups_2 = new ITuple[]{
		BASIC.createTuple(CONCRETE.createInteger(4), CONCRETE.createInteger(4)),
		BASIC.createTuple(CONCRETE.createInteger(4), CONCRETE.createInteger(6))
	};
	
	private static final ITuple[] tups = new ITuple[]{
		BASIC.createTuple(CONCRETE.createInteger(1), TERM.createString("b")), 
		BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createIri("http://www.google.com")), 
		BASIC.createTuple(CONCRETE.createInteger(2), TERM.createString("a")), 
		BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createInteger(3)), 
		BASIC.createTuple(TERM.createString("zzzzab"), CONCRETE.createInteger(4)),
		BASIC.createTuple(TERM.createString("aaa"), CONCRETE.createInteger(4)),
		BASIC.createTuple(CONCRETE.createInteger(4), CONCRETE.createInteger(4)),
		BASIC.createTuple(CONCRETE.createInteger(4), CONCRETE.createInteger(6))
	};
	
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
	
	public void testUnion() {
		IMixedDatatypeRelation relation0 = RELATION.getMixedRelation(2);
		IMixedDatatypeRelation relation1 = RELATION.getMixedRelation(2);
		IMixedDatatypeRelation relation2 = RELATION.getMixedRelation(2);
		
		
		// relation0: add tuples
		relation0.addAll(Arrays.asList(tups_0));
		
		// relation1: add tuples
		relation1.addAll(Arrays.asList(tups_1));
		
		// relation2: add tuples
		relation2.addAll(Arrays.asList(tups_2));
		
		IUnion unionOperator = RELATION_OPERATION.createUnionOperator(
				relation0, relation1, relation2);
		IMixedDatatypeRelation result = (IMixedDatatypeRelation)unionOperator.union();
		assertResults(result, Arrays.asList(tups));
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
	protected static void assertResults(final IMixedDatatypeRelation r,
			final Collection<ITuple> e) {
		Assert.assertEquals("The length of relation and the list of"
				+ " expected tuples must be equal", e.size(), r.size());
		Assert.assertTrue("The relation must contain all expected tuples", r
				.containsAll(e));
	}
}

