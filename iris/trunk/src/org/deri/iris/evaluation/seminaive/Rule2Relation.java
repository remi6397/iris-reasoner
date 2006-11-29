/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
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
package org.deri.iris.evaluation.seminaive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.evaluation.seminaive.model.IProjection;
import org.deri.iris.api.evaluation.seminaive.model.IRule;
import org.deri.iris.api.evaluation.seminaive.model.ISelection;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.basics.seminaive.NonEqualityTerm;
import org.deri.iris.builtins.EqualBuiltin;
import org.deri.iris.builtins.UnEqualBuiltin;
import org.deri.iris.evaluation.seminaive.model.ModelFactory;
import org.deri.iris.factory.Factory;
import org.deri.iris.operations.relations.JoinCondition;
import org.deri.iris.terms.StringTerm;

/**
 * Implementation of Algorithm 3.1: Computing the Relation for a Rule Body,
 * Using Relational Algebra Operations. Chapter 3. Logic as a Data Model.
 * 
 * @author Paco Garcia, University of Murcia
 * @author Darko Anicic, DERI Innsbruck
 * @date 01-sep-2006
 * 
 */
public class Rule2Relation {

	/** prefix for the variables */
	private static final String VAR_PREFIX = "?X_";

	/** counter for an arbitrarly chosen variable */
	private static int VAR_COUNTER = 0;

	/**
	 * Transform a set of rules into relational algebra operations
	 * 
	 * @param rule
	 *            Collection that contains the rules in the program
	 *            (PRECONDITION: the rules are ordered according to the
	 *            dependences within the program)
	 * @return An structure that contains the predicates head of the rules and
	 *         the algebra operations for these rules
	 */
	public Map<IPredicate, ITree> evalRule(
			final Collection<org.deri.iris.api.basics.IRule> rules) {

		Map<IPredicate, ITree> results = new Hashtable<IPredicate, ITree>();
		for (org.deri.iris.api.basics.IRule r : rules) {
			IPredicate p = r.getHeadLiteral(0).getPredicate();
			ITree head = (ITree) ModelFactory.FACTORY.createTree(p
					.getPredicateSymbol());
			head.addAllVariables(r.getHeadVariables());

			ITree body = evalRuleBody(r, head);

			if (body != null) {
				// Check whether there is a rule with the same head predicate;
				// if so, the bodies must be united
				if (results.keySet().contains(p)) {
					// UNION
					// The relations should be union-compatible:
					// (a) Same number of fields.
					// (b) Corresponding fields have the same type.
					ITree newBody = ModelFactory.FACTORY.createUnion();
					newBody.addComponent(results.get(p));
					newBody.addComponent(body);
					results.remove(p);
					results.put(p, newBody);
				} else {
					results.put(p, body);
				}
			}
		}
		return results;
	}

	/**
	 * Note: this method deals only with non-disjunctive queries
	 * 
	 * @param queries
	 * @return
	 */
	public Map<IPredicate, ITree> evalQueries(final Iterator<IQuery> qIt) {

		Map<IPredicate, ITree> results = new Hashtable<IPredicate, ITree>();
		while (qIt.hasNext()) {
			IQuery q = qIt.next();
			IPredicate p = q.getQueryLiteral(0).getPredicate();
			ITree head = (ITree) ModelFactory.FACTORY.createTree(p
					.getPredicateSymbol());
			head.addAllVariables(q.getQueryVariables());

			ITree body = evalRuleBody(Factory.BASIC.createBody(q
					.getQueryLiterals()), head);

			if (body != null) {
				// Check whether there is a rule with the same head predicate;
				// if so, the bodies must be united
				if (results.keySet().contains(p)) {
					// UNION
					// The relations should be union-compatible:
					// (a) Same number of fields.
					// (b) Corresponding fields have the same type.
					ITree newBody = ModelFactory.FACTORY.createUnion();
					newBody.addComponent(results.get(p));
					newBody.addComponent(body);
					results.remove(p);
					results.put(p, newBody);
				} else {
					results.put(p, body);
				}
			}
		}
		return results;
	}

