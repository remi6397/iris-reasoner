package at.sti2.streamingiris.evaluation.stratifiedbottomup.seminaive;

import java.util.List;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.evaluation.stratifiedbottomup.IRuleEvaluator;
import at.sti2.streamingiris.facts.Facts;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.rules.compiler.ICompiledRule;
import at.sti2.streamingiris.storage.IRelation;

/**
 * Semi-naive evaluation. see Ullman, Vol. 1
 */
public class SemiNaiveEvaluator implements IRuleEvaluator {
	// private Logger logger = LoggerFactory.getLogger(getClass());

	public void evaluateRules(List<ICompiledRule> rules, IFacts facts,
			Configuration configuration, long timestamp)
			throws EvaluationException {
		IFacts deltas = new Facts(configuration.relationFactory);

		// One pass with simple evaluation to generate deltas
		// For each rule in the collection (stratum)
		for (final ICompiledRule rule : rules) {
			IRelation delta = rule.evaluate();

			if (delta != null && delta.size() > 0) {
				IPredicate predicate = rule.headPredicate();
				deltas.get(predicate).addAll(delta, timestamp);

				// if (logger.isDebugEnabled()) {
				// for (int i = 0; i < delta.size(); i++) {
				// logger.debug("Created fact [" + timestamp + "]: "
				// + predicate + " " + delta.get(i));
				// }
				// }
			}
		}

		// Update the facts
		addAll(facts, deltas, timestamp);

		// Now do iterative evaluation (semi-naive)
		boolean newTuples;
		for (;;) {
			newTuples = false;

			IFacts previousDeltas = deltas;

			deltas = new Facts(configuration.relationFactory);

			for (final ICompiledRule rule : rules) {
				IPredicate predicate = rule.headPredicate();

				IRelation delta = rule.evaluateIteratively(previousDeltas);

				// Remove already known tuples
				if (delta != null && delta.size() > 0) {
					IRelation programFacts = facts.get(predicate);
					delta = removeDeducedTuples(predicate, delta, programFacts,
							configuration, timestamp);
				}

				if (delta != null && delta.size() > 0) {
					newTuples = true;
					deltas.get(predicate).addAll(delta);

					// if (logger.isDebugEnabled()) {
					// for (int i = 0; i < delta.size(); i++) {
					// logger.debug("Created fact [" + timestamp + "]: "
					// + predicate + " " + delta.get(i));
					// }
					// }
				}
			}

			if (!newTuples)
				break;

			// Iterate new tuples in dP[i] and add to program
			addAll(facts, deltas, timestamp);
		}
	}

	/**
	 * Add all the tuples from each of the deltas to the target facts.
	 * 
	 * @param target
	 *            The facts to be added to.
	 * @param deltas
	 *            The facts to be added.
	 */
	private static void addAll(IFacts target, IFacts deltas, long timestamp) {
		for (IPredicate predicate : deltas.getPredicates())
			target.get(predicate).addAll(deltas.get(predicate), timestamp);
	}

	/**
	 * Helper to remove tuples from a delta that are already known or computed.
	 * 
	 * @param predicate
	 *            The predicate identifying the relation.
	 * @param delta
	 *            The deltas produced by the last round of evaluation.
	 * @param programFacts
	 *            The already known or computed facts.
	 * @return
	 */
	private static IRelation removeDeducedTuples(IPredicate predicate,
			IRelation delta, IRelation programFacts,
			Configuration configuration, long timestamp) {
		// If there is nothing to take away from, or just nothing to
		// take-away...
		if (delta.size() == 0 || programFacts.size() == 0)
			return delta;

		IRelation result = configuration.relationFactory.createRelation();

		for (int t = 0; t < delta.size(); ++t) {
			ITuple tuple = delta.get(t);
			if (!programFacts.contains(tuple)) {
				result.add(tuple);
			} else if (programFacts.getTimestamp(tuple) < 0) {
				programFacts.setTimestamp(tuple, timestamp);
			}
		}

		return result;
	}
}
