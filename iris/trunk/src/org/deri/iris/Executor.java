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
package org.deri.iris;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.IExecutor;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.IEvaluator;
import org.deri.iris.api.evaluation.seminaive.IEvaluationProcedure;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.evaluation.common.AdornedProgram;
import org.deri.iris.evaluation.magic.MagicSetImpl;
import org.deri.iris.evaluation.seminaive.InMemoryProcedure;
import org.deri.iris.evaluation.seminaive.NaiveEvaluation;
import org.deri.iris.evaluation.seminaive.Rule2Relation;

/**
 * <p>
 * Executes a programm.
 * </p>
 * <p>
 * $Id: Executor.java,v 1.2 2006-12-05 13:47:17 richardpoettler Exp $
 * </p>
 * 
 * @author Richard Pöttler
 * @author Darko Anicic, DERI Innsbruck
 * @version $Revision: 1.2 $
 */
public class Executor implements IExecutor {

	/** The program to execute. */
	private IProgram prog;

	/** The evaluator. */
	private IEvaluator evaluator;

	/** The evaluation mehtod. */
	private EvaluationMethod method;

	/**
	 * Creates a new evaluator with a given programm and evaluator.
	 * 
	 * @param p
	 *            the program
	 * @param e
	 *            the evaluator
	 * @throws NullPointerException
	 *             if the program or the evaluator is {@code null}
	 */
	Executor(final IProgram p, final IEvaluator e) {
		if ((p == null) || (e == null)) {
			throw new NullPointerException(
					"The program and evaluator must not be null");
		}
		this.prog = p;
		this.evaluator = e;
	}

	public void setEvaluationMethod(EvaluationMethod method) {
		if (method == null) {
			throw new NullPointerException("The method must not be null");
		}
		this.method = method;
	}

	public Set<ITuple> computeSubstitution(final IQuery q) {
		// TODO: choice between the procedures
		if (q == null) {
			throw new NullPointerException("The query must not be null");
		}
		if (q.getQueryLenght() != 1) {
			throw new IllegalArgumentException(
					"The length of the query literals must be 1");
		}
		if (!prog.isStratified()) {
			throw new IllegalStateException("The program is not stratified.");
		}
		
		// applying the magic sets
		final MagicSetImpl ms = new MagicSetImpl(new AdornedProgram(prog
				.getRules(), q));
		
		// tests the stratum of the newly constructed program and sets it back
		// to the original one, if needed
		IProgram p = ms.createProgram(prog);
		if (!p.isStratified()) {
			p = prog;
		}
		
		// translating the query and the rules to the relational algebra model
		final Rule2Relation rr = new Rule2Relation();
		final Map<IPredicate, ITree> ruleT = rr.evalRule(p.getRules());
		final Map<IPredicate, ITree> queryT = rr.evalQueries(p.getQueries()
				.iterator());
		
		// run the evalutaion
		final IEvaluationProcedure proc = new InMemoryProcedure(null, p);
		final IEvaluator e = new NaiveEvaluation(proc, p, ruleT, queryT);
		e.evaluate();
		
		// adds the computed substitutions to the program
		final Map<IPredicate, IRelation> rs = e.getResultSet().getResults();
		for (final Map.Entry<IPredicate, IRelation> me : rs.entrySet()) {
			p.addFacts(me.getKey(), me.getValue());
		}
		return p.getFacts(q.getQueryLiteral(0).getPredicate());
	}

	public Map<IQuery, Set<ITuple>> computeSubstitutions() {
		final Map<IQuery, Set<ITuple>> res = new HashMap<IQuery, Set<ITuple>>();
		for (final IQuery q : prog.getQueries()) {
			res.put(q, computeSubstitution(q));
		}
		return res;
	}

	public boolean execute() {
		// TODO Auto-generated method stub
		return false;
	}
}
