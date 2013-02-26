package at.sti2.streamingiris.utils;

import java.util.HashSet;
import java.util.Set;

import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.storage.IRelation;

/**
 * Helper class for the semi-naive evaluator. An object of this class indexes an
 * entire relation on every term to enable fast detection of the existence of a
 * tuple within a relation.
 */
public class TotalIndex {
	/**
	 * Constructor.
	 * 
	 * @param relation
	 *            The relation to index.
	 */
	public TotalIndex(IRelation relation) {
		mRelation = relation;
	}

	/**
	 * Discover if the indexed relation contains a specific tuple.
	 * 
	 * @param tuple
	 *            The tuple to test for.
	 * @return true, if a tuple with identical term values already exists in the
	 *         relation.
	 */
	public boolean contains(ITuple tuple) {
		update();

		return mBag.contains(tuple);
	}

	/**
	 * Update the index with tuples not already seen by the index.
	 */
	private void update() {
		for (; mLastIndexOfView < mRelation.size(); ++mLastIndexOfView) {
			ITuple viewTuple = mRelation.get(mLastIndexOfView);

			mBag.add(viewTuple);
		}
	}

	/** The index in to the relation of the last seen tuple. */
	private int mLastIndexOfView = 0;

	/** The set of tuples from the relation. */
	private final Set<ITuple> mBag = new HashSet<ITuple>();

	/** The relation being indexed. */
	private final IRelation mRelation;
}
