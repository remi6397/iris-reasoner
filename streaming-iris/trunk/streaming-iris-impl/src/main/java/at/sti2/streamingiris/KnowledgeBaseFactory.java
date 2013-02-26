package at.sti2.streamingiris;

import java.util.List;
import java.util.Map;

import at.sti2.streamingiris.api.IKnowledgeBase;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.storage.IRelation;

/**
 * The factory for creating a knowledge-base.
 */
public class KnowledgeBaseFactory {
	/**
	 * Create a knowledge base with default configuration.
	 * 
	 * @param facts
	 *            The starting facts.
	 * @param rules
	 *            The rules to use.
	 * @return A new knowledge-base instance.
	 * @throws EvaluationException
	 */
	public static IKnowledgeBase createKnowledgeBase(
			Map<IPredicate, IRelation> facts, List<IRule> rules)
			throws EvaluationException {
		return createKnowledgeBase(facts, rules, new Configuration());
	}

	/**
	 * Create a knowledge base with a custom configuration.
	 * 
	 * @param facts
	 *            The starting facts.
	 * @param rules
	 *            The rules to use.
	 * @param configuration
	 *            The configuration to use for the new knowledge-base.
	 * @return A new knowledge-base instance.
	 * @throws EvaluationException
	 */
	public static IKnowledgeBase createKnowledgeBase(
			Map<IPredicate, IRelation> facts, List<IRule> rules,
			Configuration configuration) throws EvaluationException {
		return new KnowledgeBase(facts, rules, configuration);
	}

	/**
	 * Create a new default configuration and return it.
	 * 
	 * @return The new configuration.
	 */
	public static Configuration getDefaultConfiguration() {
		return new Configuration();
	}
}