	/**
	 * Returns the predicate that is equals to the one specified as parameter.
	 * Returns null if the keyset contains no predicate equals to the specified.
	 * 
	 * @param keySet
	 *            Set with the keys of a hashtable
	 * @param head
	 *            Predicate which is compared with the ones stored in the keyset
	 * @return true if the head is already stored in the keyset; false
	 *         otherwise.
	 */
	private ITree containsKey(Set<ITree> keySet, ITree head) {
		Iterator<ITree> keys = keySet.iterator();

		while (keys.hasNext()) {
			ITree r = keys.next();
			if (r.equals(head)) {
				return r;
			}
		}
		return null;
	}

	/**
	 * Algorithm 3.1
	 * 
	 * @param r
	 *            Rule Body
	 * @return A tree with the relational algebra operations for the input rule
	 */
	private ITree evalRuleBody(IBody b, ITree head) {
		Map<IVariable, List<ILiteral>> variables = new java.util.Hashtable<IVariable, List<ILiteral>>();
		/** contains builtIns of type: ?X = 'a' or ?X != 'a' */
		List<IBuiltInAtom> builtIns = new java.util.LinkedList<IBuiltInAtom>();
		/** contains builtIns of type: ?X = ?Y or ?X != ?Y */
		List<IBuiltInAtom> builtInsV = new java.util.LinkedList<IBuiltInAtom>();
		/**
		 * Algorithm 3.1: Computing the relation for a Rule Body, using
		 * relational Algebra Operation Chapter 3. Logic as a data model. INPUT:
		 * Body of rule r = S1,...,Sn with variables X1,...,Xm; Si = pi(Ai1,...,
		 * Aiki) where pi <--> Ri & Ai = variable|constant
		 */
		List<ILiteral> literals = b.getBodyLiterals();
		ITree temResult = null;
		List<ITree> treeList = new ArrayList<ITree>();

		for (ILiteral l : literals) {
			// A. Ordinary subgoal
			if (!(l.getAtom() instanceof IBuiltInAtom)) {
				temResult = evalLiteral(l, head, variables);
				// C. Natural join of all subgoals (E)
				if (temResult != null && (!temResult.equals(head))) {
					treeList.add(temResult);
				}
			}
		}
		for (ILiteral l : literals) {
			// B. Built-in subgoal
			// Note: Built-in subgoals need to be handled after
			// Ordinary subgoals
			if (l.getAtom() instanceof IBuiltInAtom) {
				temResult = evalBuiltIn((IBuiltInAtom) l.getAtom(), head,
						variables, builtIns, builtInsV);
				// C. Natural join of all subgoals (E)
				if (temResult != null && (!temResult.equals(head))) {
					treeList.add(temResult);
				}
			}
		}
		if (temResult == null)
			return null; // this rule has no sense

		ITree t0 = treeList.get(0);
		ITree t1 = null;
		ITree globalJoin = t0;
		ITree result = t0;

		for (int i = 1; i < treeList.size(); i++) {
			t1 = treeList.get(i);
			globalJoin = ModelFactory.FACTORY.createJoin(
					getJoinIndexes(t0, t1), JoinCondition.EQUALS);
			globalJoin.addComponent(t0);
			globalJoin.addComponent(t1);
			result = globalJoin;
		}
		// D. EVAL-RULE(r, R1,...,Rn) = SELECTION_F(E)
		// F conjunction of built-in subgoals appearing.
		// builtIns = getSelectionBuiltins(result.getVariables(), builtInsV);
		if ((!builtIns.isEmpty()) || (!builtInsV.isEmpty())) {
			builtIns.addAll(builtInsV);
			int[] selectionIndex = new int[result.getArity()];
			List<ITerm> selectionPattern = new ArrayList<ITerm>();
			for (int i = 0; i < result.getArity(); i++)
				selectionPattern.add(null);

			getSelectionIndexes(builtIns, selectionPattern, selectionIndex,
					result);
			ISelection s = ModelFactory.FACTORY.createSelection(Factory.BASIC
					.createTuple(selectionPattern), selectionIndex);
			s.addComponent(result);
			result = s;
		}
		if (!Arrays.equals(result.getVariables().toArray(), head.getVariables()
				.toArray())) {

			int[] globalProjectionIndex = getProjectionIndexes(result, head);

			IProjection globalProjection = ModelFactory.FACTORY
					.createProjection(globalProjectionIndex);
			globalProjection.addVariables(head.getVariables());
			globalProjection.addComponent(result);

			result = globalProjection;
		}
		return result;
	}

