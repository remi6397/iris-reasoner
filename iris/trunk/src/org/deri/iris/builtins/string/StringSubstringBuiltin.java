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
import org.deri.iris.api.terms.concrete.IIntegerTerm;
import org.deri.iris.builtins.AbstractBuiltin;
import org.deri.iris.factory.Factory;

/**
 * Represents a string substring operation.
 * 
 * @author gigi
 * 
 */
public class StringSubstringBuiltin extends AbstractBuiltin {

	private static final IPredicate PREDICATE1 = BASIC.createPredicate(
			"STRING_SUBSTRING2", 2);
	private static final IPredicate PREDICATE2 = BASIC.createPredicate(
			"STRING_SUBSTRING3", 3);

	/**
	 * Constructor.
	 * 
	 * @param string
	 *            The term representing the string.
	 * @param beginIndex
	 *            The term representing the begin index.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringSubstringBuiltin(final ITerm string, final ITerm beginIndex) {
		super(PREDICATE1, new ITerm[] { string, beginIndex });
	}

	/**
	 * Constructor.
	 * 
	 * @param string
	 *            The term representing the string.
	 * @param beginIndex
	 *            The term representing the begin index.
	 * @param endIndex
	 *            The term representing the end index.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 */
	public StringSubstringBuiltin(final ITerm string, final ITerm beginIndex,
			final ITerm endIndex) {
		super(PREDICATE2, new ITerm[] {
				string, beginIndex, endIndex });
	}

	protected ITerm evaluateTerms(ITerm[] terms, int[] variableIndexes)
			throws EvaluationException {
		assert variableIndexes.length == 0;

		if (terms.length == 2) {
			if (terms[0] instanceof IStringTerm
					&& terms[1] instanceof IIntegerTerm) {
				IStringTerm string = (IStringTerm) terms[0];
				IIntegerTerm beginIndex = (IIntegerTerm) terms[1];

				String substring = string.getValue().substring(
						beginIndex.getValue());

				return Factory.TERM.createString(substring);
			}
		} else if (terms.length == 3) {
			if (terms[0] instanceof IStringTerm
					&& terms[1] instanceof IIntegerTerm
					&& terms[2] instanceof IIntegerTerm) {
				IStringTerm string = (IStringTerm) terms[0];
				IIntegerTerm beginIndex = (IIntegerTerm) terms[1];
				IIntegerTerm endIndex = (IIntegerTerm) terms[2];

				String substring = string.getValue().substring(
						beginIndex.getValue(), endIndex.getValue());

				return Factory.TERM.createString(substring);
			}
		}

		return null;
	}

	public int maxUnknownVariables() {
		return 0;
	}
}
