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
 * type instances to YearMonthDuration instances. The following data types are
 * supported:
 * <ul>
 * <li>Duration</li>
 * <li>DayTimeDuration</li>
 * </ul>
 */
public class ToYearMonthDurationBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_YEARMONTHDURATION", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms The term representing the data type instance to be
	 *            converted.
	 */
	public ToYearMonthDurationBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IYearMonthDuration) {
			return term;
		} else if (term instanceof IDuration) {
			return toYearMonthDuration((IDuration) term);
		} else if (term instanceof IDayTimeDuration) {
			return toYearMonthDuration((IDayTimeDuration) term);
		}

		return null;
	}

	/**
	 * Converts a Duration term to a YearMonthDuration term.
	 * 
	 * @param term The Duration term to be converted.
	 * @return A new YearMonthDuration term representing the result of the
	 *         conversion.
	 */
	public static IYearMonthDuration toYearMonthDuration(IDuration term) {
		return CONCRETE.createYearMonthDuration(term.isPositive(), term
				.getYear(), term.getMonth());
	}

	/**
	 * Converts a DayTimeDuration term to a YearMonthDuration term.
	 * 
	 * @param term The DayTimeDuration term to be converted.
	 * @return A new YearMonthDuration term representing the result of the
	 *         conversion.
	 */
	public static IYearMonthDuration toYearMonthDuration(IDayTimeDuration term) {
		return CONCRETE.createYearMonthDuration(term.isPositive(), 0, 0);
	}

}
