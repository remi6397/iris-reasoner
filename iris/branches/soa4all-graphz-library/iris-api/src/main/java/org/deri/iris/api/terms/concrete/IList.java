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
import org.deri.iris.utils.equivalence.IEquivalentTerms;

public interface IList extends IConcreteTerm, java.util.List<IConcreteTerm> {

	/**
	 * Defines the RIF List URI.
	 */
	public static String DATATYPE_URI = "http://www.w3.org/2007/rif#List";

	/**
	 * Checks if this list equals the specified list taking into account the
	 * specified equivalent terms to determine the equality of the items of the
	 * lists.
	 * 
	 * @param obj
	 *            The object representing a list.
	 * @param equivalentTerms
	 *            The equivalent terms used to determine the equality of the
	 *            elements of the lists.
	 * @return Returns <code>true</code> if and only if the specified object is
	 *         also a list, both lists have the same size, and all corresponding
	 *         pairs of elements in the two lists are <i>equal</i> are
	 *         equivalent according to the specified equivalent terms.
	 */
	public boolean equals(Object obj, IEquivalentTerms equivalentTerms);

}
