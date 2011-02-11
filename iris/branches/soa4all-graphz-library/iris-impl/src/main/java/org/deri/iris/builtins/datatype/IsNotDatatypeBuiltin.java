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

import static org.deri.iris.factory.Factory.BASIC;

import java.net.URI;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IConcreteTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IIri;
import org.deri.iris.builtins.BooleanBuiltin;

/**
 * Represents a isLiteralOfType predicate as defined in
 * http://www.w3.org/2005/rules/wiki/DTB#pred:isLiteralNotOfType.
 */
public class IsNotDatatypeBuiltin extends BooleanBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"IS_NOT_DATATYPE", 2);

	/**
	 * Constructor. At least two terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param terms The terms
	 * @throws IllegalArgumentException if one of the terms is {@code null}
	 * @throws IllegalArgumentException if the number of terms submitted is not
	 *             1
	 * @throws IllegalArgumentException if t is <code>null</code>
	 */
	public IsNotDatatypeBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		if (terms[0] instanceof IConcreteTerm) {
			IConcreteTerm term = (IConcreteTerm) terms[0];

			if (terms[1] instanceof IIri) {
				URI iri = ((IIri) terms[1]).getURI();

				return !iri.equals(term.getDatatypeIRI());
			}
		}

		return false;
	}

}
