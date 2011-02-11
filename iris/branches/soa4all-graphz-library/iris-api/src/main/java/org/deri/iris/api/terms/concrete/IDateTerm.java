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

import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.deri.iris.api.terms.IConcreteTerm;

/**
 * <p>
 * An interface for representing the date datatype. 
 * </p>
 * <p>
 * Remark: IRIS supports datatypes according to the standard 
 * specification for primitive XML Schema datatypes.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public interface IDateTerm extends IConcreteTerm {
	
	/**
	 * Return the wrapped type.
	 */
	XMLGregorianCalendar getValue();

	/**
	 * Returns the month of the year.
	 * 
	 * @return the month.
	 */
	int getMonth();
	
	/**
	 * Returns the year.
	 * 
	 * @return the year.
	 */
	int getYear();
	
	
	/**
	 * Returns the day of the month.
	 * 
	 * @return the day
	 */
	int getDay();


	/**
	 * Returns the Timezone
	 * 
	 * @return the timezone
	 */
	TimeZone getTimeZone();
}
