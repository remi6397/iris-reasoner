package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.builtins.IBuiltinAtom;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringReplaceBuiltin.
 */
public class StringReplaceBuiltinTest extends TestCase {

	public static ITerm W = TERM.createVariable("W");

	public static ITerm X = TERM.createVariable("X");

	public static ITerm Y = TERM.createVariable("Y");

	public static ITerm Z = TERM.createVariable("Z");

	public static ITerm R = TERM.createVariable("R");

	public StringReplaceBuiltinTest(String name) {
		super(name);
	}

	public void testReplace1() throws EvaluationException {
		check("a*cada*", "abracadabra", "bra", "*");
		check("*", "abracadabra", "a.*a", "*");
		check("*c*bra", "abracadabra", "a.*?a", "*");
		check("brcdbr", "abracadabra", "a", "");
		check("abbraccaddabbra", "abracadabra", "a(.)", "a$1$1");
		check("b", "AAAA", "A+", "b");
		check("bbbb", "AAAA", "A+?", "b");
		check("carted", "darted", "^(.*?)d(.*)$", "$1c$2");
		check("[1=ab][2=]cd", "abcd", "(ab)|(a)", "[1=$1][2=$2]");
	}

	public void testReplace2() throws EvaluationException {
		check("hello world", "hello world", "hello world", "foobar", "x");
		check("foobar", "helloworld", "hello world", "foobar", "x");
		check("helloworld", "helloworld", "hello[ ]world", "foobar", "x");
		check("foobar", "hello world", "hello\\ sworld", "foobar", "x");
	}

	private void check(String expected, String string, String regex,
			String replacement) throws EvaluationException {
		IBuiltinAtom replace = new StringReplaceWithoutFlagsBuiltin(
				TERM.createString(string), TERM.createString(regex),
				TERM.createString(replacement), R);

		assertEquals(
				Factory.BASIC.createTuple(Factory.TERM.createString(expected)),
				replace.evaluate(Factory.BASIC.createTuple(X, Y, Z, R)));
	}

	private void check(String expected, String string, String regex,
			String replacement, String flags) throws EvaluationException {
		IBuiltinAtom replace = new StringReplaceBuiltin(
				TERM.createString(string), TERM.createString(regex),
				TERM.createString(replacement), TERM.createString(flags), R);

		assertEquals(
				Factory.BASIC.createTuple(Factory.TERM.createString(expected)),
				replace.evaluate(Factory.BASIC.createTuple(W, X, Y, Z, R)));
	}
}
