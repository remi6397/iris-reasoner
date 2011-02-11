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

import org.deri.iris.api.terms.IConcreteTerm;

/**
 * <p>
 * An interface for representing the gYear datatype 
 * (gYear represents a gregorian calendar year). 
 * </p>
 * <p>
 * Remark: IRIS supports datatypes according to the standard 
 * specification for primitive XML Schema datatypes.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 22.01.2007 16:20:59
 */
public interface IGYear extends IConcreteTerm
{
	/**
	 * Return the wrapped type.
	 */
	public Integer getValue();

	/**
	 * Returns the year (a gregorian calendar year).
	 * 
	 * @return the year.
	 */
	public abstract int getYear();
}
