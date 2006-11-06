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
 * @version $Id: DifferenceDescriptionTest.java,v 1.1 2006-11-06 09:30:10 adi Exp $
 */
public class DifferenceDescriptionTest extends TestCase {

	private DifferenceDescription differenceDescr1, differenceDescr2;
	private ArrayList variableList;
	
	public static Test suite() {
		return new TestSuite(DifferenceDescriptionTest.class, DifferenceDescriptionTest.class.getSimpleName());
	}

	/**
	 * setup for DifferenceDescription tests
	 */
	public void setUp() {
		differenceDescr1 = new DifferenceDescription();
		differenceDescr1.addVariable("x1");
		differenceDescr1.addVariable("x2");
		differenceDescr1.addVariable("x3");

		differenceDescr2 = new DifferenceDescription();
		differenceDescr2.addVariable("x1");
		differenceDescr2.addVariable("x2");
		differenceDescr2.addVariable("x3");

		variableList = new ArrayList<String>();
		variableList.add("x1");
		variableList.add("x2");
		variableList.add("x3");
	}
	
	/**
	 * test various methods of the DifferenceDescription-class
	 *
	 */
	public void testgetName() {

		assertEquals(differenceDescr1.getName(), "difference");
	}
	public void testgetArity() {

		assertEquals(differenceDescr1.getArity(), 3);
	}
	public void testgetVariables() {

		assertEquals(differenceDescr1.getVariables(), variableList);
	}
	public void testhasVariable() {

		assertTrue(differenceDescr1.hasVariable("x2"));
	}
/*	public void testEquality() {

		assertTrue(differenceDescr1.equals(differenceDescr2));
	}
*/
}