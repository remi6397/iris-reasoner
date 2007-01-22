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

import org.deri.iris.api.terms.ITerm;

/**
 * 
 * This is a interface to represent durations from seconds up to years.
 * <p>
 * Remark: IRIS supports datatypes according to the standard 
 * specification for primitive XML Schema datatypes.
 * </p>
 * <pre>
 * Created on 11.04.2006
 * Committed by $Author: darko $
 * $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/IDuration.java,v $,
 * </pre>
 *
 * @author Richard Pöttler
 *
 * @version $Revision: 1.4 $ $Date: 2007-01-22 16:09:59 $
 */
public interface IDuration extends ITerm<IDuration, Calendar> {
	/**
	 * Returns the corresponding Calendar object.
	 * 
	 * @return the Calendar object
	 */
	public abstract Calendar getDateTime();

	/**
	 * Returns the years.
	 * 
	 * @return the years
	 */
	public abstract int getYear();

	/**
	 * Returns the months
	 * 
	 * @return the months
	 */
	public abstract int getMonth();

	/**
	 * Returns the days
	 * 
	 * @return the days
	 */
	public abstract int getDay();

	/**
	 * Returns the hours
	 * 
	 * @return the hours
	 */
	public abstract int getHour();

	/**
	 * returns the minutes
	 * 
	 * @return the minutes
	 */
	public abstract int getMinute();

	/**
	 * Returns the seconds
	 * 
	 * @return the seconds
	 */
	public abstract int getSecond();
}
