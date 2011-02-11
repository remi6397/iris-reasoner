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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTests;

/**
 * @author richi
 * 
 * Revision 1.1  26.07.2006 12:09:22  Darko Anicic, DERI Innsbruck
 */
public class PredicateTest extends TestCase {

	private static final int ARITY = 3;

	private static final int ARITYMORE = 4;

	private static final String SYMBOL = "date";

	private static final String SYMBOLMORE = "date1";

	public static Test suite() {
		return new TestSuite(PredicateTest.class, PredicateTest.class
				.getSimpleName());
	}

	public void testBasic() {
		final Predicate REFERENCE = new Predicate(SYMBOL, ARITY);
		
		assertEquals("getPredicateSymbol doesn't work properly", SYMBOL,
				REFERENCE.getPredicateSymbol());
		assertEquals("getArity doesn't work properly", ARITY, REFERENCE
				.getArity());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new Predicate(SYMBOL, ARITY), new Predicate(
				SYMBOL, ARITY), new Predicate(SYMBOL, ARITYMORE));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Predicate(SYMBOL, ARITY), new Predicate(
				SYMBOL, ARITY));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new Predicate(SYMBOL, ARITY),
				new Predicate(SYMBOL, ARITY), new Predicate(SYMBOL, ARITYMORE),
				new Predicate(SYMBOLMORE, ARITY));
	}
}
