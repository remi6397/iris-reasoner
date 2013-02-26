package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import static at.sti2.streamingiris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.KnowledgeBaseFactory;
import at.sti2.streamingiris.api.IKnowledgeBase;
import at.sti2.streamingiris.api.basics.ILiteral;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.factory.Factory;
import at.sti2.streamingiris.storage.IRelation;
import at.sti2.streamingiris.storage.simple.SimpleRelationFactory;

/**
 * <p>
 * Tests for the {@code MultiplyBuiltin Evaluation} coupled with LessBuiltin.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 10.05.2007 10:23:07
 */
public class MultiplyBuiltinEvaluationTest extends TestCase {

	public static Test suite() {
		return new TestSuite(MultiplyBuiltinEvaluationTest.class,
				MultiplyBuiltinEvaluationTest.class.getSimpleName());
	}

	public void testEvaluate0() throws Exception {
		// constructing the rules
		List<IRule> rules = new ArrayList<IRule>(1);
		// p(?S,?K) :- p1(?S,?I), less(?I, 10), multiply(?I,?I,?K), p2(?S).
		List<ILiteral> h = Arrays.asList(createLiteral("p", "S", "K"));
		List<ILiteral> b = Arrays.asList(createLiteral("p1", "S", "I"),
				Factory.BASIC.createLiteral(true, Factory.BUILTIN.createLess(
						TERM.createVariable("I"), CONCRETE.createInteger(10))),
				Factory.BASIC.createLiteral(
						true,
						Factory.BUILTIN.createMultiplyBuiltin(
								TERM.createVariable("I"),
								TERM.createVariable("I"),
								TERM.createVariable("K"))),
				createLiteral("p2", "S"));

		IRule r = Factory.BASIC.createRule(h, b);
		rules.add(r);

		// create facts
		Map<IPredicate, IRelation> facts = new HashMap<IPredicate, IRelation>();
		// p1(a,1), p1(b,2), p1(c,3), p1(d,12)
		IPredicate p = Factory.BASIC.createPredicate("p1", 2);
		IRelation rel = new SimpleRelationFactory().createRelation();
		rel.add(BASIC.createTuple(TERM.createString("a"),
				CONCRETE.createInteger(1)));
		rel.add(BASIC.createTuple(TERM.createString("b"),
				CONCRETE.createInteger(2)));
		rel.add(BASIC.createTuple(TERM.createString("c"),
				CONCRETE.createInteger(3)));
		rel.add(BASIC.createTuple(TERM.createString("d"),
				CONCRETE.createInteger(12)));
		facts.put(p, rel);

		// p2(b)
		p = Factory.BASIC.createPredicate("p2", 1);
		rel = new SimpleRelationFactory().createRelation();
		rel.add(BASIC.createTuple(TERM.createString("b")));
		facts.put(p, rel);

		// p(?S,?K)
		IQuery q = Factory.BASIC.createQuery(createLiteral("p", "S", "K"));
		Set<IQuery> queries = new HashSet<IQuery>(1);
		queries.add(q);
		final IKnowledgeBase pr = KnowledgeBaseFactory.createKnowledgeBase(
				facts, rules);

		// Result: p(b,4)
		IRelation res = new SimpleRelationFactory().createRelation();
		res.add(BASIC.createTuple(TERM.createString("b"),
				CONCRETE.createInteger(4)));

		System.out.println("******** TEST 0: ********");
		ExecutionHelper.executeTest(pr, q, res);

		pr.shutdown();
	}

	/**
	 * Creates a positive literal out of a predicate name and a set of variable
	 * strings.
	 * 
	 * @param pred
	 *            the predicate name
	 * @param vars
	 *            the variable names
	 * @return the constructed literal
	 * @throws NullPointerException
	 *             if the predicate name or the set of variable names is
	 *             {@code null}
	 * @throws NullPointerException
	 *             if the set of variable names contains {@code null}
	 * @throws IllegalArgumentException
	 *             if the name of the predicate is 0 characters long
	 */
	private static ILiteral createLiteral(final String pred,
			final String... vars) {
		if ((pred == null) || (vars == null)) {
			throw new NullPointerException(
					"The predicate and the vars must not be null");
		}
		if (pred.length() <= 0) {
			throw new IllegalArgumentException(
					"The predicate name must be longer than 0 chars");
		}
		if (Arrays.asList(vars).contains(null)) {
			throw new NullPointerException("The vars must not contain null");
		}

		return BASIC.createLiteral(true,
				BASIC.createPredicate(pred, vars.length),
				BASIC.createTuple(new ArrayList<ITerm>(createVarList(vars))));
	}

	/**
	 * Creates a list of IVariables out of a list of strings.
	 * 
	 * @param vars
	 *            the variable names
	 * @return the list of correspoinding variables
	 * @throws NullPointerException
	 *             if the vars is null, or contains null
	 */
	private static List<IVariable> createVarList(final String... vars) {
		if ((vars == null) || Arrays.asList(vars).contains(null)) {
			throw new NullPointerException(
					"The vars must not be null and must not contain null");
		}
		final List<IVariable> v = new ArrayList<IVariable>(vars.length);
		for (final String var : vars) {
			v.add(TERM.createVariable(var));
		}
		return v;
	}
}
