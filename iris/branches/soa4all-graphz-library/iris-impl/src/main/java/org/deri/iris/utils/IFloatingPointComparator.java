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
package org.deri.iris.utils;

/**
 * A floating-point number comparator.
 * Classes that implement this interface can either perform strict comparison or
 * allow for round-off errors.
 */
public interface IFloatingPointComparator
{
	/**
	 * An error-safe comparison in the java style.
	 * 
	 * @param a A double value
	 * @param b A double value
	 * @return -1 if a is significantly less than b, +1 if a is significantly greater than b, 0 if a and b are close enough.
	 */
	int compare( double a, double b );

	/**
	 * Floating-point error safe comparison.
	 * 
	 * @param a A double value
	 * @param b A double value
	 * @return true If a is less than b.
	 */
	boolean less( double a, double b );

	/**
	 * Floating-point error safe comparison.
	 * 
	 * @param a A double value
	 * @param b A double value
	 * @return true If a is greater than b.
	 */
	boolean greater( double a, double b );

	/**
	 * Floating-point error safe comparison.
	 * 
	 * @param a A double value
	 * @param b A double value
	 * @return true If a is greater than or close enough to be equal to b.
	 */
	boolean greaterOrEquals( double a, double b );

	/**
	 * Floating-point error safe comparison.
	 * 
	 * @param a A double value
	 * @param b A double value
	 * @return true If a is less than or close enough to be equal to b.
	 */
	boolean lessOrEquals( double a, double b );

	/**
	 * Test two double values for equality.
	 * @param a A double value
	 * @param b A double value
	 * @return true If a and b are considered equal.
	 */
	boolean equals( double a, double b );

	/**
	 * Floating-point error safe comparison.
	 * 
	 * @param a
	 * @param b
	 * @return true If a and b are significantly different.
	 */
	boolean notEquals( double a, double b );

	/**
	 * Indicates whether a double value contains an integer or a number very,
	 * very close to an integer.
	 * @param value The value to test
	 * @return true If value holds an integer.
	 */
	boolean isIntValue( double value );
}
