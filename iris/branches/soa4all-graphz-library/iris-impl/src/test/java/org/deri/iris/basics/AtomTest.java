/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.basics;

import static org.deri.iris.factory.Factory.BASIC;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.MiscHelper;
import org.deri.iris.ObjectTests;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;

/**
 * <p>
 * Tests for the atom.
 * </p>
 * <p>
 * $Id$
 * </p>
 *
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class AtomTest extends TestCase {

	private static final int ARITY = 3;

	private static final String SYMBOL = "date";

	private static final ITuple TUPLE = MiscHelper.createTuple("a", "b", "c");
	
	private static final ITuple TUPLEMORE = MiscHelper.createTuple("a", "b", "d");
	
	private static final IPredicate PREDICATE = BASIC.createPredicate(SYMBOL,
			ARITY);

	private static final IPredicate PREDICATEMORE = BASIC.createPredicate(
			SYMBOL + 1, ARITY);

	public static Test suite() {
		return new TestSuite(AtomTest.class, AtomTest.class.getSimpleName());
	}

	public void testIsGround() {
		Atom REF = new Atom(PREDICATE, TUPLE);
		assertEquals("The isGround method doesn't work properly", true, REF.isGround());
	}

	public void testIsBuiltin() {
		Atom REF = new Atom(PREDICATE, TUPLE);
		assertEquals("The isBuiltin method doesn't work properly", false, REF.isBuiltin());
	}

	public void testGetTuple() {
		Atom REF = new Atom(PREDICATE, TUPLE);
		assertEquals("The getTuple method doesn't work properly", TUPLE, REF.getTuple());
	}

	public void testGetPredicate() {
		Atom REF = new Atom(PREDICATE, TUPLE);
		assertEquals("The getPredicate method doesn't work properly", PREDICATE, REF.getPredicate());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new Atom(PREDICATE, TUPLE), new Atom(
				PREDICATE, TUPLE), new Atom(PREDICATE, TUPLEMORE));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Atom(PREDICATE, TUPLE), new Atom(
				PREDICATE, TUPLE));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new Atom(PREDICATE, TUPLE), new Atom(
				PREDICATE, TUPLE), new Atom(PREDICATE, TUPLEMORE), new Atom(
				PREDICATEMORE, TUPLE));
	}
}
