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
package org.deri.iris.api.terms;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * An interface for representing a constructed term (function symbol). 
 * A constructed term is a term built from function-s and subter-s.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   14.11.2005 11:34:59
 */
public interface IConstructedTerm extends ITerm{

	public List<ITerm> getValue();
	
	/**
	 * Get the name of the constructed term (function symbol).
	 * 
	 * @return	The name of the constructed term.
	 */
	public String getFunctionSymbol();
	
	/**
	 * Returns a list of all terms from this constructed term (function symbol).
	 * 
	 * @return	List of all terms from this constructed term.
	 */
	public List<ITerm> getParameters();
	
	/**
	 * Returns all distinct variables from this tuple.
	 * 
	 * @return All distinct variables from this tuple.
	 */
	public Set<IVariable> getVariables();
}
