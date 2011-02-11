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

import java.net.URI;

import junit.framework.TestCase;

import org.deri.iris.ObjectTests;
import org.deri.iris.api.terms.concrete.IAnyURI;

/**
 * <p>
 * Test the implementation of the AnyURI data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class AnyURITest extends TestCase {

	private static final String URI1_STRING = "http://www.sti-innsbruck.at";
	
	private static final URI URI1 = URI.create(URI1_STRING);
	
	private static final String URI2_STRING = "http://www.sti2.at";
	
	private static final URI URI2 = URI.create(URI2_STRING);

	public void testBasic() {
		IAnyURI anyUri = new AnyURI(URI1);
		
		assertEquals(URI1, anyUri.getValue());
	}

	public void testToCanonicalString() {
		IAnyURI anyUri = new AnyURI(URI1);
		assertEquals(URI1_STRING, anyUri.toCanonicalString());
	}

	public void testCompareTo() {
		IAnyURI uri1 = new AnyURI(URI1);
		IAnyURI uri2 = new AnyURI(URI1);
		IAnyURI uri3 = new AnyURI(URI2);
		
		ObjectTests.runTestCompareTo(uri1, uri2, uri3);
	}

	public void testEquals() {
		IAnyURI uri1 = new AnyURI(URI1);
		IAnyURI uri2 = new AnyURI(URI2);
		IAnyURI uri3 = new AnyURI(URI2);

		ObjectTests.runTestEquals(uri2, uri3, uri1);
	}

	public void testHashCode() {
		IAnyURI uri1 = new AnyURI(URI1);
		IAnyURI uri2 = new AnyURI(URI1);

		ObjectTests.runTestHashCode(uri1, uri2);
	}

}
