/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IBase64Binary;
import org.deri.iris.api.terms.concrete.IHexBinary;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to Base64 instances. The following data types are supported:
 * <ul>
 * <li>HexBinary</li>
 * <li>String</li>
 * </ul>
 */
public class ToBase64Builtin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_BASE64", 2);

	/**
	 * Creates a new instance of this built-in.
	 * 
	 * @param terms The term representing the data type instance to be
	 *            converted.
	 */
	public ToBase64Builtin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IHexBinary) {
			return toBase64((IHexBinary) term);
		} else if (term instanceof IBase64Binary) {
			return term;
		} else if (term instanceof IStringTerm) {
			return toBase64((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a String term to a Base64 term.
	 * 
	 * @param term The String term to be converted.
	 * @return A new Base64 term representing the result of the conversion.
	 */
	public static IBase64Binary toBase64(IStringTerm term) {
		String binary = term.getValue();
		return toBase64(binary);
	}

	/**
	 * Converts a HexBinary term to a Base64 term.
	 * 
	 * @param term The HexBinary term to be converted.
	 * @return A new Base64 term representing the result of the conversion.
	 */
	public static IBase64Binary toBase64(IHexBinary term) {
		String binary = term.getValue();
		return toBase64(binary);
	}

	private static IBase64Binary toBase64(String binary) {
		return CONCRETE.createBase64Binary(binary);
	}

}
