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
package org.deri.iris.evaluation.topdown;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltinAtom;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.rules.RuleManipulator;
import org.deri.iris.utils.TermMatchingAndSubstitution;

/**
 * Collection of helper functions for top-down evaluation strategies
 * 
 * @author gigi
 *
 */
public class TopDownHelper {

	private static RuleManipulator rm = new RuleManipulator();
	
	/**
	 * Replaces a rule head in a Query with the rule body.
	 * Example:
	 *      Query:  ?- a(?X), b(?X), c(?X).
	 *      Rule:             b(?X) :- r(?X), s(?X).
	 *      
	 *      Return: ?- a(?X), r(?X), s(?X), c(?X).
	 *      
	 * Note that the rule variables and the query variables MUST NOT
	 * intersect. If they do an <code>EvaluationException</code> is thrown.
	 * To avoid intersection, do an occur check first.
	 * 
	 * @param query a query element that contains the rule head
	 * @param rule a rule element
	 * @param selectedLiteral the selected literal
	 * 
	 * @return A new query element where the rule head is replaced with the rule body, if unifyable with the selected literal.
	 * 
	 * @throws EvaluationException
	 */
	public static IQuery substituteRuleHeadWithBody(IQuery query, ILiteral selectedLiteral, IRule rule) throws EvaluationException {
		LinkedList<ILiteral> newLiterals = new LinkedList<ILiteral>();
		
		assert TopDownHelper.match( selectedLiteral, rule.getHead().get(0) ) == true : "Selected literal MUST match rule head";
		
		for (IVariable v : getVariables(query)) {
			if (getVariables(rule).contains(v)) {
				throw new EvaluationException("Intersection of variables in rule and query. Run occur check first.");
			}
		}
		
		for (ILiteral queryLiteral : query.getLiterals()) {
			// Replace selected literal with rule body
			if ( selectedLiteral.equals(queryLiteral) ) {
				for (ILiteral ruleBodyLiteral : rule.getBody() ) {
					newLiterals.add( ruleBodyLiteral );
				}
			} else {
				newLiterals.add( queryLiteral );
			}
		}
		
		return Factory.BASIC.createQuery(newLiterals);
	}

	/**
	 * Replaces variables in a query.
	 * Example:
	 *      Query:  ?- a( ?X ), b( ?Y ), c( ?Z ).
	 *      variableMap: { X => 3, Y => 9 }
	 *      
	 *      Return: ?- a( 3 ), b( 9 ), c( ?X ).
	 *
	 *      
	 * @param query a query element
	 * @param variableMap a map of variables and terms
	 * 
	 * @return A new query element where the matching variables are replaced 
	 *         with the terms from the <code>variableMap</code>
	 */
	public static IQuery substituteVariablesInToQuery(IQuery query, Map<IVariable, ITerm> variableMap) {
		LinkedList<ILiteral> substitutedLiterals = new LinkedList<ILiteral>();
		LinkedList<ILiteral> literals = new LinkedList<ILiteral>();
		literals.addAll(query.getLiterals());		
		
		for (ILiteral queryLiteral : literals) {
			ILiteral substitutedLiteral = TopDownHelper.substituteVariablesInToLiteral( queryLiteral, variableMap );
			substitutedLiterals.add( substitutedLiteral );
		}

		return Factory.BASIC.createQuery( substitutedLiterals );
	}

