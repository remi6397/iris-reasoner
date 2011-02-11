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

import java.net.URI;

import org.deri.iris.api.terms.IConcreteTerm;

/**
 * <p>
 * Represents the XML Schema datatype xsd:anyURI.
 * </p>
 * <p>
 * xsd:anyURI represents a Uniform Resource Identifier Reference (URI). An
 * xsd:anyURI value can be absolute or relative, and may have an optional
 * fragment identifier (i.e., it may be a URI Reference). This type should be
 * used to specify the intention that the value fulfills the role of a URI as
 * defined by RFC 2396, as amended by RFC 2732.
 * </p>
 * 
 * @author Adrian Marte
 */
public interface IAnyURI extends IConcreteTerm {

	/**
	 * Defines the XML Schema datatype URI.
	 */
	public static String DATATYPE_URI = "http://www.w3.org/2001/XMLSchema#anyURI";

	/**
	 * Returns the URI representing this anyURI.
	 * 
	 * @return The URI representing this anyURI.
	 */
	public URI getValue();

}
