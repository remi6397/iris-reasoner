package at.sti2.streamingiris.topdown.oldt;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import at.sti2.streamingiris.api.terms.IConstructedTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.evaluation.topdown.TopDownHelper;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test class for the mergeSubstitutions() helper method.
 * 
 * @author gigi
 * @see TopDownHelper.mergeSubstitutions(Map<IVariable, ITerm> deeperMap,
 *      Map<IVariable, ITerm> higherMap)
 */
public class MergeSubstitutionsTest extends TestCase {

	IVariable VAR_X = Factory.TERM.createVariable("X");
	IVariable VAR_Y = Factory.TERM.createVariable("Y");
	IVariable VAR_Z = Factory.TERM.createVariable("Z");

	ITerm TERM_A = Factory.TERM.createString("a");
	ITerm TERM_B = Factory.TERM.createString("b");
	ITerm TERM_C = Factory.TERM.createString("c");

	IConstructedTerm CONSTRUCTED_FX = Factory.TERM.createConstruct("f", VAR_X);
	IConstructedTerm CONSTRUCTED_Fa = Factory.TERM.createConstruct("f", TERM_A);

	/*
	 * Note: map2 (second argument of TopDownHelper.mergeSubstitutions(map1,
	 * map2);) is always the latest map. Similar / conflicting entries in map1
	 * will be ignored.
	 */

	public void testMergeTwoSimpleDistinctMappings() {

		Map<IVariable, ITerm> deeperMap = new HashMap<IVariable, ITerm>();
		Map<IVariable, ITerm> higherMap = new HashMap<IVariable, ITerm>();

		higherMap.put(VAR_Y, TERM_B);
		deeperMap.put(VAR_X, TERM_A);

		Map<IVariable, ITerm> combinedMap = TopDownHelper.mergeSubstitutions(
				deeperMap, higherMap);

		System.out.println(combinedMap);

		assertEquals(combinedMap.get(VAR_X), TERM_A);
		assertEquals(combinedMap.get(VAR_Y), TERM_B);
		assertEquals(combinedMap.size(), 2);
	}

	public void testMergeTwoSimpleConflictingMappings() {

		Map<IVariable, ITerm> deeperMap = new HashMap<IVariable, ITerm>();
		Map<IVariable, ITerm> higherMap = new HashMap<IVariable, ITerm>();

		higherMap.put(VAR_X, TERM_B);
		deeperMap.put(VAR_X, TERM_A);

		Map<IVariable, ITerm> combinedMap = TopDownHelper.mergeSubstitutions(
				deeperMap, higherMap);

		System.out.println(combinedMap);

		assertEquals(combinedMap.get(VAR_X), TERM_B);
		assertEquals(combinedMap.size(), 1);
	}

	public void testSimpleVariableChain() {
		Map<IVariable, ITerm> deeperMap = new HashMap<IVariable, ITerm>();
		Map<IVariable, ITerm> higherMap = new HashMap<IVariable, ITerm>();

		higherMap.put(VAR_X, VAR_Y);
		deeperMap.put(VAR_Y, TERM_A);

		Map<IVariable, ITerm> combinedMap = TopDownHelper.mergeSubstitutions(
				deeperMap, higherMap);

		System.out.println(combinedMap);

		assertEquals(combinedMap.get(VAR_X), TERM_A);
		assertEquals(combinedMap.size(), 1);
	}

	public void testConstructedTermVariableChain() {
		Map<IVariable, ITerm> deeperMap = new HashMap<IVariable, ITerm>();
		Map<IVariable, ITerm> higherMap = new HashMap<IVariable, ITerm>();

		higherMap.put(VAR_Y, CONSTRUCTED_FX);
		deeperMap.put(VAR_X, TERM_A);

		Map<IVariable, ITerm> combinedMap = TopDownHelper.mergeSubstitutions(
				deeperMap, higherMap);

		System.out.println(combinedMap);

		assertEquals(combinedMap.get(VAR_Y), CONSTRUCTED_Fa);
		assertEquals(combinedMap.get(VAR_X), TERM_A);
		assertEquals(combinedMap.size(), 2);
	}

}
