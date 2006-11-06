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
 * Foundation, Inc., 51 Franklin SRuleDescriptiont, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.evaluation.seminaive.model;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Joachim Adi Schuetz, DERI Innsbruck
 * @date $Date: 2006-11-06 10:39:57 $
 * @version $Id: RuleDescriptionTest.java,v 1.1 2006-11-06 10:39:57 adi Exp $
 */
public class RuleDescriptionTest extends TestCase {

	private RuleDescription RuleDescription, RuleDescription2;
	private ArrayList variableList;
	
	public static Test suite() {
		return new TestSuite(RuleDescriptionTest.class, RuleDescriptionTest.class.getSimpleName());
	}

	/**
	 * setup for RuleDescription tests
	 */
	public void setUp() {
		RuleDescription = new RuleDescription("TestRuleDescription", 3);
		RuleDescription.addVariable("x1");
		RuleDescription.addVariable("x2");
		RuleDescription.addVariable("x3");

		RuleDescription2 = new RuleDescription("TestRuleDescription", 3);
		RuleDescription2.addVariable("x1");
		RuleDescription2.addVariable("x2");
		RuleDescription2.addVariable("x3");

		variableList = new ArrayList<String>();
		variableList.add("x1");
		variableList.add("x2");
		variableList.add("x3");
	}
	
	/**
	 * test various things of the RuleDescription-class
	 *
	 */
	public void testRuleDescriptionName() {

		assertEquals(RuleDescription.getName(), "TestRuleDescription");
	}
	public void testRuleDescription() {

		assertEquals(RuleDescription.getArity(), 3);

	}
	public void testvariableList() {

		assertEquals(RuleDescription.getVariables(), variableList);

	}
	public void testhasVariable() {

		assertTrue(RuleDescription.hasVariable("x2"));

	}
	public void testEquality() {

		assertTrue(RuleDescription.equals(RuleDescription2));
	}

}