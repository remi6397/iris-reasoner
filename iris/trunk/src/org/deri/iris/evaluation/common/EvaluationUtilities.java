package org.deri.iris.evaluation.common;

import java.util.*;
import java.util.logging.Logger;

import org.deri.iris.api.basics.*;

/**
 * 
 * A class for collecting some functionality that might be helpful for various
 * evaluation methods.
 * 
 * @author uwekel
 * @date 30.11.2006
 *
 */

public class EvaluationUtilities {

	/**
	 * Given a set of (horn) rules and a query, the method extracts the subset of rules
	 * that are actually relevant for computing answers to the query. 
	 * The extraction is based on a simple syntactical analysis of query and rules.
	 * It does not claim to extract the minimal subset of rules, however gets rid of
	 * obviously irrelevant rules. 
	 * Computation of the filtered rule set does can be done efficiently and does not harm
	 * computational performance. 
	 * 
	 * The method tries to increase efficiency of evaluation methods that themselves do not
	 * (explicitly or implicitly) take into account obvious irrelevancy. 
	 * The algorithm works on predicates, but not on advances criteria such as literals (requires
	 * unification).
	 * 
	 * We expect that for ontologies with large schema, this method is useful, when
	 * posing queries that only address a very small specific part of the ontology.
	 * 
	 * For the standard translation of F-Logic to Datalog (allowing metamodelling) 
	 * the proposed method might not be useful since only very few predicates are 
	 * created, but for a different translation (representing classes as predicates instead of instances
	 * of "metaschema"-predicates) this method might be very effective.
	 * 
	 * The methods can be used as pre-computation step before any sort of evaluation or even 
	 * rewriting of a program. For instance, it might make sense to use it as well before
	 * doing magic sets transformations. 
	 * 
	 * The resulting set contains only objects that occur in <code>ruleSet</code>, it does not
	 * create copies.
	 * 
	 * @param ruleSet - the rule set to be filtered.
	 * @param query - a query for which answers need to be computed
	 * @return a subset of rules in <code>ruleSet</code> that contains only rules that are 
	 * obiously relevant for computing answers to <code>query</code>  
	 */
	public static Set<IRule> extractRelevantRulesForEvaluation(Set<IRule> ruleSet, IQuery query){
		Set<IPredicate> relevantPredicates = new LinkedHashSet<IPredicate>();

		// No rules in ruleSet have not classified as being relevant yet
		Set<IRule> uncertainRules  = new LinkedHashSet<IRule>();
		uncertainRules.addAll(ruleSet);
		// System.err.println("#Uncertain rules:" + uncertainRules.size());
		Set<IRule> relevantRules = new LinkedHashSet<IRule>();
		
		
		for (ILiteral l :  query.getQueryLiterals()) {
			// every predicate occuring in the query is relevant ...
			relevantPredicates.add(l.getPredicate()); // predicate relevancy in step 0
		}
		
		boolean finished = false;
		
		while (!finished){ // induction: iteration from step n-1 to step n
			
			// remember number of relevant predicates before the iteration step
			int numberOfRelevantPredicates_PreIteration = relevantPredicates.size();
			
			Set<IRule> newlyClassifiedRules  = new LinkedHashSet<IRule>();
			
			// Find all rules that (a) are uncertain and (b) define a relevant predicate.
			for (IRule r : uncertainRules ){
				
				// System.err.println("Relevant predicates are currently:" + relevantPredicates);
				
				boolean ruleClassifiedRelevant = false;
				for ( Iterator<ILiteral> it = r.getHeadLiterals().iterator(); 
					  !ruleClassifiedRelevant && it.hasNext(); ){
					// A rule is classified as relevant iff it defines at least one relevant
					// predicate (in its head)
					ruleClassifiedRelevant = relevantPredicates.contains(it.next().getPredicate());
				}
				if (ruleClassifiedRelevant) { 
					// System.err.println("Rule found relevant: " + r);
					
					relevantRules.add(r);  // mark rule as being relevant in step n-1
					// rule is now classified and therefore no longer uncertain.
					// no need to consider again. 
					newlyClassifiedRules.add(r); 
					
					// The rule now causes all predicates in its body to be relevant
					for (ILiteral l : r.getBodyLiterals()) {
						relevantPredicates.add(l.getPredicate()); // mark body predicates as being relevant in step n
					}
				}
				
			} // loop over all uncertain candidate rules
			
			uncertainRules.removeAll(newlyClassifiedRules);
			
			// if no change to the relevant predicates during the last iteration happened, 
			// we are done, since only in this case new rules would be classified 
			// (fixpoint is reached)
			
			if (relevantPredicates.size() == numberOfRelevantPredicates_PreIteration) { 
				finished = true; 
			}
			
			// termination is ensured since the set of relevant predicates is bounded by 
			// the (finite) number of predicates that occur in the finite set of rules.
			
		} // loop over all iteration of the classification process
				
		// Log how many rules have been eliminated (in total and in percent)
		
//		System.err.println("Extracted " + relevantRules.size() + 
//				" rules as being relevant for the query.\n " +
//				"Dropped in total " + uncertainRules.size() + 
//				" rules ("+  (ruleSet.size() > 0 ? (uncertainRules.size()*100.0 / ruleSet.size()) : -1.0) 
//				+ " %)");
		
		return relevantRules;
	} 
}
