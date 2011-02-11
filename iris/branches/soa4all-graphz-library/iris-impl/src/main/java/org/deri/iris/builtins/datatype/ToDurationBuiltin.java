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
import org.deri.iris.api.terms.concrete.IDayTimeDuration;
import org.deri.iris.api.terms.concrete.IDuration;
import org.deri.iris.api.terms.concrete.IYearMonthDuration;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to Duration instances. The following data types are supported:
 * <ul>
 * <li>DayTimeDuration</li>
 * <li>YearMonthDuration</li>
 * </ul>
 */
public class ToDurationBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_DURATION", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToDurationBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		return toDuration(term);
	}

	/**
	 * Converts the specified term to a Duration term, if possible.
	 * 
	 * @param term
	 *            The term to be converted.
	 * @return A new Duration term representing the result of the conversion, or
	 *         <code>null</code> if the term can not be converted.
	 */
	public static IDuration toDuration(ITerm term) {
		if (term instanceof IDuration) {
			return (IDuration) term;
		} else if (term instanceof IDayTimeDuration) {
			return toDuration((IDayTimeDuration) term);
		} else if (term instanceof IYearMonthDuration) {
			return toDuration((IYearMonthDuration) term);
		}

		return null;
	}

	/**
	 * Converts a DayTimeDuration term to a Duration term.
	 * 
	 * @param term
	 *            The DayTimeDuration term to be converted.
	 * @return A new Duration term representing the result of the conversion.
	 */
	public static IDuration toDuration(IDayTimeDuration term) {
		return CONCRETE.createDuration(term.isPositive(), 0, 0, term.getDay(),
				term.getHour(), term.getMinute(), term.getDecimalSecond());
	}

	/**
	 * Converts a YearMonthDuration term to a Duration term.
	 * 
	 * @param term
	 *            The YearMonthDuration term to be converted.
	 * @return A new Duration term representing the result of the conversion.
	 */
	public static IDuration toDuration(IYearMonthDuration term) {
		return CONCRETE.createDuration(term.isPositive(), term.getYear(),
				term.getMonth(), 0, 0, 0, 0);
	}

}
