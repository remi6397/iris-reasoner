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

/**
 * @author Joachim Adi Schuetz, DERI Innsbruck
 * @author Darko Anicic, DERI Innsbruck
 * @date $Date: 2006-11-02 12:50:55 $
 * @version $Id: TreeTest.java,v 1.1 2006-11-02 12:50:55 adi Exp $
 */
public class TreeTest extends TestCase {

	private Tree tree, tree2;
	private ArrayList variableList;
	
	public static Test suite() {
		return new TestSuite(TreeTest.class, TreeTest.class.getSimpleName());
	}

	/**
	 * setup for tree tests
	 */
	public void setUp() {
		tree = new Tree("TestHead");
		tree.addVariable("x1");
		tree.addVariable("x2");
		tree.addVariable("x3");

		tree2 = new Tree("TestHead");
		tree2.addVariable("x1");
		tree2.addVariable("x2");
		tree2.addVariable("x3");

		variableList = new ArrayList<String>();
		variableList.add("x1");
		variableList.add("x2");
		variableList.add("x3");
	}
	
	/**
	 * test various things of the tree-class
	 *
	 */
	public void testTreeName() {

		assertEquals(tree.getName(), "TestHead");
	}
	public void testTree() {

		assertEquals(tree.getArity(), 3);

	}
	public void testvariableList() {

		assertEquals(tree.getVariables(), variableList);

	}
	public void testhasVaribale() {

		assertTrue(tree.hasVariable("x2"));

	}
	public void testEquality() {

		assertTrue(tree.equals(tree2));
	}

}