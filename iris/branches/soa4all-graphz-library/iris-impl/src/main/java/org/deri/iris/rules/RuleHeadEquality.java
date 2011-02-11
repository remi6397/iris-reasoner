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
package org.deri.iris.rules;

import java.util.List;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.builtins.EqualBuiltin;

/**
 * An utility class for rule head equality.
 * 
 * @author Adrian Marte
 */
public class RuleHeadEquality {

	/**
	 * Checks if rule head equality appears in the head of the specified rule,
	 * e.g. ?X = ?Y :- p(?X, ?Y), q(?Y, ?X).
	 * 
	 * @param rule The rule to check for occurrence of rule head equality.
	 * @return <code>true</code> if the given rule has rule head equality,
	 *         <code>false</code> otherwise.
	 */
	public static boolean hasRuleHeadEquality(IRule rule) {
		List<ILiteral> head = rule.getHead();

		for (ILiteral literal : head) {
			if (hasRuleHeadEquality(literal)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if the specified literal represents rule head equality, e.g. ?X =
	 * ?Y.
	 * 
	 * @param rule The literal to check for occurrence of the rule head equality
	 *            predicate.
	 * @return <code>true</code> if the given literal represents rule head equality
	 *         predicate, <code>false</code> otherwise.
	 */
	private static boolean hasRuleHeadEquality(ILiteral literal) {
		IAtom atom = literal.getAtom();

		return atom instanceof EqualBuiltin;
	}

}
