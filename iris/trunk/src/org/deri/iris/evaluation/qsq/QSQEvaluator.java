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
package org.deri.iris.evaluation.qsq;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.EVALUATION;
import static org.deri.iris.factory.Factory.RELATION_OPERATION;
import static org.deri.iris.factory.Factory.TUPLE_OPERATION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.IEvaluator;
import org.deri.iris.api.evaluation.IResultSet;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.operations.relation.IProjection;
import org.deri.iris.api.operations.tuple.IUnification;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.common.AdornedProgram;
import org.deri.iris.evaluation.common.Adornment;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;
import org.deri.iris.exception.DataModelException;
import org.deri.iris.operations.relations.JoinCondition;
import org.deri.iris.operations.tuple.Multiequation;
import org.deri.iris.operations.tuple.Unification.UnificationResult;
import org.deri.iris.storage.Relation;

/**
 * 
 * <b>NOTE: At the moment this class only works with rules with one literal in
 * the head.</b><br/><br/>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 14.09.2006 10:20:59
 */
public class QSQEvaluator implements IEvaluator {

	private IProgram edb = null;

	/**
	 * Adorned program
	 */
	private AdornedProgram adPrg = null;

	private Set<QSQRule> qsqRules = null;

	private IJoin joinOperator = null;

	private IProjection projectionOperator = null;

	private Map<IPredicate, IRelation> inputs = null;

	private Map<IPredicate, IRelation> outputs = null;

	private IResultSet results = null;

	/**
	 * continue the evaluation while contEval==true
	 */
	private boolean contEval = false;

