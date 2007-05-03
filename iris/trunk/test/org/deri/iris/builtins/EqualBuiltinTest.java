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
 * Tests for the equals builtin.
 * </p>
 * <p>
 * $Id: EqualBuiltinTest.java,v 1.4 2007-05-03 11:28:30 darko_anicic Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.4 $
 * @date $Date: 2007-05-03 11:28:30 $
 */
public class EqualBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(EqualBuiltinTest.class, EqualBuiltinTest.class.getSimpleName());
	}

	public void testEvaluation() {
		assertTrue("5 should be equal to 5", (new EqualBuiltin(
				TERM.createVariable("X"),TERM.createVariable("Y")).evaluate(
						CONCRETE.createInteger(5), CONCRETE.createInteger(5))));
		assertTrue("5 should not be equal to 5.0", (new EqualBuiltin(CONCRETE
				.createInteger(5), CONCRETE.createDouble(5d))).evaluate(
						CONCRETE.createInteger(5), CONCRETE.createDouble(5d)));
		assertFalse("5 shouldn't be equal to 2", (new EqualBuiltin(CONCRETE
				.createInteger(2), CONCRETE.createInteger(5))).evaluate(
						CONCRETE.createInteger(2), CONCRETE.createInteger(5)));
		assertFalse("5 should be equal to a", (new EqualBuiltin(CONCRETE
				.createInteger(5), TERM.createString("a"))).evaluate(
						CONCRETE.createInteger(5), TERM.createString("a")));
	}
	
	public void test_isBuiltin() {
		assertTrue("buitin predicates should be identifiable as builtins", (new EqualBuiltin(CONCRETE
				.createInteger(5), CONCRETE.createInteger(5)).getPredicate().isBuiltIn()));
	}
}
