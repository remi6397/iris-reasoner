package at.sti2.streamingiris.rules;

import java.util.Collection;
import java.util.List;

import at.sti2.streamingiris.api.basics.IRule;

/**
 * Represents all classes that can optimise the order of rule evaluation.
 * Ideally, the rule with the fewest or no dependencies will be evaluated first
 * and so on up to the rule with the most dependencies. Cycles, branches and
 * independent networks will have to be taken in to account.
 */
public interface IRuleReOrderingOptimiser {
	/**
	 * Re-order the rules. The returned collection will have an implied
	 * ordering.
	 * 
	 * @param rules
	 *            The rules to re-order.
	 * @return The same rules, but in a more efficient order for evaluation.
	 */
	List<IRule> reOrder(Collection<IRule> rules);
}
