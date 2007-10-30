package org.deri.iris;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.BUILTIN;
import static org.deri.iris.factory.Factory.TERM;
import java.util.ArrayList;
import java.util.List;
import org.deri.iris.RuleBase;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.ILiteral;
import junit.framework.TestCase;

public class RuleBaseTest extends TestCase
{
	public void testStratify() throws Exception {

		final String stratProg = "p(?X) :- r(?X).\n" + 
			"p(?X) :- p(?X).\n" + 
			"q(?X) :- s(?X), !p(?X).";
		final IProgram e0 = org.deri.iris.compiler.Parser.parse(stratProg);
		assertEquals(true, e0.isStratified());
		
		final String unstratProg = "p(?X) :- r(?X), !q(?X).\n" + 
			"q(?X) :- r(?X), !p(?X).";
		final IProgram e1 = org.deri.iris.compiler.Parser.parse(unstratProg);
		assertEquals(false, e1.isStratified());
	}
	
	/*
	 * check safness of various rules
	 */
	
	public void testSafenessfromOwnRule() {
		
		// s(x, y) :- p(x, z), s(y, z)
		final List<ILiteral> head = new ArrayList<ILiteral>();

		head.add(MiscHelper.createLiteral("s", "X", "Y"));

		final List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(MiscHelper.createLiteral("p", "X", "Z"));
		body.add(MiscHelper.createLiteral("r", "Y", "Z"));

		assertTrue( isSafe( head, body ) );
	}
	
	public void testSafenessBuiltin_Greater() {
		
		// biggerthan(X, Y) :- X > Y -> not safe
		final List<ILiteral> head = new ArrayList<ILiteral>();

		head.add(MiscHelper.createLiteral("biggerthan", "X", "Y"));

		final List<ILiteral> body = new ArrayList<ILiteral>();
		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createGreater(TERM.createVariable("X"), TERM.createVariable("Y")));
		body.add(literal);
		
