/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2009 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.builtins.string;

import static org.deri.iris.factory.Factory.BASIC;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.AbstractBuiltin;
import org.deri.iris.factory.Factory;

/**
 * Represents a string join operation as defined in
 * http://www.w3.org/2005/rules/
 * wiki/DTB#func:string-join_.28adapted_from_fn:string-join.29.
 */
public class StringJoinBuiltin extends AbstractBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"STRING_JOIN", 3);

	/**
	 * Constructor. At least three terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param terms
	 *            The terms
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 * @throws IllegalArgumentException
	 *             if the number of terms submitted is not 1
	 * @throws IllegalArgumentException
	 *             if t is <code>null</code>
	 */
	public StringJoinBuiltin(final ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm evaluateTerms(ITerm[] terms, int[] variableIndexes)
			throws EvaluationException {
		assert variableIndexes.length == 0;
		assert terms.length == 3;

		StringBuffer buffer = new StringBuffer();
		String separator = null;

		if (terms[terms.length - 1] instanceof IStringTerm) {
			separator = ((IStringTerm) terms[terms.length - 1]).getValue();
		} else {
			return null;
		}

		for (int i = 0; i < terms.length - 1; i++) {
			if (terms[i] instanceof IStringTerm) {
				String string = ((IStringTerm) terms[i]).getValue();

				buffer.append(string);
				buffer.append(separator);
			} else {
				return null;
			}
		}

		String result = buffer.toString();
		return Factory.TERM.createString(result);
	}

	public int maxUnknownVariables() {
		return 0;
	}

}
