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
package org.deri.iris.terms.concrete;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTests;
import org.deri.iris.TermTests;
import org.deri.iris.api.terms.concrete.IIri;

public class SqNameTest extends TestCase {

	private static final String NAME = "sqName";

	private static final String NAMEMORE = "sqNbme";

	private static final String NAMEMORE1 = "sqNbmf";

	private static final String SPACE = "http://www.deri.org/reasoner";

	private static final IIri SPACEIRI = new Iri(SPACE);

	public void testBasic() {
		SqName fix = new SqName(SPACEIRI, NAME);
		SqName test = new SqName(SPACE + "#" + NAME);

		assertEquals("Something wrong whith instanciation", fix, test);
		assertEquals("Something wrong with name", NAME, test.getName());
		assertEquals("Something wrong with name", fix.getName(), test.getName());
		assertEquals("Something wrong with namespace", SPACEIRI, test
				.getNamespace());
		assertEquals("Something wrong with namespace", fix.getNamespace(), test
				.getNamespace());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new SqName(SPACE, NAME), new SqName(
				SPACE, NAME), new SqName(SPACE, NAMEMORE));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new SqName(SPACE, NAME),
				new SqName(SPACE, NAME), new SqName(SPACE, NAMEMORE),
				new SqName(SPACE, NAMEMORE1));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new SqName(SPACE, NAME), new SqName(
				SPACE, NAME));
	}

	public static Test suite() {
		return new TestSuite(SqNameTest.class, SqNameTest.class.getSimpleName());
	}
	
	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new SqName("", "a"));
	}
}
