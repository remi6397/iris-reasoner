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

import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IDateTerm;
import org.deri.iris.api.terms.concrete.IDateTime;
import org.deri.iris.api.terms.concrete.ITime;
import org.deri.iris.builtins.FunctionalBuiltin;
import org.deri.iris.factory.Factory;

/**
 * Represents the RIF built-in functions func:timezone-from-dateTime and
 * func:timezone-from-date.
 */
public class TimezonePartBuiltin extends FunctionalBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TIMEZONE_PART", 2);

	/**
	 * Constructor. Two terms must be passed to the constructor, otherwise an
	 * exception will be thrown.
	 * 
	 * @param terms The terms.
	 * @throws IllegalArgumentException If one of the terms is {@code null}.
	 * @throws IllegalArgumentException If the number of terms submitted is not
	 *             2.
	 * @throws IllegalArgumentException If terms is <code>null</code>.
	 */
	public TimezonePartBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
		GregorianCalendar calendar = null;
		TimeZone timeZone = null;

		if (terms[0] instanceof ITime) {
			ITime time = (ITime) terms[0];
			calendar = time.getValue().toGregorianCalendar();
			timeZone = time.getTimeZone();
		} else if (terms[0] instanceof IDateTerm) {
			IDateTerm date = (IDateTerm) terms[0];
			calendar = date.getValue().toGregorianCalendar();
			timeZone = date.getTimeZone();
		} else if (terms[0] instanceof IDateTime) {
			IDateTime dateTime = (IDateTime) terms[0];
			calendar = dateTime.getValue().toGregorianCalendar();
			timeZone = dateTime.getTimeZone();
		} else {
			return null;
		}

		long time = calendar.getTimeInMillis();
		long offset = timeZone.getOffset(time);

		// Other than the specification suggests, we return a Duration instance
		// here, instead of DayTimeDuration.
		return Factory.CONCRETE.createDuration(offset);
	}

}