	public QSQEvaluator(IProgram edb) {
		if (edb == null) {
			throw new IllegalArgumentException(
					"Input parameter must not be null");
		}
		this.edb = edb;
		this.results = EVALUATION.createResultSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.deri.iris.api.evaluation.IEvaluator#evaluate()
	 */
	public boolean evaluate() throws DataModelException {
		QSQRule r = null;
		AdornedPredicate ap_inner = null;
		IQuery q = null;
		Adornment[] ads = null;

		Iterator<IQuery> qi = this.edb.queryIterator();
		while (qi.hasNext()) {
			q = qi.next();

			this.adPrg = new AdornedProgram(this.edb.getRules(), q);
			QSQTemplate qsqTemplate = new QSQTemplate(adPrg);
			this.qsqRules = qsqTemplate.getQSQTemplate();
			ads = getAdornment(q);
			setEvaluation(ads, q);
			/**
			 * Repeat untill no new tuples are added to any global variable.
			 * 
			 * The evaluation is looping until no new tuples are added to any of
			 * the global variables (inputs and outputs). However for each loop
			 * we need to consider only new tuples in inputs global variables
			 * (ones that have not been considered yet).
			 */
			while (contEval) {
				Iterator<QSQRule> ri = this.qsqRules.iterator();
				contEval = false;
				while (ri.hasNext()) {
					r = ri.next();
					ap_inner = isAdorned(r.getAdornedRule().getHeadLiteral(0)
							.getPredicate());
					if (Arrays.equals(ads, ap_inner.getAdornment()))
						this.qsqRecursive(r);
				}
				emptyInputs();
			}
		}
		setResult();
		printResult();

		return true;
	}

	/**
	 * Create input and output global relations for each idb predicate.
	 * Instantiate the program (sets starting supplementary relations of
	 * relevant rules as well as global input relations).
	 * 
	 * Note: arity of the input relations is equal to number of bound variables.
	 * Arity of the output relations is equal to the arity of head literal of
	 * the entire realtions.
	 */
	private void setEvaluation(Adornment[] ads, IQuery q) {
		this.inputs = new HashMap<IPredicate, IRelation>();
		this.outputs = new HashMap<IPredicate, IRelation>();

		Set rules = this.qsqRules;
		Iterator<QSQRule> i = rules.iterator();
		QSQRule r = null;
		ILiteral h = null;
		AdornedPredicate ap = null;
		ITuple t0 = null;

		while (i.hasNext()) {
			r = i.next();
			h = r.getAdornedRule().getHeadLiteral(0);
			ap = isAdorned(h.getPredicate());

			if (ap != null) {
				if (!this.inputs.containsKey(ap)) {
					this.inputs.put(ap, new Relation(r.getInputArity(ap)));
				}
				if (!this.outputs.containsKey(ap)) {
					this.outputs
							.put(ap, new Relation(r.getAdornedRule()
									.getHeadLiteral(0).getAtom().getTuple()
									.getArity()));
				}
				// Remove from t all tuples that don't unify,
				// apply: 1...{first part of step (A)}
				if (Arrays.equals(ads, ap.getAdornment())) {
					t0 = getInitTuple(q.getQueryLiteral(0).getTuple());
					if (!this.inputs.get(ap).contains(t0)) {
						List l = r.getSup_0().getVariables();
						ITuple t1 = BASIC.createTuple(l);
						ITuple t2 = unify(t0, t1);
						if (t2 != null) {
							this.inputs.get(ap).add(t0);
							r.getSupRels().get(0).add(t2);
							contEval = true;
						}
					}
				}
			}
		}
	}

	/**
	 * It takes a subquery (R^a, t), which is a pair of an adorned rule and a
	 * set of tuples t that have not been considered with this rule yet, and
	 * evaluate it.
	 * 
	 * @param r
	 *            adorned rule to be evaluated.
	 */
	private void qsqRecursive(QSQRule r) {
		Iterator<QSQRule> i = null;
		List<IVariable> vars = null;
		// List<IVariable> litVars = null;
		List litVars = null;
		IRelation rel = null;
		AdornedPredicate ap = null;
		AdornedPredicate ap_inner = null;
		QSQRule rule = null;

		if (r.getSupRels().get(0).size() > 0) {
			// Instantiate a new rule (copy of the rule r)
			// and set the input (sup_0 relation of that rule)
			rule = new QSQRule(r.getAdornedRule(), r.cloneSupRels());
			rule.getSup_0().addAll(setSup_0(r));
			contEval = true;

			for (ILiteral lit : rule.getAdornedRule().getBodyLiterals()) {
				ap = isAdorned(lit.getPredicate());

				// lit has edb predicate R', apply: 3(a)...{(B.i)}
				if (ap == null) {
					this.joinOperator = RELATION_OPERATION.createJoinOperator(
							rule.getSup_0(), this.edb.getFacts(lit
									.getPredicate()), this.getJoinIndexes(rule
									.getSup_0().getVariables(), lit),
							JoinCondition.EQUALS, this.getProjectIndexes0(rule
									.getSup_0().getVariables(), lit, rule
									.getSup_1(rule.getSup_0()).getVariables()));

					rel = this.joinOperator.join();
					if (rel.size() > 0)
						rule.getSup_1(rule.getSup_0()).addAll(rel);
				} else {
					// lit has idb predicate R', apply: 3(b)...{(B.ii)}
					// apply: 3(b)(i)
					litVars = lit.getAtom().getTuple().getTerms();
					vars = this.getBoundVars(ap, litVars);
					this.projectionOperator = RELATION_OPERATION
							.createProjectionOperator(rule.getSup_0(), this
									.getProjectIndexes1(rule.getSup_0()
											.getVariables(), vars));

					// Add new inputs, apply: 3(b)(ii)...{(B.ii.b)}
					rel = projectionOperator.project();
					if (rel.size() > 0) {
						// If any, add new tuples
						// (ones that have not been considered yet):
						if (setInput(ap, rel)) {
							contEval = true;
							i = this.qsqRules.iterator();
							QSQRule r_inner = null;
							while (i.hasNext()) {
								r_inner = i.next();
								ap_inner = isAdorned(r_inner.getAdornedRule()
										.getHeadLiteral(0).getPredicate());
								if (ap.equals(ap_inner))
									this.qsqRecursive(r_inner);
							}
						}
						// apply: 3(b)(iv)...{(B.ii.b) & (C)}
						this.joinOperator = RELATION_OPERATION
								.createJoinOperator(rule.getSup_0(),
										this.outputs.get(ap), this
												.getJoinIndexes(rule.getSup_0()
														.getVariables(), lit),
										JoinCondition.EQUALS,
										this.getProjectIndexes0(rule.getSup_0()
												.getVariables(), lit, rule
												.getSup_1(rule.getSup_0())
												.getVariables()));

						rule.getSup_1(rule.getSup_0()).addAll(
								this.joinOperator.join());
					}
				}
				// Add the tuples produced from the last sup (sup_n) into
				// the global variable ans_R^a, apply: 4...{(D)}
				if (!rule.hasNext(rule.getSup_1(rule.getSup_0()))
						&& rule.getSup_1(rule.getSup_0()).size() > 0) {
					ap = (AdornedPredicate) rule.getAdornedRule()
							.getHeadLiteral(0).getPredicate();
					this.outputs.get(ap).addAll(rule.getSup_1(rule.getSup_0()));
					contEval = true;
				}
				if (rule.getSup_1(rule.getSup_0()).size() == 0)
					break;
				rule.getSup_0().setHandled();
			}
		}
	}

	/**
	 * Checks whether a predicate p is an adorned one
	 * 
	 * @param p
	 *            Predicate to be checked.
	 * @return Adorned predicate of p (if p is an adorned predicate), otherwise
	 *         null.
	 */
	private AdornedPredicate isAdorned(IPredicate p) {
		if (p instanceof AdornedPredicate) {
			return (AdornedPredicate) p;
		} else
			return null;
	}

	/**
	 * @param vars
	 *            list of variables of the supplementary relation
	 * @param l
	 *            literal that needs to be joined with the corresponding
	 *            supplementary relation
	 * @return indexes which the join operation will be performed on
	 */
	private int[] getJoinIndexes(List<IVariable> vars, ILiteral l) {
		int maxSize = Math.max(vars.size(), l.getAtom().getTuple().getArity());
		int[] indexes = new int[maxSize];
		IVariable v0 = null;
		IVariable v1 = null;
		List<ITerm> vars1 = l.getAtom().getTuple().getTerms();

		for (int i = 0; i < maxSize; i++) {
			indexes[i] = -1;
		}
		for (int i = 0; i < vars1.size(); i++) {
			v1 = (IVariable) vars1.get(i);
			if (v1 != null)
				for (int j = 0; j < vars.size(); j++) {
					v0 = vars.get(j);
					if (v0.equals(v1) && v0 != null) {
						indexes[j] = i;
						break;
					}
				}
		}
		return indexes;
	}

	/**
	 * Case: B(i), page: 322. Defines an index array which the projection
	 * operation will be based on.
	 * 
	 * @param vars0
	 *            Variables of a supplementary relation which has an index j-1
	 * @param l
	 *            Literal that follows a supplementary relation which has an
	 *            index j-1
	 * @param vars1
	 *            Variables of a supplementary relation which has an index j
	 * @return Projection indexes
	 */
	private int[] getProjectIndexes0(List<IVariable> vars0, ILiteral l,
			List<IVariable> vars1) {

		int[] indexes = new int[vars0.size()
				+ l.getAtom().getPredicate().getArity()];
		IVariable v0 = null;
		IVariable v1 = null;
		List<ITerm> terms = l.getAtom().getTuple().getTerms();
		ITerm t = null;
		boolean found = false;
		int pos = -1;

		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = -1;
		}
		for (int i = 0; i < vars1.size(); i++) {
			v1 = vars1.get(i);
			found = false;
			for (int j = 0; j < vars0.size(); j++) {
				v0 = vars0.get(j);
				if (v0.equals(v1)) {
					indexes[j] = ++pos;
					found = true;
					break;
				}
			}
			if (!found)
				for (int j = 0; j < terms.size(); j++) {
					t = terms.get(j);
					if (t instanceof IVariable) {
						v0 = (IVariable) t;
						if (v0.equals(v1)) {
							indexes[vars0.size() + j] = ++pos;
							break;
						}
					}
				}
		}
		return indexes;
	}

