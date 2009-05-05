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
package org.deri.iris.builtins.date;

import static org.deri.iris.factory.Factory.BASIC;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IDateTerm;
import org.deri.iris.api.terms.concrete.IDateTime;
import org.deri.iris.api.terms.concrete.IDuration;
import org.deri.iris.api.terms.concrete.IYearMonthDuration;
import org.deri.iris.builtins.AbstractBuiltin;
import org.deri.iris.builtins.datatype.ToYearMonthDurationBuiltin;
import org.deri.iris.factory.Factory;

/**
 * Represents the func:year-from-dateTime, func:year-from_date and
 * func:year-from-duration functions as described in
 * http://www.w3.org/TR/xpath-functions/#func-year-from-dateTime.
 */
public class YearPartBuiltin extends AbstractBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"YEARPART", 1);

	/**
	 * Constructor. One term must be passed to the constructor, otherwise an
	 * exception will be thrown.
	 * 
	 * @param terms
	 *            the terms
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 * @throws IllegalArgumentException
	 *             if the number of terms submitted is not 2
	 * @throws IllegalArgumentException
	 *             if t is <code>null</code>
	 */
	public YearPartBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm evaluateTerms(ITerm[] terms, int[] variableIndexes)
			throws EvaluationException {
		assert variableIndexes.length == 0;
		assert terms.length == 1;

		int year = 0;

		if (terms[0] instanceof IDateTerm) {
			IDateTerm date = (IDateTerm) terms[0];
			year = date.getYear();
		} else if (terms[0] instanceof IDateTime) {
			IDateTime dateTime = (IDateTime) terms[0];
			year = dateTime.getYear();

			// Quick fix for "new year problem". year-from-dateTime of dateTime
			// "1999-12-31T24:00:00" should yield 2000 instead of 1999.
			if (dateTime.getMonth() == 12 && dateTime.getDay() == 31
					&& dateTime.getHour() == 24 || dateTime.getHour() == 0) {
				year++;
			}
		} else if (terms[0] instanceof IDuration) {
			IDuration duration = (IDuration) terms[0];

			IYearMonthDuration yearMonth = ToYearMonthDurationBuiltin
					.toYearMonthDuration(duration);
			yearMonth = yearMonth.toCanonical();

			year = yearMonth.getYear();

			if (!yearMonth.isPositive()) {
				year *= -1;
			}
		} else {
			return null;
		}

		return Factory.CONCRETE.createInteger(year);
	}

	public int maxUnknownVariables() {
		return 0;
	}

}
