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
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTests;
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

}
