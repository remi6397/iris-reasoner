package at.sti2.streamingiris.basics;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.MiscHelper;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IConstructedTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.IVariable;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date 24.07.2006 14:19:17
 * 
 */
public class TupleTest extends TestCase {

	private static final int ARITY = 3;

	private static final ITuple REFERENCE = MiscHelper.createTuple("a", "b",
			"c");

	private static final ITuple TUPLE = MiscHelper.createTuple("a", "b", "c");

	private static final ITuple MORE = MiscHelper.createTuple("a", "b", "d");

	private static final ITuple EVENMORE = MiscHelper.createTuple("a", "b",
			"d", "e");

	public static Test suite() {
		return new TestSuite(TupleTest.class, TupleTest.class.getSimpleName());
	}

	public void testSize() {
		assertEquals("The size method doesn't work properly", ARITY,
				TUPLE.size());
	}

	public void testGet() {
		assertEquals("The get method doesn't work properly",
				TERM.createString("a"), TUPLE.get(0));
		assertEquals("The get method doesn't work properly",
				TERM.createString("b"), TUPLE.get(1));
		assertEquals("The get method doesn't work properly",
				TERM.createString("c"), TUPLE.get(2));
	}

	public void testEquals() {
		ObjectTests.runTestEquals(REFERENCE, TUPLE, MORE);
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(REFERENCE, TUPLE, MORE, EVENMORE);
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(REFERENCE, TUPLE);
	}

	public void testVariables() {
		Set<IVariable> variables = new HashSet<IVariable>();
		IVariable x = TERM.createVariable("X");
		IVariable y = TERM.createVariable("Y");

		variables.add(x);
		variables.add(y);

		IConstructedTerm c1 = TERM.createConstruct("c1", y);
		IConstructedTerm c2 = TERM.createConstruct("c2", c1, x);
		List<ITerm> terms = new ArrayList<ITerm>();
		terms.addAll(REFERENCE);
		terms.add(c2);

		assertEquals(variables, BASIC.createTuple(terms).getVariables());
	}
}
