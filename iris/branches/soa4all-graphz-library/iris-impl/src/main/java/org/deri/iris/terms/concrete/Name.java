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

import org.deri.iris.api.terms.concrete.IName;

/**
 * <p>
 * A simple implementation of Name.
 * </p>
 * 
 * @author Adrian Marte
 */
public class Name extends Token implements IName {

	/**
	 * Creates a new Name instance for the specified name. Does not check for
	 * validity of the specified name.
	 * 
	 * @param name The string representing a valid name.
	 */
	public Name(String name) {
		super(name);
	}

	@Override
	public URI getDatatypeIRI() {
		return URI.create(IName.DATATYPE_URI);
	}

}
