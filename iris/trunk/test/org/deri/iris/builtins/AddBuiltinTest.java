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

import java.util.List;
import java.util.LinkedList;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.concrete.IIntegerTerm;
import org.deri.iris.api.terms.ITerm;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests for the {@code AddBuiltin}.
 * </p>
 * <p>
 * $Id: AddBuiltinTest.java,v 1.3 2007-05-10 07:01:15 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.3 $
 */
public class AddBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(AddBuiltinTest.class, AddBuiltinTest.class
				.getSimpleName());
	}

	public void testEvaluate() {
		final ITerm X = TERM.createVariable("X");
		final ITerm Y = TERM.createVariable("Y");
		final ITerm Z = TERM.createVariable("Z");
		final ITerm T_4 = CONCRETE.createInteger(4);
		final ITerm T_65 = CONCRETE.createFloat(6.5f);

		// X + 4 = Y
		final AddBuiltin a_x4y = new AddBuiltin(X, T_4, Y);
		assertEquals(BASIC.createTuple(CONCRETE.createInteger(10)), a_x4y.evaluate(BASIC.createTuple(CONCRETE.createInteger(6), Y, X)));
		assertEquals(BASIC.createTuple(CONCRETE.createInteger(20)), a_x4y.evaluate(BASIC.createTuple(CONCRETE.createInteger(16), Y, X)));
		assertEquals(BASIC.createTuple(CONCRETE.createInteger(2)), a_x4y.evaluate(BASIC.createTuple(X, Y, CONCRETE.createInteger(6))));
		assertEquals(BASIC.createTuple(CONCRETE.createInteger(12)), a_x4y.evaluate(BASIC.createTuple(X,Y, CONCRETE.createInteger(16))));
		// X + Y = 6.5
		final AddBuiltin a_xy65 = new AddBuiltin(X, Y, T_65);
		a_xy65.evaluate(BASIC.createTuple(T_4, T_4, X));
		// X + Y = Z
		final AddBuiltin a_xyz = new AddBuiltin(X, Y, Z);
	}
}
