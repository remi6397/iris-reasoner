package at.sti2.streamingiris.storage;

import at.sti2.streamingiris.utils.equivalence.IEquivalentTerms;

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
	 * @param relation
	 *            The relation that the index will use.
	 * @param indices
	 *            The ordered collection of indices. This collection must have a
	 *            size 0 <= size <= arity of the relation. e.g. to create an
	 *            index on terms c1 and c4 for a relation Q( c0, c1, c2, c3, c4
	 *            ), the indices will be { 1, 4 }
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
	 * @param relation
	 *            The relation that the index will use.
	 * @param indices
	 *            The ordered collection of indices. This collection must have a
	 *            size 0 <= size <= arity of the relation. e.g. to create an
	 *            index on terms c1 and c4 for a relation Q( c0, c1, c2, c3, c4
	 *            ), the indices will be { 1, 4 }
	 * @param equivalentTerms
	 *            The equivalent terms.
	 * @return The new index instance.
	 */
	IIndex createIndex(IRelation relation, IEquivalentTerms equivalentTerms,
			int... indices);

}