	/**
	 * Replaces Variables in a Literal, and gives back the substituted Literal
	 * 
	 * @param literal a Literal element
	 * @param variableMap a map of variables and terms
	 * 
	 * @return A new Literal element where the matching variables are replaced 
	 *         with the terms from the <code>variableMap</code>
	 */
	static ILiteral substituteVariablesInToLiteral(ILiteral literal, Map<IVariable, ITerm> variableMap) {
		ITuple tuple = literal.getAtom().getTuple();
		ITuple substitutedTuple = tuple;
		for (IVariable variable : tuple.getVariables()) {
			if (variableMap.containsKey(variable)) {
				substitutedTuple = TermMatchingAndSubstitution.substituteVariablesInToTuple(tuple , variableMap);
			}
		}
		for (ITerm term : tuple) {
			if (term instanceof IVariable) {
				// Variable
				IVariable variable = (IVariable)term;
				if (variableMap.containsKey(variable)) {
					substitutedTuple = TermMatchingAndSubstitution.substituteVariablesInToTuple(tuple , variableMap);
				}	
			} else
			
			if (term instanceof IConstructedTerm) {
				// Constructed Term
				IConstructedTerm constructedTerm = (IConstructedTerm)term;
				LinkedList<IVariable> variables = new LinkedList<IVariable>(constructedTerm.getVariables()); 
				
				// For all variables that are in the constructed term, do the substitution
				for (IVariable variable : variables) {
					if (variableMap.containsKey(variable)) {
						substitutedTuple = TermMatchingAndSubstitution.substituteVariablesInToTuple(tuple , variableMap);
					}
				}
				
			}
		}
		
		ILiteral substitutedLiteral = literal;
		if (literal.getAtom() instanceof IBuiltinAtom) {
			// Builtin
			IBuiltinAtom builtinAtom = (IBuiltinAtom) literal.getAtom();				
	
			// Replace the original BuiltIn tuple with the substituted one
			// We need to do this via the RuleManipulator, because there 
			// is no (easy) way to generate a BuiltinAtom - so we modify our original
			for (int i = 0; i < substitutedTuple.size(); i++) {
				ITerm remove = builtinAtom.getTuple().get(i);
				ITerm replaceWith = substitutedTuple.get(i);
				builtinAtom = (IBuiltinAtom) rm.replace(builtinAtom, remove, replaceWith);
			}
			substitutedLiteral = Factory.BASIC.createLiteral( literal.isPositive(), builtinAtom );
		} else {
			// No builtin
			substitutedLiteral = Factory.BASIC.createLiteral( literal.isPositive(), literal.getAtom().getPredicate(), substitutedTuple );
		}
		return substitutedLiteral;
	}

	/**
	 * Checks if two predicates match
	 * @param pred1 a predicate element
	 * @param pred2 a second predicate element
	 * 
	 * @return <code>true</code> if the predicates have the same name and arity, <code>false</code> otherwise.
	 */
	static boolean match(IPredicate pred1, IPredicate pred2) {
		if ( pred1.getArity() == pred2.getArity() 
			 && pred1.equals( pred2 ) )
			return true;
		
		return false;
	}

	/**
	 * Checks if two literals match
	 * @param lit1 a literal element
	 * @param lit2 a second literal element
	 * 
	 * @return <code>true</code> if the predicates of the literals have the same name and arity, <code>false</code> otherwise.
	 */
	public static boolean match(ILiteral lit1, ILiteral lit2) {
		return match( lit1.getAtom().getPredicate() , lit2.getAtom().getPredicate() );
	}

	/**
	 * Wrapper. See <code>match(IPredicate pred1, IPredicate pred2)</code>.
	 * @param queryLiteral
	 * @param factPredicate
	 * 
	 * @return
	 */
	public static boolean match(ILiteral queryLiteral, IPredicate factPredicate) {
		return match( queryLiteral.getAtom().getPredicate() , factPredicate );
	}

	/**
	 * Get a list of variables, with no duplicates 
	 * @param query the query
	 * 
	 * @return A unique list of variables
	 */
	public static List<IVariable> getVariables(IQuery query) {
		Map<Integer, IVariable> varMap = new HashMap<Integer, IVariable>();
		List<IVariable> variableList = query.getVariables(); // This list can contain variables twice
		List<IVariable> uniqueList = new LinkedList<IVariable>();
		
		for (IVariable var : variableList) {
			Integer position = variableList.indexOf(var); // indexOf takes the first occurrence
			if (varMap.put(position, var) == null) // null == added
				uniqueList.add(var);
		}
		
		return uniqueList;
	}

