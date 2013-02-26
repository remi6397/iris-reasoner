package at.sti2.streamingiris.storage;

import at.sti2.streamingiris.api.basics.ITuple;

/**
 * The interface to all relation classes. The order of the tuples is given by
 * the order of insertion. The access via index position is intended to allow
 * for smart indexing, caching etc when using relation classes that support
 * large amounts of data.
 */
public interface IRelation {

	/**
	 * Add a tuple to the relation. The tuple MUST have the same arity as all
	 * other tuples in the relation.
	 * 
	 * @param tuple
	 *            The (unique) tuple to add.
	 * @return true, if it was added, false if a tuple already exists in the
	 *         relation with the same term values.
	 */
	boolean add(ITuple tuple);

	/**
	 * Add a tuple to the relation. The tuple MUST have the same arity as all
	 * other tuples in the relation.
	 * 
	 * @param tuple
	 *            The (unique) tuple to add.
	 * @param timestamp
	 *            The time when the facts become obsolete.
	 * @return true, if it was added, false if a tuple already exists in the
	 *         relation with the same term values.
	 */
	boolean add(ITuple tuple, long timestamp);

	/**
	 * Add all tuples in relation 'relation' to this relation. The tuples in
	 * 'relation' MUST have the same arity as all other tuples in this relation.
	 * 
	 * @param relation
	 *            The relation containing tuples to add.
	 * @return true if any tuples were actually added.
	 */
	boolean addAll(IRelation relation);

	/**
	 * Add all tuples in relation 'relation' to this relation. The tuples in
	 * 'relation' MUST have the same arity as all other tuples in this relation.
	 * 
	 * @param relation
	 *            The relation containing tuples to add.
	 * @param timestamp
	 *            The time when the new facts become obsolete.
	 * @return true if any tuples were actually added.
	 */
	boolean addAll(IRelation relation, long timestamp);

	/**
	 * Get the current number of tuples in this relation.
	 * 
	 * @return The number of tuples in the relation.
	 */
	int size();

	/**
	 * Get a tuple at a specific index.
	 * 
	 * @param index
	 *            The index of the tuple in the relation, 0 <= index < size().
	 * @return The tuple at the given index position.
	 */
	ITuple get(int index);

	/**
	 * Get the timestamp of a specific tuple.
	 * 
	 * @param tuple
	 *            The tuple.
	 * @return The timestamp of the tuple.
	 */
	long getTimestamp(ITuple tuple);

	/**
	 * Set the timestamp of a specific tuple.
	 * 
	 * @param tuple
	 *            The tuple.
	 * @param timestamp
	 *            The new timestamp for the tuple.
	 */
	void setTimestamp(ITuple tuple, long timestamp);

	/**
	 * Returns if the given tuple exists in this relation.
	 * 
	 * @param tuple
	 *            The tuple.
	 * @return true if the tuple exists, false otherwise.
	 */
	boolean contains(ITuple tuple);

	/**
	 * Deletes all obsolete tuples in this relation.
	 * 
	 * @param timestamp
	 *            The current time.
	 */
	void clean(long timestamp);
}
