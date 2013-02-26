package at.sti2.streamingiris.evaluation.topdown;

import java.util.Set;

import at.sti2.streamingiris.api.basics.IPredicate;

/**
 * Used by OLDT evaluation to tag predicates as so-called 'memo predicates' If
 * no predicate is tagged as a memo predicate, OLDT resolution behaves like OLD
 * resolution (equals SLD with standard literal selector).
 * 
 * @author gigi
 * 
 */
public interface IPredicateTagger {

	/**
	 * Returns a set of tagged predicates.
	 * 
	 * @return set of tagged predicates.
	 */
	public Set<IPredicate> getMemoPredicates();

}
