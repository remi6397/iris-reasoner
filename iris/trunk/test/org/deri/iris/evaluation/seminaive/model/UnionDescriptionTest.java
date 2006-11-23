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
 * @date $Date: 2006-11-23 10:54:28 $
 * @version $Id: UnionDescriptionTest.java,v 1.1 2006-11-23 10:54:28 adi Exp $
 */
public class UnionDescriptionTest extends TestCase {

	private UnionDescription UnionDescr1, UnionDescr1b, UnionDescr1c, UnionDescr2;
	private ArrayList variableList;
	
	public static Test suite() {
		return new TestSuite(UnionDescriptionTest.class, UnionDescriptionTest.class.getSimpleName());
	}

	/**
	 * setup for UnionDescription tests
	 */
	public void setUp() {
		UnionDescr1 = new UnionDescription();
		UnionDescr1.addVariable("x1");
		UnionDescr1.addVariable("x2");
		UnionDescr1.addVariable("x3");
		UnionDescr1b = new UnionDescription();
		UnionDescr1b.addVariable("x1");
		UnionDescr1b.addVariable("x2");
		UnionDescr1b.addVariable("x3");
		UnionDescr1c = new UnionDescription();
		UnionDescr1c.addVariable("x1");
		UnionDescr1c.addVariable("x2");
		UnionDescr1c.addVariable("x3");

		UnionDescr2 = new UnionDescription();
		UnionDescr2.addVariable("x1");
		UnionDescr2.addVariable("x2");

		variableList = new ArrayList<String>();
		variableList.add("x1");
		variableList.add("x2");
		variableList.add("x3");
	}
	
	/**
	 * test various methods of the UnionDescription-class
	 *
	 */
	public void testgetName() {

		assertEquals(UnionDescr1.getName(), "union");
	}
	public void testgetArity() {

		assertEquals(UnionDescr1.getArity(), 3);
	}
	public void testgetVariables() {

		assertEquals(UnionDescr1.getVariables(), variableList);
	}
	public void testhasVariable() {

		assertTrue(UnionDescr1.hasVariable("x2"));
	}

	/*
	 * equality
	 */
	public void testEquality_reflexiv() {
		assertTrue(UnionDescr1.equals(UnionDescr1));		
	}
	public void testEquality_symetric() {
		assertTrue(UnionDescr1.equals(UnionDescr1b));
		assertTrue(UnionDescr1b.equals(UnionDescr1));
	}
	public void testEquality_transitiv() {
		assertTrue(UnionDescr1b.equals(UnionDescr1c));
		assertTrue(UnionDescr1.equals(UnionDescr1c));
	}
	public void testEquality_consitence() {
		assertTrue(!UnionDescr1.equals(UnionDescr2));
		assertTrue(!UnionDescr1.equals(null));
	}
	public void testEquality_all2gether() {
		// ref
		assertTrue(UnionDescr1.equals(UnionDescr1));
		// sym
		assertTrue(UnionDescr1.equals(UnionDescr1b));
		assertTrue(UnionDescr1b.equals(UnionDescr1));
		// trans
		assertTrue(UnionDescr1b.equals(UnionDescr1c));
		assertTrue(UnionDescr1.equals(UnionDescr1c));
		// cons
		assertTrue(!UnionDescr1.equals(UnionDescr2));
		assertTrue(!UnionDescr1.equals(null));
	}

}