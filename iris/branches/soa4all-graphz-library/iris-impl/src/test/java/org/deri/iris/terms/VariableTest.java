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
package org.deri.iris.terms;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTests;

/**
 * @author richi
 * 
 */
public class VariableTest extends TestCase {

	private static final String BASIC = "aaa";

	private static final String MORE = "aab";

	private static final String MORE1 = "aac";

	public static Test suite() {
		return new TestSuite(VariableTest.class, VariableTest.class
				.getSimpleName());
	}

//	public void testBasic() {
//		Variable basic = new Variable(BASIC);
//		Variable changed = new Variable(MORE);
//		changed.setName(BASIC);
//		assertEquals("object not initialized correctly", BASIC, basic
//				.getName());
//		assertEquals("setValue(..) doesn't work correctly", basic, changed);
//	}

	public void testEquals() {
		ObjectTests.runTestEquals(new Variable(BASIC), new Variable(BASIC),
				new Variable(MORE1));
	}

	public void testHashCode() {
		ObjectTests
				.runTestHashCode(new Variable(BASIC), new Variable(BASIC));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new Variable(BASIC),
				new Variable(BASIC), new Variable(MORE), new Variable(
						MORE1));
	}
}
