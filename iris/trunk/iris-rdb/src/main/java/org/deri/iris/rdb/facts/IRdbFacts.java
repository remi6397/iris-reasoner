package org.deri.iris.rdb.facts;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rdb.storage.IRdbRelation;

/**
 * Facts that are stored in a relational database.
 */
public interface IRdbFacts extends IFacts {

	/**
	 * Returns the {@link IRdbRelation} associated with the given predicate and
	 * creates one if it does not already exist.
	 * 
	 * @param predicate
	 *            The predicate identifying the relation.
	 * @return The relation associated with the given predicate.
	 */
	public IRdbRelation get(IPredicate predicate);

	/**
	 * Copies all relations (and the tuples they contain) of the specified
	 * {@link IFacts} to this facts.
	 * 
	 * @param source
	 *            The facts to add to this facts.
	 */
	public void addAll(IFacts source);

	/**
	 * Drops all relations this instance keeps hold of.
	 */
	public void dropAll();

}
