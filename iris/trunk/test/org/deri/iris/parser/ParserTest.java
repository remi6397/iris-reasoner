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
package org.deri.iris.parser;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.BUILTIN;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserImpl;
import org.deri.iris.factory.Factory;

/**
 * @author Joachim Adi Schuetz, DERI Innsbruck
 * @date $Date: 2007-01-29 09:10:47 $
 * @version $Id: ParserTest.java,v 1.1 2007-01-29 09:10:47 darko Exp $
 */
public class ParserTest extends TestCase {

	Parser pars;
	IProgram prog;
	
	List<ILiteral> literals;
	Set<org.deri.iris.api.basics.IRule> rules;
	
	public static Test suite() {
		return new TestSuite(ParserTest.class, ParserTest.class.getSimpleName());
	}

	/**
	 * setup for Parser tests
	 */
	public void setUp() {
        pars = new ParserImpl();
    	prog = Factory.PROGRAM.createProgram();

		literals = new ArrayList<ILiteral>();
		rules = new HashSet<org.deri.iris.api.basics.IRule>();		
	}

	/**
	 * run parser test
	 *
	 */
	protected void runParser(final String expr, final Set<org.deri.iris.api.basics.IRule> rul) throws Exception {

		this.pars.compileKB(expr, prog);
		System.out.println("in: " + expr + "\nrul: "+ rul + "\nres: " + prog.getRules() + "\n");
		assertResults(rul, prog.getRules());
	}
	
	/**
	 * s(X, Y) :- p(Y, Z), r(Y, Z)
	 *
	 */
	public void testParser() {
		
		// input
		String expr = "s(X, Y) :- p(Y, Z), r(Y, Z)";
		
		// result
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate(
				"s", 2));
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
		
		rules.add(BASIC.createRule(head, body));

		try {
			runParser(expr, rules);
		} catch (Exception e) {e.printStackTrace();}
	}
	/**
	 * p(?X,?Y) :- r(?Z, ?Y) and ?X='a'
	 *
	 */
	public void testParser_1a() {

		// input
		String expr = "p(?X, ?Y) :- r(?Z, ?Y), ?X=a";
		
		// result
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate(
				"p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		literal = BASIC.createLiteral(true, BASIC.createPredicate("r", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("Z"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);

		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		literals.add(literal);

		IBody body = BASIC.createBody(literals);
		
		rules.add(BASIC.createRule(head, body));
				
		try {
			runParser(expr, rules);
		} catch (Exception e) {e.printStackTrace();}
	}
	/**
	 * p(?X,?Y) :- r(?X, ?Y) and ?X!='a'
	 *
	 */
	public void testParser_1b() {

		// input
		String expr = "p(?X, ?Y) :- r(?Z, ?Y), ?X!=a";

		// result
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate(
				"p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		literal = BASIC.createLiteral(true, BASIC.createPredicate("r", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("Z"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);

		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		literals.add(literal);

		IBody body = BASIC.createBody(literals);
		
		rules.add(BASIC.createRule(head, body));

		try {
			runParser(expr, rules);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	protected static void assertResults(final Set<IRule> a, final Set<IRule> b) {
		Assert.assertEquals("The length of relation and the list of"
				+ " expected tuples must be equal", a.size(), b.size());
		Iterator ita = a.iterator();
		Iterator itb = a.iterator();
		while(ita.hasNext() && itb.hasNext()) {
			IRule rulea = (IRule)ita.next();
			IRule ruleb = (IRule)itb.next();
			assertTrue("The keys must be equal.", rulea.equals(ruleb));
		}
	}
}
