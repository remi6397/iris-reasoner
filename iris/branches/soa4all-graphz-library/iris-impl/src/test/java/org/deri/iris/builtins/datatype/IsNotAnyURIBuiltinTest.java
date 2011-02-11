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
package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.ITerm;

public class IsNotAnyURIBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotAnyURIBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, URISyntaxException {

		String iri = "http://www.w3.org/2001/XMLSchema#AnyURI";
		String builtinName = IsNotAnyURIBuiltin.class.getName();
		ITerm term = CONCRETE.createAnyURI(new URI("http://sti.uri.at"));

		checkBuiltin(iri, term, builtinName);
	}
}
