/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.evaluation;

import static org.deri.iris.MiscHelper.createLiteral;
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.BUILTIN;
import static org.deri.iris.factory.Factory.TERM;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.basics.seminaive.ConstLiteral;

/**
 * <p>
 * Tests the methods in the MiscOps class.
 * </p>
 * <p>
 * $Id: MiscOpsTest.java,v 1.7 2007-09-27 14:52:03 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.7 $
 */
public class MiscOpsTest extends TestCase {

	// often used terms
	private static final ITerm X = TERM.createVariable("X");
	private static final ITerm Y = TERM.createVariable("Y");
	private static final ITerm Z = TERM.createVariable("Z");
	private static final ITerm X0 = TERM.createVariable("?X_0");
	private static final ITerm X1 = TERM.createVariable("?X_1");
	private static final ITerm X2 = TERM.createVariable("?X_2");
	private static final IPredicate p = BASIC.createPredicate("p", 2);

	public static TestSuite suite() {
		return new TestSuite(MiscOpsTest.class, MiscOpsTest.class
				.getSimpleName());
	}

	public void testRectrifySimple0() {
		final ILiteral hl = BASIC.createLiteral(true, BASIC.createPredicate(
				"p", 3), BASIC.createTuple(TERM.createString("a"), TERM
				.createVariable("X"), TERM.createVariable("Y")));
	 	// p(a, X, Y) :- r(X, Y)
		final IRule r0 = BASIC.createRule(BASIC.createHead(hl), BASIC
				.createBody(createLiteral("r", "X", "Y")));
	 	// p(?X_0, ?X_1, ?X_2) :- r(?X_1, ?X_2), ?X_0 = a
		final IRule rec0 = BASIC.createRule(BASIC.createHead(createLiteral("p",
				"?X_0", "?X_1", "?X_2")), BASIC.createBody(createLiteral("r",
				"?X_1", "?X_2"), new ConstLiteral(
						true,TERM.createString("a"),TERM.createVariable("?X_0"))));

		assertEquals(rec0, MiscOps.rectify(r0));
	}
		
	public void testRectifySimple1() {
	 	// p(X, Y, X) :- r(Y, X)
		final IRule r = BASIC
				.createRule(
						BASIC.createHead(createLiteral("p", "X", "Y", "X")),
						BASIC.createBody(createLiteral("r", "Y", "X")));
	 	// p(?X_0, ?X_1, ?X_2) :- r(?X_1, ?X_0), EQUAL(?X_0, ?X_2)
		final IRule rec = BASIC.createRule(BASIC.createHead(createLiteral("p",
				"?X_0", "?X_1", "?X_2")), BASIC.createBody(createLiteral("r",
				"?X_1", "?X_0"), BASIC.createLiteral(true, BUILTIN.createEqual(
				TERM.createVariable("?X_0"), TERM.createVariable("?X_2")))));

		assertEquals(rec, MiscOps.rectify(r));
	}


