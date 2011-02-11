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
package org.deri.iris.api.terms.concrete;

import javax.xml.datatype.Duration;

import org.deri.iris.api.terms.IConcreteTerm;

/**
 * 
 * <p>
 * This is a interface to represent durations from seconds up to years.
 * </p>
 * <p>
 * Remark: IRIS supports datatypes according to the standard specification for
 * primitive XML Schema datatypes.
 * </p>
 */
public interface IDuration extends IConcreteTerm {
	/**
	 * Return the wrapped type.
	 */
	Duration getValue();

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
	 * Returns the years.
	 * 
	 * @return the years
	 */
	int getYear();

	/**
	 * Returns the months
	 * 
	 * @return the months
	 */
	int getMonth();

	/**
	 * Returns the days
	 * 
	 * @return the days
	 */
	int getDay();

	/**
	 * Returns the hours
	 * 
	 * @return the hours
	 */
	int getHour();

	/**
	 * returns the minutes
	 * 
	 * @return the minutes
	 */
	int getMinute();

	/**
	 * Returns the seconds
	 * 
	 * @return the seconds
	 */
	int getSecond();

	/**
	 * Returns the milliseconds.
	 * 
	 * @return the milliseconds
	 */
	int getMillisecond();

	/**
	 * Return the complete floating point representation of the seconds
	 * components.
	 * 
	 * @return Decimal seconds
	 */
	double getDecimalSecond();
}
