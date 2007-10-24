package org.deri.iris.evaluation;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.BUILTIN;
import static org.deri.iris.factory.Factory.TERM;
import java.util.ArrayList;
import java.util.List;
import org.deri.iris.MiscHelper;
import org.deri.iris.RuleBase;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
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
		List<ILiteral> literals = new ArrayList<ILiteral>();

		literals.add(MiscHelper.createLiteral("s", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();

		literals.add(MiscHelper.createLiteral("p", "X", "Z"));
		literals.add(MiscHelper.createLiteral("r", "Y", "Z"));

		IBody body = BASIC.createBody(literals);
		
		assertTrue( isSafe( head, body ) );
	}
	
	public void testSafenessBuiltin_Greater() {
		
		// biggerthan(X, Y) :- X > Y -> not safe
		List<ILiteral> literals = new ArrayList<ILiteral>();

		literals.add(MiscHelper.createLiteral("biggerthan", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createGreater(TERM.createVariable("X"), TERM.createVariable("Y")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);
		
		assertFalse( isSafe( head, body ) );
	}

	public void testSafenessBuiltin_Equal() {
		
		// euqala(X, Y) :- X = Y, X = a -> safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createVariable("Y")));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);

		assertTrue( isSafe( head, body ) );
	}
	public void testSafenessBuiltin_Equal2() {
		
		// equals(X, Y) :- X = Y, U = W, V = W, V = Y, X = a -> safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		literals.add(MiscHelper.createLiteral("equals", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("W")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("V"), TERM.createVariable("W")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("V"), TERM.createVariable("Y")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);

		assertTrue( isSafe( head, body ) );
	}
	public void testSafenessBuiltin_Equal2b() {
		
		// equals(X, Y) :- X = Y, U = W, b = W, X = Y, X = a -> safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		literals.add(MiscHelper.createLiteral("equals", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("W")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createString("b"), TERM.createVariable("W")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createVariable("Y")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);

		assertTrue( isSafe( head, body ) );
	}
	public void testSafenessBuiltin_Equal3() {
		
		// equals(X, Y) :- X = Y, U = W, V = W, X = a -> not safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		literals.add(MiscHelper.createLiteral("equals", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("W")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("V"), TERM.createVariable("W")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);

		assertFalse( isSafe( head, body ) );
	}
	public void testSafenessBuiltin_Equal3b() {
		
		// equals(X, Y) :- X = Y, U = W, V = W, b = Y, X = a -> not safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		literals.add(MiscHelper.createLiteral("equals", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("W")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("V"), TERM.createVariable("W")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createString("b"), TERM.createVariable("Y")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);

		assertFalse( isSafe( head, body ) );
	}
	public void testSafenessBuiltin_containsGreater() {
		
		// s(X, Y) :- Y = X, V = Y, V > Y, X = a -> save
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		literals.add(MiscHelper.createLiteral("s", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("V"), TERM.createVariable("Y")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createGreater(TERM.createVariable("V"), TERM.createVariable("Y")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);

		assertTrue( isSafe( head, body ) );
	}

	public void testSafe_Variable_InNegatedSubGoal_NotInRuleHead_NotInPositiveLiteral() throws Exception
    {
		// b( ?X ) :- p( ?X ),not q( ?X, ?Y )
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		// Head
		literals.add(MiscHelper.createLiteral("b", "X"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		literals.add(MiscHelper.createLiteral("p", "X"));
		literals.add(MiscHelper.createLiteral(false, "q", "X", "Y"));

		IBody body = BASIC.createBody(literals);

		assertTrue( isSafe( head, body ) );
    }
	
	public void testSafe_Variable_InNegatedSubGoal_InRuleHead_InPositiveLiteral() throws Exception
    {
		// w(?X,?Y) :- s(?X), r(?Y), not p(?X,?Y)
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		// Head
		literals.add(MiscHelper.createLiteral("w", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		// Body
		literals.add(MiscHelper.createLiteral("s", "X"));
		literals.add(MiscHelper.createLiteral("r", "Y"));
		literals.add(MiscHelper.createLiteral(false, "p", "X", "Y"));

		IBody body = BASIC.createBody(literals);

		assertTrue( isSafe( head, body ) );
    }
	
	public void testUnsafe_Variable_InNegatedSubGoal_InRuleHead_NotInPositiveLiteral() throws Exception
    {
		// w(?X,?Y) :- s(?X), not p(?X,?Y)
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		// Head
		literals.add(MiscHelper.createLiteral("w", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		// Body
		literals.add(MiscHelper.createLiteral("s", "X"));
		literals.add(MiscHelper.createLiteral(false, "p", "X", "Y"));

		IBody body = BASIC.createBody(literals);

		assertFalse( isSafe( head, body ) );
    }
	
    public void testUnsafe_Variable_InHead_NotInBody()
    {
    	// p( ?X, ?Y ) :- q( ?X ).

		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		// Head
		literals.add(MiscHelper.createLiteral("w", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		// Body
		literals.add(MiscHelper.createLiteral("q", "X"));

		IBody body = BASIC.createBody(literals);

		assertFalse( isSafe( head, body ) );
    }
	
	public void testUnsafe_VariableInBuiltinButNotInPositiveLiteral()
	{
		//less(?X, ?Y) :- id(?X), ?X < ?Y.
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		// Head
		literals.add(MiscHelper.createLiteral("less", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		// Body
		literals.add(MiscHelper.createLiteral("id", "X"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createLess(TERM.createVariable("X"), TERM.createVariable("Y")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);

		assertFalse( isSafe( head, body ) );
	}
	
	public void testSafe_AllVariablesInBuiltinAlsoInPositiveLiteral()
	{
		//less(?X, ?Y) :- id(?X), id(?Y), ?X < ?Y.
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		// Head
		literals.add(MiscHelper.createLiteral("less", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		// Body
		literals.add(MiscHelper.createLiteral("id", "X"));
		literals.add(MiscHelper.createLiteral("id", "Y"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createLess(TERM.createVariable("X"), TERM.createVariable("Y")));
		literals.add(literal);

		IBody body = BASIC.createBody(literals);

		assertTrue( isSafe( head, body ) );
	}
	
	public void testSafeness_allTogether() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), r(L, K), X = Y, U = V, W = U -> safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		literals.add(MiscHelper.createLiteral("m", "X", "Y", "U", "V"));

		IHead head = BASIC.createHead(literals);

		literals.clear();

		literals.add(MiscHelper.createLiteral("p", "W", "Z"));
		literals.add(MiscHelper.createLiteral("q", "X", "Z"));
		literals.add(MiscHelper.createLiteral("r", "L", "K"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("V")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("W"), TERM.createVariable("U")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);

		assertTrue( isSafe( head, body ) );
	}

	public void testSafeness_negatedOrdinaryPredicate() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), !r(L, K), X = Y, U = V, W = U -> safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		literals.add(MiscHelper.createLiteral("m", "X", "Y", "U", "V"));

		IHead head = BASIC.createHead(literals);

		literals.clear();

		literals.add(MiscHelper.createLiteral("p", "W", "Z"));
		literals.add(MiscHelper.createLiteral("q", "X", "Z"));
		literals.add(MiscHelper.createLiteral(false, "r", "L", "K"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("V")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("W"), TERM.createVariable("U")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);

		assertTrue( isSafe( head, body ) );
	}
	public void testSafeness_negatedBuiltin() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), r(L, K), X = Y, !U = V, W = U -> not safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		literals.add(MiscHelper.createLiteral("m", "X", "Y", "U", "V"));

		IHead head = BASIC.createHead(literals);

		literals.clear();

		literals.add(MiscHelper.createLiteral("p", "W", "Z"));
		literals.add(MiscHelper.createLiteral("q", "X", "Z"));
		literals.add(MiscHelper.createLiteral("r", "L", "K"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		literals.add(literal);
		literal = BASIC.createLiteral(false, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("V")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("W"), TERM.createVariable("U")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);

		assertFalse( isSafe( head, body ) );
	}
	public void testSafeness_withNegation() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), !r(L, K), X = Y, !U = V, W = U -> not safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		literals.add(MiscHelper.createLiteral("m", "X", "Y", "U", "V"));

		IHead head = BASIC.createHead(literals);

		literals.clear();

		literals.add(MiscHelper.createLiteral("p", "W", "Z"));
		literals.add(MiscHelper.createLiteral("q", "X", "Z"));
		literals.add(MiscHelper.createLiteral("r", "L", "K"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		literals.add(literal);
		literal = BASIC.createLiteral(false, BUILTIN.createEqual(TERM.createVariable("U"), TERM.createVariable("V")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("W"), TERM.createVariable("U")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);

		assertFalse( isSafe( head, body ) );
	}
	public void testSafeness_withNegationofNE() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), !r(L, K), X = Y, !U != V, W = U -> V not safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		literals.add(MiscHelper.createLiteral("m", "X", "Y", "U", "V"));

		IHead head = BASIC.createHead(literals);

		literals.clear();

		literals.add(MiscHelper.createLiteral("p", "W", "Z"));
		literals.add(MiscHelper.createLiteral("q", "X", "Z"));
		literals.add(MiscHelper.createLiteral(false, "r", "L", "K"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		literals.add(literal);
		literal = BASIC.createLiteral(false, BUILTIN.createUnequal(TERM.createVariable("U"), TERM.createVariable("V")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("W"), TERM.createVariable("U")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);

		assertFalse( isSafe( head, body ) );
	}
	public void testSafeness_withNegationofotherBuiltin() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), !r(L, K), X = Y, !U > V, W = U -> safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		literals.add(MiscHelper.createLiteral("m", "X", "Y", "U", "V"));

		IHead head = BASIC.createHead(literals);

		literals.clear();

		literals.add(MiscHelper.createLiteral("p", "W", "Z"));
		literals.add(MiscHelper.createLiteral("q", "X", "Z"));
		literals.add(MiscHelper.createLiteral(false, "r", "L", "K"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
		literals.add(literal);
		literal = BASIC.createLiteral(false, BUILTIN.createGreater(TERM.createVariable("U"), TERM.createVariable("V")));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("W"), TERM.createVariable("U")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);

		assertFalse( isSafe( head, body ) );
	}

	private boolean isSafe( IHead head, IBody body )
	{
		RuleBase ruleBase = new RuleBase();
		ruleBase.addRule( BASIC.createRule( head, body ) );
		return ruleBase.checkAllRulesSafe();
	}
}
