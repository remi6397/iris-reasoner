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

import static org.deri.iris.factory.Factory.SEMINAIVE_MODEL;

import java.util.ArrayList;

import org.deri.iris.api.evaluation.seminaive.model.IJoin;
import org.deri.iris.api.evaluation.seminaive.model.IProjection;
import org.deri.iris.api.evaluation.seminaive.model.IRule;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.operations.relations.JoinCondition;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Joachim Adi Schuetz, DERI Innsbruck
 * @date $Date: 2006-11-29 09:58:56 $
 * @version $Id: TreeTest.java,v 1.5 2006-11-29 09:58:56 adi Exp $
 */
public class TreeTest extends TestCase {

	private Tree tree, tree2, tree3, tree4, tree5, tree6;
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

		// non trivial trees
		int[] index = new int[] {0, -1, 1};
		int[] index2 = new int[] {0, 1, 1};
		int[] index3 = new int[] {1, 0, 1};
		IProjection proj = SEMINAIVE_MODEL.createProjection(index2);
		proj.addVariable("X");
		proj.addVariable("Y");
		IJoin join = SEMINAIVE_MODEL.createJoin(index, JoinCondition.EQUALS);
		join.addVariable("X");
		join.addVariable("Z");
		join.addVariable("Y");
		IRule rule = SEMINAIVE_MODEL.createRule("p", 2);
		rule.addVariable("X");
		rule.addVariable("Z");
		join.addComponent(rule);
		rule = SEMINAIVE_MODEL.createRule("r", 2);
		rule.addVariable("Y");
		rule.addVariable("Z");
		join.addComponent(rule);
		proj.addComponent(join);
		tree3 = new Tree("TestHead");
		tree3.addComponent(proj);
		// same as above
		IProjection proj2 = SEMINAIVE_MODEL.createProjection(index2);
		proj2.addVariable("X");
		proj2.addVariable("Y");
		IJoin join2 = SEMINAIVE_MODEL.createJoin(index, JoinCondition.EQUALS);
		join2.addVariable("X");
		join2.addVariable("Z");
		join2.addVariable("Y");
		IRule rule2 = SEMINAIVE_MODEL.createRule("p", 2);
		rule2.addVariable("X");
		rule2.addVariable("Z");
		join2.addComponent(rule2);
		rule2 = SEMINAIVE_MODEL.createRule("r", 2);
		rule2.addVariable("Y");
		rule2.addVariable("Z");
		join2.addComponent(rule2);
		proj2.addComponent(join2);
		tree4 = new Tree("TestHead");
		tree4.addComponent(proj2);
		// same as above
		IProjection proj3 = SEMINAIVE_MODEL.createProjection(index2);
		proj3.addVariable("X");
		proj3.addVariable("Y");
		IJoin join3 = SEMINAIVE_MODEL.createJoin(index, JoinCondition.EQUALS);
		join3.addVariable("X");
		join3.addVariable("Z");
		join3.addVariable("Y");
		IRule rule3 = SEMINAIVE_MODEL.createRule("p", 2);
		rule3.addVariable("X");
		rule3.addVariable("Z");
		join3.addComponent(rule3);
		rule3 = SEMINAIVE_MODEL.createRule("r", 2);
		rule3.addVariable("Y");
		rule3.addVariable("Z");
		join3.addComponent(rule3);
		proj3.addComponent(join3);
		tree5 = new Tree("TestHead");
		tree5.addComponent(proj3);
		// different
		proj2 = SEMINAIVE_MODEL.createProjection(index3);
		proj2.addVariable("X");
		proj2.addVariable("Y");
		join2 = SEMINAIVE_MODEL.createJoin(index, JoinCondition.EQUALS);
		join2.addVariable("X");
		join2.addVariable("Z");
		join2.addVariable("Y");
		rule2 = SEMINAIVE_MODEL.createRule("p", 2);
		rule2.addVariable("X");
		rule2.addVariable("Z");
		join2.addComponent(rule2);
		rule2 = SEMINAIVE_MODEL.createRule("r", 2);
		rule2.addVariable("Y");
		rule2.addVariable("Z");
		join2.addComponent(rule2);
		proj2.addComponent(join2);
		tree6 = new Tree("TestHead2");
		tree6.addComponent(proj2);
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
	public void testhasVariable() {

		assertTrue(tree.hasVariable("x2"));

	}
	public void testSimpleEquality() {

		assertTrue(tree.equals(tree2));
		assertTrue(tree2.equals(tree));
	}
	public void test_nontrivialEquality() {

		assertTrue(tree5.equals(tree4));
		assertTrue(tree4.equals(tree5));
	}
	
	/*
	 * equality
	 */
	public void testEquality_reflexiv() {
		assertTrue(tree3.equals(tree3));		
		assertTrue(tree4.equals(tree4));		
		assertTrue(tree5.equals(tree5));		
		assertTrue(tree6.equals(tree6));		
	}
	public void testEquality_symetric() {
		assertTrue(tree5.equals(tree4));
		assertTrue(tree4.equals(tree5));
	}
	public void testEquality_transitiv() {
		assertTrue(tree3.equals(tree4));
		assertTrue(tree3.equals(tree5));
	}
	public void testEquality_consitence() {
		assertTrue(!tree4.equals(tree6));
		assertTrue(!tree4.equals(null));
		assertTrue(!tree6.equals(null));
	}
	public void testEquality_all2gether() {
		// ref
		assertTrue(tree3.equals(tree3));
		// sym
		assertTrue(tree3.equals(tree4));
		assertTrue(tree4.equals(tree3));
		// trans
		assertTrue(tree4.equals(tree5));
		assertTrue(tree3.equals(tree5));
		// cons
		assertTrue(!tree3.equals(tree2));
		assertTrue(!tree3.equals(null));
	}

}