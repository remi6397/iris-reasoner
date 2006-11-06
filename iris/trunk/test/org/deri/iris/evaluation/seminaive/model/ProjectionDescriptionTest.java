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
 * @date $Date: 2006-11-06 09:30:10 $
 * @version $Id: ProjectionDescriptionTest.java,v 1.1 2006-11-06 09:30:10 adi Exp $
 */
public class ProjectionDescriptionTest extends TestCase {

	private ProjectionDescription ProjectionDescr1, ProjectionDescr2;
	private ArrayList variableList;
	
	public static Test suite() {
		return new TestSuite(ProjectionDescriptionTest.class, ProjectionDescriptionTest.class.getSimpleName());
	}

	/**
	 * setup for ProjectionDescription tests
	 */
	public void setUp() {
		int[] index = new int[] {1, 0, 1}; 
		ProjectionDescr1 = new ProjectionDescription(index);
		ProjectionDescr1.addVariable("x1");
		ProjectionDescr1.addVariable("x2");
		ProjectionDescr1.addVariable("x3");

		variableList = new ArrayList<String>();
		variableList.add("x1");
		variableList.add("x2");
		variableList.add("x3");
	}
	
	/**
	 * test various methods of the ProjectionDescription-class
	 *
	 */
	public void testgetName() {

		assertEquals(ProjectionDescr1.getName(), "projection");
	}
	public void testgetArity() {

		assertEquals(ProjectionDescr1.getArity(), 3);
	}
	public void testgetVariables() {

		assertEquals(ProjectionDescr1.getVariables(), variableList);
	}
	public void testhasVariable() {

		assertTrue(ProjectionDescr1.hasVariable("x2"));
	}
	public void testgetIndexes() {
		int[] index = new int[] {1, 0, 1};
		for (int i = 0; i < ProjectionDescr1.getIndexes().length; i++)
			assertEquals(ProjectionDescr1.getIndexes()[i], index[i]);
		//assertTrue(ProjectionDescr1.getIndexes().equals(index));
	}
	public void testaddComponent() {
		int[] index = new int[] {1, 0, 1}; 		
		ProjectionDescription ProjectionDescr = new ProjectionDescription(index);
		Tree tree = new Tree("head"); 
		tree.addVariable("x1");
		tree.addVariable("x2");
		tree.addVariable("x3");

		assertTrue(ProjectionDescr.addComponent(tree));
		assertEquals(ProjectionDescr.getVariables(), variableList);
	}
	public void testDifference_paramsnull() {
		try {
			ProjectionDescription ProjectionDescr = new ProjectionDescription(null);
			fail ("should have raised an java.lang.IllegalArgumentException");
		} catch(java.lang.IllegalArgumentException e)  {}
	}
}