	public void testRectifySimple2() {
		// p(X, Y) :- p(X, Z), p(Z, Y)
		final IRule in = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, p, BASIC.createTuple(X, Y))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X, Z)), 
					BASIC.createLiteral(true, p, BASIC.createTuple(Z, Y))));
		final IRule inBackup = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, p, BASIC.createTuple(X, Y))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X, Z)), 
					BASIC.createLiteral(true, p, BASIC.createTuple(Z, Y))));
		// p(?X_0, ?X_1) :- p(?X_0, Z), p(Z, ?X_1)
		final IRule out = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, p, BASIC.createTuple(X0, X1))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X0, Z)), 
					BASIC.createLiteral(true, p, BASIC.createTuple(Z, X1))));

		assertEquals(out, MiscOps.rectify(in));
		// the original rule must not be altered
		assertEquals("The original rule must remain the same", inBackup, in);
	}


	public void testRectifyExtreme() {
		// q(X, X, X) :- p(X, X), p(X, X)
		final IRule in = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, BASIC.createPredicate("q", 3), BASIC.createTuple(X, X, X))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X, X)), 
					BASIC.createLiteral(true, p, BASIC.createTuple(X, X))));
		final IRule backup = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, BASIC.createPredicate("q", 3), BASIC.createTuple(X, X, X))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X, X)), 
					BASIC.createLiteral(true, p, BASIC.createTuple(X, X))));
		// q(?X_0, ?X_1, ?X_2) :- p(?X_0, ?X_0), p(?X_0, ?X_0), EQUAL(?X_0, ?X_1), EQUAL(?X_1, ?X_2)
		final IRule out = BASIC.createRule(
				BASIC.createHead(BASIC.createLiteral(true, BASIC.createPredicate("q", 3), BASIC.createTuple(X0, X1, X2))), 
				BASIC.createBody(
					BASIC.createLiteral(true, p, BASIC.createTuple(X0, X0)), 
					BASIC.createLiteral(true, p, BASIC.createTuple(X0, X0)), 
					BASIC.createLiteral(true, BUILTIN.createEqual(X0, X1)), 
					BASIC.createLiteral(true, BUILTIN.createEqual(X1, X2))));

		assertEquals(out, MiscOps.rectify(in));
		// the original rule must not be altered
		assertEquals("The original rule must remain the same", backup, in);
	}

	public void testRectiryBuiltin() {
		// p(X, Y) :- p(X, Z), Z = Y
		final IRule in = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, p, BASIC.createTuple(X, Y))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X, Z)), 
					BASIC.createLiteral(true, BUILTIN.createEqual(Z, Y))));
		final IRule backup = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, p, BASIC.createTuple(X, Y))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X, Z)), 
					BASIC.createLiteral(true, BUILTIN.createEqual(Z, Y))));
		// p(?X_0, ?X_1) :- p(?X_0, Z), Z = ?X_1
		final IRule out = BASIC.createRule(BASIC.createHead(BASIC.createLiteral(true, p, BASIC.createTuple(X0, X1))), 
				BASIC.createBody(BASIC.createLiteral(true, p, BASIC.createTuple(X0, Z)), 
					BASIC.createLiteral(true, BUILTIN.createEqual(Z, X1))));

		assertEquals(out, MiscOps.rectify(in));
		// the original rule must not be altered
		assertEquals("The original rule must remain the same", backup, in);
	}
	
	public void testStratify() throws Exception {

		final String stratProg = "p(?X) :- r(?X).\n" + 
			"p(?X) :- p(?X).\n" + 
			"q(?X) :- s(?X), !p(?X).";
		final IProgram e0 = org.deri.iris.compiler.Parser.parse(stratProg);
		assertEquals(true, MiscOps.stratify(e0));
		
		final String unstratProg = "p(?X) :- r(?X), !q(?X).\n" + 
			"q(?X) :- r(?X), !p(?X).";
		final IProgram e1 = org.deri.iris.compiler.Parser.parse(unstratProg);
		assertEquals(false, MiscOps.stratify(e1));
	}
	
	/*
	 * check safness of various rules
	 */
	
	public void testSafenessfromOwnRule() {
		
		// s(x, y) :- p(x, z), s(y, z)
		List<ILiteral> literals = new ArrayList<ILiteral>();

		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("s", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		literal = BASIC.createLiteral(true, BASIC.createPredicate("p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);

		literal = BASIC.createLiteral(true, BASIC.createPredicate("r", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("Y"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);
		
		assertTrue( isSafe( head, body ) );
	}
	
	public void testSafenessBuiltin_Greater() {
		
		// biggerthan(X, Y) :- X > Y -> not safe
		List<ILiteral> literals = new ArrayList<ILiteral>();

		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("biggerthan", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		literal = BASIC.createLiteral(true, BUILTIN.createGreater(TERM.createVariable("X"), TERM.createVariable("Y")));
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
		
		// euqala(X, Y) :- X = Y, U = W, V = W, V = Y, X = a -> safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("s", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
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
		
		// euqala(X, Y) :- X = Y, U = W, b = W, X = Y, X = a -> safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("s", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
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
		
		// euqala(X, Y) :- X = Y, U = W, V = W, X = a -> not safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("s", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
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
		
		// euqala(X, Y) :- X = Y, U = W, V = W, b = Y, X = a -> not safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("s", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
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
		
		// euqala(X, Y) :- X = Y, X = a -> safe 
		// s(X, Y) :- Y = X, V = Y, V > Y, X = a
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("s", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
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
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("b", 1));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		literal = BASIC.createLiteral(true, BASIC.createPredicate("p", 1));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literals.add(literal);
		
		literal = BASIC.createLiteral(false, BASIC.createPredicate("q", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);

		IBody body = BASIC.createBody(literals);

		assertTrue( isSafe( head, body ) );
    }
	
	public void testSafe_Variable_InNegatedSubGoal_InRuleHead_InPositiveLiteral() throws Exception
    {
		// w(?X,?Y) :- s(?X), r(?Y), not p(?X,?Y)
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		// Head
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("w", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		// Body
		literal = BASIC.createLiteral(true, BASIC.createPredicate("s", 1));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literals.add(literal);
		
		literal = BASIC.createLiteral(true, BASIC.createPredicate("r", 1));
		literal.getTuple().setTerm(0, TERM.createVariable("Y"));
		literals.add(literal);
		
		literal = BASIC.createLiteral(false, BASIC.createPredicate("p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		
		IBody body = BASIC.createBody(literals);

		assertTrue( isSafe( head, body ) );
    }
	
	public void testUnsafe_Variable_InNegatedSubGoal_InRuleHead_NotInPositiveLiteral() throws Exception
    {
		// w(?X,?Y) :- s(?X), not p(?X,?Y)
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		// Head
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("w", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		// Body
		literal = BASIC.createLiteral(true, BASIC.createPredicate("s", 1));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literals.add(literal);
		
		literal = BASIC.createLiteral(false, BASIC.createPredicate("p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		
		IBody body = BASIC.createBody(literals);

		assertFalse( isSafe( head, body ) );
    }
	
    public void testUnsafe_Variable_InHead_NotInBody()
    {
    	// p( ?X, ?Y ) :- q( ?X ).

		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		// Head
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		// Body
		literal = BASIC.createLiteral(true, BASIC.createPredicate("q", 1));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literals.add(literal);
		
		IBody body = BASIC.createBody(literals);

		assertFalse( isSafe( head, body ) );
    }
	
	public void testUnsafe_VariableInBuiltinButNotInPositiveLiteral()
	{
		//less(?X, ?Y) :- id(?X), ?X < ?Y.
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		// Head
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("less", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		// Body
		literal = BASIC.createLiteral(true, BASIC.createPredicate("id", 1));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literals.add(literal);
		
		literal = BASIC.createLiteral(true, BUILTIN.createLess(TERM.createVariable("X"), TERM.createVariable("Y")));
		literals.add(literal);
		IBody body = BASIC.createBody(literals);

		assertFalse( isSafe( head, body ) );
	}
	
	public void testSafe_AllVariablesInBuiltinAlsoInPositiveLiteral()
	{
		//less(?X, ?Y) :- id(?X), id(?Y), ?X < ?Y.
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		// Head
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("less", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		// Body
		literal = BASIC.createLiteral(true, BASIC.createPredicate("id", 1));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literals.add(literal);
		
		literal = BASIC.createLiteral(true, BASIC.createPredicate("id", 1));
		literal.getTuple().setTerm(0, TERM.createVariable("Y"));
		literals.add(literal);
		
		literal = BASIC.createLiteral(true, BUILTIN.createLess(TERM.createVariable("X"), TERM.createVariable("Y")));
		literals.add(literal);

		IBody body = BASIC.createBody(literals);

		assertTrue( isSafe( head, body ) );
	}
	
	public void testSafeness_allTogether() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), r(L, K), X = Y, U = V, W = U -> safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("m", 4));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literal.getTuple().setTerm(2, TERM.createVariable("U"));
		literal.getTuple().setTerm(3, TERM.createVariable("V"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		literal = BASIC.createLiteral(true, BASIC.createPredicate("p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("W"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BASIC.createPredicate("q", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BASIC.createPredicate("r", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("L"));
		literal.getTuple().setTerm(1, TERM.createVariable("K"));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
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
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("m", 4));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literal.getTuple().setTerm(2, TERM.createVariable("U"));
		literal.getTuple().setTerm(3, TERM.createVariable("V"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		literal = BASIC.createLiteral(true, BASIC.createPredicate("p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("W"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BASIC.createPredicate("q", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);
		literal = BASIC.createLiteral(false, BASIC.createPredicate("r", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("L"));
		literal.getTuple().setTerm(1, TERM.createVariable("K"));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
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
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("m", 4));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literal.getTuple().setTerm(2, TERM.createVariable("U"));
		literal.getTuple().setTerm(3, TERM.createVariable("V"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		literal = BASIC.createLiteral(true, BASIC.createPredicate("p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("W"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BASIC.createPredicate("q", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BASIC.createPredicate("r", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("L"));
		literal.getTuple().setTerm(1, TERM.createVariable("K"));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
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
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("m", 4));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literal.getTuple().setTerm(2, TERM.createVariable("U"));
		literal.getTuple().setTerm(3, TERM.createVariable("V"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		literal = BASIC.createLiteral(true, BASIC.createPredicate("p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("W"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BASIC.createPredicate("q", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);
		literal = BASIC.createLiteral(false, BASIC.createPredicate("r", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("L"));
		literal.getTuple().setTerm(1, TERM.createVariable("K"));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
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
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("m", 4));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literal.getTuple().setTerm(2, TERM.createVariable("U"));
		literal.getTuple().setTerm(3, TERM.createVariable("V"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		literal = BASIC.createLiteral(true, BASIC.createPredicate("p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("W"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BASIC.createPredicate("q", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);
		literal = BASIC.createLiteral(false, BASIC.createPredicate("r", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("L"));
		literal.getTuple().setTerm(1, TERM.createVariable("K"));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
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
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("m", 4));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literal.getTuple().setTerm(2, TERM.createVariable("U"));
		literal.getTuple().setTerm(3, TERM.createVariable("V"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		literal = BASIC.createLiteral(true, BASIC.createPredicate("p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("W"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BASIC.createPredicate("q", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);
		literal = BASIC.createLiteral(false, BASIC.createPredicate("r", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("L"));
		literal.getTuple().setTerm(1, TERM.createVariable("K"));
		literals.add(literal);
		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("Y"), TERM.createVariable("X")));
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
		try
		{
			MiscOps.checkRuleSafe( BASIC.createRule( head, body ) );
			return true;
		}
		catch( Exception e )
		{
			return false;
		}
	}
}
