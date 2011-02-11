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
 * type instances to HexBinary instances. The following data types are
 * supported:
 * <ul>
 * <li>Base64</li>
 * <li>String</li>
 * </ul>
 */
public class ToHexBinaryBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_HEXBINARY", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms The term representing the data type instance to be
	 *            converted.
	 */
	public ToHexBinaryBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IHexBinary) {
			return term;
		} else if (term instanceof IBase64Binary) {
			return toHexBinary((IBase64Binary) term);
		} else if (term instanceof IStringTerm) {
			return toHexBinary((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a String term to a HexBinary term.
	 * 
	 * @param term The String term to be converted.
	 * @return A new HexBinary term representing the result of the conversion,
	 *         or <code>null</code> if the conversion fails.
	 */
	public static IHexBinary toHexBinary(IStringTerm term) {
		String binary = term.getValue();
		return toHexBinary(binary);
	}

	/**
	 * Converts a Base64 term to a HexBinary term.
	 * 
	 * @param term The Base64 term to be converted.
	 * @return A new HexBinary term representing the result of the conversion.
	 */
	public static IHexBinary toHexBinary(IBase64Binary term) {
		String binary = term.getValue();
		return toHexBinary(binary);
	}

	private static IHexBinary toHexBinary(String binary) {
		try {
			return CONCRETE.createHexBinary(binary);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"The specified string does not represent a hex binary value",
					e);
		}
	}

}
