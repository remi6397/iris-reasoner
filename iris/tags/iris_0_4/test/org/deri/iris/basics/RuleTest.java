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
package org.deri.iris.basics;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.BUILTIN;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTests;
import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;

/**
 * @author richi
 * 
 * Revision 1.1  26.07.2006 11:42:46  Darko Anicic, DERI Innsbruck
 */
public class RuleTest extends TestCase {

	private Head HEAD;

	private Body BODY;

	/**
	 * setup for Rule2Relation tests
	 */
	public void setUp() {
		List<ILiteral> tempLiterals = new ArrayList<ILiteral>();

		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate(
				"sin", 1));
		literal.getTuple().setTerm(0, CONCRETE.createInteger(1));
		tempLiterals.add(literal);

		literal = BASIC.createLiteral(true, BASIC.createPredicate("cos", 1));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		tempLiterals.add(literal);

		literal = BASIC.createLiteral(true, BASIC.createPredicate("date", 3));
		literal.getTuple().setTerm(0, CONCRETE.createInteger(2005));
		literal.getTuple().setTerm(1, CONCRETE.createInteger(12));
		literal.getTuple().setTerm(2, CONCRETE.createInteger(24));
		tempLiterals.add(literal);

		HEAD = new Head(tempLiterals);

		tempLiterals = new ArrayList<ILiteral>();

		literal = BASIC.createLiteral(true, BASIC.createPredicate("sin", 1));
		literal.getTuple().setTerm(0, CONCRETE.createInteger(1));
		tempLiterals.add(literal);

		literal = BASIC.createLiteral(true, BASIC.createPredicate("cos", 1));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		tempLiterals.add(literal);

		literal = BASIC.createLiteral(true, BASIC.createPredicate("date", 3));
		literal.getTuple().setTerm(0, TERM.createVariable("J"));
		literal.getTuple().setTerm(1, TERM.createVariable("K"));
		literal.getTuple().setTerm(2, TERM.createVariable("L"));
		tempLiterals.add(literal);

		BODY = new Body(tempLiterals);
	}

	public static Test suite() {
		return new TestSuite(RuleTest.class, RuleTest.class.getSimpleName());
	}
	
	public void testEquals() {
		ObjectTests.runTestEquals(new Rule(HEAD, BODY), new Rule(HEAD, BODY),
				new Rule(null, BODY));
		ObjectTests.runTestEquals(new Rule(HEAD, BODY), new Rule(HEAD, BODY),
				new Rule(HEAD, new Body(HEAD.getHeadLiterals())));
		ObjectTests.runTestEquals(new Rule(HEAD, BODY), new Rule(HEAD, BODY),
				new Rule(new Head(BODY.getBodyLiterals()), BODY));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Rule(HEAD, BODY), new Rule(HEAD, BODY));
	}

	/*
	 * check safness of the rule already used in this test
	 */
	public void testSafenessfromGivenRule() {
		assertTrue(BASIC.createRule(HEAD, BODY).isSafe());
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
		
		assertTrue(BASIC.createRule(head, body).isSafe());
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
		
		assertTrue(!BASIC.createRule(head, body).isSafe());
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

		assertTrue(BASIC.createRule(head, body).isSafe());	
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

		assertTrue(BASIC.createRule(head, body).isSafe());	
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

		assertTrue(BASIC.createRule(head, body).isSafe());	
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

		assertTrue(!BASIC.createRule(head, body).isSafe());	
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

		assertTrue(!BASIC.createRule(head, body).isSafe());	
	}
	public void testSafenessBuiltin_containsGreater() {
		
		// euqala(X, Y) :- X = Y, X = a -> safe 
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

		assertTrue(!BASIC.createRule(head, body).isSafe());	
	}	
	public void testSafeness_allTogether() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), r(L, K), X = Y, U = V, W = U -> safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("m", 4));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literal.getTuple().setTerm(1, TERM.createVariable("U"));
		literal.getTuple().setTerm(1, TERM.createVariable("V"));
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

		assertTrue(BASIC.createRule(head, body).isSafe());	
	}
	public void testSafeness_negatedOrdinaryPredicate() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), !r(L, K), X = Y, U = V, W = U -> safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("m", 4));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literal.getTuple().setTerm(1, TERM.createVariable("U"));
		literal.getTuple().setTerm(1, TERM.createVariable("V"));
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

		assertTrue(BASIC.createRule(head, body).isSafe());	
	}
	public void testSafeness_negatedBuiltin() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), r(L, K), X = Y, !U = V, W = U -> not safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("m", 4));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literal.getTuple().setTerm(1, TERM.createVariable("U"));
		literal.getTuple().setTerm(1, TERM.createVariable("V"));
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

		assertTrue(!BASIC.createRule(head, body).isSafe());	
	}
	public void testSafeness_withNegation() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), !r(L, K), X = Y, !U = V, W = U -> not safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("m", 4));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literal.getTuple().setTerm(1, TERM.createVariable("U"));
		literal.getTuple().setTerm(1, TERM.createVariable("V"));
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

		assertTrue(!BASIC.createRule(head, body).isSafe());	
	}
	public void testSafeness_withNegationofNE() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), !r(L, K), X = Y, !U != V, W = U -> safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("m", 4));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literal.getTuple().setTerm(1, TERM.createVariable("U"));
		literal.getTuple().setTerm(1, TERM.createVariable("V"));
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

		assertTrue(BASIC.createRule(head, body).isSafe());	
	}
	public void testSafeness_withNegationofotherBuiltin() {
		
		// m(X, Y, U, V) :- p(W, Z), q(X, Z), !r(L, K), X = Y, !U > V, W = U -> safe 
		List<ILiteral> literals = new ArrayList<ILiteral>();
		
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate("m", 4));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literal.getTuple().setTerm(1, TERM.createVariable("U"));
		literal.getTuple().setTerm(1, TERM.createVariable("V"));
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

		assertTrue(!BASIC.createRule(head, body).isSafe());	
	}
}
