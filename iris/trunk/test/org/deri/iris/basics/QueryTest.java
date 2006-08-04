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
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTest;
import org.deri.iris.api.basics.ILiteral;

/**
 * @author richi
 * 
 * Revision 1.1  26.07.2006 11:52:46  Darko Anicic, DERI Innsbruck
 */
public class QueryTest extends TestCase {

	private static final Body BODY;

	private static final Body BODYMORE;

	static {
		Set<ILiteral> tempLiterals = new HashSet<ILiteral>();

		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate(
				"sin", 1));
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

		tempLiterals = new HashSet<ILiteral>();

		literal = BASIC.createLiteral(true, BASIC.createPredicate("sin", 1));
		literal.getTuple().setTerm(0, CONCRETE.createInteger(1));
		tempLiterals.add(literal);

		literal = BASIC.createLiteral(true, BASIC.createPredicate("cos", 1));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		tempLiterals.add(literal);

		literal = BASIC.createLiteral(true, BASIC.createPredicate("date", 3));
		literal.getTuple().setTerm(0, TERM.createVariable("J"));
		literal.getTuple().setTerm(1, TERM.createVariable("Q"));
		literal.getTuple().setTerm(2, TERM.createVariable("L"));
		tempLiterals.add(literal);

		BODYMORE = new Body(tempLiterals);
	}

	public static Test suite() {
		return new TestSuite(QueryTest.class, QueryTest.class.getSimpleName());
	}

	public void testEquals() {
		ObjectTest.runTestEquals(new Query(BODY), new Query(BODY), new Query(
				BODYMORE));
		ObjectTest.runTestEquals(new Query(BODY), new Query(BODY), new Query(
				null));
	}

	public void testHashCode() {
		ObjectTest.runTestHashCode(new Query(BODY), new Query(BODY));
	}
}
