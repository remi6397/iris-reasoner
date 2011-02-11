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
 * Represents the rdf:PlainLiteral data type, formerly known as rdf:text.
 * rdf:PlainLiteral is an internationalized string value that contains a
 * language tag indicating it's spoken language, e.g. "Padre de familia@es".
 */
public interface IPlainLiteral extends IConcreteTerm {

	/**
	 * Returns the wrapped type. The first element of this array is the string
	 * and the second is the language.
	 * 
	 * @return The wrapped type.
	 */
	public String[] getValue();

	/**
	 * Returns the string, e.g. "Padre de familia", if this text represents
	 * "Padre de familia@es".
	 * 
	 * @return The text.
	 */
	public String getString();

	/**
	 * Returns the language tag, e.g. "es", if this text represents
	 * "Padre de familia@es".
	 * 
	 * @return The language tag.
	 */
	public String getLang();

}
