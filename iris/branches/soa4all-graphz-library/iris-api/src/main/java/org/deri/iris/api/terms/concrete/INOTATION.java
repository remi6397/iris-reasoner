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
 * <p>
 * Represents the XML Schema datatype xsd:NOTATION.
 * </p>
 * <p>
 * According to the XML Schema specificaiton, the value space of xsd:NOTATION is
 * the set of QNames of notations declared in a schema.
 * </p>
 * 
 * @author Adrian Marte
 */
public interface INOTATION extends IConcreteTerm {

	/**
	 * Defines the XML Schema datatype URI.
	 */
	public static String DATATYPE_URI = "http://www.w3.org/2001/XMLSchema#NOTATION";

	/**
	 * Returns the namespace name of this NOTATION.
	 * 
	 * @return The namespace name of this NOTATION.
	 */
	public String getNamespaceName();

	/**
	 * Returns the local part of this NOTATION.
	 * 
	 * @return The local part of this NOTATION.
	 */
	public String getLocalPart();

	/**
	 * Returns an array containing the namespace name (first element) and the
	 * local part (second element).
	 * 
	 * @return An array containing the namespace name (first element) and the
	 *         local part (second element).
	 */
	public String[] getValue();
}
