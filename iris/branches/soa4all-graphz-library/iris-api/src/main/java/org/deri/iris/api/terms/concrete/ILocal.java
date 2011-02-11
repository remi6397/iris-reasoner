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
 * Represents the rif:local data type. A rif:local value is a constant symbol
 * that is not visible outside of the RIF document in which it occurs. The RIF
 * document is represented by an {@link Object}.
 */
public interface ILocal extends IConcreteTerm {
	
	/**
	 * Returns the wrapped type. The first element of this array is the string value
	 * and the second is the context of the rif:local represented as an {@link Object}.
	 * 
	 * @return The wrapped type.
	 */
	public Object[] getValue();

	/**
	 * Returns the string value of the rif:local, e.g. "Gordon Freeman" or "123".
	 * 
	 * @return The string value.
	 */
	public String getString();

	/**
	 * Returns context in which the {@link ILocal} is visible to the outside. 
	 * 
	 * @return The context in which the {@link ILocal} is visible to the outside.
	 */
	public Object getContext();
	

}
