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

import java.util.TimeZone;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IDateTime;
import org.deri.iris.api.terms.concrete.ITime;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to Time instances. The following data types are supported:
 * <ul>
 * <li>DateTime</li>
 * </ul>
 */
public class ToTimeBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_TIME", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms The term representing the data type instance to be
	 *            converted.
	 */
	public ToTimeBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof ITime) {
			return term;
		} else if (term instanceof IDateTime) {
			return toTime((IDateTime) term);
		}

		return null;
	}

	/**
	 * Converts a DateTime term to a Time term.
	 * 
	 * @param term The DateTime term to be converted.
	 * @return A new Time term representing the result of the conversion.
	 */
	public static ITime toTime(IDateTime term) {
		TimeZone timeZone = term.getTimeZone();
		int offset = timeZone.getRawOffset();

		int tzHour = offset / 3600000;
		int tzMinute = (Math.abs(offset) % 3600000) / 60000;

		if (offset < 0) {
			tzMinute *= -1;
		}

		return CONCRETE.createTime(term.getHour(), term.getMinute(), term
				.getSecond(), tzHour, tzMinute);
	}

}
