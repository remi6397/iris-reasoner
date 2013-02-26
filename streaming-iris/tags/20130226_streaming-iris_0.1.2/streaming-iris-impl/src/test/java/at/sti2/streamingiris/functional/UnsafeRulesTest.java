package at.sti2.streamingiris.functional;

import junit.framework.TestCase;

public class UnsafeRulesTest extends TestCase {
	public void testGelderRossSchlipf_UnsafeNegationExample1() throws Exception {
		String program = "p( ?x ) :- a( ?x ), diff( ?x, ?y )."
				+ "diff( ?x, ?y ) :- not same( ?x, ?y )."
				+ "same( ?x, ?x ) :- ." + "a(1)." +

				"?- p(?x).";

		Helper.evaluateUnsafeRules(program, "dummy(1).");
	}
}
