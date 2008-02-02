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
package org.deri.iris.terms;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.terms.concrete.Base64BinaryTest;
import org.deri.iris.terms.concrete.BooleanTest;
import org.deri.iris.terms.concrete.DateTest;
import org.deri.iris.terms.concrete.DateTimeTest;
import org.deri.iris.terms.concrete.DecimalTest;
import org.deri.iris.terms.concrete.DoubleTest;
import org.deri.iris.terms.concrete.DurationTest;
import org.deri.iris.terms.concrete.FloatTest;
import org.deri.iris.terms.concrete.GDayTest;
import org.deri.iris.terms.concrete.GMonthDayTest;
import org.deri.iris.terms.concrete.GMonthTest;
import org.deri.iris.terms.concrete.GYearMonthTest;
import org.deri.iris.terms.concrete.GYearTest;
import org.deri.iris.terms.concrete.HexBinaryTest;
import org.deri.iris.terms.concrete.IntegerTest;
import org.deri.iris.terms.concrete.IriTest;
import org.deri.iris.terms.concrete.SqNameTest;

public class AllDatatypesTest extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite(AllDatatypesTest.class.getSimpleName());
		suite.addTest(Base64BinaryTest.suite());
		suite.addTest(BooleanTest.suite());
		suite.addTest(DateTimeTest.suite());
		suite.addTest(DurationTest.suite());
		suite.addTest(GDayTest.suite());
		suite.addTest(GMonthDayTest.suite());
		suite.addTest(GMonthTest.suite());
		suite.addTest(GYearMonthTest.suite());
		suite.addTest(GYearTest.suite());
		suite.addTest(HexBinaryTest.suite());
		suite.addTest(IriTest.suite());
		suite.addTest(SqNameTest.suite());
		suite.addTest(StringTermTest.suite());
		suite.addTest(IntegerTest.suite());
		suite.addTest(FloatTest.suite());
		suite.addTest(DecimalTest.suite());
		suite.addTest(DoubleTest.suite());
		suite.addTest(VariableTest.suite());
		suite.addTest(ConstructedTermTest.suite());
		suite.addTest(DateTest.suite());
		return suite;
	}
}
