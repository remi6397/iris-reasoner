/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
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
package org.deri.iris.api.terms.concrete;

import java.util.Calendar;
import java.util.TimeZone;

import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * This is a time representation.
 * </p>
 * <p>
 * <code>ATTENTION: internally a Calendar is
 * used, so month and hour are zero-based.</code>
 * </p>
 * <p>
 * <code>ATTENTION: set the correct timezone</code>
 * </p>
 * <p>
 * $Id: ITime.java,v 1.1 2007-04-05 09:17:19 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.1 $
 */
public interface ITime extends ITerm<ITime, Calendar> {
	/**
	 * Returns a copy of this Calendar object.
	 * 
	 * @return the Calendar object
	 */
	public abstract Calendar getTime();

	/**
	 * Returns the hour of the day.
	 * 
	 * @return the hours (zero-based)
	 */
	public abstract int getHour();

	/**
	 * Returns the minute of the hour
	 * 
	 * @return the minutes
	 */
	public abstract int getMinute();

	/**
	 * Returns the seconds of the minute.
	 * 
	 * @return the seconds
	 */
	public abstract int getSecond();

	/**
	 * Returns the Timezone
	 * 
	 * @return the timezone
	 */
	public abstract TimeZone getTimeZone();
}
