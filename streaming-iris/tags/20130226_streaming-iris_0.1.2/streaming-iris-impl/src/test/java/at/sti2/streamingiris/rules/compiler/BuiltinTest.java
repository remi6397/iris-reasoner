package at.sti2.streamingiris.rules.compiler;

import java.util.ArrayList;

import junit.framework.TestCase;
import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.builtins.IBuiltinAtom;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.builtins.AddBuiltin;
import at.sti2.streamingiris.factory.Factory;
import at.sti2.streamingiris.storage.IRelation;
import at.sti2.streamingiris.storage.simple.SimpleRelationFactory;
import at.sti2.streamingiris.utils.equivalence.IgnoreTermEquivalence;

public class BuiltinTest extends TestCase {

	protected void setUp() throws Exception {
	}

	public void testHasInputAllBound() throws Exception {
		Configuration configuration = new Configuration();
		IRelation inputRelation = configuration.relationFactory
				.createRelation();

		inputRelation.add(Helper.createTuple(1, 2, 3));
		inputRelation.add(Helper.createTuple(4, 4, 8));
		inputRelation.add(Helper.createTuple(1, 2, 4));
		inputRelation.add(Helper.createTuple(1, 3, 3));

		ITuple criteria = Helper.createTuple("X", "Y", "Z");
		View view = new View(inputRelation, criteria,
				configuration.relationFactory);

		IBuiltinAtom builtinPredicate = new AddBuiltin(
				criteria.toArray(new ITerm[0]));

		Builtin builtin = new Builtin(view.variables(), builtinPredicate, true,
				new IgnoreTermEquivalence(), configuration);

		IRelation result = builtin.process(view);

		assertEquals(2, result.size());
	}

	public void testTooManyUnbound() {
		Configuration configuration = new Configuration();
		IRelation inputRelation = configuration.relationFactory
				.createRelation();
		ITuple criteria = Helper.createTuple("X");
		View view = new View(inputRelation, criteria,
				configuration.relationFactory);

		ITuple builtinTuple = Helper.createTuple("X", "Y", "Z");
		IBuiltinAtom builtinPredicate = new AddBuiltin(
				builtinTuple.toArray(new ITerm[0]));

		try {
			new Builtin(view.variables(), builtinPredicate, true,
					new IgnoreTermEquivalence(), configuration);
			fail("Builtin should have thrown an exception");
		} catch (Exception e) // TODO Choose the proper exception type later
		{
		}
	}

	public void testHasInputOneUnbound() throws Exception {
		Configuration configuration = new Configuration();
		IRelation inputRelation = configuration.relationFactory
				.createRelation();

		inputRelation.add(Helper.createTuple(1, 2));
		inputRelation.add(Helper.createTuple(4, 4));
		inputRelation.add(Helper.createTuple(1, 2));
		inputRelation.add(Helper.createTuple(1, 3));

		ITuple criteria = Helper.createTuple("X", "Y");
		View view = new View(inputRelation, criteria,
				configuration.relationFactory);

		ITuple builtinTuple = Helper.createTuple("X", "Y", "Z");
		IBuiltinAtom builtinPredicate = new AddBuiltin(
				builtinTuple.toArray(new ITerm[0]));

		Builtin builtin = new Builtin(view.variables(), builtinPredicate, true,
				new IgnoreTermEquivalence(), configuration);

		IRelation output = builtin.process(view);

		assertEquals(3, output.size());
		assertEquals(Helper.createTerm("X"), builtin.getOutputVariables()
				.get(0));
		assertEquals(Helper.createTerm("Y"), builtin.getOutputVariables()
				.get(1));
		assertEquals(Helper.createTerm("Z"), builtin.getOutputVariables()
				.get(2));
	}

	public void testNoInput() throws Exception {
		Configuration configuration = new Configuration();
		ITuple builtinTuple = Helper.createTuple(3, 4, "X");
		IBuiltinAtom builtinPredicate = new AddBuiltin(
				builtinTuple.toArray(new ITerm[0]));

		Builtin builtin = new Builtin(new ArrayList<IVariable>(),
				builtinPredicate, true, new IgnoreTermEquivalence(),
				configuration);

		IRelation output = builtin.process(mRelationWithOneZeroLengthTuple);

		assertEquals(Helper.createTerm("X"), builtin.getOutputVariables()
				.get(0));

		assertEquals(1, output.size());
		assertEquals(Helper.createTerm(7), output.get(0).get(0));
	}

	private static final IRelation mRelationWithOneZeroLengthTuple = new SimpleRelationFactory()
			.createRelation();
	static {
		// Start the evaluation with a single, zero-length tuple.
		mRelationWithOneZeroLengthTuple.add(Factory.BASIC.createTuple());
	}
}
