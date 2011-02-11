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
package org.deri.iris.api.basics;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * Represents a tuple. A tuple is a list of terms which represents a record in a
 * relation.
 * </p>
 * <p>
 * $Id: ITuple.java,v 1.14 2007-10-19 07:37:15 poettler_ric Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.14 $
 */

public interface ITuple extends List<ITerm>, Comparable<ITuple> {

	/**
	 * Checks whether this tuple contains only ground terms.
	 * 
	 * @return <code>true</code> if all terms of this tuple are grounded;
	 *         <code>false</code> otherwise.
	 */
	public boolean isGround();

	/**
	 * Returns all distinct variables from this tupple.
	 * 
	 * @return All distinct variables from this tupple.
	 */
	public Set<IVariable> getVariables();
	
	/**
	 * Returns all variables from this tupple.
	 * 
	 * @return All variables from this tupple.
	 */
	public List<IVariable> getAllVariables();

	/**
	 * Creates a new tupel with the tuples of <code>this</code> one and
	 * appends the tuples of the submitted list at the end.
	 * @param t the tuples to add
	 * @return the newly created tuple
	 * @throws IllegalArgumentException if the list is <code>null</code>
	 */
	public ITuple append(final Collection<? extends ITerm> t);
}
