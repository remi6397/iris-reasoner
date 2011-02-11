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
package org.deri.iris.api.factory;

import java.util.Collection;

import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * An interface that can be used to create a term as a logical entity. 
 * Different types of terms may be constructed.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck 
 * @date 20.02.2006 16:05:29
 */
public interface ITermFactory {

	public IConstructedTerm createConstruct(final String name,
			Collection<ITerm> terms);
	public IConstructedTerm createConstruct(final String name, ITerm... terms);

	public IStringTerm createString(String arg);

	public IVariable createVariable(String name);
}
