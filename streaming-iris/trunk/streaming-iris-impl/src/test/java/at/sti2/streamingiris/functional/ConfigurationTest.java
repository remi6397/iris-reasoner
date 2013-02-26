package at.sti2.streamingiris.functional;

import junit.framework.TestCase;
import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.KnowledgeBaseFactory;

public class ConfigurationTest extends TestCase {
	public void testDivideByZeroDiscradAndIgnore() throws Exception {
		String program = "a(0)." + "a(1)." + "b(0)."
				+ "d(?Z) :- a(?X), b(?Y), ?X / ?Y = ?Z." + "?-d(?X).";

		Helper.evaluateSemiNaive(program, "");
	}

	public void testDivideByZeroStop() throws Exception {
		String program = "a(0)." + "a(1)." + "b(0)."
				+ "d(?Z) :- a(?X), b(?Y), ?X / ?Y = ?Z." + "?-d(?X).";

		try {
			Configuration configuration = KnowledgeBaseFactory
					.getDefaultConfiguration();
			configuration.evaluationDivideByZeroBehaviour = Configuration.DivideByZeroBehaviour.STOP;

			Helper.executeAndCheckResults(program, "", configuration,
					"Divide by zero should stop");

			fail("Should have thrown an EvaluationException");
		} catch (EvaluationException e) {

		}
	}
}
