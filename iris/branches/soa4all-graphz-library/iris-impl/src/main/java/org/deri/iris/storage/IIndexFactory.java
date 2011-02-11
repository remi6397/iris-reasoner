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
package org.deri.iris.storage;

import org.deri.iris.utils.equivalence.IEquivalentTerms;

/**
 * The interface of all index factories.
 * 
 * @see IIndex
 */
public interface IIndexFactory {

	/**
	 * Creates a new index on the given relation on the given terms positions
	 * (indices).
	 * 
	 * @param relation The relation that the index will use.
	 * @param indices The ordered collection of indices. This collection must
	 *            have a size 0 <= size <= arity of the relation. e.g. to create
	 *            an index on terms c1 and c4 for a relation Q( c0, c1, c2, c3,
	 *            c4 ), the indices will be { 1, 4 }
	 * @return The new index instance.
	 */
	IIndex createIndex(IRelation relation, int... indices);

	/**
	 * Creates a new index on the given relation on the given terms positions
	 * (indices). The index uses the specified equivalent terms to identify
	 * equivalent terms. When tuples of this index are matched against a
	 * specific key, this index also returns tuples, whose corresponding terms
	 * are equivalent to the terms of the key.
	 * 
	 * @param relation The relation that the index will use.
	 * @param indices The ordered collection of indices. This collection must
	 *            have a size 0 <= size <= arity of the relation. e.g. to create
	 *            an index on terms c1 and c4 for a relation Q( c0, c1, c2, c3,
	 *            c4 ), the indices will be { 1, 4 }
	 * @param equivalentTerms The equivalent terms.
	 * @return The new index instance.
	 */
	IIndex createIndex(IRelation relation, IEquivalentTerms equivalentTerms,
			int... indices);

}
