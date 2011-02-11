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
package org.deri.iris.api.basics;

import java.util.List;

import org.deri.iris.api.terms.IVariable;

/**
 * A query is a rule without the head.
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   19.11.2005 15:35:44
 */

public interface IQuery {
    
	public List<ILiteral> getLiterals();
	
	public List<IVariable> getVariables();
}
