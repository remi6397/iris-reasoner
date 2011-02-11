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
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.MiscHelper;
import org.deri.iris.ObjectTests;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   24.07.2006 14:19:17
 * 
 */
public class TupleTest extends TestCase {

	private static final int ARITY = 3;
	
	private static final ITuple REFERENCE = MiscHelper.createTuple("a", "b", "c");
	
	private static final ITuple TUPLE = MiscHelper.createTuple("a", "b", "c");
	
	private static final ITuple MORE = MiscHelper.createTuple("a", "b", "d");
	
	private static final ITuple EVENMORE = MiscHelper.createTuple("a", "b", "d", "e");

	public static Test suite() {
		return new TestSuite(TupleTest.class, TupleTest.class
				.getSimpleName());
	}

	public void testSize() {
		assertEquals("The size method doesn't work properly", ARITY, TUPLE.size());
	}

	public void testGet() {
		assertEquals("The get method doesn't work properly", TERM.createString("a"), TUPLE.get(0));
		assertEquals("The get method doesn't work properly", TERM.createString("b"), TUPLE.get(1));
		assertEquals("The get method doesn't work properly", TERM.createString("c"), TUPLE.get(2));
	}

	public void testEquals() {
		ObjectTests.runTestEquals(REFERENCE, TUPLE, MORE);
	}
	
	public void testCompareTo() {
		ObjectTests.runTestCompareTo(REFERENCE, TUPLE, MORE, EVENMORE);
	}
	
	public void testHashCode() {
		ObjectTests.runTestHashCode(REFERENCE, TUPLE);
	}
	
	public void testVariables() {
		Set<IVariable> variables = new HashSet<IVariable>();
		IVariable x = TERM.createVariable("X");
		IVariable y = TERM.createVariable("Y");
		
		variables.add(x);
		variables.add(y);
		
		IConstructedTerm c1 = TERM.createConstruct("c1", y);
		IConstructedTerm c2 = TERM.createConstruct("c2", c1, x);
		List<ITerm> terms = new ArrayList<ITerm>();
		terms.addAll(REFERENCE);
		terms.add(c2);
		
		assertEquals(variables, BASIC.createTuple(terms).getVariables());
	}
}
