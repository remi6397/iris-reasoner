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
package org.deri.iris.terms;

import java.util.Arrays;
import java.util.Collection;

import org.deri.iris.api.factory.ITermFactory;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * @author richi
 *
 */
public class TermFactory implements ITermFactory {
	
	private static final ITermFactory FACTORY = new TermFactory();
	
	private TermFactory() {
		// this is a singelton
	}
	
	public IConstructedTerm createConstruct(String name, Collection<ITerm> terms) {
		return new ConstructedTerm(name, terms);
	}

	public IConstructedTerm createConstruct(String name, ITerm... terms) {
		return createConstruct(name, Arrays.asList(terms));
	}

	public IStringTerm createString(String arg) {
		return new StringTerm(arg);
	}

	public IVariable createVariable(String name) {
		return new Variable(name);
	}

	public static ITermFactory getInstance() {
		return FACTORY;
	}

}
