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
package org.deri.iris.api.terms.concrete;

import javax.xml.datatype.Duration;

import org.deri.iris.api.terms.IConstantTerm;

/*
 * W3C specification: http://www.w3.org/TR/xpath-functions/#dt-dayTimeDuration
 */

/**
 * <p>
 * An interface for representing the xs:dayTimeDuration data-type. xs:dayTimeDuration is derived from xs:duration by restricting its lexical
 * representation to contain only the days, hours, minutes and seconds
 * components.
 * </p>
 * <p>
 * Remark: IRIS supports data types according to the standard specification for
 * primitive XML Schema data types.
 * </p>
 */
public interface IDayTimeDuration extends IConstantTerm {

	/**
	 * Returns the wrapped type.
	 */
	public Duration getValue();

	/**
	 * Returns <code>true</code> if this is a positive duration,
	 * <code>false</code> otherwise. Also returns <code>true</code> if this is a
	 * duration of length 0.
	 * 
	 * @return <code>true</code> if this is a positive duration,
	 *         <code>false</code> otherwise.
	 */
	boolean isPositive();

	/**
	 * Returns the days.
	 * 
	 * @return The days.
	 */
	public int getDay();

	/**
	 * Returns the hours.
	 * 
	 * @return The hours.
	 */
	public int getHour();

	/**
	 * Returns the minutes.
	 * 
	 * @return The minutes.
	 */
	public int getMinute();

	/**
	 * Returns the seconds.
	 * 
	 * @return The seconds.
	 */
	public int getSecond();

	/**
	 * Returns the milliseconds.
	 * 
	 * @return The milliseconds.
	 */
	public int getMillisecond();

	/**
	 * Return the complete floating point representation of the seconds
	 * components.
	 * 
	 * @return The decimal seconds
	 */
	public double getDecimalSecond();

	/**
	 * Returns a canonical representation of dayMonthDuration as defined in
	 * http://www.w3.org/TR/xpath-functions/#canonical-yearMonthDuration.
	 * 
	 * @return A canonical representation of this yearMonthDuration instance.
	 */
	public IDayTimeDuration toCanonical();

}
