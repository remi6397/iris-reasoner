package at.sti2.streamingiris.basics;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.MiscHelper;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.api.basics.ILiteral;

/**
 * <p>
 * Tests for the rule.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class RuleTest extends TestCase {

	private List<ILiteral> HEAD;

	private List<ILiteral> BODY;

	/**
	 * setup for Rule2Relation tests
	 */
	public void setUp() {
		HEAD = new ArrayList<ILiteral>();
		ILiteral literal = BASIC.createLiteral(
				true,
				BASIC.createAtom(BASIC.createPredicate("sin", 1),
						BASIC.createTuple(CONCRETE.createInteger(1))));
		HEAD.add(literal);
		HEAD.add(MiscHelper.createLiteral("cos", "X"));
		literal = BASIC.createLiteral(true,
				BASIC.createAtom(BASIC.createPredicate("date", 3), BASIC
						.createTuple(CONCRETE.createInteger(2005),
								CONCRETE.createInteger(12),
								CONCRETE.createInteger(24))));
		HEAD.add(literal);

		BODY = new ArrayList<ILiteral>();
		literal = BASIC.createLiteral(
				true,
				BASIC.createAtom(BASIC.createPredicate("sin", 1),
						BASIC.createTuple(CONCRETE.createInteger(1))));
		BODY.add(literal);
		BODY.add(MiscHelper.createLiteral("cos", "X"));
		BODY.add(MiscHelper.createLiteral("date", "J", "K", "L"));
	}

	public static Test suite() {
		return new TestSuite(RuleTest.class, RuleTest.class.getSimpleName());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new Rule(HEAD, BODY), new Rule(HEAD, BODY),
				new Rule(HEAD, HEAD));
		ObjectTests.runTestEquals(new Rule(HEAD, BODY), new Rule(HEAD, BODY),
				new Rule(BODY, BODY));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Rule(HEAD, BODY), new Rule(HEAD, BODY));
	}

}
