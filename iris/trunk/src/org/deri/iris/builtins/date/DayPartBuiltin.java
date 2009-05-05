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
import org.deri.iris.api.terms.concrete.IDayTimeDuration;
import org.deri.iris.api.terms.concrete.IDuration;
import org.deri.iris.builtins.AbstractBuiltin;
import org.deri.iris.builtins.datatype.ToDayTimeDurationBuiltin;
import org.deri.iris.factory.Factory;

/**
 * Represents the func:day-from-dateTime, func:day-from-date and
 * func:days-from-duration functions as described in
 * http://www.w3.org/TR/xpath-functions/#func-day-from-dateTime.
 */
public class DayPartBuiltin extends AbstractBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"DAYPART", 1);

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
	public DayPartBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm evaluateTerms(ITerm[] terms, int[] variableIndexes)
			throws EvaluationException {
		assert variableIndexes.length == 0;
		assert terms.length == 1;

		int day = 0;

		if (terms[0] instanceof IDateTerm) {
			IDateTerm date = (IDateTerm) terms[0];
			day = date.getDay();
		} else if (terms[0] instanceof IDateTime) {
			IDateTime dateTime = (IDateTime) terms[0];
			day = dateTime.getDay();
		} else if (terms[0] instanceof IDuration) {
			IDuration duration = (IDuration) terms[0];

			IDayTimeDuration dayTime = ToDayTimeDurationBuiltin
					.toDayTimeDuration(duration);
			dayTime = dayTime.toCanonical();

			day = dayTime.getDay();

			if (!dayTime.isPositive()) {
				day *= -1;
			}
		} else {
			return null;
		}

		return Factory.CONCRETE.createInteger(day);
	}

	public int maxUnknownVariables() {
		return 0;
	}

}
