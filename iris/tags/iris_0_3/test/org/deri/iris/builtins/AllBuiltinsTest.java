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
package org.deri.iris.builtins;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Runns all builtins tests at once.
 * </p>
 * <p>
 * $Id: AllBuiltinsTest.java,v 1.1 2006-09-28 11:30:50 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.1 $
 * @date $Date: 2006-09-28 11:30:50 $
 */
public class AllBuiltinsTest extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite(AllBuiltinsTest.class.getSimpleName());
		suite.addTest(AddBuiltinTest.suite());
		suite.addTest(DivideBuiltinTest.suite());
		suite.addTest(EqualBuiltinTest.suite());
		suite.addTest(GreaterBuiltinTest.suite());
		suite.addTest(GreaterEqualBuiltinTest.suite());
		suite.addTest(LessBuiltinTest.suite());
		suite.addTest(LessEqualBuiltinTest.suite());
		suite.addTest(MultiplyBuiltinTest.suite());
		suite.addTest(SubtractBuiltinTest.suite());
		suite.addTest(UnequalBuiltinTest.suite());
		return suite;
	}
}
