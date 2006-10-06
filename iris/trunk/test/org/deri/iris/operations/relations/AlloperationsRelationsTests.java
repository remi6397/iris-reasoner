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
package org.deri.iris.operations.relations;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * NOTE: 
 * 
 * UnificationTest has not been added here, although 
 * this operation belongs to the relation operations. 
 * As this test is placed in a separate package 
 * (org.deri.iris.operations.relations.unification) 
 * the test is placed in the corresponding package too.
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 06.10.2006 10:02:43
 */
public class AlloperationsRelationsTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite(AlloperationsRelationsTests.class.getSimpleName());
		suite.addTest(CartesianProductTest.suite());
		suite.addTest(DifferenceTest.suite());
		suite.addTest(JoinTest.suite());
		suite.addTest(ProjectionTest.suite());
		suite.addTest(SelectionNumericTest.suite());
		suite.addTest(SelectionStringTest.suite());
		suite.addTest(UnionTest.suite());
		return suite;
	}
}
