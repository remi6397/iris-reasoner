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
package org.deri.iris.terms.concrete;

import java.net.URI;

import org.deri.iris.api.terms.concrete.IDateTimeStamp;

/**
 * <p>
 * A simple implementation of DateTimeStamp.
 * </p>
 * 
 * @author Adrian Marte
 */
public class DateTimeStamp extends DateTime implements IDateTimeStamp {

	/**
	 * Creates a new DateTimeStamp for the specified values.
	 * 
	 * @param year The year fragment.
	 * @param month The month fragment.
	 * @param day The Day of month fragment.
	 * @param hour The hour fragment.
	 * @param minute The minute fragment.
	 * @param second The second fragment.
	 * @param tzHour The timezone hour (relative to GMT) fragment.
	 * @param tzMinute The timezone minute (relative to GMT) fragment.
	 * @throws IllegalArgumentException If not both the tzHour and tzMinute are
	 *             positive or negative.
	 */
	public DateTimeStamp(int year, int month, int day, int hour, int minute,
			double second, int tzHour, int tzMinute) {
		super(year, month, day, hour, minute, second, tzHour, tzMinute);
	}

	@Override
	public URI getDatatypeIRI() {
		return URI.create(IDateTimeStamp.DATATYPE_URI);
	}

}
