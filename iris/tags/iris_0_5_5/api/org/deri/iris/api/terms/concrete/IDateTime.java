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

import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.deri.iris.api.terms.ITerm;

/**
 * This is a representation of the builtin datatype IDateTime.
 * <p>
 * Remark: IRIS supports datatypes according to the standard 
 * specification for primitive XML Schema datatypes.
 * </p>
 * <br />
 * <code>ATTENTION: internally a Calendar is
 * used, so setting month and hour is zero-based.</code>
 * <br />
 * <code>ATTENTION: set the correct timezone</code>
 * 
 * <pre>
 *       Created on 06.04.2006
 *       Committed by $Author: bazbishop237 $
 *       $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/IDateTime.java,v $,
 * </pre>
 * 
 * @author Richard Pöttler
 * 
 * @version $Revision: 1.7 $ $Date: 2007-10-09 20:21:21 $
 */
public interface IDateTime extends ITerm
{
	/**
	 * Return the wrapped type.
	 */
	public XMLGregorianCalendar getValue();

	/**
	 * Returns the year.
	 * 
	 * @return the year
	 */
	public abstract int getYear();

	/**
	 * Returns the month of the year.
	 * 
	 * @return the month (zero-based)
	 */
	public abstract int getMonth();

	/**
	 * Returns the day of the month.
	 * 
	 * @return the day
	 */
	public abstract int getDay();

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
	 * Returns the milliseconds of the second.
	 * @return the milliseconds
	 */
	public abstract int getMillisecond();

	/**
	 * Returns the Timezone
	 * 
	 * @return the timezone
	 */
	public abstract TimeZone getTimeZone();

}