	/**
	 * Evaluate an ordinary (non built-in) subgoal
	 * 
	 * @param l
	 *            Literal containing the ordinary subgoal
	 * @param head
	 *            Head of the rule that is being analyzed
	 * @param variables
	 *            Set of variables found in this rule
	 * @return The algebraic expression for this literal
	 */
	private ITree evalLiteral(ILiteral l, ITree head,
			Map<IVariable, List<ILiteral>> variables) {
		ITree result;
		/*
		 * For each ordinary Si (e.g. Si = p(X, b) ), let Qi be the expression
		 * PROJECTION_Vi(SELECTION_Fi(Ri)) where Vi = for each variable X in Si
		 * exactly one component
		 */

		// 1. Ri
		org.deri.iris.api.evaluation.seminaive.model.IRule leaf = ModelFactory.FACTORY
				.createRule(l.isPositive(), l.getPredicate()
						.getPredicateSymbol(), l.getPredicate().getArity());

		/*
		 * 2. SELECTION_Fi(Ri) Fi: -patternTerms- (a) If position k of Si has a
		 * constant a, then Fi has the term $k = a (b) If positions k and l of
		 * Si both contain the same variable, then Fi has the term $k = $l Vi:
		 * -int[]- Set of components including, for each valriable X that
		 * appears among the arguments of Si, exactly one component where X
		 * appears.
		 */
		int arity = l.getPredicate().getArity();
		List<ITerm> terms = l.getAtom().getTuple().getTerms();
		List<ITerm> selectionPattern = new ArrayList<ITerm>();
		List<IVariable> projectionVariables = new LinkedList<IVariable>();
		int[] selectionIndex = new int[arity];
		int[] projectionIndex = new int[arity];
		int[] joinIndex = new int[arity];

		boolean noFi = getIndexes(terms, selectionPattern, selectionIndex,
				projectionIndex, projectionVariables, joinIndex, variables, l,
				head, leaf);

		if (!noFi) {
			// 2.2. SELECTION
			ISelection s = ModelFactory.FACTORY.createSelection(Factory.BASIC
					.createTuple(selectionPattern));
			s.addComponent(leaf);
			// 3. PROJECTION_Vi(SELECTION_Fi(Ri))
			// 3.1 Vi - already done in 2.1
			// 3.2 PROJECTION
			IProjection p = ModelFactory.FACTORY
					.createProjection(projectionIndex);
			p.addAllVariables(projectionVariables);
			p.addComponent(s);
			result = p;
		} else {
			/*
			 * As a special case, if Si is such that there are no terms in Fi,
			 * e.g., Si = p(X,Y), then take Fi to be the identically true
			 * condition, so Qi = Ri
			 */
			result = leaf;
		}

		return result;
	}

