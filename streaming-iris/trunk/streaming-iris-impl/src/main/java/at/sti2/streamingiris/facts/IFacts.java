package at.sti2.streamingiris.facts;

import java.util.Map;
import java.util.Set;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.storage.IRelation;

public interface IFacts {

	/**
	 * Get the relation associated with the given predicate and create one if
	 * one does not already exist.
	 * 
	 * @param predicate
	 *            The predicate identifying the relation.
	 * @return The relation associated with the given predicate.
	 */
	IRelation get(IPredicate predicate);

	/**
	 * Get the set of predicate identifying all relations known to this facts
	 * object.
	 * 
	 * @return
	 */
	Set<IPredicate> getPredicates();

	/**
	 * Add new facts to the IFacts object.
	 * 
	 * @param newFacts
	 *            The new facts to be added.
	 */
	void addFacts(Map<IPredicate, IRelation> newFacts, long timestamp);

	/**
	 * Deletes all obsolete facts.
	 */
	void clean(long timestamp);

}
