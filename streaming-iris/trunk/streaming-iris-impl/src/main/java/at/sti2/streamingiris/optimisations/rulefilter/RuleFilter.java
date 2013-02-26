package at.sti2.streamingiris.optimisations.rulefilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import at.sti2.streamingiris.api.IProgramOptimisation;
import at.sti2.streamingiris.api.basics.ILiteral;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.graph.IPredicateGraph;

/**
 * <p>
 * Methods to shrink rules to the absolute minimum of needed rules.
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 */
public class RuleFilter implements IProgramOptimisation {

	public Result optimise(final Collection<IRule> rules, final IQuery query) {
		return new Result(new ArrayList<IRule>(shrinkRules(rules, query)),
				query);
	}

	/**
	 * Shrinks a set of rules to the absolute minimum of needed rules to
	 * evaluate a given query.
	 * 
	 * @param r
	 *            the rules to shrink
	 * @param q
	 *            the query for which to shrink the rules
	 * @return the minimal set of needed rules
	 * @throws NullPointerException
	 *             if the set of rules is <code>null</code>
	 * @throws NullPointerException
	 *             if the query is <code>null</code>
	 */
	public static Set<IRule> shrinkRules(final Collection<IRule> r,
			final IQuery q) {
		if (r == null) {
			throw new NullPointerException("The rules must not be null");
		}
		if (q == null) {
			throw new NullPointerException("The query must not be null");
		}
		return getRulesForPredicates(r, getDepends(r, q));
	}

	/**
	 * Filters a set of rules so that it only contains rules defining a
	 * predicate out of a set of predicates.
	 * 
	 * @param r
	 *            the rules to filter
	 * @param p
	 *            the predicate for which to filter the rules
	 * @return the filtered rules
	 * @throws NullPointerException
	 *             if the rules are <code>null</code>
	 * @throws NullPointerException
	 *             if the predicates are <code>null</code>
	 */
	public static Set<IRule> getRulesForPredicates(final Collection<IRule> r,
			final Collection<IPredicate> p) {
		if (r == null) {
			throw new NullPointerException("The rules must not be null");
		}
		if (p == null) {
			throw new NullPointerException("The predicates must not be null");
		}
		final Set<IRule> res = new HashSet<IRule>();
		for (final IRule rule : r) {
			if (isRuleDefiningPredicate(rule, p)) {
				res.add(rule);
			}
		}
		return res;
	}

	/**
	 * Determines which predicates must be evaluated to evaluate a given query.
	 * 
	 * @param r
	 *            the rules on which to determine on which predicate the query
	 *            depends
	 * @param q
	 *            the query for which to check the predicates
	 * @return the predicates the query depends on
	 * @throws NullPointerException
	 *             if the set of rules is <code>null</code>
	 * @throws NullPointerException
	 *             if the query is <code>null</code>
	 */
	public static Set<IPredicate> getDepends(final Collection<IRule> r,
			final IQuery q) {
		if (r == null) {
			throw new NullPointerException("The rules must not be null");
		}
		if (q == null) {
			throw new NullPointerException("The query must not be null");
		}
		// TODO: consider iterating over the rules and collecting the
		// predicates instead of using a graph (might be faster)
		final IPredicateGraph pg = at.sti2.streamingiris.factory.Factory.GRAPH
				.createPredicateGraph(r);
		final Set<IPredicate> depends = new HashSet<IPredicate>();
		for (final ILiteral l : q.getLiterals()) {
			depends.add(l.getAtom().getPredicate());
			depends.addAll(pg.getDepends(l.getAtom().getPredicate()));
		}
		return depends;
	}

	/**
	 * Checks whether a rule defines a given predicate. In other words it checks
	 * whether the rule got one of the given predicates in it's head.
	 * 
	 * @param r
	 *            the rule to check
	 * @param p
	 *            the predicates to check
	 * @return <code>true</code> if the rule defines one of the given predicates
	 * @throws NullPointerException
	 *             if the rule is <code>null</code>
	 * @throws NullPointerException
	 *             if the predicate set is <code>null</code>
	 */
	private static boolean isRuleDefiningPredicate(final IRule r,
			final Collection<IPredicate> p) {
		if (r == null) {
			throw new NullPointerException("The rule must not be null");
		}
		if (p == null) {
			throw new NullPointerException("The predicates must not be null");
		}
		for (final ILiteral l : r.getHead()) {
			if (p.contains(l.getAtom().getPredicate())) {
				return true;
			}
		}
		return false;
	}
}