	/**
	 * Creates a variable map that will replace variable ?X with ?X1 if
	 * the variable occurs in both tuples. Used by SLDNF evaluation.
	 *  
	 * @param rule a rule
	 * @param query a query
	 * 
	 * @return a variable map that maps the old variable names to the new ones
	 */
	public static Map<IVariable, ITerm> getVariableMapForOccurCheck(IRule rule, IQuery query) {
		Map<IVariable, ITerm> variableMapForOccurCheck = new HashMap<IVariable, ITerm>();
		
		Set<IVariable> ruleVariables = getVariables(rule);
		List<IVariable> queryVariables = getVariables(query);
		
		if (queryVariables != null) {
			for ( IVariable var : ruleVariables ) {
				int i = 0;
				IVariable varRename = var;
				while (queryVariables.contains( varRename )) {
					varRename = Factory.TERM.createVariable(var.getValue().toString() + ++i);
				}
				variableMapForOccurCheck.put(var, varRename);
			}
		}
		
		return variableMapForOccurCheck;
	}

	/**
	 * Get all distinct variables in a rule. No particular order is guaranteed.
	 *  
	 * @param rule the rule
	 * 
	 * @return a set of distinct variables
	 */
	private static Set<IVariable> getVariables(IRule rule) {
		Set<IVariable> ruleVariables = rule.getHead().get(0).getAtom().getTuple().getVariables();
		for (ILiteral ruleBodyLiteral : rule.getBody()) {
			ruleVariables.addAll( ruleBodyLiteral.getAtom().getTuple().getVariables() );
		}
		
		return ruleVariables;
	}

	/**
	 * Given a query and a tuple, create
	 * a variable map with the corresponding variable
	 * mappings for each variable in the query.
	 * 
	 * @param query the query
	 * @param branchTuple the tuple
	 * 
	 * @return a new variable map containing mappings for all 
	 * unique variables in <code>query</code>   
	 */
	public static Map<IVariable, ITerm> createVariableMapFromTupleAndQuery(IQuery query, ITuple branchTuple) {
		
		Map<IVariable, ITerm> variableMap = new HashMap<IVariable, ITerm>();
		
		int i = 0;
		for (IVariable var : getVariables(query)) {
			variableMap.put(var, branchTuple.get(i++));
		}
		
		return variableMap;
	}

	/**
	 * Given a query and a variable map, create  
	 * a tuple with the corresponding terms for each variable
	 * in the query.
	 * 
	 * @param query the query
	 * @param variableMap a map containing the variable mappings
	 * 
	 * @return a new tuple
	 */
	public static ITuple createTupleFromQueryAndVariableMap(IQuery query, Map<IVariable, ITerm> variableMap) {
		
		assert query.getVariables().size() == variableMap.size() : "All variables must have a mapping";
		
		List<ITerm> terms = new LinkedList<ITerm>();
		for (IVariable var : query.getVariables() ) {
			// For every Variable of the original query, get the mappings
			ITerm term = variableMap.get(var);
			terms.add(term);
		}
		
		ITuple tuple = Factory.BASIC.createTuple(terms);
		return tuple;
	}

	/**
	 * If <code>variableMap</code> contains variable mappings of variables
	 * that are contained in <code>query</code>, those mappings will be
	 * resolved and a proper <code>tuple</code> will be created and returned. 
	 * 
	 * e.g.
	 * 	query 		=	?- q(?X)
	 *  variableMap =	?X = 1
	 *  return		=	(1)
	 * 
	 * @param query a <code>IQuery</code> which can contain variables
	 * @param variableMap map that stores variable mappings
	 * 
	 * @return a tuple containing the resolved variables of the query
	 */
	public static ITuple resolveTuple(IQuery query, Map<IVariable, ITerm> variableMap) {
		// Resolve evaluated variables
		List<ITerm> resolvedVarList = new LinkedList<ITerm>();
		for (IVariable var : query.getVariables()) {
			resolvedVarList.add(variableMap.get(var));
		}
		
		// Create tuple from resolved variables
		ITuple tuple = Factory.BASIC.createTuple(resolvedVarList);
		return tuple;
	}

	/**
	 * Replaces all variables in a rule with the mapped ones. Always modifies the head too.
	 * 
	 * @param rule a rule
	 * @param variableMap a variable map
	 * 
	 * @return rule with replaced variables
	 */
	public static IRule replaceVariablesInRule(IRule rule, Map<IVariable, ITerm> variableMap) {
		IRule replacedRule = rule;
		for (Entry<IVariable, ITerm> entryPair : variableMap.entrySet()) {
			replacedRule = rm .replace(replacedRule, true, entryPair.getKey(), entryPair.getValue());
		}
		return replacedRule;
	}

}