	/**
	 * Case: B(ii)b, page: 322.
	 * 
	 * @param vars0
	 *            Variables of a supplementary relation which has an index j-1
	 * @param vars1
	 *            Variables of literal that follows a supplementary relation
	 *            which has an index j
	 * @return Projection indexes
	 */
	private int[] getProjectIndexes1(List<IVariable> vars0,
			List<IVariable> vars1) {

		int[] indexes = new int[Math.max(vars0.size(), vars1.size())];
		IVariable v0 = null;
		IVariable v1 = null;
		int pos = -1;

		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = -1;
		}
		for (int i = 0; i < vars1.size(); i++) {
			v1 = vars1.get(i);
			for (int j = 0; j < vars0.size(); j++) {
				v0 = vars0.get(j);
				if (v0.equals(v1)) {
					indexes[j] = ++pos;
					break;
				}
			}
		}
		return indexes;
	}

	/**
	 * @param ap
	 *            adorned predicate
	 * @param v
	 *            list of variables
	 * @return list of bound variables
	 */
	private List<IVariable> getBoundVars(AdornedPredicate ap, List<IVariable> v) {
		List<IVariable> vars = new ArrayList<IVariable>();
		Adornment[] ads = ap.getAdornment();
		for (int i = 0; i < ads.length; i++) {
			if (ads[i] == Adornment.BOUND)
				vars.add(v.get(i));
		}
		return vars;
	}

	/**
	 * @param q
	 *            query to be processed
	 * @return adornment defined by query q
	 */
	private Adornment[] getAdornment(IQuery q) {
		List<ITerm> terms = q.getQueryLiteral(0).getTuple().getTerms();
		Adornment[] ads = new Adornment[terms.size()];
		int i = 0;

		for (ITerm t : terms) {
			if (t instanceof IStringTerm)
				ads[i++] = Adornment.BOUND;
			else
				ads[i++] = Adornment.FREE;
		}
		return ads;
	}

	/**
	 * @param t
	 *            tuple provided by a query
	 * @return Returns initial tuple (created out of bound terms of the query)
	 *         for beginning of a rule evaluation or null (If the query doesn�t
	 *         contain any ground term).
	 */
	private ITuple getInitTuple(ITuple t) {
		List<ITerm> terms = new ArrayList<ITerm>();
		List<ITerm> tList = t.getTerms();

		for (ITerm term : tList) {
			if (term instanceof IStringTerm)
				terms.add(term);
		}
		if (terms.size() > 0)
			return BASIC.createTuple(terms);
		else
			return null;
	}

	/**
	 * Empties all input global relations. This operation is needed in order to
	 * terminate the overall evaluation process. Namely the evaluation is
	 * looping until no new tuples are added to any of the global variables
	 * (inputs and outputs). After each loop we empty input global variables
	 * (relation), so that we can check whether new tuples will be added in the
	 * next loop. The evaluation terminates when no new tuples are added to any
	 * of global variables (relations).
	 */
	private void emptyInputs() {
		Iterator<QSQRule> ri = this.qsqRules.iterator();
		QSQRule r = null;
		AdornedPredicate ap = null;

		while (ri.hasNext()) {
			r = ri.next();
			ap = (AdornedPredicate) r.getAdornedRule().getHeadLiteral(0)
					.getPredicate();
			if (this.inputs.get(ap).size() > 0)
				this.inputs.get(ap).clear();
		}
	}

	/**
	 * Set sup_0 := T of the rule r. (T - set of tuples that have not been
	 * considered with the entire rule r yet, apply: 2...{(A)}
	 * 
	 * @param r
	 *            rule to be evaluated
	 */
	private IRelation setSup_0(QSQRule r) {
		IRelation rel = null;
		if (r.getSupRels().get(0).size() > 0) {
			rel = new Relation(((ITuple) r.getSupRels().get(0).first())
					.getArity());
			rel.addAll(r.getSupRels().get(0));

			// Clear the rule input in order to
			// always consider only new tuples
			r.getSupRels().get(0).clear();
		}
		return rel;
	}

	/**
	 * Sets input (sup_0 relation) for each rule r with an adorned predicate ap.
	 * Only new tuples from relatin rel are added.
	 * 
	 * @param ap
	 *            adorned predicate
	 * @param rel
	 *            relation
	 * @return true if new tuples are added, otherwise false
	 */
	private boolean setInput(AdornedPredicate ap, IRelation rel) {
		QSQRule r = null;
		AdornedPredicate ap_inner = null;
		IRelation copyRel = new Relation(((ITuple) rel.first()).getArity());
		boolean added = false;

		copyRel.addAll(this.inputs.get(ap));
		if (this.inputs.get(ap).addAll(rel)) {
			Iterator<QSQRule> i = this.qsqRules.iterator();
			while (i.hasNext()) {
				r = i.next();
				ap_inner = isAdorned(r.getAdornedRule().getHeadLiteral(0)
						.getPredicate());
				if (ap.equals(ap_inner)) {
					// Only new tuples are being added
					// (ones that have not been considered yet):
					r.getSupRels().get(0).clear();
					r.getSupRels().get(0).addAll(this.inputs.get(ap));
					r.getSupRels().get(0).removeAll(copyRel);
					added = true;
				}
			}
		}
		return added;
	}

	/**
	 * Sets results (map) with answers for each query of the program.
	 */
	private void setResult() {
		Iterator<IPredicate> pi = this.outputs.keySet().iterator();
		AdornedPredicate ap = null;
		Adornment[] ads = null;
		IQuery q = null;

		Iterator<IQuery> qi = this.edb.queryIterator();
		while (qi.hasNext()) {
			q = qi.next();
			ads = getAdornment(q);
			while (pi.hasNext()) {
				ap = (AdornedPredicate) pi.next();
				if (Arrays.equals(ads, ap.getAdornment())) {
					this.results.getResults().put(
							q.getQueryLiteral(0).getPredicate(),
							this.outputs.get(ap));
				}
			}
		}
	}

	/**
	 * Prints output of the evaluation (for all query of the program).
	 */
	private void printResult() {
		StringBuilder buffer = new StringBuilder();
		Iterator<ITuple> i = null;
		Iterator<IPredicate> ri = null;
		IPredicate p = null;

		ri = this.results.getResults().keySet().iterator();
		while (ri.hasNext()) {
			p = ri.next();
			buffer.append("\n");
			buffer.append("The output for a query with predicate: "
					+ p.toString());
			i = this.results.getResults().get(p).iterator();
			while (i.hasNext()) {
				buffer.append("\n");
				buffer.append(i.next());
			}
		}
		buffer.append("\n");
		buffer.append("*** END of query ***");
		System.out.println(buffer);
	}

	/**
	 * @param t0
	 *            input tuple to be unified with t1
	 * @param t1
	 *            tuple of supplementary relation sr to be unified with t0
	 * @return Ground tuple if t0 is unifyable with t1
	 */
	private ITuple unify(ITuple t0, ITuple t1) {
		IUnification unifier = TUPLE_OPERATION
				.createUnificationOperator(t0, t1);
		UnificationResult res = unifier.unify();
		ITuple tuple = null;
		ITerm t = null;

		if (res != null) {
			tuple = BASIC.createTuple(t1.getArity());
			for (int i = 0; i < t1.getTerms().size(); i++) {
				tuple.setTerm(i, substitute(t1.getTerm(i), res.getTails()));
			}
		}
		return tuple;
	}

	/**
	 * @param t
	 *            Term to be grounded with unification result l
	 * @param l
	 *            Unification result
	 * @return Ground term
	 */
	private ITerm substitute(final ITerm t, final List<Multiequation> l) {
		IConstructedTerm ct = null;
		Multiequation me = null;
		ITerm t0 = t;
		ITerm t1 = null;

		while (!t0.isGround()) {
			if (t0 instanceof IConstructedTerm) {
				ct = (IConstructedTerm) t0;
				for (int i = 0; i < ct.getParameters().size(); i++) {
					t1 = ct.getParameter(i);
					ITerm t3 = substitute(t1, l);
					ct.getValue().set(i, t3);
				}
				t0 = ct;
			} else {
				for (int j = 0; j < l.size(); j++) {
					me = l.get(j);
					if (me.getS().contains(t0)) {
						t0 = me.getM().iterator().next();
						break;
					}
				}
			}
		}
		return t0;
	}

	public IResultSet getResultSet() {
		// TODO Auto-generated method stub
		return null;
	}
}
