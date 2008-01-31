package org.deri.iris.new_stuff.facts;

import java.util.Set;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.new_stuff.storage.IRelation;

public interface IFacts
{

	/**
	 * Get the relation associated with the given predicate and create one
	 * if one does not already exist.
	 * @param predicate The predicate identifying the relation.
	 * @return The relation associated with the given predicate.
	 */
	IRelation get( IPredicate predicate );

	/**
	 * Get the set of predicate identifying all relations known to this facts object.
	 * @return
	 */
	Set<IPredicate> getPredicates();

}
