package at.sti2.streamingiris.evaluation.stratifiedbottomup.naive;

import java.util.List;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.evaluation.stratifiedbottomup.IRuleEvaluator;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.rules.compiler.ICompiledRule;
import at.sti2.streamingiris.storage.IRelation;

/**
 * Naive evaluation. see Ullman, Vol. 1
 */
public class NaiveEvaluator implements IRuleEvaluator {
	// private Logger logger = LoggerFactory.getLogger(getClass());

	public void evaluateRules(List<ICompiledRule> rules, IFacts facts,
			Configuration configuration, long timestamp)
			throws EvaluationException {
		boolean cont = true;
		while (cont) {
			cont = false;

			// For each rule in the collection (stratum)
			for (final ICompiledRule rule : rules) {
				IRelation delta = rule.evaluate();

				if (delta != null && delta.size() > 0) {
					IPredicate predicate = rule.headPredicate();

					if (facts.get(predicate).addAll(delta, timestamp))
						cont = true;

					// if (logger.isDebugEnabled()) {
					// for (int i = 0; i < delta.size(); i++) {
					// logger.debug("Created fact [" + timestamp + "]: "
					// + predicate + " " + delta.get(i));
					// }
					// }
				}
			}
		}
	}
}
