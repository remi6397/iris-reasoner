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
 * @date $Date: 2006-11-23 10:54:28 $
 * @version $Id: JoinDescriptionTest.java,v 1.2 2006-11-23 10:54:28 adi Exp $
 */
public class JoinDescriptionTest extends TestCase {

	private JoinDescription JoinDescr1, JoinDescr1b, JoinDescr1c, JoinDescr2;
	private ArrayList variableList;
	
	public static Test suite() {
		return new TestSuite(JoinDescriptionTest.class, JoinDescriptionTest.class.getSimpleName());
	}

	/**
	 * setup for JoinDescription tests
	 */
	public void setUp() {
		int[] index = new int[] {1, 0, 1}; 
		JoinDescr1 = new JoinDescription(index, JoinCondition.EQUALS);
		JoinDescr1.addVariable("x1");
		JoinDescr1.addVariable("x2");
		JoinDescr1.addVariable("x3");
		// 4 euqality check
		JoinDescr1b = new JoinDescription(index, JoinCondition.EQUALS);
		JoinDescr1b.addVariable("x1");
		JoinDescr1b.addVariable("x2");
		JoinDescr1b.addVariable("x3");
		JoinDescr1c = new JoinDescription(index, JoinCondition.EQUALS);
		JoinDescr1c.addVariable("x1");
		JoinDescr1c.addVariable("x2");
		JoinDescr1c.addVariable("x3");

		JoinDescr2 = new JoinDescription(index, JoinCondition.GREATER_OR_EQUAL);
		JoinDescr2.addVariable("x1");
		JoinDescr2.addVariable("x2");
		JoinDescr2.addVariable("x3");

		variableList = new ArrayList<String>();
		variableList.add("x1");
		variableList.add("x2");
		variableList.add("x3");
	}
	
	/**
	 * test various methods of the JoinDescription-class
	 *
	 */
	public void testgetName() {

		assertEquals(JoinDescr1.getName(), "join");
	}
	public void testgetArity() {

		assertEquals(JoinDescr1.getArity(), 3);
	}
	public void testgetVariables() {

		assertEquals(JoinDescr1.getVariables(), variableList);
	}
	public void testgetIndexes() {
		int[] index = new int[] {1, 0, 1};
		for (int i = 0; i < JoinDescr1.getIndexes().length; i++)
			assertEquals(JoinDescr1.getIndexes()[i], index[i]);
		//assertTrue(JoinDescr1.getIndexes().equals(index));
	}
	public void testgetCondition() {

		assertEquals(JoinDescr1.getCondition(), JoinCondition.EQUALS);
	}
	public void testhasVariable() {

		assertTrue(JoinDescr1.hasVariable("x2"));
	}
	public void testaddComponent() {
		int[] index = new int[] {1, 0, 1}; 		
		JoinDescription JoinDescr = new JoinDescription(index, JoinCondition.EQUALS);
		Tree tree = new Tree("head"); 
		tree.addVariable("x1");
		tree.addVariable("x2");
		tree.addVariable("x3");

		assertTrue(JoinDescr.addComponent(tree));
		assertEquals(JoinDescr.getVariables(), variableList);
	}

	/*
	 * equality
	 */
	public void testEquality_reflexiv() {
		assertTrue(JoinDescr1.equals(JoinDescr1));		
	}
	public void testEquality_symetric() {
		assertTrue(JoinDescr1.equals(JoinDescr1b));
		assertTrue(JoinDescr1b.equals(JoinDescr1));
	}
	public void testEquality_transitiv() {
		assertTrue(JoinDescr1b.equals(JoinDescr1c));
		assertTrue(JoinDescr1.equals(JoinDescr1c));
	}
	public void testEquality_consitence() {
		assertTrue(!JoinDescr1.equals(JoinDescr2));
		assertTrue(!JoinDescr1.equals(null));
	}
	public void testEquality_all2gether() {
		// ref
		assertTrue(JoinDescr1.equals(JoinDescr1));
		// sym
		assertTrue(JoinDescr1.equals(JoinDescr1b));
		assertTrue(JoinDescr1b.equals(JoinDescr1));
		// trans
		assertTrue(JoinDescr1b.equals(JoinDescr1c));
		assertTrue(JoinDescr1.equals(JoinDescr1c));
		// cons
		assertTrue(!JoinDescr1.equals(JoinDescr2));
		assertTrue(!JoinDescr1.equals(null));
	}
	
	/**
	 * if at least one of the parameters are the null-value
	 * -> should raise an IllegalArgumentException
	 *
	 */
	public void testDifference_paramsnull() {
		try {
			JoinDescription joinDescr = new JoinDescription(null, null);
			fail ("should have raised an java.lang.IllegalArgumentException");
		} catch(java.lang.IllegalArgumentException e)  {}
	}
	public void testDifference_param1null() {
		try {
			JoinDescription joinDescr = new JoinDescription(null, JoinCondition.EQUALS);
			fail ("should have raised an java.lang.IllegalArgumentException");
		} catch(java.lang.IllegalArgumentException e)  {}
	}
	public void testDifference_param2null() {
		int[] index = {1, 0, 1};
		try {
			JoinDescription joinDescr = new JoinDescription(index, null);
			fail ("should have raised an java.lang.IllegalArgumentException");
		} catch(java.lang.IllegalArgumentException e)  {}
	}
}