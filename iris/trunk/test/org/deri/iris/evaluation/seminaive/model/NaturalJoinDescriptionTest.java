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
package org.deri.iris.evaluation.seminaive.model;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.operations.relations.JoinCondition;

/**
 * @author Joachim Adi Schuetz, DERI Innsbruck
 * @date $Date: 2006-11-06 09:30:10 $
 * @version $Id: NaturalJoinDescriptionTest.java,v 1.1 2006-11-06 09:30:10 adi Exp $
 */
public class NaturalJoinDescriptionTest extends TestCase {

	private NaturalJoinDescription JoinDescr1, JoinDescr2;
	private ArrayList variableList;
	
	public static Test suite() {
		return new TestSuite(NaturalJoinDescriptionTest.class, NaturalJoinDescriptionTest.class.getSimpleName());
	}

	/**
	 * setup for NaturalJoinDescription tests
	 */
	public void setUp() {
		int[] index = new int[] {1, 0, 1}; 
		JoinDescr1 = new NaturalJoinDescription();
		JoinDescr1.addVariable("x1");
		JoinDescr1.addVariable("x2");
		JoinDescr1.addVariable("x3");

		variableList = new ArrayList<String>();
		variableList.add("x1");
		variableList.add("x2");
		variableList.add("x3");
	}
	
	/**
	 * test various methods of the NaturalJoinDescription-class
	 *
	 */
	public void testgetName() {

		assertEquals(JoinDescr1.getName(), "natural join");
	}
	public void testgetArity() {

		assertEquals(JoinDescr1.getArity(), 3);
	}
	public void testgetVariables() {

		assertEquals(JoinDescr1.getVariables(), variableList);
	}
	public void testhasVariable() {

		assertTrue(JoinDescr1.hasVariable("x2"));
	}
	public void testaddComponent() {
		NaturalJoinDescription JoinDescr = new NaturalJoinDescription();
		Tree tree = new Tree("head"); 
		tree.addVariable("x1");
		tree.addVariable("x2");
		tree.addVariable("x3");

		assertTrue(JoinDescr.addComponent(tree));
		assertEquals(JoinDescr.getVariables(), variableList);
	}
}