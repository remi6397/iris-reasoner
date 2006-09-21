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
 * Tests for the greater builtin.
 * </p>
 * <p>
 * $Id: GreaterBuiltinTest.java,v 1.1 2006-09-21 09:02:11 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.1 $
 * @date $Date: 2006-09-21 09:02:11 $
 */
public class GreaterBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(GreaterBuiltinTest.class, GreaterBuiltinTest.class
				.getSimpleName());
	}

	public void testEvaluation() {
		assertFalse("5 shouldn't be greater than 5", (new GreaterBuiltin(CONCRETE
				.createInteger(5), CONCRETE.createInteger(5))).evaluate());
		assertFalse("5 shouldn't be greater than 5.0", (new GreaterBuiltin(
				CONCRETE.createInteger(5), CONCRETE.createDouble(5d)))
				.evaluate());

		assertFalse("2 shouldn't be greater than 5.0", (new GreaterBuiltin(CONCRETE
				.createInteger(2), CONCRETE.createDouble(5d))).evaluate());
		assertTrue("5 should be greater than 2", (new GreaterBuiltin(CONCRETE
				.createInteger(5), CONCRETE.createInteger(2))).evaluate());

		assertFalse("a shouldn't be greater than b", (new GreaterBuiltin(TERM
				.createString("a"), TERM.createString("b"))).evaluate());
		assertFalse("a shouldn't be greater to a", (new GreaterBuiltin(TERM
				.createString("a"), TERM.createString("a"))).evaluate());

		assertFalse("5 less a should be false -> not evaluable",
				(new GreaterBuiltin(CONCRETE.createInteger(5), TERM
						.createString("a"))).evaluate());
	}
}