		assertFalse( isSafe( head, body ) );
	}

	public void testSafenessBuiltin_Equal() {
		
		// euqala(X, Y) :- X = Y, X = a -> safe 
		final List<ILiteral> head = new ArrayList<ILiteral>();
		
		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createVariable("Y")));
		head.add(literal);

		final List<ILiteral> body = new ArrayList<ILiteral>();
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		body.add(literal);

		assertTrue( isSafe( head, body ) );
	}
	public void testSafenessBuiltin_Equal2() {
		
		// equals(X, Y) :- X = Y, U = W, V = W, V = Y, X = a -> safe 
		final List<ILiteral> head = new ArrayList<ILiteral>();
		
		head.add(MiscHelper.createLiteral("equals", "X", "Y"));

		final List<ILiteral> body = new ArrayList<ILiteral>();
		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("W")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("V"), TERM.createVariable("W")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("V"), TERM.createVariable("Y")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		body.add(literal);

		assertTrue( isSafe( head, body ) );
	}
	public void testSafenessBuiltin_Equal2b() {
		
		// equals(X, Y) :- X = Y, U = W, b = W, X = Y, X = a -> safe 
		final List<ILiteral> head = new ArrayList<ILiteral>();
		
		head.add(MiscHelper.createLiteral("equals", "X", "Y"));

		final List<ILiteral> body = new ArrayList<ILiteral>();
		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("W")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createString("b"), TERM.createVariable("W")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createVariable("Y")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		body.add(literal);

		assertTrue( isSafe( head, body ) );
	}
	public void testSafenessBuiltin_Equal3() {
		
		// equals(X, Y) :- X = Y, U = W, V = W, X = a -> not safe 
		final List<ILiteral> head = new ArrayList<ILiteral>();
		
		head.add(MiscHelper.createLiteral("equals", "X", "Y"));

		final List<ILiteral> body = new ArrayList<ILiteral>();
		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("W")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("V"), TERM.createVariable("W")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		body.add(literal);

		assertFalse( isSafe( head, body ) );
	}
	public void testSafenessBuiltin_Equal3b() {
		
		// equals(X, Y) :- X = Y, U = W, V = W, b = Y, X = a -> not safe 
		final List<ILiteral> head = new ArrayList<ILiteral>();
		
		head.add(MiscHelper.createLiteral("equals", "X", "Y"));

		final List<ILiteral> body = new ArrayList<ILiteral>();
		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("W")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("V"), TERM.createVariable("W")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createString("b"), TERM.createVariable("Y")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		body.add(literal);

		assertFalse( isSafe( head, body ) );
	}
	public void testSafenessBuiltin_containsGreater() {
		
		// s(X, Y) :- Y = X, V = Y, V > Y, X = a -> save
		final List<ILiteral> head = new ArrayList<ILiteral>();
		
		head.add(MiscHelper.createLiteral("s", "X", "Y"));

		final List<ILiteral> body = new ArrayList<ILiteral>();
		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("V"), TERM.createVariable("Y")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createGreater(TERM.createVariable("V"), TERM.createVariable("Y")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		body.add(literal);

		assertTrue( isSafe( head, body ) );
	}

	public void testSafe_Variable_InNegatedSubGoal_NotInRuleHead_NotInPositiveLiteral() throws Exception
    {
		// b( ?X ) :- p( ?X ),not q( ?X, ?Y )
		// Head
		final List<ILiteral> head = new ArrayList<ILiteral>();
		head.add(MiscHelper.createLiteral("b", "X"));

		final List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(MiscHelper.createLiteral("p", "X"));
		body.add(MiscHelper.createLiteral(false, "q", "X", "Y"));

		assertTrue( isSafe( head, body ) );
    }
	
	public void testSafe_Variable_InNegatedSubGoal_InRuleHead_InPositiveLiteral() throws Exception
    {
		// w(?X,?Y) :- s(?X), r(?Y), not p(?X,?Y)
		// Head
		final List<ILiteral> head = new ArrayList<ILiteral>();
		head.add(MiscHelper.createLiteral("w", "X", "Y"));

		// Body
		final List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(MiscHelper.createLiteral("s", "X"));
		body.add(MiscHelper.createLiteral("r", "Y"));
		body.add(MiscHelper.createLiteral(false, "p", "X", "Y"));

		assertTrue( isSafe( head, body ) );
    }
	
	public void testUnsafe_Variable_InNegatedSubGoal_InRuleHead_NotInPositiveLiteral() throws Exception
    {
		// w(?X,?Y) :- s(?X), not p(?X,?Y)
		// Head
		final List<ILiteral> head = new ArrayList<ILiteral>();
		head.add(MiscHelper.createLiteral("w", "X", "Y"));
		
		// Body
		final List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(MiscHelper.createLiteral("s", "X"));
		body.add(MiscHelper.createLiteral(false, "p", "X", "Y"));

		assertFalse( isSafe( head, body ) );
    }
	
    public void testUnsafe_Variable_InHead_NotInBody()
    {
    	// p( ?X, ?Y ) :- q( ?X ).

		// Head
		final List<ILiteral> head = new ArrayList<ILiteral>();
		head.add(MiscHelper.createLiteral("w", "X", "Y"));
		
		// Body
		final List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(MiscHelper.createLiteral("q", "X"));

		assertFalse( isSafe( head, body ) );
    }
	
	public void testUnsafe_VariableInBuiltinButNotInPositiveLiteral()
	{
		//less(?X, ?Y) :- id(?X), ?X < ?Y.
		// Head
		final List<ILiteral> head = new ArrayList<ILiteral>();
		head.add(MiscHelper.createLiteral("less", "X", "Y"));

		// Body
		final List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(MiscHelper.createLiteral("id", "X"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createLess(TERM.createVariable("X"), TERM.createVariable("Y")));
		body.add(literal);

		assertFalse( isSafe( head, body ) );
	}
	
	public void testSafe_AllVariablesInBuiltinAlsoInPositiveLiteral()
	{
		//less(?X, ?Y) :- id(?X), id(?Y), ?X < ?Y.
		// Head
		final List<ILiteral> head = new ArrayList<ILiteral>();
		head.add(MiscHelper.createLiteral("less", "X", "Y"));

		// Body
		final List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(MiscHelper.createLiteral("id", "X"));
		body.add(MiscHelper.createLiteral("id", "Y"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createLess(TERM.createVariable("X"), TERM.createVariable("Y")));
		body.add(literal);

		assertTrue( isSafe( head, body ) );
	}
	
	public void testSafeness_allTogether() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), r(L, K), X = Y, U = V, W = U -> safe 
		final List<ILiteral> head = new ArrayList<ILiteral>();
		head.add(MiscHelper.createLiteral("m", "X", "Y", "U", "V"));

		final List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(MiscHelper.createLiteral("p", "W", "Z"));
		body.add(MiscHelper.createLiteral("q", "X", "Z"));
		body.add(MiscHelper.createLiteral("r", "L", "K"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("V")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("W"), TERM.createVariable("U")));
		body.add(literal);

		assertTrue( isSafe( head, body ) );
	}

	public void testSafeness_negatedOrdinaryPredicate() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), !r(L, K), X = Y, U = V, W = U -> safe 
		final List<ILiteral> head = new ArrayList<ILiteral>();
		head.add(MiscHelper.createLiteral("m", "X", "Y", "U", "V"));

		final List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(MiscHelper.createLiteral("p", "W", "Z"));
		body.add(MiscHelper.createLiteral("q", "X", "Z"));
		body.add(MiscHelper.createLiteral(false, "r", "L", "K"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("V")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("W"), TERM.createVariable("U")));
		body.add(literal);

		assertTrue( isSafe( head, body ) );
	}
	public void testSafeness_negatedBuiltin() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), r(L, K), X = Y, !U = V, W = U -> not safe 
		final List<ILiteral> head = new ArrayList<ILiteral>();
		head.add(MiscHelper.createLiteral("m", "X", "Y", "U", "V"));

		final List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(MiscHelper.createLiteral("p", "W", "Z"));
		body.add(MiscHelper.createLiteral("q", "X", "Z"));
		body.add(MiscHelper.createLiteral("r", "L", "K"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		body.add(literal);
		literal = BASIC.createLiteral(false, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("V")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("W"), TERM.createVariable("U")));
		body.add(literal);

		assertFalse( isSafe( head, body ) );
	}
	public void testSafeness_withNegation() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), !r(L, K), X = Y, !U = V, W = U -> not safe 
		final List<ILiteral> head = new ArrayList<ILiteral>();
		head.add(MiscHelper.createLiteral("m", "X", "Y", "U", "V"));

		final List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(MiscHelper.createLiteral("p", "W", "Z"));
		body.add(MiscHelper.createLiteral("q", "X", "Z"));
		body.add(MiscHelper.createLiteral("r", "L", "K"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		body.add(literal);
		literal = BASIC.createLiteral(false, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("V")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("W"), TERM.createVariable("U")));
		body.add(literal);

		assertFalse( isSafe( head, body ) );
	}
	public void testSafeness_withNegationofNE() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), !r(L, K), X = Y, !U != V, W = U -> V not safe 
		final List<ILiteral> head = new ArrayList<ILiteral>();
		head.add(MiscHelper.createLiteral("m", "X", "Y", "U", "V"));

		final List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(MiscHelper.createLiteral("p", "W", "Z"));
		body.add(MiscHelper.createLiteral("q", "X", "Z"));
		body.add(MiscHelper.createLiteral(false, "r", "L", "K"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		body.add(literal);
		literal = BASIC.createLiteral(false, BUILTIN.createUnequal(TERM.createVariable("U"), TERM.createVariable("V")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("W"), TERM.createVariable("U")));
		body.add(literal);

		assertFalse( isSafe( head, body ) );
	}
	public void testSafeness_withNegationofotherBuiltin() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), !r(L, K), X = Y, !U > V, W = U -> safe 
		final List<ILiteral> head = new ArrayList<ILiteral>();
		head.add(MiscHelper.createLiteral("m", "X", "Y", "U", "V"));

		final List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(MiscHelper.createLiteral("p", "W", "Z"));
		body.add(MiscHelper.createLiteral("q", "X", "Z"));
		body.add(MiscHelper.createLiteral(false, "r", "L", "K"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		body.add(literal);
		literal = BASIC.createLiteral(false, BUILTIN.createGreater(TERM.createVariable("U"), TERM.createVariable("V")));
		body.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("W"), TERM.createVariable("U")));
		body.add(literal);

		assertFalse( isSafe( head, body ) );
	}

	private boolean isSafe(final List<ILiteral> head, final List<ILiteral> body) {
		RuleBase ruleBase = new RuleBase();
		ruleBase.addRule( BASIC.createRule( head, body ) );
		return ruleBase.checkAllRulesSafe();
	}
}
