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

import org.deri.iris.api.terms.IConcreteTerm;
import org.deri.iris.api.terms.concrete.IToken;
import org.deri.iris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the Token data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class TokenTest extends AbstractConcreteTermTest {

	@Override
	protected IConcreteTerm createBasic() {
		return new Token("\nbla bla\tbla  \r     foo");
	}

	@Override
	protected String createBasicString() {
		return "bla blabla foo";
	}

	@Override
	protected IConcreteTerm createEqual() {
		return new Token("bla blabla foo");
	}

	@Override
	protected String createEqualString() {
		return "bla blabla foo";
	}

	@Override
	protected IConcreteTerm createGreater() {
		return new Token("lulz");
	}

	@Override
	protected String createGreaterString() {
		return "lulz";
	}

	@Override
	protected URI getDatatypeIRI() {
		return URI.create(IToken.DATATYPE_URI);
	}

}
