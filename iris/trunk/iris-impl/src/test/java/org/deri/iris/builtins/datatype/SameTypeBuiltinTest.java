/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2009 Semantic Technology Institute (STI) Innsbruck, 
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
package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.ITerm;

/**
 */
public class SameTypeBuiltinTest extends AbstractBooleanBuiltinTest {

	public SameTypeBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		ITerm term_1, term_2;
		
		term_1 = CONCRETE.createNMTOKEN("nm Token");
		term_2 = CONCRETE.createNMTOKEN("nm Token");
		assertTrue(sameType(term_1, term_2));
		
		term_1 = CONCRETE.createNMTOKEN("A Token");
		term_2 = CONCRETE.createNMTOKEN("Another NM Token");
		assertTrue(sameType(term_1, term_2));
		
		
		term_1 = CONCRETE.createNMTOKEN("A Token");
		term_2 = CONCRETE.createSqName("SQ#Name!");
		assertFalse(sameType(term_1, term_2));
		
		term_1 = CONCRETE.createInt(1);
		term_2 = CONCRETE.createInteger(BigInteger.valueOf(1));
		assertFalse(sameType(term_1, term_2));
		
		term_1 = CONCRETE.createDouble(1.2);
		term_2 = CONCRETE.createFloat((float) 1.2);
		assertFalse(sameType(term_1, term_2));
		
	}
	
	public boolean sameType(ITerm one, ITerm two) {
		SameTypeBuiltin stb = new SameTypeBuiltin(one, two);
		return stb.computeResult(new ITerm[]{one, two});
	}
}
