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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.List;
import java.util.ArrayList;

import org.deri.iris.operations.relations.JoinCondition;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.basics.Tuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.terms.StringTerm;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Joachim Adi Schuetz, DERI Innsbruck
 * @date $Date: 2006-11-23 10:54:28 $
 * @version $Id: SelectionDescriptionTest.java,v 1.2 2006-11-23 10:54:28 adi Exp $
 */
public class SelectionDescriptionTest extends TestCase {

	private SelectionDescription SelectionDescr1, SelectionDescr1b, SelectionDescr1c, SelectionDescr2;
	private ITuple tup;
	private ArrayList variableList;
	
	public static Test suite() {
		return new TestSuite(SelectionDescriptionTest.class, SelectionDescriptionTest.class.getSimpleName());
	}

	/**
	 * setup for SelectionDescription tests
	 */
	public void setUp() {
		tup = BASIC.createTuple(
				TERM.createString("x1"), 
				TERM.createString("x2"),
				TERM.createString("x3"));

		int[] index = new int[] {1, 0, 1}; 
		int[] index2 = new int[] {1, 1, 0}; 
		
		SelectionDescr1 = new SelectionDescription(tup, index);
		SelectionDescr1.addVariable("x1");
		SelectionDescr1.addVariable("x2");
		SelectionDescr1.addVariable("x3");
		SelectionDescr1b = new SelectionDescription(tup, index);
		SelectionDescr1b.addVariable("x1");
		SelectionDescr1b.addVariable("x2");
		SelectionDescr1b.addVariable("x3");
		SelectionDescr1c = new SelectionDescription(tup, index);
		SelectionDescr1c.addVariable("x1");
		SelectionDescr1c.addVariable("x2");
		SelectionDescr1c.addVariable("x3");

		SelectionDescr2 = new SelectionDescription(tup, index2);

		variableList = new ArrayList<String>();
		variableList.add("x1");
		variableList.add("x2");
		variableList.add("x3");
	}
	
	/**
	 * test various methods of the SelectionDescription-class
	 *
	 */
	public void testgetName() {

		assertEquals(SelectionDescr1.getName(), "selection");
	}
	public void testgetArity() {

		assertEquals(SelectionDescr1.getArity(), 3);
	}
	public void testgetVariables() {

		assertEquals(SelectionDescr1.getVariables(), variableList);
	}
	public void testhasVariable() {

		assertTrue(SelectionDescr1.hasVariable("x2"));
	}
	public void testgetPattern() {

		assertTrue(SelectionDescr2.getPattern().equals(tup));
	}
	public void testaddComponent() {
		int[] index = new int[] {1, 0, 1}; 		
		SelectionDescription SelectionDescr = new SelectionDescription(index);
		Tree tree = new Tree("head"); 
		tree.addVariable("x1");
		tree.addVariable("x2");
		tree.addVariable("x3");

		assertTrue(SelectionDescr.addComponent(tree));
		assertEquals(SelectionDescr.getVariables(), variableList);
	}
	
	/*
	 * equality
	 */
	public void testEquality_reflexiv() {
		assertTrue(SelectionDescr1.equals(SelectionDescr1));		
	}
	public void testEquality_symetric() {
		assertTrue(SelectionDescr1.equals(SelectionDescr1b));
		assertTrue(SelectionDescr1b.equals(SelectionDescr1));
	}
	public void testEquality_transitiv() {
		assertTrue(SelectionDescr1b.equals(SelectionDescr1c));
		assertTrue(SelectionDescr1.equals(SelectionDescr1c));
	}
	public void testEquality_consitence() {
		assertTrue(!SelectionDescr1.equals(SelectionDescr2));
		assertTrue(!SelectionDescr1.equals(null));
	}
	public void testEquality_all2gether() {
		// ref
		assertTrue(SelectionDescr1.equals(SelectionDescr1));
		// sym
		assertTrue(SelectionDescr1.equals(SelectionDescr1b));
		assertTrue(SelectionDescr1b.equals(SelectionDescr1));
		// trans
		assertTrue(SelectionDescr1b.equals(SelectionDescr1c));
		assertTrue(SelectionDescr1.equals(SelectionDescr1c));
		// cons
		assertTrue(!SelectionDescr1.equals(SelectionDescr2));
		assertTrue(!SelectionDescr1.equals(null));
	}

	/**
	 * if at least one of the parameters are the null-value
	 * -> should raise an IllegalArgumentException
	 *
	 */
	public void testDifference_paramnullint() {
		try {
			SelectionDescription SelectionDescr = new SelectionDescription((int[])null);
			fail ("should have raised an java.lang.IllegalArgumentException");
		} catch(java.lang.IllegalArgumentException e)  {}
	}
	public void testDifference_paramnullituple() {
		try {
			SelectionDescription SelectionDescr = new SelectionDescription((ITuple)null);
			fail ("should have raised an java.lang.IllegalArgumentException");
		} catch(java.lang.IllegalArgumentException e)  {}
	}
	public void testDifference_paramsnull() {
		try {
			SelectionDescription SelectionDescr = new SelectionDescription(null, null);
			fail ("should have raised an java.lang.IllegalArgumentException");
		} catch(java.lang.IllegalArgumentException e)  {}
	}
	public void testDifference_param1null() {

		SelectionDescription SelectionDescr = new SelectionDescription(tup, null);
	}
	public void testDifference_param2null() {

		int[] index = {1, 0, 1};
		SelectionDescription SelectionDescr = new SelectionDescription(null, index);
	}
}