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

/**
 * <p>
 * An interface which defines a term. A term is a name for an object
 * in the universe of discourse. There are three types of terms:
 * <ul>
 * <li> variables</li>
 * <li> constants</li>
 * <li> constructed terms (functional symbols)</li>
 * </ul>
 * </p>
 * <p>
 * By convention <code>null</code> is the smalles possible term of all types.
 * So if you compare a term using the compare method you will always recieve
 * a positive number.
 * </p>
 * <p>
 * $Id: ITerm.java,v 1.15 2007-10-15 15:20:38 bazbishop237 Exp $
 * </p>
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 */

public interface ITerm extends Comparable<ITerm>{

	/**
	 * Checks whether the term is ground (a term with no variables).
	 * 
	 * @return	<code>true</code> if the term is ground, 
	 * 			otherwise <code>false</code>.
	 */
	public boolean isGround();
	
	/**
	 * Returns a vale of the term.
	 * 
	 * @return	The term value.
	 */
	public Object getValue();
}
