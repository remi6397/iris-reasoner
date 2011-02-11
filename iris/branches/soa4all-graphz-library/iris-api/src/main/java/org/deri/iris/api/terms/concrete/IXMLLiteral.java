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
package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.IConcreteTerm;

/**
 * A term representing a rdf:XMLLiteral.
 */
public interface IXMLLiteral extends IConcreteTerm {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.deri.iris.api.terms.ITerm#getValue()
	 * 
	 * The first element is the string, the second element is the language
	 * identifier.
	 */
	public String[] getValue();

	/**
	 * Returns the represented XML element content as a string.
	 * 
	 * @return The represented XML element content as a string.
	 */
	public String getString();

	/**
	 * Returns the language identifier for this XML element, or
	 * <code>null</code> if not language identifier is defined. This string
	 * directly corresponds to the xml:lang attribute.
	 * 
	 * @return The language identifier for this XML element, or
	 *         <code>null</code> if not language identifier is defined.
	 */
	public String getLang();

}
