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

import java.util.HashMap;
import java.util.Map;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltinAtom;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.builtins.EqualBuiltin;
import org.deri.iris.builtins.NotEqualBuiltin;
import org.deri.iris.utils.TermMatchingAndSubstitution;

/**
 * <p>
 * Methods to analyse rules.
 * </p>
 *
 * @author Richard Pöttler (richard dot poettler at sti2 dot at)
 */
public class RuleAnalyser {

	/**
	 * Checks, whether the rule got some head atoms in the body.
	 * @param rule the rule to check
	 * @return <code>true</code> if the rule body contains head atoms,
	 * otherwise <code>false</code>
	 * @throws IllegalArgumentException if the rule is <code>null</code>
	 */
	public static boolean hasHeadLiteralInBody(final IRule rule) {
		if (rule == null) {
			throw new IllegalArgumentException("The rule must not be null");
		}
		if (rule.getHead().size() > 1) {
			throw new IllegalArgumentException("The rule to check must contain only one head");
		}

		final RuleManipulator manipulator = new RuleManipulator();
		final IRule modRule = manipulator.replaceVariablesWithVariables(rule);

		return modRule.getBody().contains(modRule.getHead().get(0));
	}

	/**
	 * <p>
	 * Checks whether it is possible to assign any values to variables by static analysis of the rule.
	 * </p>
	 * <p>
	 * <b>The implementation of this method is not finished. At the moment
	 * it is not able to handle constructed terms.</b>
	 * </p>
	 * @param rule the rule to check
	 * @return <code>true</code> if a variable assignment was found, which
	 * doesn't make sense.
	 * @throws EvaluationException if something went wrong while evaluating
	 * one of the equality built-ins.
	 */
	public static boolean hasSatisfiableVariableAssignment(final IRule rule) throws EvaluationException {
		if (rule == null) {
			throw new IllegalArgumentException("The rule must not be null");
		}

		RuleManipulator manipulator = new RuleManipulator();
		IRule modRule = manipulator.replaceVariablesWithConstants(rule, false);
		
		for (ILiteral literal : modRule.getBody()) {
			IAtom atom = literal.getAtom();
			boolean positive = literal.isPositive();
			
			if (atom instanceof IBuiltinAtom) {
				IBuiltinAtom builtinAtom = (IBuiltinAtom) atom;
				ITuple tuple = builtinAtom.getTuple();
				
				if (tuple.isGround() && (builtinAtom.evaluate(tuple) == null)) {
					return false;
				}
				if( builtinAtom instanceof EqualBuiltin) {
					if( positive && ! unifies( tuple ) )
						return false;
					if( ! positive && unifies( tuple ) )
						return false;
				}
				else if( builtinAtom instanceof NotEqualBuiltin) {
					if( positive && unifies( tuple ) )
						return false;
					if( ! positive && ! unifies( tuple ) )
						return false;
				}
				else if( containsConstructedTerms( tuple ) )
					return false;
			}
		}
		return true;
	}
	
	private static boolean unifies( ITuple tuple )
	{
		assert tuple != null;
		assert tuple.size() == 2;
		Map<IVariable, ITerm> variableMap = new HashMap<IVariable, ITerm>();

		return TermMatchingAndSubstitution.unify( tuple.get( 0 ), tuple.get( 1 ), variableMap );
	}
	
	public static boolean containsConstructedTerms( ITuple tuple )
	{
		for( ITerm term : tuple )
			if( term instanceof IConstructedTerm )
				return true;
		return false;
	}

	/**
	 * Checks, whether a rule is productive.
	 * @param rule the rule to check
	 * @return <code>true</code> if the rule is productive, otherwise
	 * <code>false</code>
	 * @throws IllegalArgumentException if the rule is <code>null</code>
	 */
	public static boolean isProductive(final IRule rule) {
		if (rule == null) {
			throw new IllegalArgumentException("The rule must not be null");
		}

		return !hasHeadLiteralInBody(rule);
	}
}
