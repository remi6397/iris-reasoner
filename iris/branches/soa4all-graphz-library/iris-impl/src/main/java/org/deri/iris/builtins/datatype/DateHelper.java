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
package org.deri.iris.builtins.datatype;

import java.util.TimeZone;

/**
 * A helper class for handling with dates, time zones, etc.
 */
class DateHelper {

	/**
	 * Returns the hour part of a time zone.
	 * 
	 * @param timeZone The time zone.
	 * @return The hour part of the given time zone.
	 */
	static int getHourPart(TimeZone timeZone) {
		int offset = timeZone.getRawOffset();
		int tzHour = offset / 3600000;

		return tzHour;
	}

	/**
	 * Returns the minute part of a time zone.
	 * 
	 * @param timeZone The time zone.
	 * @return The minute part of the given time zone.
	 */
	static int getMinutePart(TimeZone timeZone) {
		int offset = timeZone.getRawOffset();
		int tzMinute = (Math.abs(offset) % 3600000) / 60000;

		if (offset < 0) {
			tzMinute *= -1;
		}

		return tzMinute;
	}

}
