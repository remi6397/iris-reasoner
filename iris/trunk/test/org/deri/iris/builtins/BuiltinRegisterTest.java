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
 * Tests for the <code>BuiltinRegister</code>.
 * </p>
 * <p>
 * $Id: BuiltinRegisterTest.java,v 1.1 2007-09-05 09:37:15 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.1 $
 */
public class BuiltinRegisterTest extends TestCase {

	/** The register on which to operate. */
	private BuiltinRegister reg;

	public static Test suite() {
		return new TestSuite(BuiltinRegisterTest.class, BuiltinRegisterTest.class.getSimpleName());
	}

	public void setUp() {
		reg = new BuiltinRegister();
	}

	/**
	 * Checks whether the core builtins gets registered correctly.
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1784947&group_id=167309&atid=985821">bug #1784947: simple possibility to make new core builtins parseable</a>
	 */
	public void testRegisterCoreBuiltins() {
		// test the add builtin
		checkRegisteredBuiltin("ADD", org.deri.iris.builtins.AddBuiltin.class, 3);
		// test the subtract builtin
		checkRegisteredBuiltin("SUBTRACT", org.deri.iris.builtins.SubtractBuiltin.class, 3);
		// test the multiply builtin
		checkRegisteredBuiltin("MULTIPLY", org.deri.iris.builtins.MultiplyBuiltin.class, 3);
		// test the divide builtin
		checkRegisteredBuiltin("DIVIDE", org.deri.iris.builtins.DivideBuiltin.class, 3);
		// test the equal builtin
		checkRegisteredBuiltin("EQUAL", org.deri.iris.builtins.EqualBuiltin.class, 2);
		// test the unequal builtin
		checkRegisteredBuiltin("UNEQUAL", org.deri.iris.builtins.UnEqualBuiltin.class, 2);
		// test the less-than builtin
		checkRegisteredBuiltin("LESS", org.deri.iris.builtins.LessBuiltin.class, 2);
		// test the less-equal builtin
		checkRegisteredBuiltin("LESS_EQUAL", org.deri.iris.builtins.LessEqualBuiltin.class, 2);
		// test the greather-than builtin
		checkRegisteredBuiltin("GREATR", org.deri.iris.builtins.GreaterBuiltin.class, 2);
		// test the greather-equal builtin
		checkRegisteredBuiltin("GREATER_EQUAL", org.deri.iris.builtins.GreaterEqualBuiltin.class, 2);

		// test the isboolean builtin
		checkRegisteredBuiltin("ISBOOLEAN", org.deri.iris.builtins.IsBooleanBuiltin.class, 1);
		// test the isdate builtin
		checkRegisteredBuiltin("ISDATE", org.deri.iris.builtins.IsDateBuiltin.class, 1);
		// test the isdatetime builtin
		checkRegisteredBuiltin("ISDATETIME", org.deri.iris.builtins.IsDateTimeBuiltin.class, 1);
		// test the isdecimal builtin
		checkRegisteredBuiltin("ISDECIMAL", org.deri.iris.builtins.IsDecimalBuiltin.class, 1);
		// test the isinteger builtin
		checkRegisteredBuiltin("ISINTEGER", org.deri.iris.builtins.IsIntegerBuiltin.class, 1);
		// test the isstring builtin
		checkRegisteredBuiltin("ISSTRING", org.deri.iris.builtins.IsStringBuiltin.class, 1);
	}

	/**
	 * Asserts whether a builtin was registered with the correct name, class
	 * and arity.
	 * @param name the name of the builtin to check (predicate symbol)
	 * @param clazz the class of the builtin to check
	 * @param arity the arity of the builtin to check
	 */
	private void checkRegisteredBuiltin(final String name, final Class clazz, final int arity) {
		assert name != null: "The name must not be null";
		assert clazz != null: "The class must not be null";
		assert arity > 0: "The arity must be greater than 0";

		assertEquals("Could not find the class of " + name, clazz, reg.getBuiltinClass(name));
		assertEquals("Could not find the arity for " + name, arity, reg.getBuiltinArity(name));
	}

}
