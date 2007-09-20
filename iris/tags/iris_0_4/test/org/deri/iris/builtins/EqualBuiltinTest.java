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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests for the equals builtin.
 * </p>
 * <p>
 * $Id: EqualBuiltinTest.java,v 1.6 2007-07-25 08:16:57 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.6 $
 */
public class EqualBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(EqualBuiltinTest.class, EqualBuiltinTest.class.getSimpleName());
	}

	public void testEvaluation() {
		final EqualBuiltin xy = new EqualBuiltin(TERM.createVariable("X"), TERM.createVariable("Y"));

		assertNotNull("5 should be equal to 5", xy.evaluate(
					BASIC.createTuple(CONCRETE.createInteger(5), CONCRETE.createInteger(5))));
		assertNotNull("5 should be equal to 5.0", xy.evaluate(
					BASIC.createTuple(CONCRETE.createInteger(5), CONCRETE.createDouble(5d))));
		assertNull("5 should not equal to 2", xy.evaluate(
					BASIC.createTuple(CONCRETE.createInteger(2), CONCRETE.createInteger(5))));
		assertNull("5 should not be equal to a", xy.evaluate(
					BASIC.createTuple(CONCRETE.createInteger(5), TERM.createString("a"))));
	}
	
	public void test_isBuiltin() {
		assertTrue("buitin predicates should be identifiable as builtins", (new EqualBuiltin(CONCRETE
				.createInteger(5), CONCRETE.createInteger(5)).isBuiltin()));
	}
}
