package at.sti2.streamingiris.rules;

import java.util.List;

import at.sti2.streamingiris.api.basics.IRule;

/**
 * Interface to all rule stratifiers.
 */
public interface IRuleStratifier {
	/**
	 * Stratify the rules, i.e. arrange them in to groups such that each
	 * increasing level of rules can be evaluated before the next higher level
	 * of dependent rules.
	 * 
	 * @return The rules arranged in to strata. The number of rules returned may
	 *         be different to the number provided, because the stratification
	 *         technique might require the rules to be re-written.
	 * @param rules
	 *            The collection of rules to stratify
	 * @return A set of stratified rules, or null if the rules can not be
	 *         stratified with this algorithm.
	 */
	List<List<IRule>> stratify(List<IRule> rules);
}