	/**
	 * Evaluate a built-in (e.g. ?X = 'a', ?X=?Y);
	 * 
	 * @param b
	 *            A built-in atom
	 * @param head
	 *            Head of the rule that is being analyzed
	 * @param vars
	 *            Set of variables found in this rule
	 * @param builtIns
	 *            List of built-ins in the head
	 * @return The algebraic expression for this literal
	 */
	private ITree evalBuiltIn(IBuiltInAtom b, ITree head,
			Map<IVariable, List<ILiteral>> vars, List<IBuiltInAtom> builtIns,
			List<IBuiltInAtom> builtInsV) {

		/*
		 * B. Variable X not among ordinary subgoal (eg. 'U = a' or 'U = W')
		 * 
		 * For each variable X not found among the ordinary subgoals, compute an
		 * expression Dx that produces a unary relation containing all the
		 * values that X could possibly have in an assignment that satisfies all
		 * the subgoals of rule r. (a) If Y = a is a subgoal, then let Dx be the
		 * constant expression {a} (b) If Y appears as the jth argument of
		 * ordinary subgoal Si, let Dx be PROJECTIONj(Ri)
		 */
		ITerm t1 = b.getTuple().getTerm(0);
		ITerm t2 = b.getTuple().getTerm(1);

		if (t1 instanceof IVariable && t2 instanceof IVariable) {
			// t1 & t2 are variables
			if (!t1.equals(t2)) {
				if (b instanceof EqualBuiltin) {
					builtIns.add(b);

					if (vars.containsKey(t1) && vars.containsKey(t2)) {
						// builtInsV.add(b); // ?X = ?Y or ?X != ?Y needed for
						// global selection
						return head;
					}

					// ?X = ?Y --> Dx = PROJECTIONj(Ri) (Si(...,?Y, ...))
					if (vars.containsKey(t2)) {
						ITerm tt = t1;
						t1 = t2; // t1 = ?X
						t2 = tt; // t2 = ?Y
					}
					ILiteral lit = vars.get(t1).get(0);
					List<IVariable> bVars = new LinkedList<IVariable>();
					List<ITerm> literalTerms = lit.getTuple().getTerms();
					int[] pInds = new int[literalTerms.size()];
					int j = 0, k = 0;

					for (ITerm t : literalTerms) {
						if (t.equals(t1)) {
							bVars.add((IVariable) t2);
							pInds[j++] = k++;
						} else {
							bVars.add(createVariable());
							pInds[j++] = -1;
						}
					}
					IRule bLeaf = ModelFactory.FACTORY.createRule(lit
							.getPredicate().getPredicateSymbol(), lit
							.getPredicate().getArity());
					bLeaf.addAllVariables(bVars);

					IProjection elprojection = ModelFactory.FACTORY
							.createProjection(pInds);
					elprojection.addVariable((IVariable) t2);

					elprojection.addComponent(bLeaf);
					return elprojection;
				} else if (b instanceof UnEqualBuiltin) {
					// ?X != ?Y --> needed for the global selection
					builtIns.add(b);
					return head; // do not include any term
				}
			} else if (b instanceof EqualBuiltin) {
				return head; // ?X = ?X --> true -- do not include any term
			} else
				return null; // ?X != ?X --> false -- invalidate the rule
		} else if (t1 instanceof IStringTerm && t2 instanceof IStringTerm) {
			// t1 & t2 are constants (strings)
			if (!t1.equals(t2)) {
				if (b instanceof EqualBuiltin)
					return null; // 'b' = 'c' --> false -- invalidate the
				// rule
				else if (b instanceof UnEqualBuiltin)
					return head; // 'b' != 'c' --> true -- do not include any
				// term
			} else {
				if (b instanceof EqualBuiltin)
					return head; // 'a' = 'a' --> true -- do not include any
				// term
				else if (b instanceof UnEqualBuiltin)
					return null; // 'a' !='a' --> false -- invalidate the
				// rule
			}
		} else {
			// one is variable and the other string (e.g. ?X='a')
			if (t1 instanceof StringTerm) {
				ITerm tt = t1;
				t1 = t2; // t1 = ?X
				t2 = tt; // t2 = 'a'
			}
			if (vars.containsKey(t1)) {
				builtInsV.add(b); // ?X = 'a' or ?X != 'a' needed for global
				// selection
				return head; // The variable is within ordinary predicates --
				// do not include any term
			}
			if (b instanceof EqualBuiltin) {
				// Create the unary relation
				IRule ur = ModelFactory.FACTORY
						.createUnaryRule(((StringTerm) t2).getValue());
				ur.addVariable((IVariable) t1);
				return ur;
			} else if (b instanceof UnEqualBuiltin) {
				/*
				 * TODO: Checking safeness procedure and do a case like p(?X,?Y) :-
				 * r(?Z,?Y) and ?X != 'a'
				 * 
				 * Note: a case such as p(?X,?Y) :- r(?Z,?Y) and ?X != 'a' is
				 * not currently handled, as the rule is not safe.
				 */
				return head;
			}
		}
		return head;
	}

