/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
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
package org.deri.iris.basics;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author richi
 * 
 * Revision 1.1  26.07.2006 09:16:56  Darko Anicic, DERI Innsbruck
 */
public class AllBasicstest extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite(AllBasicstest.class.getSimpleName());
		suite.addTest(PredicateTest.suite());
		suite.addTest(TupleTest.suite());
		suite.addTest(AtomTest.suite());
		suite.addTest(LiteralTest.suite());
		suite.addTest(RuleTest.suite());
		suite.addTest(QueryTest.suite());
		return suite;
	}
}
