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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;

public abstract class AbstractBooleanBuiltinTest extends TestCase {
	
	public AbstractBooleanBuiltinTest(String name) {
		super(name);
	}

	private void checkBooleanBuiltin(boolean expected, ITerm term,
			String datatypeIRI, String builtinName) throws EvaluationException,
			ClassNotFoundException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {

		ITerm datatypeIRITerm = CONCRETE.createIri(datatypeIRI);
		ITuple arguments = BASIC.createTuple(term, datatypeIRITerm);

		Class<?> builtinC = Class.forName(builtinName);
		Constructor<?> builtinO = builtinC
				.getConstructor(new Class[] { ITerm[].class });

		Object[] params = new Object[1];
		params[0] = new ITerm[] { term };
		Object builtinobject = builtinO.newInstance(params);

		Class<?>[] params2 = new Class[1];
		params2[0] = ITuple.class;
		Method em = builtinC.getMethod("evaluate", params2);
		ITuple actualTuple = (ITuple) em.invoke(builtinobject, arguments);

		if (expected) {
			assertNotNull("Error builtin should be true: " + builtinName,
					actualTuple);
		} else {
			assertNull("Error builtin should be false: " + builtinName,
					actualTuple);
		}
	}

	/**
	 * Checks if the boolean Builtin (Is... IsNot...) does right
	 * 
	 * @param iri
	 * @param builtin
	 * @param builtinNames the first builtinName is the of the testing class -
	 *            the additional ones for classes which also match (numeric,..)
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws EvaluationException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected void checkBuiltin(String iri, ITerm builtin,
			String... builtinNames) throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String builtinName = builtinNames[0];
		boolean b = true;
		String builtinNotName = null;
		if (builtinName.contains("Not")) { // if its a isNot builtin
			b = false;
			builtinNotName = builtinName.replace("IsNot", "Is");
		} else { // get out the is - builtin
			b = true;
			builtinNotName = builtinName.replace("Is", "IsNot");
		}

		// 2 lists for is and isNot builtins
		ArrayList<String> listIS = this.getISBuiltins();
		listIS.remove(builtinName);
		listIS.remove(builtinNotName);

		ArrayList<String> listISNot = this.getISNotBuiltins();
		listISNot.remove(builtinName);
		listISNot.remove(builtinNotName);

		// remove the other builtins which are also true - will be testet in
		// their own test
		for (int i = 1; i < builtinNames.length; i++) {
			checkBooleanBuiltin(b, builtin, iri, builtinNames[i]);
			listIS.remove(builtinNames[i]);
			listISNot.remove(builtinNames[i]);
			if (builtinNames[i].contains("Not")) {
				listIS.remove(builtinNames[i].replace("IsNot", "Is"));
			} else {
				listISNot.remove(builtinNames[i].replace("Is", "IsNot"));
			}
		}

		// at first check if the builtin checks the right one (or not if its a
		// NOT-builtin)
		// ex.: IsByteBuiltin - IsNotByteBuiltin
		checkBooleanBuiltin(b, builtin, iri, builtinName);
		checkBooleanBuiltin(!b, builtin, iri, builtinNotName);

		// check: all other should be false ...
		for (String name : listIS) {
			checkBooleanBuiltin(false, builtin, iri, name);
		}

		// check: all other should be true (not something) ...
		for (String name : listISNot) {
			checkBooleanBuiltin(true, builtin, iri, name);
		}

	}

	private ArrayList<String> getISBuiltins() {
		ArrayList<String> list = new ArrayList<String>();

		// list.add("org.deri.iris.builtins.datatype.IsAnyURIBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsBase64BinaryBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsBooleanBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsByteBuiltin");
		// list.add("org.deri.iris.builtins.datatype.IsDatatypeBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsDateBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsDateTimeBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsDateTimeStampBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsDayTimeDurationBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsDecimalBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsDoubleBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsDurationBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsFloatBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsGDayBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsGMonthBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsGMonthDayBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsGYearMonthBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsHexBinaryBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsIDBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsIDREFBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsIntBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsIntegerBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsIriBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsLanguageBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsLongBuiltin");
//		list.add("org.deri.iris.builtins.datatype.IsNameBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNCNameBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNegativeIntegerBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNMTOKENBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNonNegativeIntegerBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNonPositiveIntegerBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNormalizedStringBuiltin");

		list.add("org.deri.iris.builtins.datatype.IsNumericBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsPlainLiteralBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsPositiveIntegerBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsQNameBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsShortBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsSqNameBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsStringBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsTimeBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsTokenBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsUnsignedByteBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsUnsignedIntBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsUnsignedLongBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsUnsignedShortBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsXMLLiteralBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsYearMonthDurationBuiltin");

		// list.add("org.deri.iris.builtins.datatype.SameTypeBuiltin");
		return list;
	}

	private ArrayList<String> getISNotBuiltins() {
		ArrayList<String> list = new ArrayList<String>();
		// list.add("org.deri.iris.builtins.datatype.IsNotAnyURIBuiltin");
		// list.add("org.deri.iris.builtins.datatype.IsNOTATIONBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotBase64BinaryBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotBooleanBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotByteBuiltin");
		// list.add("org.deri.iris.builtins.datatype.IsNotDatatypeBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotDateBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotDateTimeBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotDateTimeStampBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotDayTimeDurationBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotDecimalBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotDoubleBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotDurationBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotFloatBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotGDayBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotGMonthBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotGMonthDayBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotGYearBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotGYearMonthBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotHexBinaryBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotIDBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotIDREFBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotIntBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotIntegerBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotIriBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotLanguageBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotLongBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotNameBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotNCNameBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotNegativeIntegerBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotNMTOKENBuiltin");
		list
				.add("org.deri.iris.builtins.datatype.IsNotNonNegativeIntegerBuiltin");
		list
				.add("org.deri.iris.builtins.datatype.IsNotNonPositiveIntegerBuiltin");
		list
				.add("org.deri.iris.builtins.datatype.IsNotNormalizedStringBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotNOTATIONBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotNumericBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotPlainLiteralBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotPositiveIntegerBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotQNameBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotShortBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotSqNameBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotStringBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotTimeBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotTokenBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotUnsignedByteBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotUnsignedIntBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotUnsignedLongBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotUnsignedShortBuiltin");
		list.add("org.deri.iris.builtins.datatype.IsNotXMLLiteralBuiltin");
		list
				.add("org.deri.iris.builtins.datatype.IsNotYearMonthDurationBuiltin");

		return list;

	}

}
