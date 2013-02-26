package at.sti2.streamingiris.functional;

import java.util.List;

import junit.framework.TestCase;
import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.KnowledgeBaseFactory;
import at.sti2.streamingiris.api.IKnowledgeBase;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.optimisations.magicsets.MagicSets;
import at.sti2.streamingiris.optimisations.rulefilter.RuleFilter;

/**
 * Tests for magic sets.
 */
public class MagicSetsTest extends TestCase {
	public void testTranslation() throws Exception {
		String program = "triple(?y, ?a, ?z) :- triple(?x, '88', ?y), triple(?x, ?a, ?z), NOT_EQUAL(?a, '88'), NOT_EQUAL(?x, ?y). "
				+ "triple(?z, ?a, ?y) :- triple(?x, '88', ?y), triple(?z, ?a, ?x), NOT_EQUAL(?a, '88'), NOT_EQUAL(?x, ?y). "
				+ "?- triple('184', ?p, ?o). ";
		String expectedResults = "a( 1, 2 )." + "a( 0, 3 ).";

		try {
			evaluateSemiNaiveAndOptimisations(program, expectedResults);
		} catch (Exception e) {
		}
		try {
			evaluateSemiNaiveAndOptimisations(program, expectedResults);
		} catch (Exception e) {
		}
		try {
			evaluateSemiNaiveAndOptimisations(program, expectedResults);
		} catch (Exception e) {
		}
		try {
			evaluateSemiNaiveAndOptimisations(program, expectedResults);
		} catch (Exception e) {
		}
		try {
			evaluateSemiNaiveAndOptimisations(program, expectedResults);
		} catch (Exception e) {
		}
		int z = 1;
	}

	public static void evaluateSemiNaiveAndOptimisations(String program,
			String expectedResults) throws Exception {
		Configuration configuration = KnowledgeBaseFactory
				.getDefaultConfiguration();

		configuration.programOptmimisers.add(new RuleFilter());
		configuration.programOptmimisers.add(new MagicSets());

		executeAndCheckResults(program, expectedResults, configuration,
				"Semi-Naive and Magic Sets");
	}

	public static void executeAndCheckResults(String program, String expected,
			Configuration configuration, String evaluationName)
			throws Exception {
		Parser parser = new Parser();
		parser.parse(program);
		List<IQuery> queries = parser.getQueries();

		assert queries.size() <= 1;

		IQuery query = null;
		if (queries.size() == 1)
			query = queries.get(0);

		// Instantiate the knowledge-base
		IKnowledgeBase kb = KnowledgeBaseFactory.createKnowledgeBase(
				parser.getFacts(), parser.getRules(), configuration);

		// Execute the query
		if (query != null)
			kb.execute(query);

		kb.shutdown();
	}
}
