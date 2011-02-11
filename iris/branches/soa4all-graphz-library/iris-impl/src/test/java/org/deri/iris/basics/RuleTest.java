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
import static org.deri.iris.factory.Factory.CONCRETE;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.MiscHelper;
import org.deri.iris.ObjectTests;
import org.deri.iris.api.basics.ILiteral;

/**
 * <p>
 * Tests for the rule.
 * </p>
 * <p>
 * $Id$
 * </p>
 *
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class RuleTest extends TestCase {

	private List<ILiteral> HEAD;

	private List<ILiteral> BODY;

	/**
	 * setup for Rule2Relation tests
	 */
	public void setUp() {
		HEAD = new ArrayList<ILiteral>();
		ILiteral literal = BASIC.createLiteral(true, 
				BASIC.createAtom(
					BASIC.createPredicate("sin", 1), 
					BASIC.createTuple(CONCRETE.createInteger(1))));
		HEAD.add(literal);
		HEAD.add(MiscHelper.createLiteral("cos", "X"));
		literal = BASIC.createLiteral(true, 
				BASIC.createAtom(
					BASIC.createPredicate("date", 3),
					BASIC.createTuple(CONCRETE.createInteger(2005), 
						CONCRETE.createInteger(12), 
						CONCRETE.createInteger(24))));
		HEAD.add(literal);

		BODY = new ArrayList<ILiteral>();
		literal = BASIC.createLiteral(true, 
				BASIC.createAtom(
					BASIC.createPredicate("sin", 1), 
					BASIC.createTuple(CONCRETE.createInteger(1))));
		BODY.add(literal);
		BODY.add(MiscHelper.createLiteral("cos", "X"));
		BODY.add(MiscHelper.createLiteral("date", "J", "K", "L"));
	}

	public static Test suite() {
		return new TestSuite(RuleTest.class, RuleTest.class.getSimpleName());
	}
	
	public void testEquals() {
		ObjectTests.runTestEquals(new Rule(HEAD, BODY), new Rule(HEAD, BODY),
				new Rule(HEAD, HEAD));
		ObjectTests.runTestEquals(new Rule(HEAD, BODY), new Rule(HEAD, BODY),
				new Rule(BODY, BODY));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Rule(HEAD, BODY), new Rule(HEAD, BODY));
	}

}
