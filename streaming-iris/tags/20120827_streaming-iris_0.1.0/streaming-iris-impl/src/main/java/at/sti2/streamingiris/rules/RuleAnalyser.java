/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
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
package at.sti2.streamingiris.rules;

import java.util.HashMap;
import java.util.Map;


import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IAtom;
import at.sti2.streamingiris.api.basics.ILiteral;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.builtins.IBuiltinAtom;
import at.sti2.streamingiris.api.terms.IConstructedTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.builtins.EqualBuiltin;
import at.sti2.streamingiris.builtins.NotEqualBuiltin;
import at.sti2.streamingiris.utils.TermMatchingAndSubstitution;

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
