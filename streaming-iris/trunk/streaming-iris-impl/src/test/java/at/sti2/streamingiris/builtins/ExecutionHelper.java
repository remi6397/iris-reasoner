package at.sti2.streamingiris.builtins;

import java.util.HashSet;
import java.util.Set;

import at.sti2.streamingiris.api.IKnowledgeBase;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.storage.IRelation;

class ExecutionHelper {
	/**
	 * Executes a program and print results.
	 * 
	 * @param p
	 *            A program to be evaluated.
	 */
	public static void executeTest(final IKnowledgeBase p, IQuery q,
			IRelation res) throws Exception {
		System.out.println("--- input ---");
		for (final IRule rule : p.getRules()) {
			System.out.println(rule);
		}
		System.out.println("--- facts ---");
		// System.out.println(p.keySet());
		// for (final IPredicate pred : p.getPredicates()) {
		// System.out.printf("%s -> %s\n", pred.getPredicateSymbol(), p
		// .getFacts(pred));
		// for (ITuple t : p.getFacts(pred)) {
		// System.out.println(pred.getPredicateSymbol() + t);
		// }
		// }

		// IExpressionEvaluator method = new ExpressionEvaluator();
		// IExecutor exec = new Executor(p, method);
		// exec.execute();
		System.out.println("Result: ");
		IRelation actual = p.execute(q);
		// Map<IPredicate, IMixedDatatypeRelation> results =
		// exec.computeSubstitutions();
		// ProgramTest.printResults(results);

		// assertTrue(results.get(results.keySet().iterator().next()).containsAll(res));
		junit.framework.Assert.assertTrue(same(res, actual));
	}

	public static boolean same(IRelation actualResults,
			IRelation expectedResults) {
		Set<ITuple> actual = new HashSet<ITuple>();
		Set<ITuple> expected = new HashSet<ITuple>();

		for (int t = 0; t < actualResults.size(); ++t)
			actual.add(actualResults.get(t));

		for (int t = 0; t < expectedResults.size(); ++t)
			expected.add(expectedResults.get(t));

		return actual.equals(expected);
	}

}
