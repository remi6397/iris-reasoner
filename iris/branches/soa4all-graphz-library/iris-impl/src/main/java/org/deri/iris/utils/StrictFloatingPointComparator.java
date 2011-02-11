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
 * A floating point comparator that uses the default java behaviour.
 * This class will make floating point comparison faster,
 * but will not allow for any round-off errors at all.
 */
public class StrictFloatingPointComparator implements IFloatingPointComparator
{
	public int compare( double a, double b )
	{
		return Double.compare( a, b );
	}

	public boolean equals( double a, double b )
	{
		return a == b;
	}

	public boolean greater( double a, double b )
	{
		return a > b;
	}

	public boolean greaterOrEquals( double a, double b )
	{
		return a >= b;
	}

	public boolean isIntValue( double value )
	{
		return value == Math.rint( value );
	}

	public boolean less( double a, double b )
	{
		return a < b;
	}

	public boolean lessOrEquals( double a, double b )
	{
		return a <= b;
	}

	public boolean notEquals( double a, double b )
	{
		return a != b;
	}
}