	private boolean getIndexes(List<ITerm> terms, List<ITerm> selectionPattern,
			int[] selectionIndex, int[] projectionIndex,
			List<IVariable> projectionVariables, int[] joinIndex,
			Map<IVariable, List<ILiteral>> variables, ILiteral l, ITree head,
			org.deri.iris.api.evaluation.seminaive.model.IRule leaf) {

		initSelectionIndex(selectionIndex);
		initIndex(projectionIndex);
		initIndex(joinIndex);
		int i = 0;
		boolean noFi = true;
		for (ITerm t : terms) {
			if (t instanceof org.deri.iris.terms.ConstructedTerm) {
				/*
				 * 
				 * TODO recursivity - Not allowed in datalog expressions
				 */
				projectionIndex[i] = -1;
				selectionPattern.add(null);
				selectionIndex[i] = -1;
				// leaf.addVariable(createString(leaf.getVariables()));
				leaf.addVariable(createVariable());
			} else if (t instanceof org.deri.iris.terms.StringTerm) {
				projectionIndex[i] = -1;
				selectionPattern.add(t);
				selectionIndex[i] = 0;
				// leaf.addVariable(createString(leaf.getVariables()));
				leaf.addVariable(createVariable());
				noFi = false;
			} else if (t instanceof org.deri.iris.terms.Variable) {
				IVariable v = (IVariable) t;
				leaf.addVariable(v);
				projectionIndex[i] = 0;
				projectionVariables.add(v);
				selectionPattern.add(null);
				if (variables.containsKey(v)) {
					List<ILiteral> literalList = variables.get(v);
					for (ILiteral literal : literalList) {
						if (literal.equals(l)) {
							// Positions k and l of Si both contain the same
							// variable,
							// then Fi has the term $k = $l.
							noFi = false;
							selectionIndex[i] = l.getAtom().getTuple()
									.getTerms().indexOf(v);
							// TODO. Check whether the variable appeared before
							// in this literal
							// and, if so, add condition $k = $l in the
							// "patternTerms way"
							// How can it be expressed? DARKO IS STUDYING IT
						} else {
							// A Join between these two literals should be
							// created
							joinIndex[i] = literal.getAtom().getTuple()
									.getTerms().indexOf(v);
						}
					}
				} else {
					// Store the literal referencing this variable
					List<ILiteral> literalList = new LinkedList<ILiteral>();
					literalList.add(l);
					variables.put(v, literalList);
				}
			}
			i++;
		}
		return noFi;
	}

