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

import org.deri.iris.api.basics.ITuple;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests for the unequals builtin.
 * </p>
 * <p>
 * $Id: UnequalBuiltinTest.java,v 1.2 2007-05-10 07:01:18 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.2 $
 */
public class UnequalBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(UnequalBuiltinTest.class, UnequalBuiltinTest.class.getSimpleName());
	}

	public void testEvaluation() throws Exception {
		final ITuple x2 = BASIC.createTuple(TERM.createVariable("X"), TERM.createVariable("X"));
		assertNull("5 shouldn't be unequal to 5", (new NotEqualBuiltin(CONCRETE
				.createInteger(5), CONCRETE.createInteger(5))).evaluate(x2));
		assertNull("5 shouldn't be unequal to 5.0", (new NotEqualBuiltin(CONCRETE
				.createInteger(5), CONCRETE.createDouble(5d))).evaluate(x2));
		assertNotNull("5 should be unequal to 2", (new NotEqualBuiltin(CONCRETE
				.createInteger(2), CONCRETE.createInteger(5))).evaluate(x2));
		assertNotNull("5 should be unequal to a", (new NotEqualBuiltin(CONCRETE
				.createInteger(5), TERM.createString("a"))).evaluate(x2));
	}
}
