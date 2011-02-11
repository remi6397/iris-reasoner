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
import org.deri.iris.utils.DisjointSets;

/**
 * An utility class to define equivalence between terms. Uses the disjoint-set
 * data structure.
 * 
 * @see org.deri.iris.utils.DisjointSets
 * @author Adrian Marte
 */
public class TermEquivalence implements IEquivalentTerms {

	/**
	 * The disjoint-set data structure.
	 */
	private final DisjointSets<ITerm> disjointSets = new DisjointSets<ITerm>();

	/**
	 * Creates a new term equivalence relation.
	 */
	public TermEquivalence() {
	}

	public boolean areEquivalent(ITerm x, ITerm y) {
		if (x.equals(y)) {
			return true;
		}

		return disjointSets.areInSameSet(x, y);
	}

	public void setEquivalent(ITerm x, ITerm y) {
		disjointSets.putInSameSet(x, y);
	}

	public ITerm findRepresentative(ITerm term) {
		ITerm representative = disjointSets.find(term);

		if (representative == null) {
			return term;
		}

		return representative;
	}

	public Set<ITerm> getEquivalent(ITerm term) {
		Set<ITerm> set = disjointSets.getSetOf(term);

		if (set.isEmpty()) {
			return Collections.singleton(term);
		}

		return set;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof TermEquivalence)) {
			return false;
		}

		TermEquivalence thatEquivalence = (TermEquivalence) obj;

		return disjointSets.equals(thatEquivalence.disjointSets);
	}

	@Override
	public int hashCode() {
		return disjointSets.hashCode();
	}

	@Override
	public String toString() {
		return disjointSets.toString();
	}

}
