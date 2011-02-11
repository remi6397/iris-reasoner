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
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IDateTerm;
import org.deri.iris.api.terms.concrete.IDateTime;
import org.deri.iris.api.terms.concrete.IGMonthDay;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to GMonthDay instances. The following data types are
 * supported:
 * <ul>
 * <li>Date</li>
 * <li>DateTime</li>
 * </ul>
 */
public class ToGMonthDayBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_GMONTHDAY", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms The term representing the data type instance to be
	 *            converted.
	 */
	public ToGMonthDayBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IGMonthDay) {
			return term;
		} else if (term instanceof IDateTerm) {
			return toGMonthDay((IDateTerm) term);
		} else if (term instanceof IDateTime) {
			return toGMonthDay((IDateTime) term);
		}

		return null;
	}

	/**
	 * Converts a Date term to a GMonthDay term.
	 * 
	 * @param term The Date term to be converted.
	 * @return A new GMonthDay term representing the result of the conversion.
	 */
	public static IGMonthDay toGMonthDay(IDateTerm term) {
		return CONCRETE.createGMonthDay(term.getMonth(), term.getDay());
	}

	/**
	 * Converts a DateTime term to a GMonthDay term.
	 * 
	 * @param term The DateTime term to be converted.
	 * @return A new GMonthDay term representing the result of the conversion.
	 */
	public static IGMonthDay toGMonthDay(IDateTime term) {
		return CONCRETE.createGMonthDay(term.getMonth(), term.getDay());
	}

}
