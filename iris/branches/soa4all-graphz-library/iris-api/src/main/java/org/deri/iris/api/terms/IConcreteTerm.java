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

package org.deri.iris.api.terms;

import java.net.URI;

/**
 * <p>
 * An interface representing a concrete term. A concrete term has a
 * corresponding data type URI. For instance, a term representing a double data
 * type should return the URI "http://www.w3.org/2001/XMLSchema#double".
 * </p>
 * <p>
 * Remark: IRIS supports data types according to the standard specification for
 * primitive XML Schema data types and additional data types required in RIF.
 * </p>
 * 
 * @see <a href="http://www.w3.org/TR/xmlschema-2/">XML Schema: Datatypes</a>
 * @see <a href="http://www.w3.org/2005/rules/wiki/DTB">RIF Datatypes and
 *      Built-Ins</a>
 */
public interface IConcreteTerm extends ITerm {

	/**
	 * Returns the fully qualified identifier for the data type corresponding to
	 * this term. For instance, a terms representing a double data type should
	 * return the URI "http://www.w3.org/2001/XMLSchema#double".
	 * 
	 * @return The fully qualified identifier for the data type corresponding to
	 *         this term.
	 */
	public URI getDatatypeIRI();

	/**
	 * Returns a canonical string representation of this term.
	 * 
	 * @return A canonical string representation of this term.
	 */
	public String toCanonicalString();

}
