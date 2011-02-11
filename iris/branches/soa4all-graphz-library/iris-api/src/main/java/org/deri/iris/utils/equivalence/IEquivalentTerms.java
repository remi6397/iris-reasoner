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
package org.deri.iris.utils.equivalence;

import java.util.Set;

import org.deri.iris.api.terms.ITerm;

/**
 * Defines equivalence between terms.
 * 
 * @author Adrian Marte
 */
public interface IEquivalentTerms {

	/**
	 * Checks if the specified terms are equivalent.
	 * 
	 * @param x The first term.
	 * @param y The second term.
	 * @return <code>true</code> if the two given terms are equivalent,
	 *         <code>false</code> otherwise.
	 */
	public boolean areEquivalent(ITerm x, ITerm y);

	/**
	 * Defines the specified terms as equivalent.
	 * 
	 * @param x The first term.
	 * @param y The second term.
	 */
	public void setEquivalent(ITerm x, ITerm y);

	/**
	 * Returns a representative term for the specified terms. If two terms are
	 * equivalent, they have the same representative term.
	 * 
	 * @param term The term.
	 * @return The representative term for the specified term.
	 */
	public ITerm findRepresentative(ITerm term);

	/**
	 * Returns the set of terms which are equivalent to the specified term. The
	 * set also contains the term itself.
	 * 
	 * @param term The term.
	 * @return The set of terms which are equivalent to the specified term. The
	 *         set also contains the term itself.
	 */
	public Set<ITerm> getEquivalent(ITerm term);

}
