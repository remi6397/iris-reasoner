/*
 * Integrated UnaryRule Inference System (IRIS):
 * An extensible UnaryRule inference system for datalog with extensions by 
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
 * Foundation, Inc., 51 Franklin SUnaryRuleDescriptiont, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.evaluation.seminaive.model;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Joachim Adi Schuetz, DERI Innsbruck
 * @date $Date: 2006-11-23 10:54:28 $
 * @version $Id: UnaryRuleDescriptionTest.java,v 1.1 2006-11-23 10:54:28 adi Exp $
 */
public class UnaryRuleDescriptionTest extends TestCase {

	private UnaryRuleDescription UnaryRuleDescription, UnaryRuleDescr1, UnaryRuleDescr1b, UnaryRuleDescr1c, UnaryRuleDescr2;
	private ArrayList variableList;
	
	public static Test suite() {
		return new TestSuite(UnaryRuleDescriptionTest.class, UnaryRuleDescriptionTest.class.getSimpleName());
	}

	/**
	 * setup for UnaryRuleDescription tests
	 */
	public void setUp() {
		UnaryRuleDescription = new UnaryRuleDescription("TestUnaryRuleDescription");
		UnaryRuleDescription.addVariable("x1");
		UnaryRuleDescr1 = new UnaryRuleDescription("TestUnaryRuleDescription");
		UnaryRuleDescr1.addVariable("x1");
		UnaryRuleDescr1b = new UnaryRuleDescription("TestUnaryRuleDescription");
		UnaryRuleDescr1b.addVariable("x1");
		UnaryRuleDescr1c = new UnaryRuleDescription("TestUnaryRuleDescription");
		UnaryRuleDescr1c.addVariable("x1");

		UnaryRuleDescr2 = new UnaryRuleDescription("OtherUnaryRuleDescription");
		UnaryRuleDescr2.addVariable("x2");

		variableList = new ArrayList<String>();
		variableList.add("x1");
	}
	
	/**
	 * test various things of the UnaryRuleDescription-class
	 *
	 */
	public void testUnaryRuleDescription() {

		assertEquals(UnaryRuleDescription.getArity(), 1);

	}
	public void testvariableList() {

		assertEquals(UnaryRuleDescription.getVariables(), variableList);

	}
	public void testhasVariable() {

		assertTrue(UnaryRuleDescr2.hasVariable("x2"));

	}
	/*
	 * equality
	 */
	public void testEquality_reflexiv() {
		assertTrue(UnaryRuleDescr1.equals(UnaryRuleDescr1));		
	}
	public void testEquality_symetric() {
		assertTrue(UnaryRuleDescr1.equals(UnaryRuleDescr1b));
		assertTrue(UnaryRuleDescr1b.equals(UnaryRuleDescr1));
	}
	public void testEquality_transitiv() {
		assertTrue(UnaryRuleDescr1b.equals(UnaryRuleDescr1c));
		assertTrue(UnaryRuleDescr1.equals(UnaryRuleDescr1c));
	}
	public void testEquality_consitence() {
		assertTrue(!UnaryRuleDescr1.equals(UnaryRuleDescr2));
		assertTrue(!UnaryRuleDescr1.equals(null));
	}
	public void testEquality_all2gether() {
		// ref
		assertTrue(UnaryRuleDescr1.equals(UnaryRuleDescr1));
		// sym
		assertTrue(UnaryRuleDescr1.equals(UnaryRuleDescr1b));
		assertTrue(UnaryRuleDescr1b.equals(UnaryRuleDescr1));
		// trans
		assertTrue(UnaryRuleDescr1b.equals(UnaryRuleDescr1c));
		assertTrue(UnaryRuleDescr1.equals(UnaryRuleDescr1c));
		// cons
		assertTrue(!UnaryRuleDescr1.equals(UnaryRuleDescr2));
		assertTrue(!UnaryRuleDescr1.equals(null));
	}
}