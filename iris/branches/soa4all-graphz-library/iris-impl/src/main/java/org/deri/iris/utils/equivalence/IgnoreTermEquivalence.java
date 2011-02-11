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

import java.util.Collections;
import java.util.Set;

import org.deri.iris.api.terms.ITerm;

/**
 * An ineffective term equivalence implementation, i.e. this term equivalence
 * relation does not keep track of equivalent terms, but only provides the basic
 * required functionality. In particular:
 * <ul>
 * <li>
 * <code>areEquivalent</code> only checks for equality of the two terms
 * using the corresponding <code>equals</code> method.</li>
 * <li><code>findRepresentative</code> returns the term itself.</li>
 * <li><code>getEquivalent</code> returns a singleton set containing the term
 * itself.</li>
 * <li><code>setEquivalent</code> does nothing.</li>
 * </ul>
 * 
 * @author Adrian Marte
 */
public class IgnoreTermEquivalence implements IEquivalentTerms {

	public boolean areEquivalent(ITerm x, ITerm y) {
		return x.equals(y);
	}

	public ITerm findRepresentative(ITerm term) {
		return term;
	}

	public Set<ITerm> getEquivalent(ITerm term) {
		return Collections.singleton(term);
	}

	public void setEquivalent(ITerm x, ITerm y) {
		// Do nothing.
	}

}
