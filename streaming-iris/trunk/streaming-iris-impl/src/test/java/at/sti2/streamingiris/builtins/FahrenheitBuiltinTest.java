package at.sti2.streamingiris.builtins;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.api.basics.ILiteral;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.builtins.IBuiltinAtom;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.compiler.BuiltinRegister;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.factory.Factory;

/**
 * <p>
 * Test the possibility of custom made builtins.
 * </p>
 * <p>
 * $Id: FahrenheitBuiltinTest.java,v 1.6 2007-10-30 08:28:31 poettler_ric Exp $
 * </p>
 * 
 * @version $Revision: 1.6 $
 * @author Richard Pöttler, richard dot poettler at deri dot org
 */
public class FahrenheitBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(FahrenheitBuiltinTest.class,
				FahrenheitBuiltinTest.class.getSimpleName());
	}

	public void testAdding() {

		final BuiltinRegister reg = new BuiltinRegister();
		reg.registerBuiltin(instance);
		final IPredicate ftoc = instance.getBuiltinPredicate();
		assertNotNull("It seems that the builtin wasn't registered correctly",
				reg.getBuiltinClass(ftoc.getPredicateSymbol()));
		assertEquals("The class of the builtin wasn't returned correctly",
				FahrenheitToCelsiusBuiltin.class,
				reg.getBuiltinClass(ftoc.getPredicateSymbol()));
		assertEquals("The arity of the builtin wasn't returned correctly",
				ftoc.getArity(), reg.getBuiltinArity(ftoc.getPredicateSymbol()));
	}

	public void testParsing() throws Exception {
		final BuiltinRegister reg = new BuiltinRegister();
		reg.registerBuiltin(instance);

		Parser parser = new Parser(reg);
		parser.parse("fahrenheit(?X) :- ftoc(?X, 10).");
		final ILiteral b = parser.getRules().iterator().next().getBody().get(0);
		assertTrue("The atom must be a IBuiltInAtom",
				b.getAtom() instanceof IBuiltinAtom);
	}

	private final ITerm t1 = Factory.TERM.createVariable("a");
	private final ITerm t2 = Factory.TERM.createVariable("a");
	private final FahrenheitToCelsiusBuiltin instance = new FahrenheitToCelsiusBuiltin(
			t1, t2);
}
