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
package org.deri.iris.evaluation.topdown;

import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.builtins.IBuiltinAtom;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.builtins.EqualBuiltin;

/**
 * Safe Standard Literal Selector. 
 * Always selects the most-left positive or negative grounded literal.
 * 
 * @author gigi
 * 
 */
public class SafeStandardLiteralSelector implements ILiteralSelector {

	public ILiteral select(List<ILiteral> list) {
		if (list.isEmpty())
			return null;
		
		for (ILiteral lit : list) {
			Set<IVariable> variables = lit.getAtom().getTuple().getVariables();
			
			if (lit.isPositive()) {
				
				if (lit.getAtom() instanceof IBuiltinAtom) {
					IBuiltinAtom builtinAtom = (IBuiltinAtom)lit.getAtom();
					
					if (builtinAtom instanceof EqualBuiltin) {
						// select it
					} else if (builtinAtom.getTuple().getVariables().size() > builtinAtom.maxUnknownVariables()) {
						// try next literal
						continue;
					}
				}
				
				return lit; // positive literal
			
			} else if (variables.isEmpty()) {
				return lit; // negative grounded literal
			}
		}
		
		return null; // literal selection not possible
	}

}
