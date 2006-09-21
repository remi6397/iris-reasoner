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

import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests for the greater equal builtin.
 * </p>
 * <p>
 * $Id: GreaterEqualBuiltinTest.java,v 1.1 2006-09-21 09:02:11 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.1 $
 * @date $Date: 2006-09-21 09:02:11 $
 */
public class GreaterEqualBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(GreaterEqualBuiltinTest.class,
				GreaterEqualBuiltinTest.class.getSimpleName());
	}

	public void testEvaluation() {
		assertTrue("5 should be greater-equal to 5", (new GreaterEqualBuiltin(
				CONCRETE.createInteger(5), CONCRETE.createInteger(5)))
				.evaluate());
		assertTrue("5 should be greater-equal to 5.0",
				(new GreaterEqualBuiltin(CONCRETE.createInteger(5), CONCRETE
						.createDouble(5d))).evaluate());

		assertFalse("2 shouldn't be greater than 5.0", (new GreaterEqualBuiltin(
				CONCRETE.createInteger(2), CONCRETE.createDouble(5d)))
				.evaluate());
		assertTrue("5 should be greater than 2", (new GreaterEqualBuiltin(
				CONCRETE.createInteger(5), CONCRETE.createInteger(2)))
				.evaluate());

		assertFalse("a shouldn't be greater than b", (new GreaterEqualBuiltin(TERM
				.createString("a"), TERM.createString("b"))).evaluate());
		assertTrue("a should be greater-equal to a", (new GreaterEqualBuiltin(
				TERM.createString("a"), TERM.createString("a"))).evaluate());

		assertFalse("5 less a should be false -> not evaluable",
				(new GreaterEqualBuiltin(CONCRETE.createInteger(5), TERM
						.createString("a"))).evaluate());
	}
}