	/**
	 * Possibilities: ?X = ?Y ?X != ?Y ?X = 'a' ?X != 'a'
	 */
	private void getSelectionIndexes(List<IBuiltInAtom> builtIns,
			List<ITerm> selectionPattern, int[] selectionIndex, ITree globalJoin) {

		int indexPoint = 1;
		int negativeIndexPoint = -1;
		List<Integer> selIndex = new ArrayList<Integer>(selectionIndex.length);
		/*
		 * for (int j = 0; j < selectionIndex.length; j++) selIndex.add(0);
		 */

		for (IBuiltInAtom b : builtIns) {
			ITerm t1 = b.getTuple().getTerm(0);
			ITerm t2 = b.getTuple().getTerm(1);

			if (t1 instanceof IVariable && t2 instanceof IVariable) {
				if (b instanceof EqualBuiltin) { // ?X = ?Y --> indexes of X
					// and Y have the same
					// number
					/*
					 * int in0 = globalJoin.getVariables().indexOf( ((IVariable)
					 * t1).getValue()); int in1 =
					 * globalJoin.getVariables().indexOf( ((IVariable)
					 * t2).getValue());
					 * 
					 * if(selIndex.get(in0)!=0 || selIndex.get(in1)!=0){ int
					 * index = selIndex.get(in0); if(index == 0)index =
					 * selIndex.get(in1); selIndex.set(in0, index);
					 * selIndex.set(in1, index); }else{ selIndex.set(in0,
					 * indexPoint); selIndex.set(in1, indexPoint); indexPoint++; }
					 */

					selectionIndex[globalJoin.getVariables().indexOf(
							((IVariable) t1).getValue())] = indexPoint;
					selectionIndex[globalJoin.getVariables().indexOf(
							((IVariable) t2).getValue())] = indexPoint;
					indexPoint++;
				} else if (b instanceof UnEqualBuiltin) { // ?X != ?Y
					/*
					 * int in0 = globalJoin.getVariables().indexOf( ((IVariable)
					 * t1).getValue()); int in1 =
					 * globalJoin.getVariables().indexOf( ((IVariable)
					 * t2).getValue());
					 * 
					 * if(selIndex.get(in0)!=0 || selIndex.get(in1)!=0){ int
					 * index = selIndex.get(in0); if(index == 0)index =
					 * selIndex.get(in1); selIndex.set(in0, index);
					 * selIndex.set(in1, index); }else{ selIndex.set(in0,
					 * negativeIndexPoint); selIndex.set(in1,
					 * negativeIndexPoint); negativeIndexPoint--; }
					 */
					selectionIndex[globalJoin.getVariables().indexOf(
							((IVariable) t1).getValue())] = negativeIndexPoint;
					selectionIndex[globalJoin.getVariables().indexOf(
							((IVariable) t2).getValue())] = negativeIndexPoint;
					negativeIndexPoint--;
				}
			} else {
				if (t1 instanceof IStringTerm) {
					ITerm tt = t1;
					t1 = t2; // t1 = ?X
					t2 = tt; // t2 = 'a'
				}
				try {
					String v = null;
					if (b instanceof EqualBuiltin) { // ?X = 'a'
						for (int i = 0; i < globalJoin.getVariables().size(); i++) {
							v = globalJoin.getVariables().get(i);
							if (v.equals(t1.getValue())) {
								selectionPattern.remove(i);
								selectionPattern.add(i, t2);
							}
						}
					} else if (b instanceof UnEqualBuiltin) { // ?X != 'a'
						for (int i = 0; i < globalJoin.getVariables().size(); i++) {
							v = globalJoin.getVariables().get(i);
							if (v.equals(t1.getValue())) {
								selectionPattern.remove(i);
								selectionPattern
										.add(i, new NonEqualityTerm(t2));
							}
						}
					}
				} catch (Exception e) {
					System.out.println("Unsafe rules are not supported! "
							+ "Rule's body transformation to algebra relation"
							+ " is inncorrect: " + e.getMessage());
				}
			}
		}
		/*
		 * for (int j = 0; j < selectionIndex.length; j++) selectionIndex[j] =
		 * selIndex.get(j);
		 */
	}

	private int[] getJoinIndexes(final ITree t0, final ITree t1) {
		int varSize = Math.max(t0.getArity(), t1.getArity());
		int minSize = Math.min(t0.getArity(), t1.getArity());
		int[] joinInds = new int[varSize];
		for (int i = 0; i < varSize; i++) {
			joinInds[i] = -1;
		}
		String v = null;
		for (int i = 0; i < minSize; i++) {
			v = t1.getVariables().get(i);
			if (t0.getVariables().contains(v)) {
				joinInds[t0.getVariables().indexOf(v)] = i;
			}
		}
		return joinInds;
	}

	/**
	 * @param t0
	 *            Join tree
	 * @param t1
	 *            Head tree
	 * @return Projection indexes
	 */
	private int[] getProjectionIndexes(final ITree t0, final ITree t1) {
		int minSize = Math.min(t0.getArity(), t1.getArity());
		int arity = t0.getArity() + t1.getArity();
		int[] projectInds = new int[arity];

		for (int i = 0; i < arity; i++) {
			projectInds[i] = -1;
		}
		String v = null;
		for (int i = 0; i < minSize; i++) {
			v = t1.getVariables().get(i);
			if (t0.getVariables().contains(v)) {
				projectInds[t0.getVariables().indexOf(v)] = i;
			}
		}
		return projectInds;
	}

	private void initIndex(int[] i) {
		for (int j = 0; j < i.length; j++)
			i[j] = -1;
	}

	private void initSelectionIndex(int[] i) {
		for (int j = 0; j < i.length; j++)
			i[j] = j;
	}

	private IVariable createVariable() {
		IVariable v = Factory.TERM.createVariable(VAR_PREFIX + VAR_COUNTER++);
		return v;
	}
}
