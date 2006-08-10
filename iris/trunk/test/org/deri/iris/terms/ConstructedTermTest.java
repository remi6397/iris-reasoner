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
package org.deri.iris.terms;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTest;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;

/**
 * @author richi
 * 
 */
public class ConstructedTermTest extends TestCase {

	private static final IStringTerm BASIC = Factory.TERM.createString("aaa");

	private static final IStringTerm MORE = Factory.TERM.createString("aab");

	private static final IStringTerm MORE1 = Factory.TERM.createString("aac");

	public static Test suite() {
		return new TestSuite(ConstructedTermTest.class,
				ConstructedTermTest.class.getSimpleName());
	}

	public void testBasic() {
		ConstantTerm basic = new ConstantTerm(BASIC);
		assertEquals("object not initialized correctly", BASIC, basic
				.getValue());
	}

	public void testEquals() {
		ObjectTest.runTestEquals(new ConstantTerm(BASIC), new ConstantTerm(
				BASIC), new ConstantTerm(MORE1));
	}

	public void testClone() {
		ObjectTest.runTestClone(new ConstantTerm(BASIC));
	}

	public void testHashCode() {
		ObjectTest.runTestHashCode(new ConstantTerm(BASIC), new ConstantTerm(
				BASIC));
	}

	public void testCompareTo() {
		ObjectTest.runTestCompareTo(new ConstantTerm(BASIC), new ConstantTerm(
				BASIC), new ConstantTerm(MORE), new ConstantTerm(MORE1));
	}
	
	public void testGroundness() {
		IConstructedTerm c1 = Factory.TERM.createConstruct("c1", BASIC, 
				Factory.TERM.createVariable("X"));
		IConstructedTerm c2 = Factory.TERM.createConstruct("c2", c1, BASIC);
		
		assertFalse("c1 is not ground, thus c2 is not ground too", c2.isGround());
	}
	
	public void testVariables() {
		Set<IVariable> variables = new HashSet<IVariable>();
		IVariable x = Factory.TERM.createVariable("X");
		IVariable y = Factory.TERM.createVariable("Y");
		
		variables.add(x);
		variables.add(y);
		
		IConstructedTerm c1 = Factory.TERM.createConstruct("c1", BASIC, y);
		IConstructedTerm c2 = Factory.TERM.createConstruct("c2", c1, x);
		
		assertEquals(variables, c2.getVariables());
	}
	
}
