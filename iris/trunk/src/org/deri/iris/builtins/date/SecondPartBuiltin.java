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
import org.deri.iris.api.terms.concrete.IDateTime;
import org.deri.iris.api.terms.concrete.IDuration;
import org.deri.iris.api.terms.concrete.ITime;
import org.deri.iris.builtins.AbstractBuiltin;
import org.deri.iris.factory.Factory;

/**
 * Represents the func:minutes-from-dateTime and func:minutes-from-duration
 * functions as described in
 * http://www.w3.org/TR/xpath-functions/#func-minutes-from-dateTime.
 */
public class SecondPartBuiltin extends AbstractBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"SECONDPART", 1);

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
	public SecondPartBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm evaluateTerms(ITerm[] terms, int[] variableIndexes)
			throws EvaluationException {
		assert variableIndexes.length == 0;
		assert terms.length == 1;

		double second = 0;

		if (terms[0] instanceof ITime) {
			ITime time = (ITime) terms[0];
			second = time.getSecond();
		} else if (terms[0] instanceof IDateTime) {
			IDateTime dateTime = (IDateTime) terms[0];
			second = dateTime.getSecond();
		} else if (terms[0] instanceof IDuration) {
			/*
			 * We do not convert to DayTimeDuration here, since we can not
			 * retrieve the second part of a duration without losing its
			 * fractional part.
			 */

			IDuration duration = (IDuration) terms[0];

			double seconds = duration.getDecimalSecond() + duration.getMinute()
					* 60 + duration.getHour() * 3600 + duration.getDay()
					* 86400;
			double remainder = ((seconds % 86400.0) % 3600.0) % 60.0;

			second = remainder * duration.getValue().getSign();
		} else {
			return null;
		}

		return Factory.CONCRETE.createDecimal(second);
	}

	public int maxUnknownVariables() {
		return 0;
	}

}
